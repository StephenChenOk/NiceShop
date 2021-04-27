package com.chen.fy.niceshop.network;

import com.chen.fy.niceshop.main.home.data.model.BaseCommodityResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CollectionService {

    ///  收藏列表
    @GET("collections/collectionList")
    Call<BaseCommodityResponse> getCollectionList();
}
