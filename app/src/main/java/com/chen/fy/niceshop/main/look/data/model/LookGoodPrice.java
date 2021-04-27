package com.chen.fy.niceshop.main.look.data.model;

/**
 * 逛逛Item
 */
public class LookGoodPrice {

    // id
    private int id;
    // 头像地址
    private int headIconPath;
    // 名字
    private String name;
    // 图片地址
    private int imagePath;
    // 内容
    private String content;
    // 点赞人数
    private int likeNum;
    // 收藏人数
    private int starNum;
    // 评论人数
    private int commentNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getStarNum() {
        return starNum;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    @Override
    public String toString() {
        return "LookGoodPrice{" +
                "id=" + id +
                ", headIconPath=" + headIconPath +
                ", name='" + name + '\'' +
                ", imagePath=" + imagePath +
                ", content='" + content + '\'' +
                ", likeNum=" + likeNum +
                ", starNum=" + starNum +
                ", commentNum=" + commentNum +
                '}';
    }
}
