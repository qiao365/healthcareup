package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by liuaixun on 2017/6/6.
 */

public class MedRecord implements Serializable {

    /**
     * uid : 53
     * drugname : 秋水仙碱
     * dose : 1
     * eattime : 0
     */

    @Expose
    private String uid;
    @Expose
    private String drugname;
    @Expose
    private String dose;
    @Expose
    private String eattime;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDrugname() {
        return drugname;
    }

    public void setDrugname(String drugname) {
        this.drugname = drugname;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getEattime() {
        return eattime;
    }

    public void setEattime(String eattime) {
        this.eattime = eattime;
    }
}
