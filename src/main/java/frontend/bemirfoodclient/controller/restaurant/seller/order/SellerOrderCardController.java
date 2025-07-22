package frontend.bemirfoodclient.controller.restaurant.seller.order;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.CartItem;
import frontend.bemirfoodclient.model.entity.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SellerOrderCardController {
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

        rawPrice.setText(String.format("%,.0f", order.getRawPrice()));
        taxFee.setText(String.format("%,.0f", order.getRestaurant().getTaxFee()));
        additionalFee.setText(String.format("%,.0f", order.getRestaurant().getAdditionalFee()));
        courierFee.setText(String.format("%,.0f", order.getCourierFee()));
        couponDetails.setText(String.format("(%,.0f)", order.getCoupon())); // Assuming a discount value
        totalPrice.setText(String.format("%,.0f toomans", order.getPayPrice()));

        orderStatusButton.setText(order.getStatus().toString());
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

}
