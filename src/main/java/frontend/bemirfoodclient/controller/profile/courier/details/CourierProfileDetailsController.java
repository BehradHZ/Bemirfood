package frontend.bemirfoodclient.controller.profile.courier.details;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import Util.ImageProcess;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Bank_info;
import frontend.bemirfoodclient.model.entity.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static HttpClientHandler.Requests.getCurrentUserProfile;
import static HttpClientHandler.Requests.updateUserProfile;
import static Util.ImageProcess.imageFileToBase64;
import static exception.exp.expHandler;

public class CourierProfileDetailsController {
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

    private static User courier;

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
        bankNameLabel.setText(courier.getBank_info().getBank_name());
        accountNumberLabel.setText(courier.getBank_info().getAccount_number());
    }

    public void setUser() {
        //do the stuff in backend

        HttpResponseData responseData = getCurrentUserProfile();
        JsonObject body = responseData.getBody().get("Current user profile").getAsJsonObject();

        courier = new User(
                body.get("full_name").getAsString(),
                body.get("phone").getAsString(),
                body.get("role").getAsString(),
                body.has("email") && !body.get("email").isJsonNull() ? body.get("email").getAsString() : null,
                body.has("profileImageBase64") && !body.get("profileImageBase64").isJsonNull() ? body.get("profileImageBase64").getAsString() : null,
                body.get("address").getAsString(),
                body.has("bank_info") && !body.get("bank_info").isJsonNull()
                        ? new Bank_info(
                        body.getAsJsonObject("bank_info").get("bank_name").getAsString(),
                        body.getAsJsonObject("bank_info").get("account_number").getAsString()
                )
                        : null
        );
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

            // Crop the image using PixelReader instead of snapshot
            PixelReader pixelReader = originalImage.getPixelReader();
            if (pixelReader == null) {
                System.err.println("PixelReader is null. Cannot read image data.");
                return;
            }
            WritableImage croppedImage = new WritableImage(pixelReader, (int) cropX, (int) cropY, (int) cropSize, (int) cropSize);

            // Ensure directory exists
            File dir = new File(ImageProcess.outputDir);
            if (!dir.exists()) dir.mkdirs();

            // Save the cropped image
            String fileName = "User" + courier.getId();
            File outputFile = new File(ImageProcess.outputDir + File.separator + fileName + "." + ImageProcess.format);

            try {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(croppedImage, null);
                ImageIO.write(bufferedImage, ImageProcess.format, outputFile);

                String base64String = imageFileToBase64(fileName);
                if (base64String == null) {
                    System.err.println("File not found when encoding to Base64.");
                    return;
                }

                base64String = base64String.replaceAll("\\s", "");

                HttpResponseData responseData = updateCurrentUserProfile(
                        getFullName(), getPhoneNumber(), getEmail(), getAddress(),
                        base64String, new Bank_info(getBank_name(), getAccount_number())
                );

                if (responseData.getStatusCode() == 200) {
                    profileImageView.setImage(originalImage);
                    profileImageView.setViewport(cropRectangle);
                } else {
                    expHandler(responseData, "Failed to update profile", null);
                }

            } catch (IOException e) {
                System.err.println("Error while saving or encoding the image.");
                e.printStackTrace();
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
        HttpResponseData responseData = getCurrentUserProfile();
        JsonObject body = responseData.getBody().get("Current user profile").getAsJsonObject();
        String base64Uri = body.has("profileImageBase64") && !body.get("profileImageBase64").isJsonNull()
                ? body.get("profileImageBase64").getAsString()
                : null;

        //boolean profilePhotoExists = false;
        if (base64Uri != null) {
            ImageLoader.displayBase64Image(base64Uri, profileImageView);
        } else {
            profileImageView.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("" +
                    "/frontend/bemirfoodclient/assets/icons/profileUpload.png"))));
        }
    }

    public static HttpResponseData updateCurrentUserProfile(String full_name, String phoneNumber, String email, String address, String profileImageBase64, Bank_info bank_info) {
        //do the stuff in backend
        Map<String, Object> request = new LinkedHashMap<>();
        if(full_name != null && !full_name.isBlank()) request.put("full_name", full_name);
        if(phoneNumber != null && !phoneNumber.isBlank()) request.put("phone_number", phoneNumber);
        if(email != null && !email.isBlank()) request.put("email", email);
        if(address != null && !address.isBlank()) request.put("address", address);
        if(profileImageBase64 != null && !profileImageBase64.isBlank()) request.put("profileImageBase64", profileImageBase64);
        if(bank_info != null) request.put("bank_info", bank_info);
        return  updateUserProfile(gson.toJson(request));
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

    public static User getCourier() {
        return courier;
    }
}
