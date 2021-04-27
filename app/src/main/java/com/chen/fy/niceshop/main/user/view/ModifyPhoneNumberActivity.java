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
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.network.UserService;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.utils.UserSP;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ModifyPhoneNumberActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ModifyPhoneNumber";
    private EditText etPhoneNumber;

    private boolean isClicked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_phone_number_layout);

        ShowUtils.changeStatusBarTextImgColor(this, true);
        initView();
    }

    private void initView() {
        ImageView ivReturn = findViewById(R.id.iv_return_modify_phone);
        TextView tvSave = findViewById(R.id.tv_save_phone);
        etPhoneNumber = findViewById(R.id.et_modify_phone);

        ivReturn.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_modify_phone:
                finish();
                break;
            case R.id.tv_save_phone:
                if (!isClicked) {
                    savePhoneNumber();
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
    private void savePhoneNumber() {
        String phoneNumber = etPhoneNumber.getText().toString();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 更新服务器数据
        SharedPreferences sp = UserSP.getUserSP();
        String phoneNumber_old = sp.getString(RUtil.toString(R.string.phoneNumber_sp_key)
                , RUtil.toString(R.string.default_phoneNumber));
        if (phoneNumber.equals(phoneNumber_old)) {
            Toast.makeText(this, "电话号码已相同", Toast.LENGTH_SHORT).show();
        } else {
            updateServerData(phoneNumber);
        }
    }

    private void updateServerData(String phoneNumber) {
        int id = UserSP.getUserID();
        String userName = UserSP.getUserName();
        // json
        HashMap<String, Object> map = new HashMap<>();
        map.put(RUtil.toString(R.string.objectId_sp_key), id);
        map.put(RUtil.toString(R.string.userName_sp_key), userName);
        map.put(RUtil.toString(R.string.phoneNumber_sp_key), phoneNumber);
        final Gson gson = new Gson();
        String postData = gson.toJson(map);

        // body
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), postData);

        // request
        UserService service = ServiceCreator.create(UserService.class);
        service.update(requestBody).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call
                    , @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    SharedPreferences.Editor editor = UserSP.getUserSP().edit();
                    editor.putString(RUtil.toString(R.string.phoneNumber_sp_key), phoneNumber);
                    editor.apply();
                    finish();
                }else {
                    Log.i(TAG,"update response body is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "update failure");
            }
        });
    }

}
