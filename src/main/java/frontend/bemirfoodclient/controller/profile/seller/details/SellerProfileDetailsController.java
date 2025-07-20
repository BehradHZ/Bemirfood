package frontend.bemirfoodclient.controller.profile.seller.details;

import frontend.bemirfoodclient.BemirfoodApplication;
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

public class SellerProfileDetailsController {
    @FXML
    public ImageView profileImageView;
    @FXML
    public Label fullNameLabel;
    @FXML
    public Label phoneNumberLabel;
    @FXML
    public Label emailLabel;

    private static User seller;

    @FXML
    public void initialize() {
        setUser();
        profileImageView.setPreserveRatio(true);
        profileImageView.setFitHeight(120);

        //fix base64image
        String uri = getProfileImageBase64();
        if (uri == null) {
            uri = "/frontend/bemirfoodclient/assets/icons/profileUpload.png";
            profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(uri))));
        } else {
            // 1. Load the image from the URI
            Image originalImage = new Image(uri);

            // 2. Add a listener to wait for the image to load
            originalImage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                if (newWidth.doubleValue() > 0) {
                    // 3. Once loaded, calculate the crop rectangle
                    double originalHeight = originalImage.getHeight();
                    double cropSize = Math.min(newWidth.doubleValue(), originalHeight);
                    double cropX = (newWidth.doubleValue() - cropSize) / 2;
                    double cropY = (originalHeight - cropSize) / 2;

                    // 4. Apply the viewport to display the cropped area
                    profileImageView.setViewport(new Rectangle2D(cropX, cropY, cropSize, cropSize));
                }
            });
            profileImageView.setImage(originalImage);
        }

        fullNameLabel.setText(getFullName());
        phoneNumberLabel.setText(getPhoneNumber());
        emailLabel.setText(getEmail());
    }

    public void setUser() {
        //do the stuff in backend
        //set "" if null
        //YAML: Get current seller profile

        //temporary
        seller = new User("Behrad Hozouri", "09220866912", "seller", "bhvsrt2006@gmail.com",
                null, "Karaj", new Bank_info("Saderat", "12345"));
    }

    public static int updateCurrentUserProfile(String full_name, String phoneNumber, String email, String address, String profileImageBase64, Object bank_info) {
        //do the stuff in backend
        //YAML: Update current seller profile
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
                        System.out.println(base64String);

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
        return seller.getFullName();
    }

    public static String getPhoneNumber() {
        return seller.getMobile();
    }

    public static String getEmail() {
        return seller.getEmail();
    }

    public static String getProfileImageBase64() {
        //do the stuff in backend
        if (true /*profile picture is added (temporary)*/) {
            return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAIGNIUk0AAHomAACAhAAA+gAAAIDoA2AfURogAAAAJ3RFWHRkYXRlOmNyZWF0ZQAyMDIzLTA3LTIwVDEzOjE0OjQ2KzAyOjAwCnaixQAAACd0RVh0ZGF0ZTptb2RpZnkAMjAyMy0wNy0yMFQxMzoxNDo0NiswMjowME9PqWwAAASjSURBVHic7ZtPaBxlHMc/n3emk3Y2myQpMStpWjGJIbXqxSlIDy2i4FG8eFGSgwdBLz79R/TQRxBERQ8ePAgqOFgECx5qC4GgiJZaAn2k1VotSburmTTJzGSSSWb2fN4HZ/a9bGaTdLrJzL7zeZ/Pe5/n/T7zPj/vA+A9WlVTB1QVcFZp1gC9+mNZeunFK1/N8+f/vS3j90oHhJ4iYp/SgB9r8q+l4zVf04HwBwT2SgE+l2B/DkgnBI4iYj8n4A/Q+gI4U4GvS4DXbv8OOKeCzxCwBxTwZQE/L+A/xfk8s/o+Y8Azy/g5/M4EPoBgX0S+LgAvw/gK/L9A/q+SbwBfz7A70vgiwjYJwJ/K+D3Cvg1A//2+N9Dhm8+sL8FfCuAXzTw42n4wXy+5sP7v9PzHgi8DIFbEfDdBHwBga8i4PMI/HyAH/d+f6L+9Xb+KACP4fAHBO5CwCkT+LoAn0Xgxwg8vG9v9/fv/346f/8Cvr8P//Y/A2+WwM0IfFLApwD8ngL/sYd/N9D//r3/PgV8A4H/E/APBPg0Aq8C4GcF/v7g35+bT/5jAX4Vgf8u4DcCfE8CDyHwSgS+jICvIfBtBP4eAb+LgF8k8JMI/DOCfyrgLwD43xTwb/8b4D8P+F3APwfwfw/4fwP+XwD/D+B/R8A3EfAnBP5CwPt1+w9MvP8n8PMAfgjAPyHgnwj4XwL+3wz8f/7+LwL/KODvKfDfA/7rD/5fgP/k9L+f+f4bAP4fgH8g4B8K+E0CPkvArxA4CgErhSA0hQBVGkCjHkC1PjG/qA3uXwIqfAIqfAKq5xKoeQhUfAQaHgK1T0DtU6D5Eaj5KDT/BGo+A83nw0DVg0D1INB8CjQ/As2HQPWRgNpHQPMT0PwEah4C1YdA9SFQ/QhU/wI1HwLNI6D5CFR/As1voOYzUH0I1HwINP+l/xCo+b/x7/9f/p/k7f8ANb8Hmp+Bmg+h5gPzH2O+f4Oa/8P/+z8Gah4ENQ+C5l+g5t/g7d8ANQ9C3cNQ9+D/DwBVB6D+P1AD/j8E6h4E6h4Eah4ENQ9C1YNA1YNA1YNA9RAg9e/g7d8AFQ8C1YNA1YNA1YNA1YNA1YNA1YNA9RAg9Q0g9Y0g9Y0g9Weg6jNQdRgk/zoo3gMlH0LJH2Oyfwpq/g00n4Oa/8w3fwdqvgM1H4Ka/wI1/wZqPqg4H1ScDysOB9UH1fdB1f3o77+BqneA6j1gukcMuoeA7iGgexDoHgS6hwDuIUA7BNAOAd2DgO5BQHcg0B0IdAdy7YFcB3JtB3L9BHL9BLJ/BNk/guyfQLZPITsEsnME2U6B7BhBNhGgTQQokwCFCZCkAbSJQHMKUKcBlEmAsAxgHwew9wD2nsHeA9h7APsewL4HsO8B7HuA/R7Afg/g/Q7wvgZ4nwM8xwEOccA+DuAcAThBAE4QgFME4BwBOMcBxjHAPQ7gHgdxDwM4hwHucYDzHOD8DvC+B3jfA9z3APc9wP0B8H4AeA8A3gOADwDgewD4HgA+BMCHAOgBADoAgA4AIAcAyAEAMgCAjAFgYwAYGMCWAbBlAFwZgAsDYGEAFgZgZQAuDMAFAbgQABcCIALAiABcEEARECCyASLNAIg0g0gTyDQDyDQByAgAyAAYyACYYAAWBgDDACgYAAWBAVAQAIUAUAgABVEA+EBEB0QEQERARDsCcSMQEwcxMYFIEIgEAYkQkBgBCQiQSAgQECAQAAICxIRAJAQIJECQgAQECAgQEAABIjEAJiYQMyEGMSMAcSMQEQERERARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARARAR-g/a-s7wS2T8yAAAAAASUVORK5CYII=";
        } else {
            return null; //don't change this!
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
}