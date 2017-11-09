package com.semioe.healthcareup.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.adapter.ArchivesItemAdapter;
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
import com.semioe.healthcareup.utils.TimeUtils;
import com.semioe.healthcareup.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShenGongActivity extends BaseActivity implements View.OnClickListener {

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

    private String starttime = TimeUtils.getBefore3mDate();
    private String endtime = TimeUtils.getCurrentData();

    @Override
    public int bindLayout() {
        return R.layout.activity_shen_gong;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    ListView mListView;
    String type = "renal_function";
    ArchivesItemAdapter mArchivesItemAdapter;
    List<SSY> mSG = new ArrayList<>();//血尿酸

    @Override
    public void initView(View view) {
        TextView title = (TextView) view.findViewById(R.id.title);
        mListView = (ListView) view.findViewById(R.id.mListView);
        mArchivesItemAdapter = new ArchivesItemAdapter(this);
        mListView.setAdapter(mArchivesItemAdapter);
        String name = getIntent().getExtras().getString("name", "");
        if (getIntent().getExtras().getString("starttime") != null) {
            starttime = getIntent().getExtras().getString("starttime");
            endtime = TimeUtils.getNextDate(starttime);
        }
        title.setText(name);
        type = getIntent().getExtras().getString("type", "renal_function");
        if (name.equals("肾功")) {
            mArchivesItemAdapter.setKeysNames(
                    getResources().getStringArray(R.array.testIndex_sg_key), getResources().getStringArray(R.array.testIndex_sg)
            );
        } else if (name.equals("血脂")) {
            mArchivesItemAdapter.setKeysNames(
                    getResources().getStringArray(R.array.testIndex_xz_key), getResources().getStringArray(R.array.testIndex_xz)
            );
        } else {
            mArchivesItemAdapter.setKeysNames(
                    getResources().getStringArray(R.array.testIndex_ncg_key), getResources().getStringArray(R.array.testIndex_ncg)
            );
        }
    }

    @Override
    public void doBusiness(Context mContext) {
        getDate();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            closeSweetADialog();
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.GET_SUCCESS:
                    mArchivesItemAdapter.addSSYs(mSG);
                    break;
                case Constants.GET_FAIL:

                    break;
                case Constants.GotoLoginActivity:
                    Utils.toast(getApplication(), "登陆失效,请重新登陆");
                    new LoginConfig(getApplication()).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(getApplication(),
                            handler);
                    break;
            }
        }
    };

    private void getDate() {
        String msgUrl = new AllUrl(getApplication()).getHealthDataUrl();
        if (HttpUtil.isNetworkAvailable(getApplication())) {
            showSweetAlertDialog(this);
            JsonObjectBuilder builder = new JsonObjectBuilder();
            builder.append("uid", LoginConfig.getUserId());
//            builder.append("uid", "158");
//            builder.append("datav", type);
            builder.append("type", type);
            builder.append("begintime", starttime);
            builder.append("endtime", endtime);
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
                            } else {
                                handler.sendEmptyMessage(Constants.GET_FAIL);
                            }
                        }

                        private void strToBean(String response) {
                            if (response.contains("OK")) {
                                mSG = new ArrayList<SSY>();
                                mSG = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(response), "result").getAsJsonArray(), SSY.class);
                                Collections.reverse(mSG); // 倒序排列
                                handler.sendEmptyMessage(Constants.GET_SUCCESS);
                            }
                        }
                    }, getApplication());
        } else {
            Utils.toast(getApplication(), "网络开小差了~");
            return;
        }
    }
}
