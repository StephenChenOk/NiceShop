package com.chen.fy.niceshop.main.look.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.XWApplication;
import com.chen.fy.niceshop.main.look.data.model.dynamic.BaseDynamicResponse;
import com.chen.fy.niceshop.main.look.data.model.dynamic.Dynamic;
import com.chen.fy.niceshop.main.look.view.activity.LookDetailActivity;
import com.chen.fy.niceshop.main.look.data.adapter.LookAroundAdapter;
import com.chen.fy.niceshop.main.look.data.model.LookAroundItem;
import com.chen.fy.niceshop.main.user.collection.BaseCollectionResponse;
import com.chen.fy.niceshop.network.DynamicService;
import com.chen.fy.niceshop.network.GiveService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LookAroundItemFragment extends Fragment {

    private View mView;

    private RecyclerView mRecyclerView;

    private LookAroundAdapter mAdapter;

    private List<Dynamic> mDataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.look_around_item_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void bindView() {
        mRecyclerView = mView.findViewById(R.id.rv_look_around_item);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2
                , StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
    }

    private void initData() {
        mAdapter = new LookAroundAdapter(getContext(), R.layout.look_around_item);
        mAdapter.setDataList(mDataList);
        mAdapter.setListener(new LookAroundAdapter.IClickItemListener() {
            @Override
            public void clickItem(int id) {
                LookDetailActivity.start(getContext(), id);
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
    }

    public void fillData(String type) {
        DynamicService service = ServiceCreator.create(DynamicService.class);
        service.getDynamic(type).enqueue(new Callback<BaseDynamicResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseDynamicResponse> call
                    , @NonNull Response<BaseDynamicResponse> response) {
                BaseDynamicResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    mDataList = base.getDynamics();
                    mAdapter.setDataList(mDataList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseDynamicResponse> call
                    , @NonNull Throwable t) {
                Log.i("getDynamic", "getDynamic Failure");
            }
        });
    }

    /// 点赞动态
    private void giveDynamic(int dynamicID){
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
    private void cancelGiveDynamic(int dynamicID){
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
