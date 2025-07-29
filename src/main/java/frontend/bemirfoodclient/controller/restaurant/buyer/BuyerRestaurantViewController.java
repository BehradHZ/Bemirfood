package frontend.bemirfoodclient.controller.restaurant.buyer;

import Deserializer.RestaurantDeserializer;
import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.*;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.buyer.item.BuyerItemCardController;
import frontend.bemirfoodclient.controller.restaurant.buyer.menu.BuyerMenuCardController;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Item;
import frontend.bemirfoodclient.model.entity.Menu;
import frontend.bemirfoodclient.model.entity.Restaurant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static HttpClientHandler.Requests.*;

public class BuyerRestaurantViewController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();

    @FXML
    public ImageView restaurantLogo;
    @FXML
    public Label restaurantName;
    @FXML
    public Label restaurantAddress;
    @FXML
    public Label restaurantPhone;
    @FXML
    public Region headerSpacer;
    @FXML
    public Button itemsSmall;
    @FXML
    public Button itemsLarge;
    @FXML
    public Button menusSmall;
    @FXML
    public Button menusLarge;
    @FXML
    public VBox itemsSection;
    @FXML
    public VBox menusSection;
    @FXML
    public ScrollPane itemsSectionScrollPane;
    @FXML
    public ScrollPane menusSectionScrollPane;
    @FXML
    public ToggleButton favoriteToggle;
    @FXML
    public ImageView favoriteRestaurantStar;

    private Restaurant restaurant;

    private List<String> keywords = new ArrayList<>(); //temporary

    List<Menu> menus = new ArrayList<>(); //temporary

    public void setRestaurantData(Restaurant restaurant) {
        this.restaurant = restaurant;
        setScene();
        itemsButtonClicked();
    }

    public void initialize() {
        restaurantLogo.setPreserveRatio(true);
        restaurantLogo.setFitHeight(120);

        favoriteRestaurantStar.setPreserveRatio(true);
        favoriteRestaurantStar.setFitHeight(30);

        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

    }

    private void setScene() {
        setRestaurantLogo();
        setFavoriteRestaurantStar();

        restaurantName.setText(restaurant.getName());
        restaurantAddress.setText(restaurant.getAddress());
        restaurantPhone.setText(restaurant.getPhone());


    }

    private void setRestaurantLogo() {
        //do the stuff in backend
        boolean profilePhotoExists = false;
        if (profilePhotoExists) {
            String base64Uri = null;
            ImageLoader.displayBase64Image(base64Uri, restaurantLogo);
        } else {
            restaurantLogo.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("" +
                    "/frontend/bemirfoodclient/assets/icons/addRestaurant.png"))));
        }
    }

    private void setFavoriteRestaurantStar() {
        boolean isThisRestaurantInFavorite = false;
        HttpResponseData response = getCustomerFavorites();
        JsonArray array = response.getBody().getAsJsonObject().get("List of favorite restaurants").getAsJsonArray();
        for(JsonElement el : array) {
            JsonObject res = el.getAsJsonObject();
            if(restaurant.getId() == res.get("id").getAsInt()){
                isThisRestaurantInFavorite = true;
                break;
            }
        }

        if (isThisRestaurantInFavorite) {
            favoriteRestaurantStar.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
            "/frontend/bemirfoodclient/assets/icons/favorite.png")
            )));
        } else {
            favoriteRestaurantStar.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
            "/frontend/bemirfoodclient/assets/icons/notFavorite.png")
            )));
        }
    }

    public void itemsButtonClicked() {
        itemsLarge.setVisible(true);
        itemsSectionScrollPane.setVisible(true);
        menusLarge.setVisible(false);
        menusSectionScrollPane.setVisible(false);

        itemsSection.setVisible(true);
        menusSection.setVisible(false);

        itemsSection.getChildren().clear();
        List<Item> items = getItems();

        for (Item item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/buyer/item/item-card.fxml"
                ));
                Pane card = loader.load();
                BuyerItemCardController controller = loader.getController();
                controller.setItemData(item);

                itemsSection.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Item> getItems() {
        Gson g = new GsonBuilder()
                .registerTypeAdapter(Restaurant.class, new RestaurantDeserializer())
                .create();

        HttpResponseData response = getRestaurantItemsCustomer(restaurant.getId());
        JsonArray itemArray = response.getBody().getAsJsonArray("Vendor items");
        List<Item> items = new ArrayList<>();
        for(JsonElement itemElement : itemArray) {
            Item item = g.fromJson(itemElement, Item.class);
            HttpResponseData res = getItemAvgRating(item.getId());
            JsonObject array = res.getBody().getAsJsonObject("List of ratings and reviews");
            item.setRating(array.get("avg_rating").getAsDouble());
            items.add(item);
        }

        return items;
    }

    public void menusButtonClicked() {
        itemsLarge.setVisible(false);
        itemsSectionScrollPane.setVisible(false);
        menusLarge.setVisible(true);
        menusSectionScrollPane.setVisible(true);

        itemsSection.setVisible(false);
        menusSection.setVisible(true);

        menusSection.getChildren().clear();
        List<Menu> menus = getMenus();

        for (Menu menu : menus) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/buyer/menu/menu-card.fxml"
                ));
                Pane card = loader.load();
                BuyerMenuCardController controller = loader.getController();
                controller.setMenuData(menu);

                menusSection.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Menu> getMenus() {

        menus = new ArrayList<>();

        Gson g = new GsonBuilder()
                .registerTypeAdapter(Restaurant.class, new RestaurantDeserializer())
                .create();

        HttpResponseData response = getRestaurantMenusCustomer(restaurant.getId());
        JsonObject body = response.getBody();
        JsonArray restaurantMenus = body.getAsJsonArray("Restaurant menus");

        for (JsonElement menuElement : restaurantMenus) {
            JsonObject menuObj = menuElement.getAsJsonObject();
            String title = menuObj.get("title").getAsString();

            List<Item> menuItems = new ArrayList<>();

            JsonArray items = menuObj.getAsJsonArray("items");

            for (JsonElement itemElement : items) {
                JsonObject item = itemElement.getAsJsonObject();
                int id = item.get("id").getAsInt();
                String name = item.get("name").getAsString();
                String image = null;
                if(item.get("imageBase64") != null && !item.get("imageBase64").isJsonNull()) image = item.get("imageBase64").getAsString();
                String description = item.get("description").getAsString();
                double price = item.get("price").getAsDouble();
                int supply = item.get("supply").getAsInt();
                JsonArray keywords = item.getAsJsonArray("keywords");

                List<String> keywordList = new ArrayList<>();
                for (JsonElement keyword : keywords) {
                    keywordList.add(keyword.getAsString());
                }

                menuItems.add(new Item((long)id, name, image, description, price, supply, keywordList));
            }

            menus.add(new Menu(title, restaurant, menuItems));
        }

        return menus;
    }

    public void favoriteRestaurantButtonClicked(MouseEvent event) {
        boolean status = true;
        HttpResponseData response = addToFavorites(restaurant.getId());
        if(response.getStatusCode() == 409) {
            response = removeFromFavorites(restaurant.getId());
            status = false;
        }
        int code = response.getStatusCode();
        if (code == 200) {
            //favoriteToggle.isSelected()
            if (status) {
                favoriteRestaurantStar.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                        "/frontend/bemirfoodclient/assets/icons/favorite.png")
                )));
            } else {
                favoriteRestaurantStar.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                        "/frontend/bemirfoodclient/assets/icons/notFavorite.png")
                )));
            }
        }
    }
}
