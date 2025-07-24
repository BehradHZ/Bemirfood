package BuildEntity;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import Util.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.Map;

import static exception.exp.expHandler;
import static HttpClientHandler.HttpClientHandler.sendGetRequest;

public class req {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();

    static Map<String, String> errorResponse = Map.of(
            "error", "Internal Server Error",
            "message", "Failed to connect to server"
    );

    @FunctionalInterface
    public interface RequestSupplierWithException<T> {
        T get() throws Exception;
    }


    static String errorContent = gson.toJson(errorResponse);

    public static HttpResponseData getUser(Long userId) {
        String path = "dt/user/" + userId;
        return tryRequestWithRetry(
                () -> sendGetRequest(path, Token.read()),
                3,
                "Failed to connect server"
        );
    }

    public static HttpResponseData getRestaurant(Long restaurantId) {
        String path = "dt/restaurant/" + restaurantId;
        return tryRequestWithRetry(
                () -> sendGetRequest(path, Token.read()),
                3,
                "Failed to connect server"
        );
    }

    public static HttpResponseData getItem(Long itemId) {
        String path = "dt/item/" + itemId;
        return tryRequestWithRetry(
                () -> sendGetRequest(path, Token.read()),
                3,
                "Failed to connect server"
        );
    }

    public static HttpResponseData tryRequestWithRetry(
            RequestSupplierWithException<HttpResponseData> requestSupplier,
            int maxRetries,
            String failMessage
    ) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return requestSupplier.get();
            } catch (Exception e) {
                System.err.println("Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt == maxRetries) {
                    e.printStackTrace();
                    HttpResponseData response = new HttpResponseData(500, errorContent);
                    expHandler(response, failMessage + " after " + maxRetries + " attempts", null);
                    return response;
                }
                try {
                    Thread.sleep(1000); // delay before retry
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return new HttpResponseData(500, errorContent);
    }
}
