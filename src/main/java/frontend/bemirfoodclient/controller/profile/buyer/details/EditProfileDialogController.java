package frontend.bemirfoodclient.controller.profile.buyer.details;

import frontend.bemirfoodclient.model.dto.UserDto;
import frontend.bemirfoodclient.model.entity.Bank_info;
import frontend.bemirfoodclient.model.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;


public class EditProfileDialogController {
    @FXML
    public TextField editPopupFullNameTextField;
    @FXML
    public TextField editPopupPhoneNumberTextField;
    @FXML
    public TextField editPopupEmailTextField;
    @FXML
    public TextField editPopupAddressTextField;
    @FXML
    public TextField editPopupBankN_nameTextField;
    @FXML
    public TextField editPopupAccount_numberTextField;

    public void initialize() {
        editPopupFullNameTextField.setPromptText(BuyerProfileDetailsController.getFullName());
        editPopupPhoneNumberTextField.setPromptText(BuyerProfileDetailsController.getPhoneNumber());
        editPopupEmailTextField.setPromptText(BuyerProfileDetailsController.getEmail());
        editPopupAddressTextField.setPromptText(BuyerProfileDetailsController.getAddress());
        editPopupBankN_nameTextField.setPromptText(BuyerProfileDetailsController.getBank_name());
        editPopupAccount_numberTextField.setPromptText(BuyerProfileDetailsController.getAccount_number());

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            try {
                if (newText.matches("\\d*") && newText.length() <= 11 && (newText.charAt(0) == '0' && newText.charAt(1) == '9')) {
                    return change;
                } else {
                    return null;
                }
            } catch (StringIndexOutOfBoundsException siobe) {
                return change;
            }
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        editPopupPhoneNumberTextField.setTextFormatter(textFormatter);
    }

    public UserDto updateCurrentUserProfile() {
        String fullName = editPopupFullNameTextField.getText();
        String phoneNumber = editPopupPhoneNumberTextField.getText();
        String email = editPopupEmailTextField.getText();
        String address = editPopupAddressTextField.getText();
        String bank_name = editPopupBankN_nameTextField.getText();
        String account_number = editPopupAccount_numberTextField.getText();
        Bank_info bank_info = new Bank_info(bank_name, account_number);

        return new UserDto(
                new User(fullName, phoneNumber, "buyer", email, null, address, bank_info));
    }
}
