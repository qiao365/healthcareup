package com.semioe.healthcareup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.bean.ServiceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjq on 2017/5/19.
 * 症状adapter
 */

public abstract class SymptomAdapter extends BaseAdapter<Object> {

    private ArrayList<ServiceItem> ServiceItems = new ArrayList<>();

    private String stType = "咨询";

    public SymptomAdapter(Context context) {
        super(context);
    }

    public ArrayList<ServiceItem> getServiceItems(){
        return ServiceItems;
    }

    public void setstType(String stType) {
        this.stType = stType;
        notifyDataSetChanged();
    }

    public void addServiceItems(List<ServiceItem> mServiceItems) {
        ServiceItems.clear();
        ServiceItems.addAll(mServiceItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ServiceItems.size();
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_symptom, null, false);
        Holder holder = new Holder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View v, final int position, Object data) {
        final Holder holder = (Holder) v.getTag();
        final ServiceItem mServiceItem = ServiceItems.get(position);
        holder.mTVname.setText(mServiceItem.getServiceName() + stType);
        holder.mRMB.setText("¥ "+mServiceItem.getPrice());
        holder.mTVxunwen.setText("您可以询问有关"  + "等，医生接单后24小时以内，您有任务相关问题都可以咨询医生");
        holder.mBtnShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mServiceItem.setServiceName(holder.mTVname.getText().toString());
                btnPress(mServiceItem, position);
            }
        });
    }

    public abstract void btnPress(ServiceItem mServiceItem, int position);

    private class Holder {
        TextView mTVname;
        TextView mTVxunwen;
        TextView mBtnShopping;
        TextView mRMB;
        ImageView mAvatar;

        public Holder(View view) {
            mRMB = (TextView) view.findViewById(R.id.mRMB);
            mTVname = (TextView) view.findViewById(R.id.mTVname);
            mTVxunwen = (TextView) view.findViewById(R.id.mTVxunwen);
            mBtnShopping = (TextView) view.findViewById(R.id.mBtnShopping);
        }
    }
}

