package com.semioe.healthcareup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.bean.Device;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by songyuequan on 2017/5/19.
 */

public class DevicesAdapter extends BaseAdapter<Object> {

    private List<Device> mDevices = new ArrayList<>();

    public void addDevices(List<Device> mDevice) {
        this.mDevices = mDevice;
        notifyDataSetChanged();
    }

    public DevicesAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_devices, null, false);
        Holder holder = new Holder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View v, final int position, Object data) {
        Holder holder = (Holder) v.getTag();
        Device mDevice = mDevices.get(position);
        holder.mName.setText(mDevice.getName());
//        holder.introduce.setText(data.getName());
        holder.btnTvpRESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private class Holder {
        TextView mName;
        TextView btnTvpRESS;
        TextView introduce;
        ImageView mAvatar;

        public Holder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mAvatar = (ImageView) view.findViewById(R.id.avatar);
            introduce = (TextView) view.findViewById(R.id.introduce);
            btnTvpRESS = (TextView) view.findViewById(R.id.btnTvpRESS);
        }
    }
}

