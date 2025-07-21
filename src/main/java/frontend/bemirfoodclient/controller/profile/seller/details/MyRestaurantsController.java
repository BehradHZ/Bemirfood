package frontend.bemirfoodclient.controller.profile.seller.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Restaurant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyRestaurantsController {
    @FXML
    public BorderPane mainBorderPane;
    @FXML
    public VBox restaurantCardsSection;

    public void initialize() {
        List<Restaurant> restaurants = getRestaurants();
        for (Restaurant restaurant : restaurants) {
            try {
                // Corrected the path to be absolute from the resources root
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/profile/seller/details/restaurant-card.fxml"));

                Pane card = loader.load();
                RestaurantCardController controller = loader.getController();
                controller.setData(restaurant);
                restaurantCardsSection.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("Starbucks", "Karaj", null));

        return restaurants;
    }
}
