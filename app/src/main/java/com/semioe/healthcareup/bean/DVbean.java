package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by liuaixun on 2017/6/5.
 */

public class DVbean implements Serializable {

    /**
     * uid : 1111
     * datak : testIndex
     * datav : 1
     * batchid : uuid
     */

    @Expose
    private String uid;
    @Expose
    private String datak;
    @Expose
    private String datav;
    @Expose
    private String batchid;
    @Expose
    private String type;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDatak() {
        return datak;
    }

    public void setDatak(String datak) {
        this.datak = datak;
    }

    public String getDatav() {
        return datav;
    }

    public void setDatav(String datav) {
        this.datav = datav;
    }

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
