package dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto {
    Long id;
    String delivery_address;
    Long customer_id;
    Long vendor_id;
    Long coupon_id;
    List<ItemHelper> items;
    Long raw_price;
    Double tax_fee;
    Double additional_fee;
    Double courier_fee;
    Long pay_price;
    Long courier_id;
    String status;
    String created_at;
    String updated_at;

    @Getter
    @Setter
    public static class ItemHelper{
        Long item_id;
        Integer quantity;

        public ItemHelper(Long item_id, Integer quantity) {
            this.item_id = item_id;
            this.quantity = quantity;
        }
    }
}
