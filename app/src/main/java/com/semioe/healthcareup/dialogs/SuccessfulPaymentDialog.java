package com.semioe.healthcareup.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.semioe.healthcareup.R;

/**
 * Created by Administrator on 2017/6/2.
 */

public class SuccessfulPaymentDialog extends Dialog {

    View.OnClickListener onClickListener;
    Context context;
    public SuccessfulPaymentDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    public SuccessfulPaymentDialog(Context context, int theme,View.OnClickListener onClickListener){
        super(context, theme);
        this.context = context;
        this.onClickListener = onClickListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_successful_payment);
        TextView gotoService = (TextView)findViewById(R.id.gotoService);
        TextView close = (TextView)findViewById(R.id.close);
        gotoService.setOnClickListener(onClickListener);
        close.setOnClickListener(onClickListener);
    }

}
