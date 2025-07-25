package frontend.bemirfoodclient.controller.restaurant.buyer;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CartViewController {

    @FXML
    public VBox cartsSection;

    public void initialize() {
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
                });

                cartsSection.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Order> getUnpaidOrders() {
        List<Order> orders = new ArrayList<>();
        //do the stuff in backend
        //اوردر هایی رئ بده که به اسم این کاربر هستن
        //اگه فیلتر کردی فیلتر منو بردار

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
//        Delivery delivery2 = new Delivery("Carol Carrier", "09333322222", "courier", "carol@email.com", null, "Delivery HQ", delivery2Bank, "pass6");

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
        order1.setPaid(false);
        System.out.println(order1.getStatus());
        orders.add(order1);

        // --- ORDER 2: A pending burger order with a percentage coupon ---
        List<CartItem> order2Items = List.of(new CartItem(classicBurger, 2), new CartItem(fries, 2));
        Order order2 = new Order(order2Items, customer2.getAddress(), customer2, burgerBarn,
                LocalDateTime.now().minusHours(1), LocalDateTime.now(),
                percentDiscount, OrderStatus.finding_courier, 18000.0);
        order2.setDelivery(null); // Manually set the delivery person
        order2.setPaid(false);
        orders.add(order2);

        // --- ORDER 3: A cancelled pizza order with no coupon ---
        List<CartItem> order3Items = List.of(new CartItem(veggiePizza, 1));
        Order order3 = new Order(order3Items, customer1.getAddress(), customer1, pizzaPalace,
                LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusHours(1),
                OrderStatus.on_the_way, 0.0);
        order3.setDelivery(null); // Manually set the delivery person
        order3.setPaid(false);
        orders.add(order3);

        return orders;
    }

}
