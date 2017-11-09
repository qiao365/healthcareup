package com.semioe.healthcareup.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.semioe.healthcareup.AppManager;
import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;
import com.semioe.healthcareup.activity.DoctorListActivity;
import com.semioe.healthcareup.activity.ShenGongActivity;
import com.semioe.healthcareup.adapter.FragmentArchivesItemAdapter;
import com.semioe.healthcareup.bean.Archive;
import com.semioe.healthcareup.bean.Doctors;
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
import com.semioe.healthcareup.views.ListViewForScrollView;
import com.spring.stepcounter.simplestepcounter.utils.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class HealthArchivesFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private boolean one = false;
    private boolean two = false;
    private boolean three = false;

    private static final String TAG = HealthArchivesFragment.class.getSimpleName();

    List<SSY> mSSY = new ArrayList<>();//收缩压
    List<SSY> mSZY = new ArrayList<>();//舒张压
    List<SSY> mXNS = new ArrayList<>();//血尿酸

    LineChart mChart;
    LineChart mChart2;

    public HealthArchivesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health_archives, container, false);
    }

    ScrollView mScrollView;
    ListViewForScrollView mListView1;
    ListViewForScrollView mListView2;
    ListViewForScrollView mListView3;
    FragmentArchivesItemAdapter mFragmentArchivesItemAdapter1;
    FragmentArchivesItemAdapter mFragmentArchivesItemAdapter2;
    FragmentArchivesItemAdapter mFragmentArchivesItemAdapter3;
    TextView mtvNCG, mtvSG, mtvXZ;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScrollView = (ScrollView) view.findViewById(R.id.mScrollView);
        mtvNCG = (TextView) view.findViewById(R.id.mtvNCG);
        mtvSG = (TextView) view.findViewById(R.id.mtvSG);
        mtvXZ = (TextView) view.findViewById(R.id.mtvXZ);
        mListView1 = (ListViewForScrollView) view.findViewById(R.id.mListView1);
        mListView2 = (ListViewForScrollView) view.findViewById(R.id.mListView2);
        mListView3 = (ListViewForScrollView) view.findViewById(R.id.mListView3);

        mFragmentArchivesItemAdapter1 = new FragmentArchivesItemAdapter(getActivity());
        mFragmentArchivesItemAdapter2 = new FragmentArchivesItemAdapter(getActivity());
        mFragmentArchivesItemAdapter3 = new FragmentArchivesItemAdapter(getActivity());
        mListView1.setAdapter(mFragmentArchivesItemAdapter1);
        mListView2.setAdapter(mFragmentArchivesItemAdapter2);
        mListView3.setAdapter(mFragmentArchivesItemAdapter3);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(this);// 当然也可以使用匿名内部类实现
        mChart = (LineChart) view.findViewById(R.id.chart);
        mChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
//        mChart.getAxisRight().setDrawGridLines(false);//么去掉网格
//        mChart.getAxisLeft().setDrawGridLines(false);
        Description des = new Description();
        des.setText("");
        mChart.setDescription(des);
        //上面右边效果图的部分代码，设置X轴
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        // 上面第一行代码设置了false,所以下面第一行即使设置为true也不会绘制AxisLine
        xAxis.setDrawAxisLine(true);//绘制该行旁边的轴线
        xAxis.setDrawGridLines(true);//竖线

        mChart2 = (LineChart) view.findViewById(R.id.chart2);
        mChart2.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
