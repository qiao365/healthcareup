package com.semioe.healthcareup.http.httputils;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.semioe.healthcareup.http.requestparams.BaseRequestParm;
import com.semioe.healthcareup.http.responsebeans.BaseResponseBean;
import com.semioe.healthcareup.http.responsebeans.RequestListener;
import com.semioe.healthcareup.utils.LoginConfig;
import com.semioe.healthcareup.utils.SystemLog;
import com.semioe.healthcareup.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description异步任务管理类 concurrent java.util.concurrent 是在并发编程中很常用的实用工具类。
 */
public class AsyncTaskManager {

    private ExecutorService mPool = null;
    public static String ContentTypeXfl = "application/x-www-form-urlencoded";
    public static String ContentTypeTS = "application/octet-stream";
    public static String ContentTypeJson = "application/json; charset=UTF-8";
    /* token */
    public static String getVerCodeToken = "Bearer 0d02588a-2b54-416b-aa77-efd0343a05ff";
    public static String LoginToken = "Basic cG1zLXNqOlpUVTk4SkFDTUlaVkxPRU0=";
    private static AsyncTaskManager mTask;
    private String result = "";

    // 声明TASK函数类

    private AsyncTaskManager(int nThreads) {
        mPool = Executors.newFixedThreadPool(nThreads);
        // 初始化网络Task类
    }

    public static AsyncTaskManager getInstance() {
        if (mTask == null) {
            int nThreads = Utils.getNumCores();
            mTask = new AsyncTaskManager(nThreads * 2);
        }
        return mTask;
    }

    // 网络交互
    public void StartHttpNotToken(final BaseRequestParm parms, final RequestListener<BaseResponseBean> listener,
                                  final Context context) {
        mPool.execute(new Runnable() {

            public void run() {
                BaseResponseBean responseBean = getResponseBean(parms);
                listener.onComplete(responseBean);
            }
        });

    }

    // 网络交互
    public void StartHttp(final BaseRequestParm parms, final RequestListener<BaseResponseBean> listener,
                          final Context context) {
        mPool.execute(new Runnable() {

            public void run() {
                final LoginConfig mloginConfig = new LoginConfig(context);
//
//				parms.setClientVersion(Utils.getVerName(context));
//				parms.setClientVersionCode(Utils.getVerCode(context));
//				parms.setNetworkType(InternetUtil.getNetworkState(context));
//				parms.setOsVersion(mloginConfig.getosVersion());
//				parms.setPlatform("android");

//				 判断token是否过期
                if (Utils.TokenBeOverdue(LoginConfig.getStartTime(),
                        Long.parseLong(mloginConfig.getAvailbleTime()))) {
                    // 刷新token
//					String jsonData = "grant_type=refresh_token" + "&refresh_token="
//							+ mloginConfig.getReAuthorization();
//					BaseRequestParm refreshParm = new BaseRequestParm(new AllUrl().getRefreshTokenUrl(), jsonData,
//							AsyncTaskManager.ContentTypeXfl, "POST", AsyncTaskManager.LoginToken);
//					BaseResponseBean refreshBean = getResponseBean(refreshParm);
//					if (analysis(refreshBean, mloginConfig)) {
//						// 刷新成功 继续下一步
//						parms.setAuthorization(LoginConfig.getAuthorization());// token重新设置
//						BaseResponseBean responseBean = getResponseBean(parms);
//						listener.onComplete(responseBean);
//					} else {
                    listener.onFailed();
                    SystemLog.Log("token刷新失败");
//					}
                } else {
                    // token未过期，继续操作
                    BaseResponseBean responseBean = getResponseBean(parms);
                    if (responseBean.response != null && responseBean.response.equals("401")) {
                        listener.onFailed();// 认证失败
                    } else {
                        listener.onComplete(responseBean);
                    }
                }
            }
        });

    }

    // 解析刷新的数据
//	private boolean analysis(BaseResponseBean bean, LoginConfig mloginConfig) {
//		if (bean.isSuccess()) {
//			JsonObject json = GsonUtils.getRootJsonObject(bean.getResult());
//			String token = GsonUtils.getKeyValue(json, "access_token").getAsString();
//			String refresh_token = GsonUtils.getKeyValue(json, "refresh_token").getAsString();
//			String expires_in = GsonUtils.getKeyValue(json, "expires_in").getAsString();// 有效时间
//			mloginConfig.setAuthorization("Bearer " + token);
//			mloginConfig.setReAuthorization(refresh_token);
//			mloginConfig.setAvailbleTime(expires_in);
//			mloginConfig.setStartTime(System.currentTimeMillis());
//			Log.i("i", "获取token=" + token);
//			Log.i("i", "获取refresh_token=" + refresh_token);
//			return true;
//		}
//		return false;
//	}

    private BaseResponseBean getResponseBean(BaseRequestParm parms) {
        if (parms.getRequest() == null) {// 传图片 数据 等 httppost
            result = HttpUtil.postMethod(parms.getUrl(), parms.getAuthorization(), parms.getTextMap(),
                    parms.getImageUrlList(), parms.getFilePath());
        } else {
            if (parms.isEncryption()) {
//				result = HttpUtil.uploadData(parms.getUrl(), parms.getContentType(), parms.getStringJsonData(),
//						parms.getRequest(), parms.getAuthorization(), "1");// 1代表加密
                result = HttpUtil.uploadData(parms, "1");
            } else {
//				result = HttpUtil.uploadData(parms.getUrl(), parms.getContentType(), parms.getStringJsonData(),
//						parms.getRequest(), parms.getAuthorization(), "0");// 0代表不加密
                result = HttpUtil.uploadData(parms, "0");
            }

        }
        return new BaseResponseBean(result);
    }

}
