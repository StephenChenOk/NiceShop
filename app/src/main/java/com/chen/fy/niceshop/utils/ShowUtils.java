package com.chen.fy.niceshop.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chen.fy.niceshop.XWApplication;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupImageLoader;

import java.io.File;


public class ShowUtils {

    public static void toast(String msg) {
        Toast.makeText(XWApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 界面设置状态栏字体颜色
     */
    public static void changeStatusBarTextImgColor(Activity activity, boolean isBlack) {
        if (isBlack) {
            //设置状态栏黑色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //恢复状态栏白色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 高亮显示
     */
    public static SpannableStringBuilder highLightText(String str, String key) {
        int start = str.toLowerCase().indexOf(key.toLowerCase());
        int end = start + key.length();

        SpannableStringBuilder sb = new SpannableStringBuilder(str);
        sb.setSpan(
                new ForegroundColorSpan(Color.RED), //设置高亮的颜色
                start,   //起始坐标
                end,     //终止坐标
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE   //旗帜  一般不用改变
        );
        return sb;
    }

    /// 放大图片
    public static void zoomPicture(Context context, View view, Object url) {
        new XPopup.Builder(context).asImageViewer((ImageView) view, url, new XPopupImageLoader() {
            @Override
            public void loadImage(int position, @NonNull Object uri, @NonNull ImageView imageView) {
                Glide.with(context).load(uri).into(imageView);
            }

            @Override
            public File getImageFile(@NonNull Context context, @NonNull Object uri) {
                try {
                    return Glide.with(context).downloadOnly().load(uri).submit().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).show();
    }

    /// 弹出软键盘
    public static void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);

    }

    /// 收起软键盘
    public static void hideSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.clearFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }
}
