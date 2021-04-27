package com.chen.fy.niceshop.main.goodprice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.comment.AllCommentActivity;
import com.chen.fy.niceshop.main.home.comment.BaseCommentResponse;
import com.chen.fy.niceshop.main.home.comment.CommentView;
import com.chen.fy.niceshop.main.home.data.adapter.CommentAdapter;
import com.chen.fy.niceshop.main.home.data.adapter.CommodityAdapter;
import com.chen.fy.niceshop.main.home.data.model.Comment;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.main.home.data.model.detail.BaseDetailResponse;
import com.chen.fy.niceshop.main.home.data.model.detail.CommodityDetail;
import com.chen.fy.niceshop.network.CommentService;
import com.chen.fy.niceshop.network.CommodityService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.network.reptile.GoodPriceCommodity;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 好价详情
 */
public class GoodPriceDetailActivity extends AppCompatActivity {

    private final int commentType = 1;

    // view
    private ImageView ivImage;
    private TextView tvName;
    private TextView tvColor;
    private TextView tvStyle;
    private TextView tvPrice;
    private TextView tvType;
    private TextView tvInfo;
    private TextView tvCommentCount;
    private TextView tvAllComment;

    // bottom
    private TextView tvHeatBox;
    private TextView tvStarBox;
    private TextView tvShareBox;
    private TextView tvCommentBox;

    // recyclerView
    private RecyclerView rvComment;
    private RecyclerView rvRecommend;

    // data
    private List<Comment> mCommentList = new ArrayList<>();
    private List<Commodity> mRecommendList = new ArrayList<>();

    // adapter
    private CommentAdapter mCommentAdapter;
    private CommodityAdapter mRecommendAdapter;

    // id
    private int commodityID;

    // url
    private String detailUrl;

    public static void start(Context context, GoodPriceCommodity commodity) {
        Intent intent = new Intent(context, GoodPriceDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RUtil.toString(R.string.good_price_commodity_key), commodity);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail);
        ShowUtils.changeStatusBarTextImgColor(this, true);

