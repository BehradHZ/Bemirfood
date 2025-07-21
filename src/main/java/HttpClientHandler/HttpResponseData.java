package HttpClientHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpResponseData {
    private final int statusCode;
    private final JsonObject body;

    public HttpResponseData(int statusCode, String responseBody) {
        this.statusCode = statusCode;
        this.body = JsonParser.parseString(responseBody).getAsJsonObject();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public JsonObject getBody() {
        return body;
    }

    // Optional: Convenience method
    public String getString(String key) {
        return body.has(key) ? body.get(key).getAsString() : null;
    }
}
