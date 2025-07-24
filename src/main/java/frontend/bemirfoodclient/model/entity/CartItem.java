package frontend.bemirfoodclient.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {

    private Long id;

    private Item item;

    private int quantity;

    public Long getCartItemPrice() { return (long) this.item.getPrice()*quantity; }

    public CartItem() {}

    public CartItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}