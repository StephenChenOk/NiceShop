package com.chen.fy.niceshop.main.home.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.data.adapter.RankingListAdapter;
import com.chen.fy.niceshop.main.home.data.model.RankingItem;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.main.home.CommodityDetailActivity;

import java.util.ArrayList;

/**
 * 超值榜单
 */
public class RankingListActivity extends AppCompatActivity {

    private RecyclerView rvRanking;

    private ArrayList<RankingItem> mList;

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
        RankingListAdapter adapter = new RankingListAdapter(this, R.layout.ranking_list_item);
        fillListData();
        adapter.setList(mList);
        adapter.setListener(id -> {
            CommodityDetailActivity.start(this, id);
        });
        rvRanking.setAdapter(adapter);
    }

    private void fillListData() {
        mList = new ArrayList<>();

        RankingItem item1 = new RankingItem();
        item1.setId(1);
        item1.setTop(1);
        item1.setName(RUtil.toString(R.string.nike));
        item1.setImgPath(R.drawable.test_img);
        item1.setPrice(1999);
        item1.setType("天猫精选");
        item1.setCommentNum(5);
        item1.setHeatNum(158);

        RankingItem item2 = new RankingItem();
        item2.setId(2);
        item2.setTop(2);
        item2.setName(RUtil.toString(R.string.massage));
        item2.setImgPath(R.drawable.test_img);
        item2.setPrice(49);
        item2.setType("天猫精选");
        item2.setCommentNum(5);
        item2.setHeatNum(158);

        mList.add(item1);
        mList.add(item2);
    }
}
