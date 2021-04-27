package com.chen.fy.niceshop.network;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

/**
 * 用户信息管理接口
 */
public interface UserService {

    @PUT("user")
    Call<ResponseBody> update(@Body RequestBody json);
}
