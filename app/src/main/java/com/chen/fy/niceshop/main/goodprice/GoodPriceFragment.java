package com.chen.fy.niceshop.main.goodprice;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.goodprice.data.model.BaseCategoryResponse;
import com.chen.fy.niceshop.main.goodprice.data.model.Category;
import com.chen.fy.niceshop.main.goodprice.data.model.GoodPriceAdapter;
import com.chen.fy.niceshop.main.goodprice.network.ReptileSearchService;
import com.chen.fy.niceshop.main.goodprice.network.ReptileService;
import com.chen.fy.niceshop.main.goodprice.network.ReptileServiceCreator;
import com.chen.fy.niceshop.main.goodprice.network.SearchServiceCreator;
import com.chen.fy.niceshop.main.goodprice.search.GoodPriceSearchActivity;
import com.chen.fy.niceshop.main.classify.data.ClassifyAdapter;
import com.chen.fy.niceshop.main.home.data.model.BaseCommodityResponse;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.main.home.view.activity.navigation.ClassifyNavigationActivity;
import com.chen.fy.niceshop.main.home.view.fragment.RecommendFragment;
import com.chen.fy.niceshop.network.CategoryService;
import com.chen.fy.niceshop.network.CommodityService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.network.reptile.GetReptileData;
import com.chen.fy.niceshop.network.reptile.GoodPriceCommodity;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 好价
 */
public class GoodPriceFragment extends Fragment {

    private View mView;

    // view
    private TextView tvSearch;
    private ImageView ivClassify;
    private RecyclerView rvGoodPrice;

    // main radio btn
    private RadioButton rbClassify;
    private RadioButton rbShopMall;
    private RadioButton rbPrice;
    private RadioButton rbMultipleMain;

    // 蒙层
    private View mMantleView;
    // 没有搜索到数据
    private View tvWu;

    // classify
    private ViewStub vsClassify;
    private RecyclerView rvClassify;
    private List<Category> mClassifyLists;
    private ClassifyAdapter mClassifyAdapter;

    // shop mall
    private ViewStub vsShopMall;
    private ArrayList<View> mShopMallList;  //所点击的商城

    // price
    private ViewStub vsPrice;
    private EditText etLowest;
    private EditText etHighest;
    private Button btnReset;
    private Button btnDefine;

    // Multiple
    private ViewStub vsMultiple;
    private RadioButton rbMultiple;
    private RadioButton rbNewest;
    private RadioButton rbThreeHour;
    private RadioButton rbTwelveHour;
    private RadioButton rbOneDay;

    // 刷新
    private SmartRefreshLayout mRefreshLayout;
    private int page = 0;

    private BasePopupView loading;

    // 当前分类
    private String classify = "推荐";

