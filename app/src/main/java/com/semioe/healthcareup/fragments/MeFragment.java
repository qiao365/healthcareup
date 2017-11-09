package com.semioe.healthcareup.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.libzxing.zxing.activity.CaptureActivity;
import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.activity.BaseActivity;
import com.semioe.healthcareup.activity.CalendarActivity;
import com.semioe.healthcareup.activity.ChangePwsActivity;
import com.semioe.healthcareup.activity.HealthStatusActivity;
import com.semioe.healthcareup.activity.MyDevicesActivity;
import com.semioe.healthcareup.activity.MyServicesActivity;
import com.semioe.healthcareup.activity.ShenGongActivity;
import com.semioe.healthcareup.activity.ShopActivity;
import com.semioe.healthcareup.activity.NearDevicesActivity;
import com.semioe.healthcareup.human.HumanBodyActivity;
import com.semioe.healthcareup.utils.LoginConfig;

import static android.app.Activity.RESULT_OK;


/**
 * 个人中心
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MeFragment.class.getSimpleName();
    private static final int SCAN = 1;
    private Context mContext;
    private LoginConfig mLoginConfig;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    public MeFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = this.getActivity();
        mLoginConfig = new LoginConfig(mContext);
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar);
        RelativeLayout center_my_service = (RelativeLayout) view.findViewById(R.id.center_my_service);
        RelativeLayout center_my_devices = (RelativeLayout) view.findViewById(R.id.center_my_devices);
        RelativeLayout center_shop = (RelativeLayout) view.findViewById(R.id.center_shop);
        RelativeLayout center_add_record = (RelativeLayout) view.findViewById(R.id.center_add_record);
        RelativeLayout center_near_devices = (RelativeLayout) view.findViewById(R.id.center_near_devices);
        RelativeLayout rl_change_password = (RelativeLayout) view.findViewById(R.id.rl_change_password);
        TextView tv_exit = (TextView) view.findViewById(R.id.tv_exit);
        TextView tv_change_info = (TextView) view.findViewById(R.id.tv_change_info);
        tv_change_info.setOnClickListener(this);
        center_my_service.setOnClickListener(this);
        avatar.setOnClickListener(this);
        center_my_devices.setOnClickListener(this);
        center_shop.setOnClickListener(this);
        center_add_record.setOnClickListener(this);
        center_near_devices.setOnClickListener(this);
        rl_change_password.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit:
                mLoginConfig.setAvailbleTime("0");
                mLoginConfig.setAuthorization("");
                AppManager.getInstance().AppExit(mContext);
                break;
            case R.id.center_my_service:
//                startActivity(new Intent(mContext, MyServicesActivity.class));
                break;
            case R.id.center_add_record://补充健康记录
                startActivity(new Intent(mContext, CalendarActivity.class));
                break;
            case R.id.center_near_devices://附近设备
                startActivity(new Intent(mContext, NearDevicesActivity.class));
                break;
            case R.id.center_my_devices:
                startActivity(new Intent(mContext, MyDevicesActivity.class));
                break;
            case R.id.center_shop://商城 h5
                startActivity(new Intent(mContext, ShopActivity.class));
                break;
            case R.id.rl_change_password://修改密码
                startActivity(new Intent(mContext, ChangePwsActivity.class));
                break;
            case R.id.avatar://头像
//                startActivity(new Intent(mContext, ShenGongActivity.class));
                break;
            case R.id.tv_change_info://修改信息

                break;
        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case SCAN:
//                    String result = data.getStringExtra("result");
//                    Log.e(TAG + " scan", result);
//                    break;
//
//            }
//        }
//    }
//
//    private void scan() {
//        Intent intent = new Intent(mContext, CaptureActivity.class);
//        startActivityForResult(intent, SCAN);
//    }


}
