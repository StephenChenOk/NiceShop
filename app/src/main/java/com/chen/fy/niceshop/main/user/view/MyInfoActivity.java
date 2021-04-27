package com.chen.fy.niceshop.main.user.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.user.MineFragment;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;
import com.lxj.xpopup.XPopup;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.model.TResult;

import java.io.File;


public class MyInfoActivity extends TakePhotoActivity implements View.OnClickListener {

    private static final String TAG = "MyInfoActivity";

    private ImageView ivHeadIcon;
    private TextView tvUsername;
    private TextView tvAccount;
    private TextView tvPhoneNumber;

    private TakePhoto mTakePhoto;
    private Uri mUri;

    // 第一次进入
    private boolean isFirst = true;
    // 头像是否已经发生改变
    private boolean isChange = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info_layout);

        ShowUtils.changeStatusBarTextImgColor(this, true);
        initView();
        initData();
        configTakePhoto();
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvUsername.setText(UserSP.getUserName());
        tvPhoneNumber.setText(UserSP.getPhoneNumber());
    }

    private void initView() {
        ImageView ivReturn = findViewById(R.id.iv_return_my_info);
        LinearLayout headIconBox = findViewById(R.id.ll_head_icon_box_my_info);
        LinearLayout usernameBox = findViewById(R.id.ll_username_box_my_info);
        LinearLayout accountBox = findViewById(R.id.ll_account_box_my_info);
        LinearLayout phoneBox = findViewById(R.id.ll_phone_box_my_info);
        Button btnOutLogin = findViewById(R.id.btn_out_login_my_info);
        ivHeadIcon = findViewById(R.id.iv_head_icon_my_info);
        tvUsername = findViewById(R.id.tv_username_my_info);
        tvAccount = findViewById(R.id.tv_account_my_info);
        tvPhoneNumber = findViewById(R.id.tv_phone_number_my_info);

        ivReturn.setOnClickListener(this);
        headIconBox.setOnClickListener(this);
        usernameBox.setOnClickListener(this);
        accountBox.setOnClickListener(this);
        phoneBox.setOnClickListener(this);
        btnOutLogin.setOnClickListener(this);
    }

    private void initData() {

        SharedPreferences sp = UserSP.getUserSP();
        String headIconURL = sp.getString(RUtil.toString(R.string.head_icon), "");

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.not_logged_head_icon)//图片加载出来前，显示的图片
                .fallback(R.drawable.not_logged_head_icon)  //url为空的时候,显示的图片
                .error(R.drawable.not_logged_head_icon);    //图片加载失败后，显示的图片

        if (isFirst) {
            Glide.with(this).load(headIconURL).apply(options).into(ivHeadIcon);
            isFirst = false;
        }
    }

    private void configTakePhoto() {
        if (mTakePhoto == null) {
            mTakePhoto = getTakePhoto();

            //图片文件名
            File file = new File(this.getExternalFilesDir(null)
                    , RUtil.toString(R.string.headURL_file));
            mUri = Uri.fromFile(file);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(getResources().getString(R.string.head_icon_is_change), isChange);
        setResult(MineFragment.RESULT_RETURN_LOGIN, intent);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_my_info:        // 返回上一页
                Intent intent = new Intent();
                intent.putExtra(getResources().getString(R.string.head_icon_is_change), isChange);
                setResult(MineFragment.RESULT_RETURN_LOGIN, intent);
                finish();
                break;
            case R.id.ll_head_icon_box_my_info:
                modifyHeadIcon();
                break;
            case R.id.ll_username_box_my_info:
                Intent nameIntent = new Intent(this, ModifyUsernameActivity.class);
                startActivity(nameIntent);
                break;
            case R.id.ll_account_box_my_info:

                break;
            case R.id.ll_phone_box_my_info:
                Intent phoneNumIntent = new Intent(this, ModifyPhoneNumberActivity.class);
                startActivity(phoneNumIntent);
                break;
            case R.id.btn_out_login_my_info:   // 退出登录
                clearLoginState();
                setResult(MineFragment.RESULT_OUT_LOGIN);
                finish();
                break;

        }
    }

    private void modifyHeadIcon() {
        new XPopup.Builder(this)
                .asCenterList("", new String[]{"拍照", "从相册选择"},
                        (position, text) -> {
                            switch (position) {
                                case 0:         //拍照
                                    mTakePhoto.onPickFromCapture(mUri);
                                    break;
                                case 1:         //相册
                                    mTakePhoto.onPickMultiple(1);
                                    break;
                            }
                        })
                .show();
    }

    /// 清除登录状态
    private void clearLoginState() {
        SharedPreferences.Editor editorUserInfo = getSharedPreferences(
                RUtil.toString(R.string.userInfo_sp_name), MODE_PRIVATE).edit();
        editorUserInfo.clear();
        editorUserInfo.apply();
    }

    ///  会与onActivityResult造成冲突
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

//        postHeadIcon(result.getImage().getOriginalPath());
    }

    /**
     * 修改头像
     */
//    private void postHeadIcon(String path) {
//        BasePopupView loading = new XPopup.Builder(this).asLoading("更新中").show();
//        int id = UserSP.getUserID();
//        CommodityService service = ServiceCreator.create(CommodityService.class);
//        RequestBody userID = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id));
//        RequestBody file = RequestBody.create(MediaType.parse("image/jpg"), new File(path));
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file"
//                , "head_icon.jpg", file);
//        service.postHeadIcon(userID, filePart).enqueue(new Callback<HeadIconResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<HeadIconResponse> call
//                    , @NonNull Response<HeadIconResponse> response) {
//                loading.dismiss();
//                HeadIconResponse response1 = response.body();
//                if (response1 != null) {
//                    isChange = true;
//
//                    String msg = response1.getMsg();
//                    String newHeadUrl = response1.getNewHeadUrl();
//                    SharedPreferences.Editor editor = getSharedPreferences(
//                            RUtil.toString(R.string.userInfo_sp_name), MODE_PRIVATE).edit();
//                    editor.putString(RUtil.toString(R.string.headUrl_sp_key), "http://" + newHeadUrl);
//                    editor.apply();
//
//                    Glide.with(MyInfoActivity.this).load(path).into(ivHeadIcon);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<HeadIconResponse> call, @NonNull Throwable t) {
//                loading.dismiss();
//                Log.i(TAG, "postHeadIcon Failure");
//            }
//        });
//    }
}
