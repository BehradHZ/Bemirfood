package Deserializer;

import com.google.gson.*;
import frontend.bemirfoodclient.model.entity.Item;

import java.lang.reflect.Type;
import java.util.List;

public class ItemDeserializer implements JsonDeserializer<Item> {
    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Integer id = obj.get("id").getAsInt();
        String name = obj.get("name").getAsString();
        String description = obj.get("description").getAsString();
        String imageBase64 = obj.get("imageBase64").isJsonNull() ? null : obj.get("imageBase64").getAsString();
        double price = obj.get("price").getAsDouble();
        int supply = obj.get("supply").getAsInt();
        List<String> keywords = context.deserialize(obj.get("keywords"), List.class);

        return new Item((long)id, name,  imageBase64,description, price, supply, keywords);
    }
}
