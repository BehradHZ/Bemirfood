package frontend.bemirfoodclient.controller.profile.seller.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.TransactionCardController;
import frontend.bemirfoodclient.model.entity.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SellerTransactionsController {

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

        //create suitable Transaction() constructors
        //if transaction is a payment, it needs a title
        //amount, payment method, status, time are also required

//        transactions.add(new Transaction("starbucks", 65, "online", "payment", LocalDateTime.now(), "successfull"));

        return transactions;
    }

    public String getBalance() {
        //do the stuff in backend
        double balance = 20000; //temporary
        return balance + " toomans"; //don't change the structure
    }
}
