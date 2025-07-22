package frontend.bemirfoodclient.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;

    private List<CartItem> cartItems;

    private String deliveryAddress;

    private OrderStatus status;

    private Customer customer;

    private Delivery delivery;

    private Restaurant restaurant;

    private Coupon coupon;

    private OrderRating rating;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean paid;

    public Order(List<CartItem> cartItems, String deliveryAddress,
                 Customer customer, Restaurant restaurant,
                 LocalDateTime createdAt, LocalDateTime updatedAt, OrderStatus status) {
        this.cartItems = cartItems;
        this.deliveryAddress = deliveryAddress;
        this.customer = customer;
        this.restaurant = restaurant;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.coupon = null;
        this.delivery = null;
        this.status = status;
        this.paid = false;
        rating = null;
    }

    public Order(List<CartItem> cartItems, String deliveryAddress,
                 Customer customer, Restaurant restaurant,
                 LocalDateTime createdAt, LocalDateTime updatedAt, Coupon coupon, OrderStatus status) {
        this.cartItems = cartItems;
        this.deliveryAddress = deliveryAddress;
        this.customer = customer;
        this.restaurant = restaurant;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.coupon = coupon;
        this.delivery = null;
        this.status = status;
        this.paid = false;
        rating = null;
    }

    public Order() {}

    public Long getRawPrice() {
        return (long) cartItems.stream()
                .mapToDouble(CartItem::getCartItemPrice)
                .sum();
    }
    public Long getPayPrice() {
        long price;

        double taxFee = restaurant.getTaxFee() == null ? 0.0 : restaurant.getTaxFee();
        double additionalTaxFee = restaurant.getAdditionalFee() == null ? 0.0 : restaurant.getAdditionalFee();

        if(taxFee > 0 && taxFee <= 1 && additionalTaxFee > 0 && additionalTaxFee <= 1)
            price = (long)  (getRawPrice() * (restaurant.getTaxFee() + restaurant.getAdditionalFee()));
        else
            price = (long) (getRawPrice() + restaurant.getTaxFee() + restaurant.getAdditionalFee());

        if(coupon == null){
            return price;
        }else{
            double value = (double) coupon.getValue();
            if (coupon.getType().equals(CouponType.fixed)){
                return (long)(price - value);
            }else{
                value = value <= 1 ? value : value / 100;
                return (long)(price * (1 - value));
            }
        }
    }
}