package com.chen.fy.niceshop.main.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.CommodityDetailActivity;
import com.chen.fy.niceshop.main.home.data.adapter.CommodityAdapter;
import com.chen.fy.niceshop.main.home.data.model.BaseCommodityResponse;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.main.home.view.fragment.RecommendFragment;
import com.chen.fy.niceshop.main.search.data.HistoryAdapter;
import com.chen.fy.niceshop.main.search.data.model.History;
import com.chen.fy.niceshop.network.CommodityService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private RecyclerView mRecyclerView;

    private EditText etSearch;
    private CommodityAdapter mSearchAdapter;
    private HistoryAdapter mHistoryAdapter;

    private List<Commodity> mSearchLists = new ArrayList<>();
    private List<String> mHistoryLists;

    private ViewStub vsHistory;
    private ViewStub vsSearch;

    private String historyID;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        initView();
    }

    private void initView() {
        etSearch = findViewById(R.id.et_search);
        TextView tvSearch = findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(this);
        etSearch.addTextChangedListener(this);
        // 获取焦点，弹出软键盘
        ShowUtils.showSoftInputFromWindow(etSearch);

        initHistoryList();
    }

    /// 历史记录
    private void initHistoryList() {
        vsHistory = findViewById(R.id.vs_history_list);
        vsHistory.inflate();
        RecyclerView mRecyclerView = findViewById(R.id.rv_history);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        findViewById(R.id.iv_delete_history).setOnClickListener(this);

        mHistoryLists = new ArrayList<>();
        mHistoryAdapter = new HistoryAdapter();
        mHistoryAdapter.setList(mHistoryLists);
        mHistoryAdapter.setOnClickSearchItem(position -> {
            // 点击搜索历史，回调EditText，进行搜索
            etSearch.setText(mHistoryLists.get(position));
            etSearch.setSelection(etSearch.getText().length());
        });
        mRecyclerView.setAdapter(mHistoryAdapter);

        getHistories();
    }

    private void getHistories() {
        SharedPreferences sp = UserSP.getUserSP();
        String id = sp.getString(RUtil.toString(R.string.token)
                , RUtil.toString(R.integer.not_login));
        BmobQuery<History> query = new BmobQuery<>();
        query.addWhereEqualTo(RUtil.toString(R.string.history_userID), String.valueOf(id));
        query.findObjects(new FindListener<History>() {
            @Override
            public void done(List<History> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 0) {
                        createHistoryTable(id);
                    }
                    for (History history : object) {
                        historyID = history.getObjectId();
                        mHistoryLists.clear();
                        mHistoryLists.addAll(history.getHistories());
                        mHistoryAdapter.notifyDataSetChanged();
                    }
                } else if (object == null) {
                    createHistoryTable(id);
                }
            }
        });
    }

    /// 新建历史记录数据表
    private void createHistoryTable(String id) {
        History history = new History();
        history.setUserID(id);
        history.setHistories(new ArrayList<>());
        history.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e != null) {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

    /// 搜索到得数据集
    private void initSearchList() {
        vsSearch = findViewById(R.id.vs_search_list);
        vsSearch.inflate();
        mRecyclerView = findViewById(R.id.rv_search);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);//1 表示列数
        mRecyclerView.setLayoutManager(layoutManager);

        mSearchAdapter = new CommodityAdapter(this, R.layout.commodity_item);
        mSearchAdapter.setListener(id -> {
            CommodityDetailActivity.start(this, id);
        });
    }

    @Override
    public void onBackPressed() {
        if (etSearch.getText().toString().isEmpty()) {
            super.onBackPressed();
        } else {
            etSearch.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_delete_history:
                showDeleteDialog();
                break;
            case R.id.tv_search:
                String search = etSearch.getText().toString();
                if (!TextUtils.isEmpty(search)) {
                    doSearch(search);
                }
                if (!TextUtils.isEmpty(historyID)) {
                    addHistory(search);
                }
                break;
        }
    }

    private void showDeleteDialog() {
        new XPopup.Builder(this)
                .asConfirm("是否选择删除", "删除后搜索记录将彻底消失",
                        "取消", "确定",
                        this::deleteHistories,
                        null,
                        false)
                .bindLayout(R.layout.delete_all_history_popup)
                .show();
    }

    /// 删除全部历史记录
    private void deleteHistories() {
        SharedPreferences sp = UserSP.getUserSP();
        String id = sp.getString(RUtil.toString(R.string.token)
                , RUtil.toString(R.string.not_login));
        History history = new History();
        history.setUserID(id);
        history.delete(historyID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("BMOB", "删除历史记录成功");
                } else {
                    Log.i("BMOB", "删除历史记录失败");
                }
            }
        });
        mHistoryLists.clear();
        mHistoryAdapter.notifyDataSetChanged();
    }

    /// 搜索
    private void doSearch(String s) {
        BasePopupView loading = new XPopup.Builder(this).asLoading("搜索中").show();

        if (vsSearch == null) {
            initSearchList();
        }
        // server
        CommodityService service = ServiceCreator.create(CommodityService.class);
        service.search(s).enqueue(new Callback<BaseCommodityResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Response<BaseCommodityResponse> response) {
                BaseCommodityResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    mSearchLists = base.getCommodity();
                    mSearchAdapter.setSearchStr(s);
                    mSearchAdapter.setData(mSearchLists);
                    mRecyclerView.setAdapter(mSearchAdapter);
                }
                loading.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
                loading.dismiss();
            }
        });
    }

    /// 添加历史记录
    private void addHistory(String s) {
        History history = new History();
        history.addUnique("histories", s);
        history.update(historyID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "更新成功");
                } else {
                    Log.i("bmob", "更新失败：" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            // 隐藏搜索List
            dismissSearch();
            // 获取最新历史记录
            getHistories();
            // 显示历史记录
            showHistories();
        } else {
            // 隐藏历史记录
            dismissHistories();
            // 显示搜索List
            showSearch();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private void showHistories() {
        vsHistory.setVisibility(View.VISIBLE);
    }

    private void dismissHistories() {
        vsHistory.setVisibility(View.GONE);
    }

    private void showSearch() {
        if (vsSearch != null) {
            vsSearch.setVisibility(View.VISIBLE);
        }
    }

    private void dismissSearch() {
        if (vsSearch != null) {
            vsSearch.setVisibility(View.GONE);
        }
    }

}

