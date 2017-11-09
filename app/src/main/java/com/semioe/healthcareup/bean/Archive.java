package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by songyuequan on 2017/5/25.
 */

public class Archive implements Serializable {
    @Expose
    private String id;
    @Expose
    private String unit;
    @Expose
    private String amount;
    @Expose
    private String name;

    public Archive(String name, String unit, String amount){
        this.name = name;
        this.unit = unit;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
