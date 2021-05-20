package com.chen.fy.niceshop.main.home.data.model;

import java.util.List;

/**
 * 所有商品请求base response
 */
public class BaseRecommendResponse {

    private int statusCode;
    private List<Commodity> recommendList;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Commodity> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<Commodity> recommendList) {
        this.recommendList = recommendList;
    }
}
