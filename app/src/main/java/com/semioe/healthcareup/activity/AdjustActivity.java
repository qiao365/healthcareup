package com.semioe.healthcareup.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.semioe.healthcareup.R;

public class AdjustActivity extends BaseActivity implements View.OnClickListener {

    private RoundTextView rtv_1, rtv_2, rtv_3, rtv_ok;
    private SeekBar seek_bar, seek_bar_2, seek_bar_3;
    private CheckBox cbx_1, cbx_2, cbx_3;
    private TextView tv_ps_1, tv_ps_2, tv_ps_3;
    private float data_1 = 0;
    private float data_2 = 0;
    private float data_3 = 0;
    private ImageView iv_back;

    @Override
    public int bindLayout() {
        return R.layout.activity_adjust;
    }

    @Override
    public void initView(View view) {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        rtv_1 = (RoundTextView) findViewById(R.id.rtv_1);
        rtv_2 = (RoundTextView) findViewById(R.id.rtv_2);
        rtv_3 = (RoundTextView) findViewById(R.id.rtv_3);
        rtv_ok = (RoundTextView) findViewById(R.id.rtv_ok);
        rtv_ok.setOnClickListener(this);
        seek_bar = (SeekBar) findViewById(R.id.seek_bar);
        seek_bar_2 = (SeekBar) findViewById(R.id.seek_bar_2);
        seek_bar_3 = (SeekBar) findViewById(R.id.seek_bar_3);
        cbx_1 = (CheckBox) findViewById(R.id.cbx_1);
        cbx_2 = (CheckBox) findViewById(R.id.cbx_2);
        cbx_3 = (CheckBox) findViewById(R.id.cbx_3);
        tv_ps_1 = (TextView) findViewById(R.id.tv_ps_1);
        tv_ps_2 = (TextView) findViewById(R.id.tv_ps_2);
        tv_ps_3 = (TextView) findViewById(R.id.tv_ps_3);
    }

    private float division(float a, float b) {
        float result;
        if (b != 0)
            result = a / b;
        else result = 0;
        return result;
    }

    @Override
    public void doBusiness(Context mContext) {

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra("data1"))
                data_1 = getIntent().getExtras().getFloat("data1");
            if (getIntent().hasExtra("data2"))
                data_2 = getIntent().getExtras().getFloat("data2");
            if (getIntent().hasExtra("data3"))
                data_3 = getIntent().getExtras().getFloat("data3");
        }

        float ps_1 = division(data_1, data_1 + data_2 + data_3) * 100;
        float ps_2 = division(data_2, data_1 + data_2 + data_3) * 100;
        float ps_3 = division(data_3, data_1 + data_2 + data_3) * 100;

        rtv_1.setWidth((int) ps_1 * 8);
        rtv_2.setWidth((int) ps_2 * 8);
        rtv_3.setWidth((int) ps_3 * 8);
        seek_bar.setProgress((int) ps_1);
        seek_bar_2.setProgress((int) ps_2);
        seek_bar_3.setProgress((int) ps_3);

        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rtv_1.setWidth(progress * 8);
                tv_ps_1.setText(progress * 30 + "g");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek_bar_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rtv_2.setWidth(progress * 8);
                tv_ps_2.setText(progress * 30 + "g");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek_bar_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rtv_3.setWidth(progress * 8);
                tv_ps_3.setText(progress * 30 + "g");
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
                finish();
                break;
            case R.id.rtv_ok:
                Intent intent = new Intent();
                if (cbx_1.isChecked())
                    intent.putExtra("data1", seek_bar.getProgress());
                else
                    intent.putExtra("data1", 0);
                if (cbx_2.isChecked())
                    intent.putExtra("data2", seek_bar_2.getProgress());
                else
                    intent.putExtra("data2", 0);
                if (cbx_3.isChecked())
                    intent.putExtra("data3", seek_bar_3.getProgress());
                else
                    intent.putExtra("data3", 0);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }

    }
}
