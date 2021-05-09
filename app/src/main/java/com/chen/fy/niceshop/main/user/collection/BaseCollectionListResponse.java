package com.chen.fy.niceshop.main.user.collection;

import com.chen.fy.niceshop.main.home.data.model.Commodity;

import java.util.List;

public class BaseCollectionListResponse {
    private int statusCode;
    private List<Commodity> list;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Commodity> getList() {
        return list;
    }

    public void setList(List<Commodity> list) {
        this.list = list;
    }
}
