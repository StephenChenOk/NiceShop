package com.chen.fy.niceshop.network;

import com.chen.fy.niceshop.main.goodprice.data.model.BaseCategoryResponse;
import com.chen.fy.niceshop.main.guide.BaseGuideResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 分类管理接口
 */
public interface CategoryService {

    @GET("Categories/categroyInfo")
    Call<BaseCategoryResponse> getCategoryInfo();


    @POST("Cold/coldRec")
    @Multipart
    Call<BaseGuideResponse> coldRec(@Header("Authorization") String token
            , @Part("category") RequestBody category);
}
