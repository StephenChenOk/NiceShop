package com.chen.fy.niceshop.main.home.comment;

import com.chen.fy.niceshop.main.home.data.model.Comment;

import java.util.List;

public class BaseAllCommentResponse {
    private int statusCode;
    private List<Comment> comments;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
