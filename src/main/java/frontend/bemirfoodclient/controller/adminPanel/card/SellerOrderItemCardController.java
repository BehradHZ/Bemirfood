package frontend.bemirfoodclient.controller.adminPanel.card;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.CartItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.Objects;

public class SellerOrderItemCardController {

    @FXML
    public ImageView itemImage;
    @FXML
    public Label itemName;
    @FXML
    public Region spacer;
    @FXML
    public Label itemQuantity;

    private CartItem cartItem;

    public void setItemData(CartItem cartItem) {
        this.cartItem = cartItem;
        setScene();
    }

    public void initialize() {
        itemImage.setPreserveRatio(true);
        itemImage.setFitHeight(50);

        HBox.setHgrow(spacer, Priority.ALWAYS);
    }

    public void setScene() {
        setItemPhoto();
        itemName.setText(cartItem.getItem().getName());
        itemQuantity.setText(String.valueOf(cartItem.getQuantity()));
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
}


