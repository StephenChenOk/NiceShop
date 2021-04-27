package com.chen.fy.niceshop.utils;
import com.chen.fy.niceshop.XWApplication;

public class RUtil {

    public static String toString(int key){
        return XWApplication.getContext().getResources().getString(key);
    }

    public static int toInt(int key){
        return XWApplication.getContext().getResources().getInteger(key);
    }
}
