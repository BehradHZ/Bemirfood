package HttpClientHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientHandler {
    public static HttpResponseData sendPostRequest(String urlStr, String jsonInputString) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int statusCode = conn.getResponseCode();
        InputStream inputStream = (statusCode >= 400) ? conn.getErrorStream() : conn.getInputStream();

        StringBuilder response = new StringBuilder();
        if (inputStream != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }
        }

        return new HttpResponseData(statusCode, response.toString());
    }

}
