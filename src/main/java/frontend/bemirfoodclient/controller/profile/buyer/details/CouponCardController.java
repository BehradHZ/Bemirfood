package frontend.bemirfoodclient.controller.profile.buyer.details;

import frontend.bemirfoodclient.model.entity.Coupon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.time.Duration;
import java.time.LocalDateTime;

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

    private Coupon coupon;

    public void setCouponData(Coupon coupon) {
        this.coupon = coupon;
        setScene();
    }

    public void initialize() {
        copyButton.setPreserveRatio(true);
        copyButton.setFitWidth(20);

        HBox.setHgrow(spacer, Priority.ALWAYS);
    }

    private void setScene() {
        couponCode.setText(coupon.getCode());

        usersLeft.setText(coupon.getUserCount() + " users left");

        validDateAndTime.setText(formatValidity(coupon.getStartDate(), coupon.getEndDate()));
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
}
