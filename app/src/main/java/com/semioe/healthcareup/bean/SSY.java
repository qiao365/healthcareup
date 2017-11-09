package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by songyuequan on 2017/5/25.
 */

public class SSY implements Serializable {
    @Expose
    private int id;
    @Expose
    private int uid;
    @Expose
    private String createBy;
    @Expose
    private String createDate;
    @Expose
    private String updateBy;
    @Expose
    private String updateDate;
    @Expose
    private String delFlag;
    @Expose
    private String type;
    @Expose
    private String datak="";
    @Expose
    private String datav = "0";
    @Expose
    private String data = "1";
    @Expose

    private String batchid;
    @Expose
    private String begintime;
    @Expose
    private String endtime;
    @Expose
    private String Did;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getDid() {
        return Did;
    }

    public void setDid(String did) {
        Did = did;
    }
}
