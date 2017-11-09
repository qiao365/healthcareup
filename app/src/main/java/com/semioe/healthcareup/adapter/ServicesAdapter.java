package com.semioe.healthcareup.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semioe.healthcareup.R;

/**
 * Created by songyuequan on 2017/5/19.
 */

public class ServicesAdapter extends BaseAdapter<Bitmap> {


    public ServicesAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_service,null,false);
        Holder holder = new Holder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View v, final int position, Bitmap data) {
        Holder holder = (Holder) v.getTag();


    }

    private class Holder {
        TextView mName;
        TextView mHospital;
        TextView mPosition;
        TextView screen;
        ImageView mAvatar;
        public Holder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mAvatar = (ImageView) view.findViewById(R.id.avatar);
            mHospital = (TextView) view.findViewById(R.id.hospital);
            mPosition = (TextView) view.findViewById(R.id.position);
            screen = (TextView) view.findViewById(R.id.screen);

        }
    }
}

