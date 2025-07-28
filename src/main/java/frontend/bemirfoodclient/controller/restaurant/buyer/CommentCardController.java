package frontend.bemirfoodclient.controller.restaurant.buyer;

import frontend.bemirfoodclient.model.ImageLoader;
import frontend.bemirfoodclient.model.entity.ItemRating;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommentCardController {

    @FXML private Label senderNameLabel;
    @FXML private Label dateTimeLabel;
    @FXML private Label commentTextLabel;
    @FXML private ScrollPane imageScrollPane;
    @FXML private FlowPane imageContainer;

    public void setData(ItemRating rating) { // Changed parameter type
        if (rating == null) return;

        // Set sender name from the user associated with the rating
        if (rating.getUser() != null) {
            senderNameLabel.setText(rating.getUser().getFull_name());
        } else {
            senderNameLabel.setText("Anonymous");
        }

        // Format and set the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        if (rating.getCreatedAt() != null) {
            dateTimeLabel.setText(rating.getCreatedAt().format(formatter));
        }

        // Set the comment text
        commentTextLabel.setText(rating.getComment());

        // Handle images
        List<String> base64Images = rating.getImageBase64();
        if (base64Images != null && !base64Images.isEmpty()) {
            imageScrollPane.setVisible(true);
            imageScrollPane.setManaged(true);
            imageContainer.getChildren().clear();

            for (String base64 : base64Images) {
                ImageView imageView = new ImageView();
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);
                imageView.setPreserveRatio(true);
                // Use the ImageLoader you created to display the images
                ImageLoader.displayBase64Image(base64, imageView);
                imageContainer.getChildren().add(imageView);
            }
        }
    }
}
