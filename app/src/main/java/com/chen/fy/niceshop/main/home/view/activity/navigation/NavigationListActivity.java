package com.chen.fy.niceshop.main.home.view.activity.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.data.adapter.CommodityAdapter;
import com.chen.fy.niceshop.main.home.data.model.BaseCommodityResponse;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.main.home.detail.CommodityDetailActivity;
import com.chen.fy.niceshop.network.CommodityService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationListActivity extends AppCompatActivity {

    private TextView tvTitle;
    private RecyclerView recyclerView;

    private CommodityAdapter mAdapter;
    private List<Commodity> mList = new ArrayList<>();

    public static void start(Context context, String title) {
        Intent intent = new Intent(context, NavigationListActivity.class);
        intent.putExtra(RUtil.toString(R.string.title), title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mavigation_list);

        bindView();
        initData();
        getData();
    }

    private void bindView() {
        tvTitle = findViewById(R.id.tv_title);

        recyclerView = findViewById(R.id.rv_navigation_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        setClickListener();
    }

    private void setClickListener() {
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void initData() {
        mAdapter = new CommodityAdapter(this, R.layout.commodity_item);
        mAdapter.setListener(id -> CommodityDetailActivity.start(this, id));
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra(RUtil.toString(R.string.title));
            tvTitle.setText(title);
            fillData(title);
        }
    }

    private void fillData(String title){
        BasePopupView loading = new XPopup.Builder(this).asLoading("搜索中").show();
        // server
        CommodityService service = ServiceCreator.create(CommodityService.class);
        service.categoryCommodity(title).enqueue(new Callback<BaseCommodityResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Response<BaseCommodityResponse> response) {
                BaseCommodityResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    mList = base.getCommodity();
                    mAdapter.setData(mList);
                    recyclerView.setAdapter(mAdapter);
                }
                loading.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
                loading.dismiss();
            }
        });
    }
}
