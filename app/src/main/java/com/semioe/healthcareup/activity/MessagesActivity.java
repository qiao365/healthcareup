package com.semioe.healthcareup.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.adapter.MsgsAdapter;
import com.semioe.healthcareup.bean.Msg;
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

import static com.baidu.mapapi.BMapManager.getContext;

public class MessagesActivity extends BaseActivity implements View.OnClickListener {

    private List<Message> mMessages = new ArrayList<>();
    MsgsAdapter mMessagesAdapter;
    ListView mListView;
    private TextView mTv02, mTv01, tvContent, title;
    private LinearLayout nLayoutPress01;
    private LinearLayout nLayoutPress02;
    private String str001 = "暂无数据";
    private String str002 = "暂无数据";
    ImageView img01;
    ImageView img02;
    List<Msg> mMessage1 = new ArrayList<>();
    List<Msg> mMessage2 = new ArrayList<>();

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
                finish();
                break;
            case R.id.nLayoutPress01:
                title.setText("健康提醒");
                img01.setVisibility(View.VISIBLE);
                img02.setVisibility(View.GONE);
                mTv01.setTextColor(getResources().getColor(R.color.grey_text2));
                mTv02.setTextColor(getResources().getColor(R.color.grey_text3));
                mMessagesAdapter.addMessages(mMessage1);
                break;
            case R.id.nLayoutPress02:
                title.setText("生活风险提醒");
                img02.setVisibility(View.VISIBLE);
                img01.setVisibility(View.GONE);
                mTv02.setTextColor(getResources().getColor(R.color.grey_text2));
                mTv01.setTextColor(getResources().getColor(R.color.grey_text3));

                mMessagesAdapter.addMessages(mMessage2);
                break;
        }
    }

    private void getDataBottom1() {
        String msgUrl = new AllUrl(getContext()).getMsgDataUrl();
        if (HttpUtil.isNetworkAvailable(getContext())) {
            showSweetAlertDialog(this);
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
                                mMessage1 = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(bean.response), "result").getAsJsonArray(), Msg.class);
//                                if (mMessage.size() > 0) {
//                                    str001 = mMessage.get(0).getMsg();
//                                }else {
//                                    str001="";
//                                }
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    closeSweetADialog();
                                }
                            });
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
            showSweetAlertDialog(this);
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
                                mMessage2 = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(bean.response), "result").getAsJsonArray(), Msg.class);
//                                if (mMessage.size() > 0) {
//                                    str002 = mMessage.get(0).getMsg();
//                                    handler.post(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            tvContent.setText(str002);
//                                        }
//                                    });
//                                }else {
//                                    handler.post(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            tvContent.setText("");
//                                        }
//                                    });
//                                }
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onClick(nLayoutPress02);
                                    closeSweetADialog();
                                }
                            });
                        }
                    }, getContext());
        } else {
            Utils.toast(getContext(), "网络开小差了~");
            return;
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_messages;
    }

    @Override
    public void initView(View view) {
        mTv01 = (TextView) view.findViewById(R.id.mTv01);
        mTv02 = (TextView) view.findViewById(R.id.mTv02);
        img01 = (ImageView) view.findViewById(R.id.img01);
        img02 = (ImageView) view.findViewById(R.id.img02);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        title = (TextView) view.findViewById(R.id.title);
        nLayoutPress01 = (LinearLayout) view.findViewById(R.id.nLayoutPress01);
        nLayoutPress02 = (LinearLayout) view.findViewById(R.id.nLayoutPress02);
        nLayoutPress01.setOnClickListener(this);
        nLayoutPress02.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.mListView);
        mMessagesAdapter = new MsgsAdapter(this);
        mListView.setAdapter(mMessagesAdapter);
    }

    @Override
    public void doBusiness(Context mContext) {
        getDataBottom1();
        getDataBottom2();
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeSweetADialog();
            switch (msg.what) {
                case Constants.GotoLoginActivity:
                    Utils.toast(getContext(), "登陆失效,请重新登陆");
                    new LoginConfig(getContext()).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(getContext(),
                            handler);
                    break;
            }
        }
    };

}
