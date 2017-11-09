package com.semioe.healthcareup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.bean.Archive;
import com.semioe.healthcareup.bean.Doctors;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjq on 2017/5/19.
 */

public class FragmentArchivesItemAdapter extends BaseAdapter<Object> {

    private List<Archive> Archives = new ArrayList<>();

    public FragmentArchivesItemAdapter(Context context) {
        super(context);
    }

    public void addArchives(List<Archive> mDoctors){
        Archives.addAll(mDoctors);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return Archives.size();
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_fragment_archives,null,false);
        Holder holder = new Holder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View v, final int position, Object data) {
        Holder holder = (Holder) v.getTag();
        Archive mDoctor = Archives.get(position);
        holder.mName.setText(mDoctor.getName());
        holder.mAmount.setText(mDoctor.getAmount());
        holder.mUnit.setText(mDoctor.getUnit());
        if (position == 0){
            holder.mAmount.setBackgroundResource(R.color.white);
            holder.mUnit.setBackgroundResource(R.color.white);
            holder.mAmount.setTextColor(v.getResources().getColor(R.color.warm_grey));
            holder.mUnit.setTextColor(v.getResources().getColor(R.color.warm_grey));
        }else {
            holder.mAmount.setBackgroundResource(R.color.line_round);
            holder.mUnit.setBackgroundResource(R.color.line_01);
            holder.mAmount.setTextColor(v.getResources().getColor(R.color.white));
            holder.mUnit.setTextColor(v.getResources().getColor(R.color.white));
        }
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

