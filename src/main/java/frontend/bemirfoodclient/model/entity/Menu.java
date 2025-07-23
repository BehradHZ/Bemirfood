package frontend.bemirfoodclient.model.entity;

import java.util.List;

public class Menu {
    private Long id;

    private String title;

    private Restaurant restaurant;

    private List<Item> items;

    public Menu() {}

    public Menu(String title, Restaurant restaurant) {
        this.title = title;
        this.restaurant = restaurant;
    }

    public Menu(String title, Restaurant restaurant, List<Item> items) {
        this.title = title;
        this.restaurant = restaurant;
        this.items = items;
    }

    public void addItem(Item item) {
        if (items == null) {
            items = new java.util.ArrayList<>();
        }
        items.add(item);
    }

    public void removeItem(Item item) {
        if (item != null) {
            items.remove(item);
        }
    }

    public String getTitle() {
        return title;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<Item> getItems() {
        return items;
    }
}