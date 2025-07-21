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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
    public ImageView profileUpload;
    @FXML
    public TextField emailTextField;
    @FXML
    public TextField bank_nameTextField;
    @FXML
    public TextField account_numberTextField;
    @FXML
    public Button registerButton;

    private BooleanProperty isRegistering = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        String homeDirectory = System.getProperty("user.home");
        Path filePath = Path.of(homeDirectory, "registerTemp.txt");
        if (Files.exists(filePath)) {
            try {
                if (Files.readString(filePath).startsWith("temporary register data")) {
                    String fileContent = Files.readString(filePath);
                    int jsonStartIndex = fileContent.indexOf('{');
                    String jsonText = fileContent.substring(jsonStartIndex);

                    Gson gson = new Gson();
                    UserDto userDto = gson.fromJson(jsonText, UserDto.class);
                    user = User.UserDtoToUser(userDto);

                    if (user.getPhoto() != null && !user.getPhoto().isEmpty())
                        profileUpload.setImage(new Image(user.getPhoto()));
                    if (user.getEmail() != null && !user.getEmail().isEmpty())
                        emailTextField.setText(user.getEmail());
                    if (user.getBank_info().getBank_name() != null && !user.getBank_info().getBank_name().isEmpty())
                        bank_nameTextField.setText(user.getBank_info().getBank_name());
                    if (user.getBank_info().getAccount_number() != null && !user.getBank_info().getAccount_number().isEmpty())
                        account_numberTextField.setText(user.getBank_info().getAccount_number());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        firstPageImage.fitHeightProperty().bind(mainBorderPane.heightProperty());

        backButtonImage.setPreserveRatio(true);
        backButtonImage.setFitHeight(20);

        profileUpload.setPreserveRatio(true);
        profileUpload.setFitHeight(120);
        profileUpload.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                "/frontend/bemirfoodclient/assets/icons/profileUpload.png"))));
    }

    public int checkLoginStatus(String fullName, String phoneNumber, String email, String password, String role,
                                String address, String profileImageBase64, String bank_name, String account_number) {
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
                bank_name, account_number, role
            );

        int code = 0;
        try {
            HttpResponseData responseData = HttpClientHandler.sendPostRequest("http://localhost:4567/auth/register", json);
            code = responseData.getStatusCode();
            String body = responseData.getBody();
            return code;
        } catch (IOException e) {
            return 200;
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

        switch (checkLoginStatus(
                user.getFullName(),
                user.getMobile(),
                emailTextField.getText(),
                user.getPassword(),
                user.getRoleAsString(),
                user.getAddress(),
                user.getPhoto(),
                bank_nameTextField.getText(),
                account_numberTextField.getText())){
            case 200:
                //Add login token
                String jsonText = String.format("""
                {
                  "phone": "%s",
                  "role": "%s"
                }
                """,
                        user.getMobile(), user.getRoleAsString()
                );

                    String fileContent = "login data:\n" + jsonText;

                try {
                    String homeDirectory = System.getProperty("user.home");
                    Path filePath = Path.of(homeDirectory, "registerTemp.txt");
                    Files.writeString(filePath, fileContent);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Stage stage = (Stage) emailTextField.getScene().getWindow();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                            checkLoginRole(user.getRoleAsString()))));
                    stage.getScene().setRoot(root);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            case 400:
                showAlert("Invalid phone number or password. (400 Invalid input)");
                break;
            case 401:
                showAlert("This phone number is not registered. (401 Unauthorized)");
                break;
            case 403:
                showAlert("You cannot access to this service. (403 Forbidden)");
                break;
            case 404:
                showAlert("Service not found. (404 Not Found)");
                break;
            case 409:
                showAlert("This phone number already exist. (409 Phone number already exist)");
                break;
            case 415:
                showAlert("This media type cannot be accepted. (415 Unsupported Media Type)");
                break;
            case 429:
                showAlert("Please try again later. (429 Too Many Requests)");
                break;
            case 500:
                showAlert("This is from our side; pleas try again later :)\n" +
                                  "(500 Internal Server Error)");
            default:
                isRegistering.set(false);
                break;
        }
    }

    public void showAlert(String content) {
        isRegistering.set(false);
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
        String jsonText = String.format("""
        {
          "full_name": "%s",
          "phone": "%s",
          "email": "%s",
          "password": "%s",
          "role": "%s",
          "address": "%s",
          "profileImageBase64": "%s",
          "bank_info": {
            "bank_name": "%s",
            "account_number": "%s"
          }
        }
        """,
                user.getFullName(), user.getMobile(), emailTextField.getText(), user.getPassword(),
                user.getRole().toString().toLowerCase(), user.getAddress()   , user.getPhoto(),
                bank_nameTextField.getText(), account_numberTextField.getText()
        );

        String fileContent = "temporary register data 2:\n" + jsonText;

        try {
            String homeDirectory = System.getProperty("user.home");
            Path filePath = Path.of(homeDirectory, "registerTemp.txt");
            Files.writeString(filePath, fileContent);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Stage stage = (Stage) emailTextField.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/register-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    public void addProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        Stage stage = (Stage) profileUpload.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            user.setPhoto(selectedFile.toURI().toString());
        }
        profileUpload.setImage(new Image(selectedFile.toURI().toString()));
    }
}
