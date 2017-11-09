package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by songyuequan on 2017/5/25.
 */

public class Device implements Serializable {
    @Expose
    private String id;
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
    private String name;
    @Expose
    private String type;
    @Expose
    private String location = "";
    @Expose
    private String deviceno;
    @Expose
    private String uid;
    @Expose
    private String remarks;
    @Expose
    private long jd;
    @Expose
    private long wd;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        if (location != null) {
            String[] split = location.split(",");
            String wdStr = split[0];
            String jdStr = split[1];
            this.jd = Long.getLong(jdStr);
            this.wd = Long.getLong(wdStr);
        }
    }

    public String getDeviceno() {
        return deviceno;
    }

    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
