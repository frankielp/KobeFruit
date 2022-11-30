package com.example.midterm.model;

import java.io.Serializable;

public class AllProduct implements Serializable {
    private String CategoryId, Description, Discount, Image, Name, Popular, key;
    public int numInCart;
    double Price;
    boolean isFav;

    public AllProduct() {
        numInCart = 0;
    }

    public AllProduct(String categoryId, String description, String discount, String image, String name, String popular, double price) {
        CategoryId = categoryId;
        Description = description;
        Discount = discount;
        Image = image;
        Name = name;
        Popular = popular;
        Price = price;
        numInCart = 0;
        key = "0";
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPopular() {
        return Popular;
    }

    public void setPopular(String popular) {
        Popular = popular;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getNumInCart() {
        return numInCart;
    }

    public void setNumInCart(int numInCart) {
        this.numInCart = numInCart;
    }
}

/*public class AllProduct implements Serializable {
    private String name;
    private String pic;
    private Double fee;
    private int numInCart;
    private String description;

    public AllProduct(String name, String pic, Double fee, String description) {
        this.name = name;
        this.pic = pic;
        this.fee = fee;
        this.description = description;
        numInCart = 0;
    }

    public AllProduct(String name, String pic, Double fee, int numInCart, String description) {
        this.name = name;
        this.pic = pic;
        this.fee = fee;
        this.numInCart = numInCart;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public int getNumInCart() {
        return numInCart;
    }

    public void setNumInCart(int numInCart) {
        this.numInCart = numInCart;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}*/
