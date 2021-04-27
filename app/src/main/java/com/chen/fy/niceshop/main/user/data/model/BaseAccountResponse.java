package com.chen.fy.niceshop.main.user.data.model;

public class BaseAccountResponse {
    private int statusCode;
    private String msg;
    private String jwt;
    private UserInfo userInfo;

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

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "BaseAccountResponse{" +
                "statusCode=" + statusCode +
                ", msg='" + msg + '\'' +
                ", jwt='" + jwt + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }
}
