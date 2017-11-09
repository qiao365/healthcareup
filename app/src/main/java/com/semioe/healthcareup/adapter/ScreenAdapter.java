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
import com.semioe.healthcareup.bean.ServiceItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by songyuequan on 2017/5/19.
 */

public abstract class ScreenAdapter extends BaseAdapter<Bitmap> {

    private List<Doctors.Doctor> Doctors = new ArrayList<>();
    public ScreenAdapter(Context context) {
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
                R.layout.item_screen, null, false);
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
//        holder.price.setText(mDoctor.getOffice());
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

        public Holder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mAvatar = (ImageView) view.findViewById(R.id.avatar);
            mHospital = (TextView) view.findViewById(R.id.hospital);
            mPosition = (TextView) view.findViewById(R.id.position);
            Office = (TextView) view.findViewById(R.id.screen);
            price = (TextView) view.findViewById(R.id.price);
            btnpress = (TextView) view.findViewById(R.id.btnpressTv);
        }
    }
}

