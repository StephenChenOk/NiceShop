package com.chen.fy.niceshop.network;

import com.chen.fy.niceshop.main.user.collection.BaseCollectionResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ScoreService {

    ///  评分
    @Multipart
    @POST("score/score")
    Call<BaseCollectionResponse> doScore(@Header("Authorization") String token
            , @Part("id") RequestBody id, @Part("score") RequestBody score);

}
