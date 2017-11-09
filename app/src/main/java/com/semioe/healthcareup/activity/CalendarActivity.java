package com.semioe.healthcareup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
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
import com.semioe.healthcareup.views.date.MonthDateView;
import com.spring.stepcounter.simplestepcounter.utils.TimeUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_left;
    private ImageView iv_right;
    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;
    private MonthDateView monthDateView;
    List<Integer> listDate = new ArrayList<Integer>();
    String startTime = TimeUtils.getCurrentFirstDate();
    String endtTime = TimeUtils.getNextFirstDate(new Date(), 1);

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

    @Override
    public int bindLayout() {
        return R.layout.activity_calendar;
    }

    @Override
    public void initView(View view) {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        tv_date = (TextView) findViewById(R.id.date_text);
        tv_week = (TextView) findViewById(R.id.week_text);
        tv_today = (TextView) findViewById(R.id.tv_today);
        monthDateView.setTextView(tv_date, tv_week);

        monthDateView.setDateClick(new MonthDateView.DateClick() {

            @Override
            public void onClickOnDate() {
//                if (listDate.contains(monthDateView.getmSelDay())) {
//                    String today = monthDateView.getmSelYear()+"-"+monthDateView.getmSelMonth()+"-"+monthDateView.getmSelDay()+" 00:00:00";
//                    String nextDate = TimeUtils.getNextDate(today);
//                    Intent int1 = new Intent(CalendarActivity.this, ShenGongActivity.class);
//                    int1.putExtra("name", "既往数据");
//                    int1.putExtra("type", "renal_function");
//                    int1.putExtra("starttime", today);
//                    startActivity(int1);
//                } else {
//                Toast.makeText(getApplication(), "点击了：" + monthDateView.getmSelDay(), Toast.LENGTH_SHORT).show();
//                补充健康记录
                    startActivity(new Intent(getApplication(), HealthStatusActivity.class));
//                }
            }
        });
        setOnlistener();
    }

    private void setOnlistener() {
        iv_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utils.isFastClick()) {
                    return;
                }
                endtTime = TimeUtils.getNextFirstDate(endtTime, -1);
                startTime = TimeUtils.getNextFirstDate(startTime, -1);
                monthDateView.onLeftClick();
                getDate();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utils.isFastClick()) {
                    return;
                }
                endtTime = TimeUtils.getNextFirstDate(endtTime, 1);
                startTime = TimeUtils.getNextFirstDate(startTime, 1);
                monthDateView.onRightClick();
                getDate();
            }
        });

        tv_today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utils.isFastClick()) {
                    return;
                }
                monthDateView.setTodayToView();
            }
        });
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeSweetADialog();
            switch (msg.what) {
                case Constants.GET_SUCCESS:
                    monthDateView.setDaysHasThingList(listDate);
                    break;
                case Constants.GET_FAIL:

                    break;
                case Constants.GotoLoginActivity:
                    Utils.toast(CalendarActivity.this, "登陆失效,请重新登陆");
                    new LoginConfig(CalendarActivity.this).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(CalendarActivity.this,
                            handler);
                    break;
            }
        }
    };


    @Override
    public void doBusiness(Context mContext) {
        getDate();
    }

    private void getDate() {
        String msgUrl = new AllUrl(this).getHealthDataUrl();
        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            JsonObjectBuilder builder = new JsonObjectBuilder();
            builder.append("uid", LoginConfig.getUserId());
            builder.append("begintime", startTime);
            builder.append("endtime", endtTime);
            final String data = builder.toString();
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
                                List<SSY> mSSY = new ArrayList<SSY>();
                                mSSY = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(response), "result").getAsJsonArray(), SSY.class);

                                Date[] m;
                                listDate.clear();
                                for (SSY item : mSSY) {
                                    if (item.getCreateDate() == null || item.getCreateDate().equals(""))
                                        continue;
                                    Date date = TimeUtils.getDate(item.getCreateDate());
                                    Calendar now = Calendar.getInstance();
                                    now.setTime(date);
                                    int day = now.get(Calendar.DAY_OF_MONTH);
                                    if (!listDate.contains(day)) {
                                        listDate.add(day);
                                    }
                                }
                                handler.sendEmptyMessage(Constants.GET_SUCCESS);
                            }
                        }
                    }, this);
        } else {
            Utils.toast(this, "网络开小差了~");
            return;
        }
    }
}
