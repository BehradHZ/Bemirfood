package frontend.bemirfoodclient.controller.profile.buyer.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.buyer.order.BuyerOrderCardController;
import frontend.bemirfoodclient.model.entity.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

import static BuildEntity.builder.buildOrderList;
import static HttpClientHandler.Requests.getCustomerOrders;

public class BuyerOrdersController {

    @FXML
    private VBox ordersVBox;

    @FXML
    public void initialize() {

        List<Order> orders = getOrders();

        // 2. Loop through each order
        for (Order order : orders) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/buyer/order/order-card.fxml"
                ));
                Pane card = loader.load();

                BuyerOrderCardController cardController = loader.getController();
                cardController.setOrderData(order);

                ordersVBox.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private List<Order> getOrders() {
        return buildOrderList(args -> getCustomerOrders(), "List of past orders", "Failed to get orders");
    }

}