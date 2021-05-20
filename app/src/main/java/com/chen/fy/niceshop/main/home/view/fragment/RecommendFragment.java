package com.chen.fy.niceshop.main.home.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.data.model.BaseCommodityResponse;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.main.home.view.activity.navigation.ClassifyNavigationActivity;
import com.chen.fy.niceshop.network.CommodityService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.main.home.view.activity.CabbageZoneActivity;
import com.chen.fy.niceshop.main.home.detail.CommodityDetailActivity;
import com.chen.fy.niceshop.main.home.data.adapter.CommodityAdapter;
import com.chen.fy.niceshop.main.home.data.adapter.viewpager.BannerPagerAdapter;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.main.home.view.activity.HotTodayActivity;
import com.chen.fy.niceshop.main.home.view.activity.RankingListActivity;
import com.chen.fy.niceshop.utils.UserSP;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private View mView;
    private Context mContext;

    // 导航栏
    private ViewPager vpHome;
    private int imageCount = 500;
    public static int slideInterval = 5000;

    // 刷新
    private SmartRefreshLayout mRefreshLayout;

    // recyclerView
    private RecyclerView mRecyclerView;
    private CommodityAdapter commodityAdapter;

    // data list
    private List<Integer> imageList;
    public static List<Commodity> allCommodityList;
    private List<Commodity> cruCommodityList;

    // 当前加载的Item所在的index
    private int cruIndex = 0;
    // 每次加载的数量
    private int count = 10;

    // 下拉
    private final int DOWN_LOAD = 1;
    // 轮播图滑动
    public static final int BANNER_SLIDE = 2;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_LOAD:
                    loadMore();
                    break;
                case BANNER_SLIDE:
                    int item = vpHome.getCurrentItem() + 1;
                    vpHome.setCurrentItem(item);
                    //延迟发消息
                    mHandler.sendEmptyMessageDelayed(BANNER_SLIDE, slideInterval);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.recommend_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        ShowUtils.changeStatusBarTextImgColor(getActivity(), false);
        bindView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 清除所有消息
        mHandler.removeCallbacksAndMessages(null);
    }

    private void bindView() {
        vpHome = mView.findViewById(R.id.vp_home);
        mRefreshLayout = mView.findViewById(R.id.refreshLayout_found);

        // recyclerView
        mRecyclerView = mView.findViewById(R.id.rv_home);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);//1 表示列数
        mRecyclerView.setLayoutManager(layoutManager);

        // click listener
        setClickListener();
    }

    /**
     * 设置点击事件
     */
    private void setClickListener() {
        /// 今日推荐
        mView.findViewById(R.id.ll_hot_goods_box).setOnClickListener(v -> {
            HotTodayActivity.start(getContext());
        });
        /// 超值榜单
        mView.findViewById(R.id.ll_price_list_box).setOnClickListener(v -> {
            RankingListActivity.start(getContext());
        });
        /// 白菜专区
        mView.findViewById(R.id.ll_low_price_box).setOnClickListener(v -> {
            CabbageZoneActivity.start(getContext());
        });
        /// 分类导航
        mView.findViewById(R.id.ll_classify_box).setOnClickListener(v -> {
            ClassifyNavigationActivity.start(getContext());
        });
    }

    private void initData() {
        // viewPager
        imageList = new ArrayList<>();
        fillImageList();
        BannerPagerAdapter adapter = new BannerPagerAdapter(getContext(), mHandler);
        adapter.setIntegerData(imageList);
        adapter.setCount(imageCount);
        vpHome.setAdapter(adapter);
        vpHome.addOnPageChangeListener(this);
        // 设置当前页面在中间位置,保证可以实现左右滑动的效果
        vpHome.setCurrentItem(imageCount / 2);
        //第一次进入时延迟发消息
        mHandler.sendEmptyMessageDelayed(BANNER_SLIDE, slideInterval);

        allCommodityList = new ArrayList<>();
        cruCommodityList = new ArrayList<>();
        if (commodityAdapter == null) {
            commodityAdapter = new CommodityAdapter(mContext, R.layout.commodity_item);
            commodityAdapter.setData(cruCommodityList);
            commodityAdapter.setListener(id ->
                    CommodityDetailActivity.start(getContext(), id)
            );
            mRecyclerView.setAdapter(commodityAdapter);
        }

        // 顶部下拉刷新
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            allCommodityList.clear();
            getAllHeadlines();
        });

        // 底部下拉加载更多
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> mHandler.sendEmptyMessage(DOWN_LOAD));
        // 第一次进入触发自动刷新
        mRefreshLayout.autoRefresh();
    }

    private void fillImageList() {
        imageList.add(R.drawable.banner_1);
        imageList.add(R.drawable.banner_2);
        imageList.add(R.drawable.banner_3);
        imageList.add(R.drawable.banner_4);
        imageList.add(R.drawable.banner_5);
        imageList.add(R.drawable.banner_6);
    }

    /// 请求服务器获取到所有的商品信息
    private void getAllHeadlines() {
        CommodityService service = ServiceCreator.create(CommodityService.class);
        service.getAllCommodity().enqueue(new Callback<BaseCommodityResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Response<BaseCommodityResponse> response) {
                BaseCommodityResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    allCommodityList = base.getCommodity();
                    refresh();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseCommodityResponse> call
                    , @NonNull Throwable t) {
                Log.i("GetAllShareInfo", "GetAllShareInfo Failure");
                t.printStackTrace();
            }
        });
    }

    /// 刷新
    private void refresh() {
        cruIndex = 0;
        cruCommodityList.clear();
        loadMore();
    }

    /// 加载更多
    private void loadMore() {
        int total = allCommodityList.size();
        for (int i = 0; i < count; i++) {
            if (cruIndex == total) {
                break;
            }
            cruCommodityList.add(allCommodityList.get(cruIndex++));
        }
        commodityAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {   //拖拽状态
        } else if (state == ViewPager.SCROLL_STATE_SETTLING) {  //滑动状态
        } else if (state == ViewPager.SCROLL_STATE_IDLE) {   //静止状态
            mHandler.removeCallbacksAndMessages(null);   //清除消息
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    }
}
