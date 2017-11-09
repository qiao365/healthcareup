package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by songyuequan on 2017/5/25.
 */

public class Msg implements Serializable {
    @Expose
    private String id;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Expose

    private String createDate;
    @Expose
    private int type;
    @Expose
    private String msg = "";
    @Expose
    private String title = "";
    @Expose
    private String content;
    @Expose
    private String year;
    @Expose
    private String date;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Msg(int type, String year , String date){
        this.year = year;
        this.date = date;
        this.type = type;
    }
}
