package Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Token {
    private static final Path filePath = Path.of(System.getProperty("user.dir"), "userToken");

    static {
        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize blacklist file", e);
        }
    }

//    public static void add(String token) {
//        try {
//
//            Files.writeString(filePath, token + System.lineSeparator(), StandardOpenOption.APPEND);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to write token", e);
//        }
//    }

    public static String read(){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read file", e);
        }
    }

    public static boolean contains(String token) {
        try {
            List<String> lines = Files.readAllLines(filePath);
            return lines.contains(token);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    public static void addToken(String token) throws IOException {
        // Prepare a temp file
        Path tempFile = Files.createTempFile("temp", ".txt");

        try (
                BufferedReader reader = Files.newBufferedReader(filePath);
                BufferedWriter writer = Files.newBufferedWriter(tempFile)
        ) {
            String firstLine = reader.readLine();

            // Write newLine depending on whether firstLine is empty
            if (firstLine == null || firstLine.trim().isEmpty()) {
                writer.write(token);
            } else {
                // Replace first line
                writer.write(token);
            }
            writer.newLine();

            // Copy the rest of the original file (if any)
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }

        // Replace original file with the modified temp file
        Files.move(tempFile, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void clearFileContent() {
        try {
            Files.writeString(filePath, "", StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear file content", e);
        }
    }
}

