package frontend.bemirfoodclient.controller.profile.buyer.details;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TopUpDialogController {
    @FXML
    public TextField amountTextField;

    public void initialize() {

    }

    public double getTopUpAmount() {
        return Integer.parseInt(amountTextField.getText());
    }
}