    // data
    private GoodPriceAdapter mAdapter;
    private List<GoodPriceCommodity> mList = new ArrayList<>();
    // query data
    private List<GoodPriceCommodity> mQueryList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.price_fragment, container, false);
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

    /**
     * Fragment在show与hide状态转换时调用此方法
     *
     * @param hidden 是否是hide状态
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && getActivity() != null) {
            ShowUtils.changeStatusBarTextImgColor(getActivity(), true);

            mList.clear();
            mRefreshLayout.autoRefresh();
        }
    }

    private void bindView() {
        tvSearch = mView.findViewById(R.id.tv_search_good_price);
        ivClassify = mView.findViewById(R.id.iv_classify_good_price);
        mMantleView = mView.findViewById(R.id.mantle_good_price);
        mMantleView.setOnClickListener(v -> resetView());
        tvWu = mView.findViewById(R.id.tv_wu);

        mRefreshLayout = mView.findViewById(R.id.refreshLayout_price);

        // recyclerView
        rvGoodPrice = mView.findViewById(R.id.rv_good_price);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        rvGoodPrice.setLayoutManager(manager);

        setListener();
    }

    private void setListener() {
        tvSearch.setOnClickListener(v -> {
            GoodPriceSearchActivity.start(getContext());
        });
        ivClassify.setOnClickListener(v -> {
            ClassifyNavigationActivity.start(getContext());
        });

        RadioGroup rgMain = mView.findViewById(R.id.rg_good_price);
        rbClassify = mView.findViewById(R.id.rb_classify);
        rbShopMall = mView.findViewById(R.id.rb_shop_mall);
        rbPrice = mView.findViewById(R.id.rb_price);
        rbMultipleMain = mView.findViewById(R.id.rb_multiple);
        rgMain.setOnCheckedChangeListener((group, id) -> {
            group.findViewById(id).setOnClickListener(new IClickMainRG());
        });
    }

    private void initData() {
        mAdapter = new GoodPriceAdapter(getContext(), R.layout.commodity_item);
        mAdapter.setListener(position -> {
            GoodPriceCommodity commodity = mList.get(position);
            GoodPriceDetailActivity.start(getContext(), commodity);
        });
        mAdapter.setData(mList);
        rvGoodPrice.setAdapter(mAdapter);

        // 顶部下拉刷新
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            if (classify.equals("推荐")) {
                mList.clear();
                fillDataList();
            } else {
                mQueryList.clear();
                clickClassify(classify);
            }
        });

        // 底部下拉加载更多
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (classify.equals("推荐")) {
                fillDataList();
            } else {
                clickClassify(classify);
            }
        });

        loading = new XPopup.Builder(getContext()).asLoading("搜索中...");
    }

    /**
     * 隐藏掉所显示的VS，出了点击的本身View
     */
    private void hideAllVS(int id) {
        if (vsClassify != null && id != R.id.rb_classify) {
            vsClassify.setVisibility(View.GONE);
        }
        if (vsShopMall != null && id != R.id.rb_shop_mall) {
            vsShopMall.setVisibility(View.GONE);
        }
        if (vsPrice != null && id != R.id.rb_price) {
            vsPrice.setVisibility(View.GONE);
        }
        if (vsMultiple != null && id != R.id.rb_multiple) {
            vsMultiple.setVisibility(View.GONE);
        }
    }

    /**
     * 点击蒙层，重置界面
     */
    private void resetView() {
        if (mMantleView.getVisibility() == View.VISIBLE) {
            mMantleView.setVisibility(View.GONE);
            rbClassify.setChecked(false);
            rbShopMall.setChecked(false);
            rbPrice.setChecked(false);
            rbMultipleMain.setChecked(false);
            rbMultipleMain.setText(RUtil.toString(R.string.multiple));
            if (mShopMallList != null) {
                mShopMallList.clear();
            }
            hideAllVS(-1);
        }
    }

    //region classify vs

    /**
     * 显示 classify vs
     */
    private void showClassifyVS() {
        if (null == vsClassify) {
            // 反射vs
            vsClassify = mView.findViewById(R.id.vs_classify_good_price);
            vsClassify.inflate();
            // bind view
            bindClassifyView();
        }
        // 显示vs
        vsClassify.setVisibility(View.VISIBLE);
    }

    /**
     * 绑定Classify模块布局
     */
    private void bindClassifyView() {
        // recyclerView
        rvClassify = mView.findViewById(R.id.rv_classify);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        rvClassify.setLayoutManager(manager);
        initClassifyData();
    }

    private void initClassifyData() {
        mClassifyAdapter = new ClassifyAdapter(getContext(), R.layout.classify_item);
        // TODO: 2021/3/30 分类item点击
        mClassifyAdapter.setListener(name -> {
            loading.show();
            clickClassify(name);
        });
        fillClassifyList();
    }

    private void clickClassify(String search) {
        // 当前分类
        classify = search;
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
                    List<GoodPriceCommodity> list = GetReptileData.spiderSearchInfo(html);
                    if (list.size() == 0) {
                        Toast.makeText(getContext(), "到底了...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mQueryList.addAll(list);
                    // 关闭所有VS
                    hideAllVS(-1);
                    mMantleView.setVisibility(View.GONE);
                    // 刷新数据
                    mAdapter.setData(mQueryList);
                    rvGoodPrice.setAdapter(mAdapter);
                    tvWu.setVisibility(View.GONE);
                    if (mQueryList.size() < 1) {
                        tvWu.setVisibility(View.VISIBLE);
                    }
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

    private void fillClassifyList() {
        mClassifyLists = new ArrayList<>();

        CategoryService service = ServiceCreator.create(CategoryService.class);
        service.getCategoryInfo().enqueue(new Callback<BaseCategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCategoryResponse> call
                    , @NonNull Response<BaseCategoryResponse> response) {
                BaseCategoryResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    changeClassifyData(base);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseCategoryResponse> call
                    , @NonNull Throwable t) {
                Log.i("getCategoryInfo", "getCategoryInfo Failure");
            }
        });
    }

    private void changeClassifyData(BaseCategoryResponse base) {
        mClassifyLists = base.getCategory();
        mClassifyAdapter.setDataList(mClassifyLists);
        rvClassify.setAdapter(mClassifyAdapter);
    }

    //endregion

    //region shop mall vs

    /**
     * 显示 shop mall vs
     */
    private void showShopMallVS() {
        if (null == vsShopMall) {
            // 反射vs
            vsShopMall = mView.findViewById(R.id.vs_shop_mall_good_price);
            vsShopMall.inflate();
            // init array
            mShopMallList = new ArrayList<>();
            // bind view
            bindShopMallView();
        }
        if (vsShopMall.getVisibility() == View.VISIBLE) {
            vsShopMall.setVisibility(View.GONE);
            mMantleView.setVisibility(View.GONE);
        } else {
            vsShopMall.setVisibility(View.VISIBLE);
        }
    }

    /// 绑定ShopMall模块布局
    private void bindShopMallView() {
        // 商城分类
        mView.findViewById(R.id.tv_jingdong).setOnClickListener(this::clickShopMallItem);
        mView.findViewById(R.id.tv_tianmao).setOnClickListener(this::clickShopMallItem);
        mView.findViewById(R.id.tv_pinduoduo).setOnClickListener(this::clickShopMallItem);
        mView.findViewById(R.id.tv_taobao).setOnClickListener(this::clickShopMallItem);
        mView.findViewById(R.id.tv_xiaomi).setOnClickListener(this::clickShopMallItem);
        mView.findViewById(R.id.tv_suning).setOnClickListener(this::clickShopMallItem);
        mView.findViewById(R.id.tv_kaola).setOnClickListener(this::clickShopMallItem);
        mView.findViewById(R.id.tv_juhuasuan).setOnClickListener(this::clickShopMallItem);
        // 重置
        mView.findViewById(R.id.btn_reset_shop_mall).setOnClickListener(v -> resetShopMall());
        // 确定
        mView.findViewById(R.id.btn_define_shop_mall).setOnClickListener(v -> defineShopMall());
    }

    /// 点击商城Item
    private void clickShopMallItem(View v) {
        // 根据点击状态改变颜色
        if (v.isSelected()) {
            v.setSelected(false);
        } else {
            v.setSelected(true);
        }
        if (mShopMallList.contains(v)) {
            for (int i = 0; i < mShopMallList.size(); i++) {
                if (v == mShopMallList.get(i)) {
                    // 重新从上一个index开始遍历，因为删除某一个值后，Index重新刷新
                    mShopMallList.remove(i);
                    i--;
                }
            }
        } else {
            mShopMallList.add(v);
        }
    }

    /// 重置商城
    private void resetShopMall() {
        for (View v : mShopMallList) {
            v.setSelected(false);
        }
        mShopMallList.clear();
        mAdapter.setData(mList);
        tvWu.setVisibility(View.GONE);
    }

    /// 确定商城搜索
    private void defineShopMall() {
        if (mShopMallList.size() < 1) {
            if (tvWu.getVisibility() == View.VISIBLE) {
                tvWu.setVisibility(View.GONE);
                mMantleView.setVisibility(View.GONE);
                mAdapter.setData(mList);
                hideAllVS(-1);
            }
            return;
        }
        if (mList == null || mList.size() < 1) {
            return;
        }
        if (mQueryList == null) {
            mQueryList = new ArrayList<>();
        } else {
            mQueryList.clear();
            mAdapter.notifyDataSetChanged();
        }
        // loading
        BasePopupView loading = new XPopup.Builder(getContext()).asLoading("加载中...").show();
        // 获取选取的商城名称
        StringBuilder stringBuilder = new StringBuilder();
        for (View v : mShopMallList) {
            TextView textView = (TextView) v;
            stringBuilder.append(textView.getText().toString());
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);
        String platform = stringBuilder.toString();
        // server
        CommodityService service = ServiceCreator.create(CommodityService.class);
        service.queryPlatform(platform).enqueue(new Callback<BaseCommodityResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Response<BaseCommodityResponse> response) {
                BaseCommodityResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    List<Commodity> list = base.getCommodity();
                    mQueryList = configData(list);
                    // 关闭所有VS
                    hideAllVS(-1);
                    mMantleView.setVisibility(View.GONE);
                    // 刷新数据
                    mAdapter.setData(mQueryList);
                    rvGoodPrice.setAdapter(mAdapter);
                    tvWu.setVisibility(View.GONE);
                    if (mQueryList.size() < 1) {
                        tvWu.setVisibility(View.VISIBLE);
                    }
                }
                // 关闭loading
                loading.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Throwable t) {
                Log.i("queryPlatform", "queryPlatform Failure");
                // 关闭loading
                loading.dismiss();
            }
        });
    }

    private List<GoodPriceCommodity> configData(List<Commodity> list) {
        List<GoodPriceCommodity> data = new ArrayList<>();
        for (Commodity commodity : list) {
            GoodPriceCommodity goodPriceCommodity = new GoodPriceCommodity(
                    commodity.getTitle(),
                    "http://" + commodity.getPic_url(),
                    commodity.getView_price() + "元",
                    "content",
                    "",
                    commodity.getPlatform()
            );
            data.add(goodPriceCommodity);
        }
        return data;
    }

    //endregion

    //region price vs

    /// 显示 price vs
    private void showPriceVS() {
        if (null == vsPrice) {
            // 反射vs
            vsPrice = mView.findViewById(R.id.vs_price_good_price);
            vsPrice.inflate();
            // bind view
            bindPriceView();
        }
        // 显示vs
        if (vsPrice.getVisibility() == View.VISIBLE) {
            vsPrice.setVisibility(View.GONE);
            mMantleView.setVisibility(View.GONE);
        } else {
            vsPrice.setVisibility(View.VISIBLE);
        }
    }

    /// 绑定Price模块布局
    private void bindPriceView() {
        etLowest = mView.findViewById(R.id.et_lowest_price);
        etHighest = mView.findViewById(R.id.et_highest_price);
        mView.findViewById(R.id.btn_reset_good_price).setOnClickListener(v -> {
            resetPrice();
        });
        mView.findViewById(R.id.btn_define_good_price).setOnClickListener(v -> {
            definePrice();
        });
    }

    /// 重置价钱
    private void resetPrice() {
        etLowest.setText("");
        etHighest.setText("");
    }

    /// 确定搜索价钱区间
    private void definePrice() {
        int lowest;
        int highest;
        // 最小价钱
        if (TextUtils.isEmpty(etLowest.getText().toString())) {
            lowest = 0;
        } else {
            lowest = Integer.valueOf(etLowest.getText().toString());
        }
        // 最大价钱
        if (TextUtils.isEmpty(etHighest.getText().toString())) {
            highest = Integer.MAX_VALUE;
        } else {
            highest = Integer.valueOf(etHighest.getText().toString());
        }
        // 出去不合理区间
        if (highest < lowest) {
            Toast.makeText(getContext(), "请正确输入价格区间", Toast.LENGTH_SHORT).show();
            return;
        }
        // loading
        BasePopupView loading = new XPopup.Builder(getContext()).asLoading("加载中...").show();
        if (mQueryList == null) {
            mQueryList = new ArrayList<>();
        }
        mQueryList.clear();
        List<Commodity> commodities = new ArrayList<>();
        // server
        for (Commodity commodity : RecommendFragment.allCommodityList) {
            int price = commodity.getView_price();
            if (price >= lowest && price <= highest) {
                commodities.add(commodity);
            }
        }
        mQueryList = configData(commodities);
        // 关闭loading
        loading.dismiss();
        // 关闭所有VS
        hideAllVS(-1);
        mMantleView.setVisibility(View.GONE);
        // 刷新数据
        mAdapter.setData(mQueryList);
        mAdapter.notifyDataSetChanged();
        if (mQueryList.size() < 1) {
            tvWu.setVisibility(View.VISIBLE);
        }
    }

    //endregion

    //region multiple vs

    /**
     * 显示 multiple vs
     */
    private void showMultipleVS() {
        if (null == vsMultiple) {
            // 反射vs
            vsMultiple = mView.findViewById(R.id.vs_multiple_good_price);
            vsMultiple.inflate();
            // bind view
            bindMultipleView();
        }
        // 显示vs
        if (vsMultiple.getVisibility() == View.VISIBLE) {
            vsMultiple.setVisibility(View.GONE);
            mMantleView.setVisibility(View.GONE);
        } else {
            vsMultiple.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 绑定Multiple模块布局
     */
    private void bindMultipleView() {
        RadioGroup rgMultiple = mView.findViewById(R.id.rg_multiple);
        rbMultiple = mView.findViewById(R.id.rb_multiple_good_price);
        rbNewest = mView.findViewById(R.id.rb_newest_good_price);
        rbThreeHour = mView.findViewById(R.id.rb_three_hour_good_price);
        rbTwelveHour = mView.findViewById(R.id.rb_twelve_hour_good_price);
        rbOneDay = mView.findViewById(R.id.rb_one_day_good_price);
        rgMultiple.setOnCheckedChangeListener((group, id) -> {
            group.findViewById(id).setOnClickListener(new IClickMultipleRG());
        });
    }
    //endregion

    private void fillDataList() {
        String pageStr = "p" + page;
        page = page + 2;
        ReptileService service = ReptileServiceCreator.create(ReptileService.class);
        service.getReptileData(pageStr).enqueue(new Callback<ResponseBody>() {
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
                    List<GoodPriceCommodity> list = GetReptileData.spiderGoodPrice(html);
                    if (list.size() == 0) {
                        Toast.makeText(getContext(), "到底了...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mList.addAll(list);
                    mAdapter.setData(mList);
                    mAdapter.notifyDataSetChanged();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //region click listener

    /**
     * 点击主RadioGroup
     */
    private class IClickMainRG implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            hideAllVS(v.getId());
            mMantleView.setVisibility(View.VISIBLE);
            switch (v.getId()) {
                case R.id.rb_classify:
                    showClassifyVS();
                    break;
                case R.id.rb_shop_mall:
                    showShopMallVS();
                    break;
                case R.id.rb_price:
                    showPriceVS();
                    break;
                case R.id.rb_multiple:
                    showMultipleVS();
                    break;
            }
        }
    }

    /**
     * 点击综合排序RadioGroup
     */
    private class IClickMultipleRG implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rb_multiple_good_price:  // 综合
                    rbMultipleMain.setText(RUtil.toString(R.string.multiple));
                    break;
                case R.id.rb_newest_good_price: //最新
                    rbMultipleMain.setText(RUtil.toString(R.string.newest));
                    break;
                case R.id.rb_three_hour_good_price: //3小时最热
                    rbMultipleMain.setText("3小时");
                    break;
                case R.id.rb_twelve_hour_good_price: //12小时最热
                    rbMultipleMain.setText("12小时");
                    break;
                case R.id.rb_one_day_good_price: //24小时最热
                    rbMultipleMain.setText("24小时");
                    break;
            }
        }
    }
    //endregion
    //</editor-fold>
}


