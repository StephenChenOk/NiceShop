package com.chen.fy.niceshop.main.goodprice.network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ReptileSearchService {

    ///  抓取什么值得买首页精选商品
    @GET("/")
    Call<ResponseBody> reptileSearchData(@QueryMap Map<String,Object> map);
}
