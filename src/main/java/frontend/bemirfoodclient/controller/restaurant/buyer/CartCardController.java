package frontend.bemirfoodclient.controller.restaurant.buyer;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import HttpClientHandler.Requests;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.PaymentGatewayDialogController;
import frontend.bemirfoodclient.controller.restaurant.buyer.order.BuyerOrderItemCardController;
import frontend.bemirfoodclient.model.entity.CartItem;
import frontend.bemirfoodclient.model.entity.Coupon;
import frontend.bemirfoodclient.model.entity.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static HttpClientHandler.Requests.validateCoupon;
import static exception.exp.expHandler;

public class CartCardController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();


    @FXML
    public VBox itemsInTheCart;
    @FXML
    public Label restaurantName;
    @FXML
    public Label orderAddress;
    @FXML
    public Region couponCodeSpacer;
    @FXML
    public TextField couponTextField;
    @FXML
    public Region rawPriceSpacer;
    @FXML
    public Label rawPrice;
    @FXML
    public Region taxFeeSpacer;
    @FXML
    public Label taxFee;
    @FXML
    public Region additionalFeeSpacer;
    @FXML
    public Label additionalFee;
    @FXML
    public Region courierFeeSpacer;
    @FXML
    public Label courierFee;
    @FXML
    public Region couponDetailsSpacer;
    @FXML
    public Label couponDetails;
    @FXML
    public Region totalPriceSpacer;
    @FXML
    public Label totalPrice;
    @FXML
    public RadioButton onlineToggle;
    @FXML
    public RadioButton walletToggle;
    @FXML
    public Button payButton;
    @FXML
    public ToggleGroup methodToggleGroup;
    @FXML
    public ImageView applyCouponIcon;

    private Order order;

    private Consumer<Order> paymentSuccessCallback;

    public void setOnPaymentSuccess(Consumer<Order> callback) {
        this.paymentSuccessCallback = callback;
    }

    public void setOrderData(Order order) {
        this.order = order;
        setScene();
    }

    public void initialize() {
        onlineToggle.setSelected(true);

        HBox.setHgrow(couponCodeSpacer, Priority.ALWAYS);
        HBox.setHgrow(rawPriceSpacer, Priority.ALWAYS);
        HBox.setHgrow(taxFeeSpacer, Priority.ALWAYS);
        HBox.setHgrow(additionalFeeSpacer, Priority.ALWAYS);
        HBox.setHgrow(courierFeeSpacer, Priority.ALWAYS);
        HBox.setHgrow(couponDetailsSpacer, Priority.ALWAYS);
        HBox.setHgrow(totalPriceSpacer, Priority.ALWAYS);

        applyCouponIcon.setPreserveRatio(true);
        applyCouponIcon.setFitWidth(30);
    }

    public void setScene() {
        restaurantName.setText(order.getRestaurant().getName());
        orderAddress.setText(order.getDeliveryAddress());
        rawPrice.setText(order.getRawPrice().toString());
        taxFee.setText(order.getRestaurant().getTaxFee().toString());
        additionalFee.setText(order.getRestaurant().getAdditionalFee().toString());
        courierFee.setText(String.valueOf(order.getCourierFee()));
        if (order.getCoupon() != null)
            couponDetails.setText(order.getCoupon().getCode().toString());
        totalPrice.setText(order.getPayPrice().toString());

        itemsInTheCart.getChildren().clear();

        // 2. Get the list of items from the order
        List<CartItem> cartItems = order.getCartItems();
        if (cartItems != null) {
            // 3. Loop through the items and create a card for each one
            for (CartItem item : cartItems) {
                try {
                    FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                            "/frontend/bemirfoodclient/restaurant/buyer/order/item-card-small-order.fxml"
                    ));
                    Pane card = loader.load();
                    BuyerOrderItemCardController controller = loader.getController();
                    controller.setItemData(item);
                    itemsInTheCart.getChildren().add(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void pay() {
        Toggle selectedToggle = methodToggleGroup.getSelectedToggle();
        HttpResponseData response = null;
        if (selectedToggle == onlineToggle) {
            showPaymentDialog(false);
            Map<String, Object> request = new HashMap<>();
            request.put("order_id", order.getId());
            request.put("method", "online");
            response = Requests.pay(gson.toJson(request));
            showPaymentDialog(false);
        } else if (selectedToggle == walletToggle) {

            Map<String, Object> request = new HashMap<>();
            request.put("order_id", order.getId());
            request.put("method", "wallet");
            response = Requests.pay(gson.toJson(request));
        }

        if(response.getStatusCode()== 200){
            showAlert("Successful payment", "Thanks for your purchase", Alert.AlertType.CONFIRMATION);
        }else if(response.getStatusCode() == 403){
            showAlert("Payment Failed", "Insufficient balance", Alert.AlertType.ERROR);
        }else{
            showAlert("Failed payment", "Something went wrong", Alert.AlertType.ERROR);
        }

    }

    private void showPaymentDialog(boolean isWalletTopUp) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(isWalletTopUp ? "Top-Up Wallet" : "Online Payment");
        dialog.initOwner(payButton.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader(BemirfoodApplication.class.getResource(
                "/frontend/bemirfoodclient/payment-gateway-dialog.fxml"
        ));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        PaymentGatewayDialogController controller = fxmlLoader.getController();
        controller.setMode(isWalletTopUp); // Configure the dialog

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String accountNumber = controller.getOtp();

            switch (acceptPayment()) {
                case 200:
                    paymentSuccessCallback.accept(order);
                    break;
            }
        }
    }

    public int acceptPayment() {
        //do the stuff in backend
        //change paid to true
        return 200;
    }

    public void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }

    public void applyCoupon() {
        Map<String, String> request = new HashMap<>();
        request.put("coupon_code", couponTextField.getText());
        HttpResponseData response = validateCoupon(request);
        JsonObject obj = response.getBody().get("Coupon details").getAsJsonObject();
        Coupon coupon = gson.fromJson(obj, Coupon.class);
        coupon.setStartDate(LocalDateTimeAdapter.StringToDateTime(obj.get("start_date").getAsString()));
        coupon.setEndDate(LocalDateTimeAdapter.StringToDateTime(obj.get("end_date").getAsString()));
        if(response.getStatusCode() == 200){
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("order_id", order.getId());
            requestMap.put("coupon_code", couponTextField.getText());
            HttpResponseData res = Requests.applyCoupon(gson.toJson(requestMap));
            if(res.getStatusCode() == 200){
                order.setCoupon(coupon);
                showAlert("Confirm", "Coupon applied successfully", Alert.AlertType.INFORMATION);
                setScene();
            }else{
                expHandler(res, "Failed to apply coupon", null);
            }
        }else{
            expHandler(response, "Failed to apply coupon", null);
        }
    }
}
