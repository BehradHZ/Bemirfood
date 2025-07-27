package frontend.bemirfoodclient.controller;

import frontend.bemirfoodclient.BemirfoodApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class PaymentGatewayDialogController {
    @FXML private VBox amountVBox;
    @FXML private TextField amountField;
    @FXML private TextField otp;

    public void setMode(boolean isWalletTopUp) {
        amountVBox.setVisible(isWalletTopUp);
        amountVBox.setManaged(isWalletTopUp); // This removes it from the layout when hidden
    }

    public String getOtp() {
        return otp.getText();
    }

    public double getAmount() {
        String amountText = amountField.getText().trim();
        if (amountText.isEmpty()) {
            showAlert("Amount cannot be empty.");
            return -1; // Signal failure
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showAlert("Amount must be a positive number.");
                return -1; // Signal failure
            }
            return amount;
        } catch (NumberFormatException e) {
            showAlert("Amount must be a valid number.");
            return -1; // Signal failure
        }
    }

    public void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                "assets/icons/error.png"
        ))));
        alert.setTitle("Wallet Top-up Failed");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }
}