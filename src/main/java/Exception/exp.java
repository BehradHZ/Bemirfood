package Exception;

import HttpClientHandler.HttpResponseData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.bemirfoodclient.BemirfoodApplication;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


public class exp {

    static Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

    public static void expHandler(HttpResponseData response, String title, BooleanProperty page) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        String message = "This is our side";
        String error = "Internal Server Error";
        Integer statusCode =  response.getStatusCode();

        if(response.getBody().get("message") != null) {
            message =  response.getBody().get("message").getAsString();
        }
        if(response.getBody().get("error") != null) {
            error =  response.getBody().get("error").getAsString();
        }

        responseBody.put("code", statusCode);
        responseBody.put("message", message);
        responseBody.put("error", error);

        String content = prettyGson.toJson(responseBody);
        showAlert(title, content, page);
    }

    private static void showAlert(String title, String content, BooleanProperty page) {
        if(page != null) page.set(false);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(BemirfoodApplication.class.getResourceAsStream("assets/icons/error.png"))));
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setGraphic(null);
        alert.showAndWait();
    }
}
