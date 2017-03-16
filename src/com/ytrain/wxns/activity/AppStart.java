package com.ytrain.wxns.activity;

import com.ssy.utils.Constants;
import com.ssy.utils.FileService;
import com.ytrain.wxns.R;
import com.ytrain.wxns.database.InitDataSqlite;
import com.ytrain.wxns.utils.ApplicationHelper;
import com.ytrain.wxns.views.InitData;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;

/**
 * app起始界面
 * 
 * @author Administrator
 * 
 */
public class AppStart extends Activity {
	private int TYPE = 0;
	private String SSID;
	ApplicationHelper ah = null;
	String localVer = "";
	InitData initData = null;
	InitDataSqlite slite = null;
	private FileService fs;
	private String content;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 120:
				initView();
				break;
			case 130:
				initViewWifi();
				break;
			default:
				break;
			}
		}

	};

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appstart);
		if (Build.VERSION.SDK_INT > 11) {
			// 设置严苛的线程策略，不过在Android
			// 2.3之前的版本上该模式不工作2.3版本该模式默认工作。严苛的线程是指不能在主线程中网络访问和磁盘操作。
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());

		}
		ah = (ApplicationHelper) getApplicationContext();
		initData = new InitData(AppStart.this, ah);
		slite = new InitDataSqlite(AppStart.this);
		fs = new FileService(this);
		content = fs.readFile("version.data");
		localVer = Constants.getVersionName(this);
		TYPE = Constants.getNetworkType(this);
		initData();
		
		switch (TYPE) {
		case 0:
			handler.sendEmptyMessageDelayed(120, 2000);
			break;
		case 1:
			SSID = Constants.getWiFiSSID(this);
			if (SSID.contains("SZ-WLAN(free)")) {
				// 如果是SZ-WLAN(free)热点，进入到认证界面，如果不是进入到主界面
				if (Constants.isProtalSuccess()) {
					handler.sendEmptyMessageDelayed(120, 2000);

				} else {
					handler.sendEmptyMessageDelayed(130, 2000);
				}

			} else {
				handler.sendEmptyMessageDelayed(120, 2000);
			}
			break;
		case 2:
			handler.sendEmptyMessageDelayed(120, 2000);
			break;
		default:
			break;
		}
	}

	private void initData() {
		new Thread(new MyRunnable()).start();
	}

	private class MyRunnable implements Runnable {
		@Override
		public void run() {
			if (!content.equals("") && !content.equals("0")) {
				// 不是第一次启动
				ah.setFisrtLoadData(false);
			} else {
				// 第一次启动
				fs.save("version.data", localVer);
				if (!Constants.isExistNetwork()) {
					ah.setFisrtLoadData(true);
					initData = new InitData(AppStart.this, ah);
					initData.addFirstData(slite);
				}
			}
			try {
				if (Constants.isExistNetwork()) {
					// 每天只做一次更新热点单位的操作
					initData.updateData();
					initData.getUnitAll(slite);
					initData.getPartByZhzw(slite);
					initData.getPartBySsid(slite);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void initView() {
		// 需要该的，判断是不是第一次登录
		if (!content.equals("") && !content.equals("0")) {// 如果不是第一次使用，跳转到主页面
			Intent intent = new Intent(AppStart.this, Wisdom.class);
			startActivity(intent);
			AppStart.this.finish();
		} else {// 如果是第一次使用，跳转到向导页
			Intent intent = new Intent(AppStart.this, Guide.class);
			startActivity(intent);
			AppStart.this.finish();
		}
	};

	private void initViewWifi() {
		// 需要该的，判断是不是第一次登录
		if (!content.equals("") && !content.equals("0")) {// 如果不是第一次使用，跳转到主页面
			Intent intent = new Intent(AppStart.this, MainProtal.class);
			startActivity(intent);
			AppStart.this.finish();
		} else {// 如果是第一次使用，跳转到向导页
			Intent intent = new Intent(AppStart.this, Guide.class);
			startActivity(intent);
			AppStart.this.finish();
		}

	}
}