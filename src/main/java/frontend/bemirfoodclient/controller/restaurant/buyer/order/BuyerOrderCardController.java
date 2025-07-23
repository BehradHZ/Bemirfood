package frontend.bemirfoodclient.controller.restaurant.buyer.order;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.seller.order.ChangeOrderStatusDialogController;
import frontend.bemirfoodclient.controller.restaurant.seller.order.SellerOrderItemCardController;
import frontend.bemirfoodclient.model.entity.CartItem;
import frontend.bemirfoodclient.model.entity.Order;
import frontend.bemirfoodclient.model.entity.OrderStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
    }

    public void setScene() {
        orderId.setText("Order #" + order.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        dateTime.setText(order.getCreatedAt().format(formatter));
        lastUpdate.setText("Last update: " + order.getUpdatedAt().format(formatter));

        orderBuyerName.setText("Buyer: " + order.getCustomer().getFullName());
        orderAddress.setText("Address: " + order.getDeliveryAddress()); // Assuming address is on the order
        orderCourierName.setText("Courier: " + order.getDelivery().getFullName());

        rawPrice.setText(String.valueOf(order.getRawPrice()));
        taxFee.setText(String.valueOf(order.getRestaurant().getTaxFee()));
        additionalFee.setText(String.valueOf(order.getRestaurant().getAdditionalFee()));
        courierFee.setText(String.valueOf(order.getCourierFee()));
        couponDetails.setText(String.valueOf(order.getCoupon())); // Assuming a discount value
        totalPrice.setText(String.valueOf(order.getPayPrice()) + " toomans");

        OrderStatus status = order.getStatus();
        orderStatusButton.setText(status.toString().replaceAll("_", " "));

        switch (status) {
            case submitted ->
                orderStatusButton.setStyle("-fx-background-color: #b5e61d; -fx-text-fill: black; -fx-background-radius: 10;" +
                        " -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            case unpaid_and_cancelled ->
                orderStatusButton.setStyle("-fx-background-color: #ff7c7c; -fx-text-fill: black; -fx-background-radius: 10;" +
                        " -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            case waiting_vendor ->
                orderStatusButton.setStyle("-fx-background-color: #ff7f27; -fx-text-fill: black; -fx-background-radius: 10;" +
                        " -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            case cancelled ->
                orderStatusButton.setStyle("-fx-background-color: #ed1c24; -fx-text-fill: white; -fx-background-radius: 10;" +
                        " -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            case finding_courier ->
                orderStatusButton.setStyle("-fx-background-color: #173f3f; -fx-text-fill: white; -fx-background-radius: 10;" +
                        " -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            case on_the_way ->
                orderStatusButton.setStyle("-fx-background-color: #99d9ea; -fx-text-fill: black; -fx-background-radius: 10;" +
                        " -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            case completed ->
                orderStatusButton.setStyle("-fx-background-color: #22b14c; -fx-text-fill: white; -fx-background-radius: 10;" +
                        " -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            case accepted ->
                orderStatusButton.setStyle("-fx-background-color: #b5e61d; -fx-text-fill: black; -fx-background-radius: 10;" +
                        " -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            case rejected ->
                orderStatusButton.setStyle("-fx-background-color: #880015; -fx-text-fill: white; -fx-background-radius: 10;" +
                        " -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            case served ->
                orderStatusButton.setStyle("-fx-background-color: #39107b; -fx-text-fill: white; -fx-background-radius: 10;" +
                        " -fx-font-size: 16; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");
        }
        lastUpdate.setText(order.getUpdatedAt().format(formatter));

        itemsSection.getChildren().clear();

        List<CartItem> cartItems = order.getCartItems();

        for (CartItem cartItem : cartItems) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/seller/order/item-card-small-order.fxml"
                ));
                Pane itemCard = loader.load();

                SellerOrderItemCardController itemController = loader.getController();
                itemController.setItemData(cartItem);

                itemsSection.getChildren().add(itemCard);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeStatus() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Restaurant");
        dialog.initOwner(orderStatusButton.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader(BemirfoodApplication.class.getResource(
                "/frontend/bemirfoodclient/restaurant/seller/order/change-order-status-dialog.fxml"
        ));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        AtomicReference<OrderStatus> orderStatus = new AtomicReference<>();
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            ChangeOrderStatusDialogController controller = fxmlLoader.getController();
            orderStatus.set(controller.getSelectedStatus());

            if (orderStatus.get() == null) {
                event.consume();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            switch (changeOrderStatus(orderStatus.get())) {
                case 200:
                    setScene();
                    break;
                //show alerts
            }
        }
    }

    public int changeOrderStatus(OrderStatus orderStatus) {
        //do the stuff in backend

        return 200; //temporary
    }
}
