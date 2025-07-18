package frontend.bemirfoodclient.controller;

import HttpClientHandler.HttpClientHandler;
import HttpClientHandler.HttpResponseData;
import com.google.gson.Gson;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.dto.UserDto;
import frontend.bemirfoodclient.model.entity.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


public class RegisterAdditionalController {
    User user;

    @FXML
    public BorderPane mainBorderPane;
    @FXML
    public ImageView firstPageImage;
    @FXML
    public Button backButton;
    @FXML
    public ImageView backButtonImage;
    @FXML
    public TextField emailTextField;
    @FXML
    public TextField bankNameTextField;
    @FXML
    public TextField accountNumberTextField;
    @FXML
    public Button registerButton;

    private BooleanProperty isRegistering = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        String homeDirectory = System.getProperty("user.home");
        Path filePath = Path.of(homeDirectory, "registerTemp.txt");
        try {
            String fileContent = Files.readString(filePath);
            int jsonStartIndex = fileContent.indexOf('{');
            String jsonText = fileContent.substring(jsonStartIndex);

            Gson gson = new Gson();
            UserDto userDto = gson.fromJson(jsonText, UserDto.class);
            user = User.UserDtoToUser(userDto);

        } catch (IOException e) {
            e.printStackTrace();
        }

        firstPageImage.fitHeightProperty().bind(mainBorderPane.heightProperty());

        backButtonImage.setPreserveRatio(true);
        backButtonImage.setFitHeight(20);
    }

    public int checkLoginStatus(String fullName, String phoneNumber, String email, String password, String role,
                                String address, String profileImageBase64, String bankName, String accountNumber) {
        //do the stuff in backend
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
                    password, fullName, phoneNumber, email,
                    bankName, accountNumber, role
            );

        int code = 0;
        try {
            HttpResponseData responseData = HttpClientHandler.sendPostRequest("http://localhost:4567/auth/register", json);
            code = responseData.getStatusCode();
            String body = responseData.getBody();
            return code;
        } catch (IOException e) {
            return 500;
        }
    }

    public String checkLoginRole(String role){
        return switch (role) {
            case "admin" -> "";
            case "buyer" -> "/frontend/bemirfoodclient/homepage/buyer-homepage-view.fxml";
            case "seller" -> "/frontend/bemirfoodclient/homepage/seller-homepage-view.fxml";
            case "courier" -> "/frontend/bemirfoodclient/homepage/courier-homepage-view.fxml";
            default -> "";
        };
    }

    @FXML
    public void handelRegisterButtonClicked() {
        isRegistering.set(true);

        String fullName = user.getFullName();
        String phoneNumber = user.getMobile();
        String password = user.getPassword();
        String role = user.getRoleAsString();
        String address = user.getAddress();

        switch (checkLoginStatus(fullName,
                phoneNumber,
                emailTextField.getText(),
                password,
                role,
                address,
                "",
                bankNameTextField.getText(),
                accountNumberTextField.getText())){
            case 200:
                try {
                    try {
                        String homeDirectory = System.getProperty("user.home");
                        Path filePath = Path.of(homeDirectory, "registerTemp.txt");
                        Files.writeString(filePath, "no register data");
                        System.out.println("Successfully wrote data to " + filePath);

                    } catch (IOException e) { // Catch the specific, correct exception
                        e.printStackTrace();
                    }
                    Stage stage = (Stage) emailTextField.getScene().getWindow();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                            checkLoginRole(role))));
                    stage.getScene().setRoot(root);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                break;
            case 400:
                isRegistering.set(false);
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
                isRegistering.set(false);
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
                isRegistering.set(false);
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
                isRegistering.set(false);
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
                isRegistering.set(false);
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
                isRegistering.set(false);
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
                isRegistering.set(false);
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
                isRegistering.set(false);
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
                isRegistering.set(false);
                break;
        }
    }




    @FXML
    public void handelLoginHyperlinkClicked() {
        try {
            Stage stage = (Stage) emailTextField.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/login-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void backButtonClicked() {
    }
}
