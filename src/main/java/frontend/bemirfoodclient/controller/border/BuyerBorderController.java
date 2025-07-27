package frontend.bemirfoodclient.controller.border;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.*;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.buyer.BuyerRestaurantViewController;
import frontend.bemirfoodclient.controller.restaurant.buyer.RestaurantCardController;
import frontend.bemirfoodclient.controller.restaurant.buyer.item.BuyerItemCardController;
import frontend.bemirfoodclient.model.entity.Item;
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

import static BuildEntity.EntityRequest.getEntity;
import static HttpClientHandler.Requests.getCustomerFavorites;

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
    public VBox recommendedItemSection;
    public VBox recommendedItemList;

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

        loadRecommendedView();
    }

    private void loadRecommendedView() {
        List<Restaurant> recommendedRestaurants = getRecommendedRestaurants();
        displayRestaurants(recommendedRestaurants);

        List<Item> recommendedItems = getRecommendedItems();
        displayItems(recommendedItems);
    }

    private void displayRestaurants(List<Restaurant> restaurants) {

        recommendedVendorList.getChildren().clear();
        List<Restaurant> res = getRecommendedRestaurants();
        if(res == null) return;

        for (Restaurant restaurant : res) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/buyer/restaurant-card.fxml"
                ));

                Pane card = loader.load();
                card.setUserData(restaurant);

                RestaurantCardController controller = loader.getController();
                controller.setData(restaurant);
                card.setOnMouseClicked(event -> cardClick((Restaurant) card.getUserData()));

                recommendedVendorList.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayItems(List<Item> items) {

        recommendedItemList.getChildren().clear();

        for (Item item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/buyer/item/item-card.fxml"));
                Pane card = loader.load();

                BuyerItemCardController controller = loader.getController();
                controller.setItemData(item);
                card.setOnMouseClicked(event -> cardClick(
                        ((Item) card.getUserData())
                                .getRestaurant()));

                recommendedItemList.getChildren().add(card);

            } catch (IOException e) {
                // This will fail if the FXML or controller doesn't exist.
                // It's here to show you the structure.
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

    @FXML
    public void searchButtonClicked() {
        String query = searchTextField.getText();

        if (query == null || query.trim().isEmpty()) {
            loadRecommendedView();
            return;
        }

        long commaCount = query.chars().filter(ch -> ch == ',').count();

        if (commaCount == 1) {
            // --- VENDOR SEARCH (e.g., "Pizza, italian") ---
            String[] parts = query.split(",");
            String searchString = parts[0].trim();
            String keywords = parts.length > 1 ? parts[1].trim() : "";


            List<Restaurant> searchedRestaurants = searchForVendors(searchString, keywords);
            displayRestaurants(searchedRestaurants);
            displayItems(new ArrayList<>()); // Clear items list

        } else if (commaCount == 2) {
            // --- ITEM SEARCH (e.g., "Burger, 150000, beef") ---
            String[] parts = query.split(",");
            String searchString = parts[0].trim();
            String price = parts.length > 1 ? parts[1].trim() : "";
            String keywords = parts.length > 2 ? parts[2].trim() : "";

            System.out.println("--- Item Search ---");
            System.out.println("Search String: " + searchString);
            System.out.println("Max Price: " + price);
            System.out.println("Keywords: " + keywords);

            // TODO: Call your backend to search for items
            List<Item> searchedItems = searchForItems(searchString, price, keywords);
            displayItems(searchedItems);
            displayRestaurants(new ArrayList<>()); // Clear vendors list

        } else {
            // Handle invalid search format
            showAlert("Invalid Search Format!\nFor vendors, use: name, keywords\nFor items, use: name, max price, keywords");
        }
    }


    private List<Restaurant> searchForVendors(String searchString, String keywords) {
        //TODO: do the stuff in backend

        return new ArrayList<>();
    }

    private List<Item> searchForItems(String searchString, String price, String keywords) {
        //TODO: do the stuff in backend

        return new ArrayList<>();
    }


    public List<Restaurant> getRecommendedRestaurants() {
        HttpResponseData response = getCustomerFavorites();
        JsonObject json = response.getBody();
        JsonArray rstaurantJsonArray = json.getAsJsonArray( "List of favorite restaurants");
        List<Restaurant> restaurants = new ArrayList<>();

        if(rstaurantJsonArray == null) return restaurants;

        for (JsonElement element : rstaurantJsonArray) {
            JsonObject restaurantJson = element.getAsJsonObject();
            Restaurant restaurant = gson.fromJson(restaurantJson, Restaurant.class);
            Seller seller = getEntity(restaurantJson.get("seller_id").getAsLong(), Seller.class);
            restaurant.setSeller(seller);
            restaurants.add(restaurant);
        }

        return restaurants;
    }

    public List<Item> getRecommendedItems() {
        //do the stuff in backend
        //recommended items can be 5 recently added items

        List<Item> items = new ArrayList<>();
        return items;
    }

    public void cardClick(Restaurant selectedRestaurant) {
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

    public void showAllVendors() {

    }

    public void showAllItems() {

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
}
