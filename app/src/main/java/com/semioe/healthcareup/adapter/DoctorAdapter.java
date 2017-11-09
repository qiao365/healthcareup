package com.semioe.healthcareup.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.activity.DoctorProfileActivity;
import com.semioe.healthcareup.bean.Doctors;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songyuequan on 2017/5/19.
 */

public class DoctorAdapter extends BaseAdapter<Object> {

    private List<Doctors.Doctor> Doctors = new ArrayList<>();

    public DoctorAdapter(Context context) {
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
                R.layout.item_doctor,null,false);
        Holder holder = new Holder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View v, final int position, Object data) {
        Holder holder = (Holder) v.getTag();
        Doctors.Doctor mDoctor = Doctors.get(position);
        holder.mName.setText(mDoctor.getDname());
        holder.mHospital.setText(mDoctor.getHospital());
        holder.mPosition.setText(mDoctor.getDlevel());
        holder.skilled.setText(mDoctor.getGoodat());
                Picasso.with(v.getContext())
                .load(mDoctor.getHead_pic())
                .error(R.mipmap.avatar)
                .fit()
                .centerCrop()
                .into(holder.mAvatar);
    }

    private class Holder {
        TextView mName;
        TextView mHospital;
        TextView mPosition;
        TextView skilled;
        ImageView mAvatar;
        public Holder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mAvatar = (ImageView) view.findViewById(R.id.avatar);
            mHospital = (TextView) view.findViewById(R.id.hospital);
            mPosition = (TextView) view.findViewById(R.id.position);
            skilled = (TextView) view.findViewById(R.id.skilled);

        }
    }
}

