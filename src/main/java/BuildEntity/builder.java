package BuildEntity;

import HttpClientHandler.HttpResponseData;
import HttpClientHandler.LocalDateTimeAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dto.OrderDto;
import frontend.bemirfoodclient.model.entity.*;

import java.util.ArrayList;
import java.util.List;

import static BuildEntity.EntityRequest.*;
import static HttpClientHandler.LocalDateTimeAdapter.StringToTime;
import static exception.exp.expHandler;



public class builder {

    @FunctionalInterface
    public interface RequestFunction {
        HttpResponseData apply(Object... args);
    }

    public static Order buildOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        User user = getUser(orderDto.getCustomer_id());

        Customer cust = new Customer();
        cust.setId(user.getId());
        if(user.getPhoto() != null) cust.setPhoto(user.getPhoto());
        cust.setAddress(user.getAddress());
        cust.setBank_info(user.getBank_info());
        cust.setPassword(user.getPassword());
        cust.setEmail(user.getEmail());
        cust.setFull_name(user.getFull_name());
        cust.setMobile(user.getMobile());
        order.setCustomer(cust);
        cust.setRole(user.getRole());

        if(orderDto.getCourier_id() != null){
            user = getUser(orderDto.getCourier_id());
            Delivery del = new Delivery();

            del.setId(user.getId());
            if(user.getPhoto() != null) del.setPhoto(user.getPhoto());
            del.setAddress(user.getAddress());
            del.setBank_info(user.getBank_info());
            del.setPassword(user.getPassword());
            del.setEmail(user.getEmail());
            del.setFull_name(user.getFull_name());
            del.setMobile(user.getMobile());
            order.setDelivery(del);
            del.setRole(user.getRole());
        }else{
            order.setDelivery(null);
        }

