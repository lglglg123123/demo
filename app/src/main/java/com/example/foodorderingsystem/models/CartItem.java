package com.example.foodorderingsystem.models;

public class CartItem {
    private int cartId;
    private int foodId;
    private int quantity;
    private String foodName;
    private double price;
    private String image;

    public CartItem(int cartId, int foodId, int quantity, String foodName, double price, String image) {
        this.cartId = cartId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.foodName = foodName;
        this.price = price;
        this.image = image;
    }

    // Getters and Setters
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}