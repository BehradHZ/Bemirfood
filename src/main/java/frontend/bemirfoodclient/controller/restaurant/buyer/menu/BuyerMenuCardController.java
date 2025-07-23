package frontend.bemirfoodclient.controller.restaurant.buyer.menu;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.seller.menu.AddItemToMenuDialogController;
import frontend.bemirfoodclient.controller.restaurant.seller.menu.SellerMenuItemCardController;
import frontend.bemirfoodclient.model.entity.Item;
import frontend.bemirfoodclient.model.entity.Menu;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class BuyerMenuCardController {

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
                    switch (deleteItemFromMenu(item)) {
                        case 200:
                            menuItemsSection.getChildren().remove(smallCard);
                            break;
                        //show alerts
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

        List<Item> allItems = menu.getRestaurant().getItems(); // Assuming this method exists
//        List<Item> allItems = menu.getItems(); //temporary
        dialogController.populateItems(allItems);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            List<Item> itemsToAdd = dialogController.getSelectedItems();

            switch (addItemsToMenu(itemsToAdd)) {
                case 200:
                    setScene();
                    break;
                //show alerts
            }

        }
    }

    public int addItemsToMenu(List<Item> items) {
        //do the stuff in backend

        return 200; //temporary
    }

    public int deleteItemFromMenu(Item item) {
        //do the stuff in backend

        return 200;
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
