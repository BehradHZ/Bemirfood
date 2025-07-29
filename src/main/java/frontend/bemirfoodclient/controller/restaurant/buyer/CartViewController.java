package frontend.bemirfoodclient.controller.restaurant.buyer;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static BuildEntity.builder.buildOrderList;
import static HttpClientHandler.Requests.searchOrderHistoryCustomer;

public class CartViewController {

    @FXML
    public VBox cartsSection;

    public void initialize() {
        setScene();
    }

    public void setScene() {
        cartsSection.getChildren().clear();

        List<Order> allOrders = getUnpaidOrders();
        
        List<Order> unpaidOrders = allOrders.stream()
                .filter(order -> !order.isPaid())
                .toList();

        for (Order order : unpaidOrders) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/buyer/cart-card.fxml"
                ));
                Pane card = loader.load();
                CartCardController controller = loader.getController();
                controller.setOrderData(order);

                controller.setOnPaymentSuccess(paidOrder -> {
                    cartsSection.getChildren().remove(card);
                    setScene();
                });

                cartsSection.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Order> getUnpaidOrders() {
        Map<String, String> queryParams = new HashMap<>();
        List<Order> orders = buildOrderList(args -> searchOrderHistoryCustomer((Map) args[0]),
                "List of past orders", "Failed to load past orders", queryParams);
        orders = orders.stream().
                filter(order -> !order.isPaid()).toList();

        return orders;
    }

}
