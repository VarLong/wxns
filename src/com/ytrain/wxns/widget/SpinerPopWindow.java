package com.ytrain.wxns.widget;

import java.util.List;

import com.ytrain.wxns.R;
import com.ytrain.wxns.adapter.AbstractSpinerAdapter;
import com.ytrain.wxns.adapter.AbstractSpinerAdapter.IOnItemSelectListener;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

public class SpinerPopWindow extends PopupWindow
		implements OnItemClickListener, AbstractSpinerAdapter.IOnItemSelectListener {

	private Context mContext;
	private ListView mListView;
	private AbstractSpinerAdapter<String> mAdapter;
	private IOnItemSelectListener mItemSelectListener;

	public SpinerPopWindow(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public void setItemListener(IOnItemSelectListener listener) {
		mItemSelectListener = listener;
	}

	private void init() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.spinner_windows, null);
		setContentView(view);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);

		mListView = (ListView) view.findViewById(R.id.listview);

		mAdapter = new AbstractSpinerAdapter<String>(mContext);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	public void refreshData(List<String> list, int selIndex) {
		if (list != null && selIndex != -1) {
			mAdapter.refreshData(list, selIndex);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		dismiss();
		if (mItemSelectListener != null) {
			mItemSelectListener.onItemClick(pos);
		}
	}

	@Override
	public void onItemClick(int pos) {
		// TODO Auto-generated method stub

	}

}
