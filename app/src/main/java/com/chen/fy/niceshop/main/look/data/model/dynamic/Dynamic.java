package com.chen.fy.niceshop.main.look.data.model.dynamic;

public class Dynamic {
    private int id;
    private int user_id;
    private String user_nickname;
    private String user_img;
    private String content;
    private String imgs;
    private int give_num;

    public Dynamic getNewDynamic(String image) {
        return new Dynamic(id, user_id, user_nickname, user_img, content, image, give_num);
    }

    public Dynamic(int id, int user_id, String user_nickname, String user_img, String content, String imgs, int give_num) {
        this.id = id;
        this.user_id = user_id;
        this.user_nickname = user_nickname;
        this.user_img = user_img;
        this.content = content;
        this.imgs = imgs;
        this.give_num = give_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public int getGive_num() {
        return give_num;
    }

    public void setGive_num(int give_num) {
        this.give_num = give_num;
    }

}
