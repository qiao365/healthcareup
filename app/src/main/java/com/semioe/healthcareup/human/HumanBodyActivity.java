package com.semioe.healthcareup.human;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.google.gson.JsonObject;
import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.activity.BaseActivity;
import com.semioe.healthcareup.activity.HealthStatusActivity;
import com.semioe.healthcareup.bean.DVbean;
import com.semioe.healthcareup.bean.HealthType;
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
import java.util.UUID;

/**
 * @className: HumanBodyActivity
 * @classDescription: 人体图
 * @author: leibing
 * @createTime: 2016/12/30
 */
public class HumanBodyActivity extends BaseActivity implements View.OnClickListener {
    // 点击部位
    public final static String CLICK_POS = "clickPos";
    // 性别
    public final static String SEX = "sex";
    // 跳到下一个activity
    final static int MSG_TO_NEXT_ACTIVITY = 1;
    // 允许触摸
    final static int MSG_CAN_TOUCH = 2;
    // 人体图容器
    private FrameLayout treatSelfBodyFly;
    // 性别tab add by leibing 2016/10/07
    private LinearLayout tabLy;
    private TextView tabLeftTv;
    private TextView tabRightTv;
    // 更改人体正反面 add by leibing 2016/10/07
    private IosButton exchangeIbtn;
    private TextView reverseTv;
    // 播放动画的点
    private ImageView ivPoint;
    private AnimationDrawable pointAnim;
    // 性别、面向标识
    public static final int WOMEN_FRONT = 1;
    public static final int WOMEN_BACK = 2;
    public static final int MAN_FRONT = 3;
    public static final int MAN_BACK = 4;
    // 当前性别和面向 (默认为男性正面)
    private int curSexFace = MAN_FRONT;
    // 屏幕被点击的坐标
    float[] touchScreenPos = new float[2];
    // 是否响应触摸
    private boolean canTouch = true;
    // 人体图页面
    private HumanBodyFragment fragmentHumanBody;
    // 人体图部位提示框
    private HumanBodyPopupWindow mHumanBodyPopupWindow;
    // 点击人体图提示部位名称
    private String posName = "全身";
    // actionbar容器
    private RelativeLayout actionbarRly;
    // 点击人体图时人体图界面与手机屏幕顶部距离
    private int toTopDistance = 0;
    // 自定义Handler（用于点击人体图的触发事件）
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TO_NEXT_ACTIVITY:
                    treatSelfBodyFly.removeView(ivPoint);
                    mHandler.sendEmptyMessageDelayed(MSG_CAN_TOUCH, 500);
                    break;
                case MSG_CAN_TOUCH:
                    // 停止点动画
                    pointAnim.stop();
                    setCanTouch(true);
                    break;
                case Constants.GET_SUCCESS2:
                    data_front.clear();
                    data_back.clear();
                    switch (curSexFace) {
                        case WOMEN_FRONT://女-正
                            for (int i = 0; i < data_list.size(); i++) {
                                if (data_list.get(i).getRemarks().equals("0"))
                                    data_front.add(data_list.get(i));
                            }
                            Log.i("女：   data_front---->", GsonUtils.toJsonString(data_front));
                            break;
                        case WOMEN_BACK://女-背
                            for (int i = 0; i < data_list.size(); i++) {
                                if (data_list.get(i).getRemarks().equals("0"))
                                    data_back.add(data_list.get(i));
                            }
                            Log.i("女：  data_back---->", GsonUtils.toJsonString(data_back));
                            break;
                        case MAN_FRONT://男-正
                            for (int i = 0; i < data_list.size(); i++) {
                                if (data_list.get(i).getRemarks().equals("1"))
                                    data_front.add(data_list.get(i));
                            }
                            Log.i("男：  data_front---->", GsonUtils.toJsonString(data_front));
                            break;
                        case MAN_BACK://男-背
                            for (int i = 0; i < data_list.size(); i++) {
                                if (data_list.get(i).getRemarks().equals("1"))
                                    data_back.add(data_list.get(i));
                            }
                            Log.i("男：   data_back---->", GsonUtils.toJsonString(data_back));
                            break;
                    }
                    break;
                case Constants.GET_SUCCESS3:
                    final ArrayList<String> list = new ArrayList<>();
                    if (zhengzhuang_list == null || zhengzhuang_list.size() == 0) {
                        list.add("暂无");
                        return;
                    }

