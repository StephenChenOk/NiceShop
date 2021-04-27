package com.chen.fy.niceshop.main.look.view.activity;

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
import com.chen.fy.niceshop.main.look.data.adapter.LookGoodPriceAdapter;
import com.chen.fy.niceshop.main.look.data.model.dynamic.BaseDynamicResponse;
import com.chen.fy.niceshop.main.look.data.model.dynamic.Dynamic;
import com.chen.fy.niceshop.network.DynamicService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 更多好货
 */
public class LookGoodPriceActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private List<Dynamic> mDataList = new ArrayList<>();

    private LookGoodPriceAdapter mAdapter;

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, LookGoodPriceActivity.class);
        intent.putExtra(RUtil.toString(R.string.look_around_item_id), id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_good_price);
        ShowUtils.changeStatusBarTextImgColor(this, true);

        bindView();
        initData();
    }

    private void bindView() {
        mRecyclerView = findViewById(R.id.rv_look_good_price);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);

        setClick();
    }

    private void setClick() {
        findViewById(R.id.iv_back_look_good_price).setOnClickListener(v -> finish());
    }

    private void initData() {
        mAdapter = new LookGoodPriceAdapter(this, R.layout.look_good_price_item);
        mAdapter.setDataList(mDataList);
        mAdapter.setListener(position -> {
            LookDetailActivity.start(this, -1);
        });
        mRecyclerView.setAdapter(mAdapter);

        fillData();
    }

    public void fillData() {
        int id = getIntent().getIntExtra(RUtil.toString(R.string.look_around_item_id), -1);
        DynamicService service = ServiceCreator.create(DynamicService.class);
        service.related(id).enqueue(new Callback<BaseDynamicResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseDynamicResponse> call
                    , @NonNull Response<BaseDynamicResponse> response) {
                BaseDynamicResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    refresh(base.getDynamics());
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseDynamicResponse> call
                    , @NonNull Throwable t) {
                Log.i("getDynamic", "getDynamic Failure");
            }
        });
    }

    private void refresh(List<Dynamic> dynamics) {
        mDataList.clear();
        for (Dynamic dynamic : dynamics) {
            String imgs = dynamic.getImgs();
            String[] images = imgs.split(",");
            for (String image : images) {
                mDataList.add(dynamic.getNewDynamic(image));
            }
        }
        mAdapter.setDataList(mDataList);
        mAdapter.notifyDataSetChanged();
    }
}
