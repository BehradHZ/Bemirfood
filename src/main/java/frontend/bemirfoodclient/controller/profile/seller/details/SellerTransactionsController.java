package frontend.bemirfoodclient.controller.profile.seller.details;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.TransactionCardController;
import frontend.bemirfoodclient.model.entity.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static HttpClientHandler.Requests.getSellerTransaction;

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
        HttpResponseData response = getSellerTransaction();
        JsonArray jsonArray = response.getBody().getAsJsonArray("List of transactions");
        transactions = gson.fromJson(jsonArray, new TypeToken<List<Transaction>>(){}.getType());

        for (Transaction t : transactions) {
            System.out.println("Transaction ID: " + t.getId() + ", Status: " + t.getStatus());
        }

        return transactions;
    }

    public String getBalance() {
        //do the stuff in backend
        double balance = 20000; //temporary
        return balance + " toomans"; //don't change the structure
    }
}
