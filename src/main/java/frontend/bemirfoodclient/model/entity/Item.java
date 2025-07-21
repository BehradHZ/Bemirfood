package frontend.bemirfoodclient.model.entity;

import java.util.List;

public class Item {

    private Long id;
    private String name;
    private String photo;
    private String description;
    private double price;
    private Integer supply;

    private List<String> keywords;

    private Double rating;

    private Restaurant restaurant;

    public void subtractSupplyCount(int count){
        this.supply = this.supply - count;
    }


    public Item() {}

    public Item(String name, String photo, String description, Double price, Integer supply, List<String> keywords, Restaurant restaurant, Double rating) {
        if(name != null) this.name = name;
        if(photo != null) this.photo = photo;
        if(description != null) this.description = description;
        if(price != null) this.price = price;
        if(supply != null) this.supply = supply;
        if(keywords != null) this.keywords = keywords;
        if(restaurant != null) this.restaurant = restaurant;
        if(rating != null) this.rating = rating;
    }

}