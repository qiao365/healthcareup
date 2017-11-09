package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by songyuequan on 2017/5/25.
 */

public class Doctors implements Serializable {
    @Expose
    private int errcode = -1;
    @Expose
    private String msg = "";
    @Expose
    private List<Doctor> result = new ArrayList<>();

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

    public List<Doctor> getResult() {
        return result;
    }

    public void setResult(List<Doctor> result) {
        this.result = result;
    }

    public class Doctor implements Serializable {
        @Expose
        private int id = -1;
        @Expose
        private String createBy = "";
        @Expose
        private String createDate = "";
        @Expose
        private String updateBy = "";
        @Expose
        private String updateDate = "";
        @Expose
        private String delFlag = "";
        @Expose
        private String dname = "";
        @Expose
        private String head_pic = "";
        @Expose
        private String refid = "";
        @Expose
        private String hospital = "";
        @Expose
        private String office = "";
        @Expose
        private String dlevel = "";
        @Expose
        private String goodat = "";
        @Expose
        private String remarks = "";

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

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getRefid() {
            return refid;
        }

        public void setRefid(String refid) {
            this.refid = refid;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getOffice() {
            return office;
        }

        public void setOffice(String office) {
            this.office = office;
        }

        public String getDlevel() {
            return dlevel;
        }

        public void setDlevel(String dlevel) {
            this.dlevel = dlevel;
        }

        public String getGoodat() {
            return goodat;
        }

        public void setGoodat(String goodat) {
            this.goodat = goodat;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
