package com.chen.fy.niceshop.main.home.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.data.adapter.CabbageZoneAdapter;
import com.chen.fy.niceshop.main.home.data.model.BaseCommodityResponse;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.network.CommodityService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.main.home.detail.CommodityDetailActivity;
import com.chen.fy.niceshop.main.look.view.activity.LookGoodPriceActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 白菜专区
 */
public class CabbageZoneActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private CabbageZoneAdapter mAdapter;

    private List<Commodity> mList;

    public static void start(Context context) {
        Intent intent = new Intent(context, CabbageZoneActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cabbage_zone);
        bindView();
        initData();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.rv_cabbage);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        setListener();
    }

    private void setListener() {
        /// 返回
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        /// 推荐
        findViewById(R.id.tv_recommend_cabbage).setOnClickListener(v -> {
        });
        /// 最新
        findViewById(R.id.tv_newest_cabbage).setOnClickListener(v -> {
        });
        /// 12小时最新
        findViewById(R.id.tv_twelve_hour_hot_cabbage).setOnClickListener(v -> {
        });
        /// 白菜头条
        findViewById(R.id.tv_headline_cabbage).setOnClickListener(v -> {
        });
    }

    private void initData() {
        mAdapter = new CabbageZoneAdapter(this, R.layout.cabbage_zone_item);
        mAdapter.setListener(new CabbageZoneAdapter.IClickListener() {
            @Override
            public void gotoDetail(int id) {
                CommodityDetailActivity.start(CabbageZoneActivity.this, id);
            }

            @Override
            public void gotoLookAround(int position) {
                LookGoodPriceActivity.start(CabbageZoneActivity.this,-1);
            }
        });
        fillData();
    }

    private void fillData() {
        BasePopupView loading = new XPopup.Builder(this).asLoading("加载中").show();
        CommodityService service = ServiceCreator.create(CommodityService.class);
        service.getCheapest().enqueue(new Callback<BaseCommodityResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Response<BaseCommodityResponse> response) {
                BaseCommodityResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    dataSuccess(base);
                }
                loading.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Throwable t) {
                Log.i("GetAllShareInfo", "GetAllShareInfo Failure");
                loading.dismiss();
            }
        });
    }

    private void dataSuccess(BaseCommodityResponse base) {
        mList = base.getCommodity();
        mAdapter.setDataList(mList);
        recyclerView.setAdapter(mAdapter);
    }
}
