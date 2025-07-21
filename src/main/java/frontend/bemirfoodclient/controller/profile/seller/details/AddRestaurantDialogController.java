package frontend.bemirfoodclient.controller.profile.seller.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Restaurant;
import frontend.bemirfoodclient.model.entity.Seller;
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

public class AddRestaurantDialogController {

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

    public void initialize() {
        addLogoImageView.setPreserveRatio(true);
        addLogoImageView.setFitWidth(200);
        addLogoImageView.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                "/frontend/bemirfoodclient/assets/icons/addRestaurant.png"
        ))));
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
        }
        addLogoImageView.setImage(new Image(selectedFile.toURI().toString()));
    }

    public Restaurant addRestaurant(String sellerMobile) {
        if (nameTextField.getText().isEmpty()) {
            showAlert("Restaurant Name cannot be empty!");
            nameTextField.requestFocus();
            return null;
        }
        if (addressTextField.getText().isEmpty()) {
            showAlert("Restaurant Address cannot be empty!");
            addressTextField.requestFocus();
            return null;
        }
        if (phoneTextField.getText().isEmpty()) {
            showAlert("Restaurant Phone cannot be empty!");
            phoneTextField.requestFocus();
            return null;
        }

        double taxFee = 0;
        double additionalFee = 0;

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
                nameTextField.getText(),
                new Seller(sellerMobile),
                addressTextField.getText(),
                phoneTextField.getText(),
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
