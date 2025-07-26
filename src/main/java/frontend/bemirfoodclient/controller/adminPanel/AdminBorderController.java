package frontend.bemirfoodclient.controller.adminPanel;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.controller.adminPanel.card.UserCardController;
import frontend.bemirfoodclient.model.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminBorderController {
    public ImageView borderBemirfoodLogo;
    public ImageView searchIcon;
    public TextField searchTextField;
    public Region toolbarSpacer;
    public Button usersSmall;
    public Button usersLarge;
    public Button ordersSmall;
    public Button ordersLarge;
    public Button transactionsSmall;
    public Button transactionsLarge;
    public Button discountCodesSmall;
    public Button discountCodesLarge;
    public Button logoutSmall;
    public Button logoutLarge;
    public VBox contentVBox;

    public void initialize() {
        borderBemirfoodLogo.setPreserveRatio(true);
        borderBemirfoodLogo.setFitHeight(40);

        searchIcon.setPreserveRatio(true);
        searchIcon.setFitHeight(17);

        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);
        usersButtonClicked();
    }

    public void searchButtonClicked() {
    }

    public void usersButtonClicked() {
        usersLarge.setVisible(true);
        ordersLarge.setVisible(false);
        transactionsLarge.setVisible(false);
        discountCodesLarge.setVisible(false);
        logoutLarge.setVisible(false);

        contentVBox.getChildren().clear();
        List<User> users = getUsers(); // Get user data from backend/factory

        for (User user : users) {
            try {
                // --- 3. Load the user-card.fxml for each user ---
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/adminPanel/card/user-card.fxml"
                ));
                Pane card = loader.load();

                // --- 4. Get the card's controller and set its data ---
                UserCardController controller = loader.getController();
                controller.setUser(user);

                controller.setOnDelete(userToDelete -> {
                    switch (deleteUser(user)) {
                        case 200:
                            usersButtonClicked();
                            break;
                        //show alerts
                    }
                });

                contentVBox.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        // Create a few sample users to display
        userList.add(new User("Alice", "09111111111", "buyer", "alice@email.com", null, "123 Apple St", null, "pass1"));
        userList.add(new User("Bob", "09222222222", "seller", "bob@email.com", null, "456 Burger Blvd", null, "pass2"));
        userList.add(new User("Charlie", "09333333333", "courier", "charlie@email.com", null, "789 Delivery Dr", null, "pass3"));
        return userList;
    }

    public void ordersButtonClicked(ActionEvent event) {
    }

    public void transactionsButtonClicked(ActionEvent event) {
    }

    public void discountCodesButtonClicked(ActionEvent event) {
    }

    public void logoutButtonClicked(ActionEvent event) {
    }

    public int deleteUser(User user) {
        //do the stuff in backend

        return 200;
    }
}
