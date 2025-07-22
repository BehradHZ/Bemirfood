package HttpClientHandler;

import Util.Token;

import java.io.IOException;

import static HttpClientHandler.HttpClientHandler.sendRequest;

public class Requests {
    public static HttpResponseData getCurrentUserProfile() {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "auth/profile", HttpRequest.GET, null, Token.read());
            return responseData;
        }catch (IOException e){
            return new HttpResponseData(500, "Internal Server Error");
        }
    }

    public static HttpResponseData updateUserProfile(String requestBody) {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "auth/profile", HttpRequest.PUT, requestBody, Token.read());
            return responseData;
        }catch (IOException e){
            return new HttpResponseData(500, "Internal Server Error");
        }
    }

    public static HttpResponseData addRestaurant(String requestBody) {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "restaurants", HttpRequest.POST, requestBody, Token.read());
            return responseData;
        }catch (IOException e){
            return new HttpResponseData(500, "Internal Server Error");
        }
    }

    public static HttpResponseData getSellerRestaurants() {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "restaurants/mine", HttpRequest.GET, null,Token.read());
            return responseData;
        }catch (IOException e){
            return new HttpResponseData(500, "Internal Server Error");
        }
    }

    public static HttpResponseData editSellerRestaurant(String requestBody, Long restaurantId) {
        try{
            HttpResponseData responseData =
                    sendRequest(
                            "restaurants/" + restaurantId, HttpRequest.PUT, requestBody,Token.read());
            return responseData;
        }catch (IOException e){
            return new HttpResponseData(500, "Internal Server Error");
        }
    }
}
