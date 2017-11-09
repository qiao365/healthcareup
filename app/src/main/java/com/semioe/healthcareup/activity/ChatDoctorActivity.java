package com.semioe.healthcareup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.bean.Devices;
import com.semioe.healthcareup.bean.Doctors;
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

public class ChatDoctorActivity extends BaseActivity implements View.OnClickListener {
    Doctors.Doctor mDoctor;
    TextView content;
    Devices mDATA;
    private WebView webview;
    private ImageView iv_back;
    private String msgUrl = "http://api.php-x.com/dialog?doctorId=58cbaccc7027fd633298d8b0&userId=5&code=203";

    private int procedureId = 0;
    @Override
    public int bindLayout() {
        return R.layout.activity_shop;
    }

    @Override
    public void initView(View view) {
        webview = (WebView) findViewById(R.id.webview);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        TextView title = (TextView) findViewById(R.id.title);
        Intent intent = this.getIntent();
        mDoctor = (Doctors.Doctor) intent.getSerializableExtra("doctor");
        procedureId = intent.getExtras().getInt("procedureId",0);
        title.setText("与" + mDoctor.getDname() + "医生的对话");
    }

    @Override
    public void doBusiness(Context mContext) {
        getAllDates();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
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

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeSweetADialog();
            switch (msg.what) {
                case Constants.GET_SUCCESS:
                    msgUrl = new AllUrl(ChatDoctorActivity.this).getStartHHUrl() + "?doctorId=" + mDoctor.getRefid() + "&userId=" + LoginConfig.getUserId()
                            + "&code=" + mDATA.getCode();
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.loadUrl(msgUrl);
                    webview.setWebViewClient(new HelloWebViewClient());
                    break;
                case Constants.GET_FAIL:

                    break;
                case Constants.GotoLoginActivity:
                    Utils.toast(ChatDoctorActivity.this, "登陆失效,请重新登陆");
                    new LoginConfig(ChatDoctorActivity.this).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(ChatDoctorActivity.this,
                            handler);
                    break;
            }
        }
    };


    private void getAllDates() {
        String msgUrl = new AllUrl(this).getCreateHHUrl();
        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            JsonObjectBuilder builder = new JsonObjectBuilder();
            builder.append("userId", LoginConfig.getUserId() + "");
            builder.append("doctorId", mDoctor.getRefid());
            builder.append("procedureId", procedureId+"");
            String data = builder.toString();
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(msgUrl, data,
                            AsyncTaskManager.ContentTypeJson, "POST", mLoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(Constants.GotoLoginActivity);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                strToBean(bean.response);
                            } else
                                handler.sendEmptyMessage(Constants.GET_FAIL);
                        }
                    }, this);
        } else {
            Utils.toast(this, "网络开小差了~");
            return;
        }
    }

    private void strToBean(String data) {
        mDATA = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(data), Devices.class);
        handler.sendEmptyMessage(Constants.GET_SUCCESS);
    }
}
