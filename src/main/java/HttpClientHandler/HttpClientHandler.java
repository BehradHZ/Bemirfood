package HttpClientHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpClientHandler {
    private static final String baseUrl = "http://localhost:4567";

    public static HttpResponseData sendRequest(String urlStr, HttpRequest method,String jsonInputString) throws IOException {
        String completeUrl = String.format("%s/%s", baseUrl, urlStr);
        URL url = new URL(completeUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method.toString());
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            if(jsonInputString != null) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
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

    public static HttpResponseData sendRequest(String urlStr, HttpRequest method, String jsonInputString, String token) throws IOException {
        String completeUrl = String.format("%s/%s", baseUrl, urlStr);
        URL url = new URL(completeUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method.toString());
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");

        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        conn.setDoOutput(true);

        if (jsonInputString != null) {
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
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

    public static HttpResponseData sendGetRequest(String urlStr, String token) throws IOException {
        String completeUrl = String.format("%s/%s", baseUrl, urlStr);
        URL url = new URL(completeUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");

        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        conn.setDoOutput(true);

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

    public static HttpResponseData sendGetRequest(String urlStr, Map<String, String> queryParams ,String token) throws IOException {
        String completeUrl =
                buildQueryString(String.format("%s/%s", baseUrl, urlStr), queryParams);

        URL url = new URL(completeUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");

        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        conn.setDoOutput(true);

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


    public static String buildQueryString(String basePath, Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(basePath);
        if (params != null && !params.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
            sb.deleteCharAt(sb.length() - 1); // Remove trailing '&'
        }
        return sb.toString();
    }



}
