package com.chen.fy.niceshop;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

public class XWApplication extends Application {

    private static Context context;
    private final String bmobAppId = "360aa6d4420277421a357d6acfe4651b";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //初始化Bmob
        Bmob.initialize(this, bmobAppId);
    }

    public static Context getContext(){
        return context;
    }
}
