package com.semioe.healthcareup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.roundview.RoundTextView;
import com.google.gson.JsonObject;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.bean.LoginBean;
import com.semioe.healthcareup.bean.LoginErrorBean;
import com.semioe.healthcareup.http.httputils.AllUrl;
import com.semioe.healthcareup.http.httputils.AsyncTaskManager;
import com.semioe.healthcareup.http.httputils.GsonUtils;
import com.semioe.healthcareup.http.httputils.HttpUtil;
import com.semioe.healthcareup.http.httputils.JsonObjectBuilder;
import com.semioe.healthcareup.http.requestparams.BaseRequestParm;
import com.semioe.healthcareup.http.responsebeans.BaseResponseBean;
import com.semioe.healthcareup.http.responsebeans.RequestListener;
import com.semioe.healthcareup.utils.LoginConfig;
import com.semioe.healthcareup.utils.Utils;

public class LoginActivity extends BaseActivity implements OnClickListener {

    private EditText tv_name, tv_pw;
    private RoundTextView rtv_ok;

    private final static int LOGIN_SUCCESS = 10001;// 登陆成功
    private final static int LOGIN_FALSE = 10002;// 登陆失败
    private final static int UPDATE_MES = 10005;// 登陆失败

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            closeSweetADialog();
            switch (msg.what) {
                case LOGIN_FALSE:
                    if (errmsg != null)
                        Utils.toast(LoginActivity.this, errmsg);
                    else
                        Utils.toast(LoginActivity.this, "请检查手机号密码");
                    break;
                case LOGIN_SUCCESS:
                    mLoginConfig.setUserName(tv_name.getText().toString().trim());
                    mLoginConfig.setUserPsw(tv_pw.getText().toString().trim());

                    upLoadGATA();
                    break;
                case UPDATE_MES:
                    Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
            }
        }
    };
    private String errmsg;
    private TextView tv_register;

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(View view) {
        tv_name = (EditText) findViewById(R.id.tv_name);
        tv_pw = (EditText) findViewById(R.id.tv_pw);
        rtv_ok = (RoundTextView) findViewById(R.id.rtv_ok);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_name.setText(mLoginConfig.getUserName());
        tv_pw.setText(mLoginConfig.getUserPsw());
    }

    @Override
    public void doBusiness(Context mContext) {
        rtv_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rtv_ok:
                if (Utils.isFastClick()) {
                    return;
                }
                if (tv_name.getText().toString().length() > 0 && tv_pw.getText().toString().length() > 0) {
                    doLogin(tv_name.getText().toString().trim(), tv_pw.getText().toString().trim());
                } else {
                    Toast.makeText(LoginActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            default:
                break;
        }

    }

    private void upLoadGATA(){
        String loginUrl = new AllUrl(this).getSaveUserUrl();

        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("id", mLoginConfig.getUserId());
        builder.append("teldevice", LoginConfig.getChannelId());
        String data = builder.toString();

        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(
                    new BaseRequestParm(loginUrl, data, AsyncTaskManager.ContentTypeJson, "POST",
                            LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                                handler.sendEmptyMessage(UPDATE_MES);

                        }

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(UPDATE_MES);
                        }

                    }, this);
        } else {
            Utils.toast(LoginActivity.this, "网络不给力呀~ o(︶︿︶)o");
            return;
        }
    }

    private void doLogin(String name, String psw) {
        String loginUrl = new AllUrl(this).getUserLoginUrl();

        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("tel", name);
        builder.append("password", psw);
        String data = builder.toString();

        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(
                    new BaseRequestParm(loginUrl, data, AsyncTaskManager.ContentTypeJson, "POST",
                            ""),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                getGsonAndLogin(bean);
                            } else {
                                handler.sendEmptyMessage(LOGIN_FALSE);
                            }
                        }

                        @Override
                        public void onFailed() {

                        }

                    }, this);
        } else {
            Utils.toast(LoginActivity.this, "网络不给力呀~ o(︶︿︶)o");
            return;
        }
    }

    // 数据解析
    private void getGsonAndLogin(BaseResponseBean bean) {
        JsonObject json = GsonUtils.getRootJsonObject(bean.getResult());
        if(!bean.getResult().contains("OK")){
            LoginErrorBean loginbean = GsonUtils.JsonObjectToBean(json, LoginErrorBean.class);
            errmsg = loginbean.getMsg();
            handler.sendEmptyMessage(LOGIN_FALSE);
            return;
        }
        LoginBean loginbean = GsonUtils.JsonObjectToBean(json, LoginBean.class);
        if (loginbean != null && loginbean.getErrcode() == 0) {
            String access_token = loginbean.getResult().getAccess_token();
            String token_type = loginbean.getResult().getToken_type();
            String userId = loginbean.getResult().getUserId();
            int expires_in = loginbean.getResult().getExpires_in();

//            mLoginConfig.setAuthorization(token_type + " " + access_token);
            mLoginConfig.setAuthorization(access_token);
            mLoginConfig.setAvailbleTime(expires_in + "");
            mLoginConfig.setStartTime(System.currentTimeMillis());
            mLoginConfig.setUserId(userId);
            Log.i("i", "获取token=" + access_token);
            handler.sendEmptyMessage(LOGIN_SUCCESS);
        } else {
            LoginErrorBean errorloginbean = GsonUtils.JsonObjectToBean(json, LoginErrorBean.class);
            errmsg = errorloginbean.getMsg();
            handler.sendEmptyMessage(LOGIN_FALSE);
        }
    }

    @Override
    protected String[] getNeedPermissions() {
        return new String[0];
    }

    @Override
    protected void permissionGrantedSuccess() {

    }

    @Override
    protected void permissionGrantedFail() {

    }
}

