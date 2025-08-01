package frontend.bemirfoodclient.controller.profile.seller;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import HttpClientHandler.Requests;
import Util.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.profile.seller.details.SellerProfileDetailsController;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static HttpClientHandler.Requests.updateUserProfile;
import static exception.exp.expHandler;

public class SellerProfileController {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();

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

//    @FXML
//    public Button backButton;
//    @FXML
//    public ImageView backButtonImage;
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

//        backButtonImage.setPreserveRatio(true);
//        backButtonImage.setFitHeight(20);

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

//    @FXML
//    public void backButtonClicked() {
//        try {
//            Stage stage = (Stage) backButton.getScene().getWindow();
//            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
//                    "/frontend/bemirfoodclient/border/seller-border-view.fxml")));
//            stage.getScene().setRoot(root);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public HttpResponseData editUserProfile(UserDto userDto) {
        Map<String, Object> request = new LinkedHashMap<>();
        if(userDto.getFullName() != null && !userDto.getFullName().isBlank()) request.put("full_name", userDto.getFullName());
        if(userDto.getPhone() != null && !userDto.getPhone() .isBlank()) request.put("phone", userDto.getPhone() );
        if(userDto.getEmail() != null && !userDto.getEmail().isBlank()) request.put("email", userDto.getEmail());
        if(userDto.getAddress() != null && !userDto.getAddress().isBlank()) request.put("address", userDto.getAddress());
        if(userDto.getBank_info() != null) request.put("bank_info", userDto.getBank_info());

        return updateUserProfile(gson.toJson(request));
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

        EditProfileDialogController editController = fxmlLoader.getController();
        editController.setUserData(SellerProfileDetailsController.getSeller());

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            EditProfileDialogController controller = fxmlLoader.getController();
            UserDto userDto = controller.updateCurrentUserProfile();

            HttpResponseData responseData = editUserProfile(userDto);
            if(responseData.getStatusCode() == 200){
                profileButtonClicked();
            }else{
                expHandler(responseData, "Failed to update profile", null);
            }
        });

        dialog.showAndWait();
    }

    private HttpResponseData addRestaurant(Restaurant restaurant) {
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("name", restaurant.getName());
        requestBody.put("address", restaurant.getAddress());
        requestBody.put("phone", restaurant.getPhone());
        requestBody.put("logoBase64", restaurant.getLogo());
        requestBody.put("tax_fee", restaurant.getTaxFee());
        requestBody.put("additional_fee", restaurant.getAdditionalFee());

        return Requests.addRestaurant(gson.toJson(requestBody));
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
            HttpResponseData response = addRestaurant(restaurant.get());
            if(response.getStatusCode() == 200) {
                myRestaurantsButtonClicked();
            }else{
                expHandler(response, "Failed to add restaurant", null);
            }
        }
    }
}