        order.setRestaurant(getRestaurant(orderDto.getVendor_id()));
        List<CartItem> cartItems = new ArrayList<>();
        for(OrderDto.ItemHelper ih : orderDto.getItems()){
            if(ih.getQuantity() > 0){
                Item item = new Item();
                item = getItem(ih.getItem_id());
                cartItems.add(new CartItem(item, ih.getQuantity()));
            }
        }
        if(orderDto.getCoupon_id() != null){
            HttpResponseData res = req.getCoupon(orderDto.getCoupon_id());
            JsonObject obj = res.getBody().get("Coupon details").getAsJsonObject();
            Coupon coupon = new Coupon();
            coupon.setId(orderDto.getCoupon_id());
            coupon.setCode(obj.get("code").getAsString());
            coupon.setType(CouponType.valueOf(obj.get("type").getAsString().toLowerCase()));
            coupon.setValue(obj.get("value").getAsLong());
            coupon.setUserCount(obj.get("user_count").getAsLong());
            coupon.setMinPrice(obj.get("min_price").getAsLong());
            coupon.setStartDate(LocalDateTimeAdapter.StringToDateTime(obj.get("start_date").getAsString()));
            coupon.setEndDate(LocalDateTimeAdapter.StringToDateTime(obj.get("end_date").getAsString()));
            order.setCoupon(coupon);
        }
        order.setCartItems(cartItems);
        order.setDeliveryAddress(orderDto.getDelivery_address());
        order.setCreatedAt(StringToTime(orderDto.getCreated_at()));
        order.setUpdatedAt(StringToTime(orderDto.getUpdated_at()));
        String status = orderDto.getStatus();
        try{
            order.setStatus(OrderStatus.strToStatus(status));
        }catch (Exception e){
            order.setStatus(OrderStatus.submitted);
        }
        order.setCourierFee(orderDto.getCourier_fee());
        order.setPaid(orderDto.getIs_paid());
        return order;
    }


    public static List<Order> buildOrderList(RequestFunction requestFunction, String title, String error, Object... args) {
        HttpResponseData response = requestFunction.apply(args);

        if(response.getStatusCode() != 200){
            expHandler(response, error, null);
        }

        JsonObject json = response.getBody();
        JsonArray ordersJsonArray = json.getAsJsonArray(title);
        List<Order> orders = new ArrayList<>();

        for (JsonElement element : ordersJsonArray) {
            JsonObject orderJson = element.getAsJsonObject();

            OrderDto dto = new OrderDto();
            dto.setId(orderJson.get("id").getAsLong());
            if(!orderJson.get("delivery_address").isJsonNull()){
                dto.setDelivery_address(orderJson.get("delivery_address").getAsString());
            }else{
                dto.setDelivery_address("");
            }
            dto.setCustomer_id(orderJson.get("customer_id").getAsLong());
            dto.setVendor_id(orderJson.get("vendor_id").getAsLong());

            if (!orderJson.get("coupon_id").isJsonNull()) {
                dto.setCoupon_id(orderJson.get("coupon_id").getAsLong());
            }

            JsonArray itemsArray = orderJson.getAsJsonArray("items");
            List<OrderDto.ItemHelper> itemHelpers = new ArrayList<>();
            for (JsonElement itemElement : itemsArray) {
                JsonObject itemObj = itemElement.getAsJsonObject();
                Long itemId = itemObj.get("item_id").getAsLong();
                int quantity = itemObj.get("quantity").getAsInt();
                itemHelpers.add(new OrderDto.ItemHelper(itemId, quantity));
            }
            dto.setItems(itemHelpers);

            dto.setRaw_price(orderJson.get("raw_price").getAsLong());
            dto.setTax_fee(orderJson.get("tax_fee").getAsDouble());
            dto.setAdditional_fee(orderJson.get("additional_fee").getAsDouble());
            dto.setCourier_fee(orderJson.get("courier_fee").getAsDouble());
            dto.setPay_price(orderJson.get("pay_price").getAsLong());

            if (!orderJson.get("courier_id").isJsonNull()) {
                dto.setCourier_id(orderJson.get("courier_id").getAsLong());
            }

            dto.setStatus(orderJson.get("status").getAsString());
            dto.setCreated_at(orderJson.get("created_at").getAsString());
            dto.setUpdated_at(orderJson.get("updated_at").getAsString());
            dto.setIs_paid(orderJson.get("is_paid").getAsBoolean());

            Order order = buildOrder(dto);

            orders.add(order);
        }

        return orders;
    }

    public static List<Transaction> buildTransactionList(RequestFunction requestFunction, String title, String error, Object ... args) {
        List<Transaction> transactions = new ArrayList<>();
        HttpResponseData response = requestFunction.apply(args);
        if(response.getStatusCode() != 200) expHandler(response, null, null);
        JsonArray transactionsJsonArray = response.getBody().get(title).getAsJsonArray();
        for(JsonElement element : transactionsJsonArray){
            Transaction transaction = new Transaction();
            JsonObject obj = element.getAsJsonObject();
            transaction.setId(obj.get("id").getAsLong());
            User user = EntityRequest.getUser(obj.get("user_id").getAsLong());
            transaction.setSender(user);
            transaction.setPaymentStatus(PaymentStatus.strToStatus(obj.get("status").getAsString()));
            transaction.setPaymentMethod(PaymentMethod.strToStatus(obj.get("method").getAsString()));
            transaction.setTimestamp(LocalDateTimeAdapter.StringToTime(obj.get("timestamp").getAsString()));

            HttpResponseData orderDtoRes = req.getOrder(obj.get("order_id").getAsLong());
            if(orderDtoRes.getStatusCode() != 200)  expHandler(response, error, null);
            JsonObject orderJson = orderDtoRes.getBody().getAsJsonObject();

            OrderDto dto = new OrderDto();
            dto.setId(orderJson.get("id").getAsLong());
            if(!orderJson.get("delivery_address").isJsonNull()){
                dto.setDelivery_address(orderJson.get("delivery_address").getAsString());
            }else{
                dto.setDelivery_address("");
            }
            dto.setCustomer_id(orderJson.get("customer_id").getAsLong());
            dto.setVendor_id(orderJson.get("vendor_id").getAsLong());

            if (!orderJson.get("coupon_id").isJsonNull()) {
                dto.setCoupon_id(orderJson.get("coupon_id").getAsLong());
            }

            JsonArray itemsArray = orderJson.getAsJsonArray("items");
            List<OrderDto.ItemHelper> itemHelpers = new ArrayList<>();
            for (JsonElement itemElement : itemsArray) {
                JsonObject itemObj = itemElement.getAsJsonObject();
                Long itemId = itemObj.get("item_id").getAsLong();
                int quantity = itemObj.get("quantity").getAsInt();
                itemHelpers.add(new OrderDto.ItemHelper(itemId, quantity));
            }
            dto.setItems(itemHelpers);

            dto.setRaw_price(orderJson.get("raw_price").getAsLong());
            dto.setTax_fee(orderJson.get("tax_fee").getAsDouble());
            dto.setAdditional_fee(orderJson.get("additional_fee").getAsDouble());
            dto.setCourier_fee(orderJson.get("courier_fee").getAsDouble());
            dto.setPay_price(orderJson.get("pay_price").getAsLong());

            if (!orderJson.get("courier_id").isJsonNull()) {
                dto.setCourier_id(orderJson.get("courier_id").getAsLong());
            }

            dto.setStatus(orderJson.get("status").getAsString());
            dto.setCreated_at(orderJson.get("created_at").getAsString());
            dto.setUpdated_at(orderJson.get("updated_at").getAsString());
            dto.setIs_paid(orderJson.get("is_paid").getAsBoolean());

            transaction.setOrder(buildOrder(dto));

            transactions.add(transaction);
        }

        return transactions;
    }
}
