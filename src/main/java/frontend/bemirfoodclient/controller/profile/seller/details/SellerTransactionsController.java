package frontend.bemirfoodclient.controller.profile.seller.details;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import HttpClientHandler.Requests;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.TransactionCardController;
import frontend.bemirfoodclient.model.entity.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static BuildEntity.builder.buildTransactionList;
import static exception.exp.expHandler;

public class SellerTransactionsController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();


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
        Collections.reverse(transactions);
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
        return buildTransactionList(args -> Requests.getTransactions(), "List of transactions", "Failed to get transactions");
    }

    public String getBalance() {
        HttpResponseData response = Requests.getBalance();
        if(response.getStatusCode() != 200) expHandler(response, "Failed to get balance", null);
        return "$" + response.getBody().get("Balance").getAsString();
    }
}
