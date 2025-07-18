package frontend.bemirfoodclient.controller;

import com.google.gson.Gson;
import frontend.bemirfoodclient.model.dto.UserDto;
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
            System.out.println("File does not exist yet.");
            return;
        }

        try {
            String content = Files.readString(filePath);

            if (content.startsWith("temporary register data")) {
                try {
                    String fileContent = Files.readString(filePath);
                    int jsonStartIndex = fileContent.indexOf('{');
                    String jsonText = fileContent.substring(jsonStartIndex);

                    Gson gson = new Gson();
                    UserDto userDto = gson.fromJson(jsonText, UserDto.class);

                    fullNameTextField.setText(userDto.getFullName());
                    System.out.println(userDto.getPhone());
                    phoneNumberTextField.setText(userDto.getPhone());
                    passwordField.setText(userDto.getPassword());
                    addressTextField.setText(userDto.getAddress());
                    switch (userDto.getRole()) {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File does not start with the expected text.");
                // Logic for when the file has other data
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
    }

    public void loadPreviousData() {

    }

    @FXML
    public void handelNextButtonClicked() {
        isNextClicking.set(true);

        RadioButton selectedToggle = (RadioButton) roleToggleGroup.getSelectedToggle();
        role = ((RadioButton) selectedToggle).getText().toLowerCase();

        String jsonTemplate = """
                {
                    "full_name": "%s",
                    "phone": "%s",
                    "password": "%s",
                    "role": "%s",
                    "address": "%s"
                }""";
        String jsonText = String.format(jsonTemplate, fullNameTextField.getText(), phoneNumberTextField.getText(),
                passwordField.getText(), role, addressTextField.getText());

        String fileContent = "temporary register data:\n" + jsonText;

        try {
            String homeDirectory = System.getProperty("user.home");
            Path filePath = Path.of(homeDirectory, "registerTemp.txt");
            Files.writeString(filePath, fileContent);
            System.out.println("Successfully wrote data to " + filePath);

        } catch (IOException e) { // Catch the specific, correct exception
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
