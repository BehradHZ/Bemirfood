package frontend.bemirfoodclient.controller.profile.seller.details;

import Deserializer.RestaurantDeserializer;
import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import HttpClientHandler.Requests;
import com.google.gson.*;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.border.SellerBorderController;
import frontend.bemirfoodclient.controller.restaurant.seller.SellerRestaurantViewController;
import frontend.bemirfoodclient.model.entity.Restaurant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MyRestaurantsController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();

    @FXML
    public BorderPane mainBorderPane;
    @FXML
    public VBox restaurantCardsSection;

    public void initialize() {
        List<Restaurant> restaurants = getRestaurants();
        for (Restaurant restaurant : restaurants) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/profile/seller/details/restaurant-card.fxml"));

                Pane card = loader.load();

                card.setUserData(restaurant);

                RestaurantCardController controller = loader.getController();
                controller.setData(restaurant);

                card.setOnMouseClicked(event -> cardClick(card));

                restaurantCardsSection.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Restaurant> getRestaurants() {
        Gson g = new GsonBuilder()
                .registerTypeAdapter(Restaurant.class, new RestaurantDeserializer())
                .create();

        HttpResponseData responseData = Requests.getSellerRestaurants();
        JsonObject jsonObject = responseData.getBody().getAsJsonObject();
        JsonArray restaurantArray = jsonObject.getAsJsonArray("List of restaurants");

        List<Restaurant> restaurants = new ArrayList<>();

        for (JsonElement restaurantElement : restaurantArray) {
            Restaurant restaurant = g.fromJson(restaurantElement, Restaurant.class);
            restaurants.add(restaurant);
        }

        return restaurants;
    }

    public void cardClick(Pane card) {
        Restaurant selectedRestaurant = (Restaurant) card.getUserData();

        if (selectedRestaurant != null) {
            try {
                FXMLLoader borderLoader = new FXMLLoader(getClass().getResource(
                        "/frontend/bemirfoodclient/border/seller-border-view.fxml"
                ));
                Parent homepageRoot = borderLoader.load();

                SellerBorderController borderController = borderLoader.getController();

                FXMLLoader restaurantPageLoader = new FXMLLoader(getClass().getResource(
                        "/frontend/bemirfoodclient/restaurant/seller/seller-restaurant-view.fxml"
                ));
                Parent restaurantPageView = restaurantPageLoader.load();

                SellerRestaurantViewController pageController = restaurantPageLoader.getController();

                pageController.setRestaurantData(selectedRestaurant);

                borderController.setCenterContent(restaurantPageView);

                Stage stage = (Stage) card.getScene().getWindow();
                stage.getScene().setRoot(homepageRoot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
