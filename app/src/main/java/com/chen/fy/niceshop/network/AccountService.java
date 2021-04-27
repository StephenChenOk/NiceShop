package com.chen.fy.niceshop.network;


import com.chen.fy.niceshop.main.user.data.model.BaseAccountResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * 账号管理接口
 */
public interface AccountService {

    @Multipart
    @POST("account/login")
    Call<BaseAccountResponse> login(@Part("username") RequestBody username
            , @Part("password") RequestBody password);

    @Multipart
    @POST("account/register")
    Call<BaseAccountResponse> register(@Part("username") RequestBody username
            , @Part("password") RequestBody password);

    @PUT("user")
    Call<ResponseBody> update(@Body RequestBody json);
}
