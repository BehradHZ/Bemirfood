package frontend.bemirfoodclient.controller.adminPanel;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import HttpClientHandler.Requests;
import Util.Token;
import com.google.gson.*;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static BuildEntity.builder.buildOrderList;
import static BuildEntity.builder.buildTransactionList;
import static HttpClientHandler.Requests.*;
import static exception.exp.expHandler;

public class AdminBorderController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();

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
                     HttpResponseData response = deleteUser(user);
                     if(response.getStatusCode() != 200) expHandler(response, "Failed to remove user", null);
                });

                contentVBox.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        HttpResponseData response = getUserAdmin();
        JsonArray jsonArray = response.getBody().get("List of users").getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            User user = gson.fromJson(jsonObject, User.class);
            user.setId(jsonObject.get("id").getAsLong());
            user.setRole(UserRole.valueOf(jsonObject.get("role").getAsString().toUpperCase()));
            if(user.getRole() !=  UserRole.ADMIN){
                userList.add(user);
            }
        }
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
        Map<String, String> query = new HashMap<>();
        if(searchQuery != null && !searchQuery.isEmpty()) query.put("search", searchQuery);
        return buildOrderList(args ->
                searchOrderHistoryAdmin((Map) args[0]),
                "List of orders",
                "Failed to find orders",
                query);
    }

    public void transactionsButtonClicked() {
        System.out.println("search button clicked");
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

        System.out.println("List of transactions : ");
        for (Transaction transaction : transactions) {
            try {
                System.out.println(transaction.getId() + " : " + transaction.getPaymentMethod());
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
        System.out.println("get transactions method invoked, Search Query: " + searchQuery);
        String searchText = "";
        String user = "";
        String method = "";
        String status = "";

        String[] parts = searchQuery.split(",");

        if (parts.length > 0) searchText = parts[0].trim();
        if (parts.length > 1) user = parts[1].trim();
        if (parts.length > 2) method = parts[2].trim();
        if (parts.length > 3) status = parts[3].trim();

        Map<String, String> queryParams = new HashMap<>();
        if(!searchText.isEmpty())queryParams.put("search", searchText);
        if(!user.isEmpty())queryParams.put("user", user);
        if(!method.isEmpty())queryParams.put("method", method);
        if(!status.isEmpty())queryParams.put("status", status);

        return buildTransactionList(
                args -> searchTransactionAdmin((Map) args[0]),
                "List of financial transactions",
                "Failed to get Transactions",
                queryParams
                );
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
                    HttpResponseData responseData = deleteCoupon(couponToDelete);
                    if(responseData.getStatusCode() == 200) {
                        discountCodesButtonClicked();
                    }else{
                        expHandler(responseData, "Failed to remove coupon", null);
                    }
                });

                // Set the action for the edit button
                controller.setOnEdit(couponToEdit -> {
                    HttpResponseData response = updateCoupon(couponToEdit);
                    if (response.getStatusCode() == 200) {
                        discountCodesButtonClicked();
                    }else{
                        expHandler(response, "Failed to update coupon", null);
                    }
                });

                contentVBox.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private HttpResponseData updateCoupon(Coupon couponToEdit) {
        Map<String, Object> request = new HashMap<>();
        if(couponToEdit.getCode() != null) request.put("coupon_code", couponToEdit.getCode());
        if(couponToEdit.getCode() != null) request.put("type", couponToEdit.getType());
        if(couponToEdit.getValue() != 0) request.put("value", couponToEdit.getValue());
        if(couponToEdit.getMinPrice() != null) request.put("min_price", couponToEdit.getMinPrice());
        if(couponToEdit.getUserCount() != null) request.put("user_count", couponToEdit.getUserCount());
        if(couponToEdit.getStartDate() != null)
            request.put("start_date", LocalDateTimeAdapter.DateTimeToString(couponToEdit.getStartDate()));
        if(couponToEdit.getEndDate() != null)
            request.put("end_date", LocalDateTimeAdapter.DateTimeToString(couponToEdit.getEndDate()));

        return updateCouponAdmin(gson.toJson(request),  couponToEdit.getId());
    }

    private HttpResponseData deleteCoupon(Coupon couponToDelete) {
        return removeCouponAdmin(couponToDelete.getId());
    }

    public List<Coupon> getCoupons() {
        List<Coupon> coupons = new ArrayList<>();
        HttpResponseData response = Requests.getCouponsAdmin();
        JsonArray array = response.getBody().get("List of all coupons").getAsJsonArray();
        for (JsonElement element : array) {
            JsonObject couponObj = element.getAsJsonObject();
            Coupon coupon = gson.fromJson(couponObj, Coupon.class);
            coupon.setStartDate(LocalDateTimeAdapter.StringToDateTime(couponObj.get("start_date").getAsString()));
            coupon.setEndDate(LocalDateTimeAdapter.StringToDateTime(couponObj.get("end_date").getAsString()));

            coupons.add(coupon);
        }
        return coupons;
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

            Token.clearFileContent();


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

    public HttpResponseData deleteUser(User user) {
       return removeUserAdmin(user.getId());
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
                HttpResponseData response = createCoupon(newCoupon.get());
                if(response.getStatusCode() == 200){
                    discountCodesButtonClicked();
                }else{
                    expHandler(response, "Failed to get coupons", null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpResponseData createCoupon(Coupon coupon) {
        Map<String, Object> request = new HashMap<>();
        request.put("coupon_code", coupon.getCode());
        request.put("type", coupon.getType());
        request.put("value", coupon.getValue());
        request.put("min_price", coupon.getMinPrice());
        request.put("user_count",  coupon.getUserCount());
        request.put("start_date", LocalDateTimeAdapter.DateTimeToString(coupon.getStartDate()));
        request.put("end_date", LocalDateTimeAdapter.DateTimeToString(coupon.getStartDate()));
        return Requests.addCouponAdmin(gson.toJson(request));
    }
}
