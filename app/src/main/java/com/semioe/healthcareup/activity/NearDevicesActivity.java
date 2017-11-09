package com.semioe.healthcareup.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;

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
import com.semioe.healthcareup.application.LocationApplication;
import com.semioe.healthcareup.baidu.LocationService;
import com.semioe.healthcareup.bean.Device;
import com.semioe.healthcareup.bean.Devices;
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

//附近的设备

public class NearDevicesActivity extends BaseActivity implements View.OnClickListener {

    private MapView mapView;
    private  BaiduMap mBaiduMap;
    private List<Device> mDevices = new ArrayList<>();
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
        return R.layout.activity_near_devices;
    }

    @Override
    public void initView(View view) {
        //绑定XML中得 mapview 控件
        mapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定义Maker坐标点
        List<LatLng> LatLngs = new ArrayList<LatLng>();
//        LatLngs.add(new LatLng(39.9281700000,116.2632700000));
//        LatLngs.add(new LatLng(40.0530410000,116.4124120000));
//        LatLngs.add(new LatLng(39.8749700000,116.3753400000));
        for (LatLng point:LatLngs) {
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.lac_ico);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }

//		mapView.getMap().getMapStatus().zoom =
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

    //百度地图定位
    private LocationService locationService;

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        locationService = ((LocationApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }


    /*****
     * @see copy funtion to you project
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        navigateTo(location);
                    }
                });
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };


    boolean ifFrist = true;
    private void navigateTo(BDLocation location) {
        // 按照经纬度确定地图位置
//        if (ifFrist) {
//            LatLng ll = new LatLng(location.getLatitude(),
//                    location.getLongitude());
//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
//            // 移动到某经纬度
//            mBaiduMap.animateMapStatus(update);
//            update = MapStatusUpdateFactory.zoomBy(5f);
//            // 放大
//            mBaiduMap.animateMapStatus(update);
//            ifFrist = false;
//        }

        // 设置自定义图标
//        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.mipmap.ic_shop);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS,true, null);
        mBaiduMap.setMyLocationConfigeration(config);
        // 第一次定位时，将地图位置移动到当前位置
        if (ifFrist)
        {
            ifFrist = false;
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
        }

        // 显示个人位置图标
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        builder.accuracy(500);//自定义误差半径
        MyLocationData data = builder.build();
        mBaiduMap.setMyLocationData(data);

        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
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
                    Utils.toast(NearDevicesActivity.this, "登陆失效,请重新登陆");
                    new LoginConfig(NearDevicesActivity.this).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(NearDevicesActivity.this,
                            handler);
                    break;
            }
        }
    };

    //显示网络设备
    private void showDevices(List<Device> mDevices) {
        for (Device mDevice:mDevices) {
            if (mDevice.getLocation() == null || mDevice.getLocation().equals("")){
                continue;
            }
            String [] str = mDevice.getLocation().split(",");
            LatLng mLatLng = new LatLng(Double.valueOf(str[0]).doubleValue(),Double.valueOf(str[1]).doubleValue());
            //构建Marker图标
            BitmapDescriptor bitmap;
            if (mDevice.getType().equals("健康一体机")) {
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.lac_ico);
            }else{
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.my_local_iv);
            }
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(mLatLng)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }
    }

    private void getAllDevices() {
        String msgUrl = new AllUrl(this).getDevicesUrl()+"?page.pageNum=1&page.pageSize=170";
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
        Devices mmDoctors = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(data), Devices.class);
        mDevices = mmDoctors.getResult();
        handler.sendEmptyMessage(Constants.GET_SUCCESS);
    }
}
