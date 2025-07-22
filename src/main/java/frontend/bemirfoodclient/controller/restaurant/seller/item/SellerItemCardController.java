package frontend.bemirfoodclient.controller.restaurant.seller.item;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class SellerItemCardController {

    @FXML
    public ImageView itemImage;
    @FXML
    public Label itemName;
    @FXML
    public Label itemDescription;
    @FXML
    public Label itemPrice;
    @FXML
    public Label itemRating;
    @FXML
    public ImageView ratingStarImage;
    @FXML
    public Label itemKeywords;

    private Item item;
    private Consumer<Item> deleteCallback;
    private Consumer<Item> editCallback;



    public void setItemData(Item item) {
        this.item = item;
        setScene();
    }

    public void initialize() {
        itemImage.setPreserveRatio(true);
        itemImage.setFitHeight(120);
        ratingStarImage.setPreserveRatio(true);
        ratingStarImage.setFitHeight(18);
    }

    public void setScene() {
        setItemPhoto();
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemPrice.setText("$" + String.valueOf(item.getPrice()));
        itemRating.setText(String.valueOf(item.getRating()));

        List<String> keywordsList = item.getKeywords();
        itemKeywords.setText(String.join(", ", keywordsList));
    }

    private void setItemPhoto() {
        //do the stuff in backend
        boolean profilePhotoExists = false;
        if (profilePhotoExists) {
            String base64Uri = null;
            ImageLoader.displayBase64Image(base64Uri, itemImage);
        } else {
            itemImage.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("" +
                    "/frontend/bemirfoodclient/assets/icons/addItem.png"))));
        }
    }

    public void setOnEdit(Consumer<Item> callback) {
        this.editCallback = callback;
    }

    public void editItemButtonClicked() {
        if (editCallback != null) {
            editCallback.accept(this.item);
        }
    }

    public void deleteItemButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                "/frontend/bemirfoodclient/assets/icons/deleteRed.png"))));

        alert.setTitle("Delete Item");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this item?");
        alert.getDialogPane().setGraphic(null);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (deleteCallback != null) {
                deleteCallback.accept(item);
            }
        }
    }

    public void setOnDelete(Consumer<Item> callback) {
        this.deleteCallback = callback;
    }
}
