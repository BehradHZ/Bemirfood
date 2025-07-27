package frontend.bemirfoodclient.controller.restaurant.courier;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.CartItem;
import frontend.bemirfoodclient.model.entity.Order;
import frontend.bemirfoodclient.model.entity.OrderStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DeliveryCardController {
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
    public Region courierFeeSpacer;
    @FXML
    public Label courierFee;
    @FXML
    public Region lastUpdateSpacer;
    @FXML
    public Label lastUpdate;
    @FXML
    public ComboBox statusComboBox;
    @FXML
    public Button acceptButton;
    public Button completeButton;
    public Button cancelButton;

    private Order order;

    private boolean accepted = false;

    private Consumer<Order> acceptCallback;
    private BiConsumer<Order, OrderStatus> statusChangeCallback;

    public void setOrderData(Order order) {
        this.order = order;
        setScene();
    }

    public void initialize() {
        HBox.setHgrow(idTimeSpacer, Priority.ALWAYS);
        HBox.setHgrow(courierFeeSpacer, Priority.ALWAYS);
        HBox.setHgrow(lastUpdateSpacer, Priority.ALWAYS);

        statusComboBox.getItems().setAll(OrderStatus.values());

        Callback<ListView<OrderStatus>, ListCell<OrderStatus>> cellFactory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(OrderStatus status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    Button statusButton = new Button(getStatusText(status));
                    styleStatusButton(statusButton, status);
                    setGraphic(statusButton);
                }
            }
        };

        statusComboBox.valueProperty().addListener((obs, oldStatus, newStatus) -> {
            if (newStatus != null && newStatus != oldStatus) {
                // When the value changes, execute the callback
                if (statusChangeCallback != null) {
                    statusChangeCallback.accept(order, (OrderStatus) newStatus);
                }
            }
        });

        statusComboBox.setCellFactory(cellFactory);
        statusComboBox.setButtonCell(cellFactory.call(null));

        statusComboBox.valueProperty().addListener((obs, oldStatus, newStatus) -> {
            if (newStatus != null && newStatus != oldStatus) {
                changeOrderStatus((OrderStatus) newStatus);

                statusComboBox.setButtonCell(cellFactory.call(null));
            }
        });
    }

    public void setScene() {
        orderAddress.setText("Address: " + order.getDeliveryAddress());
        courierFee.setText(String.valueOf(order.getCourierFee()));

        orderId.setText("Order #" + order.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        dateTime.setText(order.getCreatedAt().format(formatter));
        orderBuyerName.setText("Buyer: " + order.getCustomer().getFull_name());
        orderAddress.setText("Address: " + order.getDeliveryAddress());
        courierFee.setText(String.valueOf(order.getCourierFee()));
        lastUpdate.setText("Last update: " + order.getUpdatedAt().format(formatter));

        // --- 2. Clear and Robust Logic to Determine Card State ---
        boolean isRecommended = order.getDelivery() == null && order.getStatus() == OrderStatus.finding_courier;
        boolean isActive = order.getDelivery() != null && (order.getStatus() == OrderStatus.accepted || order.getStatus() == OrderStatus.on_the_way);
        boolean isFinished = order.getStatus() == OrderStatus.completed || order.getStatus() == OrderStatus.cancelled;

        acceptButton.setVisible(isRecommended);
        acceptButton.setManaged(isRecommended);

        statusComboBox.setVisible(isActive);
        statusComboBox.setManaged(isActive);
        if (isActive) {
            statusComboBox.setValue(order.getStatus());
            orderCourierName.setText("Courier: " + order.getDelivery().getFull_name());
        }

        orderCourierName.setVisible(isActive || isFinished);
        lastUpdate.setVisible(isActive || isFinished);

        completeButton.setVisible(isFinished && order.getStatus() == OrderStatus.completed);
        completeButton.setDisable(isFinished && order.getStatus() == OrderStatus.completed);
        cancelButton.setVisible(isFinished && order.getStatus() == OrderStatus.cancelled);
        cancelButton.setDisable(isFinished && order.getStatus() == OrderStatus.cancelled);

        itemsSection.getChildren().clear();

        List<CartItem> cartItems = order.getCartItems();

        for (CartItem cartItem : cartItems) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/courier/item-card-small-order.fxml"
                ));
                Pane itemCard = loader.load();

                CourierOrderItemCardController itemController = loader.getController();
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

    public void setOnAccept(Consumer<Order> callback) {
        this.acceptCallback = callback;
    }

    public void setOnStatusChange(BiConsumer<Order, OrderStatus> callback) {
        this.statusChangeCallback = callback;
    }

    public int changeOrderStatus(OrderStatus orderStatus) {
        //do the stuff in backend

        return 200; //temporary
    }

    public void acceptButtonClicked() {
        acceptCallback.accept(order);
    }
}
