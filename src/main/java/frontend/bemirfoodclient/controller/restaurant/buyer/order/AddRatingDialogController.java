package frontend.bemirfoodclient.controller.restaurant.buyer.order;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.OrderRating;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class AddRatingDialogController {

    @FXML private HBox starsContainer;
    @FXML private TextArea commentTextArea;
    @FXML private Button addImageButton;

    private OrderRating existingRating;

    public void setExistingRating(BuyerOrderCardController.OrderRatingDto ratingDto) {
        //this.existingRating = rating;
        if (ratingDto != null) {
            this.currentRating = ratingDto.getRating();
            this.commentTextArea.setText(ratingDto.getComment());
            if (ratingDto.getImageBase64() != null) {
                this.imageBase64List.addAll(ratingDto.getImageBase64());
                updateImageButtonText();
            }
            updateStars();
        }
    }

    private void updateImageButtonText() {
        int count = imageBase64List.size();
        addImageButton.setText(count + " image(s) selected");
    }

    private int currentRating = 0;
    private final List<ImageView> starImageViews = new ArrayList<>();
    private final List<String> imageBase64List = new ArrayList<>();
    private long orderId;

    private final Image starEmpty = new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
            "/frontend/bemirfoodclient/assets/icons/notFavorite.png")));
    private final Image starFilled = new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
            "/frontend/bemirfoodclient/assets/icons/favorite.png")));

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @FXML
    public void initialize() {
        // Create 5 stars for rating
        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView(starEmpty);
            star.setFitHeight(30);
            star.setPreserveRatio(true);
            star.setUserData(i); // Store the star's value (1 to 5)
            star.setOnMouseClicked(this::starClicked);
            starImageViews.add(star);
            starsContainer.getChildren().add(star);
        }
    }

    private void starClicked(MouseEvent event) {
        ImageView clickedStar = (ImageView) event.getSource();
        currentRating = (int) clickedStar.getUserData();
        updateStars();
    }

    private void updateStars() {
        for (ImageView star : starImageViews) {
            int starValue = (int) star.getUserData();
            if (starValue <= currentRating) {
                star.setImage(starFilled);
            } else {
                star.setImage(starEmpty);
            }
        }
    }

    @FXML
    void addImageClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Rating Images");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            imageBase64List.clear();

            for (File file : selectedFiles) {
                try {
                    // Read file bytes
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    // Encode to Base64 using the standard java.util.Base64
                    String base64String = Base64.getEncoder().encodeToString(fileContent);
                    imageBase64List.add(base64String);
                } catch (IOException e) {
                    System.err.println("Error while reading or encoding the image file: " + file.getName());
                    e.printStackTrace();
                }
            }
            // Update button text to give user feedback
            int count = imageBase64List.size();
            addImageButton.setText(count + " image(s) selected");
        }
    }

    public Map<String, Object> processResult() {
        if (currentRating == 0) {
            showAlert("Validation Error", "Rating is required. Please select 1 to 5 stars.");
            return null;
        }
        if (commentTextArea.getText() == null || commentTextArea.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Comment is required.");
            return null;
        }

        Map<String, Object> ratingData = new HashMap<>();
        ratingData.put("order_id", this.orderId);
        ratingData.put("rating", this.currentRating);
        ratingData.put("comment", commentTextArea.getText().trim());
        ratingData.put("imageBase64", this.imageBase64List);

        return ratingData;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
