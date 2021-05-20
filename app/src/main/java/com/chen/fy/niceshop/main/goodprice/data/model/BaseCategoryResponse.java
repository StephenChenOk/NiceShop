package com.chen.fy.niceshop.main.goodprice.data.model;

import java.util.List;

public class BaseCategoryResponse {
    private int statusCode;
    private List<Category> category;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "BaseGuideResponse{" +
                "statusCode=" + statusCode +
                ", category=" + category +
                '}';
    }
}
