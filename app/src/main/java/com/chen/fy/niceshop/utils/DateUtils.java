package com.chen.fy.niceshop.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 时间格式转化( 年.月.日 -> 毫秒数)
     */
    public static long dateToLong(String time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //格式需要一样
        Date date = null;
        try {
            date = dateFormat.parse(time);     //解析
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 时间格式转化1( 毫秒数 -> 年.月.日 )
     */
    public static String dateToString(long time) {
        Date date = new Date(time);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

}
