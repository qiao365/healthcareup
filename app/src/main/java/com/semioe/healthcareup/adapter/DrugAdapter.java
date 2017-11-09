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

/**
 * Created by songyuequan on 2017/5/19.
 */

public class DrugAdapter extends BaseAdapter<Object> {


    public DrugAdapter(Context context) {
        super(context);
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
//        holder.mName.setText(data.getTruename());
//        holder.mHospital.setText(data.getHospital());
//        holder.skilled.setText(data.getGoodat());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, DoctorProfileActivity.class));
            }
        });

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

