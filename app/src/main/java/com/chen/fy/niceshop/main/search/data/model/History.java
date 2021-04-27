package com.chen.fy.niceshop.main.search.data.model;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class History extends BmobObject {

    private String userID;
    private List<String> histories;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<String> getHistories() {
        return histories;
    }

    public void setHistories(List<String> histories) {
        this.histories = histories;
    }

    @Override
    public String toString() {
        return "History{" +
                "userID=" + userID +
                ", histories=" + histories +
                '}';
    }
}
