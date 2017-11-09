package com.semioe.healthcareup;

import android.app.Application;
import android.content.Context;

/**
 *
 * Created by songyuequan on 2017/2/24.
 */
public class MainApplication extends Application {


    private static final String TAG = MainApplication.class.getSimpleName();
    private static Context context;
    public static float sScale;
    public static int sWidthDp;
    public static int sWidthPix;
    public static int sHeightPix;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        sScale = getResources().getDisplayMetrics().density;
        sWidthPix = getResources().getDisplayMetrics().widthPixels;
        sHeightPix = getResources().getDisplayMetrics().heightPixels;
        sWidthDp = (int) (sWidthPix / sScale);
    }


}
