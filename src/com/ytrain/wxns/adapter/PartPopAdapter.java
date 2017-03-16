package com.ytrain.wxns.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ytrain.wxns.R;
import com.ytrain.wxns.entity.PartEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PartPopAdapter extends BaseAdapter {
	private Context context;
	List<PartEntity> list = null;

	public PartPopAdapter(Context context, List<PartEntity> list) {
		super();
		this.context = context;
		setGetNavData(list);
	}

	private void setGetNavData(List<PartEntity> list) {
		if (list != null) {
			this.list = list;
		} else {
			list = new ArrayList<PartEntity>();
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		TextView title;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.partlist_item, null);
			mHolder = new ViewHolder();
			mHolder.title = (TextView) convertView.findViewById(R.id.tv_pop);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		PartEntity part = list.get(position);
		mHolder.title.setText(part.getName());
		return convertView;
	}

}
