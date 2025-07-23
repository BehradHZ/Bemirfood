package frontend.bemirfoodclient.controller;

import HttpClientHandler.HttpClientHandler;
import HttpClientHandler.HttpRequest;
import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.time.LocalDateTime;
import java.util.Objects;

import static Exception.exp.expHandler;


public class RegisterAdditionalController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();

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
    Path filePath;

    @FXML
    public void initialize() {
        String homeDirectory = System.getProperty("user.dir");
        filePath = Path.of(homeDirectory, "registerTemp.txt");
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

    public HttpResponseData checkLoginStatus(String fullName, String phoneNumber, String email, String password, String role,
                                String address, String profileImageBase64, String bank_name, String account_number) {
        //do the stuff in backend
//        Map<String, Object> responseBody = new LinkedHashMap<>();
//        Map<String, String> bank_info = new LinkedHashMap<>();
//        bank_info.put("bank_name", bank_name);
//        bank_info.put("account_number", account_number);
//        responseBody.put("password", password);
//        responseBody.put("fullName", fullName);
//        responseBody.put("phoneNumber", phoneNumber);
//        responseBody.put("email", email);
//        responseBody.put("address", address);
//        responseBody.put("bank_info", bank_info);
//        responseBody.put("role", role);
//        String body =  gson.toJson(responseBody);
        String body = String.format("""
        {
          "password": "%s",
          "full_name": "%s",
          "mobile": "%s",
          "email": "%s",
          "address": "%s",
          "profileImageBase64": "%s",
          "bank_info": {
            "bank_name": "%s",
            "account_number": "%s"
          },
          "role": "%s"
        }
        """,
                    password, fullName, phoneNumber, email,address,profileImageBase64,
                bank_name, account_number, role
            );

        try {
            return HttpClientHandler
                    .sendRequest("auth/register",HttpRequest.POST,body);
        } catch (IOException e) {
            return new HttpResponseData(500, "Internal Server Error");
        }
    }

    public String checkLoginRole(String role){
        return switch (role) {
            case "admin" -> "";
            case "customer" -> "/frontend/bemirfoodclient/border/buyer-border-view.fxml";
            case "seller" -> "/frontend/bemirfoodclient/border/seller-border-view.fxml";
            case "delivery" -> "/frontend/bemirfoodclient/border/courier-border-view.fxml";
            default -> "";
        };
    }

    @FXML
    public void handelRegisterButtonClicked() {
        isRegistering.set(true);

        HttpResponseData response = checkLoginStatus(
                user.getFullName(),
                user.getMobile(),
                emailTextField.getText(),
                user.getPassword(),
                user.getRoleAsString(),
                user.getAddress(),
                user.getPhoto(),
                bank_nameTextField.getText(),
                account_numberTextField.getText());

        int code = response.getStatusCode();
        if(code == 200) {
            //Add login token
//            Map<String, Object> body = new LinkedHashMap<>();
//            body.put("phone", user.getMobile());
//            body.put("role", user.getRoleAsString());
//            String jsonText = gson.toJson(body);

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
                Files.writeString(filePath, fileContent);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Stage stage = (Stage) emailTextField.getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                        checkLoginRole(response.getBody().get("role").getAsString().toLowerCase()))));
                stage.getScene().setRoot(root);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            expHandler(response, "Registration failed", isRegistering);
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
