package frontend.bemirfoodclient.controller;

import frontend.bemirfoodclient.BemirfoodApplication;
import javafx.animation.PauseTransition;
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
    public Button registerButton;

    public String role;


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

    }

    public int checkLoginStatus(String fullName, String phoneNumber, String email, String password, String role) {
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
        switch (checkLoginStatus(fullNameTextField.getText(),
                phoneNumberTextField.getText(),
                emailTextField.getText(),
                passwordField.getText(),
                role)){
            case 200:
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

                break;
            case 400:
                registerButton.setDisable(false);
                Alert alert400 = new Alert(Alert.AlertType.ERROR);
                Stage stage400 = (Stage) alert400.getDialogPane().getScene().getWindow();
                stage400.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                alert400.setTitle("Login failed");
                alert400.setHeaderText(null);
                alert400.setContentText("Invalid phone number or password. (400 Invalid input)");
                alert400.getDialogPane().setGraphic(null);
                alert400.showAndWait();
                break;
            case 401:
                registerButton.setDisable(false);
                Alert alert401 = new Alert(Alert.AlertType.ERROR);
                Stage stage401 = (Stage) alert401.getDialogPane().getScene().getWindow();
                stage401.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                alert401.setTitle("Login failed");
                alert401.setHeaderText(null);
                alert401.setContentText("This phone number is not registered. (401 Unauthorized)");
                alert401.getDialogPane().setGraphic(null);
                alert401.showAndWait();
                break;
            case 403:
                registerButton.setDisable(false);
                Alert alert403 = new Alert(Alert.AlertType.ERROR);
                Stage stage403 = (Stage) alert403.getDialogPane().getScene().getWindow();
                stage403.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                alert403.setTitle("Login failed");
                alert403.setHeaderText(null);
                alert403.setContentText("You cannot access to this service. (403 Forbidden)");
                alert403.getDialogPane().setGraphic(null);
                alert403.showAndWait();
                break;
            case 404:
                registerButton.setDisable(false);
                Alert alert404 = new Alert(Alert.AlertType.ERROR);
                Stage stage404 = (Stage) alert404.getDialogPane().getScene().getWindow();
                stage404.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                alert404.setTitle("Login failed");
                alert404.setHeaderText(null);
                alert404.setContentText("Service not found. (404 Not Found)");
                alert404.getDialogPane().setGraphic(null);
                alert404.showAndWait();
                break;
            case 409:
                registerButton.setDisable(false);
                Alert alert409 = new Alert(Alert.AlertType.ERROR);
                Stage stage409 = (Stage) alert409.getDialogPane().getScene().getWindow();
                stage409.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                alert409.setTitle("Login failed");
                alert409.setHeaderText(null);
                alert409.setContentText("This phone number already exist. (409 Phone number already exist)");
                alert409.getDialogPane().setGraphic(null);
                alert409.showAndWait();
                break;
            case 415:
                registerButton.setDisable(false);
                Alert alert415 = new Alert(Alert.AlertType.ERROR);
                Stage stage415 = (Stage) alert415.getDialogPane().getScene().getWindow();
                stage415.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                alert415.setTitle("Login failed");
                alert415.setHeaderText(null);
                alert415.setContentText("This media type cannot be accepted. (415 Unsupported Media Type)");
                alert415.getDialogPane().setGraphic(null);
                alert415.showAndWait();
                break;
            case 429:
                registerButton.setDisable(false);
                Alert alert429 = new Alert(Alert.AlertType.ERROR);
                Stage stage429 = (Stage) alert429.getDialogPane().getScene().getWindow();
                stage429.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                alert429.setTitle("Login failed");
                alert429.setHeaderText(null);
                alert429.setContentText("Please try again later. (429 Too Many Requests)");
                alert429.getDialogPane().setGraphic(null);
                alert429.showAndWait();
                break;
            case 500:
                registerButton.setDisable(false);
                Alert alert500 = new Alert(Alert.AlertType.ERROR);
                Stage stage500 = (Stage) alert500.getDialogPane().getScene().getWindow();
                stage500.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                alert500.setTitle("Login failed");
                alert500.setHeaderText(null);
                alert500.setContentText("This is from our side; pleas try again later :) (500 Internal Server Error)");
                alert500.getDialogPane().setGraphic(null);
                alert500.showAndWait();
                break;
            default:
                registerButton.setDisable(false);
                break;
        }
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
