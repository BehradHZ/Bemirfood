package frontend.bemirfoodclient.model.entity;

import java.util.List;


public class Restaurant {

    public Restaurant() {}

    public Restaurant(String name, String address, String phone, String logo) {
        this.name = name;
        this.seller = null;
        this.address = address;
        this.phone = phone;
        this.logo = logo;
        this.taxFee = 0.0;
        this.additionalFee = 0.0;
    }

    public Restaurant(String name, Seller seller, String address, String phone, String logo,
                      Double taxFee, Double additionalFee) {
        this.name = name;
        this.seller = seller;
        this.address = address;
        this.phone = phone;
        this.logo = logo;
        this.taxFee = taxFee;
        this.additionalFee = additionalFee;
    }

    public Restaurant(Long id, String name, Seller seller, String address, String phone, String logo,
                      Double taxFee, Double additionalFee) {
        this.id = id;
        this.name = name;
        this.seller = seller;
        this.address = address;
        this.phone = phone;
        this.logo = logo;
        this.taxFee = taxFee;
        this.additionalFee = additionalFee;
    }

    public Restaurant(Long id, String name, String address, String phone, String logo,
                      Double taxFee, Double additionalFee) {
        this.id =  id;
        this.name = name;
        this.seller = null;
        this.address = address;
        this.phone = phone;
        this.logo = logo;
        this.taxFee = taxFee;
        this.additionalFee = additionalFee;
    }

    private Long id;

    private String name;

    private Seller seller;

    private String address;
    private String phone;
    private String logo;
    private String description;
    private String workingHours;
    private int totalOrders;
    private Double averageRating;
    private Double taxFee = 0.0;
    private Double additionalFee = 0.0;


    private List<Item> items;

    private List<Menu> menus ;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Double getTaxFee() {
        return taxFee;
    }

    public void setTaxFee(Double taxFee) {
        this.taxFee = taxFee;
    }

    public Double getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(Double additionalFee) {
        this.additionalFee = additionalFee;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public void addItem(Item item) {
        if (items == null) {
            items = new java.util.ArrayList<>();
        }
        items.add(item);
    }

    public void removeItem(Item item) {
        if (items != null) {
            items.remove(item);
        }
    }

    public void addMenu(Menu menu) {
        if (menus == null) {
            menus = new java.util.ArrayList<>();
        }
        menus.add(menu);
    }

    public void removeMenu(Menu menu) {
        if (menus != null) {
            menus.remove(menu);
        }
    }
}
