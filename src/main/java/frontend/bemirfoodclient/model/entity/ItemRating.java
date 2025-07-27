package frontend.bemirfoodclient.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRating {

    private Long id;

    private Item item;

    private User user;

    private int rating;

    private String comment;

    private List<String> imageBase64;

    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(List<String> imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}