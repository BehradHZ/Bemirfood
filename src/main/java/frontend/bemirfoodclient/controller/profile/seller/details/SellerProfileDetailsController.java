package frontend.bemirfoodclient.controller.profile.seller.details;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static exception.exp.expHandler;
import static HttpClientHandler.Requests.getCurrentUserProfile;
import static HttpClientHandler.Requests.updateUserProfile;

public class SellerProfileDetailsController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();

    @FXML
    public ImageView profileImageView;
    @FXML
    public Label fullNameLabel;
    @FXML
    public Label phoneNumberLabel;
    @FXML
    public Label emailLabel;
    @FXML
    public Label bankNameLabel;
    @FXML
    public Label accountNumberLabel;

    private static User seller;

    @FXML
    public void initialize() {
        setUser();
        profileImageView.setPreserveRatio(true);
        profileImageView.setFitHeight(120);
        setScene();
    }

    public void setScene() {
        setProfileImageBase64(profileImageView);
        fullNameLabel.setText(getFullName());
        phoneNumberLabel.setText(getPhoneNumber());
        emailLabel.setText(getEmail());
        bankNameLabel.setText(seller.getBank_info().getBank_name());
        accountNumberLabel.setText(seller.getBank_info().getAccount_number());
    }

    public void setUser() {
        HttpResponseData responseData = getCurrentUserProfile();

        seller = new User(
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

    public static HttpResponseData updateCurrentUserProfile(String full_name, String phoneNumber, String email, String address, String profileImageBase64, Object bank_info) {
        Map<String, Object> request = new LinkedHashMap<>();
        if(full_name != null && !full_name.isBlank()) request.put("full_name", full_name);
        if(phoneNumber != null && !phoneNumber.isBlank()) request.put("phone", phoneNumber);
        if(email != null && !email.isBlank()) request.put("email", email);
        if(address != null && !address.isBlank()) request.put("address", address);
        if(bank_info != null) request.put("bank_info", bank_info);
        if(profileImageBase64 != null && !profileImageBase64.isBlank()) request.put("profileImageBase64", profileImageBase64);

       return updateUserProfile(gson.toJson(request));
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

            HttpResponseData responseData = updateCurrentUserProfile(getFullName(), getPhoneNumber(), getEmail(), getAddress(),
                        base64String, new Bank_info(getBank_name(), getAccount_number()));
            if(responseData.getStatusCode() == 200) {
                profileImageView.setImage(originalImage);
                profileImageView.setViewport(cropRectangle);
            }else{
                expHandler(responseData, "Failed to update profile", null);
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
        return seller.getFull_name();
    }

    public static String getPhoneNumber() {
        return seller.getMobile();
    }

    public static String getEmail() {
        return seller.getEmail();
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
        return seller.getAddress();
    }

    public static String getBank_name() {
        return seller.getBank_info().getBank_name();
    }

    public static String getAccount_number() {
        return seller.getBank_info().getAccount_number();
    }

    public static User getSeller() {
        return seller;
    }
}