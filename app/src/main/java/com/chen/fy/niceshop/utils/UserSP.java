package com.chen.fy.niceshop.utils;

import android.content.SharedPreferences;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.XWApplication;

import static android.content.Context.MODE_PRIVATE;

public class UserSP {

    private static SharedPreferences preferences;

    public static SharedPreferences getUserSP() {
        if (preferences == null) {
            String spName = XWApplication.getContext().getResources().getString(R.string.userInfo_sp_name);
            preferences = XWApplication.getContext().getSharedPreferences(spName, MODE_PRIVATE);
        }
        return preferences;
    }

    /// 得到当前账号的用户名
    public static String getUserName() {
        return UserSP.getUserSP().getString(RUtil.toString(R.string.nickname), "");
    }

    /// 得到当前账号的id
    public static int getUserID() {
        return UserSP.getUserSP().getInt(RUtil.toString(R.string.objectId_sp_key)
                , Integer.valueOf(RUtil.toString(R.string.not_login)));
    }

    /// 得到当前账号的id
    public static String getToken() {
        return UserSP.getUserSP().getString(RUtil.toString(R.string.token), "");
    }

    /// 得到当前账号的id
    public static String getPhoneNumber() {
        return UserSP.getUserSP().getString(RUtil.toString(R.string.phoneNumber_sp_key)
                , RUtil.toString(R.string.default_phoneNumber));
    }
}
