package com.chen.fy.niceshop.network;

import com.chen.fy.niceshop.main.goodprice.data.model.BaseCategoryResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 分类管理接口
 */
public interface CategoryService {

    @GET("Categories/categroyInfo")
    Call<BaseCategoryResponse> getCategoryInfo();
}
