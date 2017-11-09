package com.semioe.healthcareup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.adapter.DoctorAdapter;
import com.semioe.healthcareup.bean.Doctors;
import com.semioe.healthcareup.http.httputils.AllUrl;
import com.semioe.healthcareup.http.httputils.AsyncTaskManager;
import com.semioe.healthcareup.http.httputils.GsonUtils;
import com.semioe.healthcareup.http.httputils.HttpUtil;
import com.semioe.healthcareup.http.requestparams.BaseRequestParm;
import com.semioe.healthcareup.http.responsebeans.BaseResponseBean;
import com.semioe.healthcareup.http.responsebeans.RequestListener;
import com.semioe.healthcareup.utils.LoginConfig;
import com.semioe.healthcareup.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class DoctorListActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = DoctorListActivity.class.getSimpleName();
    private ListView listDoctor;
    private DoctorAdapter adapter;
    private List<Doctors.Doctor> mDoctors = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_doctor_list;
    }

    @Override
    public void initView(View view) {
        listDoctor = (ListView) findViewById(R.id.list_doctor);
        TextView title = (TextView) findViewById(R.id.title);
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        title.setText("医生列表");
        adapter = new DoctorAdapter(this);
        listDoctor.setAdapter(adapter);
        listDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mIntent = new Intent(DoctorListActivity.this, DoctorProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("doctor", mDoctors.get(i));
                mIntent.putExtras(bundle);
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void doBusiness(Context mContext) {
        getAllDoctors();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeSweetADialog();
            switch (msg.what) {
                case Constants.GET_SUCCESS:
                    adapter.addDoctors(mDoctors);
                    break;
                case Constants.GET_FAIL:

                    break;
                case Constants.GotoLoginActivity:
                    Utils.toast(DoctorListActivity.this, "登陆失效,请重新登陆");
                    new LoginConfig(DoctorListActivity.this).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(DoctorListActivity.this,
                            handler);
                    break;
            }
        }
    };

    private void getAllDoctors() {
        String msgUrl = new AllUrl(this).getDoctorsUrl();
        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(msgUrl, "",
                            AsyncTaskManager.ContentTypeXfl, "GET", LoginConfig.getAuthorization()),
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
        Doctors mmDoctors = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(data), Doctors.class);
        mDoctors = mmDoctors.getResult();
        handler.sendEmptyMessage(Constants.GET_SUCCESS);
    }
}
