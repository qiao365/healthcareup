package com.semioe.healthcareup.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by liuaixun on 2017/5/29.
 */

public class LoginBean implements Serializable {

    /**
     * errcode : 0
     * errmsg : OK
     * result : {}
     */

    @Expose
    private Integer errcode;
    @Expose
    private String errmsg;
    @Expose
    private ResultBean result;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {

        /**
         * access_token :
         * token_type : bearer
         * userId : 4651321
         * expires_in : 10
         */

        @Expose
        private String access_token;
        @Expose
        private String token_type;
        @Expose
        private String userId;
        @Expose
        private int expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }
}
