package com.chen.fy.niceshop.network;

import com.chen.fy.niceshop.main.home.data.model.BaseCommodityResponse;
import com.chen.fy.niceshop.main.home.data.model.detail.BaseDetailResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 商品接口
 */
public interface CommodityService {

    ///  全部商品数据
    @GET("Commodities/recommendCommodity")
    Call<BaseCommodityResponse> getAllCommodity();

    ///  白菜专区数据
    @GET("Commodities/cheapest")
    Call<BaseCommodityResponse> getCheapest();

    ///  今日爆款数据
    @GET("Commodities/hotCommodity")
    Call<BaseCommodityResponse> getHotCommodity();

    ///  超值榜单数据
    @GET("Commodities/rank")
    Call<BaseCommodityResponse> getRankCommodity();

    ///  商品详情
    @GET("Commodities/commodityDetail")
    Call<BaseDetailResponse> getCommodityDetail(@Query("id") int id);

    ///  搜索
    @GET("Commodities/searchCommodity")
    Call<BaseCommodityResponse> search(@Query("keyword") String keyword);

    ///  好价：商城
    @GET("Commodities/allCommodity")
    Call<BaseCommodityResponse> queryPlatform(@Query("platform") String keyword);

    ///  好价：商城
    @GET("Commodities/allCommodity")
    Call<BaseCommodityResponse> queryCategory(@Query("category") String keyword);

    ///  实时爬虫抓取数据
    @GET("Commodities/allCommodity")
    Call<BaseCommodityResponse> dynamicReptile(@Query("category") String keyword);
}
