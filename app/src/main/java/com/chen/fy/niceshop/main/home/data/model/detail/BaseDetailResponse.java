package com.chen.fy.niceshop.main.home.data.model.detail;

import com.chen.fy.niceshop.main.home.data.model.Comment;
import com.chen.fy.niceshop.main.home.data.model.Commodity;

import java.util.List;

public class BaseDetailResponse {

    // 状态码
    private int statusCode;
    // 商品详情
    private CommodityDetail commodity;
    // 评论
    private List<Comment> comments;
    // 总评论数
    private int count;
    // 推荐商品
    private List<Commodity> recommendCommodity;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public CommodityDetail getCommodity() {
        return commodity;
    }

    public void setCommodity(CommodityDetail commodity) {
        this.commodity = commodity;
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

    public List<Commodity> getRecommendCommodity() {
        return recommendCommodity;
    }

    public void setRecommendCommodity(List<Commodity> recommendCommodity) {
        this.recommendCommodity = recommendCommodity;
    }
}
