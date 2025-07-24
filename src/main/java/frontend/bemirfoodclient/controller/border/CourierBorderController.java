package frontend.bemirfoodclient.controller.border;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.courier.DeliveryCardController;
import frontend.bemirfoodclient.model.entity.Order;
import frontend.bemirfoodclient.model.entity.OrderStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CourierBorderController {
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

    public List<Order> getAllDeliveries() {
        List<Order> orders = new ArrayList<>();

    }

    public void showAllDeliveries() {
        List<Order> allOrders = getAllDeliveries();

        // --- 2. Separate Orders into Active and Recommended Lists ---
        // An active delivery is one that is currently 'on_the_way'
        List<Order> activeDeliveries = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.on_the_way)
                .collect(Collectors.toList());

        // A recommended delivery is one that is 'finding_courier'
        List<Order> recommendedDeliveries = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.finding_courier)
                .collect(Collectors.toList());

        // --- 3. Populate the Active Delivery Section ---
        activeDeliverySection.getChildren().clear();
        if (activeDeliveries.isEmpty()) {
            // If no active deliveries, show a separator
            activeDeliverySection.getChildren().add(new Separator());
            activeDeliverySection.getChildren().add(new Label("No Active Deliveries"));
        } else {
            // If there is an active delivery, create a card for it
            for (Order order : activeDeliveries) {
                activeDeliverySection.getChildren().add(createDeliveryCard(order));
            }
        }

        // --- 4. Populate the Recommended Deliveries Section ---
        recommendedDeliveryList.getChildren().clear();
        for (Order order : recommendedDeliveries) {
            recommendedDeliveryList.getChildren().add(createDeliveryCard(order));
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
            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null or an empty pane on error
        }
    }
}
