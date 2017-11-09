package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/1.
 */

public class Devices implements Serializable {
    @Expose
    private int errcode = -1;
    @Expose
    private String msg = "";
    @Expose
    private List<Device> result = new ArrayList<>();
    @Expose
    private String code = "";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Device> getResult() {
        return result;
    }

    public void setResult(List<Device> result) {
        this.result = result;
    }


}
