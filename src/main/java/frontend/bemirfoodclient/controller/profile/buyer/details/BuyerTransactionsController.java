package frontend.bemirfoodclient.controller.profile.buyer.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.TransactionCardController;
import frontend.bemirfoodclient.model.entity.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuyerTransactionsController {

    @FXML
    public VBox transactionsCardsSection;
    @FXML
    public Region balanceSpacer;

    public void initialize() {
        HBox.setHgrow(balanceSpacer, Priority.ALWAYS);
        List<Transaction> transactions = getTransactions();
        for (Transaction transaction : transactions) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource("transaction-card.fxml"));
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
            transactions.add(new Transaction("کباب فروشی اصغر آقا", 65.0));
            transactions.add(new Transaction("کباب فروشی اصغر آقا", 655.0));
            transactions.add(new Transaction("کباب فروشی اصغر آقا", 75.0));

        return transactions;
    }
}
