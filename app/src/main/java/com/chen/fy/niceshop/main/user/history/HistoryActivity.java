package com.chen.fy.niceshop.main.user.history;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.detail.CommodityDetailActivity;
import com.chen.fy.niceshop.main.home.data.adapter.CommodityAdapter;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.main.home.view.fragment.RecommendFragment;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.UserSP;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private CommodityAdapter mAdapter;

    private List<Commodity> mList = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, HistoryActivity.class);
        context.startActivity(intent);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            mAdapter.setData(mList);
            recyclerView.setAdapter(mAdapter);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        bindView();
        initData();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.rv_history);
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
        new Thread(() -> fillData(RecommendFragment.allCommodityList)).start();
    }

    private void fillData(List<Commodity> allList) {
        // token
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        String spName = getResources().getString(R.string.history_sp_name);
        // 获取当前浏览记录
        SharedPreferences historySP = getSharedPreferences(spName, MODE_PRIVATE);
        String history = historySP.getString(token, "");
        if (history.isEmpty()) {
            return;
        }
        String[] historys = history.split(",");
        // 获取
        for (int j = historys.length - 1; j >= 0; j--) {
            String s = historys[j];
            int history_id = Integer.valueOf(s);
            for (int i = 0; i < allList.size(); i++) {
                Commodity commodity = allList.get(i);
                int commodity_id = commodity.getId();
                if (commodity_id == history_id) {
                    mList.add(commodity);
                    break;
                }
            }
        }
        mHandler.sendEmptyMessage(0);
    }
}
