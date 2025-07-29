package frontend.bemirfoodclient.controller.restaurant.seller;

import Deserializer.RestaurantDeserializer;
import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.*;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.seller.item.AddItemDialogController;
import frontend.bemirfoodclient.controller.restaurant.seller.item.EditItemDialogController;
import frontend.bemirfoodclient.controller.restaurant.seller.item.SellerItemCardController;
import frontend.bemirfoodclient.controller.restaurant.seller.menu.AddMenuDialogController;
import frontend.bemirfoodclient.controller.restaurant.seller.menu.SellerMenuCardController;
import frontend.bemirfoodclient.controller.restaurant.seller.order.SellerOrderCardController;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Item;
import frontend.bemirfoodclient.model.entity.Menu;
import frontend.bemirfoodclient.model.entity.Order;
import frontend.bemirfoodclient.model.entity.Restaurant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static BuildEntity.builder.buildOrderList;
import static HttpClientHandler.Requests.*;
import static exception.exp.expHandler;

public class SellerRestaurantViewController {

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
    public ImageView editRestaurantDetails;
    @FXML
    public Region tabSpacer;
    @FXML
    public Button itemsSmall;
    @FXML
    public Button itemsLarge;
    @FXML
    public Button menusSmall;
    @FXML
    public Button menusLarge;
    @FXML
    public Button ordersSmall;
    @FXML
    public Button ordersLarge;
    @FXML
    public VBox itemsSection;
    @FXML
    public VBox menusSection;
    @FXML
    public VBox ordersSection;
    @FXML
    public Button addItemButton;
    @FXML
    public Button addMenuButton;
    @FXML
    public ImageView addItemButtonImage;
    @FXML
    public ImageView addMenuButtonImage;
    @FXML
    public ScrollPane itemsSectionScrollPane;
    @FXML
    public ScrollPane menusSectionScrollPane;
    @FXML
    public ScrollPane ordersSectionScrollPane;
    public ImageView favoriteRestaurantStar;

    private Restaurant restaurant;

    private List<String> keywords = new ArrayList<>();

    List<Menu> menus = new ArrayList<>();

    public void setRestaurantData(Restaurant restaurant) {
        this.restaurant = restaurant;
        setScene();
        itemsButtonClicked();
    }

    public void initialize() {
        restaurantLogo.setPreserveRatio(true);
        restaurantLogo.setFitHeight(120);

        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        editRestaurantDetails.setPreserveRatio(true);
        editRestaurantDetails.setFitHeight(23);

        HBox.setHgrow(tabSpacer, Priority.ALWAYS);

        addItemButtonImage.setPreserveRatio(true);
        addItemButtonImage.setFitHeight(40);

        addMenuButtonImage.setPreserveRatio(true);
        addMenuButtonImage.setFitHeight(40);

//        keywords.add("kebab");
//        keywords.add("irani");
//        keywords.add("madagascar");
    }

