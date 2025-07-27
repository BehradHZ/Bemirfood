package frontend.bemirfoodclient.controller.adminPanel.card;

import HttpClientHandler.HttpResponseData;
import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.User;
import frontend.bemirfoodclient.model.entity.UserStatus;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static HttpClientHandler.Requests.removeUserAdmin;

public class UserCardController {

    public ImageView userPhoto;
    public Label idRoleLabel;
    public Region spacer;
    public ImageView deleteButtonIcon;
    public Label nameLabel;
    public Button approveButton;
    public Button disapproveButton;

    private User user;
    private Consumer<User> deleteCallback;
    
    public void setUser(User user) {
        this.user = user;
        setScene();
    }
    
    public void initialize() {
        HBox.setHgrow(spacer, Priority.ALWAYS);
        deleteButtonIcon.setPreserveRatio(true);
        deleteButtonIcon.setFitWidth(25);
    }
    
    public void setScene() {
        idRoleLabel.setText("#" + user.getId() + " - " + user.getRole() + " -> ");
        nameLabel.setText(user.getFull_name());
        approveButton.setVisible(!user.getStatus().equals(UserStatus.approved));
        disapproveButton.setVisible(!user.getStatus().equals(UserStatus.not_approved));
    }

    public void approveButtonClicked(ActionEvent event) {
        user.setStatus(UserStatus.approved);
        setScene();
    }

    public void disapproveButtonClicked(ActionEvent event) {
        user.setStatus(UserStatus.not_approved);
        setScene();
    }

    public void setOnDelete(Consumer<User> callback) {
        this.deleteCallback = callback;
    }

    public void deleteButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
        alert.setTitle("Delete User");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete user: #" + user.getId() + " -> " + user.getFull_name() + "?");
        alert.setGraphic(null);
        Platform.runLater(() -> alert.getDialogPane().lookupButton(ButtonType.CANCEL).requestFocus());

        Optional<ButtonType> result = alert.showAndWait();

        // 2. If the user confirms, execute the callback
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteCallback.accept(user);
            deleteUser(user);
        }
    }


    public HttpResponseData deleteUser(User user) {
        return removeUserAdmin(user.getId());
    }
}
