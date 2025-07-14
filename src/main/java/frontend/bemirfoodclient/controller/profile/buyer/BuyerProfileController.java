package frontend.bemirfoodclient.controller.profile.buyer;

import frontend.bemirfoodclient.BemirfoodApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

    }
}
