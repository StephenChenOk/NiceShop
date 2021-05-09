package com.chen.fy.niceshop.main.home.comment;

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
import com.chen.fy.niceshop.main.home.detail.CommodityDetailActivity;
import com.chen.fy.niceshop.main.home.data.adapter.CommentAdapter;
import com.chen.fy.niceshop.main.home.data.model.Comment;
import com.chen.fy.niceshop.network.CommentService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCommentActivity extends AppCompatActivity {

    private CommentAdapter mAdapter;
    private RecyclerView recyclerView;

    private List<Comment> mList = new ArrayList<>();

    public static void start(Context context, int commodityID) {
        Intent intent = new Intent(context, AllCommentActivity.class);
        intent.putExtra(RUtil.toString(R.string.commodity_id_detail), commodityID);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.all_comment);
        bindView();
        initData();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.rv_all_comment);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        setListener();
    }

    private void setListener() {
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void initData() {
        mAdapter = new CommentAdapter(this, R.layout.comment_item);
        mAdapter.setListener(id -> CommodityDetailActivity.start(this, id));
        fillData();
    }

    /// 获取商品ID
    private int getCommodityID() {
        int id = RUtil.toInt(R.integer.detail_failure);
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra(RUtil.toString(R.string.commodity_id_detail), id);
        }
        return id;
    }

    private void fillData() {
        int commodityID = getCommodityID();
        CommentService service = ServiceCreator.create(CommentService.class);
        service.getAllComment(commodityID).enqueue(new Callback<BaseAllCommentResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseAllCommentResponse> call
                    , @NonNull Response<BaseAllCommentResponse> response) {
                BaseAllCommentResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    mList = base.getComments();
                    mAdapter.setData(mList);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseAllCommentResponse> call
                    , @NonNull Throwable t) {
                Log.e("getAllComment", "getAllComment Failure");
            }
        });
    }
}
