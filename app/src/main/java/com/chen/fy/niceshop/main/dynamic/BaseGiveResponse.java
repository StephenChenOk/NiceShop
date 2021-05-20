package com.chen.fy.niceshop.main.dynamic;

import com.chen.fy.niceshop.main.look.data.model.dynamic.Dynamic;

import java.util.List;

public class BaseGiveResponse {
    private int code;
    private List<Dynamic> giveList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Dynamic> getGiveList() {
        return giveList;
    }

    public void setGiveList(List<Dynamic> giveList) {
        this.giveList = giveList;
    }
}
