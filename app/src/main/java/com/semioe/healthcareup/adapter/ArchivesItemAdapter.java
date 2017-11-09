package com.semioe.healthcareup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.bean.SSY;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjq on 2017/5/19.
 */

public class ArchivesItemAdapter extends BaseAdapter<Object> {

    private List<SSY> SSYs = new ArrayList<>();
    Context context;
    String[] keys = new String[30];
    String[] names = new String[30];

    public ArchivesItemAdapter(Context context) {
        super(context);
        this.context = context;
        keys = context.getResources().getStringArray(R.array.testIndex_sg_key);
        names = context.getResources().getStringArray(R.array.testIndex_sg);
    }

    public void setKeysNames(String[] keys, String[] names) {
        this.names = names;
        this.keys = keys;
    }

    public void addSSYs(List<SSY> mDoctors) {
        for(SSY mSSY:mDoctors){
            for(String key:keys){
                if (mSSY.getDatak().equals(key)){
                    SSYs.add(mSSY);
                }
            }
        }
//        SSYs.addAll(mDoctors);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return SSYs.size();
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_fragment_archives, null, false);
        Holder holder = new Holder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View v, final int position, Object data) {
        Holder holder = (Holder) v.getTag();
        SSY mDoctor = SSYs.get(position);
        holder.mName.setText(mDoctor.getCreateDate());
        holder.mAmount.setText(mDoctor.getDatak());
        holder.mUnit.setText(mDoctor.getDatav());
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(mDoctor.getDatak())) {
                holder.mAmount.setText(names[i]);
                break;
            }
        }
//        if (position == 0){
//            holder.mAmount.setBackgroundResource(R.color.white);
//            holder.mUnit.setBackgroundResource(R.color.white);
//            holder.mAmount.setTextColor(v.getResources().getColor(R.color.warm_grey));
//            holder.mUnit.setTextColor(v.getResources().getColor(R.color.warm_grey));
//        }else {
//            holder.mAmount.setBackgroundResource(R.color.line_round);
//            holder.mUnit.setBackgroundResource(R.color.line_01);
//            holder.mAmount.setTextColor(v.getResources().getColor(R.color.white));
//            holder.mUnit.setTextColor(v.getResources().getColor(R.color.white));
//        }
    }

    private class Holder {
        TextView mName;
        TextView mAmount;
        TextView mUnit;

        public Holder(View view) {
            mName = (TextView) view.findViewById(R.id.but1);
            mAmount = (TextView) view.findViewById(R.id.but2);
            mUnit = (TextView) view.findViewById(R.id.but3);

        }
    }
}

