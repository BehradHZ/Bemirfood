package frontend.bemirfoodclient.controller.restaurant.buyer.order;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.CartItem;
import frontend.bemirfoodclient.model.entity.Order;
import frontend.bemirfoodclient.model.entity.OrderStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BuyerOrderCardController {
    @FXML
    public Label orderId;
    @FXML
    public Region idTimeSpacer;
    @FXML
    public Label dateTime;
    @FXML
    public VBox itemsSection;
    @FXML
    public Label orderBuyerName;
    @FXML
    public Label orderAddress;
    @FXML
    public Label orderCourierName;
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
    public Button orderStatusButton;
    @FXML
    public Region lastUpdateSpacer;
    @FXML
    public Label lastUpdate;

    private Order order;

    public void setOrderData(Order order) {
        this.order = order;
        setScene();
    }

    public void initialize() {
        HBox.setHgrow(idTimeSpacer, Priority.ALWAYS);
        HBox.setHgrow(rawPriceSpacer, Priority.ALWAYS);
        HBox.setHgrow(taxFeeSpacer, Priority.ALWAYS);
        HBox.setHgrow(additionalFeeSpacer, Priority.ALWAYS);
        HBox.setHgrow(courierFeeSpacer, Priority.ALWAYS);
        HBox.setHgrow(couponDetailsSpacer, Priority.ALWAYS);
        HBox.setHgrow(totalPriceSpacer, Priority.ALWAYS);
        HBox.setHgrow(lastUpdateSpacer, Priority.ALWAYS);

        orderStatusButton.setDisable(true);
    }

    public void setScene() {
        orderId.setText("Order #" + order.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        dateTime.setText(order.getCreatedAt().format(formatter));
        lastUpdate.setText("Last update: " + order.getUpdatedAt().format(formatter));

        orderBuyerName.setText("Buyer: " + order.getCustomer().getFull_name());
        orderAddress.setText("Address: " + order.getDeliveryAddress()); // Assuming address is on the order
        orderCourierName.setText("Courier: " + order.getDelivery().getFull_name());

        rawPrice.setText(String.valueOf(order.getRawPrice()));
        taxFee.setText(String.valueOf(order.getRestaurant().getTaxFee()));
        additionalFee.setText(String.valueOf(order.getRestaurant().getAdditionalFee()));
        courierFee.setText(String.valueOf(order.getCourierFee()));
        couponDetails.setText(String.valueOf(order.getCoupon())); // Assuming a discount value
        totalPrice.setText(order.getPayPrice() + " toomans");
        orderStatusButton.setText(getStatusText(order.getStatus()));
        styleStatusButton(orderStatusButton, order.getStatus());
        lastUpdate.setText(order.getUpdatedAt().format(formatter));
        itemsSection.getChildren().clear();

        List<CartItem> cartItems = order.getCartItems();

        for (CartItem cartItem : cartItems) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/buyer/order/item-card-small-order.fxml"
                ));
                Pane itemCard = loader.load();

                BuyerOrderItemCardController itemController = loader.getController();
                itemController.setItemData(cartItem);

                itemsSection.getChildren().add(itemCard);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getStatusText(OrderStatus status) {
        if (status == null) return "";
        return switch (status) {
            case submitted -> "Submitted";
            case unpaid_and_cancelled -> "Unpaid";
            case waiting_vendor -> "Waiting for Vendor";
            case cancelled -> "Cancelled";
            case finding_courier -> "Finding Courier";
            case on_the_way -> "On the Way!";
            case completed -> "Completed";
            case accepted -> "Accepted!";
            case rejected -> "Rejected";
            case served -> "Served";
        };
    }

    private void styleStatusButton(Button button, OrderStatus status) {
        String baseStyle = "-fx-background-radius: 10; -fx-font-size: 16; -fx-font-weight: bold; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);";
        String colorStyle = switch (status) {
            case submitted, accepted -> "-fx-background-color: #b5e61d; -fx-text-fill: black;";
            case unpaid_and_cancelled -> "-fx-background-color: #ff7c7c; -fx-text-fill: black;";
            case waiting_vendor -> "-fx-background-color: #ff7f27; -fx-text-fill: black;";
            case cancelled -> "-fx-background-color: #ed1c24; -fx-text-fill: white;";
            case finding_courier -> "-fx-background-color: #173f3f; -fx-text-fill: white;";
            case on_the_way -> "-fx-background-color: #99d9ea; -fx-text-fill: black;";
            case completed -> "-fx-background-color: #22b14c; -fx-text-fill: white;";
            case rejected -> "-fx-background-color: #880015; -fx-text-fill: white;";
            case served -> "-fx-background-color: #39107b; -fx-text-fill: white;";
        };
        button.setStyle(baseStyle + colorStyle);
    }

    public int changeOrderStatus(OrderStatus orderStatus) {
        //do the stuff in backend

        return 200; //temporary
    }
}
