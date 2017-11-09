package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by liuaixun on 2017/5/29.
 */

public class LoginErrorBean implements Serializable {

    @Expose
    private Integer errcode;
    @Expose
    private String msg;
    @Expose
    private String result;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
