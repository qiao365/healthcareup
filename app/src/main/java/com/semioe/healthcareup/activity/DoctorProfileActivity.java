package com.semioe.healthcareup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.adapter.SymptomAdapter;
import com.semioe.healthcareup.bean.Doctors;
import com.semioe.healthcareup.bean.ServiceItem;
import com.semioe.healthcareup.views.ListViewForScrollView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//医生简介
public class DoctorProfileActivity extends BaseActivity implements View.OnClickListener {

    private TextView hospitalNameTV;
    private TextView mTVjianjie;
    private TextView doctorName;
    private TextView doctorPosition;
    private ScrollView mScrollView;
    private ListViewForScrollView mListViewForScrollView;
    private ImageView myImgView;
    private SymptomAdapter adapter;
    private int defaltNum = 1;
    private  String name;

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
            case R.id.ll_consult://咨询
                defaltNum = 1;
                mList1.clear();
                mList2.clear();
                mList1.add(new ServiceItem("心血管疾病", "5元"));
                mList2.add(new ServiceItem("肾病及痛风", "5元"));
                adapter.setstType("咨询");
                setData();
                break;
            case R.id.ll_query://筛查
                defaltNum = 2;
                mList1.clear();
                mList2.clear();
                mList1.add(new ServiceItem("头晕症状", "5元"));
                mList1.add(new ServiceItem("头痛症状", "5元"));
                mList1.add(new ServiceItem("咽部不适症状", "5元"));
                mList1.add(new ServiceItem("胸痛症状", "5元"));
                mList1.add(new ServiceItem("颈部不适症状", "5元"));
                mList1.add(new ServiceItem("呼吸困难症状", "5元"));

                mList2.add(new ServiceItem("关节疼痛症状", "5元"));
                mList2.add(new ServiceItem("肢端麻木症状", "5元"));
                mList2.add(new ServiceItem("头晕症状", "5元"));
                mList2.add(new ServiceItem("头痛症状", "5元"));
                adapter.setstType("筛查");
                setData();
                break;
            case R.id.ll_evaluate://风险评估
                defaltNum = 3;
                mList1.clear();
                mList2.clear();
                mList1.add(new ServiceItem("高血压", "50元"));
                mList2.add(new ServiceItem("痛风", "50元"));
                adapter.setstType("风险评估");
                setData();
                break;
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_doctor_profile;
    }

    @Override
    public void initView(View view) {
        hospitalNameTV = (TextView) findViewById(R.id.hospitalNameTV);
        mTVjianjie = (TextView) findViewById(R.id.mTVjianjie);
        doctorName = (TextView) findViewById(R.id.doctorName);
        doctorPosition = (TextView) findViewById(R.id.doctorPosition);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);
        mListViewForScrollView = (ListViewForScrollView) findViewById(R.id.mListViewForScrollView);
        myImgView = (ImageView) findViewById(R.id.myImgView);

    }

    private void setData(){
        if (name.equals("施海峰")) {
            adapter.addServiceItems(mList1);
        } else {
            adapter.addServiceItems(mList2);
        }
        mScrollView.smoothScrollTo(0, 0);
    }

    @Override
    public void doBusiness(Context mContext) {
        final Intent intent = this.getIntent();
        final Doctors.Doctor mDoctor = (Doctors.Doctor) intent.getSerializableExtra("doctor");
        hospitalNameTV.setText(mDoctor.getHospital());
        mTVjianjie.setText(mDoctor.getGoodat());
        doctorName.setText(mDoctor.getDname());
        doctorPosition.setText(mDoctor.getDlevel());
        Picasso.with(mContext)
                .load(mDoctor.getHead_pic())
                .error(R.mipmap.avatar)
                .fit()
                .centerCrop()
                .into(myImgView);

        setDefortData();
        adapter = new SymptomAdapter(this) {
            @Override
            public void btnPress(ServiceItem mServiceItem, int position) {
                Intent mIntent = new Intent(DoctorProfileActivity.this,BuyServiceActivity.class);
                switch (defaltNum) {
                    case 1:
                        mIntent.putExtra("type","咨询");
                        break;
                    case 2:
                        mIntent.putExtra("type","筛查");
                        break;
                    case 3:
                        mIntent.putExtra("type","风险评估");
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("doctor", mDoctor);
                bundle.putSerializable("ServiceItem", mServiceItem);
                bundle.putSerializable("ServiceItems", adapter.getServiceItems());
                mIntent.putExtras(bundle);
                startActivity(mIntent);
            }
        };
        mListViewForScrollView.setAdapter(adapter);

        name = mDoctor.getDname();
        setData();
    }

    List<ServiceItem> mList1 = new ArrayList<>();
    List<ServiceItem> mList2 = new ArrayList<>();

    private void setDefortData() {
        mList1.add(new ServiceItem("心血管疾病", "5元"));

        mList2.add(new ServiceItem("肾病及痛风", "5元"));
    }
}
