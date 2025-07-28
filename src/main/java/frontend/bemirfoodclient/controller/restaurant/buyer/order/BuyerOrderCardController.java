package frontend.bemirfoodclient.controller.restaurant.buyer.order;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.*;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.CartItem;
import frontend.bemirfoodclient.model.entity.Order;
import frontend.bemirfoodclient.model.entity.OrderRating;
import frontend.bemirfoodclient.model.entity.OrderStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static HttpClientHandler.Requests.*;
import static exception.exp.expHandler;

public class BuyerOrderCardController {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();
    @FXML
    public BorderPane mainBorderPane;
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
    @FXML
    public Button addRatingButton;
    @FXML
    public ImageView orderRatingButtonIcon;

    private Order order;
    private OrderRating existingRating;

    public void setOrderData(Order order) {
        this.order = order;
        this.existingRating = order.getRating();
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
        orderRatingButtonIcon.setPreserveRatio(true);
        orderRatingButtonIcon.setFitHeight(20);
    }

    public void setScene() {
        orderId.setText("Order #" + order.getId());

//        if (order.getStatus() == OrderStatus.completed) {
//            addRatingButton.setVisible(true);
//            addRatingButton.setManaged(true);
//        } else {
//            addRatingButton.setVisible(false);
//            addRatingButton.setManaged(false);
//        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        dateTime.setText(order.getCreatedAt().format(formatter));
        lastUpdate.setText("Last update: " + order.getUpdatedAt().format(formatter));

        orderBuyerName.setText("Buyer: " + order.getCustomer().getFull_name());
        orderAddress.setText("Address: " + order.getDeliveryAddress()); // Assuming address is on the order
        orderCourierName.setText("Courier: " + ( order.getDelivery() != null ? order.getDelivery().getFull_name() : "no delivery"));

        rawPrice.setText(String.valueOf(order.getRawPrice()));
        taxFee.setText(String.valueOf(order.getRestaurant().getTaxFee()));
        additionalFee.setText(String.valueOf(order.getRestaurant().getAdditionalFee()));
        courierFee.setText(String.valueOf(order.getCourierFee()));
        couponDetails.setText(String.valueOf(order.getCoupon())); // Assuming a discount value
        totalPrice.setText("$" + order.getPayPrice());
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

    public void manageRatingButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                    "/frontend/bemirfoodclient/restaurant/buyer/order/add-rating-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            AddRatingDialogController dialogController = loader.getController();
            dialogController.setOrderId(this.order.getId());

            //String title = "Add Rating";
//            if (existingRating != null) {
//                dialogController.setExistingRating(existingRating);
//                title = "Edit Your Rating";
//            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainBorderPane.getScene().getWindow());
            dialog.setDialogPane(dialogPane);

            ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OTHER);
            dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.CANCEL, ButtonType.OK);
            if (existingRating == null) {
                dialog.getDialogPane().lookupButton(deleteButtonType).setVisible(false);
            } else {
                dialog.getDialogPane().lookupButton(deleteButtonType).setVisible(true);
            }

            dialog.setHeaderText(null);

            final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("Submit");

            String title;

            HttpResponseData response = getOrderRating(order.getId());
            if(response.getStatusCode() != 200) {
                title = "Add Rating";
            }else{
                JsonObject body = response.getBody().get("Order rating").getAsJsonObject();
                OrderRatingDto ratingDto = new OrderRatingDto();
                ratingDto.setOrder_id(body.get("order_id").getAsLong());
                ratingDto.setRating(body.get("rating").getAsInt());
                ratingDto.setComment(body.get("comment").getAsString());
                JsonArray imageArray = body.getAsJsonArray("imageBase64");
                List<String> images = new ArrayList<>();
                for (JsonElement element : imageArray) {
                    images.add(element.getAsString());
                }
                ratingDto.setImageBase64(images);
                title = "Edit Your Rating";
                dialogController.setExistingRating(ratingDto);
            }

            dialog.setTitle(title);


            AtomicReference<Map<String, Object>> ratingDataRef = new AtomicReference<>();

            okButton.addEventFilter(ActionEvent.ACTION, e -> {
                Map<String, Object> resultData = dialogController.processResult();
                ratingDataRef.set(resultData); // Store the result
                if (resultData == null) {
                    e.consume();
                }
            });

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Map<String, Object> ratingData = ratingDataRef.get();
                long orderId = Long.parseLong(ratingData.get("order_id").toString());
                int rating = Integer.parseInt(ratingData.get("rating").toString());
                String comment = ratingData.get("comment").toString();
                List<String> images = new ArrayList<>();
                try {
                    images = (List<String>) ratingData.get("imageBase64");
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

                if(response.getStatusCode() != 200) {
                    HttpResponseData res = addRating(orderId, rating, comment, images);

                    if (res.getStatusCode() != 200) expHandler(response, "Filed to submit rating",null);
                    System.out.println("Successfully added rating");

                }else{
                    HttpResponseData res = editRating( orderId, rating, comment, images);
                    if(res.getStatusCode() != 200) expHandler(res, "Filed to edit rating",null);
                    System.out.println("Successfully edited rating");
                }

            } else if (result.get() == deleteButtonType) {
                //TODO: delete the existing rating
                if (deleteRating() == 200) {
                    System.out.println("Successfully delete rating");
                }
                //TODO: show alerts
                System.out.println("Delete button pressed");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpResponseData addRating(long orderId, int rating, String comment, List<String> images) {
        Map<String, Object> request = new HashMap<>();
        request.put("order_id", orderId);
        request.put("rating", rating);
        if(comment != null) request.put("comment", comment);
        if(images != null && !images.isEmpty())request.put("imageBase64", images);
        return submitRating(gson.toJson(request));
    }

    public HttpResponseData editRating(Long orderId, int rating, String comment, List<String> images) {
        Map<String, Object> request = new HashMap<>();
        request.put("order_id", orderId);
        request.put("rating", rating);
        request.put("comment", comment);
        request.put("imageBase64", images);
        return editOrderRating(gson.toJson(request), orderId);
    }

    public int deleteRating() {
        /*TODO: do the stuff in backend
        *  you can also use this.order to find the order*/
        return 200;
    }

    @Getter
    @Setter
    class OrderRatingDto {
        Long order_id;
        int rating;
        String comment;
        List<String> imageBase64;
    }
}
