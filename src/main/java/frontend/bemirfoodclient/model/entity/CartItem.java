package frontend.bemirfoodclient.model.entity;

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