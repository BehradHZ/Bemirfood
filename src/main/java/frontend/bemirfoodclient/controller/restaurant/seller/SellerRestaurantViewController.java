package frontend.bemirfoodclient.controller.restaurant.seller;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.seller.item.AddItemDialogController;
import frontend.bemirfoodclient.controller.restaurant.seller.item.EditItemDialogController;
import frontend.bemirfoodclient.controller.restaurant.seller.item.SellerItemCardController;
import frontend.bemirfoodclient.controller.restaurant.seller.menu.AddMenuDialogController;
import frontend.bemirfoodclient.controller.restaurant.seller.menu.SellerMenuCardController;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Item;
import frontend.bemirfoodclient.model.entity.Order;
import frontend.bemirfoodclient.model.entity.Restaurant;
import frontend.bemirfoodclient.model.entity.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class SellerRestaurantViewController {
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
            switch (editRestaurant(updatedRestaurant.get())) {
                case 200:
                    this.restaurant = updatedRestaurant.get();
                    setScene();
                    break;
                //show alerts
            }
        }

    }

    private int editRestaurant(Restaurant restaurant) {
        //do the stuff in backend

        return 200; //temporary
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
                    switch (deleteItem(item)) {
                        case 200:
                            itemsSection.getChildren().remove(card);
                            break;
                        //show alerts
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
            switch (addItem(newItem.get())) {
                case 200:
                    itemsButtonClicked();
                    break;
                //show alerts
            }
        }

        itemsButtonClicked();

    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
//        items.addAll(restaurant.getItems());
        items.add(new Item("Kebab", null, "Kebab koobide asl ghafghaz!", 65.50, 5, keywords, restaurant, 5.0));
        items.add(new Item("joojeh", null, "joojeh kabab bahal!", 75.50, 55, keywords, restaurant, 3.2));
//              items.add(new Item("berenj", null, "berenje shomal!", 65.50, 5, keywords, restaurant, 1.2));
//        items.add(new Item("berenj", null, "berenje shomal!", 65.50, 5, keywords, restaurant, 1.2));

        return items;
    }

    private int addItem(Item item) {
        //de the stuff in backend

        return 200; //temporary
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

            switch (editItem(updatedItem)) {
                case 200:
                    cardController.setItemData(updatedItem);
                    itemsButtonClicked();
                    break;
                //show alerts
            }
        }
    }

    private int editItem(Item item) {
        //do the stuff in backend

        return 200;
    }

    private int deleteItem(Item item) {
        //do the stuff in backend

        return 200; //temporary
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
                    switch (deleteMenu(menu)) {
                        case 200:
                            menusSection.getChildren().remove(card);
                            break;
                        //show alerts
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
            switch (addNewMenu(newMenuName.get())){
                case 200:
                menusButtonClicked();
                break;
                //show alerts
            }
        }
    }

    public List<Menu> getMenus() {
        menus.add(new Menu("menu1", restaurant, getItems()));
        menus.add(new Menu("menu2", restaurant, getItems()));
        menus.add(new Menu("menu3", restaurant, getItems()));

        return menus;
    }

    private int addNewMenu(String menuName) {
        //do the stuff in backend

        return 200; //temporary
    }

    private int deleteMenu(Menu menu) {
        //do the stuff in backend

        return 200; //temporary
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
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();

        return orders;
    }
}
