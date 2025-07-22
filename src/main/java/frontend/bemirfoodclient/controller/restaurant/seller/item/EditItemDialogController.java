package frontend.bemirfoodclient.controller.restaurant.seller.item;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.ImageLoader;
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

public class EditItemDialogController {

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

    private Item item;

    public void setItemData(Item item) {
        this.item = item;
        setScene();
    }

    public void setScene() {
        nameTextField.setPromptText(item.getName());
        descriptionTextField.setPromptText(item.getDescription());

        List<String> keywordsList = item.getKeywords();
        keywordTextField.setPromptText(String.join(", ", keywordsList));

        priceTextField.setPromptText(String.valueOf(item.getPrice()));
        supplyTextField.setPromptText(String.valueOf(item.getSupply()));

        setProfileImageBase64(addPhotoImageView);
    }

    public void initialize() {
        addPhotoImageView.setPreserveRatio(true);
        addPhotoImageView.setFitWidth(200);
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

    public Item editItem() {
        String name = item.getName();
        String description = item.getDescription();
        int supply = item.getSupply();
        double price = item.getPrice();
        List<String> keywordsList = item.getKeywords();

        if (!nameTextField.getText().isEmpty())
            name = nameTextField.getText();

        if (!descriptionTextField.getText().isEmpty())
            description = descriptionTextField.getText();

        if (!supplyTextField.getText().isEmpty())
            try {
                supply = Integer.parseInt(supplyTextField.getText());
            } catch (NumberFormatException e) {
                showAlert("Item Supply must be a valid number!");
                supplyTextField.requestFocus();
                return null;
            }

        if (!priceTextField.getText().isEmpty())
            try {
                price = Integer.parseInt(priceTextField.getText());
            } catch (NumberFormatException e) {
                showAlert("Item Price must be a valid number!");
                priceTextField.requestFocus();
                return null;
            }

        String keywordsInput = null;
        if (!keywordTextField.getText().isEmpty()) {
            keywordsInput = keywordTextField.getText();
            keywordsList = Arrays.stream(keywordsInput.split(",")) // 1. Split the string by commas into an array
                .map(String::trim)                // 2. Trim whitespace from each keyword
                .filter(keyword -> !keyword.isEmpty()) // 3. Remove any keywords that are now empty
                .toList();
        }

        System.out.printf("%s %s %s %s %s", name, description, supply, price, keywordsList);
        return new Item(
                name,
                imageUri,
                description,
                price,
                supply,
                keywordsList
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

    public void setProfileImageBase64(ImageView profileImageView) {
        //do the stuff in backend
        boolean profilePhotoExists = false;
        if (profilePhotoExists) {
            String base64Uri = null;
            ImageLoader.displayBase64Image(base64Uri, profileImageView);
        } else {
            profileImageView.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("" +
                    "/frontend/bemirfoodclient/assets/icons/addItem.png"))));
        }
    }
}
