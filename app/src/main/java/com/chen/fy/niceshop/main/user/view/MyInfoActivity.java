package com.chen.fy.niceshop.main.user.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.dynamic.BasePersonResponse;
import com.chen.fy.niceshop.main.user.MineFragment;
import com.chen.fy.niceshop.main.user.data.model.BaseUserResponse;
import com.chen.fy.niceshop.network.PersonService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.model.TResult;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyInfoActivity extends TakePhotoActivity implements View.OnClickListener {

    private static final String TAG = "MyInfoActivity";

    private ImageView ivHeadIcon;
    private TextView tvUsername;

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
    }

    private void initView() {
        ImageView ivReturn = findViewById(R.id.iv_return_my_info);
        LinearLayout headIconBox = findViewById(R.id.ll_head_icon_box_my_info);
        LinearLayout usernameBox = findViewById(R.id.ll_username_box_my_info);
        Button btnOutLogin = findViewById(R.id.btn_out_login_my_info);
        ivHeadIcon = findViewById(R.id.iv_head_icon_my_info);
        tvUsername = findViewById(R.id.tv_username_my_info);

        ivReturn.setOnClickListener(this);
        headIconBox.setOnClickListener(this);
        usernameBox.setOnClickListener(this);
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
        String imgPath = result.getImage().getOriginalPath();
        // 更新头像
        updateHeadIcon(imgPath);
    }

    private void updateHeadIcon(String imgPath) {
        /// 上传
        final BasePopupView loading = new XPopup.Builder(this)
                .asLoading("上传中...")
                .show();
        loading.setOnTouchListener((v, event) -> true);

        String token = UserSP.getToken();
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), new File(imgPath));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("img"
                , RUtil.toString(R.string.photo_name), fileBody);

        PersonService service = ServiceCreator.create(PersonService.class);
        service.changeImg(token, filePart).enqueue(new Callback<BasePersonResponse>() {
            @Override
            public void onResponse(@NonNull Call<BasePersonResponse> call
                    , @NonNull Response<BasePersonResponse> response) {
                BasePersonResponse base = response.body();
                if (base != null) {
                    int code = base.getCode();
                    if (code == RUtil.toInt(R.integer.server_success)) {
                        resetHeadIcon(base.getImg());
                    }
                    Toast.makeText(MyInfoActivity.this,base.getMsg(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyInfoActivity.this,"更换头像失败",Toast.LENGTH_SHORT).show();
                }
                // 上传成功
                loading.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<BasePersonResponse> call, @NonNull Throwable t) {
                loading.dismiss();
                t.printStackTrace();
            }
        });
    }

    /// 重置头像
    private void resetHeadIcon(String imgPath) {
        // 加载新头像
        Glide.with(MyInfoActivity.this).load(imgPath).into(ivHeadIcon);
        // 保存头像
        SharedPreferences.Editor editor = getSharedPreferences(
                RUtil.toString(R.string.userInfo_sp_name), MODE_PRIVATE).edit();
        editor.putString(RUtil.toString(R.string.head_icon), imgPath);
        editor.apply();
    }
}
