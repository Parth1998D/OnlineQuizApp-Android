package com.example.quizapp.Model;

public class Category
{
    private String Name;
    private String Image;
    private String bg;
    public Category() {
    }

    public Category(String name, String image, String bg) {
        Name = name;
        Image = image;
        this.bg = bg;
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

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }
}

