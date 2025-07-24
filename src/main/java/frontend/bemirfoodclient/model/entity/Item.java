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

    private Double rating = 0.0;

    private Restaurant restaurant;

    public void subtractSupplyCount(int count){
        this.supply = this.supply - count;
    }


    public Item() {}

    public Item(String name, String photo, String description, Double price, Integer supply, List<String> keywords) {
        if(name != null) this.name = name;
        if(photo != null) this.photo = photo;
        if(description != null) this.description = description;
        if(price != null) this.price = price;
        if(supply != null) this.supply = supply;
        if(keywords != null) this.keywords = keywords;
        this.restaurant = null;
        this.rating = 0.0;
    }

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

    public Item(Long id, String name, String photo, String description, Double price, Integer supply, List<String> keywords) {
        this.id = id;
        if(name != null) this.name = name;
        if(photo != null) this.photo = photo;
        if(description != null) this.description = description;
        if(price != null) this.price = price;
        if(supply != null) this.supply = supply;
        if(keywords != null) this.keywords = keywords;
        this.restaurant = null;
        this.rating = 0.0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Integer getSupply() {
        return supply;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Double getRating() {
        return rating;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}