package frontend.bemirfoodclient.controller.restaurant.seller;

import frontend.bemirfoodclient.model.entity.Item;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddItemToMenuDialogController {
    @FXML
    private VBox itemListVBox;

    // A list to keep track of the controllers for each selectable card
    private final List<SelectableItemCardController> cardControllers = new ArrayList<>();

    // This method is called by the parent to fill the dialog
    public void populateItems(List<Item> allItems) {
        for (Item item : allItems) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/frontend/bemirfoodclient/restaurant/seller/selectable-item-card.fxml"
                ));
                Pane card = loader.load();
                SelectableItemCardController cardController = loader.getController();
                cardController.setItemData(item);

                cardControllers.add(cardController); // Store the controller
                itemListVBox.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // This method returns the list of items that were checked
    public List<Item> getSelectedItems() {
        List<Item> selectedItems = new ArrayList<>();
        for (SelectableItemCardController controller : cardControllers) {
            if (controller.isSelected()) {
                selectedItems.add(controller.getItem());
            }
        }
        return selectedItems;
    }
}
