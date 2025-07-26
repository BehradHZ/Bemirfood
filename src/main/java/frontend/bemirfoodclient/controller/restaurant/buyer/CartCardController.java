package frontend.bemirfoodclient.controller.restaurant.buyer;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.PaymentGatewayDialogController;
import frontend.bemirfoodclient.controller.restaurant.buyer.order.BuyerOrderItemCardController;
import frontend.bemirfoodclient.model.entity.CartItem;
import frontend.bemirfoodclient.model.entity.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class CartCardController {

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
            couponDetails.setText(order.getCoupon().toString());
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

        if (selectedToggle == onlineToggle) {
            showPaymentDialog(false);
        } else if (selectedToggle == walletToggle) {

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

    public void applyCoupon() {

    }
}
