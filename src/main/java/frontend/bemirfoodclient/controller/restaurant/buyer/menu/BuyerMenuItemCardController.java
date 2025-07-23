package frontend.bemirfoodclient.controller.restaurant.buyer.menu;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class BuyerMenuItemCardController {

    @FXML
    public ImageView itemImage;
    @FXML
    public Label itemName;
    @FXML
    public Region spacer;
    @FXML
    public ImageView deleteItemImageView;

    private Item item;
    private Consumer<Item> deleteCallback;

    public void setItemData(Item item) {
        this.item = item;
        setScene();
    }

    public void initialize() {
        itemImage.setPreserveRatio(true);
        itemImage.setFitHeight(70);

        HBox.setHgrow(spacer, Priority.ALWAYS);

        deleteItemImageView.setPreserveRatio(true);
        deleteItemImageView.setFitHeight(30);
    }

    public void setScene() {
        setItemPhoto();
        itemName.setText(item.getName());
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

    public void deleteItemButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                "/frontend/bemirfoodclient/assets/icons/deleteRed.png"))));

        alert.setTitle("Remove Item");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to remove this item form this menu?");
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


