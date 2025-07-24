package frontend.bemirfoodclient.controller.restaurant.seller.menu;

import Deserializer.RestaurantDeserializer;
import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.*;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Item;
import frontend.bemirfoodclient.model.entity.Menu;
import frontend.bemirfoodclient.model.entity.Restaurant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

import static exception.exp.expHandler;
import static HttpClientHandler.Requests.*;

public class SellerMenuCardController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();


    @FXML
    public Label menuTitle;
    @FXML
    public VBox menuItemsSection;
    @FXML
    public ImageView deleteMenuButtonImageView;
    @FXML
    public ImageView addItemToMenuButtonImageView;

    private Menu menu;
    private Consumer<Menu> deleteMenuCallback;

    public void setMenuData(Menu menu) {
        this.menu = menu;
        setScene();
    }

    public void setOnDelete(Consumer<Menu> callback) {
        this.deleteMenuCallback = callback;
    }

    public void initialize() {
        deleteMenuButtonImageView.setPreserveRatio(true);
        deleteMenuButtonImageView.setFitHeight(20);
        addItemToMenuButtonImageView.setPreserveRatio(true);
        addItemToMenuButtonImageView.setFitHeight(23);
    }

    public void setScene() {
        menuTitle.setText(menu.getTitle());

        menuItemsSection.getChildren().clear();

        HttpResponseData response = getRestaurantMenu(menu.getRestaurant().getId(), menu.getTitle());
        JsonObject menuObj = response.getBody().getAsJsonObject("menu");
        String title = menuObj.get("title").getAsString();

        List<Item> menuItems = new ArrayList<>();

        JsonArray its = menuObj.getAsJsonArray("items");

        for (JsonElement itemElement : its) {
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

        menu = new Menu(title, menu.getRestaurant(), menuItems);

        List<Item> items = menu.getItems();

        for (Item item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/seller/menu/item-card-small-menu.fxml"
                ));
                Pane smallCard = loader.load();
                SellerMenuItemCardController smallCardController = loader.getController();
                smallCardController.setItemData(item);

                smallCardController.setOnDelete(itemToRemove -> {
                    HttpResponseData res = deleteItemFromMenu(item);
                    if(res.getStatusCode() == 200) {
                        menuItemsSection.getChildren().remove(smallCard);
                    }else{
                        expHandler(res, "Failed to delete item from menu", null);
                    }
                });

                menuItemsSection.getChildren().add(smallCard);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void addItemToMenuButtonClicked() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Items to " + menu.getTitle());
        dialog.initOwner(menuTitle.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/frontend/bemirfoodclient/restaurant/seller/menu/add-item-to-menu-dialog.fxml"
        ));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        AddItemToMenuDialogController dialogController = fxmlLoader.getController();

        //List<Item> allItems = menu.getRestaurant().getItems(); // Assuming this method exists
//        List<Item> allItems = menu.getItems(); //temporary

        Gson g = new GsonBuilder()
                .registerTypeAdapter(Restaurant.class, new RestaurantDeserializer())
                .create();

        HttpResponseData response = getRestaurantItemsSeller(menu.getRestaurant().getId());

        JsonArray itemArray = response.getBody().getAsJsonArray("Restaurant items");
        List<Item> allItems = new ArrayList<>();

        for(JsonElement itemElement : itemArray) {
            Item item = g.fromJson(itemElement, Item.class);
            allItems.add(item);
        }

        dialogController.populateItems(allItems);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            List<Item> itemsToAdd = dialogController.getSelectedItems();
            HttpResponseData res = addItemsToMenu(itemsToAdd);
            setScene();
            if(res.getStatusCode() == 200) {
                setScene();
            }else{
                expHandler(res, "Failed to add item to menu", null);
            }

        }
    }

    public HttpResponseData addItemsToMenu(List<Item> items) {
        HttpResponseData response = null;
        for (Item item : items) {
            Map<String, Long> request = new LinkedHashMap<>();
            request.put("item_id", item.getId());
            response =
                    addItemMenu(menu.getRestaurant().getId(), menu.getTitle(), gson.toJson(request));
            if(response.getStatusCode() != 200){
                expHandler(response, "Failed to add item to menu", null);
            }
        }
        return response;
    }

    public HttpResponseData deleteItemFromMenu(Item item) {
        return removeItemMenu(menu.getRestaurant().getId(), menu.getTitle(), (long)item.getId());
    }

    public void deleteMenuButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                "/frontend/bemirfoodclient/assets/icons/deleteRed.png"))));

        alert.setTitle("Delete Menu");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this menu?");
        alert.getDialogPane().setGraphic(null);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (deleteMenuCallback != null) {
                deleteMenuCallback.accept(this.menu);
            }
        }
    }
}
