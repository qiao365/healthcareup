package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by cjq on 2017/5/25.
 */

public class ServiceItem implements Serializable {
    @Expose
    private String id;
    @Expose
    private String serviceName;
    @Expose
    private String price;
    @Expose
    private String content = "";

    public ServiceItem(){}

    public ServiceItem(String serviceName,String price){
        this.price = price;
        this.serviceName = serviceName;
    }

    public ServiceItem(String serviceName,String price,String content){
        this.price = price;
        this.serviceName = serviceName;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
