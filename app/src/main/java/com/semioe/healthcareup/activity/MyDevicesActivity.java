package com.semioe.healthcareup.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.adapter.DevicesAdapter;
import com.semioe.healthcareup.application.LocationApplication;
import com.semioe.healthcareup.baidu.LocationService;
import com.semioe.healthcareup.bean.Device;
import com.semioe.healthcareup.bean.Devices;
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

import java.util.ArrayList;
import java.util.List;

//我的设备

public class MyDevicesActivity extends BaseActivity implements View.OnClickListener {

    private List<Device> mDevices = new ArrayList<>();
    DevicesAdapter mDevicesAdapter;
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public int bindLayout() {
        return R.layout.activity_my_devices;
    }

    @Override
    public void initView(View view) {
        TextView title = (TextView) findViewById(R.id.title);
        mListView = (ListView) findViewById(R.id.mListView);
        title.setText("我的设备");
        mListView = (ListView) findViewById(R.id.mListView);
        mDevicesAdapter = new DevicesAdapter(this);
        mListView.setAdapter(mDevicesAdapter);
    }

    @Override
    public void doBusiness(Context mContext) {
        getAllDevices();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
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
                    showDevices(mDevices);
                    break;
                case Constants.GET_FAIL:

                    break;
                case Constants.GotoLoginActivity:
                    Utils.toast(MyDevicesActivity.this, "登陆失效,请重新登陆");
                    new LoginConfig(MyDevicesActivity.this).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(MyDevicesActivity.this,
                            handler);
                    break;
            }
        }
    };

    //显示网络设备
    private void showDevices(List<Device> mDevices) {
        mDevicesAdapter.addDevices(mDevices);
    }

    private void getAllDevices() {
        String msgUrl = new AllUrl(this).getMyDevicesUrl();
        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            JsonObjectBuilder builder = new JsonObjectBuilder();
            builder.append("uid", LoginConfig.getUserId());
//            builder.append("uid", "465456128");
            String data = builder.toString();
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(msgUrl, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
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
        if(data.contains("50002")){//{"errcode":50002,"msg":"用户不存在或者未登录","result":""}
            return;
        }
        Devices mmDoctors = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(data), Devices.class);
        mDevices = mmDoctors.getResult();
        handler.sendEmptyMessage(Constants.GET_SUCCESS);
    }
}
