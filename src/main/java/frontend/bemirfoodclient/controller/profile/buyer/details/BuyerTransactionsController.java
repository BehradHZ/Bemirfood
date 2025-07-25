package frontend.bemirfoodclient.controller.profile.buyer.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.TransactionCardController;
import frontend.bemirfoodclient.model.entity.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuyerTransactionsController {

    @FXML
    public BorderPane mainBorderPane;
    @FXML
    public VBox transactionsCardsSection;
    @FXML
    public Label balance;
    @FXML
    public Region balanceSpacer;

    public void initialize() {
        balance.setText(getBalance());
        HBox.setHgrow(balanceSpacer, Priority.ALWAYS);

        List<Transaction> transactions = getTransactions();
        for (Transaction transaction : transactions) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/profile/transaction-card.fxml"));
                Pane card = loader.load();
                TransactionCardController controller = loader.getController();
                controller.setData(transaction);
                transactionsCardsSection.getChildren().add(card);
            } catch (IOException e) {
                //
            }
        }
    }

    private List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        //do the stuff in backend
        //Get user's transaction history

        return transactions;
    }

    public String getBalance() {
        //do the stuff in backend
        double balance = 20000; //temporary
        return "$" + balance; //don't change the structure
    }

    public int walletTopUp(double amount) {
        //do the stuff in backend
        //YAML: Top up user's wallet

        return 200;
    }

    @FXML
    public void topUpButtonClicked() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Wallet Top-Up");
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(BemirfoodApplication.class.getResource(
                "/frontend/bemirfoodclient/profile/buyer/details/top-up-dialog-view.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            TopUpDialogController controller = fxmlLoader.getController();
            double amount = controller.getTopUpAmount();

            switch (walletTopUp(amount)) {
                case 200:
                    initialize();
                    break;
                case 400:
                    showAlert("Invalid `field name`. (400 Invalid input)", event);
                    break;
                case 401:
                    showAlert("Unauthorized request. (401 Unauthorized)", event);
                    break;
                case 403:
                    showAlert("Forbidden request. (403 Forbidden)", event);
                    break;
                case 404:
                    showAlert("Resource not found. (404 Not Found)", event);
                    break;
                case 409:
                    showAlert("Conflict occurred. (409 Conflict)", event);
                    break;
                case 415:
                    showAlert("Unsupported media type. (415 Unsupported Media Type)", event);
                    break;
                case 429:
                    showAlert("Too many requests. (429 Too Many Requests)", event);
                    break;
                case 500:
                    showAlert("Internal server error. (500 Internal Server Error)", event);
                default:
                    break;
            }
        });

        dialog.showAndWait();

    }

    public void showAlert(String content, ActionEvent event) {
        event.consume();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                "assets/icons/error.png"))));
        alert.setTitle("Login failed");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }
}
