package com.semioe.healthcareup.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ebon-lax on 16/9/23.
 */

public class Utils {

    public static void toast(Context context, String text) {
        // Toast.makeText(context, text, 50).show();
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    // 去除html标签，保留内容
    public static String removeHtmlTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串

    }

    // 判断是是否为手机号
    public static boolean isMobileNO(String mobiles) {
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile("^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$");

        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String SafePhoneNumber(String mobile){
        String maskNumber = mobile.substring(0,3)+" **** "+mobile.substring(7,mobile.length());
        return maskNumber;

    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(),
                        PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email))
            return false;
        // Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断是否有外部存储卡
     *
     * @return
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // 判断token是否过期
    public static boolean TokenBeOverdue(long startTime, long availbaltime) {
        long nowTime = System.currentTimeMillis();
        return nowTime - startTime > availbaltime * 1000 && availbaltime > 0;
    }

    // 防止 多次点击
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    //获取版本号
    public static int getVerCode(Context c) {
        int verCode = -1;
        try {
            String packageName = c.getPackageName();
            verCode = c.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verCode;
    }

    //获取版本名称
    public static String getVerName(Context c) {
        String verName = "";
        try {
            String packageName = c.getPackageName();
            verName = c.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verName;
    }

    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void installApk(Context context, Uri file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(file, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    public static boolean isServiceRunning(Context ctx, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        Iterator<ActivityManager.RunningServiceInfo> l = servicesList.iterator();
        while (l.hasNext()) {
            ActivityManager.RunningServiceInfo si = l.next();
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    public static boolean stopRunningService(Context ctx, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(ctx, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = ctx.stopService(intent_service);
        }
        return ret;
    }

    public static int getNumCores() {
        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    // Check if filename is "cpu", followed by “a” single digit
                    // number
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }

            });
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return 1 core
            return 1;
        }
    }

    /**
     * 判断网络连接是否打开,包括移动数据连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (info != null && info.isConnectedOrConnecting()) {
                    SystemLog.Log("当前网络:" + info.getTypeName());
                    SystemLog.Log("WIFI连接开启");
                    return true;
                } else {
                    SystemLog.Log("WIFI连接未开启");
                }
                info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (info != null && info.isConnectedOrConnecting()
                        && telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) {
                    SystemLog.Log("2G数据连接开启");
                    return true;
                } else {
                    SystemLog.Log("2G数据连接未开启");
                }
            }

        } catch (Exception e) {
            return false;
        }
        return false;

    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * dip转为 px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px 转为 dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取当前时间日期
     */
    public static String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));

        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);

        return sbBuffer.toString();
    }

    // //显示键盘
    public static void ShowSoftInput(Activity mActivity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // //显示键盘
        imm.showSoftInput(editText, 0);
    }

    // //隐藏键盘
    public static void HideSoftInput(Activity mActivity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // //隐藏键盘
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    // string 获取前几位
    public static String getFrontStr(String str, int id) {
        if (str.length() <= id)
            return str;
        return str.substring(0, id);
    }

    public static int getScreenWith(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
        // height = wm.getDefaultDisplay().getHeight();
    }

    public static String InputStreamTOString(InputStream in) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024 * 4];
        int count = -1;
        while ((count = in.read(data, 0, 1024 * 4)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(), "UTF-8");
    }

    public static void finish(Context context) {
        // 强制关闭软键盘
        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        ((Activity) context).finish();
    }

    /**
     * 判断文件类型
     **/
    public static String getFileType(File f) {
        String type = "";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

		/* 依扩展名的类型决定FileType */
        if (end.equals("mp3") || end.equals("mid") || end.equals("wav") || end.equals("aac") || end.equals("amr")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4") || end.equals("rmvb")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")
                || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        } else if (end.equals("txt") || end.equals("c") || end.equals("java") || end.equals("xml")
                || end.equals("cpp")) {
            type = "text";
        } else {
            type = "*";
        }
        /* 若无法直接打开文件，就弹出软件列表 */
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }

    //获取屏幕高度
    public static int getWindowHeight(Context c) {
        WindowManager wm = (WindowManager) c
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public static boolean CheckEmpty(EditText et) {
        if (et.getText().toString().trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean CheckEmpty(TextView et) {
        if (et.getText().toString().trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String getRandomColor() {
        List<String> colorList = new ArrayList<String>();
        colorList.add("#303F9F");
        colorList.add("#FF4081");
        colorList.add("#59dbe0");
        colorList.add("#f57f68");
        colorList.add("#87d288");
        colorList.add("#f8b552");
        colorList.add("#990099");
        colorList.add("#90a4ae");
        colorList.add("#7baaf7");
        colorList.add("#4dd0e1");
        colorList.add("#4db6ac");
        colorList.add("#aed581");
        colorList.add("#fdd835");
        colorList.add("#f2a600");
        colorList.add("#ff8a65");
        colorList.add("#f48fb1");
        colorList.add("#7986cb");
        colorList.add("#FFFFE0");
        colorList.add("#ADD8E6");
        colorList.add("#DEB887");
        colorList.add("#C0C0C0");
        colorList.add("#AFEEEE");
        colorList.add("#F0FFF0");
        colorList.add("#FF69B4");
        colorList.add("#FFE4B5");
        colorList.add("#FFE4E1");
        colorList.add("#FFEBCD");
        colorList.add("#FFEFD5");
        colorList.add("#FFF0F5");
        colorList.add("#FFF5EE");
        colorList.add("#FFF8DC");
        colorList.add("#FFFACD");

        return colorList.get((int)(Math.random() * colorList.size()));
    }

    // 简单的日期选择框
    public static void showDateDialog(final EditText et, Context context) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = sdf = new SimpleDateFormat("yyyy-MM-dd");
                calendar.set(year, month, dayOfMonth);
                et.setText(sdf.format(calendar.getTime()));
            }
        };

        AlertDialog dialog = new DatePickerDialog(context, dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        Window window = dialog.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;
        window.setAttributes(lp);
        dialog.show();
    }

    public static void showDateDialog(final TextView et, Context context) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = sdf = new SimpleDateFormat("yyyy-MM-dd");
                calendar.set(year, month, dayOfMonth);
                et.setText(sdf.format(calendar.getTime()));
            }
        };

        AlertDialog dialog = new DatePickerDialog(context, dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        Window window = dialog.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;
        window.setAttributes(lp);
        dialog.show();
    }

}
