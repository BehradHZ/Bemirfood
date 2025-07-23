package frontend.bemirfoodclient.controller.restaurant.buyer;

import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

//        keywords.add("kebab");
//        keywords.add("irani");
//        keywords.add("madagascar");
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
        //do the stuff in backend
        boolean isThisRestaurantInFavorite = false;
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
        //do the sstuff in backend

        //temporary
        List<Item> items = new ArrayList<>();
        items.add(new Item("Kebab", null, "Kebab koobide asl ghafghaz!", 65.50, 5, keywords, restaurant, 5.0));
        items.add(new Item("joojeh", null, "joojeh kabab bahal!", 75.50, 55, keywords, restaurant, 3.2));
//        items.add(new Item("berenj", null, "berenje shomal!", 65.50, 5, keywords, restaurant, 1.2));
//        items.add(new Item("berenj", null, "berenje shomal!", 65.50, 5, keywords, restaurant, 1.2));

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
//        List<Menu> menus = new ArrayList<>();
        menus.add(new Menu("menu1", restaurant, getItems()));
        menus.add(new Menu("menu2", restaurant, getItems()));
        menus.add(new Menu("menu3", restaurant, getItems()));

        return menus;
    }

    public void favoriteRestaurantButtonClicked(MouseEvent event) {
        //do the stuff in backend
        int code = 200; //temporary
        if (code == 200) {
            if (favoriteToggle.isSelected()) {
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
