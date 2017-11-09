package com.semioe.healthcareup.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.activity.HealthStatusActivity;
import com.semioe.healthcareup.activity.RiskAssessmentActivity;
import com.semioe.healthcareup.bean.Message;
import com.semioe.healthcareup.bean.Msg;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cjq on 2017/5/19.
 */

public class MsgsAdapter extends BaseAdapter<Object> {

    private List<Msg> mMessages = new ArrayList<>();

    public void addMessages(List<Msg> mMessage) {
        this.mMessages.clear();
        this.mMessages.addAll(mMessage);
        notifyDataSetChanged();
    }

    public MsgsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_message, null, false);
        Holder holder = new Holder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View v, final int position, Object data) {
        Holder holder = (Holder) v.getTag();
        Msg mMessage = mMessages.get(position);
        holder.year.setText(mMessage.getCreateDate());
        holder.contrnt.setText(mMessage.getMsg());
        if(mMessage.getType() == 0){
            holder.tvJKTX.setText("健康提醒");
        }else {
            holder.tvJKTX.setText("生活风险提醒");
        }
        holder.tvJKTX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent( view.getContext(), RiskAssessmentActivity.class));
            }
        });
        holder.tvDBTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent( view.getContext(), HealthStatusActivity.class));
            }
        });
    }

    private class Holder {
        TextView year;
        TextView contrnt;
        TextView mtvGoPg;
        TextView tvJKTX;
        TextView tvDBTT;

        public Holder(View view) {
            year = (TextView) view.findViewById(R.id.year);
            tvJKTX = (TextView) view.findViewById(R.id.tvJKTX);
            contrnt = (TextView) view.findViewById(R.id.contrnt);
            mtvGoPg = (TextView) view.findViewById(R.id.mtvGoPg);
            tvDBTT = (TextView) view.findViewById(R.id.tvDBTT);
        }
    }
}

