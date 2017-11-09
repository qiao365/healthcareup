package com.semioe.healthcareup.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hhl.library.FlowTagLayout;
import com.hhl.library.OnTagSelectListener;
import com.libzxing.zxing.activity.CaptureActivity;
import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.adapter.TagAdapter;
import com.semioe.healthcareup.bean.DVbean;
import com.semioe.healthcareup.bean.HealthType;
import com.semioe.healthcareup.bean.MedRecord;
import com.semioe.healthcareup.http.httputils.AllUrl;
import com.semioe.healthcareup.http.httputils.AsyncTaskManager;
import com.semioe.healthcareup.http.httputils.GsonUtils;
import com.semioe.healthcareup.http.httputils.HttpUtil;
import com.semioe.healthcareup.http.httputils.JsonObjectBuilder;
import com.semioe.healthcareup.http.requestparams.BaseRequestParm;
import com.semioe.healthcareup.http.responsebeans.BaseResponseBean;
import com.semioe.healthcareup.http.responsebeans.RequestListener;
import com.semioe.healthcareup.human.HumanBodyActivity;
import com.semioe.healthcareup.utils.LoginConfig;
import com.semioe.healthcareup.utils.Utils;
import com.semioe.healthcareup.views.DynamicWave;
import com.semioe.healthcareup.views.VerticalSeekBar;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.spring.stepcounter.simplestepcounter.constant.Constant;

