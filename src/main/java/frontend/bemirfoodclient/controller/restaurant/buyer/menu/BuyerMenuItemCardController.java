package frontend.bemirfoodclient.controller.restaurant.buyer.menu;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.Objects;

public class BuyerMenuItemCardController {

    @FXML
    public ImageView itemImage;
    @FXML
    public Label itemName;
    @FXML
    public Region spacer;
    @FXML
    public Label itemPrice;
    @FXML
    public Label quantity;
    public Button minus;
    public Button plus;
    public HBox addToCart;

    private Item item;

    public void setItemData(Item item) {
        this.item = item;
        setScene();
    }

    public void initialize() {
        itemImage.setPreserveRatio(true);
        itemImage.setFitHeight(70);

        HBox.setHgrow(spacer, Priority.ALWAYS);
    }

    public void setScene() {
        setItemPhoto();
        itemName.setText(item.getName());
        itemPrice.setText(String.valueOf(item.getPrice()) + " toomans   ");
        if (getItemQuantity() == 0) {
            addToCart.setVisible(true);
            addToCart.setManaged(true);
            plus.setVisible(false);
            minus.setVisible(false);
        } else {
            addToCart.setVisible(false);
            addToCart.setManaged(false);
            plus.setVisible(true);
            minus.setVisible(true);
        }

        quantity.setText(String.valueOf(getItemQuantity()));    }

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

    public int getItemQuantity() {
        //do the stuff in backend

        return 5; //temporary
    }

    public void addItemToCart() {
        //do the stuff in backend
    }

    @FXML
    public void plusButtonClicked() {
        //do the stuff in backend
    }

    @FXML
    public void minusButtonClicked() {
        //do the stuff in backend
    }
}