//        mChart.getAxisRight().setDrawGridLines(false);//么去掉网格
//        mChart.getAxisLeft().setDrawGridLines(false);
        mChart2.setDescription(des);
        //上面右边效果图的部分代码，设置X轴
        XAxis xAxis2 = mChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        // 上面第一行代码设置了false,所以下面第一行即使设置为true也不会绘制AxisLine
        xAxis2.setDrawAxisLine(true);//绘制该行旁边的轴线
        xAxis2.setDrawGridLines(true);//竖线

        set3Date();

        mtvNCG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int1 = new Intent(getContext(), ShenGongActivity.class);
                int1.putExtra("name", "尿常规");
                int1.putExtra("type", "urine_routine");
                startActivity(int1);
            }
        });
        mtvSG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int1 = new Intent(getContext(), ShenGongActivity.class);
                int1.putExtra("name", "肾功");
                int1.putExtra("type", "renal_function");
                startActivity(int1);
            }
        });
        mtvXZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int1 = new Intent(getContext(), ShenGongActivity.class);
                int1.putExtra("name", "血脂");
                int1.putExtra("type", "blood_lipid_level_analysis");
                startActivity(int1);
            }
        });
    }

    @Override
    public void onResume() {
        getAllDatas();
        super.onResume();
    }

    private void set3Date() {
        List<Archive> l1 = new ArrayList<>();
        List<Archive> l2 = new ArrayList<>();
        List<Archive> l3 = new ArrayList<>();
        l1.add(new Archive("项目", "单位", "检测值"));
        l2.add(new Archive("项目", "单位", "检测值"));
        l3.add(new Archive("项目", "单位", "检测值"));
        String[] str = {"血尿素氮(BUN)", "血肌酐(Scr)", "血尿酸 血肌酐(Cr)", "尿蛋白",
                "选择性蛋白尿指数(SPI)", "β2-微球蛋白清除试验", "尿素清除率", "血内生肌酐清除率", "尿素氮/肌酐比值(BUN)", "酚红(酚磺太)排泄试验(PSP)",
                "酚红(酚磺太)排泄试验(PSP)"};

        l1.add(new Archive(str[0], "mmol/L", (int) (Math.random() * 100 + 1) + ""));
        l1.add(new Archive(str[1], "μmol/L", (int) (Math.random() * 100 + 1) + ""));
        l1.add(new Archive(str[2], "μmol/L", (int) (Math.random() * 100 + 1) + ""));
        l1.add(new Archive(str[3], "μmmol·kg-1/d", (int) (Math.random() * 100 + 1) + ""));
        l1.add(new Archive(str[4], "", (int) (Math.random() * 100 + 1) + ""));
        l1.add(new Archive(str[5], "", (int) (Math.random() * 100 + 1) + ""));
        l1.add(new Archive(str[6], "μl/min", (int) (Math.random() * 100 + 1) + ""));
        l1.add(new Archive(str[7], "ml·s-1/m2", (int) (Math.random() * 100 + 1) + ""));
        l1.add(new Archive(str[8], "ml·s-1/m2", (int) (Math.random() * 100 + 1) + ""));
        l1.add(new Archive(str[9], "", (int) (Math.random() * 100 + 1) + ""));
        l1.add(new Archive(str[10], "", (int) (Math.random() * 100 + 1) + ""));

        l2.add(new Archive("总胆固醇（TC）", "mmol/L", (int) (Math.random() * 100 + 1) + ""));
        l2.add(new Archive("甘油三酯（TG）", "mmol/L", (int) (Math.random() * 100 + 1) + ""));
        l2.add(new Archive("高密度脂蛋白（HDL-C）", "mmol/L", (int) (Math.random() * 100 + 1) + ""));
        l2.add(new Archive("低密度脂蛋白（LDL-C）", "mmol/L", (int) (Math.random() * 100 + 1) + ""));

        l3.add(new Archive("白细胞(LEU)", "阴性", (int) (Math.random() * 100 + 1) + ""));
        l3.add(new Archive("亚硝酸盐(NIT)", "阴性", (int) (Math.random() * 100 + 1) + ""));
        l3.add(new Archive("尿胆原(UBG) ", "阴性", (int) (Math.random() * 100 + 1) + ""));
        l3.add(new Archive("蛋白质(PRO)", "阴性", (int) (Math.random() * 100 + 1) + ""));
        l3.add(new Archive("酸碱度(pH)", "4.6 - 8.0", (int) (Math.random() * 100 + 1) + ""));
        l3.add(new Archive("潜血(BLD)", "阴性", (int) (Math.random() * 100 + 1) + ""));
        l3.add(new Archive("比重(SG)", "1.002 - 1.030", (int) (Math.random() * 100 + 1) + ""));
        l3.add(new Archive("酮体(KET)", "阴性", (int) (Math.random() * 100 + 1) + ""));
        l3.add(new Archive("胆红素(BIL)", "阴性", (int) (Math.random() * 100 + 1) + ""));
        l3.add(new Archive("葡萄糖(GLU)", "阴性", (int) (Math.random() * 100 + 1) + ""));
        l3.add(new Archive("维生素C(VC)", "阴性", (int) (Math.random() * 100 + 1) + ""));

        mFragmentArchivesItemAdapter1.addArchives(l1);
        mFragmentArchivesItemAdapter2.addArchives(l2);
        mFragmentArchivesItemAdapter3.addArchives(l3);
        mScrollView.smoothScrollTo(0, 0);
    }

    private void setData(int count, float range) {
        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<String> xVals1 = new ArrayList<>();
        for (int i = 0; i < mSSY.size(); i++) {
            String str = mSSY.get(i).getDatav();
            if (!isNumericAndPoint(str) || str.equals("")) {
                str = "0";
            }
            float mult = Float.parseFloat(str);
            yVals1.add(new Entry(i, mult));
            if (mSSY.size() > 0) {
                xVals1.add(TimeUtils.getFormatDate(mSSY.get(i).getCreateDate()));
            }
        }

        ArrayList<Entry> yVals2 = new ArrayList<>();
        ArrayList<String> xVals2 = new ArrayList<>();
        for (int i = 0; i < mSZY.size(); i++) {
            String str = mSZY.get(i).getDatav();
            if (!isNumericAndPoint(str) || str.equals("")) {
                str = "0";
            }
            float mult = Float.parseFloat(str);
            yVals2.add(new Entry(i, mult));
//            xVals2.add(TimeUtils.getFormatDate(mSZY.get(i).getCreateDate()));
        }

        LineDataSet set1, set2, set3;
        // create a dataset and give it a type
        set1 = new LineDataSet(yVals1, "收缩压");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(getResources().getColor(R.color.line_01));
        set1.setCircleColor(getResources().getColor(R.color.line_round));
        set1.setLineWidth(1f);
        set1.setCircleRadius(2f);
        set1.setFillAlpha(65);
        set1.setFillColor(getResources().getColor(R.color.line_01));
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(true);
        set1.setCubicIntensity(0.6f);
        set1.setLineWidth(.4f);

        // create a dataset and give it a type
        set2 = new LineDataSet(yVals2, "舒张压");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(getResources().getColor(R.color.line_02));
        set2.setCircleColor(getResources().getColor(R.color.line_round));
        set2.setLineWidth(1f);
        set2.setCircleRadius(2f);
        set2.setFillAlpha(65);
        set2.setFillColor(getResources().getColor(R.color.line_02));
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<LineDataSet> dataSets1 = new ArrayList<LineDataSet>();
        dataSets1.add(set1); // add the datasets

        // create a data object with the datasets

        LineData data = new LineData(set1, set2);
        if (yVals1.size() > 0 || yVals2.size() > 0) {
            if (yVals1.size() > 0 && yVals2.size() > 0) {
                data = new LineData(set1, set2);
            } else if (yVals1.size() > 0) {
                data = new LineData(set1);
            } else {
                data = new LineData(set2);
            }
            data.setDrawValues(true);
            data.setValueTextColor(getResources().getColor(R.color.warm_grey));
            data.setValueTextSize(8f);

//            //自定义x轴显示
            MyXFormatter formatter = new MyXFormatter(xVals1);
            XAxis xAxis = mChart.getXAxis();
            //显示个数
//            xAxis.setLabelCount(xVals1.size(),true);
            xAxis.setValueFormatter(formatter);
//            xAxis.setLabelRotationAngle(9f);
            mChart.setData(data);
            mChart.animateX(1000);
        }


        ArrayList<Entry> yVals3 = new ArrayList<>();
        ArrayList<String> xVals3 = new ArrayList<>();
        for (int i = 0; i < mXNS.size(); i++) {
            String str = mXNS.get(i).getDatav();
            if (!isNumericAndPoint(str) || str.equals("")) {
                str = "0";
            }
            float mult = Float.parseFloat(str);
            yVals3.add(new Entry(i, mult));
            xVals3.add(TimeUtils.getFormatDate(mXNS.get(i).getCreateDate()));
        }

        set3 = new LineDataSet(yVals3, "血尿酸");
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
        set3.setColor(getResources().getColor(R.color.line_04));
        set3.setCircleColor(getResources().getColor(R.color.line_round));
        set3.setLineWidth(1f);
        set3.setCircleRadius(2f);
        set3.setFillAlpha(65);
        set3.setFillColor(getResources().getColor(R.color.line_04));
        set3.setDrawCircleHole(false);
        set3.setHighLightColor(Color.rgb(244, 117, 117));

        if (yVals3.size() > 0) {
            LineData data2 = new LineData(set3);
            data2.setValueTextColor(getResources().getColor(R.color.warm_grey));
            data2.setValueTextSize(8f);

            //            //自定义x轴显示
            MyXFormatter formatter = new MyXFormatter(xVals3);
            XAxis xAxis = mChart2.getXAxis();
            //显示个数
//            xAxis.setLabelCount(xVals1.size());
            xAxis.setValueFormatter(formatter);

            mChart2.setData(data2);
            mChart2.animateX(1000);
        }
    }

    public class MyXFormatter implements IAxisValueFormatter {

        private ArrayList<String> mValues;

        //        ArrayList<String> xVals2 = new ArrayList<>();
        public MyXFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        private static final String TAG = "MyXFormatter";

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            Log.d(TAG, "----->getFormattedValue: " + value);
            System.out.println("----->getFormattedValue: " + value);
            if (mValues.size() == 0) {
                return "";
            }
            return mValues.get((int) value % mValues.size());
        }
    }

    private String begintime = TimeUtils.getBefore3mDate();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.GET_SUCCESS:
                    if (one && two && three) {
                        setData(10, 400);
                    }
                    break;
                case Constants.GET_FAIL:

                    break;
                case Constants.GotoLoginActivity:
                    Utils.toast(getContext(), "登陆失效,请重新登陆");
                    new LoginConfig(getContext()).setAvailbleTime("0");
                    AppManager.getInstance().AppExitToLogin(getContext(),
                            handler);
                    break;
            }
        }
    };

    private void getAllDatas() {
        String msgUrl = new AllUrl(getContext()).getHealthDataUrl();
        if (HttpUtil.isNetworkAvailable(getContext())) {
            JsonObjectBuilder builder = new JsonObjectBuilder();
            builder.append("uid", LoginConfig.getUserId());
            builder.append("datak", "systolic_blood_pressure");
            builder.append("begintime", begintime);
            builder.append("endtime", TimeUtils.getCurrentData());
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
                            one = true;
                        }

                        private void strToBean(String response) {
                            if (response.contains("OK")) {
                                mSSY = new ArrayList<SSY>();
                                if (!GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(response), "result").toString().contains("[]"))
                                    mSSY = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(response), "result").getAsJsonArray(), SSY.class);
                                handler.sendEmptyMessage(Constants.GET_SUCCESS);
                            }
                        }
                    }, getContext());
        } else {
            Utils.toast(getContext(), "网络开小差了~");
            return;
        }

        if (HttpUtil.isNetworkAvailable(getContext())) {
            JsonObjectBuilder builder = new JsonObjectBuilder();
            builder.append("uid", LoginConfig.getUserId());
            builder.append("datak", "diastolic_blood_pressure");
            builder.append("begintime", begintime);
            builder.append("endtime", TimeUtils.getCurrentData());
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
                            two = true;

                        }

                        private void strToBean(String response) {
                            if (response.contains("OK")) {
                                mSZY = new ArrayList<SSY>();
                                mSZY = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(response), "result").getAsJsonArray(), SSY.class);
                                handler.sendEmptyMessage(Constants.GET_SUCCESS);
                            }

                        }
                    }, getContext());
        } else {
            Utils.toast(getContext(), "网络开小差了~");
            return;
        }

        //血尿酸
        if (HttpUtil.isNetworkAvailable(getContext())) {
            JsonObjectBuilder builder = new JsonObjectBuilder();
            builder.append("uid", LoginConfig.getUserId());
            builder.append("datak", "serum_uric_acid");
            builder.append("begintime", begintime);
            builder.append("endtime", TimeUtils.getCurrentData());
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
                            three = true;
                        }

                        private void strToBean(String response) {
                            if (response.contains("OK")) {
                                mXNS = new ArrayList<SSY>();
                                mXNS = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(response), "result").getAsJsonArray(), SSY.class);
                                handler.sendEmptyMessage(Constants.GET_SUCCESS);
                            }

                        }
                    }, getContext());
        } else {
            Utils.toast(getContext(), "网络开小差了~");
            return;
        }

