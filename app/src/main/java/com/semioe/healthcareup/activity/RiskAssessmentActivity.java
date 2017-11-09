package com.semioe.healthcareup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.adapter.RiskAssessmentItemAdapter;
import com.semioe.healthcareup.bean.Doctors;
import com.semioe.healthcareup.bean.ServiceItem;
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

//风险评估
public class RiskAssessmentActivity extends BaseActivity implements View.OnClickListener {

    private ListView listScreen;
    private RiskAssessmentItemAdapter mRiskAssessmentItemAdapter;
    private ListView listDoctor;
    private List<Doctors.Doctor> mDoctors = new ArrayList<>();
    ArrayList<ServiceItem> mList1 = new ArrayList<>();
    ArrayList<ServiceItem> mList2 = new ArrayList<>();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_screen_list;
    }

    @Override
    public void initView(View view) {
        TextView title = (TextView) findViewById(R.id.title);
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        title.setText("风险评估");
        listScreen = (ListView) findViewById(R.id.list_screen);
        mRiskAssessmentItemAdapter = new RiskAssessmentItemAdapter(this) {
            @Override
            public void btnPress(Doctors.Doctor mDoctor, int position) {
                Intent mIntent = new Intent(RiskAssessmentActivity.this,BuyServiceActivity.class);
                 mIntent.putExtra("type","风险评估");
                Bundle bundle = new Bundle();
                bundle.putSerializable("doctor", mDoctor);
                if (mDoctor.getDname().equals("施海峰")){
                    bundle.putSerializable("ServiceItem", new ServiceItem("高血压风险评估","¥ 50元","评估高血压患者饮食营养、生活习惯和用药合理性等。"));
                }else {
                    bundle.putSerializable("ServiceItem", new ServiceItem("痛风风险评估","¥ 50元","评估痛风患者痛风性关节炎急性发作风险，生活习惯和用药合理性等。"));
                }
                if (mDoctor.getDname().equals("施海峰")) {
                    bundle.putSerializable("ServiceItems", mList1);
                } else {
                    bundle.putSerializable("ServiceItems", mList2);
                }

                mIntent.putExtras(bundle);
                startActivity(mIntent);

            }
        };
        listScreen.setAdapter(mRiskAssessmentItemAdapter);
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

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeSweetADialog();
            switch (msg.what) {
                case Constants.GET_SUCCESS:
                    mRiskAssessmentItemAdapter.addDoctors(mDoctors);
                    break;
                case Constants.GET_FAIL:

                    break;
                case Constants.GotoLoginActivity:
                    Utils.toast(RiskAssessmentActivity.this, "登陆失效,请重新登陆");
                    new LoginConfig(RiskAssessmentActivity.this).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(RiskAssessmentActivity.this,
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
