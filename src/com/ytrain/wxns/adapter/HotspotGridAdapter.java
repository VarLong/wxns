package com.ytrain.wxns.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.ssy.utils.Constants;
import com.ytrain.wxns.R;
import com.ytrain.wxns.asynloading.GlideRoundTransform;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HotspotGridAdapter extends BaseAdapter {
	private Context context;
	List<Map<String, Object>> list = null;
	String imgUrl = null;

	public HotspotGridAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		setGetGridData(list);
	}

	private void setGetGridData(List<Map<String, Object>> list) {
		if (list != null) {
			this.list = list;
		} else {
			list = new ArrayList<Map<String, Object>>();
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
		ImageView ivImg;
		TextView title;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.hotspot_grid_item, null);
			mHolder = new ViewHolder();
			mHolder.ivImg = (ImageView) convertView.findViewById(R.id.ivImg);
			mHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			// 动态设置item的高度
			convertView.setLayoutParams(
					new AbsListView.LayoutParams(parent.getWidth() / 9 * 4, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			// 为自定义的imageview设置高度，
			mHolder.ivImg.getLayoutParams().height = (int) (parent.getWidth() / 9 * 4 * 0.6);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		Map<String, Object> hotspot = list.get(position);
		mHolder.title.setText(Constants.nullOfProperty(hotspot.get("title")).toString());
		imgUrl = hotspot.get("img").toString();
		if (imgUrl == null) {
			mHolder.ivImg.setImageResource(R.drawable.app_default);
		} else {

			Glide.with(context).load(Constants.dataUrl + "/" + imgUrl)
			.transform(new GlideRoundTransform(context)).into(mHolder.ivImg);
		}
		return convertView;
	}

}
