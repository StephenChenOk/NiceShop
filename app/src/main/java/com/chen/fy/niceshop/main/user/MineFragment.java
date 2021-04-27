package com.chen.fy.niceshop.main.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.look.publish.PublishActivity;
import com.chen.fy.niceshop.main.user.collection.CollectionActivity;
import com.chen.fy.niceshop.main.user.view.LoginActivity;
import com.chen.fy.niceshop.main.user.view.MyInfoActivity;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;
import com.lxj.xpopup.XPopup;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends Fragment implements View.OnClickListener {

    private final int LOGIN_REQUEST_CODE = 1;
    private final int MY_INFO_REQUEST_CODE = 2;
    public static final int RESULT_OUT_LOGIN = 3;
    public static final int RESULT_RETURN_LOGIN = 4;

    private ViewStub vsLoggedIn;
    private ViewStub vsNotLoggedIn;

    private CircleImageView ivHeadIcon;
    private TextView tvUsername;
    private TextView tvUserInfo;

    private TextView tvHeadlineNum;
    private TextView tvLikeNum;
    private TextView tvFansNum;
    private TextView tvAttentionNum;

    private View mView;

    // 判断是否登入账号
    private String token;

    private boolean isInitLoggedInView = false;
    private boolean isInitNotLoggedInView = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.mine_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化View
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initLoginState();
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
        }
    }

    private void initView() {
        vsNotLoggedIn = mView.findViewById(R.id.vs_not_logged_in);
        vsLoggedIn = mView.findViewById(R.id.vs_logged_in);
        mView.findViewById(R.id.btn_publish_mine).setOnClickListener(this);
    }

    /// 初始化未登录界面
    private void initNotLoggedInVS() {
        vsNotLoggedIn.inflate();

        mView.findViewById(R.id.btn_login_mine).setOnClickListener(this);
    }

    /// 初始化已登录界面
    private void initLoggedInVS() {
        vsLoggedIn.inflate();

        ivHeadIcon = mView.findViewById(R.id.iv_head_icon_mine);
        tvUsername = mView.findViewById(R.id.tv_username_mine);
        tvUserInfo = mView.findViewById(R.id.tv_personal_home);

        tvHeadlineNum = mView.findViewById(R.id.tv_headline_num);
        tvLikeNum = mView.findViewById(R.id.tv_like_num);
        tvFansNum = mView.findViewById(R.id.tv_fans_num);
        tvAttentionNum = mView.findViewById(R.id.tv_attention_num);

        ivHeadIcon.setOnClickListener(this);
        tvUsername.setOnClickListener(this);
        tvUserInfo.setOnClickListener(this);
        mView.findViewById(R.id.ll_star_box).setOnClickListener(v -> {
            CollectionActivity.start(getContext());
        });
    }

    /// 初始化登录状态
    private void initLoginState() {
        SharedPreferences preferences = UserSP.getUserSP();
        String token = preferences.getString(RUtil.toString(R.string.token), "");
        if (!token.isEmpty()) {  //已经登录
            prepareLoggedInView();
        } else {
            prepareNotLoggedInView();
        }
    }

    /// 若未登录，则准备未登录界面
    private void prepareNotLoggedInView() {
        if (!isInitNotLoggedInView) {
            initNotLoggedInVS();
            isInitNotLoggedInView = true;
        }
        vsNotLoggedIn.setVisibility(View.VISIBLE);
        vsLoggedIn.setVisibility(View.GONE);
    }

    /// 若已登录，则准备已登录界面
    private void prepareLoggedInView() {
        if (!isInitLoggedInView) {
            initLoggedInVS();
            isInitLoggedInView = true;
        }
        vsLoggedIn.setVisibility(View.VISIBLE);
        vsNotLoggedIn.setVisibility(View.GONE);
        fillLoggedInView();
    }

    /// 填充数据到已登录界面
    private void fillLoggedInView() {
        SharedPreferences sp = UserSP.getUserSP();
        String userName = sp.getString(RUtil.toString(R.string.userName_sp_key),
                RUtil.toString(R.string.default_userName));

        int headlineNum = getHeadlineNum(sp);
        int likeNum = getLikeNum();
        int fansNum = getFansNum();
        int attentionNum = getAttentionNum();

        tvUsername.setText(userName);
        tvUserInfo.setText("个人主页>");
        tvHeadlineNum.setText(String.valueOf(headlineNum));
        tvLikeNum.setText(String.valueOf(likeNum));
        tvFansNum.setText(String.valueOf(fansNum));
        tvAttentionNum.setText(String.valueOf(attentionNum));

        setHeadIcon();
    }

    private int getHeadlineNum(SharedPreferences sp) {
        return 5;
    }

    private int getLikeNum() {
        return 3;
    }

    private int getFansNum() {
        return 1;
    }

    private int getAttentionNum() {
        return 2;
    }

    private void setHeadIcon() {
        SharedPreferences preferences = UserSP.getUserSP();
        String headUrl = preferences.getString(RUtil.toString(R.string.headUrl_sp_key), "");

        if (!headUrl.equals("")) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.not_logged_head_icon)//图片加载出来前，显示的图片
                    .fallback(R.drawable.not_logged_head_icon)  //url为空的时候,显示的图片
                    .error(R.drawable.not_logged_head_icon);    //图片加载失败后，显示的图片

            Glide.with(Objects.requireNonNull(getActivity())).load(headUrl).apply(options).into(ivHeadIcon);
        } else {
            Glide.with(Objects.requireNonNull(getActivity())).load(R.drawable.not_logged_head_icon).into(ivHeadIcon);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_mine:
                jumpActivity(LOGIN_REQUEST_CODE);
                break;
            case R.id.iv_head_icon_mine:
            case R.id.tv_username_mine:
            case R.id.tv_personal_home:
                jumpActivity(MY_INFO_REQUEST_CODE);
                break;
            case R.id.btn_publish_mine:
                PublishActivity.start(getContext());
                break;
        }
    }

    /**
     * 根据登录与否跳转不同的界面
     */
    private void jumpActivity(int code) {
        switch (code) {
            case LOGIN_REQUEST_CODE:
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent1, LOGIN_REQUEST_CODE);
                break;
            case MY_INFO_REQUEST_CODE:
                Intent intent2 = new Intent(getActivity(), MyInfoActivity.class);
                startActivityForResult(intent2, MY_INFO_REQUEST_CODE);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case LOGIN_REQUEST_CODE:
                break;
            case MY_INFO_REQUEST_CODE:
                switch (resultCode) {
                    case RESULT_RETURN_LOGIN:  // 返回
                        boolean isChange = Objects.requireNonNull(data).getBooleanExtra(getResources().
                                getString(R.string.head_icon_is_change), false);
                        if (isChange) {
                            setHeadIcon();
                        }
                        break;
                    case RESULT_OUT_LOGIN:     // 退出账号
                        Glide.with(Objects.requireNonNull(getContext()))
                                .load(R.drawable.not_logged_head_icon).into(ivHeadIcon);
                        break;
                }
                break;
        }
    }
}
