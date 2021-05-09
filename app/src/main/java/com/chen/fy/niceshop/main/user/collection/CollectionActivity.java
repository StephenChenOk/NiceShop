package com.chen.fy.niceshop.main.user.collection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.detail.CommodityDetailActivity;
import com.chen.fy.niceshop.main.home.data.adapter.CommodityAdapter;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.network.CollectionService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.UserSP;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private CommodityAdapter mAdapter;

    private List<Commodity> mList = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, CollectionActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection);

        bindView();
        initData();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.rv_collection);
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
        BasePopupView loading = new XPopup.Builder(this).asLoading("加载中").show();
        // token
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        CollectionService service = ServiceCreator.create(CollectionService.class);
        service.getCollectionList(token).enqueue(new Callback<BaseCollectionListResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCollectionListResponse> call
                    , @NonNull Response<BaseCollectionListResponse> response) {
                loading.dismiss();
                BaseCollectionListResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    mList = base.getList();
                    mAdapter.setData(mList);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseCollectionListResponse> call
                    , @NonNull Throwable t) {
                loading.dismiss();
                t.printStackTrace();
            }
        });
    }

}
