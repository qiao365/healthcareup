package com.semioe.healthcareup.fragments;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.flyco.roundview.RoundTextView;
import com.google.gson.JsonArray;
import com.libzxing.zxing.activity.CaptureActivity;
import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.activity.BaseActivity;
import com.semioe.healthcareup.activity.DoctorListActivity;
import com.semioe.healthcareup.activity.HealthStatusActivity;
import com.semioe.healthcareup.activity.MessagesActivity;
import com.semioe.healthcareup.activity.NearDevicesActivity;
import com.semioe.healthcareup.activity.RiskAssessmentActivity;
import com.semioe.healthcareup.activity.ScreenListActivity;
import com.semioe.healthcareup.application.LocationApplication;
import com.semioe.healthcareup.baidu.LocationService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.semioe.healthcareup.activity.ShopActivity;
import com.semioe.healthcareup.bean.Msg;
import com.semioe.healthcareup.bean.SSY;
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

import static android.app.Activity.RESULT_OK;
import static com.baidu.mapapi.BMapManager.getContext;


/**
 * 健康解惑
 * Created by liuaixun on 2017/5/31.
 */
public class HealthFAQFragment extends Fragment implements View.OnClickListener {

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ((BaseActivity) getActivity()).closeSweetADialog();
            switch (msg.what) {
                case Constants.GET_SUCCESS:
                    Toast.makeText(getContext(), "绑定成功", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.GET_FAIL:
                    Toast.makeText(getContext(), "绑定失败", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.GET_SUCCESS2:
                    mLayout02.setVisibility(View.VISIBLE);
                    mLayout01.setVisibility(View.GONE);
                    getDataBottom1();
                    getDataBottom2();
                    break;
                case Constants.GET_FAIL2:
                    mLayout01.setVisibility(View.VISIBLE);
                    mLayout02.setVisibility(View.GONE);
                    break;
                case Constants.GotoLoginActivity:
                    Utils.toast(getContext(), "登陆失效,请重新登陆");
                    new LoginConfig(getContext()).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(getContext(),
                            handler);
                    break;
            }
        }
    };

    private static final int SCAN = 1;
    private static final String TAG = HealthFAQFragment.class.getSimpleName();

    private TextView locationTextView;
    private TextView name, mTv02, mTv01, tvContent, mTvotoPG, mTvGotoHeath;
    private LinearLayout nLayoutPress01;
    private LinearLayout nLayoutPress02;
    private LinearLayout mLayout01;
    private RelativeLayout mLayout02;
    ImageView img01;
    ImageView img02;

