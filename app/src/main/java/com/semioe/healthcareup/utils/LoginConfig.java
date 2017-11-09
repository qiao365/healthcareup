package com.semioe.healthcareup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by liuaixun on 2017/5/29.
 */

public class LoginConfig {
    public static SharedPreferences sp;

    public LoginConfig(Context context) {
        super();
        sp = context.getSharedPreferences("Loginconfig", Context.MODE_PRIVATE);
    }

    public void setUserName(String schema) {
        Editor ed = sp.edit();
        ed.putString("userName", schema);
        ed.commit();
    }

    public void setChannelId(String channelId) {
        Editor ed = sp.edit();
        ed.putString("channelId", channelId);
        ed.commit();
    }

    public static String getChannelId() {
        return sp.getString("channelId", "");
    }

    public static String getUserName() {
        return sp.getString("userName", "");
    }

    // 设置user密码,在登录设置
    public void setUserPsw(String pass) {
        Editor ed = sp.edit();
        ed.putString("userpw", pass);
        ed.commit();
    }

    public String getUserPsw() {
        return sp.getString("userpw", "");
    }

    // 认证码,保持登陆状态
    public static String getAuthorization() {
        return sp.getString("Authorization", "");
    }

    // 认证码,保持登陆状态
    public void setAuthorization(String token) {
        Editor ed = sp.edit();
        ed.putString("Authorization", token);
        ed.commit();
    }

    // 有效时间
    public void setAvailbleTime(String expires_in) {
        Editor ed = sp.edit();
        ed.putString("AvailbleTime", expires_in);
        ed.commit();
    }

    // 有效时间
    public String getAvailbleTime() {
        return sp.getString("AvailbleTime", "0");
    }

    // token获取时的系统时间
    public void setStartTime(long expires_in) {
        Editor ed = sp.edit();
        ed.putLong("NowTime", expires_in);
        ed.commit();
    }

    // token获取时的系统时间
    public static long getStartTime() {
        return sp.getLong("NowTime", 0);
    }

    public void setUserId(String userId) {
        Editor ed = sp.edit();
        ed.putString("UserId", userId);
        ed.commit();
    }

    public static String getUserId() {
        return sp.getString("UserId", "");
    }

}
