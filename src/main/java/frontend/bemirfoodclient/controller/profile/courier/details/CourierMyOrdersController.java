package frontend.bemirfoodclient.controller.profile.courier.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.courier.DeliveryCardController;
import frontend.bemirfoodclient.model.entity.Bank_info;
import frontend.bemirfoodclient.model.entity.Delivery;
import frontend.bemirfoodclient.model.entity.Order;
import frontend.bemirfoodclient.model.entity.OrderStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static BuildEntity.builder.buildOrderList;
import static HttpClientHandler.Requests.getAvailableDeliveryOrders;
import static HttpClientHandler.Requests.searchDeliveryHistory;

public class CourierMyOrdersController {
    @FXML
    public VBox activeDeliverySection;
    @FXML
    public ScrollPane activeScroll;
    @FXML
    public Separator noActiveOrder;
    @FXML
    public ScrollPane recommendedScroll;
    @FXML
    public VBox recommendedDeliveryList;
    @FXML
    public Separator noDeliveryOrder;
    @FXML
    public ScrollPane historyScroll;
    @FXML
    public VBox historyDeliveryList;
    @FXML
    public Separator noHistoryOrder;

    public void initialize() {
        showAllDeliveries();
    }

    public List<Order> getAllDeliveries() {
        return buildOrderList(args -> getAvailableDeliveryOrders(), "List of available deliveries", "Failed to get available orders");
    }

    public void showAllDeliveries() {
        List<Order> allOrders = getAllDeliveries();

        List<Order> recommendedDeliveries =
                buildOrderList(args -> getAvailableDeliveryOrders(),
                        "List of available deliveries", "Failed to get available orders");


        Map<String, String> queryParams = new HashMap<>();
        List<Order> historyAndRecommendedOrders =
                buildOrderList(args -> searchDeliveryHistory((Map) args[0]), "List of completed and active deliveries", "Failed to get history", queryParams);

        List<Order> activeDeliveries = historyAndRecommendedOrders.stream().filter(o -> o.getStatus().equals(OrderStatus.on_the_way) || o.getStatus().equals(OrderStatus.accepted)).toList();

        List<Order> historyList = historyAndRecommendedOrders.stream().filter(o -> !o.getStatus().equals(OrderStatus.on_the_way) && !o.getStatus().equals(OrderStatus.accepted)).toList();

        activeDeliverySection.getChildren().clear();
        if (activeDeliveries.isEmpty()) {
            activeScroll.setPrefHeight(30);
            noActiveOrder.setVisible(true);
        } else {
            noActiveOrder.setVisible(false);
            for (Order order : activeDeliveries) {
                activeDeliverySection.getChildren().add(createDeliveryCard(order));
            }
        }

        recommendedDeliveryList.getChildren().clear();
        if (recommendedDeliveries.isEmpty()) {
            recommendedScroll.setPrefHeight(30);
            noDeliveryOrder.setVisible(true);
        } else {
            noDeliveryOrder.setVisible(false);
            for (Order order : recommendedDeliveries) {
                recommendedDeliveryList.getChildren().add(createDeliveryCard(order));
            }
        }

        historyDeliveryList.getChildren().clear();
        if (historyList.isEmpty()) {
            historyScroll.setPrefHeight(30);
            noHistoryOrder.setVisible(true);
        } else {
            noHistoryOrder.setVisible(false);
            for (Order order : historyList) {
                historyDeliveryList.getChildren().add(createDeliveryCard(order));
            }
        }
    }

    private Pane createDeliveryCard(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                    "/frontend/bemirfoodclient/restaurant/courier/delivery-card.fxml"
            ));
            Pane card = loader.load();
            DeliveryCardController controller = loader.getController();
            controller.setOrderData(order);

            Bank_info delivery1Bank = new Bank_info("Courier Bank", "DEL-555");
            Delivery delivery = new Delivery("Dan Driver", "09333311111", "courier",
                    "dan@email.com", null, "Delivery HQ", delivery1Bank, "pass5");


            controller.setOnAccept(acceptedOrder -> {
                switch (deliveryAccept(acceptedOrder)) {
                    case 200:
                        showAllDeliveries();
                        break;
                    //show alerts
                }
            });

            controller.setOnStatusChange((orderToUpdate, newStatus) -> {
                switch (deliveryStatusChange(orderToUpdate, newStatus)) {
                    case 200:
                        showAllDeliveries();
                        break;
                    //show alerts
                }
            });

            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null or an empty pane on error
        }
    }

    public int deliveryAccept(Order order) {
        //do the stuff in backend

        return 200; //temporary
    }

    public int deliveryStatusChange(Order order, OrderStatus newStatus) {
        //do the stuff in backend

        return 200; //temporary
    }
}
