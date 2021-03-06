package com.chen.fy.niceshop.main.goodprice.search;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.goodprice.GoodPriceDetailActivity;
import com.chen.fy.niceshop.main.goodprice.data.model.GoodPriceAdapter;
import com.chen.fy.niceshop.main.goodprice.network.ReptileSearchService;
import com.chen.fy.niceshop.main.goodprice.network.SearchServiceCreator;
import com.chen.fy.niceshop.main.search.data.HistoryAdapter;
import com.chen.fy.niceshop.main.search.data.model.History;
import com.chen.fy.niceshop.network.reptile.GetReptileData;
import com.chen.fy.niceshop.network.reptile.GoodPriceCommodity;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodPriceSearchActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private RecyclerView mRecyclerView;

    private EditText etSearch;
    private GoodPriceAdapter mSearchAdapter;
    private HistoryAdapter mHistoryAdapter;

    private List<GoodPriceCommodity> mSearchLists = new ArrayList<>();
    private List<String> mHistoryLists;

    private ViewStub vsHistory;
    private ViewStub vsSearch;

    private String historyID;

    // ??????
    private SmartRefreshLayout mRefreshLayout;

    // ??????????????????
    private int page = 1;

    private BasePopupView loading;

    public static void start(Context context) {
        Intent intent = new Intent(context, GoodPriceSearchActivity.class);
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

        // ??????????????????????????????
        ShowUtils.showSoftInputFromWindow(etSearch);

        initHistoryList();
        initSearchList();
    }

    /// ????????????
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
            // ???????????????????????????EditText???????????????
            etSearch.setText(mHistoryLists.get(position));
            etSearch.setSelection(etSearch.getText().length());
        });
        mRecyclerView.setAdapter(mHistoryAdapter);

        getHistories();
    }

    private void getHistories() {
        SharedPreferences sp = UserSP.getUserSP();
        String id = sp.getString(RUtil.toString(R.string.objectId_sp_key)
                , RUtil.toString(R.string.not_login));
        BmobQuery<History> query = new BmobQuery<>();
        query.addWhereEqualTo(RUtil.toString(R.string.history_userID), id);
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

    /// ???????????????????????????
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

    /// ?????????????????????
    private void initSearchList() {
        vsSearch = findViewById(R.id.vs_search_list);
        vsSearch.inflate();
        mRecyclerView = findViewById(R.id.rv_search);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);//1 ????????????
        mRecyclerView.setLayoutManager(layoutManager);

        mSearchAdapter = new GoodPriceAdapter(this, R.layout.commodity_item);
        mSearchAdapter.setListener(position -> {
            GoodPriceCommodity commodity = mSearchLists.get(position);
            GoodPriceDetailActivity.start(this, commodity);
        });

        mRefreshLayout = findViewById(R.id.refreshLayout_search);

        // ??????????????????
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mSearchLists.clear();
            doSearch();
        });

        // ????????????????????????
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> doSearch());

        loading = new XPopup.Builder(this).asLoading("?????????...");
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
                loading.show();
                String search = etSearch.getText().toString();
                if (!TextUtils.isEmpty(search)) {
                    showSearch();
                    mRefreshLayout.autoRefresh();
                }
                if (!TextUtils.isEmpty(historyID)) {
                    addHistory(search);
                }
                break;
        }
    }

    private void showDeleteDialog() {
        new XPopup.Builder(this)
                .asConfirm("??????????????????", "????????????????????????????????????",
                        "??????", "??????",
                        this::deleteHistories,
                        null,
                        false)
                .bindLayout(R.layout.delete_all_history_popup)
                .show();
    }

    /// ????????????????????????
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
                    Log.i("BMOB", "????????????????????????");
                } else {
                    Log.i("BMOB", "????????????????????????");
                }
            }
        });
        mHistoryLists.clear();
        mHistoryAdapter.notifyDataSetChanged();
    }

    /// ??????
    private void doSearch() {
        String search = etSearch.getText().toString();
        // map
        Map<String, Object> map = new HashMap<>();
        map.put("c", "faxian");
        map.put("s", search);
        map.put("v", "b");
        map.put("p", page++);

        ReptileSearchService service = SearchServiceCreator.create(ReptileSearchService.class);
        service.reptileSearchData(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call
                    , @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    String html = null;
                    try {
                        html = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //????????????
                    List<GoodPriceCommodity> list = GetReptileData.spiderSearchInfo(html);
                    if (list.size() == 0) {
                        Toast.makeText(GoodPriceSearchActivity.this, "?????????...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mSearchLists.addAll(list);
                    mSearchAdapter.setSearchStr(search);
                    mSearchAdapter.setData(mSearchLists);
                    mRecyclerView.setAdapter(mSearchAdapter);
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadMore();
                }
                loading.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
                loading.dismiss();
            }
        });
    }

    /// ??????????????????
    private void addHistory(String s) {
        History history = new History();
        history.addUnique("histories", s);
        history.update(historyID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "????????????");
                } else {
                    Log.i("bmob", "???????????????" + e.getMessage());
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
            // ????????????List
            dismissSearch();
            // ????????????????????????
            getHistories();
            // ??????????????????
            showHistories();
        } else {
            // ??????????????????
            dismissHistories();
            // ????????????List
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
            mSearchLists.clear();
            mSearchAdapter.notifyDataSetChanged();
        }
    }

    private void dismissSearch() {
        if (vsSearch != null) {
            vsSearch.setVisibility(View.GONE);
        }
    }

}

