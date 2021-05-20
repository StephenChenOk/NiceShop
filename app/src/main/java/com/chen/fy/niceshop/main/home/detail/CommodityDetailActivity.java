package com.chen.fy.niceshop.main.home.detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.XWApplication;
import com.chen.fy.niceshop.main.home.comment.AllCommentActivity;
import com.chen.fy.niceshop.main.home.comment.BaseCommentResponse;
import com.chen.fy.niceshop.main.home.data.adapter.CommentAdapter;
import com.chen.fy.niceshop.main.home.data.adapter.CommodityAdapter;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.main.home.data.model.Comment;
import com.chen.fy.niceshop.main.home.data.model.detail.BaseDetailResponse;
import com.chen.fy.niceshop.main.home.data.model.detail.CommodityDetail;
import com.chen.fy.niceshop.main.home.comment.CommentView;
import com.chen.fy.niceshop.main.home.detail.score.ScoreFragment;
import com.chen.fy.niceshop.main.user.collection.BaseCollectionListResponse;
import com.chen.fy.niceshop.main.user.collection.BaseCollectionResponse;
import com.chen.fy.niceshop.network.CollectionService;
import com.chen.fy.niceshop.network.CommentService;
import com.chen.fy.niceshop.network.CommodityService;
import com.chen.fy.niceshop.network.ScoreService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShareUtil;
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
 * 商品详情
 */
public class CommodityDetailActivity extends AppCompatActivity {

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
    private TextView tvScoreBox;
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

    // 当前商品id
    private int commodityID;

    // url
    private String detailUrl;
    private ScoreFragment scoreFragment;

    public static void start(Context context, int commodityID) {
        Intent intent = new Intent(context, CommodityDetailActivity.class);
        intent.putExtra(RUtil.toString(R.string.commodity_id_detail), commodityID);
        context.startActivity(intent);
    }

    // 判断当前用户是否已经收藏过此商品
    private boolean isStar = false;

