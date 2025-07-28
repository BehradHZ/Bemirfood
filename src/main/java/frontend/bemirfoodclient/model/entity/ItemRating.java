package frontend.bemirfoodclient.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRating {
    private Long id;
    private int rating;
    private String comment;
    private List<String> imageBase64;
    private LocalDateTime createdAt;
    private User user; // The user who submitted the rating
    private Long itemId;

    // Constructor for creating mock data or hydrating from backend
    public ItemRating(Long id, int rating, String comment, List<String> imageBase64, LocalDateTime createdAt, User user, Long itemId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.imageBase64 = imageBase64;
        this.createdAt = createdAt;
        this.user = user;
        this.itemId = itemId;
    }

    // Getters
    public Long getId() { return id; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public List<String> getImageBase64() { return imageBase64; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public User getUser() { return user; }
    public Long getItemId() { return itemId; }
}
