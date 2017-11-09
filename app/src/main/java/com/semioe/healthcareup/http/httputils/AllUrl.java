package com.semioe.healthcareup.http.httputils;

import android.content.Context;

/**
 * Created by Ebon-lax on 17/5/28.
 */

public class AllUrl {

    public AllUrl() {
    }

    public AllUrl(Context context) {

    }

    // 短信验证码（定义接口，需要调试）
    public String getVCodeUrl() {
        return Url.BASE_URL + "/user/getvcode";
    }

    // 短信验证码2(暂时可用)
    public String getVCodeUrl(String phone) {
        return Url.BASE_URL + "/user/getvcode?tel=" + phone;
    }

    // 注册
    public String getRegisterUrl() {
        return Url.BASE_URL + "/user/register";
    }

    //医生列表
    public String getDoctorsUrl() {
        return Url.BASE_URL + "/api/doctor/getAll";
    }

    //所有设备
    public String getDevicesUrl() {
        return Url.BASE_URL + "/api/device/getAll";
    }

    //我的设备
    public String getMyDevicesUrl() {
        return Url.BASE_URL + "/api/device/getByUid";
    }

    // 登陆
    public String getUserLoginUrl() {
        return Url.BASE_URL + "/oauth/login";
    }

    // 修改密码
    public String getChangePWUrl() {
        return Url.BASE_URL + "/oauth/login";
    }

    // 绑定设备
    public String getBindDiverUrl() {
        return Url.BASE_URL + "/api/device/bind";
    }

    // 健康数据收集-选填项-数据格式接口
    public String getInitQuestionUrl() {
        return Url.BASE_URL + "/api/health/initQuestion";
    }

    // 健康状况数据汇总
    public String getAnswerUrl() {
        return Url.BASE_URL + "/api/health/getAnswer";
    }

    // 健康档案 图标数据
    public String getHealthDataUrl() {
        return Url.BASE_URL + "/api/health/getByCondition";
    }

    //慢病数据
    public String getChronicUrl() {
        return Url.BASE_URL + "/api/health/getChronic";
    }

    public String getMsgDataUrl() {
        return Url.BASE_URL + "/api/sysmsg/getMsg";
    }

    //创建会话
    public String getCreateHHUrl() {
        return "http://api.php-x.com" + "/dialog/create-dialog";
    }

    //开始会话
    public String getStartHHUrl() {
        return "http://api.php-x.com" + "/dialog";
    }

    //更新用户信息
    public String getSaveUserUrl() {
        return Url.BASE_URL + "/api/user/save";
    }

    //提交数据 datak,datav
    public String AnswerUrl() {
        return Url.BASE_URL + "/api/health/answer";
    }

    //运动步数上传
    public String MovestepsUrl() {
        return Url.BASE_URL + "/api/movesteps/save";
    }

    //服药记录上传
    public String eatdrugUrl() {
        return Url.BASE_URL + "/api/health/eatdrug/save";
    }

    //马上看结果
    public String instantpushUrl() {
        return Url.BASE_URL + "/api/health/instantpush";
    }

    //现在的症状
    public String getCurSymptomsUrl() {
        return Url.BASE_URL + "/api/health/getCurSymptoms";
    }
}
