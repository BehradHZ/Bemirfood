package frontend.bemirfoodclient.controller.profile.seller.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class SellerProfileDetailsController {
    @FXML
    public ImageView profileUpload;
    @FXML
    public Label fullNameLabel;
    @FXML
    public Label phoneNumberLabel;
    @FXML
    public Label emailLabel;

    private User user;

    @FXML
    public void initialize() {
        profileUpload.setPreserveRatio(true);
        profileUpload.setFitHeight(120);
        profileUpload.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(getProfileImageBase64()))));

        fullNameLabel.setText(getFullName());
        phoneNumberLabel.setText(getPhoneNumber());
        emailLabel.setText(getEmail());

    }

    public static int updateCurrentUserProfile(String full_name, String phoneNumber, String email, String address, String profileImageBase64, Object bank_info) {
        //do the stuff in backend
        return 200; //temporary
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
            switch (updateCurrentUserProfile(null, null, null, null,
                    selectedFile.toURI().toString(), null)) {
                case 200:
                    profileUpload.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(getProfileImageBase64()))));
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
                    showAlert("There was a conflict for access to this service. (409 Conflict)");
                    break;
                case 415:
                    showAlert("This media type cannot be accepted. (415 Unsupported Media Type)");
                    break;
                case 429:
                    showAlert("Please try again later. (429 Too Many Requests)");
                    break;
                case 500:
                    showAlert("This is from our side; pleas try again later :) (500 Internal Server Error)");
                default:
                    break;
            }
        }
    }

    public void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
        alert.setTitle("Loading profile failed");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }

    public static String getFullName() {
        //do the stuff in backend
        return "Behrad Hozouri"; //temporary
    }

    public static String getPhoneNumber() {
        //do the stuff in backend
        return "09220866912"; //temporary
    }

    public static String getEmail() {
        //do the stuff in backend
        return "bhvsrt2006@gmail.com"; //temporary
    }

    public static String getProfileImageBase64() {
        //do the stuff in backend
        if (false /*profile picture is added (temporary)*/) {
            return "/frontend/bemirfoodclient/assets/userProfilePicture.jpg"; //temporary
        } else {
            return "/frontend/bemirfoodclient/assets/icons/profileUpload.png";
        }
    }

    public static String getAddress() {
        //do the stuff in backend
        return "Karaj"; //temporary
    }

    //change this based on backend Bank_info Object
    public static String getBank_name() {
        //do the stuff in backend
        return "Saderat"; //temporary
    }

    public static String getAccount_number() {
        //do the stuff in backend
        return "12345"; //temporary
    }
}
