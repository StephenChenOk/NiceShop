package com.chen.fy.niceshop.main.look.data.model;

/**
 * 逛逛Item
 */
public class LookAroundItem {

    // id
    private int id;
    // 图片地址
    private int imagePath;
    // 内容
    private String content;
    // 头像地址
    private int headIconPath;
    // 名字
    private String name;
    // 是否点赞
    private boolean isLike;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHeadIconPath() {
        return headIconPath;
    }

    public void setHeadIconPath(int headIconPath) {
        this.headIconPath = headIconPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    @Override
    public String toString() {
        return "LookAroundItem{" +
                "id=" + id +
                ", imagePath=" + imagePath +
                ", content='" + content + '\'' +
                ", headIconPath=" + headIconPath +
                ", name='" + name + '\'' +
                ", isLike=" + isLike +
                '}';
    }
}
