package com.chen.fy.niceshop.network;

import com.chen.fy.niceshop.main.look.data.model.dynamic.BaseDynamicDetailResponse;
import com.chen.fy.niceshop.main.look.data.model.dynamic.BaseDynamicResponse;
import com.chen.fy.niceshop.main.look.publish.data.BasePublishResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface DynamicService {

    ///  上传
    @POST("publish/publishDynamic")
    @Multipart
    Call<BasePublishResponse> publishOne(
            @Header("Authorization") String kye
            , @Part("content") RequestBody content
            , @Part("type") RequestBody type
            , @Part MultipartBody.Part img);

    @POST("publish/publishDynamic")
    @Multipart
    Call<BasePublishResponse> publishTwo(
            @Header("Authorization") String kye
            , @Part("content") RequestBody content
            , @Part("type") RequestBody type
            , @Part MultipartBody.Part img1
            , @Part MultipartBody.Part img2);

    @POST("publish/publishDynamic")
    @Multipart
    Call<BasePublishResponse> publishThree(
            @Header("Authorization") String kye
            , @Part("content") RequestBody content
            , @Part("type") RequestBody type
            , @Part MultipartBody.Part img1
            , @Part MultipartBody.Part img2
            , @Part MultipartBody.Part img3);


    ///  获取动态
    @GET("Dynamics/allDynamic")
    Call<BaseDynamicResponse> getDynamic(@Query("type") String type);

    ///  动态详情
    @GET("Dynamics/dynamicDetail")
    Call<BaseDynamicDetailResponse> dynamicDetail(@Header("Authorization") String kye, @Query("id") int id);

    ///  用户相关动态
    @GET("Dynamics/related")
    Call<BaseDynamicResponse> related(@Query("id") int id);


}
