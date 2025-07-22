package frontend.bemirfoodclient.controller.restaurant.seller.item;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddItemDialogController {

    @FXML
    public Button addPhotoButton;
    @FXML
    public ImageView addPhotoImageView;
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField descriptionTextField;
    @FXML
    public TextField keywordTextField;
    @FXML
    public TextField priceTextField;
    @FXML
    public TextField supplyTextField;

    private String imageUri;

    public void initialize() {
        addPhotoImageView.setPreserveRatio(true);
        addPhotoImageView.setFitWidth(200);
        addPhotoImageView.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class
                .getResourceAsStream(
                        "/frontend/bemirfoodclient/assets/icons/addItem.png"
                ))));
    }

    @FXML
    public void addPhotoButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Item Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        Stage stage = (Stage) addPhotoImageView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imageUri = selectedFile.toURI().toString();
            addPhotoImageView.setImage(new Image(selectedFile.toURI().toString()));
        }

    }

    public Item addNewItem() {
        if (nameTextField.getText().isEmpty()) {
            showAlert("Item Name cannot be empty!");
            nameTextField.requestFocus();
            return null;
        }
        if (descriptionTextField.getText().isEmpty()) {
            showAlert("Item Description cannot be empty!");
            descriptionTextField.requestFocus();
            return null;
        }

        int supply = 0;
        if (supplyTextField.getText().isEmpty()) {
            showAlert("Item Supply cannot be empty!");
            supplyTextField.requestFocus();
            return null;
        } else {
            try {
                supply = Integer.parseInt(supplyTextField.getText());
            } catch (NumberFormatException e) {
                showAlert("Item Supply must be a valid number!");
                supplyTextField.requestFocus();
                return null;
            }
        }

        double price = 0.0;
        if (priceTextField.getText().isEmpty()) {
            showAlert("Item Price cannot be empty!");
            priceTextField.requestFocus();
            return null;
        } else {
            try {
                price = Double.parseDouble(priceTextField.getText());
            } catch (NumberFormatException e) {
                showAlert("Item Price must be a valid number!");
                priceTextField.requestFocus();
                return null;
            }
        }

        String keywordsInput = keywordTextField.getText();
        List<String> keywords = Arrays.stream(keywordsInput.split(",")) // 1. Split the string by commas into an array
                .map(String::trim)                // 2. Trim whitespace from each keyword
                .filter(keyword -> !keyword.isEmpty()) // 3. Remove any keywords that are now empty
                .toList();

        return new Item(
            nameTextField.getText(),
            imageUri,
            descriptionTextField.getText(),
            price,
            supply,
            keywords
        );
    }

    public void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                "assets/icons/error.png"))));
        alert.setTitle("Adding item failed");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }
}
