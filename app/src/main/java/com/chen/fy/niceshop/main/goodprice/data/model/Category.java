package com.chen.fy.niceshop.main.goodprice.data.model;

public class Category {
    private int id;
    private String name;
    private String img;
    private int gmt_create;
    private int gmt_modify;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(int gmt_create) {
        this.gmt_create = gmt_create;
    }

    public int getGmt_modify() {
        return gmt_modify;
    }

    public void setGmt_modify(int gmt_modify) {
        this.gmt_modify = gmt_modify;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", gmt_create=" + gmt_create +
                ", gmt_modify=" + gmt_modify +
                '}';
    }
}
