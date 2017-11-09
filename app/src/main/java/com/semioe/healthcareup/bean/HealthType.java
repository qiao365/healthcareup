package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by liuaixun on 2017/6/1.
 */

public class HealthType implements Serializable {

    /**
     * id : 2
     * createBy :
     * createDate :
     * updateBy :
     * updateDate : 2017-05-19 03:34:29
     * delFlag : 0
     * lable : 健康
     * type : healthybymyself
     * description : 健康状况自评
     * sort : 0
     * parentId : 0
     * remarks :
     */

    @Expose
    private int id;
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
    private String lable;
    @Expose
    private String type;
    @Expose
    private String description;
    @Expose
    private int sort;
    @Expose
    private String parentId;
    @Expose
    private String remarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
