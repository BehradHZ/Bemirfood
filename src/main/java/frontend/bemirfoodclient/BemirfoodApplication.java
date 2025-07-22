package frontend.bemirfoodclient;

import com.google.gson.Gson;
import frontend.bemirfoodclient.model.dto.UserDto;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class BemirfoodApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/Bemirfood_Logo.png"))); // Example resource path
        stage.getIcons().add(icon);

        FXMLLoader fxmlLoader;

        if (isUserLoggedIn() != null) {
            fxmlLoader = switch (getUserRole()) {
                case "admin" -> new FXMLLoader(BemirfoodApplication.class.getResource("border/admin-border-view.fxml"));
                case "buyer" -> new FXMLLoader(BemirfoodApplication.class.getResource("border/buyer-border-view.fxml"));
                case "seller" ->
                        new FXMLLoader(BemirfoodApplication.class.getResource("border/seller-border-view.fxml"));
                case "courier" ->
                        new FXMLLoader(BemirfoodApplication.class.getResource("border/courier-border-view.fxml"));
                default -> new FXMLLoader(BemirfoodApplication.class.getResource("login-view.fxml"));
            };
        } else {
            fxmlLoader = new FXMLLoader(BemirfoodApplication.class.getResource(
//                    "login-view.fxml"
//                    "register-view.fxml"
//                    "register-additional-view.fxml"
//                    "border/seller-border-view.fxml"
//                    "profile/buyer/buyer-profile-view.fxml"
                    "profile/seller/seller-profile-view.fxml"
//                    "profile/courier/courier-profile-view.fxml"


            ));
        }
        Scene scene = new Scene(fxmlLoader.load(), 787.5, 700 );
        stage.setTitle("Bemirfood!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        try {
            String homeDirectory = System.getProperty("user.dir");
            Path filePath = Path.of(homeDirectory, "registerTemp.txt");
            if (Files.exists(filePath) && !Files.readString(filePath).startsWith("login data")) {
                Files.writeString(filePath, "");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }

    public String isUserLoggedIn() {
        //do the stuff in backend
        //save some kind of token so if user session is valid, they don't have to login again

        //temporary
        String homeDirectory = System.getProperty("user.dir");
        Path filePath = Path.of(homeDirectory, "registerTemp.txt");

        if (!Files.exists(filePath)) {
            return null;
        }

        try {
            if (Files.readString(filePath).startsWith("login data")) {
                String fileContent = Files.readString(filePath);
                int jsonStartIndex = fileContent.indexOf('{');
                String jsonText = fileContent.substring(jsonStartIndex);

                Gson gson = new Gson();
                UserDto userDto = gson.fromJson(jsonText, UserDto.class);
                return userDto.getPhone();  //returns user mobile for login
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getUserRole() {
        //do the stuff in backend
        //save some kind of token so without login they can reach right view

        String homeDirectory = System.getProperty("user.dir");
        Path filePath = Path.of(homeDirectory, "registerTemp.txt");

        if (!Files.exists(filePath)) {
            return null;
        }

        try {
            if (Files.readString(filePath).startsWith("login data")) {
                String fileContent = Files.readString(filePath);
                int jsonStartIndex = fileContent.indexOf('{');
                String jsonText = fileContent.substring(jsonStartIndex);

                Gson gson = new Gson();
                UserDto userDto = gson.fromJson(jsonText, UserDto.class);
                return userDto.getRole();  //returns user role for login
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}