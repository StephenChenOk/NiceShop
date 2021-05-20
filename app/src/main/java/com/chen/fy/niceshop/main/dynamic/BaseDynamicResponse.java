package com.chen.fy.niceshop.main.dynamic;

import com.chen.fy.niceshop.main.look.data.model.dynamic.Dynamic;

import java.util.List;

public class BaseDynamicResponse {
    private int code;
    private List<Dynamic> dynamicList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Dynamic> getDynamicList() {
        return dynamicList;
    }

    public void setDynamicList(List<Dynamic> dynamicList) {
        this.dynamicList = dynamicList;
    }
}
