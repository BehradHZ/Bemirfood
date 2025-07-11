package frontend.bemirfoodclient.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class loginController {
    @FXML
    public BorderPane mainBorderPane;
    @FXML
    public ImageView firstPageImage;
    @FXML
    public TextField phoneNumberTextField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField visiblePasswordField;
    @FXML
    public Button makePasswordVisible;
    @FXML
    public Button makePasswordHidden;
    @FXML
    public Button loginButton;

    @FXML
    public void initialize() {
        firstPageImage.fitHeightProperty().bind(mainBorderPane.heightProperty());

        UnaryOperator<TextFormatter.Change> filter = change -> {
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
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        phoneNumberTextField.setTextFormatter(textFormatter);

        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());
        visiblePasswordField.visibleProperty().bind(makePasswordHidden.visibleProperty());
        passwordField.visibleProperty().bind(makePasswordVisible.visibleProperty());
    }

    public int checkLoginStatus(String phoneNumber, String password) {
        return 200;
    }

    public String checkLoginRole(String phoneNumber){
        return "/frontend/bemirfoodclient/homepage/buyer-homepage-view.fxml";
    }

    @FXML
    public void loginButtonClicked() {
        loginButton.setDisable(true);
        switch (checkLoginStatus(phoneNumberTextField.getText(), passwordField.getText())){
            case 200:
                System.out.println("User logged in with " + phoneNumberTextField.getText() + " " + passwordField.getText());
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
                    loginButton.setDisable(false);
                });
                delay.play();

                break;
            case 400:
                loginButton.setDisable(false);
                break;
            case 401:
                loginButton.setDisable(false);
                break;
            case 403:
                loginButton.setDisable(false);
                break;
            case 404:
                loginButton.setDisable(false);
                break;
            case 409:
                loginButton.setDisable(false);
                break;
            case 415:
                loginButton.setDisable(false);
                break;
            case 429:
                loginButton.setDisable(false);
                break;
            case 500:
                loginButton.setDisable(false);
                break;
            default:
                loginButton.setDisable(false);
                break;
        }
    }

    @FXML
    public void registerHyperlinkClicked() {
        try {
            Stage stage = (Stage) phoneNumberTextField.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/register-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    public void makePasswordVisible() {
        makePasswordVisible.setVisible(false);
        makePasswordHidden.setVisible(true);
    }

    @FXML
    public void makePasswordHidden() {
        makePasswordHidden.setVisible(false);
        makePasswordVisible.setVisible(true);
    }
}
