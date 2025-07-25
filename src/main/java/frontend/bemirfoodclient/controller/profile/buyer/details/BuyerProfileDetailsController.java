package frontend.bemirfoodclient.controller.profile.buyer.details;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
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

import static HttpClientHandler.Requests.getCurrentUserProfile;
import static HttpClientHandler.Requests.updateUserProfile;
import static exception.exp.expHandler;

public class BuyerProfileDetailsController {

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

    private static User buyer;

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
        bankNameLabel.setText(buyer.getBank_info().getBank_name());
        accountNumberLabel.setText(buyer.getBank_info().getAccount_number());
    }

    public void setUser() {
        //do the stuff in backend

        HttpResponseData responseData = getCurrentUserProfile();

        JsonObject body = responseData.getBody().get("Current user profile").getAsJsonObject();

        String fullName = body.has("full_name") && !body.get("full_name").isJsonNull()
                ? body.get("full_name").getAsString()
                : null;

        String phone = body.has("phone") && !body.get("phone").isJsonNull()
                ? body.get("phone").getAsString()
                : null;

        String email = body.has("email") && !body.get("email").isJsonNull()
                ? body.get("email").getAsString()
                : null;

        String profileImageBase64 = body.has("profileImageBase64") && !body.get("profileImageBase64").isJsonNull()
                ? body.get("profileImageBase64").getAsString()
                : null;

        String address = body.has("address") && !body.get("address").isJsonNull()
                ? body.get("address").getAsString()
                : null;

        Bank_info bankInfo = null;
        if (body.has("bank_info") && !body.get("bank_info").isJsonNull()) {
            JsonObject bankJson = body.getAsJsonObject("bank_info");

            String bankName = bankJson.has("bank_name") && !bankJson.get("bank_name").isJsonNull()
                    ? bankJson.get("bank_name").getAsString()
                    : null;

            String accountNumber = bankJson.has("account_number") && !bankJson.get("account_number").isJsonNull()
                    ? bankJson.get("account_number").getAsString()
                    : null;

            bankInfo = new Bank_info(bankName, accountNumber);
        }

        buyer = new User(fullName, phone, "buyer", email, profileImageBase64, address, bankInfo);

        setScene();
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
                HttpResponseData response =     updateCurrentUserProfile(
                        fullNameLabel.getText(),
                        phoneNumberLabel.getText(),
                        emailLabel.getText(),
                        getAddress(),
                        base64String,
                        new Bank_info(getBank_name(), getAccount_number())
                );
                if(response.getStatusCode() == 200) {
                    profileImageView.setImage(originalImage);
                    profileImageView.setViewport(cropRectangle);
                }else{
                    expHandler(response, "Update profile info failed", null);
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
        return buyer.getFull_name();
    }

    public static String getPhoneNumber() {
        return buyer.getMobile();
    }

    public static String getEmail() {
        return buyer.getEmail();
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
        return buyer.getAddress();
    }

    public static String getBank_name() {
        return buyer.getBank_info().getBank_name();
    }

    public static String getAccount_number() {
        return buyer.getBank_info().getAccount_number();
    }

    public static User getBuyer() {
        return buyer;
    }
}
