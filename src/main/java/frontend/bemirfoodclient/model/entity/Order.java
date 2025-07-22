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

    private double courierFee;

    private boolean paid;

    public Order(List<CartItem> cartItems, String deliveryAddress,
                 Customer customer, Restaurant restaurant,
                 LocalDateTime createdAt, LocalDateTime updatedAt, OrderStatus status, double courierFee) {
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
        this.courierFee = courierFee;
    }

    public Order(List<CartItem> cartItems, String deliveryAddress,
                 Customer customer, Restaurant restaurant,
                 LocalDateTime createdAt, LocalDateTime updatedAt, Coupon coupon, OrderStatus status, double courierFee) {
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
        this.courierFee = courierFee;
    }

    public Order() {}

    public Long getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public OrderRating getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isPaid() {
        return paid;
    }

    public double getCourierFee() {
        return courierFee;
    }

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

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}