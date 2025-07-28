package frontend.bemirfoodclient.controller.restaurant.buyer.item;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.restaurant.buyer.ViewRatingsDialogController;
import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static HttpClientHandler.Requests.getCartItemQuantity;
import static HttpClientHandler.Requests.modifyCartItems;
import static frontend.bemirfoodclient.controller.border.BuyerBorderController.staticMainBorderPane;

public class BuyerItemCardController {

    @FXML
    public ImageView itemImage;
    @FXML
    public Label itemName;
    @FXML
    public Label itemDescription;
    @FXML
    public Label itemPrice;
    @FXML
    public Label itemRating;
    @FXML
    public ImageView ratingStarImage;
    @FXML
    public Label itemKeywords;
    @FXML
    public Label quantity;
    @FXML
    public Button addToCartButton;
    @FXML
    public Button minus;
    @FXML
    public Button plus;
    @FXML
    public HBox addToCart;
    @FXML
    public Button viewRatingsButton;
    public Region spacer;

    private Item item;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();

    public void setItemData(Item item) {
        this.item = item;
        setScene();
    }

    public void initialize() {
        itemImage.setPreserveRatio(true);
        itemImage.setFitHeight(120);
        ratingStarImage.setPreserveRatio(true);
        ratingStarImage.setFitHeight(18);

        HBox.setHgrow(spacer, Priority.ALWAYS);
    }

    public void setScene() {
        setItemPhoto();
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemPrice.setText("$" + String.valueOf(item.getPrice()));
        itemRating.setText(String.valueOf(item.getRating()));

        List<String> keywordsList = item.getKeywords();
        itemKeywords.setText(String.join(", ", keywordsList));

        if (getItemQuantity() == 0) {
            addToCart.setVisible(true);
            addToCart.setManaged(true);
            plus.setVisible(false);
            minus.setVisible(false);
        } else {
            addToCart.setVisible(false);
            addToCart.setManaged(false);
            plus.setVisible(true);
            minus.setVisible(true);
        }

        quantity.setText(String.valueOf(getItemQuantity()));
    }

    @FXML
    void viewRatingsButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                    "/frontend/bemirfoodclient/restaurant/buyer/view-ratings-dialog.fxml"
            ));
            DialogPane dialogPane = loader.load();

            ViewRatingsDialogController controller = loader.getController();
            controller.setItemData(this.item);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(staticMainBorderPane.getScene().getWindow());
            dialog.setDialogPane(dialogPane);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.setTitle("Ratings & Reviews");
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setItemPhoto() {
        //do the stuff in backend
        boolean profilePhotoExists = false;
        if (profilePhotoExists) {
            String base64Uri = null;
            ImageLoader.displayBase64Image(base64Uri, itemImage);
        } else {
            itemImage.setImage(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
            "/frontend/bemirfoodclient/assets/icons/addItem.png"))));
        }
    }

    public int getItemQuantity() {
        HttpResponseData response = getCartItemQuantity(item.getId());
        JsonObject body = response.getBody();
        JsonObject cartItemObj = body.getAsJsonObject("Cart item");
        if(cartItemObj != null && !cartItemObj.get("quantity").isJsonNull()) {
            return cartItemObj.get("quantity").getAsInt();
        }else{
            return 0;
        }
    }

    public void addItemToCart() {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("item_id", item.getId());
        request.put("quantity", 1);
        HttpResponseData response = modifyCartItems(gson.toJson(request));
        setScene();
    }

    @FXML
    public void plusButtonClicked() {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("item_id", item.getId());
        request.put("quantity", 1);
        HttpResponseData response = modifyCartItems(gson.toJson(request));
        setScene();
    }

    @FXML
    public void minusButtonClicked() {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("item_id", item.getId());
        request.put("quantity", -1);
        HttpResponseData response = modifyCartItems(gson.toJson(request));
        setScene();
    }
}
