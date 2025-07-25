package frontend.bemirfoodclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PaymentGatewayDialogController {
    @FXML private VBox amountVBox;
    @FXML private TextField amountField;
    @FXML private TextField accountNumberField;

    /**
     * Configures the dialog for a specific mode.
     * @param isWalletTopUp true if it's for topping up the wallet, false for cart payment.
     */
    public void setMode(boolean isWalletTopUp) {
        amountVBox.setVisible(isWalletTopUp);
        amountVBox.setManaged(isWalletTopUp); // This removes it from the layout when hidden
    }

    public String getAccountNumber() {
        return accountNumberField.getText();
    }

    public String getAmount() {
        return amountField.getText();
    }
}