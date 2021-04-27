package com.chen.fy.niceshop.main.look.data.model.dynamic;

import com.chen.fy.niceshop.main.home.data.model.Comment;

import java.util.List;

public class BaseDynamicDetailResponse {
    private int statusCode;
    private Dynamic dynamic;
    private List<Comment> comments;
    private int count;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Dynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
