package com.chen.fy.niceshop.main.home.comment;

import com.chen.fy.niceshop.main.home.data.model.Comment;

public class BaseCommentResponse {
    private int statusCode;
    private String msg;
    private Comment comment;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
