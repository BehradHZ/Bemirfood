package frontend.bemirfoodclient.model.entity;

import java.time.LocalDateTime;
import java.util.List;


public class OrderRating {
    private Long id;

    private int rating;
    private String comment;
    private List<String> imageBase64;
    LocalDateTime createdAt;


    private Order order;
}