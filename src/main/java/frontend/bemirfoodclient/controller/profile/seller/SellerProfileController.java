package frontend.bemirfoodclient.controller.profile.seller;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.profile.seller.details.AddRestaurantDialogController;
import frontend.bemirfoodclient.controller.profile.seller.details.EditProfileDialogController;
import frontend.bemirfoodclient.model.dto.UserDto;
import frontend.bemirfoodclient.model.entity.Restaurant;
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
import java.util.concurrent.atomic.AtomicReference;

public class SellerProfileController {
    @FXML
    public BorderPane mainBorderPane;

    @FXML
    public Button profileSmall;
    @FXML
    public Button profileLarge;

    @FXML
    public Button myRestaurantsSmall;
    @FXML
    public Button myRestaurantsLarge;

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
    @FXML
    public Button addRestaurantButton;
    @FXML
    public ImageView addRestaurantImage;
    @FXML
    public Region profileSpacer;

    @FXML
    public void initialize() {
        profileButtonClicked();

        backButtonImage.setPreserveRatio(true);
        backButtonImage.setFitHeight(20);

        HBox.setHgrow(profileSpacer, Priority.ALWAYS);

        editButtonImage.setPreserveRatio(true);
        editButtonImage.setFitHeight(20);

        addRestaurantImage.setPreserveRatio(true);
        addRestaurantImage.setFitHeight(25);
    }

    @FXML
    public void profileButtonClicked(){
        profileView = "profile";
        profileLarge.setVisible(true);
        myRestaurantsLarge.setVisible(false);
        myTransactionsLarge.setVisible(false);
        logoutLarge.setVisible(false);
        editButton.setVisible(true);
        addRestaurantButton.setVisible(false);

        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/profile/seller/details/profile-details.fxml")));
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void myRestaurantsButtonClicked(){
        profileView = "myRestaurants";
        profileLarge.setVisible(false);
        myRestaurantsLarge.setVisible(true);
        myTransactionsLarge.setVisible(false);
        logoutLarge.setVisible(false);
        editButton.setVisible(false);
        addRestaurantButton.setVisible(true);

        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/profile/seller/details/my-restaurants-details.fxml")));
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void myTransactionsButtonClicked() {
        profileView = "myTransactions";
        profileLarge.setVisible(false);
        myRestaurantsLarge.setVisible(false);
        myTransactionsLarge.setVisible(true);
        logoutLarge.setVisible(false);
        editButton.setVisible(false);
        addRestaurantButton.setVisible(false);

        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/profile/seller/details/my-transactions-details.fxml")));
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logoutButtonClicked() {
        profileLarge.setVisible(false);
        myRestaurantsLarge.setVisible(false);
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
            //YAML: Logout User

            try {
                Stage mainWindow = (Stage) profileSmall.getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                        "/frontend/bemirfoodclient/login-view.fxml")));
                mainWindow.getScene().setRoot(root);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            switch (profileView){
                case "profile":
                    profileButtonClicked();
                    break;
                case "myRestaurants":
                    myRestaurantsButtonClicked();
                    break;
                case "myTransactions":
                    myTransactionsButtonClicked();
                    break;
                default:
                    break;
            }
        }
    }

    public void showAlert(String content, ActionEvent event) {
        if (event != null) {
            event.consume();
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream(
                "assets/icons/error.png"))));
        alert.setTitle("Login failed");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }

    @FXML
    public void backButtonClicked() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/border/seller-border-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int editUserProfile(UserDto userDto) {
        //do the stuff in backend
        //YAML: Update current user profile
        return 200;
    }

    @FXML
    public void editButtonClicked() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(BemirfoodApplication.class.getResource(
                "/frontend/bemirfoodclient/profile/seller/details/edit-profile-dialog-view.fxml"));

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

            switch (editUserProfile(userDto)) {
                case 200:
                    profileButtonClicked();
                    break;
                case 400:
                    showAlert("Invalid phone number or password. (400 Invalid input)", event);
                    break;
                case 401:
                    showAlert("This phone number is not registered. (401 Unauthorized)", event);
                    break;
                case 403:
                    showAlert("You cannot access to this service. (403 Forbidden)", event);
                    break;
                case 404:
                    showAlert("Service not found. (404 Not Found)", event);
                    break;
                case 409:
                    showAlert("There was a conflict for access to this service. (409 Conflict)", event);
                    break;
                case 415:
                    showAlert("This media type cannot be accepted. (415 Unsupported Media Type)", event);
                    break;
                case 429:
                    showAlert("Please try again later. (429 Too Many Requests)", event);
                    break;
                case 500:
                    showAlert("This is from our side; pleas try again later :) (500 Internal Server Error)", event);
                default:
                    break;
            }
        });

        dialog.showAndWait();
    }

    private int addRestaurant(Restaurant restaurant) {
        //do the stuff in backend
        return 200;
    }

    public void addRestaurantButtonClicked() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Restaurant");
        dialog.initOwner(mainBorderPane.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader(BemirfoodApplication.class.getResource(
                "/frontend/bemirfoodclient/profile/seller/details/add-restaurant-dialog-view.fxml"
        ));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        AtomicReference<Restaurant> restaurant = new AtomicReference<>();
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
           AddRestaurantDialogController controller = fxmlLoader.getController();
            restaurant.set(controller.addRestaurant(/*fix later*/""));

            if (restaurant.get() == null) {
                event.consume();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            switch (addRestaurant(restaurant.get())) {
                case 200:
                    myRestaurantsButtonClicked();
                    break;
                case 400:
                    showAlert("Invalid phone number or password. (400 Invalid input)", null);
                    break;
                case 401:
                    showAlert("This phone number is not registered. (401 Unauthorized)", null);
                    break;
                case 403:
                    showAlert("You cannot access to this service. (403 Forbidden)", null);
                    break;
                case 404:
                    showAlert("Service not found. (404 Not Found)", null);
                    break;
                case 409:
                    showAlert("There was a conflict for access to this service. (409 Conflict)", null);
                    break;
                case 415:
                    showAlert("This media type cannot be accepted. (415 Unsupported Media Type)", null);
                    break;
                case 429:
                    showAlert("Please try again later. (429 Too Many Requests)", null);
                    break;
                case 500:
                    showAlert("This is from our side; pleas try again later :) (500 Internal Server Error)", null);
                default:
                    break;
            }
        }
    }
}
