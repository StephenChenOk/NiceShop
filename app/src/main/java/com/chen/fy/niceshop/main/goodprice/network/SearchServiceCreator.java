package com.chen.fy.niceshop.main.goodprice.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchServiceCreator {

    private static final String BASE_URL = "https://search.smzdm.com/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <T> T create(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
