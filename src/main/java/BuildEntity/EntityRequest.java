package BuildEntity;

import Deserializer.RestaurantDeserializer;
import HttpClientHandler.HttpResponseData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import frontend.bemirfoodclient.model.entity.Item;
import frontend.bemirfoodclient.model.entity.Restaurant;
import frontend.bemirfoodclient.model.entity.User;

public class EntityRequest {
    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Restaurant.class, new RestaurantDeserializer())
            .create();

    public static User getUser(Long userId) {
        HttpResponseData response = req.getUser(userId);
        return gson.fromJson(response.getBody(), User.class);
    }

    public static Restaurant getRestaurant(Long restaurantId) {
        HttpResponseData response = req.getRestaurant(restaurantId);
        return gson.fromJson(response.getBody(), Restaurant.class);
    }

    public static Item getItem(Long itemId) {
        HttpResponseData response = req.getItem(itemId);
        return gson.fromJson(response.getBody(), Item.class);
    }
}
