package com.chen.fy.niceshop.main.goodprice.data;

public class ClassifyItem {

    private int id;
    private int imagePath;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImagePath() {
        return imagePath;
    }

    public void setImagePath(int imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClassifyItem{" +
                "id=" + id +
                ", imgID=" + imagePath +
                ", name='" + name + '\'' +
                '}';
    }
}
