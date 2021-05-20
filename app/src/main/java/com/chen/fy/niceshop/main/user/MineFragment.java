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
import com.chen.fy.niceshop.main.dynamic.BaseGiveResponse;
import com.chen.fy.niceshop.main.dynamic.DynamicActivity;
import com.chen.fy.niceshop.main.dynamic.GiveActivity;
import com.chen.fy.niceshop.main.look.publish.PublishActivity;
import com.chen.fy.niceshop.main.user.collection.CollectionActivity;
import com.chen.fy.niceshop.main.user.data.model.BaseUserInfoResponse;
import com.chen.fy.niceshop.main.user.data.model.UserInfo;
import com.chen.fy.niceshop.main.user.history.HistoryActivity;
import com.chen.fy.niceshop.main.user.view.LoginActivity;
import com.chen.fy.niceshop.main.user.view.MyInfoActivity;
import com.chen.fy.niceshop.network.PersonService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;
import com.lxj.xpopup.XPopup;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private TextView tvDynamicNum;
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

        tvDynamicNum = mView.findViewById(R.id.tv_dynamic_num);
        tvLikeNum = mView.findViewById(R.id.tv_like_num);
        tvFansNum = mView.findViewById(R.id.tv_fans_num);
        tvAttentionNum = mView.findViewById(R.id.tv_attention_num);

        ivHeadIcon.setOnClickListener(this);
        tvUsername.setOnClickListener(this);
        tvUserInfo.setOnClickListener(this);
        mView.findViewById(R.id.ll_star_box).setOnClickListener(v -> {
            CollectionActivity.start(getContext());
        });
        mView.findViewById(R.id.ll_history_box).setOnClickListener(v -> {
            HistoryActivity.start(getContext());
        });
        // 动态
        mView.findViewById(R.id.ll_dynamic_box).setOnClickListener(v -> {
            DynamicActivity.start(getContext());
        });
        // 获赞
        mView.findViewById(R.id.ll_give_box).setOnClickListener(v -> {
            GiveActivity.start(getContext());
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
        String userName = sp.getString(RUtil.toString(R.string.nickname),
                RUtil.toString(R.string.default_userName));

        tvUsername.setText(userName);
        tvUserInfo.setText("个人主页>");

        setHeadIcon();
        getUserInfo();
    }

    private void getUserInfo(){
        String token = UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");

        PersonService service = ServiceCreator.create(PersonService.class);
        service.userInfo(token).enqueue(new Callback<BaseUserInfoResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseUserInfoResponse> call
                    , @NonNull Response<BaseUserInfoResponse> response) {
                BaseUserInfoResponse base = response.body();
                if (base != null) {
                    if(base.getStatus() == RUtil.toInt(R.integer.server_success)) {
                        UserInfo userInfo = base.getUserInfo();
                        setUserInfo(userInfo);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseUserInfoResponse> call
                    , @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setUserInfo(UserInfo userInfo){
        tvDynamicNum.setText(String.valueOf(userInfo.getComment_num()));
        tvLikeNum.setText(String.valueOf(userInfo.getGive_num()));
        tvFansNum.setText(String.valueOf(userInfo.getFans_num()));
        tvAttentionNum.setText(String.valueOf(userInfo.getFollow_num()));
    }

    private void setHeadIcon() {
        SharedPreferences preferences = UserSP.getUserSP();
        String headUrl = preferences.getString(RUtil.toString(R.string.head_icon), "");

        Glide.with(Objects.requireNonNull(getActivity())).load(headUrl).into(ivHeadIcon);
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
