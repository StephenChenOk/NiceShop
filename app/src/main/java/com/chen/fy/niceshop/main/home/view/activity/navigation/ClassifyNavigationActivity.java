package com.chen.fy.niceshop.main.home.view.activity.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.classify.data.ClassifyAdapter;
import com.chen.fy.niceshop.main.goodprice.data.model.Category;

import java.util.ArrayList;
import java.util.List;

public class ClassifyNavigationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private NavigationAdapter mAdapter;

    private List<Category> mList;

    public static void start(Context context) {
        Intent intent = new Intent(context, ClassifyNavigationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classify_navigation_new);

        bindView();
        initData();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.rv_navigation);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        setListener();
    }

    private void setListener() {
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void initData() {
        mAdapter = new NavigationAdapter(this, R.layout.navigation_item);
        mAdapter.setListener(title -> NavigationListActivity.start(this, title));
        fillData();
    }

    private void fillData() {
        mList = new ArrayList<>();

        mList.add(new Category(1, "电脑", R.drawable.computer));
        mList.add(new Category(2, "手机", R.drawable.phone));
        mList.add(new Category(3, "包包", R.drawable.bao));
        mList.add(new Category(4, "潮鞋", R.drawable.xie));
        mList.add(new Category(5, "衣服", R.drawable.yifu));
        mList.add(new Category(6, "运动", R.drawable.yundong));
        mList.add(new Category(7, "美食", R.drawable.meishi));
        mList.add(new Category(8, "家具", R.drawable.jiaju));

        mAdapter.setDataList(mList);
        recyclerView.setAdapter(mAdapter);
    }
}
