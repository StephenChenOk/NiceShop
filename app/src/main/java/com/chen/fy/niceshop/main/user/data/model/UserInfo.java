package com.chen.fy.niceshop.main.user.data.model;

public class UserInfo {
    private String nickname;
    private String img;

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

    @Override
    public String toString() {
        return "UserInfo{" +
                "nickname='" + nickname + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
