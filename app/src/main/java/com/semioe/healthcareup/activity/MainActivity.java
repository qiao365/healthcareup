package com.semioe.healthcareup.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.fragments.HealthArchivesFragment;
import com.semioe.healthcareup.fragments.HealthFAQFragment;
import com.semioe.healthcareup.fragments.MeFragment;
import com.semioe.healthcareup.http.httputils.AllUrl;
import com.semioe.healthcareup.http.httputils.AsyncTaskManager;
import com.semioe.healthcareup.http.httputils.HttpUtil;
import com.semioe.healthcareup.http.httputils.JsonObjectBuilder;
import com.semioe.healthcareup.http.requestparams.BaseRequestParm;
import com.semioe.healthcareup.http.responsebeans.BaseResponseBean;
import com.semioe.healthcareup.http.responsebeans.RequestListener;
import com.semioe.healthcareup.utils.LoginConfig;
import com.semioe.healthcareup.utils.Utils;
import com.spring.stepcounter.simplestepcounter.bean.StepEntity;
import com.spring.stepcounter.simplestepcounter.constant.Constant;
import com.spring.stepcounter.simplestepcounter.db.StepDataDao;
import com.spring.stepcounter.simplestepcounter.service.StepService;
import com.spring.stepcounter.simplestepcounter.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mCenter;
    private RelativeLayout mHealthArchives;
    private RelativeLayout mHealthFAQ;
    private ViewPager mViewPager;
    private String CURRENT_DATE;
    private int CURRENT_STEP = 0;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {
        mHealthFAQ = (RelativeLayout) findViewById(R.id.btn_health_faq);
        mHealthArchives = (RelativeLayout) findViewById(R.id.btn_health_archives);
        mCenter = (RelativeLayout) findViewById(R.id.btn_center);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Constants.BAIDU_PUSH_API_KEY);
        mHealthFAQ.setOnClickListener(this);
        mHealthArchives.setOnClickListener(this);
        mCenter.setOnClickListener(this);

        mViewPager.setCurrentItem(0, false);
        setClickMenu(mHealthFAQ);
        stepCounter();
    }
    ViewPagerAdapter viewPagerAdapter;
    @Override
    public void doBusiness(Context mContext) {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HealthFAQFragment());
        fragments.add(new HealthArchivesFragment());
        fragments.add(new MeFragment());
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(viewPagerAdapter);

        //获取当前时间
        CURRENT_DATE = TimeUtil.getCurrentDate();
        //获取数据库
        StepDataDao stepDataDao = new StepDataDao(getApplicationContext());
        //获取当天的数据，用于展示
        StepEntity entity = stepDataDao.getCurDataByDate(CURRENT_DATE);
        //为空则说明还没有该天的数据，有则说明已经开始当天的计步了
        if (entity == null) {
            CURRENT_STEP = 0;
        } else {
            CURRENT_STEP = Integer.parseInt(entity.getSteps());
        }

        Log.i("今日步数---->", CURRENT_DATE + "---->" + CURRENT_STEP);
        getSubmitData(CURRENT_STEP + "", Utils.getDate() + ":00");
        upLoadGATA();
    }

    private long firstime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondtime = System.currentTimeMillis();
            if (secondtime - firstime > 2000) {
//                ("再按一次返回键退出");
                firstime = System.currentTimeMillis();
                return true;
            } else {
                AppManager.getInstance().AppExit(this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getSubmitData(String step, String date) {
        String Url = new AllUrl(this).MovestepsUrl();
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("uid", mLoginConfig.getUserId());
        builder.append("stepcount", step);
        builder.append("createDate", date);
        String data = builder.toString();
        if (HttpUtil.isNetworkAvailable(this)) {
//            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(Url, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
//                            handler.sendEmptyMessage(Constants.GotoLoginActivity);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
//                            if (bean.isSuccess()) {
//                                handler.sendEmptyMessage(Constants.GET_SUCCESS3);
//                            } else
//                                handler.sendEmptyMessage(Constants.GET_FAIL);
                        }
                    }, this);
        } else {
            Utils.toast(this, "网络开小差了~");
            return;
        }
    }

    private void upLoadGATA(){
        String loginUrl = new AllUrl(this).getSaveUserUrl();

        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("id", mLoginConfig.getUserId());
        builder.append("teldevice", LoginConfig.getChannelId());
        String data = builder.toString();

        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(
                    new BaseRequestParm(loginUrl, data, AsyncTaskManager.ContentTypeJson, "POST",
                            LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onComplete(BaseResponseBean bean) {
//                            handler.sendEmptyMessage(UPDATE_MES);

                        }

                        @Override
                        public void onFailed() {
//                            handler.sendEmptyMessage(UPDATE_MES);
                        }

                    }, this);
        } else {
            Utils.toast(MainActivity.this, "网络不给力呀~ o(︶︿︶)o");
            return;
        }
    }


    private void setClickMenu(RelativeLayout view) {
        mHealthFAQ.getChildAt(0).setVisibility(View.VISIBLE);
        mHealthFAQ.getChildAt(1).setVisibility(View.GONE);
        ((TextView) mHealthFAQ.getChildAt(2)).setTextColor(getResources().getColor(R.color.grey_aeb3b8));

        mHealthArchives.getChildAt(0).setVisibility(View.VISIBLE);
        mHealthArchives.getChildAt(1).setVisibility(View.GONE);
        ((TextView) mHealthArchives.getChildAt(2)).setTextColor(getResources().getColor(R.color.grey_aeb3b8));

        mCenter.getChildAt(0).setVisibility(View.VISIBLE);
        mCenter.getChildAt(1).setVisibility(View.GONE);
        ((TextView) mCenter.getChildAt(2)).setTextColor(getResources().getColor(R.color.grey_aeb3b8));


        view.getChildAt(0).setVisibility(View.GONE);
        view.getChildAt(1).setVisibility(View.VISIBLE);
        ((TextView) view.getChildAt(2)).setTextColor(getResources().getColor(R.color.grey_4d5764));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_health_faq:
                mViewPager.setCurrentItem(0, false);
                viewPagerAdapter.getItem(0).onResume();
                setClickMenu(mHealthFAQ);
                break;
            case R.id.btn_health_archives:
                mViewPager.setCurrentItem(1, false);
                viewPagerAdapter.getItem(1).onResume();
                setClickMenu(mHealthArchives);
                break;
            case R.id.btn_center:
                mViewPager.setCurrentItem(2, false);
                setClickMenu(mCenter);
                break;
        }
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public List<Fragment> mFragments;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
            super(fm);
            this.mFragments = mFragments;
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragments.get(arg0);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

    }

    private boolean isBind = false;
    private Messenger messenger;

    /**
     * 计步器设置
     */
    private void stepCounter() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 定时任务
     */
    private TimerTask timerTask;
    private Timer timer;
    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            /**
             * 设置定时器，每个三秒钟去更新一次运动步数
             */
//            timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    try {
//                        messenger = new Messenger(service);
//                        Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
////                        msg.replyTo = mGetReplyMessenger;
//                        messenger.send(msg);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            timer = new Timer();
//            timer.schedule(timerTask, 0, 3000);
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得解绑Service，不然多次绑定Service会异常
        if (isBind) this.unbindService(conn);
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
}
