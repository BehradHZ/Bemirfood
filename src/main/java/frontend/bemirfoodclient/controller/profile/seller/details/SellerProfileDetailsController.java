package frontend.bemirfoodclient.controller.profile.seller.details;

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
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;

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

import static HttpClientHandler.Requests.getCurrentUserProfile;
import static HttpClientHandler.Requests.updateUserProfile;
import static Util.ImageProcess.imageFileToBase64;
import static exception.exp.expHandler;

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

    @Getter
    private static User seller;

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
        HttpResponseData responseData = getCurrentUserProfile();
        JsonObject body = responseData.getBody().get("Current user profile").getAsJsonObject();
        if (responseData.getStatusCode() != 200) {
            expHandler(responseData, "Failed to get profile", null);
        }
        String fullName = "username", phoneNumber = "phone", email = "", address = "address", role = "seller",
                profileImageBase64 = "";
        Bank_info bank_info = null;
        try {
            fullName = body.has("full_name") && !body.get("full_name").isJsonNull()
                    ? body.get("full_name").getAsString()
                    : null;

            phoneNumber = body.has("phone") && !body.get("phone").isJsonNull()
                    ? body.get("phone").getAsString()
                    : null;

            email = body.has("email") && !body.get("email").isJsonNull()
                    ? body.get("email").getAsString()
                    : null;

            address = body.has("address") && !body.get("address").isJsonNull()
                    ? body.get("address").getAsString()
                    : null;

            role = body.has("role") && !body.get("role").isJsonNull()
                    ? body.get("role").getAsString()
                    : null;

            if (body.has("profileImageBase64") && !body.get("profileImageBase64").isJsonNull()) {
                profileImageBase64 = body.get("profileImageBase64").getAsString();
            }

            if (body.has("bank_info") && !body.get("bank_info").isJsonNull()) {
                JsonObject bankInfoJson = body.getAsJsonObject("bank_info");

                String bankName = bankInfoJson.has("bank_name") && !bankInfoJson.get("bank_name").isJsonNull()
                        ? bankInfoJson.get("bank_name").getAsString()
                        : null;

                String accountNumber = bankInfoJson.has("account_number") && !bankInfoJson.get("account_number").isJsonNull()
                        ? bankInfoJson.get("account_number").getAsString()
                        : null;

                bank_info = new Bank_info(bankName, accountNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        seller = new User(
                fullName, phoneNumber, role, email, profileImageBase64, address, bank_info
        );
        seller.setId(body.get("id").getAsLong());
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
            String fileName = "User" + seller.getId();
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
        return seller.getFull_name();
    }

    public static String getPhoneNumber() {
        return seller.getMobile();
    }

    public static String getEmail() {
        return seller.getEmail();
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