                    for (int i = 0; i < zhengzhuang_list.size(); i++) {
                        list.add(zhengzhuang_list.get(i).getLable());
                    }
                    ArrayAdapter<String> sp_zz = new ArrayAdapter<String>(HumanBodyActivity.this, R.layout.simple_spinner_item_blue, list);
                    sp_zz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    View item_jcx_v = LayoutInflater.from(HumanBodyActivity.this).inflate(R.layout.item_zz, null);
                    item_jcx_v.setLayoutParams(
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
                    TextView tv_des = (TextView) item_jcx_v.findViewById(R.id.tv_des);
                    final Spinner sp_zhengzhuang = (Spinner) item_jcx_v.findViewById(R.id.sp_zhengzhuang);
                    sp_zhengzhuang.setAdapter(sp_zz);
                    tv_des.setText(posName);
                    ll_zz.addView(item_jcx_v);

                    sp_zhengzhuang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            sp_zhengzhuang.setTag(zhengzhuang_list.get(position).getId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    arr_data.add(sp_zhengzhuang);

                    break;
                case Constants.GET_SUCCESS:
                    finish();
                    break;
                case Constants.GET_FAIL:
                    finish();
                    break;
                case Constants.GotoLoginActivity:
                    Utils.toast(HumanBodyActivity.this, "登陆失效,请重新登陆");
                    new LoginConfig(HumanBodyActivity.this).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(HumanBodyActivity.this,
                            mHandler);
                    break;
                default:
                    break;
            }
        }
    };
    private List<HealthType> data_list = new ArrayList<HealthType>();
    private List<HealthType> data_front = new ArrayList<HealthType>();
    private List<HealthType> data_back = new ArrayList<HealthType>();
    private List<HealthType> zhengzhuang_list;
    private LinearLayout ll_zz;
    private RoundTextView rtv_ok;
    private ArrayList<Spinner> arr_data = new ArrayList<Spinner>();

    @Override
    public int bindLayout() {
        return R.layout.human_activity_human;
    }

    @Override
    public void initView(View view) {
        initView();
        switch (curSexFace) {
            case WOMEN_FRONT://女-正
                getChronic("front");
                break;
            case WOMEN_BACK://女-背
                getChronic("back");
                break;
            case MAN_FRONT://男-正
                getChronic("front");
                break;
            case MAN_BACK://男-背
                getChronic("back");
                break;
        }

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

    /**
     * init view
     *
     * @param
     * @return
     * @author leibing
     * @createTime 2016/12/30
     * @lastModify 2016/12/30
     */
    public void initView() {
        // 初始化人体图容器
        treatSelfBodyFly = (FrameLayout) findViewById(R.id.fly_treat_self_body);
        // 初始化性别分页控件 add by leibing 2016/10/07
        actionbarRly = (RelativeLayout) findViewById(R.id.rly_actionbar);
        tabLy = (LinearLayout) findViewById(R.id.ly_tab);
        tabLeftTv = (TextView) findViewById(R.id.tv_tab_left);
        tabRightTv = (TextView) findViewById(R.id.tv_tab_right);
        ll_zz = (LinearLayout) findViewById(R.id.ll_zz);
        // 初始化人体正反面控件 add by leibing 2016/10/07
        exchangeIbtn = (IosButton) findViewById(R.id.ibtn_exchange);
        reverseTv = (TextView) findViewById(R.id.tv_reverse);
        // 初始化播放动画的点
        ivPoint = new ImageView(this.getApplicationContext());
        ivPoint.setLayoutParams(new FrameLayout.LayoutParams(50, 50));
        ivPoint.setBackgroundResource(R.drawable.point_in_out);
        // 设置人体图 （默认为男性正面）
        FragmentTransaction fm = getFragmentManager().beginTransaction();
        fragmentHumanBody = HumanBodyFragment.newInstance(MAN_FRONT);
        fm.replace(R.id.ly_fragment_area, fragmentHumanBody, "FragmentHumanBody");
        fm.commit();
        // 设置退出点击事件
        findViewById(R.id.rly_btn_back).setOnClickListener(this);
        // 设置性别分页控件点击事件 add by leibing 2016/10/07
        tabRightTv.setOnClickListener(this);
        tabLeftTv.setOnClickListener(this);
        // 设置更改人体正反面点击事件 add by leibing 2016/10/07
        exchangeIbtn.setOnClickListener(this);
        // 获取actionbar容器高度(计算点击人体图时人体图界面与手机屏幕顶部距离)
        actionbarRly.post(new Runnable() {
            @Override
            public void run() {
                toTopDistance += actionbarRly.getMeasuredHeight();
            }
        });
        rtv_ok = (RoundTextView) findViewById(R.id.rtv_ok);
        rtv_ok.setOnClickListener(this);

    }

    public boolean isCanTouch() {
        return canTouch;
    }

    public void setCanTouch(boolean canTouch) {
        this.canTouch = canTouch;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 获取屏幕被点击点的坐标
        touchScreenPos[0] = ev.getX();
        touchScreenPos[1] = ev.getY();
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 改变性别逻辑
     *
     * @param isMan   是否男性
     * @param isFront 是否正面
     * @return
     * @author leibing
     * @createTime 2016/10/07
     * @lastModify 2016/10/07
     */

//    back_male  front_female back_female  front_male
    private void changeSexLogic(boolean isMan, boolean isFront) {
        if (isMan && isFront) {
            // 男性&&正面
            curSexFace = MAN_FRONT;
        } else if (isMan && !isFront) {
            // 男性&&反面
            curSexFace = MAN_BACK;
        } else if (!isMan && isFront) {
            // 女性&&正面
            curSexFace = WOMEN_FRONT;
        } else {
            // 女性&&反面
            curSexFace = WOMEN_BACK;
        }

        if (curSexFace == MAN_FRONT || curSexFace == WOMEN_FRONT) {
            // 当前面为男性正面或女性正面均设置为正面
            reverseTv.setText("正面");
        } else if (curSexFace == MAN_BACK || curSexFace == WOMEN_BACK) {
            // 当前面为男性反面或女性反面均设置为反面
            reverseTv.setText("反面");
        }

        // 更新fragment内容
        FragmentTransaction fm = getFragmentManager().beginTransaction();
        fragmentHumanBody = HumanBodyFragment.newInstance(curSexFace);
        fm.replace(R.id.ly_fragment_area, fragmentHumanBody, "FragmentHumanBody");
        fm.commit();
    }

    /**
     * 将圆点add入，播放动画
     *
     * @param clickPos 点击部位
     * @return
     * @author leibing
     * @createTime 2016/10/07
     * @lastModify 2016/10/07
     */
    public void addIVandPlay(int clickPos) {
        setCanTouch(false);
        // 设置点X、Y坐标
        ivPoint.setX(touchScreenPos[0] - 15);
        ivPoint.setY(touchScreenPos[1] - getStatusBarHeight() - toTopDistance);
        // 将点添加到父类容器
        if (ivPoint.getParent() == null) {
            treatSelfBodyFly.addView(ivPoint);
        }
        // 人体部位提示框
        // 1：头部；2：颈部；3：胸部；4：腹部；5：男性勾股；6：女性盆骨；7：上肢；8：下肢；
        // 9：下肢（本应足部，但数据没分出来）；10：背部；11：腰部；12：臀部；
        // 13：上肢（本应手指，但数据没分出来）
        switch (clickPos) {
            case 1:
                posName = "头";
//                posName = "五官";
                setData(posName, "null", curSexFace);
                break;
            case 2:
                posName = "肩颈";
                setData(posName, "null", curSexFace);
                break;
            case 3:
                posName = "胸";
                setData(posName, "null", curSexFace);
                break;
            case 4:
                posName = "腹部";
                setData(posName, "null", curSexFace);
                break;
            case 5:
//                posName = "男性勾股";
                posName = "会阴";
                setData(posName, "null", curSexFace);
                break;
            case 6:
//                posName = "女性盆骨";
                posName = "会阴";
                setData(posName, "null", curSexFace);
                break;
            case 7:
                posName = "上肢";
                setData(posName, "null", curSexFace);
                break;
            case 13:
                posName = "手";
                setData(posName, "null", curSexFace);
                break;
            case 8:
                posName = "下肢";
                setData(posName, "null", curSexFace);
                break;
            case 9:
                posName = "足";
                setData(posName, "null", curSexFace);
                break;
            case 10:
                posName = "背部";
                setData(posName, "null", curSexFace);
                break;
            case 11:
                posName = "腰部";
                setData(posName, "null", curSexFace);
                break;
            case 12:
                posName = "臀部";
//                posName = "肛门";
                setData(posName, "肛门", curSexFace);
                break;
            default:
                posName = "其他";
                setData(posName, "null", curSexFace);
                break;
        }
        treatSelfBodyFly.post(new Runnable() {
            @Override
            public void run() {
                mHumanBodyPopupWindow = new HumanBodyPopupWindow(HumanBodyActivity.this, posName);
                mHumanBodyPopupWindow.showAsDropDown(ivPoint, -80, -150);
                mHumanBodyPopupWindow.delayDismissDialog(mHumanBodyPopupWindow, 0);
            }
        });

        // 执行点动画
        pointAnim = (AnimationDrawable) ivPoint.getBackground();
        pointAnim.start();
        int duration = 0;
        for (int i = 0; i < pointAnim.getNumberOfFrames(); i++) {
            duration += pointAnim.getDuration(i);
        }
        // 将点击的部位发送到新启动的activity
        Message msg = Message.obtain();
        msg.what = MSG_TO_NEXT_ACTIVITY;
        Bundle bundle = new Bundle();
        bundle.putInt(CLICK_POS, clickPos);
        // 当前性别
        String curSex = (curSexFace == MAN_FRONT || curSexFace == MAN_BACK) ? "男" : "女";
        // 将性别值传给Handler
        bundle.putString(SEX, curSex);
        msg.setData(bundle);
        mHandler.sendMessageDelayed(msg, duration);
    }

    /**
     * 获取通知栏的高度
     *
     * @param
     * @return
     * @author leibing
     * @createTime 2016/10/07
     * @lastModify 2016/10/07
     */
    public int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height",
                        "dimen", "android"));
    }

    private void setData(String posName1, String posName2, int curSexFace) {
        if (curSexFace == MAN_FRONT || curSexFace == WOMEN_FRONT) {
            for (int i = 0; i < data_front.size(); i++) {
                if (data_front.get(i).getLable().contains(posName1) || data_front.get(i).getLable().contains(posName2)) {
                    getZhengzhuang(data_front.get(i).getId());
                }
            }
        } else {
            for (int i = 0; i < data_back.size(); i++) {
                if (data_back.get(i).getLable().contains(posName1) || data_back.get(i).getLable().contains(posName2)) {
                    getZhengzhuang(data_back.get(i).getId());
                }
            }
        }
    }


    //提交数据
    private void getSubmitData(String data) {
        String Url = new AllUrl(this).AnswerUrl();
        if (HttpUtil.isNetworkAvailable(this)) {
//            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(Url, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            mHandler.sendEmptyMessage(Constants.GotoLoginActivity);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                mHandler.sendEmptyMessage(Constants.GET_SUCCESS);
                            } else
                                mHandler.sendEmptyMessage(Constants.GET_FAIL);
                        }
                    }, this);
        } else {
            Utils.toast(this, "网络开小差了~");
            return;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rly_btn_back:
                // 退出该页面
                HumanBodyActivity.this.finish();
                break;
            case R.id.ibtn_exchange:
                // 设置人体正反面
                setBodyFrontBack();
                break;
            case R.id.tv_tab_left:
                // 设置背景为男性
                tabLy.setBackgroundResource(R.drawable.man_new);
                // 更改字体颜色
                tabLeftTv.setTextColor(getResources().getColor(R.color.white));
                tabRightTv.setTextColor(getResources().
                        getColor(R.color.symptoms_self_diagnostics_tab_blue_text));
                // 选择性别分页男选项
                changeSexLogic(true, true);
                getChronic("front");
                break;
            case R.id.tv_tab_right:
                // 设置背景为女性
                tabLy.setBackgroundResource(R.drawable.women_new);
                // 更改字体颜色
                tabRightTv.setTextColor(getResources().getColor(R.color.white));
                tabLeftTv.setTextColor(getResources().
                        getColor(R.color.symptoms_self_diagnostics_tab_blue_text));
                // 选择性别分页女选项
                changeSexLogic(false, true);
                getChronic("front");
                break;
            case R.id.rtv_ok:
                UUID uuid = UUID.randomUUID();
                ArrayList<DVbean> list = new ArrayList();
                if (arr_data != null && arr_data.size() > 0) {
                    for (int i = 0; i < arr_data.size(); i++) {
                        DVbean bean = new DVbean();
                        bean.setUid(mLoginConfig.getUserId());
                        bean.setBatchid(uuid.toString());
                        bean.setDatak("symptoms");
                        bean.setDatav(arr_data.get(i).getTag().toString());
                        list.add(bean);
                    }
                    getSubmitData(GsonUtils.toJsonString(list));
                }

                break;
            default:
                break;
        }
    }

    private void getChronic(String type) {
        String Url = new AllUrl(this).getInitQuestionUrl();
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("type", type);
        String data = builder.toString();
        if (HttpUtil.isNetworkAvailable(this)) {
//            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(Url, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            mHandler.sendEmptyMessage(Constants.GotoLoginActivity);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                Data(bean);
                            } else
                                mHandler.sendEmptyMessage(Constants.GET_FAIL);
                        }
                    }, this);
        } else {
            Utils.toast(this, "网络开小差了~");
            return;
        }
    }

    private void Data(BaseResponseBean bean) {
        JsonObject json = GsonUtils.getRootJsonObject(bean.getResult());
        int errcode = GsonUtils.getKeyValue(json, "errcode").getAsInt();
        if (errcode == 0) {
            data_list = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(json, "result").getAsJsonArray(), HealthType.class);
        }
        mHandler.sendEmptyMessage(Constants.GET_SUCCESS2);
    }

    private void getZhengzhuang(int parentId) {
        String Url = new AllUrl(this).getInitQuestionUrl();
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("type", "symptom");
        builder.append("parentId", parentId);
        String data = builder.toString();
        if (HttpUtil.isNetworkAvailable(this)) {
//            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(Url, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            mHandler.sendEmptyMessage(Constants.GotoLoginActivity);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                Data2(bean);
                            } else
                                mHandler.sendEmptyMessage(Constants.GET_FAIL);
                        }
                    }, this);
        } else {
            Utils.toast(this, "网络开小差了~");
            return;
        }
    }

    private void Data2(BaseResponseBean bean) {
        JsonObject json = GsonUtils.getRootJsonObject(bean.getResult());
        int errcode = GsonUtils.getKeyValue(json, "errcode").getAsInt();
        if (errcode == 0) {
            zhengzhuang_list = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(json, "result").getAsJsonArray(), HealthType.class);
        }
        mHandler.sendEmptyMessage(Constants.GET_SUCCESS3);
    }

    /**
     * 设置人体正反面
     *
     * @param
     * @return
     * @author leibing
     * @createTime 2016/10/07
     * @lastModify 2016/10/07
     */
    private void setBodyFrontBack() {
        switch (curSexFace) {
            case MAN_FRONT:
                // 当为男性正面，则设置为男性反面
                curSexFace = MAN_BACK;
                break;
            case MAN_BACK:
                // 当为男性反面，则设置为男性正面
                curSexFace = MAN_FRONT;
                break;
            case WOMEN_FRONT:
                // 当为女性正面，则设置为女性反面
                curSexFace = WOMEN_BACK;
                break;
            case WOMEN_BACK:
                // 当为女性反面，则设置为女性正面
                curSexFace = WOMEN_FRONT;
                break;
        }

        if (curSexFace == MAN_FRONT || curSexFace == WOMEN_FRONT) {
            // 当前面为男性正面或女性正面均设置为正面
            reverseTv.setText("正面");
            getChronic("front");
        } else if (curSexFace == MAN_BACK || curSexFace == WOMEN_BACK) {
            // 当前面为男性反面或女性反面均设置为反面
            reverseTv.setText("反面");
            getChronic("back");
        }

        // 执行动画更新fragment内容
        FragmentTransaction fm = getFragmentManager().beginTransaction();
        fm.setCustomAnimations(R.anim.animator_two_enter, R.anim.animator_one_exit);
        fragmentHumanBody = HumanBodyFragment.newInstance(curSexFace);
        fm.replace(R.id.ly_fragment_area, fragmentHumanBody, "FragmentHumanBody");
        fm.commit();
    }
}
