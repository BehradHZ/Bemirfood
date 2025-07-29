package frontend.bemirfoodclient.controller.profile.buyer;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import Util.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.border.BuyerBorderController;
import frontend.bemirfoodclient.controller.profile.buyer.details.EditProfileDialogController;
import frontend.bemirfoodclient.model.dto.UserDto;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static HttpClientHandler.Requests.updateUserProfile;
import static exception.exp.expHandler;

public class BuyerProfileController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();
    private static BorderPane mainNavigationPane;

    @FXML
    public BorderPane mainBorderPane;

    @FXML
    public Button profileSmall;
    @FXML
    public Button profileLarge;

    @FXML
    public Button myOrdersSmall;
    @FXML
    public Button myOrdersLarge;

    @FXML
    public Button discountCodesSmall;
    @FXML
    public Button discountCodesLarge;

    @FXML
    public Button myTransactionsSmall;
    @FXML
    public Button myTransactionsLarge;

    @FXML
    public Button logoutSmall;
    @FXML
    public Button logoutLarge;

    public String profileView;

    @FXML
    public Button backButton;
    @FXML
    public ImageView backButtonImage;
    @FXML
    public Button editButton;
    @FXML
    public ImageView editButtonImage;
    public Region profileSpacer;

    @FXML
    public void initialize() {
        mainNavigationPane = BuyerBorderController.staticMainBorderPane;

        profileButtonClicked();

        backButtonImage.setPreserveRatio(true);
        backButtonImage.setFitHeight(20);

        HBox.setHgrow(profileSpacer, Priority.ALWAYS);

        editButtonImage.setPreserveRatio(true);
        editButtonImage.setFitHeight(20);
    }

    @FXML
    public void profileButtonClicked(){
        profileView = "profile";
        profileLarge.setVisible(true);
        myOrdersLarge.setVisible(false);
        discountCodesLarge.setVisible(false);
        myTransactionsLarge.setVisible(false);
        logoutLarge.setVisible(false);
        editButton.setVisible(true);

        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/profile/buyer/details/profile-details.fxml")));
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void myOrdersButtonClicked(){
        profileView = "myOrders";
        profileLarge.setVisible(false);
        myOrdersLarge.setVisible(true);
        discountCodesLarge.setVisible(false);
        myTransactionsLarge.setVisible(false);
        logoutLarge.setVisible(false);
        editButton.setVisible(false);

        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/profile/buyer/details/my-orders-details.fxml")));
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void discountCodesButtonClicked(){
        profileView = "discountCodes";
        profileLarge.setVisible(false);
        myOrdersLarge.setVisible(false);
        discountCodesLarge.setVisible(true);
        myTransactionsLarge.setVisible(false);
        logoutLarge.setVisible(false);
        editButton.setVisible(false);

        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/profile/buyer/details/discount-codes-details.fxml")));
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void myTransactionsButtonClicked() {
        profileView = "myTransactions";
        profileLarge.setVisible(false);
        myOrdersLarge.setVisible(false);
        discountCodesLarge.setVisible(false);
        myTransactionsLarge.setVisible(true);
        logoutLarge.setVisible(false);
        editButton.setVisible(false);

        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/profile/buyer/details/my-transactions-details.fxml")));
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logoutButtonClicked() {
        profileLarge.setVisible(false);
        myOrdersLarge.setVisible(false);
        discountCodesLarge.setVisible(false);
        myTransactionsLarge.setVisible(false);
        logoutLarge.setVisible(true);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));

        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        alert.setGraphic(null);
        Platform.runLater(() -> alert.getDialogPane().lookupButton(ButtonType.CANCEL).requestFocus());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            Token.clearFileContent();

            String homeDirectory = System.getProperty("user.dir");
            Path filePath = Path.of(homeDirectory, "registerTemp.txt");

            try {
                if (Files.exists(filePath)) {
                    Files.writeString(filePath, "");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Stage mainWindow = (Stage) profileSmall.getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/frontend/bemirfoodclient/login-view.fxml")));
                mainWindow.getScene().setRoot(root);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            switch (profileView){
                case "profile":
                    profileButtonClicked();
                    break;
                case "myOrders":
                    myOrdersButtonClicked();
                    break;
                case "discountCodes":
                    discountCodesButtonClicked();
                    break;
                case "myTransactions":
                    myTransactionsButtonClicked();
                    break;
                default:
                    break;
            }
        }
    }

    @FXML
    public void backButtonClicked() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/border/buyer-border-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpResponseData editUserProfile(UserDto userDto) {
        Map<String, Object> request = new LinkedHashMap<>();
        if(userDto.getFullName() != null && !userDto.getFullName().isBlank()) request.put("full_name", userDto.getFullName());
        if(userDto.getPhone() != null && !userDto.getPhone().isBlank()) request.put("phone", userDto.getPhone());
        if(userDto.getEmail() != null && !userDto.getEmail().isBlank()) request.put("email", userDto.getEmail());
        if(userDto.getAddress() != null && !userDto.getAddress().isBlank()) request.put("address", userDto.getAddress());
        if(userDto.getBank_info() != null) request.put("bank_info", userDto.getBank_info());
        if(userDto.getProfileImageBase64() != null) request.put("profileImageBase64", userDto.getProfileImageBase64());

        return updateUserProfile(gson.toJson(request));
    }

    @FXML
    public void editButtonClicked() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(BemirfoodApplication.class.getResource(
                "/frontend/bemirfoodclient/profile/buyer/details/edit-profile-dialog-view.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            EditProfileDialogController controller = fxmlLoader.getController();
            UserDto userDto = controller.updateCurrentUserProfile();
            HttpResponseData response = editUserProfile(userDto);
            if(response.getStatusCode() == 200){
                profileButtonClicked();
            }else{
                expHandler(response, "Failed to update profile", null);
            }
        });

        dialog.showAndWait();
    }

    public void showAlert(String content, ActionEvent event) {
        event.consume();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
        alert.setTitle("Login failed");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }

    public static BorderPane getMainNavigationPane() {
        return mainNavigationPane;
    }
}
