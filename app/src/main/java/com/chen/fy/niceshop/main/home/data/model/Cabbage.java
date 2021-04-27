package com.chen.fy.niceshop.main.home.data.model;

public class Cabbage {

    // id
    private int id;
    // 名字
    private String name;
    // 图片地址
    private int imgPath;
    // 价格
    private int price;
    // 所属类别、商店
    private String type;

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

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

    public String getPrice() {
        return price + "元";
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                '}';
    }
}