    private void setScene() {
        setRestaurantLogo();

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

    public void editRestaurantButtonClicked() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Restaurant");
        dialog.initOwner(restaurantLogo.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader(BemirfoodApplication.class.getResource(
                "/frontend/bemirfoodclient/restaurant/seller/edit-restaurant-dialog-view.fxml"
        ));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        EditRestaurantDialogController controller = fxmlLoader.getController();
        controller.setRestaurantData(this.restaurant);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        AtomicReference<Restaurant> updatedRestaurant = new AtomicReference<>();
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            updatedRestaurant.set(controller.editRestaurant());

            if (updatedRestaurant.get() == null) {
                event.consume();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            HttpResponseData response = editRestaurant(updatedRestaurant.get());
            if(response.getStatusCode() == 200) {
                this.restaurant = updatedRestaurant.get();
                setScene();
            }else{
                expHandler(response, "Failed update restaurant info", null);
            }
        }

    }

    private HttpResponseData editRestaurant(Restaurant restaurant) {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("name", restaurant.getName());
        request.put("address", restaurant.getAddress());
        request.put("phone", restaurant.getPhone());
        request.put("logo", restaurant.getLogo());
        request.put("tax-fee",  restaurant.getTaxFee());
        request.put("additional_fee",  restaurant.getAdditionalFee());

        return editSellerRestaurant(gson.toJson(request), (long)restaurant.getId());
    }

    public void itemsButtonClicked() {
        itemsLarge.setVisible(true);
        itemsSectionScrollPane.setVisible(true);
        menusLarge.setVisible(false);
        menusSectionScrollPane.setVisible(false);
        ordersLarge.setVisible(false);
        ordersSectionScrollPane.setVisible(false);

        addItemButton.setVisible(true);
        addMenuButton.setVisible(false);

        itemsSection.setVisible(true);
        menusSection.setVisible(false);
        ordersSection.setVisible(false);

        itemsSection.getChildren().clear();
        List<Item> items = getItems();

        for (Item item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/seller/item/item-card.fxml"
                ));
                Pane card = loader.load();
                SellerItemCardController controller = loader.getController();
                controller.setItemData(item);

                controller.setOnEdit(itemToEdit -> {
                    editItemButtonClicked(item, controller);
                });

                controller.setOnDelete(itemToDelete -> {

                    HttpResponseData response = deleteItem(item);
                    if(response.getStatusCode() == 200) {
                        itemsSection.getChildren().remove(card);
                    }else{
                        expHandler(response, "Failed delete item", null);
                    }
                });

                itemsSection.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addItemButtonClicked() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Item");
        dialog.initOwner(restaurantLogo.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/frontend/bemirfoodclient/restaurant/seller/item/add-item-dialog-view.fxml"
        ));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        AddItemDialogController controller = fxmlLoader.getController();

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        AtomicReference<Item> newItem = new AtomicReference<>();
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            Item item = controller.addNewItem();

            if (item == null) {
                event.consume();
            } else {
                newItem.set(item);
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            HttpResponseData response = addItem(newItem.get());
            if(response.getStatusCode() == 200) {
                itemsButtonClicked();
            }else{
                expHandler(response, "Failed add item to restaurant", null);
            }
        }

