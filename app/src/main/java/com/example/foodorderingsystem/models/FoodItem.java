//package com.example.foodorderingsystem.models;
//
//public class FoodItem {
//    private int foodId;
//    private String name;
//    private String description;
//    private double price;
//
//private String image;
//    private String category;
//
//    public FoodItem(int foodId, String name, String description, double price, String image, String category) {
//        this.foodId = foodId;
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.image = image;
//        this.category = category;
//    }
//
//    // Getters and Setters
//    public int getFoodId() {
//        return foodId;
//    }
//
//    public void setFoodId(int foodId) {
//        this.foodId = foodId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//}
package com.example.foodorderingsystem.models;

import android.content.Context;

public class FoodItem {
    private int foodId;
    private String name;
    private String description;
    private double price;
    private String imageName;  // 改为更明确的imageName
    private String category;

    public FoodItem(int foodId, String name, String description, double price, String imageName, String category) {
        this.foodId = foodId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageName = imageName; //
        this.category = category;
    }

    // 新增：获取图片资源ID的便捷方法
    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(
                imageName,
                "drawable",
                context.getPackageName()
        );
    }

    // Getters and Setters
    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageName() {  // 改名更语义化
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // 新增：toString方法便于调试
    @Override
    public String toString() {
        return "FoodItem{" +
                "foodId=" + foodId +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                ", price=" + price +
                '}';
    }


}