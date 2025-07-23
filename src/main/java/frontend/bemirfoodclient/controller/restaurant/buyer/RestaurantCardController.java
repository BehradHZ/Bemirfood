package frontend.bemirfoodclient.controller.restaurant.buyer;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Restaurant;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class RestaurantCardController {

    @FXML
    public Button restaurantPhoto;
    @FXML
    public Label nameButton;
    @FXML
    public Label address;
    @FXML
    public ImageView restaurantLogo;

    private Restaurant restaurant;

    public void initialize() {
        restaurantLogo.setPreserveRatio(true);
        restaurantLogo.setFitHeight(120);
        setProfileImageBase64(restaurantLogo);
    }

    public void setData(Restaurant restaurant) {
        this.restaurant = restaurant;
        nameButton.setText(restaurant.getName());
        address.setText(restaurant.getAddress());

    }

    public void setProfileImageBase64(ImageView profileImageView) {
        //do the stuff in backend
        boolean profilePhotoExists = false;
        if (restaurant != null && restaurant.getLogo() != null) {
            String base64Uri = restaurant.getLogo();
            ImageLoader.displayBase64Image(base64Uri, profileImageView);
        } else {
            profileImageView.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("" +
                    "/frontend/bemirfoodclient/assets/icons/addRestaurant.png"))));
        }
    }
}
