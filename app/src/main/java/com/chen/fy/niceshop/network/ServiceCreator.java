package com.chen.fy.niceshop.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit Creator
 * 根据传入的接口，获取到接口实例对象
 * 通过对象就可以直接调用相应的接口方法
 */
public class ServiceCreator {

    public static final String ROOT_URL = "http://39.106.225.65";

    private static final String BASE_URL = "http://39.106.225.65/index/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <T> T create(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
