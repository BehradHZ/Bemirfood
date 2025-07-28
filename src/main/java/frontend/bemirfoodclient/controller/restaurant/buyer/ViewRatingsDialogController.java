package frontend.bemirfoodclient.controller.restaurant.buyer;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Item;
import frontend.bemirfoodclient.model.entity.ItemRating;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        /*// Create some mock users to associate with the ratings
        User user1 = new User("John Doe", "555-0101", "buyer", "john@email.com", null, "123 Oak St", null, "pass1");
        User user2 = new User("Jane Smith", "555-0102", "buyer", "jane@email.com", null, "456 Pine St", null, "pass2");

        // --- Mock Rating 1 ---
        ItemRating rating1 = new ItemRating(1L, 5, "Absolutely delicious! Best pizza in town. Will order again for sure.", new ArrayList<>(), LocalDateTime.now().minusDays(1), user1, itemId);
        ratings.add(rating1);

        // --- Mock Rating 2 (with a mock image) ---
        List<String> mockImages = new ArrayList<>();
        // This is a tiny, valid Base64 string for a red dot image for testing.
        mockImages.add("iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==");
        ItemRating rating2 = new ItemRating(2L, 4, "Pretty good, but a bit cold when it arrived.", mockImages, LocalDateTime.now().minusHours(5), user2, itemId);
        ratings.add(rating2);*/

        return ratings;

        /*TODO: do the stuff in backend
        *  take a look at ItemRating entity*/
    }
}