        bindView();
        setClickListener();
        initData();
    }

    private void bindView() {
        // 商品栏
        ivImage = findViewById(R.id.iv_image_detail);
        tvName = findViewById(R.id.tv_name_detail);
        tvColor = findViewById(R.id.tv_color_detail);
        tvStyle = findViewById(R.id.tv_style_detail);
        tvPrice = findViewById(R.id.tv_price_detail);
        tvType = findViewById(R.id.tv_type_detail);
        tvInfo = findViewById(R.id.tv_info_detail);

        // 评论栏
        tvCommentCount = findViewById(R.id.tv_comment_count_detail);
        tvAllComment = findViewById(R.id.tv_all_comment_detail);
        rvComment = findViewById(R.id.rv_comment_detail);

        // 相关推荐
        rvRecommend = findViewById(R.id.rv_recommend_detail);

        // bottom
        tvHeatBox = findViewById(R.id.tv_heat_box_detail);
        tvStarBox = findViewById(R.id.tv_star_box_detail);
        tvShareBox = findViewById(R.id.tv_share_box_detail);
        tvCommentBox = findViewById(R.id.tv_comment_box_detail);
    }

    private void setClickListener() {
        // 顶部栏
        /// 1返回
        findViewById(R.id.iv_back_detail).setOnClickListener(v -> finish());
        /// 2分享
        findViewById(R.id.iv_share_detail).setOnClickListener(v -> {
        });
        /// 3更多
        findViewById(R.id.iv_more_detail).setOnClickListener(v -> {
        });

        // 去逛逛
        findViewById(R.id.tv_goto_look).setOnClickListener(v -> {
        });

        // 查看全部评论
        tvAllComment.setOnClickListener(v -> AllCommentActivity.start(this, commodityID));

        // 底部栏
        /// 1热度
        tvHeatBox.setOnClickListener(v -> {
        });
        /// 2收藏
        tvStarBox.setOnClickListener(v -> {
        });
        /// 3分享
        tvShareBox.setOnClickListener(v -> {
        });
        /// 4评论
        tvCommentBox.setOnClickListener(this::clickComment);
        /// 5直达连接
        findViewById(R.id.btn_link_detail).setOnClickListener(v -> {
            gotoDetailRrl();
        });
    }

    private void gotoDetailRrl() {
        if (detailUrl != null) {
            String path = detailUrl;
            Uri uri = Uri.parse(path);
            Intent gdNav = new Intent("android.intent.action.VIEW", uri);
            gdNav.addCategory("android.intent.category.DEFAULT");
            startActivity(gdNav);
        }
    }

    private void initData() {
        // 评论信息init
        GridLayoutManager commentManager = new GridLayoutManager(this, 1);
        rvComment.setLayoutManager(commentManager);
        mCommentAdapter = new CommentAdapter(this, R.layout.comment_item);
        mCommentAdapter.setListener(position -> {
        });
        tvCommentCount.setText(String.valueOf(mCommentList.size()));

        // 推荐信息init
        GridLayoutManager recommendManager = new GridLayoutManager(this, 1);
        rvRecommend.setLayoutManager(recommendManager);
        mRecommendAdapter = new CommodityAdapter(this, R.layout.commodity_item);

        // 填充数据
        fillData();
    }

    private void fillData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            GoodPriceCommodity commodity =
                    bundle.getParcelable(RUtil.toString(R.string.good_price_commodity_key));
            fillCommodityDetailInfo(commodity);
        }
    }

    /// 填充商品详情信息
    private void fillCommodityDetailInfo(GoodPriceCommodity commodity) {
        String path = commodity.getImgUrl();
        Glide.with(this).load(path).into(ivImage);
        ivImage.setOnClickListener(v -> ShowUtils.zoomPicture(this, v, path));

        tvName.setText(commodity.getTitle());
        tvPrice.setText(commodity.getPrice());
        tvInfo.setText(commodity.getContent());
        tvCommentCount.setText(String.valueOf(0));

        // bottom
        tvHeatBox.setText(String.valueOf(0));
        tvCommentBox.setText(String.valueOf(0));

        // 跳转url
        detailUrl = commodity.getUrl();
    }

    /// 填充评论数据
    private void fillCommentData(List<Comment> comments, int count) {
        tvAllComment.setText("查看全部" + count + "条评论");
        mCommentList = comments;
        mCommentAdapter.setData(mCommentList);
        rvComment.setAdapter(mCommentAdapter);
    }

    /// 填充推荐数据
    private void fillRecommendData(List<Commodity> commodities) {
        mRecommendList = commodities;
        mRecommendAdapter.setData(mRecommendList);
        rvRecommend.setAdapter(mRecommendAdapter);
    }

    /// 评论
    private void clickComment(View v) {
        new XPopup.Builder(this)
                .autoOpenSoftInput(true)
                .asCustom(new CommentView(this, this::doComment))
                .show();
    }

    /// 进行评论
    private void doComment(String content) {
        // Authorization
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        // service
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody idBody = RequestBody.create(null, String.valueOf(commodityID).getBytes(Util.UTF_8));
        RequestBody typeBody = RequestBody.create(null, String.valueOf(1).getBytes(Util.UTF_8));
        RequestBody contentBody = RequestBody.create(null, content.getBytes(Util.UTF_8));
        map.put("id", idBody);
        map.put("type", typeBody);
        map.put("content", contentBody);
        // service
        CommentService service = ServiceCreator.create(CommentService.class);
        service.publishComment(token, map)
                .enqueue(new Callback<BaseCommentResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseCommentResponse> call
                            , @NonNull Response<BaseCommentResponse> response) {
                        BaseCommentResponse base = response.body();
                        if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                            Comment comment = base.getComment();
                            mCommentList.add(comment);
                            mCommentAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseCommentResponse> call, @NonNull Throwable t) {
                        Log.e("publishComment", "评论出错");
                    }
                });
    }
}
