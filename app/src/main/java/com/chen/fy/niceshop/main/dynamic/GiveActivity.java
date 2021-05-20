package com.chen.fy.niceshop.main.dynamic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.XWApplication;
import com.chen.fy.niceshop.main.look.data.adapter.LookAroundAdapter;
import com.chen.fy.niceshop.main.look.data.model.dynamic.BaseDynamicResponse;
import com.chen.fy.niceshop.main.look.data.model.dynamic.Dynamic;
import com.chen.fy.niceshop.main.look.view.activity.LookDetailActivity;
import com.chen.fy.niceshop.main.user.collection.BaseCollectionResponse;
import com.chen.fy.niceshop.network.DynamicService;
import com.chen.fy.niceshop.network.GiveService;
import com.chen.fy.niceshop.network.PersonService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.UserSP;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiveActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;

    private LookAroundAdapter mAdapter;

    private List<Dynamic> mDataList = new ArrayList<>();

    private TextView tvNo;

    public static void start(Context context) {
        Intent intent = new Intent(context, GiveActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_layout);

        bindView();
        initData();
    }

    private void bindView() {
        mRecyclerView = findViewById(R.id.rv_dynamic);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2
                , StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        tvNo = findViewById(R.id.tv_no);
    }

    private void initData() {
        mAdapter = new LookAroundAdapter(this, R.layout.look_around_item);
        mAdapter.setDataList(mDataList);
        mAdapter.setListener(new LookAroundAdapter.IClickItemListener() {
            @Override
            public void clickItem(int id) {
                LookDetailActivity.start(GiveActivity.this, id);
            }

            @Override
            public void doGive(int id) {
                giveDynamic(id);
            }

            @Override
            public void cancelGive(int id) {
                cancelGiveDynamic(id);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        fillData();
    }

    public void fillData() {
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");

        PersonService service = ServiceCreator.create(PersonService.class);
        service.giveList(token).enqueue(new Callback<BaseGiveResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseGiveResponse> call
                    , @NonNull Response<BaseGiveResponse> response) {
                BaseGiveResponse base = response.body();
                if (base != null) {
                    if(base.getCode() == RUtil.toInt(R.integer.server_success)) {
                        mDataList = base.getGiveList();
                        mAdapter.setDataList(mDataList);
                        mAdapter.notifyDataSetChanged();
                    }
                    if(base.getCode() == RUtil.toInt(R.integer.server_error)) {
                        tvNo.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseGiveResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /// 点赞动态
    private void giveDynamic(int dynamicID) {
        // token
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        RequestBody idBody = RequestBody.create(
                null, String.valueOf(dynamicID).getBytes(Util.UTF_8));
        GiveService service = ServiceCreator.create(GiveService.class);
        service.doGive(token, idBody).enqueue(new Callback<BaseCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCollectionResponse> call
                    , @NonNull Response<BaseCollectionResponse> response) {
                BaseCollectionResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    Toast.makeText(XWApplication.getContext()
                            , base.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(XWApplication.getContext()
                            , "操作失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseCollectionResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /// 取消点赞动态
    private void cancelGiveDynamic(int dynamicID) {
        // token
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        RequestBody idBody = RequestBody.create(
                null, String.valueOf(dynamicID).getBytes(Util.UTF_8));
        GiveService service = ServiceCreator.create(GiveService.class);
        service.cancelGive(token, idBody).enqueue(new Callback<BaseCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCollectionResponse> call
                    , @NonNull Response<BaseCollectionResponse> response) {
                BaseCollectionResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    Toast.makeText(XWApplication.getContext()
                            , base.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(XWApplication.getContext()
                            , "操作失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseCollectionResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
