package frontend.bemirfoodclient.model.entity;

import java.util.List;

public class Delivery extends User {
    private Boolean available;
    private Double latitude;
    private Double longitude;
    private Double averageRating;
    private Integer totalDeliveries;

    public  Delivery() {
        super();
    }

    public Delivery(String fullName, String mobile, String role, String email,
                    String photo, String address, Bank_info bankInfo, String password) {
        super(fullName, mobile, role, email, photo, address, bankInfo, password);
        this.totalDeliveries = 0;
        this.available = false;
        this.averageRating = 0.0;
        //this.isApproved = false;
    }

    public Delivery(String mobile){
        super(mobile);
        this.totalDeliveries = 0;
        this.available = false;
        this.averageRating = 0.0;
        //this.isApproved = false;
    }

    private List<Order> orders;

    public void addOrder(Order order) {
        if (orders == null) {
            orders = new java.util.ArrayList<>();
        }
        orders.add(order);
    }

    public Boolean getAvailable() {
        return available;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public Integer getTotalDeliveries() {
        return totalDeliveries;
    }

    public List<Order> getOrders() {
        return orders;
    }
}