    // 时间间隔
    private final int interval = 10000;
    // 定时弹窗进行评分
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            scoreFragment.show(getSupportFragmentManager(), "评分");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail);
        ShowUtils.changeStatusBarTextImgColor(this, true);

        bindView();
        setClickListener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
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
        tvScoreBox = findViewById(R.id.tv_score_detail);
        tvCommentBox = findViewById(R.id.tv_comment_box_detail);
    }

    private void setClickListener() {
        /// 返回
        findViewById(R.id.iv_back_detail).setOnClickListener(v -> finish());

        /// 分享
        findViewById(R.id.iv_share_box_detail).setOnClickListener(v -> {
            String name = tvName.getText().toString();
            ShareUtil.share(getSupportFragmentManager(), name, detailUrl);
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
            clickStar();
        });
        /// 3评分
        scoreFragment = ScoreFragment.getInstance(commodityID, this::submitScore);
        tvScoreBox.setOnClickListener(v -> {
            scoreFragment.show(getSupportFragmentManager(), "评分");
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
            String path = "http:" + detailUrl;
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
        tvCommentCount.setText(String.valueOf(mCommentList.size()));

        // 推荐信息init
        GridLayoutManager recommendManager = new GridLayoutManager(this, 1);
        rvRecommend.setLayoutManager(recommendManager);
        mRecommendAdapter = new CommodityAdapter(this, R.layout.commodity_item);
        mRecommendAdapter.setListener(id -> {
            CommodityDetailActivity.start(this, id);
        });

        // 填充数据
        fillData();
        // 设置收藏状态
        setStarState();
        // 历史浏览记录存储
        storeHistory();

        // 10s后弹窗提示评分
        mHandler.sendEmptyMessageDelayed(0, interval);
    }


    private void fillData() {
        commodityID = getCommodityID();
        CommodityService service = ServiceCreator.create(CommodityService.class);
        service.getCommodityDetail(commodityID).enqueue(new Callback<BaseDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseDetailResponse> call
                    , @NonNull Response<BaseDetailResponse> response) {
                BaseDetailResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    fillCommodityDetailInfo(base.getCommodity());
                    fillCommentData(base.getComments(), base.getCount());
                    fillRecommendData(base.getRecommendCommodity());
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseDetailResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
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

    /// 填充商品详情信息
    private void fillCommodityDetailInfo(CommodityDetail detail) {
        String picUrl = "http:" + detail.getPic_url();
        Glide.with(this).load(picUrl).into(ivImage);
        ivImage.setOnClickListener(v -> ShowUtils.zoomPicture(this, v, picUrl));

        tvName.setText(detail.getRaw_title());
        tvPrice.setText(String.valueOf(detail.getView_price()));
        tvType.setText(detail.getPlatform());
        tvInfo.setText(detail.getTitle());
        tvColor.setText(detail.getNick());
        tvStyle.setText(detail.getCategory());
        tvCommentCount.setText(String.valueOf(detail.getComment_num()));

        // bottom
        tvHeatBox.setText("热度:" + detail.getHot());

        // 跳转url
        detailUrl = detail.getDetail_url();
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

    /// 存储浏览记录
    private void storeHistory() {
        // token
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        String spName = getResources().getString(R.string.history_sp_name);
        // 获取当前浏览记录
        SharedPreferences historySP = getSharedPreferences(spName, MODE_PRIVATE);
        String history = historySP.getString(token, "");
        if (!history.isEmpty()) {
            String[] historys = history.split(",");
            // 匹配是否已经存储过
            for (String s : historys) {
                // 存储过，则直接跳过
                if (s.equals(String.valueOf(commodityID))) {
                    return;
                }
            }
            history = history + "," + commodityID;
        } else {
            history = String.valueOf(commodityID);
        }
        // 存储
        SharedPreferences.Editor editor = getSharedPreferences(spName, MODE_PRIVATE).edit();
        editor.putString(token, history);
        editor.apply();
    }

    /// 设置收藏状态
    private void setStarState() {
        // token
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        CollectionService service = ServiceCreator.create(CollectionService.class);
        service.getCollectionList(token).enqueue(new Callback<BaseCollectionListResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCollectionListResponse> call
                    , @NonNull Response<BaseCollectionListResponse> response) {
                BaseCollectionListResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    List<Commodity> list = base.getList();
                    if (list == null) {
                        return;
                    }
                    for (Commodity commodity : list) {
                        if (commodityID == commodity.getId()) {
                            isStar = true;
                            // drawable
                            Drawable drawable = getResources().getDrawable(R.drawable.ic_star_red_24dp);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            tvStarBox.setCompoundDrawables(null, drawable, null, null);
                            // count
                            String countStr = tvStarBox.getText().toString().substring(3);
                            int count = Integer.valueOf(countStr) + 1;
                            tvStarBox.setText("收藏:" + count);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseCollectionListResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /// 点击收藏
    private void clickStar() {
        isStar = !isStar;
        changeStarState();
    }

    /// 改变收藏状态
    private void changeStarState() {
        Drawable drawable;
        if (isStar) {
            drawable = getResources().getDrawable(R.drawable.ic_star_red_24dp);
            doStar();
        } else {
            drawable = getResources().getDrawable(R.drawable.ic_star_border_black_24dp);
            cancelStar();
        }
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvStarBox.setCompoundDrawables(null, drawable, null, null);
    }

    /// 收藏
    private void doStar() {
        // token
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        RequestBody idBody = RequestBody.create(
                null, String.valueOf(commodityID).getBytes(Util.UTF_8));
        CollectionService service = ServiceCreator.create(CollectionService.class);
        service.doCollection(token, idBody).enqueue(new Callback<BaseCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCollectionResponse> call
                    , @NonNull Response<BaseCollectionResponse> response) {
                BaseCollectionResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    Toast.makeText(CommodityDetailActivity.this
                            , base.getMsg(), Toast.LENGTH_SHORT).show();
                    String countStr = tvStarBox.getText().toString().substring(3);
                    int count = Integer.valueOf(countStr) + 1;
                    tvStarBox.setText("收藏:" + count);
                } else {
                    Toast.makeText(CommodityDetailActivity.this
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

    /// 取消收藏
    private void cancelStar() {
        // token
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        RequestBody idBody = RequestBody.create(
                null, String.valueOf(commodityID).getBytes(Util.UTF_8));
        CollectionService service = ServiceCreator.create(CollectionService.class);
        service.cancelCollection(token, idBody).enqueue(new Callback<BaseCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCollectionResponse> call
                    , @NonNull Response<BaseCollectionResponse> response) {
                BaseCollectionResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    Toast.makeText(CommodityDetailActivity.this
                            , base.getMsg(), Toast.LENGTH_SHORT).show();
                    String countStr = tvStarBox.getText().toString().substring(3);
                    int count = Integer.valueOf(countStr) - 1;
                    tvStarBox.setText("收藏:" + count);
                } else {
                    Toast.makeText(CommodityDetailActivity.this
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
                            int commentCount = Integer.valueOf(tvCommentCount.getText().toString()) + 1;
                            tvCommentCount.setText(String.valueOf(commentCount));
                            tvAllComment.setText("查看全部" + commentCount + "条评论");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseCommentResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    /// 点击评分
    private void submitScore(float scoreNumber) {
        // 清除消息
        mHandler.removeCallbacksAndMessages(null);
        // token
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
        RequestBody idBody = RequestBody.create(
                null, String.valueOf(commodityID).getBytes(Util.UTF_8));
        RequestBody scoreBody = RequestBody.create(
                null, String.valueOf(scoreNumber).getBytes(Util.UTF_8));
        ScoreService service = ServiceCreator.create(ScoreService.class);
        service.doScore(token, idBody, scoreBody).enqueue(new Callback<BaseCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseCollectionResponse> call
                    , @NonNull Response<BaseCollectionResponse> response) {
                BaseCollectionResponse base = response.body();
                if (base != null && base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                    Toast.makeText(XWApplication.getContext()
                            , base.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    if (base != null) {
                        Toast.makeText(XWApplication.getContext()
                                , base.getMsg(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(XWApplication.getContext()
                                , "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }
                scoreFragment.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<BaseCollectionResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
                scoreFragment.dismiss();
            }
        });
    }
}
