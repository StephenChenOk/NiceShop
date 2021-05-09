package com.chen.fy.niceshop.main.user.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
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
import com.chen.fy.niceshop.main.user.data.model.BaseAccountResponse;
import com.chen.fy.niceshop.main.user.data.model.UserInfo;
import com.chen.fy.niceshop.network.AccountService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.UserSP;

import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPwd;

    private boolean bPwdSwitch = false;
    private ImageView ivPwdSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPwd = findViewById(R.id.et_pwd);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView btnRegister = findViewById(R.id.btn_login_to_register);
        ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        ImageView ivReturn = findViewById(R.id.iv_return_login);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        ivPwdSwitch.setOnClickListener(this);
        ivReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String account = etUsername.getText().toString();
                String pwd = etPwd.getText().toString();
                if (isValidInput(account, pwd)) {
                    login(account, pwd);
                }
                break;
            case R.id.btn_login_to_register:
                Intent intent2 = new Intent(this, RegisterActivity.class);
                startActivity(intent2);
                break;
            case R.id.iv_return_login:
                finish();
                break;
            case R.id.iv_pwd_switch:
                bPwdSwitch = !bPwdSwitch;
                if (bPwdSwitch) {
                    ivPwdSwitch.setImageResource(R.drawable.ic_visibility_on_24dp);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);   //显示密码
                } else {
                    ivPwdSwitch.setImageResource(R.drawable.ic_visibility_off_24dp);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD |
                            InputType.TYPE_CLASS_TEXT);     //隐藏密码
                    etPwd.setTypeface(Typeface.DEFAULT);    //设置字体样式
                }
                break;
        }
    }

    /// 判断是否是否合法
    private boolean isValidInput(String account, String pwd) {
        if (account.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(this, "输入不可为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login(String account, String pwd) {
        if (account.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(this, "输入不可为空", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody username = RequestBody.create(null
                , account.getBytes(Util.UTF_8));
        RequestBody password = RequestBody.create(null
                , pwd.getBytes(Util.UTF_8));

        // 发起请求
        AccountService service = ServiceCreator.create(AccountService.class);
        service.login(username, password).enqueue(new Callback<BaseAccountResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseAccountResponse> call
                    , @NonNull Response<BaseAccountResponse> response) {
                BaseAccountResponse base = response.body();
                if (base != null) {
                    if (base.getStatusCode() == RUtil.toInt(R.integer.server_success)) {
                        parseResponse(base);
                    } else {
                        Toast.makeText(LoginActivity.this, base.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "response body is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseAccountResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
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
        Toast.makeText(LoginActivity.this, RUtil.toString(R.string.login_success), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();

    }
}
