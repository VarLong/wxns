package com.ytrain.wxns.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ytrain.wxns.R;
import com.ytrain.wxns.adapter.HotspotGridAdapter;
import com.ytrain.wxns.database.InitDataSqlite;
import com.ytrain.wxns.entity.UnitEntity;
import com.ytrain.wxns.utils.ApplicationHelper;
import com.ytrain.wxns.utils.ShowLoading;
import com.ytrain.wxns.views.InitData;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class HotsportActivity extends Activity {
	private GridView gvUnit;
	private TextView tvBack, tvTitle;
	private RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9, rb10, rb11, rb12;
	private HotspotGridAdapter gridAdapter;
	private List<Map<String, Object>> list;
	Dialog dialog;
	private int flag = 1;
	private int position = 0;
	private List<UnitEntity> lstAll = null, lstUe = null;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 100) {
				gridAdapter = new HotspotGridAdapter(HotsportActivity.this, list);
				gvUnit.setAdapter(gridAdapter);

			}
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotspot);
		ExitApp.getInstance().addActivity(this);
		Constants.SetTitle(this);
		initView();
		loadGridData(flag);
		initListener();

	}

	private void initView() {
		tvBack = (TextView) findViewById(R.id.tvBack);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("热点切换");
		rb1 = (RadioButton) findViewById(R.id.rb_1);
		rb2 = (RadioButton) findViewById(R.id.rb_2);
		rb3 = (RadioButton) findViewById(R.id.rb_3);
		rb4 = (RadioButton) findViewById(R.id.rb_4);
		rb5 = (RadioButton) findViewById(R.id.rb_5);
		rb6 = (RadioButton) findViewById(R.id.rb_6);
		rb7 = (RadioButton) findViewById(R.id.rb_7);
		rb8 = (RadioButton) findViewById(R.id.rb_8);
		rb9 = (RadioButton) findViewById(R.id.rb_9);
		rb10 = (RadioButton) findViewById(R.id.rb_10);
		rb11 = (RadioButton) findViewById(R.id.rb_11);
		rb12 = (RadioButton) findViewById(R.id.rb_12);
		gvUnit = (GridView) findViewById(R.id.gv_unit);
	}

	private void loadGridData(final int flag) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				list = new ArrayList<Map<String, Object>>();
				try {
					if (Constants.isExistNetwork(HotsportActivity.this)) {
						ApplicationHelper ah = (ApplicationHelper) HotsportActivity.this.getApplicationContext();
						if (ah != null) {
							InitData initData = new InitData(HotsportActivity.this, ah);
							InitDataSqlite slite = new InitDataSqlite(HotsportActivity.this);
							initData.getUnitAll(slite);
							lstAll = ah.getLstUnit();
							Map<String, Object> map = null;
							lstUe = new ArrayList<UnitEntity>();
							for (UnitEntity unitEntity : lstAll) {
								if (flag == unitEntity.getFlag()) {
									map = new HashMap<String, Object>();
									map.put("title", unitEntity.getWifiname());
									map.put("info", unitEntity.getDescn());
									map.put("img", unitEntity.getImgUrl());
									list.add(map);
									lstUe.add(unitEntity);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(100);
			}
		});
		thread.start();

	}

	private void initListener() {
		tvBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HotsportActivity.this.finish();

			}
		});
		rb1.setOnClickListener(new ClickListener());
		rb2.setOnClickListener(new ClickListener());
		rb3.setOnClickListener(new ClickListener());
		rb4.setOnClickListener(new ClickListener());
		rb5.setOnClickListener(new ClickListener());
		rb6.setOnClickListener(new ClickListener());
		rb7.setOnClickListener(new ClickListener());
		rb8.setOnClickListener(new ClickListener());
		rb9.setOnClickListener(new ClickListener());
		rb10.setOnClickListener(new ClickListener());
		rb11.setOnClickListener(new ClickListener());
		rb12.setOnClickListener(new ClickListener());

		gvUnit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ShowLoading sl = new ShowLoading(HotsportActivity.this);
				dialog = sl.loading();
				dialog.show();
				position = arg2;
				showActivitySite();
			}
		});
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rb_1:
				flag = 1;
				break;
			case R.id.rb_2:
				flag = 2;
				break;
			case R.id.rb_3:
				flag = 3;
				break;
			case R.id.rb_4:
				flag = 4;
				break;
			case R.id.rb_5:
				flag = 5;
				break;
			case R.id.rb_6:
				flag = 6;
				break;
			case R.id.rb_7:
				flag = 7;
				break;
			case R.id.rb_8:
				flag = 8;
				break;
			case R.id.rb_9:
				flag = 9;
				break;
			case R.id.rb_10:
				flag = 10;
				break;
			case R.id.rb_11:
				flag = 11;
				break;
			case R.id.rb_12:
				flag = 12;
				break;
			}
			ShowLoading sl = new ShowLoading(HotsportActivity.this);
			dialog = sl.loading();
			dialog.show();
			loadGridData(flag);

		}
	}

	/**
	 * 进入具体单位页面
	 */
	private void showActivitySite() {
		new Thread() {
			@Override
			public void run() {
				UnitEntity ue = lstUe.get(position);
				if (ue != null) {
					Intent intent = new Intent(HotsportActivity.this, Wisdom.class);
					intent.putExtra("ssid", ue.getSsid());
					intent.putExtra("tag", "wifi");
					startActivity(intent);
				}
				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}
}
