package com.semioe.healthcareup.http.responsebeans;

import android.util.Log;

import com.semioe.healthcareup.http.httputils.GsonUtils;

public class BaseResponseBean extends ResponseBean {
    private boolean success = false;
    private String result;

    public boolean isSuccess() {
        return success;
    }

    public String getResult() {
        return result;
    }

    public BaseResponseBean(String response) {
        super(response);
        this.response = response;
        invoke();
    }

    protected void invoke() {
        if (GsonUtils.isGoodJson(response)) {
            Log.i("response===", response.toString());
            if (response.contains("code400")) {
                result = response.replace("code400", "");
                success = false;
            } else {
                result = response;
                success = true;
            }
        } else {
            success = false;
            result = "";
            if (response != null && response.equals("401")) {
                result = "401";
                Log.i("response===", 401 + "认证失败");
            }
        }
    }
}
