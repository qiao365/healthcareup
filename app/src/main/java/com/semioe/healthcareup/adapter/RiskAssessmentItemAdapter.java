package com.semioe.healthcareup.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.bean.Doctors;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cjq on 2017/5/19.
 */

public abstract class RiskAssessmentItemAdapter extends BaseAdapter<Bitmap> {

    private List<Doctors.Doctor> Doctors = new ArrayList<>();
    public RiskAssessmentItemAdapter(Context context) {
        super(context);
    }

    public void addDoctors(List<Doctors.Doctor> mDoctors){
        Doctors.addAll(mDoctors);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return Doctors.size();
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_risk_assessment, null, false);
        Holder holder = new Holder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View v, final int position, Bitmap data) {
        Holder holder = (Holder) v.getTag();
        final Doctors.Doctor mDoctor = Doctors.get(position);
        holder.mName.setText(mDoctor.getDname());
        holder.mHospital.setText(mDoctor.getHospital());
        holder.mPosition.setText(mDoctor.getDlevel());
        holder.Office.setText(mDoctor.getOffice());
//        holder.price.setText(mDoctor.getHead_pic());
        Picasso.with(v.getContext())
                .load(mDoctor.getHead_pic())
                .error(R.mipmap.avatar)
                .fit()
                .centerCrop()
                .into(holder.mAvatar);
        holder.btnpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPress(mDoctor,position);
            }
        });
        if (mDoctor.getDname().equals("施海峰")){
            holder.mTvAssessmentName.setText("高血压风险评估");
        }else {
            holder.mTvAssessmentName.setText("痛风风险评估");
        }
    }
    public abstract void btnPress(Doctors.Doctor mDoctor, int position);
    private class Holder {
        TextView mName;
        TextView mHospital;
        TextView mPosition;
        TextView Office;
        TextView price;
        ImageView mAvatar;
        TextView btnpress;
        TextView mTvAssessmentName;

        public Holder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mAvatar = (ImageView) view.findViewById(R.id.avatar);
            mHospital = (TextView) view.findViewById(R.id.hospital);
            mPosition = (TextView) view.findViewById(R.id.position);
            Office = (TextView) view.findViewById(R.id.screen);
            price = (TextView) view.findViewById(R.id.price);
            btnpress = (TextView) view.findViewById(R.id.btnpressTv);
            mTvAssessmentName = (TextView) view.findViewById(R.id.mTvAssessmentName);
        }
    }
}

