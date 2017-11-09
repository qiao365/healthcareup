package com.semioe.healthcareup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.http.httputils.AllUrl;
import com.semioe.healthcareup.http.httputils.AsyncTaskManager;
import com.semioe.healthcareup.http.httputils.GsonUtils;
import com.semioe.healthcareup.http.httputils.HttpUtil;
import com.semioe.healthcareup.http.httputils.JsonObjectBuilder;
import com.semioe.healthcareup.http.requestparams.BaseRequestParm;
import com.semioe.healthcareup.http.responsebeans.BaseResponseBean;
import com.semioe.healthcareup.http.responsebeans.RequestListener;
import com.semioe.healthcareup.utils.Utils;

import java.nio.CharBuffer;

/**
 * Created by liuaixun on 2017/5/29.
 * 验证码
 */

public class VCodeActivity extends BaseActivity implements View.OnClickListener {

    public static final int REG_FALSE = 1001;
    public static final int REG_SUCCESS = 1002;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            closeSweetADialog();
            switch (msg.what) {
                case REG_FALSE:
                    if (errmsg != null)
                        Toast.makeText(VCodeActivity.this, errmsg, Toast.LENGTH_SHORT).show();
                    else
                        Utils.toast(VCodeActivity.this, "注册失败");
                    break;
                case REG_SUCCESS:
                    if (errcode == 0) {
                        mLoginConfig.setUserName(phone);
                        mLoginConfig.setUserPsw(pwd);
                        Intent intent2 = new Intent(VCodeActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                    } else {
                        Toast.makeText(VCodeActivity.this, errmsg, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    private ImageView iv_back;
    private TextView tv_phone;
    private TextView tv_pw1, tv_pw2, tv_pw3, tv_pw4;
    private TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6, tv_7, tv_8, tv_9, tv_0, tv_del, tv_ok;
    private StringBuffer mPassword = new StringBuffer();
    private String phone, pwd;
    private int errcode;
    private String errmsg;

    @Override
    public int bindLayout() {
        return R.layout.activity_vcode;
    }

    @Override
    public void initView(View view) {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_pw1 = (TextView) findViewById(R.id.tv_pw1);
        tv_pw2 = (TextView) findViewById(R.id.tv_pw2);
        tv_pw3 = (TextView) findViewById(R.id.tv_pw3);
        tv_pw4 = (TextView) findViewById(R.id.tv_pw4);

        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_7 = (TextView) findViewById(R.id.tv_7);
        tv_8 = (TextView) findViewById(R.id.tv_8);
        tv_9 = (TextView) findViewById(R.id.tv_9);
        tv_0 = (TextView) findViewById(R.id.tv_0);
        tv_del = (TextView) findViewById(R.id.tv_del);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
    }

    @Override
    public void doBusiness(Context mContext) {
        if (getIntent().getExtras() != null) {
            phone = getIntent().getExtras().get("phone").toString();
            pwd = getIntent().getExtras().get("pwd").toString();
            tv_phone.setText(Utils.SafePhoneNumber(phone));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_0:
            case R.id.tv_1:
            case R.id.tv_2:
            case R.id.tv_3:
            case R.id.tv_4:
            case R.id.tv_5:
            case R.id.tv_6:
            case R.id.tv_7:
            case R.id.tv_8:
            case R.id.tv_9:
                add(((TextView) findViewById(v.getId())).getText().toString());
                if (getText().length() == 4) {
                    Register(phone, pwd, getText());
                }
                break;
            case R.id.tv_del:
                remove();
                break;
            case R.id.tv_ok:
                if (getText().length() == 4) {
                    Register(phone, pwd, getText());
                }
                break;
        }

    }

    private void Register(String phone, String psw, String vcode) {
        String loginUrl = new AllUrl(this).getRegisterUrl();

        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("tel", phone);
        builder.append("password", psw);
        builder.append("vcode", vcode);
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
                                Data(bean);
                            } else {
                                handler.sendEmptyMessage(REG_FALSE);
                            }
                        }

                        @Override
                        public void onFailed() {

                        }

                    }, this);
        } else {
            Utils.toast(VCodeActivity.this, "网络不给力呀~ o(︶︿︶)o");
            return;
        }
    }

    private void Data(BaseResponseBean bean) {
        JsonObject json = GsonUtils.getRootJsonObject(bean.getResult());
        errcode = GsonUtils.getKeyValue(json, "errcode").getAsInt();
        errmsg = GsonUtils.getKeyValue(json, "msg").getAsString();
        handler.sendEmptyMessage(REG_SUCCESS);
    }


    /**
     * 输入密码，根据StringBuilder中数据的多少进行逻辑判断
     *
     * @param value
     */
    public void add(String value) {
        Log.i("keyboard-----》", value);
        if (mPassword.length() < 4) {
            if (mPassword.length() == 0) {
                tv_pw1.setText(value);
            } else if (mPassword.length() == 1) {
                tv_pw2.setText(value);
            } else if (mPassword.length() == 2) {
                tv_pw3.setText(value);
            } else if (mPassword.length() == 3) {
                tv_pw4.setText(value);
            }
            mPassword.append(value);
        }

    }

    /**
     * 删除密码，根据StringBuilder中数据的多少进行逻辑判断
     */
    public void remove() {
        if (mPassword != null && mPassword.length() > 0) {
            if (mPassword.length() == 1) {
                tv_pw1.setText("");
            } else if (mPassword.length() == 2) {
                tv_pw2.setText("");
            } else if (mPassword.length() == 3) {
                tv_pw3.setText("");
            } else if (mPassword.length() == 4) {
                tv_pw4.setText("");
            }
            mPassword.deleteCharAt(mPassword.length() - 1);
        }
    }

    /**
     * 返回完整密码
     *
     * @return
     */
    public String getText() {
        if (mPassword == null)
            return "";
        else
            return (mPassword == null) ? null : mPassword.toString();
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
