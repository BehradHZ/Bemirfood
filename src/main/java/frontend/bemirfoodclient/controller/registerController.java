package frontend.bemirfoodclient.controller;

import HttpClientHandler.HttpClientHandler;
import HttpClientHandler.HttpResponseData;
import frontend.bemirfoodclient.BemirfoodApplication;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.function.UnaryOperator;


public class registerController {
    @FXML
    public BorderPane mainBorderPane;
    @FXML
    public ImageView firstPageImage;
    @FXML
    public TextField fullNameTextField;
    @FXML
    public TextField phoneNumberTextField;
    @FXML
    public TextField emailTextField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField visiblePasswordField;
    @FXML
    public Button makePasswordVisible;
    @FXML
    public Button makePasswordHidden;
    @FXML
    public ToggleGroup roleToggleGroup;
    @FXML
    public TextField bankNameTextField;
    @FXML
    public TextField accountNumberTextField;
    @FXML
    public Button registerButton;

    public String role;

    private BooleanProperty isRegistering = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        firstPageImage.fitHeightProperty().bind(mainBorderPane.heightProperty());

        UnaryOperator<TextFormatter.Change> phoneNumberFilter = change -> {
            String newText = change.getControlNewText();
            try {
                if (newText.matches("\\d*") && newText.length() <= 11 && (newText.charAt(0) == '0' && newText.charAt(1) == '9')) {
                    return change;
                } else {
                    return null;
                }
            } catch (StringIndexOutOfBoundsException siobe) {
                return change;
            }
        };
        TextFormatter<String> phoneNumberTextFormatter = new TextFormatter<>(phoneNumberFilter);
        phoneNumberTextField.setTextFormatter(phoneNumberTextFormatter);

        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());
        visiblePasswordField.visibleProperty().bind(makePasswordHidden.visibleProperty());
        passwordField.visibleProperty().bind(makePasswordVisible.visibleProperty());

        roleToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
           if(newValue != null) {
               RadioButton selectedRadioButton = (RadioButton) newValue;
               role = selectedRadioButton.getText().toLowerCase();
           }
        });

        BooleanBinding allFieldsFilled =
                fullNameTextField.textProperty().isEmpty().or(
                        phoneNumberTextField.textProperty().isEmpty().or(
                                emailTextField.textProperty().isEmpty().or(
                                        passwordField.textProperty().isEmpty().or(
                                                bankNameTextField.textProperty().isEmpty().or(
                                                        accountNumberTextField.textProperty().isEmpty()
                                        )
                                )
                        )
                )
        );

        registerButton.disableProperty().bind(allFieldsFilled.or(isRegistering));

    }

    public int checkLoginStatus(String fullName, String phoneNumber, String email, String password, String role, String bankName, String accountNumber) {
        //do the stuff in backend
        return 200; //temporary
    }

    public String checkLoginRole(String phoneNumber){
        //do the stuff in backend
        String role = "buyer"; //temporary
        switch (role){
            case "buyer":
                return "/frontend/bemirfoodclient/homepage/buyer-homepage-view.fxml";
            case "seller":
                return "/frontend/bemirfoodclient/homepage/seller-homepage-view.fxml";
            case "courier":
                return "/frontend/bemirfoodclient/homepage/courier-homepage-view.fxml";
            case "admin":
                return "/frontend/bemirfoodclient/homepage/admin-homepage-view.fxml";
            default:
                return "";
        }    }



    @FXML
    public void handelRegisterButtonClicked() {
        registerButton.setDisable(true);

        String fullName = fullNameTextField.getText();
        String mobile = phoneNumberTextField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        String bankName = bankNameTextField.getText();
        String accountNumber = accountNumberTextField.getText();

        String json = String.format("""
    {
      "password": "%s",
      "full_name": "%s",
      "mobile": "%s",
      "email": "%s",
      "address": "",
      "profileImageBase64": "",
      "bank_info": {
        "bank_name": "%s",
        "account_number": "%s"
      },
      "role": "%s"
    }
    """,
                password, fullName, mobile, email,
                bankName, accountNumber, role
        );

        new Thread(() -> {
            int code = 0;
            try {
                HttpResponseData responseData = HttpClientHandler.sendPostRequest("http://localhost:4567/auth/register", json);
                code = responseData.getStatusCode();
                String body = responseData.getBody();
            } catch (IOException e) {
                showErrorAlertWithIcon("This is from our side; please try again later :) (500 Internal Server Error)");
            }

            int finalCode = code;
            Platform.runLater(() -> {
                switch (finalCode) {
                    case 200 -> {
                        PauseTransition delay = new PauseTransition(Duration.seconds(5));
                        delay.setOnFinished(event -> {
                            try {
                                Stage stage = (Stage) phoneNumberTextField.getScene().getWindow();
                                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                                        checkLoginRole(phoneNumberTextField.getText()))));
                                stage.getScene().setRoot(root);
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        });
                        delay.play();
                    }
                    case 400 -> showErrorAlertWithIcon("Invalid phone number or password. (400 Invalid input)");
                    case 401 -> showErrorAlertWithIcon("This phone number is not registered. (401 Unauthorized)");
                    case 403 -> showErrorAlertWithIcon("You cannot access this service. (403 Forbidden)");
                    case 404 -> showErrorAlertWithIcon("Service not found. (404 Not Found)");
                    case 409 -> showErrorAlertWithIcon("This phone number already exists. (409 Conflict)");
                    case 415 -> showErrorAlertWithIcon("This media type cannot be accepted. (415 Unsupported Media Type)");
                    case 429 -> showErrorAlertWithIcon("Please try again later. (429 Too Many Requests)");
                    case 500 -> showErrorAlertWithIcon("This is from our side; please try again later :) (500 Internal Server Error)");
                    default -> {
                        registerButton.setDisable(false);
                        showErrorAlertWithIcon("Unexpected response code: " + finalCode);
                    }
                }
            });
        }).start();
    }


    private void showErrorAlertWithIcon(String message) {
        registerButton.setDisable(false);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(
                BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
        alert.setTitle("Registration failed");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }



    @FXML
    public void handelLoginHyperlinkClicked() {
        try {
            Stage stage = (Stage) phoneNumberTextField.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/login-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void makePasswordVisible() {
        makePasswordVisible.setVisible(false);
        makePasswordHidden.setVisible(true);
    }

    public void makePasswordHidden() {
        makePasswordHidden.setVisible(false);
        makePasswordVisible.setVisible(true);
    }
}
