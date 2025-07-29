package HttpClientHandler;

import Util.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static HttpClientHandler.HttpClientHandler.sendGetRequest;
import static HttpClientHandler.HttpClientHandler.sendRequest;
import static exception.exp.expHandler;

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

    public static HttpResponseData getRestaurantItemsSeller(Long restaurantId) {
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

    public static HttpResponseData getRestaurantMenusSeller(Long restaurantId) {
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

    public static HttpResponseData getCustomerFavorites() {
        String path = "favorites";
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

    public static HttpResponseData getRestaurantItemsCustomer(Long restaurantId) {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "vendors/" + restaurantId +"/items", HttpRequest.GET, null,Token.read());
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

    public static HttpResponseData getRestaurantMenusCustomer(Long restaurantId) {
        String path = "vendors/" + restaurantId + "/menus";
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

    public static HttpResponseData addToFavorites(Long restaurantId) {
        String path = "favorites/" + restaurantId;
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path, HttpRequest.PUT, null,Token.read());
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

    public static HttpResponseData removeFromFavorites(Long restaurantId) {
        String path = "favorites/" + restaurantId;
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

    public static HttpResponseData getCartItemQuantity(Long itemId) {
        String path = "items/"+ itemId + "/cart-item";
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

    public static HttpResponseData modifyCartItems(String requestBody) {
        String path = "items/cart-items";
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

    public static HttpResponseData getCustomerOrders() {
        String path = "orders/history";
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

    public static HttpResponseData getAvailableDeliveryOrders() {
        String path = "deliveries/available";
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

    public static HttpResponseData searchDeliveryHistory(Map<String, String> queryParams) {
        String path = "deliveries/history";
        try{
            HttpResponseData responseData =
                    sendGetRequest(
                            path, queryParams,Token.read());
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

    public static HttpResponseData changeOrderStatusDelivery(Long orderId, String requestBody) {
        String path = "deliveries/" + orderId;
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

    public static HttpResponseData changeOrderStatusSeller(Long orderId, String requestBody) {
        String path = "restaurants/orders/" + orderId;
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

    public static HttpResponseData searchOrderHistoryCustomer(Map<String, String> queryParams) {
        String path = "orders/history";
        try{
            HttpResponseData responseData =
                    sendGetRequest(
                            path, queryParams,Token.read());
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

    public static HttpResponseData pay(String requestBody) {
        String path = "payment/online";
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path,HttpRequest.POST, requestBody, Token.read());
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

    public static HttpResponseData getBalance() {
        String path = "wallet/balance";
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path,HttpRequest.GET, null, Token.read());
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

    public static HttpResponseData walletTopUp(String requestBody) {
        String path = "wallet/top-up";
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path,HttpRequest.POST, requestBody, Token.read());
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

    public static HttpResponseData getTransactions() {
        String path = "transactions";
        try{
            HttpResponseData responseData =
                    sendGetRequest(
                            path, Token.read());
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

    public static HttpResponseData getUserAdmin() {
        String path = "admin/users";
        try{
            HttpResponseData responseData =
                    sendGetRequest(
                            path, Token.read());
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

    public static HttpResponseData removeUserAdmin(Long userId) {
        String path = "admin/users/" + userId;
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

    public static HttpResponseData searchOrderHistoryAdmin(Map<String, String> queryParams) {
        String path = "admin/orders";
        try{
            HttpResponseData responseData =
                    sendGetRequest(
                            path, queryParams,Token.read());
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

    public static HttpResponseData searchTransactionAdmin(Map<String, String> queryParams) {
        String path = "admin/transactions";
        try{
            HttpResponseData responseData =
                    sendGetRequest(
                            path, queryParams,Token.read());
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

    public static HttpResponseData getCouponsAdmin() {
        String path = "admin/coupons";
        try{
            HttpResponseData responseData =
                    sendGetRequest(
                            path,Token.read());
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

    public static HttpResponseData addCouponAdmin(String requestBody) {
        String path = "admin/coupons";
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path,HttpRequest.POST,requestBody,Token.read());
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

    public static HttpResponseData removeCouponAdmin(Long couponId) {
        String path = "admin/coupons/" + couponId;
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path,HttpRequest.DELETE, null,Token.read());
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

    public static HttpResponseData updateCouponAdmin(String requestBody, Long  couponId) {
        String path = "admin/coupons/" + couponId;
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path,HttpRequest.PUT, requestBody,Token.read());
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

    public static HttpResponseData validateCoupon(Map<String, String> queryParams) {
        String path = "coupons";
        try{
            HttpResponseData responseData =
                    sendGetRequest(
                            path,queryParams,Token.read());
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

    public static HttpResponseData applyCoupon(String requestBody) {
        String path = "coupons/apply";
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path,HttpRequest.POST, requestBody,Token.read());
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

    public static HttpResponseData getItemsWithFilter(String requestBody) {
        String path = "items";
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path,HttpRequest.POST, requestBody,Token.read());
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

    public static HttpResponseData submitRating(String requestBody) {
        String path = "ratings";
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path,HttpRequest.POST, requestBody,Token.read());
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

    public static HttpResponseData getOrderRating(Long orderId) {
        String path = "ratings/order/" + orderId;
        try{
            HttpResponseData responseData =
                    sendGetRequest(
                            path,Token.read());
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

    public static HttpResponseData editOrderRating(String requestBody, Long ratingId) {
        String path = "ratings/" + ratingId;
        try{
            HttpResponseData responseData =
                    sendRequest(
                            path,HttpRequest.PUT, requestBody,Token.read());
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

    public static HttpResponseData getItemAvgRating(Long itemId) {
        String path = "ratings/items/" + itemId;
        try{
            HttpResponseData responseData =
                    sendGetRequest(
                            path,Token.read());
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
