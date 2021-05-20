package com.chen.fy.niceshop.network;

import com.chen.fy.niceshop.main.dynamic.BaseDynamicResponse;
import com.chen.fy.niceshop.main.dynamic.BaseGiveResponse;
import com.chen.fy.niceshop.main.dynamic.BasePersonResponse;
import com.chen.fy.niceshop.main.user.data.model.BaseUserInfoResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 账号管理接口
 */
public interface PersonService {

    /// 自己发布的动态
    @GET("person/dynamicList")
    Call<BaseDynamicResponse> dynamicList(@Header("Authorization") String token);

    /// 你点赞的信息
    @GET("person/giveList")
    Call<BaseGiveResponse> giveList(@Header("Authorization") String token);

    /// 你点赞的信息
    @GET("person/userInfo")
    Call<BaseUserInfoResponse> userInfo(@Header("Authorization") String token);

    /// 改变头像
    @POST("person/changeImg")
    @Multipart
    Call<BasePersonResponse> changeImg(@Header("Authorization") String token
            , @Part MultipartBody.Part img);

    /// 改变头像
    @POST("person/changeNickname")
    @Multipart
    Call<BasePersonResponse> changeNickname(@Header("Authorization") String token
            , @Part("nickname") String nickname);
}
