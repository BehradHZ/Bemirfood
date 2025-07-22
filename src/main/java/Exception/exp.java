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
        int statusCode = response.getStatusCode();
        String message =  response.getBody().get("message").getAsString();
        String error = response.getBody().get("error").getAsString();
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("code", statusCode);
        responseBody.put("error", error);
        responseBody.put("message", message);

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
