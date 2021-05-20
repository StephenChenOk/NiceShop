package com.chen.fy.niceshop.main.home.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.data.adapter.CommodityAdapter;
import com.chen.fy.niceshop.main.home.data.model.BaseCommodityResponse;
import com.chen.fy.niceshop.main.home.data.model.BaseRecommendResponse;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.main.home.detail.CommodityDetailActivity;
import com.chen.fy.niceshop.network.CommodityService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.UserSP;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 今日推荐
 */
public class HotTodayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private CommodityAdapter mAdapter;

    private List<Commodity> mList;

    public static void start(Context context) {
        Intent intent = new Intent(context, HotTodayActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hot_today);
        bindView();
        initData();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.rv_hot_today);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        setListener();
    }

    private void setListener() {
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void initData() {
        mAdapter = new CommodityAdapter(this, R.layout.commodity_item);
        mAdapter.setListener(id -> CommodityDetailActivity.start(this, id));
        fillData();
    }

    private void fillData() {
        mList = new ArrayList<>();

        String token = UserSP.getToken();
        if(TextUtils.isEmpty(token)){
            doPostNotSign();
        }else{
            doPostSigned(token);
        }

    }

    /// 已登录
    private void doPostSigned(String token){
        CommodityService service = ServiceCreator.create(CommodityService.class);
        service.recommendItems(token).enqueue(new Callback<BaseRecommendResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseRecommendResponse> call
                    , @NonNull Response<BaseRecommendResponse> response) {
                BaseRecommendResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    mList = base.getRecommendList();
                    mAdapter.setData(mList);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseRecommendResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /// 未登录
    private void doPostNotSign(){
//        CommodityService service = ServiceCreator.create(CommodityService.class);
//        service.recommendItems().enqueue(new Callback<BaseCommodityResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<BaseCommodityResponse> call
//                    , @NonNull Response<BaseCommodityResponse> response) {
//                BaseCommodityResponse base = response.body();
//                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
//                    mList = base.getCommodity();
//                    mAdapter.setData(mList);
//                    recyclerView.setAdapter(mAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<BaseCommodityResponse> call
//                    , @NonNull Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }

}
