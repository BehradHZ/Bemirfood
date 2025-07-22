package HttpClientHandler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exception.NotFoundException;

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
        if(!body.has(key)) throw new NotFoundException("key " + key + " not found");
        JsonElement element = body.get(key);
        if (element == null || element.isJsonNull()) {
            return null;
        }
        return element.getAsString();
    }
}
