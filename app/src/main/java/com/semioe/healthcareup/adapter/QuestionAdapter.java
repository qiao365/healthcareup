package com.semioe.healthcareup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.bean.Question;

/**
 * Created by songyuequan on 2017/5/19.
 */

public class QuestionAdapter extends BaseAdapter<Question> {


    public QuestionAdapter(Context context) {
        super(context);
    }


    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_question,null,false);
        Holder holder = new Holder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View v, final int position, final Question data) {
        Holder holder = (Holder) v.getTag();
        holder.mName.setText(data.getLable());
        holder.mHospital.setText(data.getType());
        holder.skilled.setText(data.getDescription());
        holder.mPosition.setText(data.getValue());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AnswerParam answerParam = new AnswerParam(SP.getUser(mContext).getUserId(), data.getType(), data.getValue());
//                AnswerParam[] answerParams = new AnswerParam[1];
//                answerParams[0] = answerParam;
//                HealthWebService.answer(mContext, answerParams, new CommonCallBack(mContext) {
//                    @Override
//                    public void onSuccess(String data) {
//                        Toast.makeText(mContext,"保存成功",Toast.LENGTH_SHORT).show();
//                    }
//                });
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

