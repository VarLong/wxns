package com.ytrain.wxns.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ytrain.wxns.R;
import com.ytrain.wxns.database.InitDataSqlite;
import com.ytrain.wxns.entity.UnitEntity;
import com.ytrain.wxns.utils.ApplicationHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class Dial extends Activity {

	Button btnCall;
	ListView lvDial;
	TextView tvTitle, tvUnitDescn, tvBack;
	String[] vals;
	Bundle bundle;
	String ssid, phones;
	ApplicationHelper ah = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dial);
		ExitApp.getInstance().addActivity(this);
		Constants.SetTitle(this);
		// 设置顶部的标题
		bundle = this.getIntent().getExtras();
		ssid = bundle.getString("ssid");
		initView();
		initListener();

	}

	private void initView() {
		tvBack = (TextView) findViewById(R.id.tvBack);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvUnitDescn = (TextView) findViewById(R.id.tvUnitDescn);
		lvDial = (ListView) this.findViewById(R.id.lvDial);
		phones = "";
		// 第一步从本地中取
		InitDataSqlite slite = new InitDataSqlite(this);
		try {
			UnitEntity ue = slite.findUnitBySsid(ssid);
			tvTitle.setText(ue.getWifiname());
			tvUnitDescn.setText(ue.getDescn());
			phones = ue.getPhones();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (phones != null && phones.length() > 0) {
			vals = phones.split(",");
			List<HashMap<String, Object>> lst = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < vals.length; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("info", tvTitle.getText() + "的对外服务电话,办公时间9:00-17:30");
				map.put("title", vals[i] + "(点击图标可一键拨号)");
				map.put("img", R.drawable.phone);
				lst.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(this, lst, R.layout.dial_items,
					new String[] { "info", "title", "img" }, new int[] { R.id.info, R.id.title, R.id.img });
			lvDial.setAdapter(adapter);
		}

	}

	private void initListener() {
		tvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Dial.this.finish();
			}
		});

		lvDial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//显式调用
				Uri uri = Uri.parse("tel:" + vals[arg2]);
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(intent);
				//隐式调用，Intent隐式调用发送的意图可能被第三方劫持，如果含有隐私信息可能导致内部隐私数据泄露				
				// Intent intent = new Intent();
				// intent.setAction(Intent.ACTION_DIAL);
				// intent.setData(Uri.parse("tel:" + vals[arg2]));
				// startActivity(intent);
			}
		});

	}

}