//        //肾功renal_function
//        if (HttpUtil.isNetworkAvailable(getContext())) {
//            JsonObjectBuilder builder = new JsonObjectBuilder();
//            builder.append("uid", LoginConfig.getUserId());
//            builder.append("datak", "renal_function");
//            builder.append("begintime", begintime);
//            builder.append("endtime", TimeUtils.getCurrentData());
//            String data = builder.toString();
//            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(msgUrl, data,
//                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
//                    new RequestListener<BaseResponseBean>() {
//
//                        @Override
//                        public void onFailed() {
//                            handler.sendEmptyMessage(Constants.GotoLoginActivity);
//                        }
//
//                        @Override
//                        public void onComplete(BaseResponseBean bean) {
//                            if (bean.isSuccess()) {
//                                strToBean(bean.response);
//                            } else
//                                handler.sendEmptyMessage(Constants.GET_FAIL);
//                        }
//
//                        private void strToBean(String response) {
//                            if (response.contains("OK")) {
//                                mXNS = new ArrayList<SSY>();
//                                mXNS = GsonUtils.JsonArrayToListBean(GsonUtils.getKeyValue(GsonUtils.getRootJsonObject(response), "result").getAsJsonArray(), SSY.class);
//                                handler.sendEmptyMessage(Constants.GET_SUCCESS);
//                            }
//
//                        }
//                    }, getContext());
//        } else {
//            Utils.toast(getContext(), "网络开小差了~");
//            return;
//        }
    }

    public static boolean isNumericAndPoint(String str) {
        if (str == null || str.equals("")) return false;
        if (str.contains("-")) {
            str = str.replace("-", "");
        }
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {//代表不是数字
                if (Character.toString(str.charAt(i)).equals(".")) {//代表是小数点
                    Log.e("wwwwww", "找到小数点");
                } else {
                    return false;
                }

            }
        }

        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
            case R.id.rb0:
                begintime = TimeUtils.getBefore3mDate();
                break;
            case R.id.rb1:
                begintime = TimeUtils.getBefore6mDate();
                break;
            case R.id.rb2:
                begintime = "2010-09-11 00:00:00";
                break;
            default:
                break;
        }
        getAllDatas();
    }
}
