package frontend.bemirfoodclient.controller.border;

import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.buyer.BuyerRestaurantViewController;
import frontend.bemirfoodclient.controller.restaurant.buyer.RestaurantCardController;
import frontend.bemirfoodclient.model.entity.Bank_info;
import frontend.bemirfoodclient.model.entity.Restaurant;
import frontend.bemirfoodclient.model.entity.Seller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuyerBorderController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();

    @FXML
    public BorderPane mainBorderPane;
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
    public VBox recommendedVendorList;
    @FXML
    public ImageView cartIcon;

    public void initialize() {
        borderBemirfoodLogo.setPreserveRatio(true);
        borderBemirfoodLogo.setFitHeight(40);

        searchIcon.setPreserveRatio(true);
        searchIcon.setFitHeight(17);

        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);

        cartIcon.setPreserveRatio(true);
        cartIcon.setFitHeight(30);

        profileIcon.setPreserveRatio(true);
        profileIcon.setFitHeight(27);

        List<Restaurant> restaurants = getRecommendedRestaurants();

        for (Restaurant restaurant : restaurants) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/buyer/restaurant-card.fxml"));

                Pane card = loader.load();
                card.setUserData(restaurant);

                RestaurantCardController controller = loader.getController();
                controller.setData(restaurant);
                card.setOnMouseClicked(event -> cardClick(card));

                recommendedVendorList.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void borderBemirfoodLogoClicked() {
        try {
            Stage stage = (Stage) profileIcon.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/border/buyer-border-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void profileButtonClicked() {
        try {
            Stage stage = (Stage) profileIcon.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/profile/buyer/buyer-profile-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
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

    public List<Restaurant> getRecommendedRestaurants() {
        //do the stuff in backend

        //temporary
        List<Restaurant> restaurants = new ArrayList<>();
        Bank_info seller1Bank = new Bank_info("Pizza Bank", "PB-111");
        Seller seller1 = new Seller("Pizza Pete", "09111111111", "pete@pizzapalace.com", null, "1 Pizza Plaza", seller1Bank, "pass1");


        restaurants.add( new Restaurant("Pizza Palace", seller1, "1 Pizza Plaza, 1 Pizza Plaza, 1 Pizza Plaza, 1 Pizza Plaza, ", "555-PIZZA", null, 0.09, 2000.0));
        restaurants.add( new Restaurant("Pizza Palace", seller1, "1 Pizza Plaza, 1 Pizza Plaza, 1 Pizza Plaza, 1 Pizza Plaza, ", "555-PIZZA", null, 0.09, 2000.0));
        restaurants.add( new Restaurant("Pizza Palace", seller1, "1 Pizza Plaza, 1 Pizza Plaza, 1 Pizza Plaza, 1 Pizza Plaza, ", "555-PIZZA", null, 0.09, 2000.0));
        restaurants.add( new Restaurant("Pizza Palace", seller1, "1 Pizza Plaza, 1 Pizza Plaza, 1 Pizza Plaza, 1 Pizza Plaza, ", "555-PIZZA", null, 0.09, 2000.0));

        return restaurants;
    }

    public void cardClick(Pane card) {
        Restaurant selectedRestaurant = (Restaurant) card.getUserData();

        if (selectedRestaurant != null) {
            try {
                FXMLLoader restaurantPageLoader = new FXMLLoader(getClass().getResource(
                        "/frontend/bemirfoodclient/restaurant/buyer/buyer-restaurant-view.fxml"
                ));
                Parent restaurantPageView = restaurantPageLoader.load();

                BuyerRestaurantViewController pageController = restaurantPageLoader.getController();
                pageController.setRestaurantData(selectedRestaurant);

                mainBorderPane.setCenter(restaurantPageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showAllVendors() {

    }

    public void cartButtonClicked() {
        try {
            FXMLLoader restaurantPageLoader = new FXMLLoader(getClass().getResource(
                    "/frontend/bemirfoodclient/restaurant/buyer/cart-view.fxml"
            ));
            Parent cartPageView = restaurantPageLoader.load();

            mainBorderPane.setCenter(cartPageView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
