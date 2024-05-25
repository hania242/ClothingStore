package com.example.clothingstore;

import java.util.List;

public class Product {
    private String name, color, size, description,imageUrl;
    private double price;
    private int quantity;
    private int userQuantity;

    private int id;
    private float rating;
    private boolean isFavorite, isAddedToCart;

    private List<String> colors;
    private List<String> sizes;

    private String category;

    public Product(){};

    public Product(int id,String name, double price, String imageUrl, float rating, boolean isFavorite, String category,int userQuantity) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.isFavorite = isFavorite;
        this.category = category;
        this.quantity = quantity;
        this.userQuantity = userQuantity;
        this.description = "";
        this.id = id;
    }

    public Product(int id,String name, double price, String imageUrl, float rating,  String category, String size, String color, int quantity,  String description,int userQuantity) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.isFavorite = false;
        this.category = category;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.userQuantity = userQuantity;
        this.isAddedToCart = false;
        this.description = description != null ? description : "";
        this.id = id;
    }

    public Product(int id,String name, double price, String color, String size) {
        this.name = name;
        this.price = price;
        this.color = color;
        this.size = size;
        this.isAddedToCart = false;
        this.id=id;
    }

    public String getName() { return name; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public String getCategory() { return category; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public void setCategory(String category) { this.category = category; }

    public String getSelectedSize() { return size; }
    public void setSelectedSize(String size) { this.size = size; }

    public String getSelectedColor() { return color; }
    public void setSelectedColor(String color) { this.color = color; }

    public boolean isAddedToCart() { return isAddedToCart; }
    public void setAddedToCart(boolean addedToCart) { isAddedToCart = addedToCart; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getUserQuantity() {
        return userQuantity;
    }

    public void setUserQuantity(int userQuantity) {
        this.userQuantity = userQuantity;
    }

    public List<String> getColors() { return colors; }
    public void setColors(List<String> colors) { this.colors = colors; }

    public List<String> getSizes() { return sizes; }
    public void setSizes(List<String> sizes) { this.sizes = sizes; }
}
