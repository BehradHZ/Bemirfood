package frontend.bemirfoodclient.controller.profile.courier.details;

import HttpClientHandler.HttpResponseData;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import static exception.exp.expHandler;
import static HttpClientHandler.Requests.updateUserProfile;


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

        Map<String, Object> request = new LinkedHashMap<>();
        if(fullName != null && !fullName.isBlank()) request.put("full_name", fullName);
        if(phoneNumber != null && !phoneNumber.isBlank()) request.put("phone_number", phoneNumber);
        if(email != null && !email.isBlank()) request.put("email", email);
        if(address != null && !address.isBlank()) request.put("address", address);
        if(bank_info != null) request.put("bank_info", bank_info);

        HttpResponseData responseData = updateUserProfile(gson.toJson(request));
        if(responseData.getStatusCode() == 200){

        }else{
            expHandler(responseData, "Failed to update profile", null);
        }


        return new UserDto(
                new User(fullName, phoneNumber, "courier", email, null, address, bank_info));
    }
}
