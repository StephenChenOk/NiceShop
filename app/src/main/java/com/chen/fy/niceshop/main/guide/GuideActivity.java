package com.chen.fy.niceshop.main.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.XWApplication;
import com.chen.fy.niceshop.main.goodprice.data.model.BaseCategoryResponse;
import com.chen.fy.niceshop.main.goodprice.data.model.Category;
import com.chen.fy.niceshop.main.user.collection.BaseCollectionResponse;
import com.chen.fy.niceshop.network.CategoryService;
import com.chen.fy.niceshop.network.GiveService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 新用户注册成功后跳转的 引导页面
public class GuideActivity extends AppCompatActivity {

    private Button btnCommit;

    private String token;
    private RecyclerView recyclerView;

    private GuideAdapter mAdapter;

    private List<Category> mList;

    private List<String> mClickItemList = new ArrayList<>();

    public static void start(Context context, String token) {
        Intent intent = new Intent(context, GuideActivity.class);
        intent.putExtra(RUtil.toString(R.string.token), token);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        ShowUtils.changeStatusBarTextImgColor(this, true);
        bindView();
        getIntentData();
        initData();
    }

    private void bindView() {
        btnCommit = findViewById(R.id.btn_submit);

        recyclerView = findViewById(R.id.rv_guide);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        setClickListener();
    }

    private void setClickListener() {
        findViewById(R.id.tv_skip).setOnClickListener(v -> finish());
        btnCommit.setOnClickListener(v -> commit());
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            token = intent.getStringExtra(RUtil.toString(R.string.token));
        }
    }

    private void initData() {
        mAdapter = new GuideAdapter(this, R.layout.guide_item);
        mAdapter.setListener(this::clickItem);
        fillData();
    }

    private void fillData() {
        // server
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

    /// 点击商城Item
    private void clickItem(String title) {
        if (mClickItemList.contains(title)) {
            for (int i = 0; i < mClickItemList.size(); i++) {
                if (title.equals(mClickItemList.get(i))) {
                    // 重新从上一个index开始遍历，因为删除某一个值后，Index重新刷新
                    mClickItemList.remove(i);
                    i--;
                }
            }
        } else {
            mClickItemList.add(title);
        }

        if (mClickItemList.size() >= 1) {
            btnCommit.setBackground(getResources().getDrawable(R.drawable.guide_btn_red));
        } else {
            btnCommit.setBackground(getResources().getDrawable(R.drawable.guide_btn_gray));
        }
    }

    /// 提交
    private void commit() {
        // loading
        BasePopupView loading = new XPopup.Builder(this).asLoading("提交中...").show();
        // 获取选取的商城名称
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : mClickItemList) {
            stringBuilder.append(s);
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);
        String interests = stringBuilder.toString();
        doPost(interests);

        loading.dismiss();
    }

    private void doPost(String category) {
        // server
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        RequestBody categoryBody = RequestBody.create(
                null, category.getBytes(Util.UTF_8));
        CategoryService service = ServiceCreator.create(CategoryService.class);
        service.coldRec(token, categoryBody).enqueue(new Callback<BaseGuideResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseGuideResponse> call
                    , @NonNull Response<BaseGuideResponse> response) {
                BaseGuideResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    Toast.makeText(GuideActivity.this, base.getMsg(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseGuideResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
