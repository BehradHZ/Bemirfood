package frontend.bemirfoodclient.controller.profile.buyer.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class BuyerProfileDetailsController {
    @FXML
    public ImageView profileUpload;
    @FXML
    public Label fullNameLabel;
    @FXML
    public Label phoneNumberLabel;
    @FXML
    public Label emailLabel;

    @FXML
    public void initialize() {
        profileUpload.setPreserveRatio(true);
        profileUpload.setFitHeight(120);
        profileUpload.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(getProfileImageBase64()))));

        fullNameLabel.setText(getFullName());
        phoneNumberLabel.setText(getPhoneNumber());
        emailLabel.setText(getEmail());

    }

    public int updateCurrentUserProfile(String full_name, String phoneNumber, String email, String address, String profileImageBase64, Object bank_info) {
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
                    Alert alert409 = new Alert(Alert.AlertType.ERROR);
                    Stage stage409 = (Stage) alert409.getDialogPane().getScene().getWindow();
                    stage409.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                    alert409.setTitle("Login failed");
                    alert409.setHeaderText(null);
                    alert409.setContentText("There was a conflict for access to this service. (409 Conflict)");
                    alert409.getDialogPane().setGraphic(null);
                    alert409.showAndWait();
                    break;
                case 415:
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
                    break;

            }
        }
    }

    @FXML
    public void editProfileButtonClicked() {

    }

    public String getFullName() {
        //do the stuff in backend
        return "Behrad Hozouri";
    }

    public String getPhoneNumber() {
        //do the stuff in backend
        return "09220866912";
    }

    public String getEmail() {
        //do the stuff in backend
        return "bhvsrt2006@gmail.com";
    }

    public String getProfileImageBase64() {
        //do the stuff in backend
        if (false /*profile picture is added (temporary)*/) {
            return "/frontend/bemirfoodclient/assets/userProfilePicture.jpg";
        } else {
            return "/frontend/bemirfoodclient/assets/icons/profileUpload.png";
        }
    }

}
