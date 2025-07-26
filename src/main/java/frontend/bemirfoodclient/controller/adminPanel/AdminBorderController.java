package frontend.bemirfoodclient.controller.adminPanel;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.TransactionCardController;
import frontend.bemirfoodclient.controller.adminPanel.card.AddCouponDialogController;
import frontend.bemirfoodclient.controller.adminPanel.card.AdminOrderCardController;
import frontend.bemirfoodclient.controller.adminPanel.card.CouponCardController;
import frontend.bemirfoodclient.controller.adminPanel.card.UserCardController;
import frontend.bemirfoodclient.model.entity.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AdminBorderController {
    public ImageView borderBemirfoodLogo;
    public ImageView searchIcon;
    public TextField searchTextField;
    public Region toolbarSpacer;
    public Button usersSmall;
    public Button usersLarge;
    public Button ordersSmall;
    public Button ordersLarge;
    public Button transactionsSmall;
    public Button transactionsLarge;
    public Button discountCodesSmall;
    public Button discountCodesLarge;
    public Button logoutSmall;
    public Button logoutLarge;
    public VBox contentVBox;
    public Button searchButton;
    public Button addCouponButton;
    public ImageView addCouponIcon;

    public String profileView;

    List<Coupon> coupons = new ArrayList<>();

    public void initialize() {
        borderBemirfoodLogo.setPreserveRatio(true);
        borderBemirfoodLogo.setFitHeight(40);

        searchIcon.setPreserveRatio(true);
        searchIcon.setFitHeight(17);

        addCouponIcon.setPreserveRatio(true);
        addCouponIcon.setFitHeight(30);

        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);
        usersButtonClicked();
    }

    public void searchButtonClicked() {
        switch (profileView){
            case "orders":
                ordersButtonClicked();
                break;
            case "transactions":
                transactionsButtonClicked();
                break;
            default:
                break;
        }
    }

    public void usersButtonClicked() {
        profileView = "users";

        searchButton.setVisible(false);
        searchTextField.setVisible(false);

        addCouponButton.setVisible(false);

        contentVBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4);");

        usersLarge.setVisible(true);
        ordersLarge.setVisible(false);
        transactionsLarge.setVisible(false);
        discountCodesLarge.setVisible(false);
        logoutLarge.setVisible(false);

        contentVBox.getChildren().clear();
        List<User> users = getUsers(); // Get user data from backend/factory

        for (User user : users) {
            try {
                // --- 3. Load the user-card.fxml for each user ---
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/adminPanel/card/user-card.fxml"
                ));
                Pane card = loader.load();

                // --- 4. Get the card's controller and set its data ---
                UserCardController controller = loader.getController();
                controller.setUser(user);

                controller.setOnDelete(userToDelete -> {
                    switch (deleteUser(user)) {
                        case 200:
                            usersButtonClicked();
                            break;
                        //show alerts
                    }
                });

                contentVBox.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        // Create a few sample users to display
        userList.add(new User("Alice", "09111111111", "buyer", "alice@email.com", null, "123 Apple St", null, "pass1"));
        userList.add(new User("Bob", "09222222222", "seller", "bob@email.com", null, "456 Burger Blvd", null, "pass2"));
        userList.add(new User("Charlie", "09333333333", "courier", "charlie@email.com", null, "789 Delivery Dr", null, "pass3"));
        return userList;
    }

    public void ordersButtonClicked() {
        profileView = "orders";

        searchButton.setVisible(true);
        searchTextField.setVisible(true);

        addCouponButton.setVisible(false);

        contentVBox.setStyle("-fx-background-color: transparent");

        usersLarge.setVisible(false);
        ordersLarge.setVisible(true);
        transactionsLarge.setVisible(false);
        discountCodesLarge.setVisible(false);
        logoutLarge.setVisible(false);

        contentVBox.getChildren().clear();

        List<Order> orders = getOrders(searchTextField.getText()); // Get user data from backend/factory

        for (Order order : orders) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/adminPanel/card/order-card.fxml"
                ));
                Pane card = loader.load();

                AdminOrderCardController controller = loader.getController();
                controller.setOrderData(order);

                contentVBox.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        searchTextField.setText("");
    }

    public List<Order> getOrders(String searchQuery) {
        List<Order> orders = new ArrayList<>();

        if (searchQuery == null || searchQuery.isEmpty()) {
            //temporary:
          /*List<Order> orders = new ArrayList<>();

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

            Delivery delivery1 = new Delivery("Dan Driver", "09555555555", "courier", "dan@email.com", null, "Delivery HQ", delivery1Bank, "pass5");
            Delivery delivery2 = new Delivery("Carol Carrier", "09666666666", "courier", "carol@email.com", null, "Delivery HQ", delivery2Bank, "pass6");

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
                    fixedDiscount, OrderStatus.waiting_vendor, 15000.0);
            order1.setDelivery(delivery1); // Manually set the delivery person
            orders.add(order1);

            // --- ORDER 2: A pending burger order with a percentage coupon ---
            List<CartItem> order2Items = List.of(new CartItem(classicBurger, 2), new CartItem(fries, 2));
            Order order2 = new Order(order2Items, customer2.getAddress(), customer2, burgerBarn,
                    LocalDateTime.now().minusHours(1), LocalDateTime.now(),
                    percentDiscount, OrderStatus.waiting_vendor, 18000.0);
            order2.setDelivery(delivery1); // Manually set the delivery person
            orders.add(order2);

            // --- ORDER 3: A cancelled pizza order with no coupon ---
            List<CartItem> order3Items = List.of(new CartItem(veggiePizza, 1));
            Order order3 = new Order(order3Items, customer1.getAddress(), customer1, pizzaPalace,
                    LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusHours(1),
                    OrderStatus.cancelled, 0.0);
            order3.setDelivery(delivery2); // Manually set the delivery person
            orders.add(order3);

            return orders;*/

            //do the stuff in backend
            //return all orders here
            return orders;
        } else {

            String searchText = "";
            String vendor = "";
            String courier = "";
            String customer = "";
            String status = "";

            String[] parts = searchQuery.split(",");

            if (parts.length > 0) searchText = parts[0].trim();
            if (parts.length > 1) vendor = parts[1].trim();
            if (parts.length > 2) courier = parts[2].trim();
            if (parts.length > 3) customer = parts[3].trim();
            if (parts.length > 4) status = parts[4].trim();

            //do the stuff in backend
            //searched query will be trimmed by commas ','

            //temporary:
            /*System.out.println(searchText);
            System.out.println(vendor);
            System.out.println(courier);
            System.out.println(customer);
            System.out.println(status);*/

            return orders;
        }
    }

    public void transactionsButtonClicked() {
        profileView = "transactions";
        searchButton.setVisible(true);
        searchTextField.setVisible(true);

        addCouponButton.setVisible(false);

        contentVBox.setStyle("-fx-background-color: transparent");

        usersLarge.setVisible(false);
        ordersLarge.setVisible(false);
        transactionsLarge.setVisible(true);
        discountCodesLarge.setVisible(false);
        logoutLarge.setVisible(false);

        contentVBox.getChildren().clear();

        List<Transaction> transactions = getTransactions(searchTextField.getText());

        for (Transaction transaction : transactions) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/profile/transaction-card.fxml"));
                Pane card = loader.load();
                TransactionCardController controller = loader.getController();
                controller.setData(transaction);

                card.setPadding(new Insets(0, 15, 0, 15));

                contentVBox.getChildren().add(card);

                Separator separator = new Separator();
                separator.setPadding(new Insets(15, 5, 15, 5));
                contentVBox.getChildren().add(separator);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        searchTextField.setText("");
    }

    public List<Transaction> getTransactions(String searchQuery) {
        List<Transaction> transactions = new ArrayList<>();
        if (searchQuery == null || searchQuery.isEmpty()) {
            //do the stuff in backend
            //return all orders here
            /*Bank_info seller1Bank = new Bank_info("Pizza Bank", "PB-111");
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

                    // --- ORDER 2: A pending burger order with a percentage coupon ---
                    List<CartItem> order2Items = List.of(new CartItem(classicBurger, 2), new CartItem(fries, 2));
                    Order order2 = new Order(order2Items, customer2.getAddress(), customer2, burgerBarn,
                            LocalDateTime.now().minusHours(1), LocalDateTime.now(),
                            percentDiscount, OrderStatus.finding_courier, 18000.0);
                    order2.setDelivery(null); // Manually set the delivery person
                    order2.setPaid(false);

                    // --- ORDER 3: A cancelled pizza order with no coupon ---
                    List<CartItem> order3Items = List.of(new CartItem(veggiePizza, 1));
                    Order order3 = new Order(order3Items, customer1.getAddress(), customer1, pizzaPalace,
                            LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusHours(1),
                            OrderStatus.on_the_way, 0.0);
                    order3.setDelivery(null); // Manually set the delivery person
                    order3.setPaid(false);

                    transactions.add(new Transaction(PaymentMethod.ONLINE, LocalDateTime.now(), PaymentStatus.SUCCESS, seller1, order1));
                    transactions.add(new Transaction(PaymentMethod.ONLINE, LocalDateTime.now(), PaymentStatus.FAILED, seller2, order2));
                    transactions.add(new Transaction(PaymentMethod.WALLET, LocalDateTime.now(), PaymentStatus.SUCCESS, seller1, order1));*/

            return transactions;
        } else {
            String searchText = "";
            String user = "";
            String method = "";
            String status = "";

            String[] parts = searchQuery.split(",");

            if (parts.length > 0) searchText = parts[0].trim();
            if (parts.length > 1) user = parts[1].trim();
            if (parts.length > 2) method = parts[2].trim();
            if (parts.length > 3) status = parts[3].trim();

            //do the stuff in backend
            //searched query will be trimmed by commas ','
        }
            return transactions;
    }

    public void discountCodesButtonClicked() {
        profileView = "discountCodes";
        searchButton.setVisible(false);
        searchTextField.setVisible(false);

        addCouponButton.setVisible(true);

        contentVBox.setStyle("-fx-background-color: transparent");

        usersLarge.setVisible(false);
        ordersLarge.setVisible(false);
        transactionsLarge.setVisible(false);
        discountCodesLarge.setVisible(true);
        logoutLarge.setVisible(false);

        contentVBox.getChildren().clear();

        List<Coupon> coupons = getCoupons();

        for (Coupon coupon : coupons) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                   "/frontend/bemirfoodclient/adminPanel/card/coupon-card.fxml"
                ));
                Pane card = loader.load();
                CouponCardController controller = loader.getController();
                controller.setCouponData(coupon);

                controller.setOnDelete(couponToDelete -> {
                    if (deleteCoupon(couponToDelete) == 200)
                        discountCodesButtonClicked(); // Refresh the list
                });

                // Set the action for the edit button
                controller.setOnEdit(couponToEdit -> {
                    if (updateCoupon(couponToEdit) == 200){
                        discountCodesButtonClicked(); // Refresh the list
                    }
                });

                contentVBox.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int updateCoupon(Coupon couponToEdit) {
        //do the stuff in backend
        return 200; //temporary
    }

    private int deleteCoupon(Coupon couponToDelete) {
        //do the stuff in backend
        return 200;
    }

    public List<Coupon> getCoupons() {

        return List.of(
                // An active percentage-based coupon that ends in a few days
                new Coupon(
                        "SUMMER25",
                        CouponType.percent,
                        25L,
                        50000L,
                        99L,
                        LocalDateTime.now().minusDays(10),
                        LocalDateTime.now().plusDays(5)
                ),

                // An active fixed-amount coupon that ends in a few hours
                new Coupon(
                        "WELCOME10K",
                        CouponType.fixed,
                        10000L,
                        0L,
                        1L,
                        LocalDateTime.now().minusHours(1),
                        LocalDateTime.now().plusHours(12)
                ),

                // An already expired coupon
                new Coupon(
                        "EXPIREDDEAL",
                        CouponType.fixed,
                        5000L,
                        0L,
                        0L,
                        LocalDateTime.now().minusDays(20),
                        LocalDateTime.now().minusDays(2)
                ),

                // An upcoming coupon that hasn't started yet
                new Coupon(
                        "NEXTWINTER",
                        CouponType.percent,
                        15L,
                        100000L,
                        500L,
                        LocalDateTime.now().plusDays(3),
                        LocalDateTime.now().plusDays(15)
                )
        );
    }

    @FXML
    public void logoutButtonClicked() {
        usersLarge.setVisible(false);
        ordersLarge.setVisible(false);
        transactionsLarge.setVisible(false);
        discountCodesLarge.setVisible(false);
        logoutLarge.setVisible(true);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));

        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        alert.setGraphic(null);
        Platform.runLater(() -> alert.getDialogPane().lookupButton(ButtonType.CANCEL).requestFocus());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            //do the stuff in backend
            //YAML: Logout User

            String homeDirectory = System.getProperty("user.dir");
            Path filePath = Path.of(homeDirectory, "registerTemp.txt");

            try {
                if (Files.exists(filePath)) {
                    Files.writeString(filePath, "");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Stage mainWindow = (Stage) usersSmall.getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/frontend/bemirfoodclient/login-view.fxml")));
                mainWindow.getScene().setRoot(root);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            switch (profileView){
                case "users":
                    usersButtonClicked();
                    break;
                case "orders":
                    ordersButtonClicked();
                    break;
                case "transactions":
                    transactionsButtonClicked();
                    break;
                case "discountCodes":
                    discountCodesButtonClicked();
                    break;
                default:
                    break;
            }
        }
    }

    public int deleteUser(User user) {
        //do the stuff in backend

        return 200;
    }

    public void addCouponButtonClicked() {
        try {
            // Load the NEW dialog FXML
            FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource("/frontend/bemirfoodclient/adminPanel/card/add-coupon-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            // Get the NEW dialog's controller
            AddCouponDialogController dialogController = loader.getController();

            // Create and configure the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add New Coupon");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Add Coupon");

            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                    "/frontend/bemirfoodclient/assets/Bemirfood_Logo.png"
            ))));

            AtomicReference<Coupon> newCoupon = new AtomicReference<>(new Coupon());
            Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            okButton.addEventFilter(ActionEvent.ACTION, event -> {
                newCoupon.set(dialogController.processResult());
                if (newCoupon.get() == null) {
                    event.consume();
                }
            });

            // Show the dialog and process the result
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (createCoupon(newCoupon.get()) == 200) // Call the backend logic
                    discountCodesButtonClicked(); // Refresh the view
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int createCoupon(Coupon coupon) {
        //do the stuff in backend
        return 200;
    }
}
