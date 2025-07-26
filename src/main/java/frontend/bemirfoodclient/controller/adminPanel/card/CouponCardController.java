package frontend.bemirfoodclient.controller.adminPanel.card;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Coupon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class CouponCardController {

    @FXML
    public ImageView copyButton;
    @FXML
    public Label couponCode;
    @FXML
    public Region spacer;
    @FXML
    public Label validDateAndTime;
    @FXML
    public Label usersLeft;
    @FXML
    public Label type;
    @FXML
    public Label value;
    @FXML
    public Label minPrice;
    public ImageView deleteButtonIcon;

    private Coupon coupon;
    private Consumer<Coupon> onDelete;
    private Consumer<Coupon> onEdit;

    public void setCouponData(Coupon coupon) {
        this.coupon = coupon;
        setScene();
    }

    public void initialize() {
        copyButton.setPreserveRatio(true);
        copyButton.setFitWidth(20);

        HBox.setHgrow(spacer, Priority.ALWAYS);

        deleteButtonIcon.setPreserveRatio(true);
        deleteButtonIcon.setFitWidth(20);
    }

    private void setScene() {
        couponCode.setText(coupon.getCode());
        usersLeft.setText(coupon.getUserCount() + " users left");
        validDateAndTime.setText(formatValidity(coupon.getStartDate(), coupon.getEndDate()));

        type.setText("type: " + coupon.getType() + " | ");
        value.setText("value: " + coupon.getValue() + " | ");
        minPrice.setText("min price: " + coupon.getMinPrice());
    }

    private String formatValidity(LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(end)) {
            // Coupon has expired
            validDateAndTime.setStyle("-fx-font-size: 12; -fx-text-fill: #bb0000");

            Duration durationSinceExpired = Duration.between(end, now);
            return "Expired " + formatDuration(durationSinceExpired) + " ago";
        } else if (now.isBefore(start)) {
            // Coupon has not started yet
            validDateAndTime.setStyle("-fx-font-size: 12; -fx-text-fill: #666666");

            Duration durationUntilStart = Duration.between(now, start);
            return "Starts in " + formatDuration(durationUntilStart);
        } else {
            // Coupon is currently active
            validDateAndTime.setStyle("-fx-font-size: 12; -fx-text-fill: darkgreen; -fx-font-weight: bold");
            Duration durationUntilEnd = Duration.between(now, end);
            return "Ends in " + formatDuration(durationUntilEnd);
        }
    }

    private String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(days == 1 ? " day" : " days");
        }
        if (hours > 0) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(hours).append(hours == 1 ? " hour" : " hours");
        }
        if (minutes > 0) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(minutes).append(minutes == 1 ? " minute" : " minutes");
        }

        // Handle cases where duration is less than a minute
        if (sb.isEmpty()) {
            return "less than a minute";
        }

        return sb.toString();
    }

    public void copyButtonClicked() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(coupon.getCode());
        clipboard.setContent(content);
    }

    public void editButtonClicked() {
        try {
            // Load the FXML for the dialog
            FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                    "/frontend/bemirfoodclient/adminPanel/card/edit-coupon-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            EditCouponDialogController dialogController = loader.getController();
            dialogController.setData(coupon);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Coupon");
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                    "/frontend/bemirfoodclient/assets/Bemirfood_Logo.png"))));

            // Add the "Save" and "Cancel" buttons to the dialog pane
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Coupon updatedCoupon = dialogController.processResult();
                if (updatedCoupon != null && onEdit != null) {
                    onEdit.accept(updatedCoupon); // Pass the updated coupon back
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Optionally show an error alert to the user
        }
    }

    @FXML
    public void deleteButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Coupon");
        alert.setHeaderText("Are you sure you want to delete this coupon?");
        alert.setContentText("Coupon Code: " + coupon.getCode());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (onDelete != null) {
                onDelete.accept(coupon);
            }
        }
    }

    public void setOnDelete(Consumer<Coupon> onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnEdit(Consumer<Coupon> onEdit) {
        this.onEdit = onEdit;
    }
}
