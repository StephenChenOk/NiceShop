package com.chen.fy.niceshop.main.goodprice.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReptileService {

    ///  抓取什么值得买首页精选商品
    @GET("jingxuan/{page}")
    Call<ResponseBody> getReptileData(@Path("page") String page);
}