        itemsButtonClicked();
    }


    private HttpResponseData addItem(Item item) {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("name", item.getName());
        request.put("imageBase64", item.getPhoto());
        request.put("description", item.getDescription());
        request.put("price", item.getPrice());
        request.put("supply", item.getSupply());
        request.put("keywords", item.getKeywords());

        return addRestaurantItem(gson.toJson(request), restaurant.getId());

    }

    public List<Item> getItems() {

        Gson g = new GsonBuilder()
                .registerTypeAdapter(Restaurant.class, new RestaurantDeserializer())
                .create();

        HttpResponseData response = getRestaurantItemsSeller(restaurant.getId());
        JsonArray itemArray = response.getBody().getAsJsonArray("Restaurant items");
        List<Item> items = new ArrayList<>();
        for(JsonElement itemElement : itemArray) {
            Item item = g.fromJson(itemElement, Item.class);
            items.add(item);
        }


        return items;
    }


    public void editItemButtonClicked(Item item, SellerItemCardController cardController) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Item");
        dialog.initOwner(restaurantLogo.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/frontend/bemirfoodclient/restaurant/seller/item/edit-item-dialog-view.fxml"
        ));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        EditItemDialogController dialogController = fxmlLoader.getController();
        dialogController.setItemData(item);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (dialogController.editItem() == null) {
                event.consume();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Item updatedItem = dialogController.editItem();
            HttpResponseData response = editItem(updatedItem);
            if(response.getStatusCode() == 200) {
                cardController.setItemData(updatedItem);
                itemsButtonClicked();
            }else{
                expHandler(response, "Failed edit item to restaurant", null);
            }
        }
    }

    private HttpResponseData editItem(Item item) {
        Map<String, Object> request = new LinkedHashMap<>();
        if(item.getName() != null) request.put("name", item.getName());
        if(item.getPhoto() != null) request.put("imageBase64", item.getPhoto());
        if(item.getDescription() != null) request.put("description", item.getDescription());
        if(item.getPrice() != 0) request.put("price", item.getPrice());
        if(item.getSupply() != null) request.put("supply", item.getSupply());
        if(item.getKeywords() != null) request.put("keywords", item.getKeywords());

        return editRestaurantItem(gson.toJson(request), restaurant.getId(), item.getId());
    }

    private HttpResponseData deleteItem(Item item) {
        return deleteRestaurantItem(restaurant.getId(), item.getId());
    }

    public void menusButtonClicked() {
        itemsLarge.setVisible(false);
        itemsSectionScrollPane.setVisible(false);
        menusLarge.setVisible(true);
        menusSectionScrollPane.setVisible(true);
        ordersLarge.setVisible(false);
        ordersSectionScrollPane.setVisible(false);

        addItemButton.setVisible(false);
        addMenuButton.setVisible(true);

        itemsSection.setVisible(false);
        menusSection.setVisible(true);
        ordersSection.setVisible(false);

        menusSection.getChildren().clear();
        List<Menu> menus = getMenus();

        for (Menu menu : menus) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/seller/menu/menu-card.fxml"
                ));
                Pane card = loader.load();
                SellerMenuCardController controller = loader.getController();
                controller.setMenuData(menu);

                controller.setOnDelete(menuToDelete -> {
                    HttpResponseData response = deleteMenu(menu);
                    if(response.getStatusCode() == 200) {
                        menusSection.getChildren().remove(card);
                    }else{
                        expHandler(response, "Failed delete menu from restaurant", null);
                    }
                });

                menusSection.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMenuButtonClicked() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Menu");
        dialog.initOwner(addMenuButton.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/frontend/bemirfoodclient/restaurant/seller/menu/add-menu-dialog-view.fxml"
        ));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        AddMenuDialogController dialogController = fxmlLoader.getController();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        AtomicReference<String> newMenuName = new AtomicReference<>();

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            String menuName = dialogController.getNewMenuName();
            if (menuName == null) {
                event.consume();
            } else {
                newMenuName.set(menuName);
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            HttpResponseData response = addNewMenu(newMenuName.get());
            if (response.getStatusCode() == 200) {
                menusButtonClicked();
            }else {
                expHandler(response, "Failed to add new menu", null);
            }
        }
    }

    public List<Menu> getMenus() {

        menus = new ArrayList<>();

        Gson g = new GsonBuilder()
                .registerTypeAdapter(Restaurant.class, new RestaurantDeserializer())
                .create();

        HttpResponseData response = getRestaurantMenusSeller(restaurant.getId());
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

    private HttpResponseData addNewMenu(String menuName) {
        Map<String, String> request = new LinkedHashMap<>();
        if(menuName != null) request.put("title", menuName);
        return addRestaurantMenus(restaurant.getId(),gson.toJson(request));
    }

    private HttpResponseData deleteMenu(Menu menu) {
         return removeRestaurantMenus(restaurant.getId(), menu.getTitle());
    }


    public void ordersButtonClicked() {
        itemsLarge.setVisible(false);
        itemsSectionScrollPane.setVisible(false);
        menusLarge.setVisible(false);
        menusSectionScrollPane.setVisible(false);
        ordersLarge.setVisible(true);
        ordersSectionScrollPane.setVisible(true);

        addItemButton.setVisible(false);
        addMenuButton.setVisible(false);

        itemsSection.setVisible(false);
        menusSection.setVisible(false);
        ordersSection.setVisible(true);

        ordersSection.getChildren().clear();
        List<Order> orders = getOrders(); // Get orders from your backend
        Collections.reverse(orders);
        for (Order order : orders) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/seller/order/order-card.fxml"
                ));
                Pane card = loader.load();

                SellerOrderCardController controller = loader.getController();
                controller.setOrderData(order);

                ordersSection.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Order> getOrders() {
        return buildOrderList(args -> getRestaurantOrders((Long) args[0]), "List of orders", "Failed to get restaurant orders", restaurant.getId());
    }
}
