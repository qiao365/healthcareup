package com.semioe.healthcareup.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.adapter.ServicesAdapter;


public class MyServicesActivity extends BaseActivity implements View.OnClickListener {

    private ListView listServices;
    private ServicesAdapter servicesAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_my_services;
    }

    @Override
    public void initView(View view) {
        listServices = (ListView) findViewById(R.id.list_services);
        TextView title = (TextView) findViewById(R.id.title);
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        title.setText("我的服务");
        servicesAdapter = new ServicesAdapter(this);
        listServices.setAdapter(servicesAdapter);

    }

    @Override
    public void doBusiness(Context mContext) {

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

}
