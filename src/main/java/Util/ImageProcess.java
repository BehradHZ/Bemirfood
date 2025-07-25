package Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageProcess {
    public final static String outputDir = "Photos";
    public final static String format = "png";
    // -> * fileName = User{userId}

    public static String base64ToImageFile(String base64String, String fileName) throws IOException {
        // Decode base64 to bytes
        byte[] imageBytes = Base64.getDecoder().decode(base64String);

        // Read bytes as BufferedImage
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bis);

        if (image == null) {
            throw new IOException("Failed to decode image from base64 string.");
        }

        // Create directory if not exists
        File dir = new File(outputDir);
        if (!dir.exists()) dir.mkdirs();

        // Create full file path
        String filePath = outputDir + File.separator + fileName + "." + format;
        File outputFile = new File(filePath);

        // Write image to file
        boolean success = ImageIO.write(image, format, outputFile);
        if (!success) {
            throw new IOException("ImageIO.write failed for format: " + format);
        }

        return outputFile.getAbsolutePath();
    }

    public static String imageFileToBase64(String fileName) throws IOException {
        String imagePath = outputDir + File.separator + fileName + "." + format;
        Path path = Paths.get(imagePath);
        if(Files.exists(path)){
            byte[] fileContent = Files.readAllBytes(Paths.get(imagePath));
            return Base64.getEncoder().encodeToString(fileContent);
        }else{
            return null;
        }
    }

}
