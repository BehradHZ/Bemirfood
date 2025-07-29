package frontend.bemirfoodclient.controller.border;

import frontend.bemirfoodclient.BemirfoodApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SellerBorderController {
    @FXML
    public BorderPane mainBorderpane;
    @FXML
    public ImageView borderBemirfoodLogo;
    @FXML
    public TextField searchTextField;
    @FXML
    public ImageView searchIcon;
    @FXML
    public ImageView profileIcon;
    @FXML
    public Region toolbarSpacer;

    public void initialize() {
        borderBemirfoodLogo.setPreserveRatio(true);
        borderBemirfoodLogo.setFitHeight(40);

        searchIcon.setPreserveRatio(true);
        searchIcon.setFitHeight(17);

        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);

        profileIcon.setPreserveRatio(true);
        profileIcon.setFitHeight(27);

        profileButtonClicked();
    }

    public void setCenterContent(Node content) {
        mainBorderpane.setCenter(content);
    }

    @FXML
    public void borderBemirfoodLogoClicked() {
        try {
            Stage stage = (Stage) profileIcon.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/frontend/bemirfoodclient/border/seller-border-view.fxml")));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCurrentUserProfile() {
        //do the stuff in backend
        return 200; //temporary
    }

    @FXML
    public void profileButtonClicked() {
        switch (getCurrentUserProfile()){
            case 200:
                try {
//                    Stage stage = (Stage) profileIcon.getScene().getWindow();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                            "/frontend/bemirfoodclient/profile/seller/seller-profile-view.fxml")));
//                    stage.getScene().setRoot(root);
                    mainBorderpane.setCenter(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 400:
                showAlert("Invalid phone number or password. (400 Invalid input)");
                break;
            case 401:
                showAlert("This phone number is not registered. (401 Unauthorized)");
                break;
            case 403:
                showAlert("You cannot access to this service. (403 Forbidden)");
                break;
            case 404:
                showAlert("Service not found. (404 Not Found)");
                break;
            case 409:
                showAlert("There was a conflict for access to this service. (409 Conflict)");
                break;
            case 415:
                showAlert("This media type cannot be accepted. (415 Unsupported Media Type)");
                break;
            case 429:
                showAlert("Please try again later. (429 Too Many Requests)");
                break;
            case 500:
                showAlert("This is from our side; pleas try again later :) (500 Internal Server Error)");
            default:
                break;
        }
    }

    public void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
        alert.setTitle("Loading profile failed");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }

    @FXML
    public void searchButtonClicked() {

    }

}
