package frontend.bemirfoodclient.controller.profile.courier.details;

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
    public TextField editPopupBank_nameTextField;
    @FXML
    public TextField editPopupAccount_numberTextField;

    public void initialize() {
        editPopupFullNameTextField.setPromptText(CourierProfileDetailsController.getFullName());
        editPopupPhoneNumberTextField.setPromptText(CourierProfileDetailsController.getPhoneNumber());
        editPopupEmailTextField.setPromptText(CourierProfileDetailsController.getEmail());
        editPopupAddressTextField.setPromptText(CourierProfileDetailsController.getAddress());
        editPopupBank_nameTextField.setPromptText(CourierProfileDetailsController.getBank_name());
        editPopupAccount_numberTextField.setPromptText(CourierProfileDetailsController.getAccount_number());

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
        String bank_name = editPopupBank_nameTextField.getText();
        String account_number = editPopupAccount_numberTextField.getText();
        Bank_info bank_info = new Bank_info(bank_name, account_number);


        return new UserDto(
                new User(fullName, phoneNumber, "courier", email, null, address, bank_info));
    }
}
