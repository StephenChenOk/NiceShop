package com.chen.fy.niceshop.network;

import com.chen.fy.niceshop.main.home.comment.BaseAllCommentResponse;
import com.chen.fy.niceshop.main.home.comment.BaseCommentResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * 评论模块
 */
public interface CommentService {

    /// 评论
    @POST("comments/publicComment")
    @Multipart
    Call<BaseCommentResponse> publishComment(@Header("Authorization") String kye
            , @PartMap Map<String, RequestBody> map);

    /// 获取全部评论
    @GET("commodities/detailComment")
    Call<BaseAllCommentResponse> getAllComment(@Query("id") int id);
}
