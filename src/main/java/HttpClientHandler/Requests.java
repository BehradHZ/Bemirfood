package HttpClientHandler;

import Util.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static Exception.exp.expHandler;
import static HttpClientHandler.HttpClientHandler.sendRequest;

public class Requests {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).serializeNulls()
            .create();

    static Map<String, String> errorResponse = Map.of(
            "error", "Internal Server Error",
            "message", "Failed to connect to server"
    );

    static String errorContent = gson.toJson(errorResponse);

    public static HttpResponseData getCurrentUserProfile() {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "auth/profile", HttpRequest.GET, null, Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect to server",
                    null
                    );
            return response;
        }
    }

    public static HttpResponseData updateUserProfile(String requestBody) {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "auth/profile", HttpRequest.PUT, requestBody, Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData addRestaurant(String requestBody) {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "restaurants", HttpRequest.POST, requestBody, Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData getSellerRestaurants() {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "restaurants/mine", HttpRequest.GET, null,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData editSellerRestaurant(String requestBody, Long restaurantId) {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "restaurants/" + restaurantId, HttpRequest.PUT, requestBody,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData getRestaurantItems(Long restaurantId) {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "restaurants/" + restaurantId +"/items", HttpRequest.GET, null,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData addRestaurantItem(String requestBody, Long restaurantId) {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "restaurants/" + restaurantId +"/item", HttpRequest.GET, requestBody,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData editRestaurantItem(String requestBody, Long restaurantId, Long itemId) {
        String path = "restaurants/" + restaurantId + "/item/" + itemId;
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path, HttpRequest.PUT, requestBody,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData deleteRestaurantItem(Long restaurantId, Long itemId) {
        String path = "restaurants/" + restaurantId + "/item/" + itemId;
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path, HttpRequest.DELETE, null,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData getRestaurantMenus(Long restaurantId) {
        String path = "restaurants/" + restaurantId + "/menus";
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path, HttpRequest.GET, null,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData getRestaurantMenu(Long restaurantId, String menuTitle) {
        String path = "restaurants/" + restaurantId + "/menu/" + menuTitle;
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path, HttpRequest.GET, null,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData addRestaurantMenus(Long restaurantId, String requestBody) {
        String path = "restaurants/" + restaurantId + "/menu";
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path, HttpRequest.POST, requestBody,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData removeRestaurantMenus(Long restaurantId, String menuTitle) {
        String path = "restaurants/" + restaurantId + "/menu/" +  menuTitle;
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path, HttpRequest.DELETE, null,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData addItemMenu(Long restaurantId, String menuTitle, String requestBody) {
        String path = "restaurants/" + restaurantId + "/menu/" +  menuTitle;
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path, HttpRequest.PUT, requestBody,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData removeItemMenu(Long restaurantId, String menuTitle, Long itemId) {
        String path = "restaurants/" + restaurantId + "/menu/" +  menuTitle + "/" +  itemId;
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path, HttpRequest.DELETE, null,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData getSellerTransaction() {
        String path = "transactions";
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path, HttpRequest.GET, null,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }

    public static HttpResponseData getRestaurantOrders(Long restaurantId) {
        String path = "restaurants/" + restaurantId + "/orders";
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path, HttpRequest.GET, null,Token.read());
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
            HttpResponseData response = new HttpResponseData(500, errorContent);
            expHandler(
                    response,
                    "Failed to connect server",
                    null
            );
            return response;
        }
    }
}
