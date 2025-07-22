package frontend.bemirfoodclient.controller.restaurant.seller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddMenuDialogController {
    @FXML
    public TextField menuTitleTextField;

    public void initialize() {

    }

    public String getNewMenuName() {
        return menuTitleTextField.getText();
    }
}
