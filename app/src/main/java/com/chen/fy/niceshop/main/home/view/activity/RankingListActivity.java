package com.chen.fy.niceshop.main.home.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.data.adapter.RankingListAdapter;
import com.chen.fy.niceshop.main.home.data.model.BaseCommodityResponse;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.main.home.data.model.RankingItem;
import com.chen.fy.niceshop.network.CommodityService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.main.home.detail.CommodityDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 超值榜单
 */
public class RankingListActivity extends AppCompatActivity {

    private RecyclerView rvRanking;

    private RankingListAdapter mAdapter;

    private List<Commodity> mList;

    public static void start(Context context) {
        Intent intent = new Intent(context, RankingListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShowUtils.changeStatusBarTextImgColor(this, false);
        setContentView(R.layout.ranking_list);
        bindView();
        initData();
    }

    private void bindView() {
        rvRanking = findViewById(R.id.rv_ranking_list);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rvRanking.setLayoutManager(manager);

        setListener();
    }

    private void setListener() {
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void initData() {
        mAdapter = new RankingListAdapter(this, R.layout.ranking_list_item);
        mAdapter.setListener(id -> {
            CommodityDetailActivity.start(this, id);
        });
        fillListData();
    }

    private void fillListData() {
        mList = new ArrayList<>();

        CommodityService service = ServiceCreator.create(CommodityService.class);
        service.getRankCommodity().enqueue(new Callback<BaseCommodityResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Response<BaseCommodityResponse> response) {
                BaseCommodityResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    mList = base.getCommodity();
                    mAdapter.setList(mList);
                    rvRanking.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
