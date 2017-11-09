package com.semioe.healthcareup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.bean.Doctors;
import com.semioe.healthcareup.bean.ServiceItem;
import com.semioe.healthcareup.dialogs.SuccessfulPaymentDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//购买服务项目
public class BuyServiceActivity extends BaseActivity implements View.OnClickListener {

    TextView title;
    TextView doctorName;
    TextView mPosition;
    TextView hospitalName;
    TextView mtvShanChang;
    ImageView myImgView;
    TextView mTVtypeName;
    TextView mTVxunwen;
    TextView Prece;
    Doctors.Doctor mDoctor;
    int procedureId = 0;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.buyTextView:
                //初始化一个自定义的Dialog
                dialog = new SuccessfulPaymentDialog(this,
                        R.style.custom_dialog, onDialogClickListener);
                dialog.show();
                break;
        }
    }

    private SuccessfulPaymentDialog dialog;
    private View.OnClickListener onDialogClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            switch (view.getId()) {
                case R.id.close:
                    break;
                case R.id.gotoService:
                    Intent mIntent = new Intent(BuyServiceActivity.this, ChatDoctorActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("doctor", mDoctor);
                    bundle.putSerializable("procedureId", procedureId);
                    mIntent.putExtras(bundle);
                    startActivity(mIntent);
                    break;
            }
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_buy_service;
    }

    @Override
    public void initView(View view) {
        title = (TextView) findViewById(R.id.title);
        doctorName = (TextView) findViewById(R.id.doctorName);
        mPosition = (TextView) findViewById(R.id.mPosition);
        hospitalName = (TextView) findViewById(R.id.hospitalName);
        mtvShanChang = (TextView) findViewById(R.id.mtvShanChang);
        myImgView = (ImageView) findViewById(R.id.myImgView);
        mTVtypeName = (TextView) findViewById(R.id.mTVtypeName);
        mTVxunwen = (TextView) findViewById(R.id.mTVxunwen);
        Prece = (TextView) findViewById(R.id.Prece);
    }

    @Override
    public void doBusiness(Context mContext) {
        Intent intent = this.getIntent();
        mDoctor = (Doctors.Doctor) intent.getSerializableExtra("doctor");
        final ServiceItem mServiceItem = (ServiceItem) intent.getSerializableExtra("ServiceItem");
        final ArrayList<ServiceItem> ServiceItems = (ArrayList<ServiceItem>) intent.getSerializableExtra("ServiceItems");
        String type = intent.getExtras().getString("type", "");
        title.setText("购买" + type + "服务");
        doctorName.setText(mDoctor.getDname());
        mPosition.setText(mDoctor.getDlevel());
        hospitalName.setText(mDoctor.getHospital());
        setprocedureId(mServiceItem.getServiceName());
        String mShanChang = "";
        for (ServiceItem m : ServiceItems) {
            mShanChang += (m.getServiceName() + " ");
        }
        mtvShanChang.setText(mShanChang);
        Picasso.with(mContext)
                .load(mDoctor.getHead_pic())
                .error(R.mipmap.avatar)
                .fit()
                .centerCrop()
                .into(myImgView);
        mTVtypeName.setText(mServiceItem.getServiceName());

        if(ServiceItems.size()>1){
            mTVxunwen.setText("您可以询问有关" + ServiceItems.get(0).getServiceName() + "," + ServiceItems.get(1).getServiceName() + "等，医生接单后24小时以内，您有任务相关问题都可以咨询医生");
        }else{
            if (type.equals("风险评估")){
                mTVxunwen.setText(mServiceItem.getContent());
                Prece.setText("¥ 50元");
            }else {
                mTVxunwen.setText("您可以询问有关" + ServiceItems.get(0).getServiceName() + "等，医生接单后24小时以内，您有任务相关问题都可以咨询医生");
            }
        }


    }

    private void setprocedureId(String type) {
        switch (type){
            case "高血压风险评估":
                procedureId = 41;
                break;
            case "痛风风险评估":
                procedureId = 123;
                break;
            case "头晕症状筛查":
                procedureId = 35;
                break;
            case "头痛症状筛查":
                procedureId = 36;
                break;
            case "关节疼痛症状筛查":
                procedureId = 38;
                break;
            case "肢端麻木症状筛查":
                procedureId = 570;
                break;
        }
    }
}
