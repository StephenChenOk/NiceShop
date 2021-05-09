package com.chen.fy.niceshop.network;

import com.chen.fy.niceshop.main.home.data.model.BaseCommodityResponse;
import com.chen.fy.niceshop.main.look.publish.data.BasePublishResponse;
import com.chen.fy.niceshop.main.user.collection.BaseCollectionListResponse;
import com.chen.fy.niceshop.main.user.collection.BaseCollectionResponse;
import com.chen.fy.niceshop.main.user.data.model.BaseAccountResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CollectionService {

    ///  收藏列表
    @GET("collections/collectionList")
    Call<BaseCollectionListResponse> getCollectionList(@Header("Authorization") String token);

    ///  收藏
    @Multipart
    @POST("collections/collection")
    Call<BaseCollectionResponse> doCollection(@Header("Authorization") String token
            , @Part("id") RequestBody id);

    ///  取消收藏
    @Multipart
    @POST("collections/cancel")
    Call<BaseCollectionResponse> cancelCollection(@Header("Authorization") String token
            , @Part("id") RequestBody id);
}
