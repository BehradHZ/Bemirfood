package frontend.bemirfoodclient.controller.restaurant.seller.order;

import frontend.bemirfoodclient.model.entity.OrderStatus;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ChangeOrderStatusDialogController {

    @FXML
    private VBox statusButtonsContainer;

    private OrderStatus selectedStatus;

    /**
     * Initializes the dialog by creating a button for each OrderStatus enum constant.
     * Each button is styled and configured to set the selected status and close the dialog on click.
     */
    public void initialize() {

    }

    public OrderStatus getSelectedStatus() {
        return selectedStatus;
    }
}
