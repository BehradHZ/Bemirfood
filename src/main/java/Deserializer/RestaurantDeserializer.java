package Deserializer;

import com.google.gson.*;
import frontend.bemirfoodclient.model.entity.Restaurant;

import java.lang.reflect.Type;

public class RestaurantDeserializer implements JsonDeserializer<Restaurant> {
    @Override
    public Restaurant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Integer id = obj.get("id").getAsInt();
        String name = obj.get("name").getAsString();
        String address = obj.get("address").getAsString();
        String phone = obj.get("phone").getAsString();
        String logoBase64 = obj.get("logoBase64").isJsonNull() ? null : obj.get("logoBase64").getAsString();
        double tax_fee = obj.get("tax_fee").getAsDouble();
        double additional_fee = obj.get("additional_fee").getAsDouble();

        // Use your custom constructor
        return new Restaurant((long)id, name, address, phone, logoBase64, tax_fee, additional_fee);
    }
}
