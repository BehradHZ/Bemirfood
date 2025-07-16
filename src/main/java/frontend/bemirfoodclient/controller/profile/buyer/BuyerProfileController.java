package frontend.bemirfoodclient.controller.profile.buyer;

import frontend.bemirfoodclient.BemirfoodApplication;
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
import java.util.Objects;
import java.util.Optional;

public class BuyerProfileController {
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
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/frontend/bemirfoodclient/profile/buyer/details/my-orders-details.fxml")));
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
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/frontend/bemirfoodclient/profile/buyer/details/discount-codes-details.fxml")));
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
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/frontend/bemirfoodclient/profile/buyer/details/my-transactions-details.fxml")));
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

            //do the stuff in backend

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
                    "/frontend/bemirfoodclient/homepage/buyer-homepage-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            System.out.println("Couldn't show new item dialog");
            ioe.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            EditProfileDialogController controller = fxmlLoader.getController();
            UserDto userDto = controller.updateCurrentUserProfile();
            System.out.println(1);
            int code = 200;
            switch (/*do the stuff in backend*/ code /*temporary*/) {
                case 200:

                    break;
                case 400:
                    event.consume();
                    Alert alert400 = new Alert(Alert.AlertType.ERROR);
                    Stage stage400 = (Stage) alert400.getDialogPane().getScene().getWindow();
                    stage400.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                    alert400.setTitle("Login failed");
                    alert400.setHeaderText(null);
                    alert400.setContentText("Invalid phone number or password. (400 Invalid input)");
                    alert400.getDialogPane().setGraphic(null);
                    alert400.showAndWait();
                    break;
                case 401:
                    event.consume();
                    Alert alert401 = new Alert(Alert.AlertType.ERROR);
                    Stage stage401 = (Stage) alert401.getDialogPane().getScene().getWindow();
                    stage401.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                    alert401.setTitle("Login failed");
                    alert401.setHeaderText(null);
                    alert401.setContentText("This phone number is not registered. (401 Unauthorized)");
                    alert401.getDialogPane().setGraphic(null);
                    alert401.showAndWait();
                    break;
                case 403:
                    event.consume();
                    Alert alert403 = new Alert(Alert.AlertType.ERROR);
                    Stage stage403 = (Stage) alert403.getDialogPane().getScene().getWindow();
                    stage403.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                    alert403.setTitle("Login failed");
                    alert403.setHeaderText(null);
                    alert403.setContentText("You cannot access to this service. (403 Forbidden)");
                    alert403.getDialogPane().setGraphic(null);
                    alert403.showAndWait();
                    break;
                case 404:
                    event.consume();
                    Alert alert404 = new Alert(Alert.AlertType.ERROR);
                    Stage stage404 = (Stage) alert404.getDialogPane().getScene().getWindow();
                    stage404.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                    alert404.setTitle("Login failed");
                    alert404.setHeaderText(null);
                    alert404.setContentText("Service not found. (404 Not Found)");
                    alert404.getDialogPane().setGraphic(null);
                    alert404.showAndWait();
                    break;
                case 409:
                    event.consume();
                    Alert alert409 = new Alert(Alert.AlertType.ERROR);
                    Stage stage409 = (Stage) alert409.getDialogPane().getScene().getWindow();
                    stage409.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                    alert409.setTitle("Login failed");
                    alert409.setHeaderText(null);
                    alert409.setContentText("There was a conflict for access to this service. (409 Conflict)");
                    alert409.getDialogPane().setGraphic(null);
                    alert409.showAndWait();
                    break;
                case 415:
                    event.consume();
                    Alert alert415 = new Alert(Alert.AlertType.ERROR);
                    Stage stage415 = (Stage) alert415.getDialogPane().getScene().getWindow();
                    stage415.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                    alert415.setTitle("Login failed");
                    alert415.setHeaderText(null);
                    alert415.setContentText("This media type cannot be accepted. (415 Unsupported Media Type)");
                    alert415.getDialogPane().setGraphic(null);
                    alert415.showAndWait();
                    break;
                case 429:
                    event.consume();
                    Alert alert429 = new Alert(Alert.AlertType.ERROR);
                    Stage stage429 = (Stage) alert429.getDialogPane().getScene().getWindow();
                    stage429.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                    alert429.setTitle("Login failed");
                    alert429.setHeaderText(null);
                    alert429.setContentText("Please try again later. (429 Too Many Requests)");
                    alert429.getDialogPane().setGraphic(null);
                    alert429.showAndWait();
                    break;
                case 500:
                    event.consume();
                    Alert alert500 = new Alert(Alert.AlertType.ERROR);
                    Stage stage500 = (Stage) alert500.getDialogPane().getScene().getWindow();
                    stage500.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
                    alert500.setTitle("Login failed");
                    alert500.setHeaderText(null);
                    alert500.setContentText("This is from our side; pleas try again later :) (500 Internal Server Error)");
                    alert500.getDialogPane().setGraphic(null);
                    alert500.showAndWait();
                    break;
                default:
                    break;
            }
        });

        dialog.showAndWait();
    }
}
