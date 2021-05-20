package com.chen.fy.niceshop.main.user.data.model;

public class UserInfo {
    private String nickname;
    private String img;
    private int comment_num;
    private int follow_num;
    private int give_num;
    private int fans_num;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImg() {
        return "http://39.106.225.65" + img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public int getGive_num() {
        return give_num;
    }

    public void setGive_num(int give_num) {
        this.give_num = give_num;
    }

    public int getFans_num() {
        return fans_num;
    }

    public void setFans_num(int fans_num) {
        this.fans_num = fans_num;
    }
}
