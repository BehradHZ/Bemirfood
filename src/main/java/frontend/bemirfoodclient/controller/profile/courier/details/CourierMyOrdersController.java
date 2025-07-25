package frontend.bemirfoodclient.controller.profile.courier.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.courier.DeliveryCardController;
import frontend.bemirfoodclient.model.entity.*;
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
        List<Order> orders = new ArrayList<>();

        //temporary

        // --- 1. Create Sellers and Customers ---
        Bank_info seller1Bank = new Bank_info("Pizza Bank", "PB-111");
        Bank_info seller2Bank = new Bank_info("Burger Bank", "BB-222");
        Bank_info customer1Bank = new Bank_info("User Bank", "CUST-333");
        Bank_info customer2Bank = new Bank_info("User Bank", "CUST-444");
        Bank_info delivery1Bank = new Bank_info("Courier Bank", "DEL-555");
        Bank_info delivery2Bank = new Bank_info("Courier Bank", "DEL-666");

        // --- 2. Create Sellers, Customers, and Deliveries ---
        Seller seller1 = new Seller("Pizza Pete", "09111111111", "pete@pizzapalace.com", null, "1 Pizza Plaza", seller1Bank, "pass1");
        Seller seller2 = new Seller("Bob Burger", "09222222222", "bob@burgerbarn.com", null, "2 Burger Blvd", seller2Bank, "pass2");

        Customer customer1 = new Customer("Alice Wonder", "09333333333", "buyer", "alice@email.com", null, "123 Apple St", customer1Bank, "pass3");
        Customer customer2 = new Customer("Charlie Bucket", "09444444444", "buyer", "charlie@email.com", null, "456 Chocolate Ave", customer2Bank, "pass4");

        Delivery delivery1 = new Delivery("Dan Driver", "09333311111", "courier", "dan@email.com", null, "Delivery HQ", delivery1Bank, "pass5");
        Delivery delivery2 = new Delivery("Carol Carrier", "09333322222", "courier", "carol@email.com", null, "Delivery HQ", delivery2Bank, "pass6");

        // --- 3. Create Restaurants ---
        Restaurant pizzaPalace = new Restaurant("Pizza Palace", seller1, "1 Pizza Plaza", "555-PIZZA", null, 0.09, 2000.0);
        Restaurant burgerBarn = new Restaurant("Burger Barn", seller2, "2 Burger Blvd", "555-BURG", null, 0.0, 3000.0);

        // --- 4. Create Items for each Restaurant ---
        Item pepperoniPizza = new Item("Pepperoni Pizza", null, "Classic pepperoni with mozzarella.", 250000.0, 50, List.of("pizza", "fast food"), pizzaPalace, 4.5);
        Item veggiePizza = new Item("Veggie Supreme", null, "Bell peppers, onions, and olives.", 220000.0, 40, List.of("pizza", "vegetarian"), pizzaPalace, 4.8);
        Item classicBurger = new Item("Classic Burger", null, "A juicy beef patty with lettuce and tomato.", 180000.0, 100, List.of("burger", "beef"), burgerBarn, 4.2);
        Item fries = new Item("Golden Fries", null, "Crispy golden french fries.", 50000.0, 200, List.of("side", "vegetarian"), burgerBarn, 4.0);

        // --- 5. Create Coupons ---
        Coupon fixedDiscount = new Coupon("SAVE10K", CouponType.fixed, 10000L, 100000L, 50L, LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5));
        Coupon percentDiscount = new Coupon("SAVE20", CouponType.percent, 20L, 50000L, 100L, LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(10));

        // ================================================================= //
        // ---           BUILD THE ORDERS                                --- //
        // ================================================================= //

        // --- ORDER 1: A completed pizza order with a fixed coupon ---
        List<CartItem> order1Items = List.of(new CartItem(pepperoniPizza, 1), new CartItem(veggiePizza, 1));
        Order order1 = new Order(order1Items, customer1.getAddress(), customer1, pizzaPalace,
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1),
                fixedDiscount, OrderStatus.finding_courier, 15000.0);
        order1.setDelivery(null); // Manually set the delivery person
        orders.add(order1);

        System.out.println(order1.getStatus());

        return orders;
    }

    public void showAllDeliveries() {
        List<Order> allOrders = getAllDeliveries();

        List<Order> activeDeliveries = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.on_the_way)
                .toList();

        List<Order> recommendedDeliveries = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.finding_courier)
                .toList();

        List<Order> historyList = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.completed || o.getStatus() == OrderStatus.cancelled)
                .toList();

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
