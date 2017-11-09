package com.semioe.healthcareup.application;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mapapi.SDKInitializer;
import com.semioe.healthcareup.baidu.LocationService;
import com.semioe.healthcareup.utils.CrashHandler;

import java.util.ArrayList;
import java.util.List;

public class LocationApplication extends Application {

    public LocationService locationService;
    public Vibrator mVibrator;
    /**
     * activity栈保存
     */
    public List<Activity> activityStack = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化栈
        activityStack = new ArrayList<Activity>();

        CrashHandler.getInstance().init(getApplicationContext());
/***
 * 初始化定位sdk，建议在Application中创建
 */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        //推送
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"fgqI36Z0B81Qnwpdrm69PZU6");
//        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"qODUvq9RlcykCzxY76YHPEbY6jTpG9hw");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 在内存低时,发送广播可以释放一些内存
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * 退出整个应用
     */
    public void exitApp() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
//				android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

}
