package com.example.midterm.model;

import java.io.Serializable;

public class Category {
    String Name, Image, Id;

    public Category() {
    }

    public Category(String name, String image, String id) {
        Name = name;
        Image = image;
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
