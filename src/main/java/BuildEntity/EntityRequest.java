package BuildEntity;

import Deserializer.RestaurantDeserializer;
import HttpClientHandler.HttpResponseData;
import com.google.gson.*;
import frontend.bemirfoodclient.model.entity.*;

import java.util.ArrayList;
import java.util.List;

import static HttpClientHandler.LocalDateTimeAdapter.StringToDateTime;

public class EntityRequest {
    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Restaurant.class, new RestaurantDeserializer())
            .create();

    public static User getUser(Long userId) {
        HttpResponseData response = req.getUser(userId);
        return gson.fromJson(response.getBody(), User.class);
    }

    public static <T> T getEntity(Long id, Class<T> clazz) {
        HttpResponseData response = req.getUser(id);
        return gson.fromJson(response.getBody(), clazz);
    }

    public static Restaurant getRestaurant(Long restaurantId) {
        HttpResponseData response = req.getRestaurant(restaurantId);
        return gson.fromJson(response.getBody(), Restaurant.class);
    }

    public static Item getItem(Long itemId) {
        HttpResponseData response = req.getItem(itemId);
        return gson.fromJson(response.getBody(), Item.class);
    }
    public static List<Coupon> getCoupon() {
        List<Coupon> coupons = new ArrayList<>();
        HttpResponseData response = req.getCoupons();
        JsonObject json = response.getBody();
        JsonArray couponsJsonArray = json.getAsJsonArray("List of coupons");
        for(JsonElement jsonElement : couponsJsonArray) {
            JsonObject couponJson = jsonElement.getAsJsonObject();
            Coupon coupon = new Coupon();
            coupon.setId(couponJson.get("id").getAsLong());
            coupon.setCode(couponJson.get("code").getAsString());
            coupon.setType(couponJson.get("type").getAsString().equalsIgnoreCase("fixed") ? CouponType.fixed : CouponType.percent);
            coupon.setValue(couponJson.get("value").getAsLong());
            coupon.setMinPrice(couponJson.get("min_price").getAsLong());
            coupon.setUserCount(couponJson.get("user_count").getAsLong());
            coupon.setStartDate(StringToDateTime(couponJson.get("start_date").getAsString()));
            coupon.setEndDate(StringToDateTime(couponJson.get("end_date").getAsString()));
            coupons.add(coupon);
        }

        return coupons;
    }
}
