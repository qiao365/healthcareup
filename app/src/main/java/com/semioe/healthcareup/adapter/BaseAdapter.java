package com.semioe.healthcareup.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
	protected Context mContext;
	protected List<T> data;

	public BaseAdapter(Context context) {
		mContext = context;
		data = new ArrayList<T>();
	}

	public BaseAdapter(Context context, List<T> data) {
		this.mContext = context;
		this.data = data;
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findViewById(View view, int id) {
		return (T) view.findViewById(id);
	}

	public int findPosition(T message) {
		int index = getCount();
		int position = -1;
		while (index-- > 0) {
			if (message.equals(getItem(index))) {
				position = index;
				break;
			}
		}
		return position;
	}

	public int findPosition(long id) {
		int index = getCount();
		int position = -1;
		while (index-- > 0) {
			if (getItemId(index) == id) {
				position = index;
				break;
			}
		}
		return position;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> list) {
		data = list;
	}

	public void add(List<T> list) {
		data.addAll(list);
	}

	public void add(T t, int position) {
		data.add(position, t);
	}

	public void add(T t) {
		data.add(t);
	}

	public void remove(int position) {
		data.remove(position);
	}

	public void removeAll() {
		data.clear();
	}

	public void clear() {
		data.clear();
	}

	@Override
	public int getCount() {
		if (data == null)
			return 0;

		return data.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public T getItem(int position) {
		if (data == null)
			return null;

		if (position >= data.size())
			return null;

		return data.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = newView(mContext, position, parent);
		}
		bindView(view, position, getItem(position));
		return view;
	}

	protected abstract View newView(Context context, int position,
									ViewGroup group);

	protected abstract void bindView(View v, int position, T data);

}
