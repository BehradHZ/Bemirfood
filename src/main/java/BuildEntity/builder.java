package BuildEntity;

import dto.OrderDto;
import frontend.bemirfoodclient.model.entity.*;

import java.util.ArrayList;
import java.util.List;

import static BuildEntity.EntityRequest.*;
import static HttpClientHandler.LocalDateTimeAdapter.StringToTime;

public class builder {
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
        }else{
            order.setDelivery(null);
        }

        order.setRestaurant(getRestaurant(orderDto.getId()));
        List<CartItem> cartItems = new ArrayList<>();
        for(OrderDto.ItemHelper ih : orderDto.getItems()){
            Item item = new Item();
            int quantity = 0;
            item = getItem(ih.getItem_id());
            quantity = ih.getQuantity();
            cartItems.add(new CartItem(item, quantity));
        }
        order.setCartItems(cartItems);
        order.setDeliveryAddress(orderDto.getDelivery_address());
        order.setCreatedAt(StringToTime(orderDto.getCreated_at()));
        order.setUpdatedAt(StringToTime(orderDto.getUpdated_at()));
        order.setStatus(order.getStatus() != null ? order.getStatus() : OrderStatus.submitted);
        order.setCourierFee(orderDto.getCourier_fee());
        return order;
    }
}