import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class HealthStatusActivity extends BaseActivity implements View.OnClickListener,
        OnChartValueSelectedListener {

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeSweetADialog();
            switch (msg.what) {
                case Constants.GET_SUCCESS:
                    if (list != null) {
                        dataSource.clear();
                        for (int i = 0; i < list.size(); i++) {
                            dataSource.add(list.get(i).getLable().trim());
                        }
                        mSizeTagAdapter.onlyAddAll(dataSource);
                    }
                    break;
                case Constants.GET_FAIL:

                    break;
                case Constants.GET_SUCCESS3:

                    break;
                case Constants.GET_SUCCESS4:
                    ll_zhengzhuang.removeAllViews();
                    if (zhengzhuang_list != null) {
                        for (int i = 0; i < zhengzhuang_list.size(); i++) {
                            View item_zz = LayoutInflater.from(HealthStatusActivity.this).inflate(R.layout.item_mb, null);
                            item_zz.setLayoutParams(
                                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
                            final CheckBox cbx_mb = (CheckBox) item_zz.findViewById(R.id.cbx_mb);
                            cbx_mb.setChecked(true);
                            cbx_mb.setTag(i);
                            cbx_mb.setText(zhengzhuang_list.get(i).getLable().trim());
                            ll_zhengzhuang.addView(item_zz);
                        }
                    }
                    break;
                case Constants.GET_SUCCESS2:
                    if (list_manbing != null) {
                        dataMB.clear();
                        for (int i = 0; i < list_manbing.size(); i++) {
                            View item_mb = LayoutInflater.from(HealthStatusActivity.this).inflate(R.layout.item_mb, null);
                            item_mb.setLayoutParams(
                                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
                            final CheckBox cbx_mb = (CheckBox) item_mb.findViewById(R.id.cbx_mb);
                            cbx_mb.setText(list_manbing.get(i).getLable().trim());
                            cbx_mb.setTag(list_manbing.get(i).getId());
                            cbx_mb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked)
                                        arr_mb_cbx.add(cbx_mb);
                                    else arr_mb_cbx.remove(cbx_mb);
                                }
                            });
                            ll_mb.addView(item_mb);
                        }


                        ll_1.setVisibility(View.GONE);
                        ll_2.setVisibility(View.GONE);
                        ll_3.setVisibility(View.GONE);
                        ll_4.setVisibility(View.VISIBLE);
                        ll_5.setVisibility(View.GONE);
                        ll_6.setVisibility(View.GONE);
                        ll_7.setVisibility(View.GONE);
                        ll_8.setVisibility(View.GONE);
                        lable = "";
                    }
                    break;
                case Constants.GotoLoginActivity:
                    Utils.toast(HealthStatusActivity.this, "登陆失效,请重新登陆");
                    new LoginConfig(HealthStatusActivity.this).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(HealthStatusActivity.this,
                            handler);
                    break;
                default:

                    break;
            }
        }
    };

    private FlowTagLayout mSizeFlowTagLayout;
    private TagAdapter<String> mSizeTagAdapter;
    private List<HealthType> list = new ArrayList<HealthType>();
    private List<String> dataSource = new ArrayList<>();
    private List<String> dataMB = new ArrayList<>();
    private TextView tv_date;
    private EditText et_name, et_cardId;
    private Spinner sp_sex;
    private ArrayAdapter<String> sp_adapter;
    private String[] sp_data = {"男", "女"};
    private RoundTextView rtv_next, rtv_next2;
    private SeekBar seekBar;
    private DynamicWave waveView;
    private TextView tv_ysl;
    private int steps = 10;
    private LinearLayout ll_1, ll_2, ll_3, ll_4, ll_5, ll_6, ll_7, ll_8;
    private PieChart pieChart_1, pieChart_2, pieChart_3;
    private Typeface mTfLight;
    private Typeface mTfRegular;
    private int[] s = {50, 30, 20};
    private TextView tv_center_1, tv_center_2, tv_center_3;
    private String lable = "";
    private List<HealthType> list_manbing;
    private LinearLayout ll_mb;
    private CheckBox cbx_11, cbx_22, cbx_33;
    private LinearLayout ll_parent_jcx;
    private TextView tv_add_recode;
    private Spinner sp_zhibiao;
    private LinearLayout ll_child_value;
    private ArrayAdapter<String> sp_jcx;
    private ArrayAdapter<String> sp_ad;
    private UUID uuid;
    private SeekBar seek_bar_yan, seek_bar_you;
    private CheckBox cbx_1, cbx_2, cbx_3, cbx_4, cbx_5, cbx_6, cbx_7, cbx_8, cbx_9, cbx_10;
    private SeekBar seek_bar_yd;
    private TextView tv_add_zhengzhuang;
    private TextView tv_add_MedRecord;
    private LinearLayout ll_record;
    private ArrayAdapter<String> sp_med_unit;
    private ArrayAdapter<String> sp_med_name;
    private static final int SCAN = 1;
    private ArrayList<EditText> arr_ed = new ArrayList<EditText>();

    ArrayList<Spinner> arr_sp = new ArrayList<Spinner>();
    ArrayList<EditText> arr_edit = new ArrayList<EditText>();
    final ArrayList<String> time = new ArrayList<String>();
    ArrayList<CheckBox> arr_mb_cbx = new ArrayList<CheckBox>();
    private LinearLayout ll_status;
    private ImageView img_shop, img_scan, img_devices, img_message;
    private EditText et_gao, et_zhong, et_di;
    private boolean flag = true;
    private List<HealthType> zhengzhuang_list = new ArrayList<>();
    private LinearLayout ll_zhengzhuang;


    @Override
    public int bindLayout() {
        return R.layout.activity_health_status;
    }

    @Override
    public void initView(View view) {
        uuid = UUID.randomUUID();
        img_shop = (ImageView) findViewById(R.id.img_shop);
        img_shop.setOnClickListener(this);
        img_scan = (ImageView) findViewById(R.id.img_scan);
        img_scan.setOnClickListener(this);
        img_devices = (ImageView) findViewById(R.id.img_devices);
        img_devices.setOnClickListener(this);
        img_message = (ImageView) findViewById(R.id.img_message);
        img_message.setOnClickListener(this);

        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        TextView config_hidden = (TextView) findViewById(R.id.config_hidden);
        config_hidden.requestFocus();//禁止软键盘自动弹出（焦点）
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_cardId = (EditText) findViewById(R.id.et_cardId);
        sp_sex = (Spinner) findViewById(R.id.sp_sex);
        rtv_next = (RoundTextView) findViewById(R.id.rtv_next);
        rtv_next.setOnClickListener(this);
        rtv_next2 = (RoundTextView) findViewById(R.id.rtv_next2);
        rtv_next2.setOnClickListener(this);
        mSizeFlowTagLayout = (FlowTagLayout) findViewById(R.id.size_flow_layout);
        sp_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sp_data);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sex.setAdapter(sp_adapter);
        tv_add_zhengzhuang = (TextView) findViewById(R.id.tv_add_zhengzhuang);
        tv_add_zhengzhuang.setOnClickListener(this);
        ll_mb = (LinearLayout) findViewById(R.id.ll_mb);
        tv_ysl = (TextView) findViewById(R.id.tv_ysl);
        ll_1 = (LinearLayout) findViewById(R.id.ll_1);
        ll_2 = (LinearLayout) findViewById(R.id.ll_2);
        ll_3 = (LinearLayout) findViewById(R.id.ll_3);
        ll_4 = (LinearLayout) findViewById(R.id.ll_4);
        ll_5 = (LinearLayout) findViewById(R.id.ll_5);
        ll_6 = (LinearLayout) findViewById(R.id.ll_6);
        ll_7 = (LinearLayout) findViewById(R.id.ll_7);
        ll_8 = (LinearLayout) findViewById(R.id.ll_8);
        cbx_11 = (CheckBox) findViewById(R.id.cbx_11);
        cbx_22 = (CheckBox) findViewById(R.id.cbx_22);
        cbx_33 = (CheckBox) findViewById(R.id.cbx_33);
        pieChart_1 = (PieChart) findViewById(R.id.pieChart);
        pieChart_2 = (PieChart) findViewById(R.id.pieChart_2);
        pieChart_3 = (PieChart) findViewById(R.id.pieChart_3);
        showPieChart(pieChart_1, setData(s, 100));
        showPieChart(pieChart_2, setData(s, 100));
        showPieChart(pieChart_3, setData(s, 100));
        tv_center_1 = (TextView) findViewById(R.id.tv_center_1);
        tv_center_1.setOnClickListener(this);
        tv_center_2 = (TextView) findViewById(R.id.tv_center_2);
        tv_center_2.setOnClickListener(this);
        tv_center_3 = (TextView) findViewById(R.id.tv_center_3);
        tv_center_3.setOnClickListener(this);
        seek_bar_yd = (SeekBar) findViewById(R.id.seek_bar_yd);
        et_gao = (EditText) findViewById(R.id.et_gao);
        et_zhong = (EditText) findViewById(R.id.et_zhong);
        et_di = (EditText) findViewById(R.id.et_di);

        ll_zhengzhuang = (LinearLayout) findViewById(R.id.ll_zhengzhuang);

        cbx_1 = (CheckBox) findViewById(R.id.cbx_1);
        cbx_2 = (CheckBox) findViewById(R.id.cbx_2);
        cbx_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    cbx_2.setChecked(false);
            }
        });
        cbx_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    cbx_1.setChecked(false);
            }
        });
        cbx_3 = (CheckBox) findViewById(R.id.cbx_3);
        cbx_4 = (CheckBox) findViewById(R.id.cbx_4);
        cbx_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    cbx_4.setChecked(false);
            }
        });
        cbx_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    cbx_3.setChecked(false);
            }
        });

        cbx_5 = (CheckBox) findViewById(R.id.cbx_5);
        cbx_6 = (CheckBox) findViewById(R.id.cbx_6);
        cbx_7 = (CheckBox) findViewById(R.id.cbx_7);
        cbx_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbx_6.setChecked(false);
                    cbx_7.setChecked(false);
                }
            }
        });
        cbx_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbx_5.setChecked(false);
                    cbx_7.setChecked(false);
                }
            }
        });
        cbx_7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbx_5.setChecked(false);
                    cbx_6.setChecked(false);
                }
            }
        });

        cbx_8 = (CheckBox) findViewById(R.id.cbx_8);
        cbx_9 = (CheckBox) findViewById(R.id.cbx_9);
        cbx_10 = (CheckBox) findViewById(R.id.cbx_10);
        cbx_8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbx_9.setChecked(false);
                    cbx_10.setChecked(false);
                }
            }
        });
        cbx_9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbx_8.setChecked(false);
                    cbx_10.setChecked(false);
                }
            }
        });
        cbx_10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbx_8.setChecked(false);
                    cbx_9.setChecked(false);
                }
            }
        });


        ll_parent_jcx = (LinearLayout) findViewById(R.id.ll_parent_jcx);
        tv_add_recode = (TextView) findViewById(R.id.tv_add_recode);
        tv_add_recode.setOnClickListener(this);

        tv_add_MedRecord = (TextView) findViewById(R.id.tv_add_MedRecord);
        tv_add_MedRecord.setOnClickListener(this);
        ll_record = (LinearLayout) findViewById(R.id.ll_record);

        seek_bar_yan = (SeekBar) findViewById(R.id.seek_bar_yan);
        seek_bar_you = (SeekBar) findViewById(R.id.seek_bar_you);

        sp_jcx = new ArrayAdapter<String>(this, R.layout.simple_spinner_item_blue, getResources().getStringArray(R.array.testName));
        sp_jcx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_med_unit = new ArrayAdapter<String>(this, R.layout.simple_spinner_item_blue, getResources().getStringArray(R.array.med_unit));
        sp_med_unit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_med_name = new ArrayAdapter<String>(this, R.layout.simple_spinner_item_blue, getResources().getStringArray(R.array.med_name));
        sp_med_name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ll_status = (LinearLayout) findViewById(R.id.ll_status);

        seekBar = (VerticalSeekBar) findViewById(R.id.seek_bar);
        waveView = (DynamicWave) findViewById(R.id.wave_view);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                waveView.changePos(progress * 9);
                if (progress <= 40)
                    tv_ysl.setText(progress * 25 + "ml");
                else if (progress > 40 && progress <= 80)
                    tv_ysl.setText(1000 + (progress - 40) * 50 + "ml");
                else if (progress > 80)
                    tv_ysl.setText(3000 + (progress - 80) * 100 + "ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void doBusiness(Context mContext) {
        if (getIntent().hasExtra("Flag")) {
            steps = 10;
            ll_1.setVisibility(View.VISIBLE);
        } else {
            steps = 10;
            ll_1.setVisibility(View.GONE);
            ll_7.setVisibility(View.VISIBLE);
            getCurSymptoms();
        }
        getChoseData();
        mSizeTagAdapter = new TagAdapter<>(this);
        mSizeFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        mSizeFlowTagLayout.setAdapter(mSizeTagAdapter);
        mSizeFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        sb.append(":");
                    }
                    Log.i("选择标签：---> ", sb.toString());
                    lable = sb.toString();
                } else {
//                    Snackbar.make(parent, "没有选择标签", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    lable = "";
                }
            }
        });

        sp_ad = new ArrayAdapter<String>(HealthStatusActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.textIndex_unit));
        sp_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    private void showPieChart(PieChart mChart, PieData pieData) {
        mChart.setUsePercentValues(true);//显示成百分比
        mChart.getDescription().setEnabled(false);
//        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.getLegend().setEnabled(false);//图例是否可用

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(55f);//半径
        mChart.setTransparentCircleRadius(61f);// 半透明圈
        //mChart.setHoleRadius(0)  //实心圆

        mChart.setDrawCenterText(true);//饼状图中间可以添加文字

        mChart.setRotationAngle(90);// 初始旋转角度
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);// 可以手动旋转
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

