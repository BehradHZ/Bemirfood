package frontend.bemirfoodclient.controller.profile.buyer.details;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import HttpClientHandler.Requests;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.time.LocalDateTime;
import java.util.*;

import static BuildEntity.builder.buildTransactionList;
import static exception.exp.expHandler;

public class BuyerTransactionsController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
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

    public HttpResponseData walletTopUp(double amount) {
        Map<String, Long> request = new HashMap<>();
        request.put("amount", (long) amount);
        return Requests.walletTopUp(gson.toJson(request));
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
            HttpResponseData response = walletTopUp(amount);
            if(response.getStatusCode() == 200){
                initialize();
            }else{
                expHandler(response, "Failed to top-up wallet", null);
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
