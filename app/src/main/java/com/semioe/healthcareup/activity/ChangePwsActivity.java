package com.semioe.healthcareup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.roundview.RoundTextView;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.http.httputils.AllUrl;
import com.semioe.healthcareup.http.httputils.AsyncTaskManager;
import com.semioe.healthcareup.http.httputils.HttpUtil;
import com.semioe.healthcareup.http.httputils.JsonObjectBuilder;
import com.semioe.healthcareup.http.requestparams.BaseRequestParm;
import com.semioe.healthcareup.http.responsebeans.BaseResponseBean;
import com.semioe.healthcareup.http.responsebeans.RequestListener;
import com.semioe.healthcareup.utils.Utils;

public class ChangePwsActivity extends BaseActivity implements View.OnClickListener {

    private final static int SUCCESS = 10001;// 成功
    private final static int FALSE = 10002;// 失败

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            closeSweetADialog();
            switch (msg.what) {
                case FALSE:
                    Toast.makeText(ChangePwsActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    Toast.makeText(ChangePwsActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    mLoginConfig.setUserPsw(et_newPwd.getText().toString().trim());
                    mLoginConfig.setAvailbleTime("0");
                    Intent intent2 = new Intent(ChangePwsActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
            }
        }
    };

    private ImageView iv_back;
    private RoundTextView rtv_ok;
    private TextView tv_phone;
    private EditText et_oldPwd, et_newPwd;

    @Override
    public int bindLayout() {
        return R.layout.activity_change_pws;
    }

    @Override
    public void initView(View view) {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        rtv_ok = (RoundTextView) findViewById(R.id.rtv_ok);
        rtv_ok.setOnClickListener(this);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_phone.setText(Utils.SafePhoneNumber(mLoginConfig.getUserName()));

        et_oldPwd = (EditText) findViewById(R.id.et_oldPwd);
        et_newPwd = (EditText) findViewById(R.id.et_newPwd);

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    private void changePW(String oldpw, String newpw) {
        String Url = new AllUrl(this).getChangePWUrl();

        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("tel", mLoginConfig.getUserName());
        builder.append("oldpw", oldpw);
        builder.append("newpw", newpw);
        String data = builder.toString();

        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(
                    new BaseRequestParm(Url, data, AsyncTaskManager.ContentTypeJson, "POST",
                            mLoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                handler.sendEmptyMessage(SUCCESS);
                            } else {
                                handler.sendEmptyMessage(FALSE);
                            }
                        }

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(FALSE);
                        }

                    }, this);
        } else {
            Utils.toast(ChangePwsActivity.this, "网络不给力呀~ o(︶︿︶)o");
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rtv_ok:
//                changePW(et_oldPwd.getText().toString().trim(), et_newPwd.getText().toString().trim());
                break;
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
