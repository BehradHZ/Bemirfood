package frontend.bemirfoodclient.controller.restaurant.seller;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Restaurant;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class EditRestaurantDialogController {
    @FXML
    public Button addLogoButton;
    @FXML
    public ImageView addLogoImageView;
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField addressTextField;
    @FXML
    public TextField phoneTextField;
    @FXML
    public TextField taxFeeTextField;
    @FXML
    public TextField additionalFeeTextField;

    private String imageUri;

    private Restaurant restaurant;

    public void setRestaurantData(Restaurant restaurant) {
        this.restaurant = restaurant;
        setScene();
    }

    public void setScene() {
        nameTextField.setPromptText(restaurant.getName());
        addressTextField.setPromptText(restaurant.getAddress());
        phoneTextField.setPromptText(restaurant.getPhone());

        if (restaurant.getTaxFee() != null)
            taxFeeTextField.setPromptText(String.valueOf(restaurant.getTaxFee()));

        if (restaurant.getAdditionalFee() != null)
            additionalFeeTextField.setPromptText(String.valueOf(restaurant.getAdditionalFee()));

        setProfileImageBase64(addLogoImageView);
    }

    public void setProfileImageBase64(ImageView profileImageView) {
        //do the stuff in backend
        boolean profilePhotoExists = false;
        if (profilePhotoExists) {
            String base64Uri = null;
            ImageLoader.displayBase64Image(base64Uri, profileImageView);
        } else {
            profileImageView.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("" +
                    "/frontend/bemirfoodclient/assets/icons/addRestaurant.png"))));
        }
    }

    public void initialize() {
        addLogoImageView.setPreserveRatio(true);
        addLogoImageView.setFitWidth(200);
    }

    @FXML
    public void addLogoButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        Stage stage = (Stage) addLogoImageView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imageUri = selectedFile.toURI().toString();
            addLogoImageView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    public Restaurant editRestaurant() {
        String name = restaurant.getName();
        String address = restaurant.getAddress();
        String phone = restaurant.getPhone();
        double taxFee = restaurant.getTaxFee();
        double additionalFee = restaurant.getAdditionalFee();

        if (!nameTextField.getText().isEmpty())
            name = nameTextField.getText();
        if (!addressTextField.getText().isEmpty())
            address = addressTextField.getText();
        if (!phoneTextField.getText().isEmpty())
            phone = phoneTextField.getText();

        if (!taxFeeTextField.getText().isEmpty()) {
            try {
                taxFee = Double.parseDouble(taxFeeTextField.getText());
            } catch (NumberFormatException e) {
                showAlert("Tax fee must be a valid number.");
                taxFeeTextField.requestFocus();
                return null;
            }
        }

        if (!additionalFeeTextField.getText().isEmpty()) {
            try {
                additionalFee = Double.parseDouble(additionalFeeTextField.getText());
            } catch (NumberFormatException e) {
                showAlert("Additional fee must be a valid number.");
                additionalFeeTextField.requestFocus();
                return null;
            }
        }

        return new Restaurant(
                name,
                restaurant.getSeller(),
                address,
                phone,
                imageUri,
                taxFee,
                additionalFee
        );
    }

    public void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                "assets/icons/error.png"))));
        alert.setTitle("Login failed");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }
}