    public HealthFAQFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_health_faq, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout mEvaluate = (LinearLayout) view.findViewById(R.id.ll_evaluate);
        LinearLayout mConsult = (LinearLayout) view.findViewById(R.id.ll_consult);
        LinearLayout mQuery = (LinearLayout) view.findViewById(R.id.ll_query);
        ImageView img_shop = (ImageView) view.findViewById(R.id.img_shop);
        ImageView mImageScan = (ImageView) view.findViewById(R.id.img_scan);
        ImageView mImageDevices = (ImageView) view.findViewById(R.id.img_devices);
        ImageView mImageMessage = (ImageView) view.findViewById(R.id.img_message);
        img01 = (ImageView) view.findViewById(R.id.img01);
        img02 = (ImageView) view.findViewById(R.id.img02);
        RoundTextView rtv_add = (RoundTextView) view.findViewById(R.id.rtv_add);
        locationTextView = (TextView) view.findViewById(R.id.location);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        mTv01 = (TextView) view.findViewById(R.id.mTv01);
        mTv02 = (TextView) view.findViewById(R.id.mTv02);
        mTvGotoHeath = (TextView) view.findViewById(R.id.mTvGotoHeath);
        mTvotoPG = (TextView) view.findViewById(R.id.mTvotoPG);
        mLayout01 = (LinearLayout) view.findViewById(R.id.mLayout01);
        mLayout02 = (RelativeLayout) view.findViewById(R.id.mLayout02);
        nLayoutPress01 = (LinearLayout) view.findViewById(R.id.nLayoutPress01);
        nLayoutPress02 = (LinearLayout) view.findViewById(R.id.nLayoutPress02);
        name = (TextView) view.findViewById(R.id.name);
        name.setText(LoginConfig.getUserName());
        mTvGotoHeath.setOnClickListener(this);
        mTvotoPG.setOnClickListener(this);
        mEvaluate.setOnClickListener(this);
        mConsult.setOnClickListener(this);
        nLayoutPress01.setOnClickListener(this);
        nLayoutPress02.setOnClickListener(this);
        mQuery.setOnClickListener(this);
        img_shop.setOnClickListener(this);
        mImageScan.setOnClickListener(this);
        mImageDevices.setOnClickListener(this);
        mImageMessage.setOnClickListener(this);
        rtv_add.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        onClick(nLayoutPress01);
        getRecord();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_consult://咨询
                startActivity(new Intent(getActivity(), DoctorListActivity.class));
                break;
            case R.id.ll_evaluate://风险评估
                startActivity(new Intent(getActivity(), RiskAssessmentActivity.class));
                break;
            case R.id.ll_query://筛选
                startActivity(new Intent(getActivity(), DoctorListActivity.class));
                break;
            case R.id.img_shop://商城 h5
                startActivity(new Intent(getActivity(), ShopActivity.class));
                break;
            case R.id.img_scan://扫描
                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), SCAN);
                break;
            case R.id.img_devices://定位
                startActivity(new Intent(getActivity(), NearDevicesActivity.class));
                break;
            case R.id.img_message://消息
                startActivity(new Intent(getActivity(), MessagesActivity.class));
                break;
            case R.id.mTvotoPG://mTvotoPG,
                startActivity(new Intent(getActivity(), RiskAssessmentActivity.class));
                break;
            case R.id.mTvGotoHeath://,mTvGotoHeath
                startActivity(new Intent(getActivity(), HealthStatusActivity.class));
                break;
            case R.id.rtv_add://首次添加健康状况
                Intent intent = new Intent();
                intent.setClass(getActivity(), HealthStatusActivity.class);
                intent.putExtra("Flag", "first");
                startActivity(intent);
                break;
            case R.id.nLayoutPress01:
                img01.setVisibility(View.VISIBLE);
                img02.setVisibility(View.GONE);
                mTv01.setTextColor(getResources().getColor(R.color.grey_text2));
                mTv02.setTextColor(getResources().getColor(R.color.grey_text3));
                tvContent.setText(str001);
                break;
            case R.id.nLayoutPress02:
                img02.setVisibility(View.VISIBLE);
                img01.setVisibility(View.GONE);
                mTv02.setTextColor(getResources().getColor(R.color.grey_text2));
                mTv01.setTextColor(getResources().getColor(R.color.grey_text3));
                tvContent.setText(str002);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SCAN:
                    String result = data.getStringExtra("result");
                    Log.e(TAG + " scan---->", result);
                    getBindDiver(result, new LoginConfig(getContext()).getUserId());
                    break;
            }
        }
    }

    private void getBindDiver(String deviceno, String uid) {
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("deviceno", deviceno);
        builder.append("uid", uid);
        String data = builder.toString();

        String msgUrl = new AllUrl(getContext()).getBindDiverUrl();
        if (HttpUtil.isNetworkAvailable(getContext())) {
            ((BaseActivity) getActivity()).showSweetAlertDialog(getContext());
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
                                handler.sendEmptyMessage(Constants.GET_SUCCESS);
                            } else
                                handler.sendEmptyMessage(Constants.GET_FAIL);
                        }
                    }, getContext());
        } else {
            Utils.toast(getContext(), "网络开小差了~");
            return;
        }
    }


    private void getRecord() {
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("datak", "healthybymyself");
        builder.append("uid", new LoginConfig(getContext()).getUserId());
        String data = builder.toString();

        String msgUrl = new AllUrl(getContext()).getAnswerUrl();
        if (HttpUtil.isNetworkAvailable(getContext())) {
            ((BaseActivity) getActivity()).showSweetAlertDialog(getContext());
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
                                handler.sendEmptyMessage(Constants.GET_FAIL2);
                        }

                        private void strToBean(String response) {
                            if (response.contains("OK")) {
                                JsonArray mJsonArray = GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(response), "result").getAsJsonArray();
                                if (mJsonArray.size() > 0) {
                                    handler.sendEmptyMessage(Constants.GET_SUCCESS2);
                                } else {
                                    handler.sendEmptyMessage(Constants.GET_FAIL2);
                                }

                            }

                        }
                    }, getContext());
        } else {
            Utils.toast(getContext(), "网络开小差了~");
            return;
        }
    }

    private void getDataBottom1() {
        String msgUrl = new AllUrl(getContext()).getMsgDataUrl();
        if (HttpUtil.isNetworkAvailable(getContext())) {
            JsonObjectBuilder builder = new JsonObjectBuilder();
            builder.append("uid", LoginConfig.getUserId());
            builder.append("type", "0"); //0健康小贴士1生活风险提示
            builder.append("begintime", "2010-09-11 00:00:00");
            builder.append("endtime", "2018-01-11 00:00:00");
            builder.append("page.pageNum", "1");
            builder.append("page.pageSize", "10");

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
                                List<Msg> mMessage = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(bean.response), "result").getAsJsonArray(), Msg.class);
                                if (mMessage.size() > 0) {
                                    str001 = mMessage.get(0).getMsg();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvContent.setText(str001);
                                        }
                                    });
                                }else{
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvContent.setText("");
                                        }
                                    });
                                }
                            }
                        }
                    }, getContext());
        } else {
            Utils.toast(getContext(), "网络开小差了~");
            return;
        }
    }

    private void getDataBottom2() {
        String msgUrl = new AllUrl(getContext()).getMsgDataUrl();
        if (HttpUtil.isNetworkAvailable(getContext())) {
            JsonObjectBuilder builder = new JsonObjectBuilder();
            builder.append("uid", LoginConfig.getUserId());
            builder.append("type", "1");
            builder.append("begintime", "2010-09-11 00:00:00");
            builder.append("endtime", "2018-01-11 00:00:00");
            builder.append("page.pageNum", "1");
            builder.append("page.pageSize", "10");

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
                                List<Msg> mMessage = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(bean.response), "result").getAsJsonArray(), Msg.class);
                                if (mMessage.size() > 0) {
                                    str002 = mMessage.get(0).getMsg();
                                }else {
                                    str002 = "";
                                }
                            }
                        }
                    }, getContext());
        } else {
            Utils.toast(getContext(), "网络开小差了~");
            return;
        }
    }

    private String str001 = "暂无数据";
    private String str002 = "暂无数据";

    //百度地图定位
    private LocationService locationService;

    /***
     * Stop location service
     */
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        locationService = ((LocationApplication) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getActivity().getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }


    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        locationTextView.setText(getAmPm() + "好！您现在定位为 " + location.getCity());
                        locationService.stop();
                    }
                });
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    //获取上下午
    private String getAmPm() {
        long time = System.currentTimeMillis();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int apm = mCalendar.get(Calendar.AM_PM);
        if (apm == 0) {
            return "上午";
        } else {
            return "下午";
        }
    }
}
