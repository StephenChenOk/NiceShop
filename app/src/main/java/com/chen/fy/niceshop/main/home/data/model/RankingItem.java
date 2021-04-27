package com.chen.fy.niceshop.main.home.data.model;

public class RankingItem {

    // id
    private int id;
    // top
    private int top;
    // 名字
    private String name;
    // 图片地址
    private int imgPath;
    // 价格
    private int price;
    // 所属类别、商店
    private String type;
    // 评论数
    private int commentNum;
    // 热度
    private int heatNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
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

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getHeatNum() {
        return heatNum;
    }

    public void setHeatNum(int heatNum) {
        this.heatNum = heatNum;
    }

    @Override
    public String toString() {
        return "RankingItem{" +
                "id=" + id +
                ", top=" + top +
                ", name='" + name + '\'' +
                ", imgPath=" + imgPath +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", commentNum=" + commentNum +
                ", heatNum=" + heatNum +
                '}';
    }
}
