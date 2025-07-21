package frontend.bemirfoodclient.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class ImageLoader {
    public static void displayBase64Image(String base64String, ImageView imageView) {
        if (base64String.startsWith("data:image")) {
            base64String = base64String.substring(base64String.indexOf(",") + 1);
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64String);

        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        Image image = new Image(bis);

        imageView.setImage(image);
    }
}
