package com.chen.fy.niceshop.main.look.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.data.adapter.CommentAdapter;
import com.chen.fy.niceshop.main.home.data.adapter.viewpager.BannerPagerAdapter;
import com.chen.fy.niceshop.main.home.data.model.Comment;
import com.chen.fy.niceshop.main.home.view.fragment.RecommendFragment;
import com.chen.fy.niceshop.main.look.data.model.dynamic.BaseDynamicDetailResponse;
import com.chen.fy.niceshop.main.look.data.model.dynamic.BaseDynamicResponse;
import com.chen.fy.niceshop.main.look.data.model.dynamic.Dynamic;
import com.chen.fy.niceshop.network.DynamicService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 逛逛详情
 */
public class LookDetailActivity extends AppCompatActivity {

    // view
    private CircleImageView civHeadIcon;
    private TextView tvName;
    private ViewPager vpDetail;
    private TextView tvCommentCount;
    private TextView tvAllComment;
    private TextView tvContent;
    private TextView tvMore;

    // recyclerView
    private RecyclerView rvComment;

    // data
    private List<Comment> mCommentList = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    private BannerPagerAdapter mBannerPagerAdapter;

    // 滑动时间间隔
    public static final int slideInterval = 5000;
    // 滑动总次数
    private static final int slideCount = 500;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RecommendFragment.BANNER_SLIDE:
                    int item = vpDetail.getCurrentItem() + 1;
                    vpDetail.setCurrentItem(item);
                    //延迟发消息
                    mHandler.sendEmptyMessageDelayed(RecommendFragment.BANNER_SLIDE, slideInterval);
                    break;
            }
        }
    };

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, LookDetailActivity.class);
        intent.putExtra(RUtil.toString(R.string.look_around_item_id), id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_around_detail);
        ShowUtils.changeStatusBarTextImgColor(this, true);
        bindView();
        initData();
    }

    private void bindView() {
        civHeadIcon = findViewById(R.id.civ_headIcon_look);
        tvName = findViewById(R.id.tv_name_look);
        vpDetail = findViewById(R.id.vp_look_around_detail);
        tvContent = findViewById(R.id.id_content_look_around_detail);
        tvMore = findViewById(R.id.tv_more_commodity);

        // 评论栏
        tvCommentCount = findViewById(R.id.tv_comment_count_look);
        tvAllComment = findViewById(R.id.tv_all_comment_look);
        rvComment = findViewById(R.id.rv_comment_look);

        setListener();
    }

    private void setListener() {
        findViewById(R.id.iv_back_look).setOnClickListener(v -> {
        });
    }

    private void initData() {
        // 商品信息
        fillCommodityData();
        // 评论栏
        GridLayoutManager commentManager = new GridLayoutManager(this, 1);
        rvComment.setLayoutManager(commentManager);
        mCommentAdapter = new CommentAdapter(this, R.layout.comment_item);
        mCommentAdapter.setData(mCommentList);
        mCommentAdapter.setListener(position -> {
        });
        rvComment.setAdapter(mCommentAdapter);
        tvCommentCount.setText(String.valueOf(mCommentList.size()));
        tvAllComment.setText("查看全部" + mCommentList.size() + "条评论");
    }

    public void fillCommodityData() {
        // look around item id
        int id = getIntent().getIntExtra(RUtil.toString(R.string.look_around_item_id), -1);
        // Authorization
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        DynamicService service = ServiceCreator.create(DynamicService.class);
        service.dynamicDetail(token, id).enqueue(new Callback<BaseDynamicDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseDynamicDetailResponse> call
                    , @NonNull Response<BaseDynamicDetailResponse> response) {
                BaseDynamicDetailResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    refresh(base);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseDynamicDetailResponse> call
                    , @NonNull Throwable t) {
                Log.i("getDynamic", "getDynamic Failure");
            }
        });
    }

    private void refresh(BaseDynamicDetailResponse base) {
        Dynamic dynamic = base.getDynamic();
        // fill
        Glide.with(this)
                .load(ServiceCreator.ROOT_URL + dynamic.getUser_img()).into(civHeadIcon);
        tvName.setText(dynamic.getUser_nickname());
        tvMore.setText(dynamic.getUser_nickname() + "的更多商品");
        tvContent.setText(dynamic.getContent());
        // images
        setViewPager(dynamic.getImgs());
        // comment
        tvCommentCount.setText(String.valueOf(base.getCount()));
        mCommentList = base.getComments();
        mCommentAdapter.setData(mCommentList);
        mCommentAdapter.notifyDataSetChanged();
        // click
        findViewById(R.id.ll_good_price_box).setOnClickListener(v ->
                LookGoodPriceActivity.start(this, dynamic.getUser_id()));
    }

    private void setViewPager(String imgs) {
        // images
        String[] images = imgs.split(",");
        List<String> list = new ArrayList<>(Arrays.asList(images));
        // adapter
        mBannerPagerAdapter = new BannerPagerAdapter(this, mHandler);
        mBannerPagerAdapter.setStringData(list);
        mBannerPagerAdapter.setCount(slideCount);
        vpDetail.setAdapter(mBannerPagerAdapter);
        // 设置当前页面在中间位置,保证可以实现左右滑动的效果
        vpDetail.setCurrentItem(slideCount / 2);
        //第一次进入时延迟发消息
        mHandler.sendEmptyMessageDelayed(RecommendFragment.BANNER_SLIDE, slideInterval);
    }
}
