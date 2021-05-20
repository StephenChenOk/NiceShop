package com.chen.fy.niceshop.main.user.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.guide.GuideActivity;
import com.chen.fy.niceshop.main.user.data.model.BaseAccountResponse;
import com.chen.fy.niceshop.main.user.data.model.UserInfo;
import com.chen.fy.niceshop.network.AccountService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity.Log";
    private EditText etUsername;
    private EditText etPwd;
    private EditText etPwd2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        initView();

    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPwd = findViewById(R.id.et_pwd);
        etPwd2 = findViewById(R.id.et_pwd_again);
        Button btnRegister = findViewById(R.id.btn_register);
        ImageView ivReturn = findViewById(R.id.iv_return_register);
        TextView tvReturn = findViewById(R.id.tv_return);

        btnRegister.setOnClickListener(this);
        ivReturn.setOnClickListener(this);
        tvReturn.setOnClickListener(this);

        setEditTextInhibitInputSpace(etUsername);
        setEditTextInhibitInputSpace(etPwd);
        setEditTextInhibitInputSpace(etPwd2);
    }

    /**
     * 禁止EditText输入空格
     */
    private void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" "))
                    return "";
                else
                    return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                String account = etUsername.getText().toString();
                String pwd1 = etPwd.getText().toString();
                String pwd2 = etPwd2.getText().toString();
                if (isValidInput(account, pwd1, pwd2)) {
                    register(account, pwd1);
                }
                break;
            case R.id.iv_return_register:
            case R.id.tv_return:
                finish();
                break;
        }
    }

    /// 判断是否是否合法
    private boolean isValidInput(String account, String pwd1, String pwd2) {
        if (account.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty()) {
            Toast.makeText(this, "输入不可为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!pwd1.equals(pwd2)) {
            Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void register(String account, String pwd) {
        final BasePopupView loadingPopup = new XPopup.Builder(this)
                .asLoading("注册中")
                .show();
        loadingPopup.setOnTouchListener((v, event) -> true);

        RequestBody username = RequestBody.create(null
                , account.getBytes(Util.UTF_8));
        RequestBody password = RequestBody.create(null
                , pwd.getBytes(Util.UTF_8));

        // 发起请求
        AccountService service = ServiceCreator.create(AccountService.class);
        service.register(username, password).enqueue(new Callback<BaseAccountResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseAccountResponse> call
                    , @NonNull Response<BaseAccountResponse> response) {
                BaseAccountResponse base = response.body();
                if (base != null) {
                    if (base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                        parseResponse(base);
                        GuideActivity.start(RegisterActivity.this, base.getJwt());
                    } else {
                        Toast.makeText(RegisterActivity.this, base.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "response body is null");
                }
                loadingPopup.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<BaseAccountResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                loadingPopup.dismiss();
            }
        });
    }

    private void parseResponse(BaseAccountResponse base) {
        UserInfo userInfo = base.getUserInfo();
        SharedPreferences.Editor editor = getSharedPreferences(
                RUtil.toString(R.string.userInfo_sp_name), MODE_PRIVATE).edit();
        // 存储token
        editor.putString(RUtil.toString(R.string.token), base.getJwt());
        // 存储用户名
        editor.putString(RUtil.toString(R.string.nickname), userInfo.getNickname());
        // 存储头像
        editor.putString(RUtil.toString(R.string.head_icon), userInfo.getImg());

        editor.apply();
        Toast.makeText(this, RUtil.toString(R.string.register_success), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();

    }
}
