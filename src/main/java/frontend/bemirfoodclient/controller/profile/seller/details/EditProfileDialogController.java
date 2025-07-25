package frontend.bemirfoodclient.controller.profile.seller.details;

import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.bemirfoodclient.model.dto.UserDto;
import frontend.bemirfoodclient.model.entity.Bank_info;
import frontend.bemirfoodclient.model.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.time.LocalDateTime;
import java.util.function.UnaryOperator;

public class EditProfileDialogController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();

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

    private User currentUser;

    public void setUserData(User user) {
        this.currentUser = user;
        setScene();
    }

    public void initialize() {

    }

    public void setScene() {
        editPopupFullNameTextField.setText(currentUser.getFull_name());
        editPopupPhoneNumberTextField.setText(currentUser.getMobile());
        editPopupEmailTextField.setText(currentUser.getEmail());
        editPopupAddressTextField.setText(currentUser.getAddress());
        if (currentUser.getBank_info() != null) {
            editPopupBank_nameTextField.setText(currentUser.getBank_info().getBank_name());
            editPopupAccount_numberTextField.setText(currentUser.getBank_info().getAccount_number());
        }

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
                new User(fullName, phoneNumber, "seller", email, null, address, bank_info));
    }
}
