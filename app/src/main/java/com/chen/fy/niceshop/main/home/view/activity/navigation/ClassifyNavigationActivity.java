package com.chen.fy.niceshop.main.home.view.activity.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.classify.data.ClassifyAdapter;
import com.chen.fy.niceshop.main.goodprice.data.model.BaseCategoryResponse;
import com.chen.fy.niceshop.main.goodprice.data.model.Category;
import com.chen.fy.niceshop.network.CategoryService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        CategoryService service = ServiceCreator.create(CategoryService.class);
        service.getCategoryInfo().enqueue(new Callback<BaseCategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCategoryResponse> call
                    , @NonNull Response<BaseCategoryResponse> response) {
                BaseCategoryResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    mList = base.getCategory();
                    mAdapter.setDataList(mList);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseCategoryResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
