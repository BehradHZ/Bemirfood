package frontend.bemirfoodclient.controller.profile.courier.details;

import HttpClientHandler.HttpResponseData;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Bank_info;
import frontend.bemirfoodclient.model.entity.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static HttpClientHandler.Requests.getCurrentUserProfile;

public class CourierProfileDetailsController {
    @FXML
    public ImageView profileImageView;
    @FXML
    public Label fullNameLabel;
    @FXML
    public Label phoneNumberLabel;
    @FXML
    public Label emailLabel;

    private static User courier;

    @FXML
    public void initialize() {
        setUser();
        profileImageView.setPreserveRatio(true);
        profileImageView.setFitHeight(120);
        setProfileImageBase64(profileImageView);

        fullNameLabel.setText(getFullName());
        phoneNumberLabel.setText(getPhoneNumber());
        emailLabel.setText(getEmail());

    }

    public void setUser() {
        //do the stuff in backend

        HttpResponseData responseData = getCurrentUserProfile();

        courier = new User(
                responseData.getString("full_name"),
                responseData.getString("phone"),
                responseData.getString("role"),
                responseData.getString("email"),
                responseData.getString("profileImageBase64"),
                responseData.getString("address"),
                new Bank_info(
                        responseData.getBody().getAsJsonObject("bank_info").get("bank_name").getAsString(),
                        responseData.getBody().getAsJsonObject("bank_info").get("account_number").getAsString()
                )
        );
    }

    public static int updateCurrentUserProfile(String full_name, String phoneNumber, String email, String address, String profileImageBase64, Object bank_info) {
        //do the stuff in backend
        //YAML: Update current user profile
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
        Stage stage = (Stage) profileImageView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String selectedFileURI = selectedFile.toURI().toString();
            Image originalImage = new Image(selectedFileURI);

            double originalWidth = originalImage.getWidth();
            double originalHeight = originalImage.getHeight();
            double cropSize = Math.min(originalWidth, originalHeight);
            double cropX = (originalWidth - cropSize) / 2;
            double cropY = (originalHeight - cropSize) / 2;
            Rectangle2D cropRectangle = new Rectangle2D(cropX, cropY, cropSize, cropSize);

            SnapshotParameters params = new SnapshotParameters();
            params.setViewport(cropRectangle);
            params.setFill(Color.TRANSPARENT);
            WritableImage croppedImage = profileImageView.snapshot(params, null);

            String base64String = null;
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(croppedImage, null);
            try (ByteArrayOutputStream byteOutput = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, "png", byteOutput);
                base64String = Base64.getEncoder().encodeToString(byteOutput.toByteArray());
            } catch (IOException e) {
                System.err.println("Failed to convert image to Base64.");
                e.printStackTrace();
            }

            if (base64String != null) {
                switch (updateCurrentUserProfile(null, null, null, null,
                        base64String, null)) {
                    case 200:
                        profileImageView.setImage(originalImage);
                        profileImageView.setViewport(cropRectangle);

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
    }

    public void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
        alert.setTitle("Login failed");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }


    public static String getFullName() {
        return courier.getFull_name();
    }

    public static String getPhoneNumber() {
        return courier.getMobile();
    }

    public static String getEmail() {
        return courier.getEmail();
    }

    public void setProfileImageBase64(ImageView profileImageView) {
        //do the stuff in backend
        boolean profilePhotoExists = false;
        if (profilePhotoExists) {
            String base64Uri = null;
            ImageLoader.displayBase64Image(base64Uri, profileImageView);
        } else {
            profileImageView.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("" +
                    "/frontend/bemirfoodclient/assets/icons/profileUpload.png"))));
        }
    }

    public static String getAddress() {
        return courier.getAddress();
    }

    public static String getBank_name() {
        return courier.getBank_info().getBank_name();
    }

    public static String getAccount_number() {
        return courier.getBank_info().getAccount_number();
    }
}
