package com.semioe.healthcareup.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final int SEND_FALSE = 10001;
    private static final int SEND_SUCCESS = 10002;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            closeSweetADialog();
            switch (msg.what) {
                case SEND_FALSE:
                    Utils.toast(RegisterActivity.this, "请求失败，请稍后重试");
                    break;
                case SEND_SUCCESS:
                    if (errcode == 0) {
                        Intent intent = new Intent();
                        intent.putExtra("phone", et_phoneNum.getText().toString().trim());
                        intent.putExtra("pwd", et_pwd.getText().toString().trim());
                        intent.setClass(RegisterActivity.this, VCodeActivity.class);
                        startActivity(intent);
                    } else {
                        Utils.toast(RegisterActivity.this, errmsg);
                    }
                    break;
            }
        }
    };

    private ImageView iv_back;
    private TextView tv_next;
    private EditText et_phoneNum, et_pwd, et_pwd2;
    private CheckBox chexkbox;
    private int errcode;
    private String errmsg;

    @Override
    public int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(View view) {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setOnClickListener(this);
        et_phoneNum = (EditText) findViewById(R.id.et_phoneNum);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd2 = (EditText) findViewById(R.id.et_pwd2);
        chexkbox = (CheckBox) findViewById(R.id.checkBox);
    }

    @Override
    public void doBusiness(Context mContext) {
        et_phoneNum.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    if (!Utils.isMobileNO(et_phoneNum.getText().toString().trim())) {
                        Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                if (!Utils.isMobileNO(et_phoneNum.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!et_pwd.getText().toString().trim().equals(et_pwd2.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!chexkbox.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "请阅读并同意协议", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    SendSMS(et_phoneNum.getText().toString().trim());
                }

                break;
        }

    }

    private void SendSMS(String phoneNum) {
//        String Url = new AllUrl(this).getVCodeUrl();
        String Url = new AllUrl(this).getVCodeUrl(phoneNum);

        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("tel", phoneNum);
        String data = builder.toString();

        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttpNotToken(
                    new BaseRequestParm(Url, data, AsyncTaskManager.ContentTypeXfl, "POST",
                            ""),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                Data(bean);
                            } else {
                                handler.sendEmptyMessage(SEND_FALSE);
                            }
                        }

                        @Override
                        public void onFailed() {

                        }

                    }, this);
        } else {
            Utils.toast(RegisterActivity.this, "网络不给力呀~ o(︶︿︶)o");
            return;
        }
    }

    private void Data(BaseResponseBean bean) {
        JsonObject json = GsonUtils.getRootJsonObject(bean.getResult());
        errcode = GsonUtils.getKeyValue(json, "errcode").getAsInt();
        errmsg = GsonUtils.getKeyValue(json, "msg").getAsString();
        handler.sendEmptyMessage(SEND_SUCCESS);
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
