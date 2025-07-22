package frontend.bemirfoodclient.controller.restaurant.seller;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Item;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class SelectableItemCardController {

    @FXML
    private CheckBox selectCheckBox;
    @FXML private ImageView itemImageView;
    @FXML private Label itemNameLabel;

    private Item item;

    public void setItemData(Item item) {
        this.item = item;
        itemNameLabel.setText(item.getName());
        setItemPhoto();
    }

    private void setItemPhoto() {
        //do the stuff in backend
        boolean profilePhotoExists = false;
        if (profilePhotoExists) {
            String base64Uri = null;
            ImageLoader.displayBase64Image(base64Uri, itemImageView);
        } else {
            itemImageView.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("" +
                    "/frontend/bemirfoodclient/assets/icons/addItem.png"))));
        }
    }

    public boolean isSelected() {
        return selectCheckBox.isSelected();
    }

    public Item getItem() {
        return this.item;
    }

    public void select() {
        if (selectCheckBox.isSelected()) {
            selectCheckBox.setSelected(false);
        } else {
            selectCheckBox.setSelected(true);
        }
    }
}
