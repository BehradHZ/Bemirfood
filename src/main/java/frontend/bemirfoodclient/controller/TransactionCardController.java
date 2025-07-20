package frontend.bemirfoodclient.controller;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.Objects;

public class TransactionCardController {

    @FXML
    private Label time;
    @FXML
    public Label date;
    @FXML
    public Region firstLineSpacer;
    @FXML
    public Label title;
    @FXML
    public Label amount;
    @FXML
    public Region secondLineSpacer;
    @FXML
    public HBox statusHBox;
    @FXML
    public Label status;
    @FXML
    public ImageView statusIcon;
    @FXML
    public Label paymentMethod;

    @FXML
    private Image successIcon = new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
            "/frontend/bemirfoodclient/assets/icons/success.png")));
    @FXML
    private Image failureIcon = new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
            "/frontend/bemirfoodclient/assets/icons/failure.png")));

    private String transactionType;

    private Transaction transaction;

    public void initialize() {
        HBox.setHgrow(firstLineSpacer, Priority.ALWAYS);
        HBox.setHgrow(secondLineSpacer, Priority.ALWAYS);
        statusIcon.setImage(successIcon);
        statusIcon.setFitHeight(15);

    }

    public void setData(Transaction transaction) {
        this.transaction = transaction;
        time.setText(getTime(transaction));
        date.setText(getDate(transaction));
        switch (getTransactionType(transaction)) {
            case "payment":
                title.setText("Order from " + getTitle(transaction));
                break;
            case "wallet top up":
                title.setText("Wallet Top up");
                break;
            default:
                break;
        }
        amount.setText(getAmount(transaction));
        status.setText(" " + getStatus(transaction) + " ");
        paymentMethod.setText(getPaymentMethod(transaction));

        if (getStatus(transaction).equals("successful")) {
            statusIcon.setImage(successIcon);
            statusHBox.setPrefWidth(100);
            statusHBox.setStyle("-fx-background-color: rgba(68,130,74,0.2); -fx-background-radius: 50");
        } else {
            statusIcon.setImage(failureIcon);
            statusHBox.setPrefWidth(80);
            statusHBox.setStyle("-fx-background-color: rgba(130,68,68,0.2); -fx-background-radius: 50");
        }
    }

    public String getTime(Transaction transaction) {
        return transaction.getTimestamp().getHour() + ":" + transaction.getTimestamp().getMinute();
    }
    public String getDate(Transaction transaction) {
        return transaction.getTimestamp().getMonth().name() + " " + transaction.getTimestamp().getDayOfMonth() + ", " +
                transaction.getTimestamp().getYear();
    }
    public String getTitle(Transaction transaction) {
        return transaction.getTitle();
    }
    public String getTransactionType(Transaction transaction) {
        return transaction.getTransactionType();
    }
    public String getAmount(Transaction transaction) {
        return transaction.getAmount() + "";
    }
    public String getStatus(Transaction transaction) {
        return transaction.getStatus();
    }
    public String getPaymentMethod(Transaction transaction) {
        return transaction.getPaymentMethod();
    }
}
