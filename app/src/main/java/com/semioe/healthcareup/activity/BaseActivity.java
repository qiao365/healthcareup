package com.semioe.healthcareup.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.BaseAtyInterface;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.utils.LoginConfig;
import com.semioe.healthcareup.utils.Utils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ebon-lax on 16/9/23.
 */

public abstract class BaseActivity extends AutoLayoutActivity implements BaseAtyInterface, ActivityCompat.OnRequestPermissionsResultCallback {

    private View mContextView = null;
    protected final String TAG = this.getClass().getSimpleName();
    public LoginConfig mLoginConfig;
    private View view;
    private TextView tipTextView;
    private Dialog dialog = null;
    private ProgressBar pb;
    public SweetAlertDialog pDialog;

    private static final int REQUEST_CODE_PERMISSON = 2020; //权限请求码
    private boolean isNeedCheckPermission = true; //判断是否需要检测，防止无限弹框申请权限


    private Handler hander = new Handler() {

        public void handleMessage(Message msg) {
            dialog.dismiss();
        }
    };

    public Handler logOuthandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            closeQuicklyProgressDialog();
            switch (msg.what) {
                case Constants.GotoLoginActivity:
                    closeQuicklyProgressDialog();
                    Utils.toast(BaseActivity.this, "登陆失效,请重新登陆");
//                    new LoginConfig(BaseActivity.this).setAvailbleTime("0"); //是否加密
                    AppManager.getInstance().AppExitToLogin(BaseActivity.this, logOuthandler);
                    break;

                default:
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginConfig = new LoginConfig(this);
        AppManager.getInstance().addActivity(this);//加入堆栈
        //设置渲染视图View
        mContextView = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(mContextView);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        initProgressDialog();
        //初始化控件
        initView(mContextView);
        //业务操作
        doBusiness(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4以上
            setTranslucentStatus(true);
        }
//
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.black);//通知栏所需颜色
        tintManager.setNavigationBarTintEnabled(true);

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    /**
     * 初始化进度提示对话框
     */
    private void initProgressDialog() {
        view = getLayoutInflater().inflate(R.layout.progressbar_dialog, null);
        tipTextView = (TextView) view.findViewById(R.id.mTVContent);
        pb = (ProgressBar) view.findViewById(R.id.mProgressBar);
        dialog = new Dialog(this, R.style.custom_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(true);// 对话框禁止返回键
        dialog.setCanceledOnTouchOutside(false);// 对话框外不取消
    }

    /**
     * 打开进度对话框,已经显示就不在显示
     *
     * @param text
     */
    public void showProgressDialog(String text) {
        try {
            if (!dialog.isShowing()) {
                tipTextView.setText(text);
                dialog.show();

            } else {
                doProgressDialog(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭进度对话框
     */
    public void closeProgressDialog() {
        hander.sendEmptyMessageDelayed(0x500, 1000);
    }

    /**
     * 立刻关闭进度对话框
     */
    public void closeQuicklyProgressDialog() {
        hander.sendEmptyMessageDelayed(0x500, 0);
    }

    /**
     * 在进度中显示的文字
     *
     * @param text
     */
    public void doProgressDialog(String text) {
        tipTextView.setText(text);
    }

    /**
     * 在进度中显示的文字加图片
     *
     * @param text
     * @param drawable
     */
    public void doProgressDialog(String text, Drawable drawable) {
        pb.setIndeterminateDrawable(drawable);
        tipTextView.setText(text);
    }

    /**
     * 对话框释放
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 对话框销毁
        dialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheckPermission) {
            checkAllNeedPermissions();
        }
    }

    public void showSweetAlertDialog(Context mContext) {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#4dc1fd"));
        pDialog.setTitleText("Waiting...");
        pDialog.setCancelable(false);
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    public void closeSweetADialog() {
//        pDialog.cancel();
        pDialog.dismiss();
    }


    /**
     * 检查所有权限，无权限则开始申请相关权限
     */
    protected void checkAllNeedPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        List<String> needRequestPermissonList = getDeniedPermissions(getNeedPermissions());
        if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this, needRequestPermissonList.toArray(
                    new String[needRequestPermissonList.size()]), REQUEST_CODE_PERMISSON);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissonList.add(permission);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 所有权限是否都已授权
     *
     * @return
     */
    protected boolean isGrantedAllPermission() {
        List<String> needRequestPermissonList = getDeniedPermissions(getNeedPermissions());
        return needRequestPermissonList.size() == 0;
    }

    /**
     * 权限授权结果回调
     *
     * @param requestCode
     * @param permissions
     * @param paramArrayOfInt
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] paramArrayOfInt) {
        if (requestCode == REQUEST_CODE_PERMISSON) {
            if (!verifyPermissions(paramArrayOfInt)) {
                permissionGrantedFail();
                showTipsDialog();
                isNeedCheckPermission = false;
            } else permissionGrantedSuccess();
        }
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示对话框
     */
    protected void showTipsDialog() {
        new AlertDialog.Builder(this).setTitle("提示信息").setMessage("当前应用缺少" + getDialogTipsPart()
                + "权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }


    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * 获取弹框提示部分内容
     *
     * @return
     */
    protected String getDialogTipsPart() {
        return "必要";
    }

    /**
     * 获取需要进行检测的权限数组
     */
    protected abstract String[] getNeedPermissions();

    /**
     * 权限授权成功
     */
    protected abstract void permissionGrantedSuccess();

    /**
     * 权限授权失败
     */
    protected abstract void permissionGrantedFail();
}
