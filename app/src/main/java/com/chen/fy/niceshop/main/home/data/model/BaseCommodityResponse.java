package com.chen.fy.niceshop.main.home.data.model;

import java.util.List;

/**
 * 所有商品请求base response
 */
public class BaseCommodityResponse {

    private int statusCode;
    private List<Commodity> commodity;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Commodity> getCommodity() {
        return commodity;
    }

    public void setCommodity(List<Commodity> commodity) {
        this.commodity = commodity;
    }

    @Override
    public String toString() {
        return "BaseCommodityResponse{" +
                "statusCode=" + statusCode +
                ", commodity=" + commodity +
                '}';
    }
}
