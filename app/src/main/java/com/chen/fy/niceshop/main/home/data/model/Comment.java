package com.chen.fy.niceshop.main.home.data.model;

public class Comment {

    // id
    private int id;
    // 名字
    private String user_nickname;
    // 头像
    private String user_img;
    // 内容
    private String content;
    // 时间
    private String date;
    // 点赞
    private boolean isLike;

    private int give_num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getGive_num() {
        return give_num;
    }

    public void setGive_num(int give_num) {
        this.give_num = give_num;
    }
}
