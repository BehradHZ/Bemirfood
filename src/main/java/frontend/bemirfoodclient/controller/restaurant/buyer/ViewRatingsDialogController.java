package frontend.bemirfoodclient.controller.restaurant.buyer;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Item;
import frontend.bemirfoodclient.model.entity.ItemRating;
import frontend.bemirfoodclient.model.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static HttpClientHandler.Requests.getItemAvgRating;

public class ViewRatingsDialogController {

    @FXML private Label itemNameLabel;
    @FXML private Label averageRatingLabel;
    @FXML private VBox commentsVBox;

    public void setItemData(Item item) {
        if (item == null) return;

        itemNameLabel.setText(item.getName());
        averageRatingLabel.setText(String.format("%.1f", item.getRating()));

        // Use the new mock method for ItemRating
        List<ItemRating> ratings = getRatingsForItem(item.getId());

        // Populate the VBox with comment cards
        commentsVBox.getChildren().clear();
        for (ItemRating rating : ratings) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/restaurant/buyer/comment-card.fxml"
                ));
                Pane card = loader.load();
                CommentCardController controller = loader.getController();
                controller.setData(rating);
                commentsVBox.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<ItemRating> getRatingsForItem(long itemId) {
        List<ItemRating> ratings = new ArrayList<>();
        HttpResponseData res = getItemAvgRating(itemId);
        JsonObject jsonObject = res.getBody().get("List of ratings and reviews").getAsJsonObject();
        Integer avgRatings = jsonObject.get("avg_rating").getAsInt();
        JsonArray array = jsonObject.get("comments").getAsJsonArray();

        for(JsonElement itemElement : array) {
            JsonObject obj = itemElement.getAsJsonObject();
            Integer rating = obj.get("rating").getAsInt();
            String comment = obj.get("comment").getAsString();
            JsonArray imageArray = obj.get("imageBase64").getAsJsonArray();
            List<String> images = new ArrayList<>();
            for (JsonElement element : imageArray) {
                images.add(element.getAsString());
            }
            LocalDateTime createdAt = LocalDateTimeAdapter.StringToTime(obj.get("created_at").getAsString());
            User user = new User();
            user.setId(obj.get("userId").getAsLong());
            ItemRating itemRating = new ItemRating(
                    (long)0,rating, comment, images, createdAt, user, itemId
            );
            ratings.add(itemRating);
        }
        return ratings;

        /*TODO: do the stuff in backend
        *  take a look at ItemRating entity*/
    }
}
