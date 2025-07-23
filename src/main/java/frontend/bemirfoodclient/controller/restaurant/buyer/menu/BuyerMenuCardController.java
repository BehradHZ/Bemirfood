package frontend.bemirfoodclient.controller.restaurant.buyer.menu;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Item;
import frontend.bemirfoodclient.model.entity.Menu;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class BuyerMenuCardController {

    @FXML
    public Label menuTitle;
    @FXML
    public VBox menuItemsSection;

    private Menu menu;

    public void setMenuData(Menu menu) {
        this.menu = menu;
        setScene();
    }

    public void initialize() {

    }

    public void setScene() {
        menuTitle.setText(menu.getTitle());

        menuItemsSection.getChildren().clear();

        List<Item> items = menu.getItems();

        for (Item item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/buyer/menu/item-card-small-menu.fxml"
                ));
                Pane smallCard = loader.load();
                BuyerMenuItemCardController smallCardController = loader.getController();
                smallCardController.setItemData(item);

                menuItemsSection.getChildren().add(smallCard);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
