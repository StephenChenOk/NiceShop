package com.chen.fy.niceshop.network;

import com.chen.fy.niceshop.main.user.collection.BaseCollectionResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GiveService {

    /// 点赞
    @Multipart
    @POST("gives/give")
    Call<BaseCollectionResponse> doGive(@Header("Authorization") String token
            , @Part("id") RequestBody id);

    /// 取消点赞
    @Multipart
    @POST("gives/cancelGive")
    Call<BaseCollectionResponse> cancelGive(@Header("Authorization") String token
            , @Part("id") RequestBody id);
}
