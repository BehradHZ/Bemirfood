package frontend.bemirfoodclient.model.entity;

import java.time.LocalDateTime;
import java.util.List;


public class OrderRating {
    private Long id;

    private int rating;
    private String comment;
    private List<String> imageBase64;
    private LocalDateTime createdAt;

    private Order order;

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public List<String> getImageBase64() {
        return imageBase64;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Order getOrder() {
        return order;
    }
}