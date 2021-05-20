package com.chen.fy.niceshop.main.user.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.dynamic.BasePersonResponse;
import com.chen.fy.niceshop.network.PersonService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyUsernameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ModifyUsername";
    private EditText etUsername;

    private boolean isClicked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_username_layout);

        ShowUtils.changeStatusBarTextImgColor(this, true);
        initView();
    }

    private void initView() {
        ImageView ivReturn = findViewById(R.id.iv_return_modify_username);
        TextView tvSave = findViewById(R.id.tv_save_username);
        etUsername = findViewById(R.id.et_modify_name);

        ivReturn.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_modify_username:
                finish();
                break;
            case R.id.tv_save_username:
                if (!isClicked) {
                    saveUsername();
                    isClicked = true;
                } else {
                    ShowUtils.toast("已保存");
                }
                break;
        }
    }

    /**
     * 更改昵称
     */
    private void saveUsername() {
        String userName = etUsername.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //1 更新服务器数据
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        String userName_old = sharedPreferences.getString("userName", "");
        if (userName.equals(userName_old)) {
            Toast.makeText(this, "昵称已相同", Toast.LENGTH_SHORT).show();
        } else {
            updateServerData(userName);
        }
    }

    private void updateServerData(String nickname) {

        String token = UserSP.getToken();

        // request
        PersonService service = ServiceCreator.create(PersonService.class);
        service.changeNickname(token, nickname).enqueue(new Callback<BasePersonResponse>() {
            @Override
            public void onResponse(@NonNull Call<BasePersonResponse> call
                    , @NonNull Response<BasePersonResponse> response) {
                BasePersonResponse base = response.body();
                if (base != null) {
                    int code = base.getCode();
                    if (code == RUtil.toInt(R.integer.server_success)) {
                        resetNickname(base.getNickname());
                    }
                    Toast.makeText(ModifyUsernameActivity.this, base.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyUsernameActivity.this, "更换昵称失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BasePersonResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void resetNickname(String nickname) {
        String newNickname = nickname.replace("\"", "");

        SharedPreferences.Editor editor = UserSP.getUserSP().edit();
        editor.putString(RUtil.toString(R.string.nickname), newNickname);
        editor.apply();
        finish();
    }
}
