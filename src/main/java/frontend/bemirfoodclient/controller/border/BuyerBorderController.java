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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static BuildEntity.EntityRequest.getEntity;
import static HttpClientHandler.Requests.getCustomerFavorites;
import static HttpClientHandler.Requests.getItemsWithFilter;

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

    private final List<Restaurant> allRestaurants = MockDataFactory.createMockRestaurants();
    private final List<Item> allItems = MockDataFactory.createMockItems(allRestaurants);


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

//        return new ArrayList<>();
        Stream<Restaurant> stream = allRestaurants.stream();
        String finalSearchString = searchString.toLowerCase();
        String finalKeywords = keywords.toLowerCase();

        // Filter by the main search string
        if (!finalSearchString.isEmpty()) {
            stream = stream.filter(r -> r.getName().toLowerCase().contains(finalSearchString));
        }

        // Also filter by the keywords within the name
        if (!finalKeywords.isEmpty()) {
            stream = stream.filter(r -> r.getName().toLowerCase().contains(finalKeywords));
        }
        return stream.collect(Collectors.toList());
    }

    private List<Item> searchForItems(String searchString, String price, String keywords) {
        //TODO: do the stuff in backend

//        return new ArrayList<>();
        Stream<Item> stream = allItems.stream();
        if (searchString != null && !searchString.isEmpty()) {
            stream = stream.filter(i -> i.getName().toLowerCase().contains(searchString.toLowerCase()));
        }
        if (price != null && !price.isEmpty()) {
            try {
                double maxPrice = Double.parseDouble(price);
                stream = stream.filter(i -> i.getPrice() <= maxPrice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format, ignoring price filter.");
            }
        }
        if (keywords != null && !keywords.isEmpty()) {
            stream = stream.filter(i -> i.getKeywords().contains(keywords.toLowerCase()));
        }
        return stream.collect(Collectors.toList());
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
        Map<String, Object> request =  new HashMap<>();
        HttpResponseData response = getItemsWithFilter(gson.toJson(request));
        JsonArray itemArray = response.getBody().getAsJsonArray("List of items");
        List<Item> items = new ArrayList<>();
        for(JsonElement itemElement : itemArray) {
            Item item = gson.fromJson(itemElement, Item.class);
            items.add(item);
        }

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

class MockDataFactory {
    /**
     * CORRECTED MOCK DATA
     * Creates mock restaurants without a keywords field.
     */
    public static List<Restaurant> createMockRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        // Note: In a real app, the Seller object would be fully populated.
        // The constructor no longer takes a list of keywords.
        restaurants.add(new Restaurant("Pizza Palace Italian", new Seller(), "123 Main St", "555-1111", null, 0.0, 0.0));
        restaurants.add(new Restaurant("Burger Barn Grill", new Seller(), "456 Oak Ave", "555-2222", null, 0.0, 0.0));
        restaurants.add(new Restaurant("Sushi Station Japanese", new Seller(), "789 Pine Ln", "555-3333", null, 0.0, 0.0));
        restaurants.add(new Restaurant("Taco Town Mexican", new Seller(), "101 Maple Rd", "555-4444", null, 0.0, 0.0));
        return restaurants;
    }

    public static List<Item> createMockItems(List<Restaurant> restaurants) {
        List<Item> items = new ArrayList<>();
        Restaurant pizzaPlace = restaurants.get(0);
        Restaurant burgerPlace = restaurants.get(1);
        Restaurant sushiPlace = restaurants.get(2);
        Restaurant tacoPlace = restaurants.get(3);

        items.add(new Item("Pepperoni Pizza", null, "Classic cheese and pepperoni.", 12.99, 50, List.of("pizza", "meat"), pizzaPlace, 4.5));
        items.add(new Item("Margherita Pizza", null, "Fresh tomatoes, mozzarella, and basil.", 10.99, 30, List.of("pizza", "vegetarian"), pizzaPlace, 4.8));
        items.add(new Item("Classic Burger", null, "A juicy all-beef patty.", 9.99, 100, List.of("burger", "beef"), burgerPlace, 4.2));
        items.add(new Item("Bacon Cheeseburger", null, "Classic burger with bacon and cheese.", 11.99, 80, List.of("burger", "cheese"), burgerPlace, 4.6));
        items.add(new Item("California Roll", null, "Crab, avocado, and cucumber.", 8.50, 60, List.of("sushi", "crab"), sushiPlace, 4.7));
        items.add(new Item("Spicy Tuna Roll", null, "Tuna with a spicy kick.", 9.50, 55, List.of("sushi", "tuna", "spicy"), sushiPlace, 4.9));
        items.add(new Item("Spicy Beef Taco", null, "Seasoned ground beef in a crispy shell.", 3.50, 200, List.of("taco", "beef", "spicy"), tacoPlace, 4.3));
        items.add(new Item("Chicken Taco", null, "Grilled chicken with fresh salsa.", 3.75, 150, List.of("taco", "chicken"), tacoPlace, 4.4));
        return items;
    }
}