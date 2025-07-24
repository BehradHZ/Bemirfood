package frontend.bemirfoodclient.controller;

import HttpClientHandler.HttpRequest;
import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import Util.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.bemirfoodclient.BemirfoodApplication;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

import static exception.exp.expHandler;
import static HttpClientHandler.HttpClientHandler.sendRequest;

public class LoginController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();

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

    private BooleanProperty isLoggingIn = new SimpleBooleanProperty(false);

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

        BooleanBinding allFieldsFilled =
                phoneNumberTextField.textProperty().isEmpty().or(
                        passwordField.textProperty().isEmpty()
                );

        loginButton.disableProperty().bind(allFieldsFilled.or(isLoggingIn));
    }

    public HttpResponseData checkLoginStatus(String phoneNumber, String password) {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "auth/login", HttpRequest.POST, gson.toJson(Map.of("mobile", phoneNumber,
                                    "password", password))
                    );
            return responseData;
        }catch (IOException e){

            return new HttpResponseData(500, "Internal Server Error");
        }
    }

    public String checkLoginRole(String role){
        switch (role){
            case "buyer":
                return "/frontend/bemirfoodclient/border/buyer-border-view.fxml";
            case "seller":
                return "/frontend/bemirfoodclient/border/seller-border-view.fxml";
            case "courier":
                return "/frontend/bemirfoodclient/border/courier-border-view.fxml";
            case "admin":
                return "/frontend/bemirfoodclient/border/admin-border-view.fxml";
            default:
                return "";
        }
    }

    @FXML
    public void loginButtonClicked() {
        isLoggingIn.set(true);
        HttpResponseData response = checkLoginStatus(phoneNumberTextField.getText(), passwordField.getText());
        int code = response.getStatusCode();
        if(code == 200){
            try {
                String role = response.getBody().getAsJsonObject("user").get("role").getAsString();
                String token =  response.getBody().get("token").getAsString();
                Token.addToken(token);

                String homeDirectory = System.getProperty("user.dir");
                Path filePath = Path.of(homeDirectory, "registerTemp.txt");

                String jsonText = String.format("""
                {
                  "phone": "%s",
                  "role": "%s"
                }
                """,
                        phoneNumberTextField.getText(), role
                );
                String fileContent = "login data:\n" + jsonText;

                try {
                    Files.writeString(filePath, fileContent);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = (Stage) phoneNumberTextField.getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                        checkLoginRole(role))));
                stage.getScene().setRoot(root);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            isLoggingIn.set(false);
        }else {
            expHandler(response, "Login failed",isLoggingIn);
        }

    }

    public void showAlert(String content) {
        isLoggingIn.set(false);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
        alert.setTitle("Login failed");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
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
