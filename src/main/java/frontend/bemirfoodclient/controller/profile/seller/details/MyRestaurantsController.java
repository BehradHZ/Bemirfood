package frontend.bemirfoodclient.controller.profile.seller.details;

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
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("Starbucks", "کرج، گوهردشت، خیابان یازدهم اصلی فاز دو، پلاک 33", "0922", null));

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
