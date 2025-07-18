package frontend.bemirfoodclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class BemirfoodApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/Bemirfood_Logo.png"))); // Example resource path
        stage.getIcons().add(icon);

        FXMLLoader fxmlLoader;

        if (isUserLoggedIn() != null) {
            fxmlLoader = switch (getUserRole()) {
                case "admin" -> new FXMLLoader(BemirfoodApplication.class.getResource("homepage/admin-homepage-view.fxml"));
                case "buyer" -> new FXMLLoader(BemirfoodApplication.class.getResource("homepage/buyer-homepage-view.fxml"));
                case "seller" ->
                        new FXMLLoader(BemirfoodApplication.class.getResource("homepage/seller-homepage-view.fxml"));
                case "courier" ->
                        new FXMLLoader(BemirfoodApplication.class.getResource("homepage/courier-homepage-view.fxml"));
                default -> new FXMLLoader(BemirfoodApplication.class.getResource("login-view.fxml"));
            };
        } else {
            fxmlLoader = new FXMLLoader(BemirfoodApplication.class.getResource(
//                    "login-view.fxml"
//                    "register-view.fxml"
//                    "homepage/seller-homepage-view.fxml"
                    "profile/buyer/buyer-profile-view.fxml"
//                    " profile/seller/seller-profile-view.fxml"
//                    "profile/courier/courier-profile-view.fxml"


            ));
        }
        Scene scene = new Scene(fxmlLoader.load(), 787.5, 700 );
        stage.setTitle("Bemirfood!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public String isUserLoggedIn() {
        return null; //returns user id for login
    }

    public String getUserRole() {
        return "courier";
    }
}