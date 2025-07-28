package frontend.bemirfoodclient.controller.border;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.courier.DeliveryCardController;
import frontend.bemirfoodclient.model.entity.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static BuildEntity.builder.buildOrderList;
import static HttpClientHandler.Requests.*;
import static exception.exp.expHandler;

public class CourierBorderController {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();


    @FXML
    public ImageView borderBemirfoodLogo;
    @FXML
    public TextField searchTextField;
    @FXML
    public ImageView searchIcon;
    @FXML
    public ImageView profileIcon;
    @FXML
    public Region toolbarSpacer;
    @FXML
    public VBox activeDeliverySection;
    @FXML
    public VBox recommendedDeliveryList;
    @FXML
    public ScrollPane activeScroll;
    @FXML
    public ScrollPane recommendedScroll;

    public void initialize() {
        borderBemirfoodLogo.setPreserveRatio(true);
        borderBemirfoodLogo.setFitHeight(40);

        searchIcon.setPreserveRatio(true);
        searchIcon.setFitHeight(17);

        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);

        profileIcon.setPreserveRatio(true);
        profileIcon.setFitHeight(27);

        showAllDeliveries();
    }

    @FXML
    public void borderBemirfoodLogoClicked() {
        try {
            Stage stage = (Stage) profileIcon.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/border/courier-border-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCurrentUserProfile() {
        //do the stuff in backend
        return 200; //temporary
    }

    @FXML
    public void profileButtonClicked() {
        switch (getCurrentUserProfile()){
            case 200:
                try {
                    Stage stage = (Stage) profileIcon.getScene().getWindow();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                            "/frontend/bemirfoodclient/profile/courier/courier-profile-view.fxml")));
                    stage.getScene().setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 400:
                showAlert("Invalid phone number or password. (400 Invalid input)");
                break;
            case 401:
                showAlert("This phone number is not registered. (401 Unauthorized)");
                break;
            case 403:
                showAlert("You cannot access to this service. (403 Forbidden)");
                break;
            case 404:
                showAlert("Service not found. (404 Not Found)");
                break;
            case 409:
                showAlert("There was a conflict for access to this service. (409 Conflict)");
                break;
            case 415:
                showAlert("This media type cannot be accepted. (415 Unsupported Media Type)");
                break;
            case 429:
                showAlert("Please try again later. (429 Too Many Requests)");
                break;
            case 500:
                showAlert("This is from our side; pleas try again later :) (500 Internal Server Error)");
            default:
                break;
        }
    }

    public void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
        alert.setTitle("Loading profile failed");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }

    @FXML
    public void searchButtonClicked() {

    }

    public void showAllDeliveries() {
        List<Order> recommendedDeliveries =
                buildOrderList(args -> getAvailableDeliveryOrders(),
                        "List of available deliveries", "Failed to get available orders");


        Map<String, String> queryParams = new HashMap<>();
        List<Order> historyAndActiveOrders =
                buildOrderList(args -> searchDeliveryHistory((Map) args[0]), "List of completed and active deliveries", "Failed to get history", queryParams);

        List<Order> activeDeliveries = historyAndActiveOrders.stream().filter(
                o -> o.getStatus().equals(OrderStatus.on_the_way) ||
                        o.getStatus().equals(OrderStatus.accepted)).toList();

        activeDeliverySection.getChildren().clear();

        if (activeDeliveries.isEmpty()) {
            activeScroll.setPrefHeight(30);
        } else {
            activeScroll.setPrefHeight(400);
            for (Order order : activeDeliveries) {
                activeDeliverySection.getChildren().add(createDeliveryCard(order));
            }
        }

        recommendedDeliveryList.getChildren().clear();

        if (recommendedDeliveries.isEmpty()) {
            recommendedScroll.setPrefHeight(30);
        } else {
            recommendedScroll.setPrefHeight(400);
            for (Order order : recommendedDeliveries) {
                recommendedDeliveryList.getChildren().add(createDeliveryCard(order));
            }
        }
    }

    private Pane createDeliveryCard(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                    "/frontend/bemirfoodclient/restaurant/courier/delivery-card.fxml"
            ));
            Pane card = loader.load();
            DeliveryCardController controller = loader.getController();
            controller.setOrderData(order);

            controller.setOnAccept(acceptedOrder -> {
                HttpResponseData response = deliveryAccept(acceptedOrder);
                if(response.getStatusCode() == 200){
                    showAllDeliveries();
                }else{
                    expHandler(response, "Failed to change status", null);
                }
            });

            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null or an empty pane on error
        }
    }

    public HttpResponseData deliveryAccept(Order order) {
        Map<String, String> request = new HashMap<>();
        request.put("status", OrderStatus.accepted.name());
        return changeOrderStatusDelivery(order.getId(), gson.toJson(request));
    }

    public int deliveryStatusChange(Order order, OrderStatus newStatus) {
        //do the stuff in backend

        return 200; //temporary
    }
}