//        Legend l = mChart.getLegend();  //设置比例图
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);

        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);

        mChart.setData(pieData);
        mChart.highlightValues(null);
        mChart.invalidate();

    }

    private void getChoseData() {
        String msgUrl = new AllUrl(this).getInitQuestionUrl();
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("type", "healthybymyself");
        String data = builder.toString();
        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
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
                            } else
                                handler.sendEmptyMessage(Constants.GET_FAIL);
                        }
                    }, this);
        } else {
            Utils.toast(this, "网络开小差了~");
            return;
        }
    }

    private void strToBean(String response) {
        JsonObject json = GsonUtils.getRootJsonObject(response);
        int errcode = GsonUtils.getKeyValue(json, "errcode").getAsInt();
        if (errcode == 0)
            list = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(json, "result").getAsJsonArray(), HealthType.class);

        handler.sendEmptyMessage(Constants.GET_SUCCESS);
    }

    private void getChronic() {
        String Url = new AllUrl(this).getInitQuestionUrl();
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("type", "chronic");
        String data = builder.toString();
        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(Url, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(Constants.GotoLoginActivity);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                Data(bean);
                            } else
                                handler.sendEmptyMessage(Constants.GET_FAIL);
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
            list_manbing = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(json, "result").getAsJsonArray(), HealthType.class);
        }
        handler.sendEmptyMessage(Constants.GET_SUCCESS2);
    }

    //更新用户信息
    private void getSaveUser(String data) {
        String Url = new AllUrl(this).getSaveUserUrl();
        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(Url, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(Constants.GotoLoginActivity);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                handler.sendEmptyMessage(Constants.GET_SUCCESS3);
                            } else
                                handler.sendEmptyMessage(Constants.GET_FAIL);
                        }
                    }, this);
        } else {
            Utils.toast(this, "网络开小差了~");
            return;
        }
    }

    //提交数据
    private void getSubmitData(String data) {
        String Url = new AllUrl(this).AnswerUrl();
        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(Url, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(Constants.GotoLoginActivity);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                handler.sendEmptyMessage(Constants.GET_SUCCESS3);
                            } else
                                handler.sendEmptyMessage(Constants.GET_FAIL);
                        }
                    }, this);
        } else {
            Utils.toast(this, "网络开小差了~");
            return;
        }
    }

    private PieData setData(int[] s, float range) {

        float mult = range;
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < s.length; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
//                    mParties[i % mParties.length],
//                    getResources().getDrawable(R.drawable.star)));
            entries.add(new PieEntry(s[i]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);//设置个饼状图之间的距离
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        // 饼图颜色
        colors.add(Color.parseColor("#eb5637"));
        colors.add(Color.parseColor("#fd7f00"));
        colors.add(Color.parseColor("#7ed321"));
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
//        dataSet.setSelectionShift(0f);// 选中态多出的长度
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);

        return data;
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("点击调整\n摄取比例");
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 4, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 4, s.length() - 5, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 4, s.length() - 5, 0);
        s.setSpan(new RelativeSizeSpan(0.8f), 4, s.length() - 5, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 4, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 4, s.length(), 0);
        return s;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_shop://商城 h5
                startActivity(new Intent(this, ShopActivity.class));
                break;
            case R.id.img_scan://扫描
                startActivityForResult(new Intent(this, CaptureActivity.class), SCAN);
                break;
            case R.id.img_devices://定位
                startActivity(new Intent(this, NearDevicesActivity.class));
                break;
            case R.id.img_message://消息
                startActivity(new Intent(this, MessagesActivity.class));
                break;
            case R.id.tv_date:
                Utils.showDateDialog(tv_date, this);
                break;
            case R.id.tv_add_zhengzhuang:
                startActivity(new Intent(this, HumanBodyActivity.class));
                break;
            case R.id.rtv_next:
                if (lable.contains("慢性病")) {
                    getChronic();
                    steps = 10;
                } else {
                    steps += 1;

                    //慢病
                    if (flag)
                        if (arr_mb_cbx.size() > 0) {
                            ArrayList<DVbean> list = new ArrayList<>();
                            for (int i = 0; i < arr_mb_cbx.size(); i++) {
                                DVbean bean = new DVbean();
                                bean.setUid(mLoginConfig.getUserId());
                                bean.setBatchid(uuid.toString());
                                bean.setDatak("chronic");
                                bean.setDatav(arr_mb_cbx.get(i).getTag().toString());
                                list.add(bean);
                            }
                            DVbean bean = new DVbean();
                            bean.setUid(mLoginConfig.getUserId());
                            bean.setBatchid(uuid.toString());
                            bean.setDatak("healthybymyself");
                            bean.setDatav("5");
                            list.add(bean);
                            getSubmitData(GsonUtils.toJsonString(list));
                            flag = false;
                        }

                    if (steps == 11) {
                        ll_1.setVisibility(View.GONE);
                        ll_2.setVisibility(View.GONE);
                        ll_3.setVisibility(View.GONE);
                        ll_4.setVisibility(View.GONE);
                        ll_5.setVisibility(View.GONE);
                        ll_6.setVisibility(View.GONE);
                        ll_7.setVisibility(View.VISIBLE);
                        ll_8.setVisibility(View.GONE);
                        getCurSymptoms();

                        steps = 1;

                    } else if (steps == 2) {
                        JsonObjectBuilder builder = new JsonObjectBuilder();
                        builder.append("id", mLoginConfig.getUserId());
                        if (!et_name.getText().toString().trim().equals(""))
                            builder.append("truename", et_name.getText().toString().trim());
                        builder.append("sex", sp_sex.getSelectedItemId());
                        if (!et_cardId.getText().toString().trim().equals(""))
                            builder.append("cardno", et_cardId.getText().toString().trim());
                        if (!tv_date.getText().toString().equals(""))
                            builder.append("birthday", tv_date.getText().toString().trim());
                        builder.append("teldevice", LoginConfig.getChannelId());
                        getSaveUser(builder.toString());

                        //饮食
                        ll_1.setVisibility(View.GONE);
                        ll_2.setVisibility(View.GONE);
                        ll_3.setVisibility(View.VISIBLE);
                        ll_4.setVisibility(View.GONE);
                        ll_5.setVisibility(View.GONE);
                        ll_6.setVisibility(View.GONE);
                        ll_7.setVisibility(View.GONE);
                        ll_8.setVisibility(View.GONE);
                    } else if (steps == 3) {
                        ArrayList<DVbean> list = new ArrayList();
                        IDataSet<PieEntry> dataSet = pieChart_1.getData().getDataSet();
                        IDataSet<PieEntry> dataSet2 = pieChart_2.getData().getDataSet();
                        IDataSet<PieEntry> dataSet3 = pieChart_3.getData().getDataSet();
                        DVbean bean = new DVbean();
                        bean.setUid(mLoginConfig.getUserId());
                        bean.setBatchid(uuid.toString());
                        bean.setDatak("breakfastmain");
                        bean.setDatav(dataSet.getEntryForIndex(0).getValue() * 30 + "");

                        DVbean bean2 = new DVbean();
                        bean2.setUid(mLoginConfig.getUserId());
                        bean2.setBatchid(uuid.toString());
                        bean2.setDatak("breakfastMeatandeggs");
                        bean2.setDatav(dataSet.getEntryForIndex(1).getValue() * 30 + "");

                        DVbean bean3 = new DVbean();
                        bean3.setUid(mLoginConfig.getUserId());
                        bean3.setBatchid(uuid.toString());
                        bean3.setDatak("breakfastVegetables");
                        bean3.setDatav(dataSet.getEntryForIndex(2).getValue() * 30 + "");
                        if (cbx_11.isChecked()) {
                            list.add(bean);
                            list.add(bean2);
                            list.add(bean3);
                        }
                        DVbean bean4 = new DVbean();
                        bean4.setUid(mLoginConfig.getUserId());
                        bean4.setBatchid(uuid.toString());
                        bean4.setDatak("lunchmain");
                        bean4.setDatav(dataSet2.getEntryForIndex(0).getValue() * 30 + "");

                        DVbean bean5 = new DVbean();
                        bean5.setUid(mLoginConfig.getUserId());
                        bean5.setBatchid(uuid.toString());
                        bean5.setDatak("lunchMeatandeggs");
                        bean5.setDatav(dataSet2.getEntryForIndex(1).getValue() * 30 + "");

                        DVbean bean6 = new DVbean();
                        bean6.setUid(mLoginConfig.getUserId());
                        bean6.setBatchid(uuid.toString());
                        bean6.setDatak("lunchVegetables");
                        bean6.setDatav(dataSet2.getEntryForIndex(2).getValue() * 30 + "");
                        if (cbx_22.isChecked()) {
                            list.add(bean4);
                            list.add(bean5);
                            list.add(bean6);
                        }
                        DVbean bean7 = new DVbean();
                        bean7.setUid(mLoginConfig.getUserId());
                        bean7.setBatchid(uuid.toString());
                        bean7.setDatak("dinnermain");
                        bean7.setDatav(dataSet3.getEntryForIndex(0).getValue() * 30 + "");

                        DVbean bean8 = new DVbean();
                        bean8.setUid(mLoginConfig.getUserId());
                        bean8.setBatchid(uuid.toString());
                        bean8.setDatak("dinnermilk");
                        bean8.setDatav(dataSet3.getEntryForIndex(1).getValue() * 30 + "");

                        DVbean bean9 = new DVbean();
                        bean9.setUid(mLoginConfig.getUserId());
                        bean9.setBatchid(uuid.toString());
                        bean9.setDatak("dinnerVegetables");
                        bean9.setDatav(dataSet3.getEntryForIndex(2).getValue() * 30 + "");
                        if (cbx_33.isChecked()) {
                            list.add(bean8);
                            list.add(bean7);
                            list.add(bean9);
                        }

                        DVbean bean10 = new DVbean();
                        bean10.setUid(mLoginConfig.getUserId());
                        bean10.setBatchid(uuid.toString());
                        bean10.setDatak("saltintakeOfHypertension");
                        if (seek_bar_yan.getProgress() <= 30)
                            bean10.setDatav("0");
                        else if (seek_bar_yan.getProgress() > 30 && seek_bar_yan.getProgress() < 65)
                            bean10.setDatav("1");
                        else bean10.setDatav("2");
                        list.add(bean10);

                        DVbean bean11 = new DVbean();
                        bean11.setUid(mLoginConfig.getUserId());
                        bean11.setBatchid(uuid.toString());
                        bean11.setDatak("oilInjection");
                        if (seek_bar_you.getProgress() <= 30)
                            bean11.setDatav("0");
                        else if (seek_bar_you.getProgress() > 30 && seek_bar_you.getProgress() < 65)
                            bean11.setDatav("1");
                        else bean11.setDatav("2");
                        list.add(bean11);
                        if (!et_gao.getText().toString().trim().equals("")) {
                            DVbean bean12 = new DVbean();
                            bean12.setUid(mLoginConfig.getUserId());
                            bean12.setBatchid(uuid.toString());
                            bean12.setDatak("highpurine");
                            bean12.setDatav(et_gao.getText().toString().trim());
                            list.add(bean12);
                        }

                        if (!et_zhong.getText().toString().trim().equals("")) {
                            DVbean bean13 = new DVbean();
                            bean13.setUid(mLoginConfig.getUserId());
                            bean13.setBatchid(uuid.toString());
                            bean13.setDatak("purine");
                            bean13.setDatav(et_zhong.getText().toString().trim());
                            list.add(bean13);
                        }

                        if (!et_di.getText().toString().trim().equals("")) {
                            DVbean bean14 = new DVbean();
                            bean14.setUid(mLoginConfig.getUserId());
                            bean14.setBatchid(uuid.toString());
                            bean14.setDatak("lowpurine");
                            bean14.setDatav(et_di.getText().toString().trim());
                            list.add(bean14);
                        }

                        getSubmitData(GsonUtils.toJsonString(list));

                        //饮水
                        ll_1.setVisibility(View.GONE);
                        ll_2.setVisibility(View.VISIBLE);
                        ll_3.setVisibility(View.GONE);
                        ll_4.setVisibility(View.GONE);
                        ll_5.setVisibility(View.GONE);
                        ll_6.setVisibility(View.GONE);
                        ll_7.setVisibility(View.GONE);
                        ll_8.setVisibility(View.GONE);
                    } else if (steps == 4) {
                        if (!tv_ysl.getText().toString().replace("ml", "").equals("")) {
                            ArrayList<DVbean> list = new ArrayList();
                            DVbean bean = new DVbean();
                            bean.setUid(mLoginConfig.getUserId());
                            bean.setBatchid(uuid.toString());
                            bean.setDatak("drinkWater");
                            bean.setDatav(tv_ysl.getText().toString().replace("ml", ""));
                            list.add(bean);
                            getSubmitData(GsonUtils.toJsonString(list));
                        }
                        //检测指标
                        ll_1.setVisibility(View.GONE);
                        ll_2.setVisibility(View.GONE);
                        ll_3.setVisibility(View.GONE);
                        ll_4.setVisibility(View.GONE);
                        ll_5.setVisibility(View.VISIBLE);
                        ll_6.setVisibility(View.GONE);
                        ll_7.setVisibility(View.GONE);
                        ll_8.setVisibility(View.GONE);
                    } else if (steps == 5) {
                        ArrayList<DVbean> list = new ArrayList();
                        for (int i = 0; i < arr_ed.size(); i++) {
                            DVbean bean = new DVbean();
                            bean.setUid(mLoginConfig.getUserId());
                            bean.setBatchid(uuid.toString());
                            bean.setDatak(arr_ed.get(i).getTag().toString());
                            bean.setDatav(arr_ed.get(i).getText().toString());
                            if (!arr_ed.get(i).getText().toString().equals(""))
                                list.add(bean);
                            if (Arrays.asList(getResources().getStringArray(
                                    R.array.testIndex_xz_key)).contains(arr_ed.get(i).getTag().toString())) {
                                bean.setType("blood_lipid_level_analysis");

                            } else if (Arrays.asList(getResources().getStringArray(
                                    R.array.testIndex_sg_key)).contains(arr_ed.get(i).getTag().toString())) {
                                bean.setType("renal_function");
                            } else if (Arrays.asList(getResources().getStringArray(
                                    R.array.testIndex_ncg_key)).contains(arr_ed.get(i).getTag().toString())) {
                                bean.setType("urine_routine");
                            }

                        }
                        getSubmitData(GsonUtils.toJsonString(list));
                        //生活习惯
                        ll_1.setVisibility(View.GONE);
                        ll_2.setVisibility(View.GONE);
                        ll_3.setVisibility(View.GONE);
                        ll_4.setVisibility(View.GONE);
                        ll_5.setVisibility(View.GONE);
                        ll_6.setVisibility(View.VISIBLE);
                        ll_7.setVisibility(View.GONE);
                        ll_8.setVisibility(View.GONE);
                    } else if (steps == 6) {
                        ArrayList<DVbean> list = new ArrayList();
                        DVbean bean = new DVbean();
                        bean.setUid(mLoginConfig.getUserId());
                        bean.setBatchid(uuid.toString());
                        bean.setDatak("smoke");
                        if (cbx_3.isChecked())
                            bean.setDatav("1");
                        else bean.setDatav("0");
                        list.add(bean);

                        DVbean bean2 = new DVbean();
                        bean2.setUid(mLoginConfig.getUserId());
                        bean2.setBatchid(uuid.toString());
                        bean2.setDatak("drink");
                        if (cbx_5.isChecked()) bean2.setDatav("2");
                        else if (cbx_6.isChecked()) bean2.setDatav("1");
                        else bean2.setDatav("0");
                        list.add(bean2);

                        DVbean bean5 = new DVbean();
                        bean5.setUid(mLoginConfig.getUserId());
                        bean5.setBatchid(uuid.toString());
                        bean5.setDatak("feel");
                        if (cbx_8.isChecked()) bean5.setDatav("2");
                        else if (cbx_9.isChecked()) bean5.setDatav("1");
                        else bean5.setDatav("0");
                        list.add(bean5);

                        DVbean bean3 = new DVbean();
                        bean3.setUid(mLoginConfig.getUserId());
                        bean3.setBatchid(uuid.toString());
                        bean3.setDatak("sport_level");
                        if (seek_bar_yd.getProgress() < 35) bean3.setDatav("0");
                        else if (seek_bar_yd.getProgress() > 70) bean3.setDatav("1");
                        else bean3.setDatav("2");
                        list.add(bean3);

                        DVbean bean4 = new DVbean();
                        bean4.setUid(mLoginConfig.getUserId());
                        bean4.setBatchid(uuid.toString());
                        bean4.setDatak("sport_time");
                        if (cbx_1.isChecked()) bean4.setDatav("1");
                        else bean4.setDatav("0");
                        list.add(bean4);
                        getSubmitData(GsonUtils.toJsonString(list));

                        //服药记录
                        ll_1.setVisibility(View.GONE);
                        ll_2.setVisibility(View.GONE);
                        ll_3.setVisibility(View.GONE);
                        ll_4.setVisibility(View.GONE);
                        ll_5.setVisibility(View.GONE);
                        ll_6.setVisibility(View.GONE);
                        ll_7.setVisibility(View.GONE);
                        ll_8.setVisibility(View.VISIBLE);
                        rtv_next.setText("完成并汇总");
                    } else if (steps == 7) {
                        ArrayList<MedRecord> list = new ArrayList<>();
                        for (int i = 0; i < arr_sp.size(); i++) {
                            MedRecord medrecord = new MedRecord();
                            medrecord.setUid(mLoginConfig.getUserId());
                            medrecord.setDrugname(arr_sp.get(i).getSelectedItem().toString());
                            medrecord.setDose(arr_edit.get(i).getText().toString().trim());
                            medrecord.setEattime(time.get(i));
                            list.add(medrecord);
//                            JsonObjectBuilder build = new JsonObjectBuilder();
//                            build.append("uid", mLoginConfig.getUserId());
//                            build.append("drugname", arr_sp.get(i).getSelectedItem().toString());
//                            build.append("dose", arr_edit.get(i).getText().toString().trim());
//                            build.append("eattime", time.get(i));
//                            getSubmitMedData(build.toString());
                        }
                        if (list.size() > 0)
                            getSubmitMedData(GsonUtils.toJsonString(list));

//                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
//                        rtv_next.setText("汇总");

                        ll_1.setVisibility(View.GONE);
                        ll_2.setVisibility(View.VISIBLE);
                        ll_3.setVisibility(View.VISIBLE);
                        ll_4.setVisibility(View.VISIBLE);
                        ll_5.setVisibility(View.VISIBLE);
                        ll_6.setVisibility(View.VISIBLE);
                        ll_7.setVisibility(View.VISIBLE);
                        ll_8.setVisibility(View.VISIBLE);
                        rtv_next.setText("提交分析");
                        rtv_next2.setVisibility(View.VISIBLE);

                    } else if (steps == 8) {
                        ll_status.removeAllViews();
                        WebView webView = new WebView(this);
                        webView.setLayoutParams(
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        ll_status.addView(webView);
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.loadUrl("file:///android_asset/jiance.html");
                        webView.setWebViewClient(new HelloWebViewClient());

                        final Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                timer.cancel();
                                finish();
                            }
                        }, 7000);

                    } else {
                        return;
                    }
                }
                break;
            case R.id.rtv_next2:
                InstantPush();
                ll_status.removeAllViews();
                WebView webView = new WebView(this);
                webView.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                ll_status.addView(webView);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl("file:///android_asset/jiance.html");
                webView.setWebViewClient(new HelloWebViewClient());

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        timer.cancel();
                        finish();
                    }
                }, 7000);

                break;

            case R.id.tv_center_1:
                if (!cbx_11.isChecked())
                    Toast.makeText(HealthStatusActivity.this, "请先勾选", Toast.LENGTH_SHORT).show();
                else {
                    IDataSet<PieEntry> dataSet = pieChart_1.getData().getDataSet();
                    Intent intent = new Intent();
                    intent.putExtra("data1", dataSet.getEntryForIndex(0).getValue());
                    intent.putExtra("data2", dataSet.getEntryForIndex(1).getValue());
                    intent.putExtra("data3", dataSet.getEntryForIndex(2).getValue());
                    intent.setClass(this, AdjustActivity.class);
                    startActivityForResult(intent, 101);
                }
                break;
            case R.id.tv_center_2:
                if (!cbx_22.isChecked())
                    Toast.makeText(HealthStatusActivity.this, "请先勾选", Toast.LENGTH_SHORT).show();
                else {
                    IDataSet<PieEntry> dataSet2 = pieChart_1.getData().getDataSet();
                    Intent intent2 = new Intent();
                    intent2.putExtra("data1", dataSet2.getEntryForIndex(0).getValue());
                    intent2.putExtra("data2", dataSet2.getEntryForIndex(1).getValue());
                    intent2.putExtra("data3", dataSet2.getEntryForIndex(2).getValue());
                    intent2.setClass(this, AdjustActivity.class);
                    startActivityForResult(intent2, 102);
                }
                break;
            case R.id.tv_center_3:
                if (!cbx_33.isChecked())
                    Toast.makeText(HealthStatusActivity.this, "请先勾选", Toast.LENGTH_SHORT).show();
                else {
                    IDataSet<PieEntry> dataSet3 = pieChart_1.getData().getDataSet();
                    Intent intent3 = new Intent();
                    intent3.putExtra("data1", dataSet3.getEntryForIndex(0).getValue());
                    intent3.putExtra("data2", dataSet3.getEntryForIndex(1).getValue());
                    intent3.putExtra("data3", dataSet3.getEntryForIndex(2).getValue());
                    intent3.setClass(this, AdjustActivity.class);
                    startActivityForResult(intent3, 103);
                }
                break;
            case R.id.tv_add_recode:
                final UUID uuid = UUID.randomUUID();
                View item_zb = LayoutInflater.from(this).inflate(R.layout.item_jcx, null);
                item_zb.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                sp_zhibiao = (Spinner) item_zb.findViewById(R.id.sp_zhibiao);
                sp_zhibiao.setTag(uuid);
                ll_child_value = (LinearLayout) item_zb.findViewById(R.id.ll_child_value);
                ll_child_value.setTag(uuid);
                ll_parent_jcx.addView(item_zb);
                sp_zhibiao.setAdapter(sp_jcx);

                sp_zhibiao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String[] data = {"检测值"};

                        switch (getResources().getStringArray(R.array.testName)[position]) {
                            case "血压":
                                data = getResources().getStringArray(R.array.testIndex_xy);
                                break;
                            case "血糖":
                                data = getResources().getStringArray(R.array.testIndex_xt);
                                break;
                            case "OGTT":
                                data = getResources().getStringArray(R.array.testIndex_ogtt);
                                break;
                            case "肝功":
                                data = getResources().getStringArray(R.array.testIndex_gg);
                                break;
                            case "肾功":
                                data = getResources().getStringArray(R.array.testIndex_sg);
                                break;
                            case "血脂":
                                data = getResources().getStringArray(R.array.testIndex_xz);
                                break;
                            case "离子检测":
                                data = getResources().getStringArray(R.array.testIndex_lzjc);
                                break;
                            case "血常规":
                                data = getResources().getStringArray(R.array.testIndex_xcg);
                                break;
                            case "尿常规":
                                data = getResources().getStringArray(R.array.testIndex_ncg);
                                break;
                        }

                        ll_child_value.removeAllViews();
                        for (int i = 0; i < data.length; i++) {
                            View item_jcx_v = LayoutInflater.from(HealthStatusActivity.this).inflate(R.layout.item_jcx_value, null);
                            item_jcx_v.setLayoutParams(
                                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
                            TextView tv_jcxName = (TextView) item_jcx_v.findViewById(R.id.tv_jcxName);
                            EditText et_jcx_value = (EditText) item_jcx_v.findViewById(R.id.et_jcx_value);
                            switch (position) {
                                case 0:
                                    et_jcx_value.setTag("hight");
                                    break;
                                case 1:
                                    et_jcx_value.setTag("weight");
                                    break;
                                case 2:
                                    et_jcx_value.setTag("waist");
                                    break;
                                case 3:
                                    et_jcx_value.setTag("serum_uric_acid");
                                    break;
                                case 4:
                                    et_jcx_value.setTag(getResources().getStringArray(R.array.testIndex_xy_key)[i]);
                                    break;
                                case 5:
                                    et_jcx_value.setTag(getResources().getStringArray(R.array.testIndex_sg_key)[i]);
                                    break;
                                case 6:
                                    et_jcx_value.setTag(getResources().getStringArray(R.array.testIndex_xz_key)[i]);
                                    break;
                                case 7:
                                    et_jcx_value.setTag(getResources().getStringArray(R.array.testIndex_ncg_key)[i]);
                                    break;
                            }
                            arr_ed.add(et_jcx_value);

                            tv_jcxName.setTag(uuid);
                            tv_jcxName.setText(data[i]);
                            Spinner sp_unit = (Spinner) item_jcx_v.findViewById(R.id.sp_unit);
                            sp_unit.setAdapter(sp_ad);
                            sp_unit.setTag(uuid);
                            ll_child_value.addView(item_jcx_v);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case R.id.tv_add_MedRecord:
                View item_med_record = LayoutInflater.from(HealthStatusActivity.this).inflate(R.layout.item_med_record, null);
                item_med_record.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Spinner sp_medName = (Spinner) item_med_record.findViewById(R.id.sp_medName);
                Spinner sp_jiliang = (Spinner) item_med_record.findViewById(R.id.sp_jiliang);
                EditText et_jiliang = (EditText) item_med_record.findViewById(R.id.et_jiliang);
                final CheckBox cbx_med_1 = (CheckBox) item_med_record.findViewById(R.id.cbx_med_1);
                final CheckBox cbx_med_2 = (CheckBox) item_med_record.findViewById(R.id.cbx_med_2);
                final CheckBox cbx_med_3 = (CheckBox) item_med_record.findViewById(R.id.cbx_med_3);
                final CheckBox cbx_med_4 = (CheckBox) item_med_record.findViewById(R.id.cbx_med_4);
                sp_medName.setAdapter(sp_med_name);
                sp_jiliang.setAdapter(sp_med_unit);
                ll_record.addView(item_med_record);

                arr_sp.add(sp_medName);
                arr_edit.add(et_jiliang);
                cbx_med_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cbx_med_2.setChecked(false);
                            cbx_med_3.setChecked(false);
                            cbx_med_4.setChecked(false);
                            time.add("0");
                        }
                    }
                });

                cbx_med_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cbx_med_1.setChecked(false);
                            cbx_med_3.setChecked(false);
                            cbx_med_4.setChecked(false);
                            time.add("1");
                        }
                    }
                });
                cbx_med_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cbx_med_2.setChecked(false);
                            cbx_med_1.setChecked(false);
                            cbx_med_4.setChecked(false);
                            time.add("2");
                        }
                    }
                });

                cbx_med_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cbx_med_2.setChecked(false);
                            cbx_med_3.setChecked(false);
                            cbx_med_1.setChecked(false);
                            time.add("3");
                        }
                    }
                });
                break;
        }
    }

    //提交服药记录
    private void getSubmitMedData(String data) {
        String Url = new AllUrl(this).eatdrugUrl();
        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(Url, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(Constants.GotoLoginActivity);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                handler.sendEmptyMessage(Constants.GET_SUCCESS3);
                            } else
                                handler.sendEmptyMessage(Constants.GET_FAIL);
                        }
                    }, this);
        } else {
            Utils.toast(this, "网络开小差了~");
            return;
        }
    }

    //马上看结果
    private void InstantPush() {
        String Url = new AllUrl(this).instantpushUrl();
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("id", mLoginConfig.getUserId());
        String data = builder.toString();
        if (HttpUtil.isNetworkAvailable(this)) {
//            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(Url, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(Constants.GotoLoginActivity);
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

    //现在症状
    private void getCurSymptoms() {
        String Url = new AllUrl(this).getCurSymptomsUrl();
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("uid", mLoginConfig.getUserId());
        String data = builder.toString();
        if (HttpUtil.isNetworkAvailable(this)) {
            showSweetAlertDialog(this);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(Url, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(Constants.GotoLoginActivity);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                zhengzhuangdata(bean.response);
                            } else
                                handler.sendEmptyMessage(Constants.GET_FAIL);
                        }
                    }, this);
        } else {
            Utils.toast(this, "网络开小差了~");
            return;
        }
    }

    private void zhengzhuangdata(String response) {
        JsonObject json = GsonUtils.getRootJsonObject(response);
        int errcode = GsonUtils.getKeyValue(json, "errcode").getAsInt();
        if (errcode == 0)
            zhengzhuang_list = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(json, "result").getAsJsonArray(), HealthType.class);

        handler.sendEmptyMessage(Constants.GET_SUCCESS4);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case 101:
                    int a = data.getExtras().getInt("data1", 0);
                    int b = data.getExtras().getInt("data2", 0);
                    int c = data.getExtras().getInt("data3", 0);
                    int[] mData = {a, b, c};
                    showPieChart(pieChart_1, setData(mData, 100));
                    break;
                case 102:
                    int a2 = data.getExtras().getInt("data1", 0);
                    int b2 = data.getExtras().getInt("data2", 0);
                    int c2 = data.getExtras().getInt("data3", 0);
                    int[] mData2 = {a2, b2, c2};
                    showPieChart(pieChart_2, setData(mData2, 100));
                    break;
                case 103:
                    int a3 = data.getExtras().getInt("data1", 0);
                    int b3 = data.getExtras().getInt("data2", 0);
                    int c3 = data.getExtras().getInt("data3", 0);
                    int[] mData3 = {a3, b3, c3};
                    showPieChart(pieChart_3, setData(mData3, 100));
                    break;
            }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());

    }

    @Override
    public void onNothingSelected() {

    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurSymptoms();
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
