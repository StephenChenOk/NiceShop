package com.chen.fy.niceshop.main.look.data.model.dynamic;

import java.util.List;

public class BaseDynamicResponse {
    private int statusCode;
    private List<Dynamic> dynamics;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Dynamic> getDynamics() {
        return dynamics;
    }

    public void setDynamics(List<Dynamic> dynamics) {
        this.dynamics = dynamics;
    }
}
