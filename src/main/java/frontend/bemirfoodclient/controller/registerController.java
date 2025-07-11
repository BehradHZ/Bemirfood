package frontend.bemirfoodclient.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
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
        return 200;
    }

    public String checkLoginRole(String phoneNumber){
        return "/frontend/bemirfoodclient/homepage/buyer-homepage-view.fxml";
    }

    @FXML
    public void handelRegisterButtonClicked() {
        registerButton.setDisable(true);
        switch (checkLoginStatus(fullNameTextField.getText(),
                phoneNumberTextField.getText(),
                emailTextField.getText(),
                passwordField.getText(),
                role)){
            case 200:
                System.out.println("User registered with " + fullNameTextField.getText() + " " +
                        phoneNumberTextField.getText() + " " +
                        emailTextField.getText() + " " +
                        passwordField.getText() + " " +
                        role);
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
                break;
            case 401:
                break;
            case 403:
                break;
            case 404:
                break;
            case 409:
                break;
            case 415:
                break;
            case 429:
                break;
            case 500:
                break;
            default:
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
