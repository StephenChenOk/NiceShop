package com.chen.fy.niceshop.main.home.data.model;

/**
 * 商品
 */
public class Commodity {

    private int id;
    private String pic_url;
    private String title;
    private int view_price;
    private String platform;
    private int comment_num;
    private int hot;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getView_price() {
        return view_price;
    }

    public void setView_price(int view_price) {
        this.view_price = view_price;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "id=" + id +
                ", pic_url='" + pic_url + '\'' +
                ", title='" + title + '\'' +
                ", view_price=" + view_price +
                ", platform='" + platform + '\'' +
                ", comment_num=" + comment_num +
                ", hot=" + hot +
                '}';
    }
}
