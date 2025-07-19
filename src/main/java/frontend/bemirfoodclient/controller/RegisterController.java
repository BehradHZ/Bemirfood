package frontend.bemirfoodclient.controller;

import com.google.gson.Gson;
import frontend.bemirfoodclient.model.dto.UserDto;
import frontend.bemirfoodclient.model.entity.User;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.UnaryOperator;


public class RegisterController {
    @FXML
    public BorderPane mainBorderPane;
    @FXML
    public ImageView firstPageImage;
    @FXML
    public TextField fullNameTextField;
    @FXML
    public TextField phoneNumberTextField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField visiblePasswordField;
    @FXML
    public Button makePasswordVisible;
    @FXML
    public Button makePasswordHidden;
    @FXML
    public ToggleGroup roleToggleGroup;
    @FXML
    public RadioButton buyerToggleOption;
    @FXML
    public RadioButton sellerToggleOption;
    @FXML
    public RadioButton courierToggleOption;
    @FXML
    public TextField addressTextField;
    @FXML
    public Button nextButton;

    public String role;

    private BooleanProperty isNextClicking = new SimpleBooleanProperty(false);

    private User user;

    @FXML
    public void initialize() {
        firstPageImage.fitHeightProperty().bind(mainBorderPane.heightProperty());

        UnaryOperator<TextFormatter.Change> phoneNumberFilter = change -> {
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
        TextFormatter<String> phoneNumberTextFormatter = new TextFormatter<>(phoneNumberFilter);
        phoneNumberTextField.setTextFormatter(phoneNumberTextFormatter);

        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());
        visiblePasswordField.visibleProperty().bind(makePasswordHidden.visibleProperty());
        passwordField.visibleProperty().bind(makePasswordVisible.visibleProperty());

        BooleanBinding allFieldsFilled =
                fullNameTextField.textProperty().isEmpty().or(
                        phoneNumberTextField.textProperty().isEmpty().or(
                                passwordField.textProperty().isEmpty().or(
                                        addressTextField.textProperty().isEmpty()
                                )

                )
        );

        nextButton.disableProperty().bind(allFieldsFilled.or(isNextClicking));

        String homeDirectory = System.getProperty("user.home");
        Path filePath = Path.of(homeDirectory, "registerTemp.txt");

        if (!Files.exists(filePath)) {
            return;
        }

        try {
            if (Files.readString(filePath).startsWith("temporary register data 2")) {
                String fileContent = Files.readString(filePath);
                int jsonStartIndex = fileContent.indexOf('{');
                String jsonText = fileContent.substring(jsonStartIndex);

                Gson gson = new Gson();
                UserDto userDto = gson.fromJson(jsonText, UserDto.class);
                user = User.UserDtoToUser(userDto);

                fullNameTextField.setText(user.getFullName());
                phoneNumberTextField.setText(user.getMobile());
                passwordField.setText(user.getPassword());
                addressTextField.setText(user.getAddress());
                switch (user.getRoleAsString()) {
                    case "buyer":
                        buyerToggleOption.setSelected(true);
                        break;
                    case "seller":
                        sellerToggleOption.setSelected(true);
                        break;
                    case "courier":
                        courierToggleOption.setSelected(true);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    @FXML
    public void handelNextButtonClicked() {
        isNextClicking.set(true);

        RadioButton selectedToggle = (RadioButton) roleToggleGroup.getSelectedToggle();
        role = ((RadioButton) selectedToggle).getText().toLowerCase();

        try {
            String homeDirectory = System.getProperty("user.home");
            Path filePath = Path.of(homeDirectory, "registerTemp.txt");
            String jsonText;

            if (Files.readString(filePath).startsWith("temporary register data 2")) {
                jsonText = String.format("""
                {
                  "full_name": "%s",
                  "phone": "%s",
                  "email": "%s",
                  "password": "%s",
                  "role": "%s",
                  "address": "%s",
                  "profileImageBase64": "%s",
                  "bank_info": {
                    "bank_name": "%s",
                    "account_number": "%s"
                  }
                }
                """,

                        fullNameTextField.getText(), phoneNumberTextField.getText(), user.getEmail(), passwordField.getText(),
                        role, addressTextField.getText(), user.getPhoto(), user.getBank_info().getBank_name(),
                        user.getBank_info().getAccount_number()
                );
            } else {
                jsonText = String.format("""
                {
                  "full_name": "%s",
                  "phone": "%s",
                  "email": "%s",
                  "password": "%s",
                  "role": "%s",
                  "address": "%s",
                  "profileImageBase64": "%s",
                  "bank_info": {
                    "bank_name": "%s",
                    "account_number": "%s"
                  }
                }
                """,

                        fullNameTextField.getText(), phoneNumberTextField.getText(), "", passwordField.getText(),
                        role, addressTextField.getText(), "", "", ""
                );
            }

            String fileContent = "temporary register data 1:\n" + jsonText;

            Files.writeString(filePath, fileContent);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Stage stage = (Stage) phoneNumberTextField.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/register-additional-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        isNextClicking.set(false);
    }

    @FXML
    public void handelLoginHyperlinkClicked() {
        try {
            Stage stage = (Stage) phoneNumberTextField.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/login-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void makePasswordVisible() {
        makePasswordVisible.setVisible(false);
        makePasswordHidden.setVisible(true);
    }

    public void makePasswordHidden() {
        makePasswordHidden.setVisible(false);
        makePasswordVisible.setVisible(true);
    }
}
