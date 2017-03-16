package com.ytrain.wxns.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ssy.utils.FileService;
import com.ssy.utils.UpdateManager;
import com.ytrain.wxns.R;
import com.ytrain.wxns.database.InitDataSqlite;
import com.ytrain.wxns.entity.PartEntity;
import com.ytrain.wxns.entity.UnitEntity;
import com.ytrain.wxns.utils.ApplicationHelper;
import com.ytrain.wxns.utils.ConnectorService;
import com.ytrain.wxns.views.InitData;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@SuppressLint({ "HandlerLeak", "InlinedApi" })
@SuppressWarnings("deprecation")
public class Wisdom extends FragmentActivity {

	private FileService fs = null;
	private String tag = "home", apkUrl = "";
	private ApplicationHelper ah = null;
	private String localVer = "", serverVer = "";
	private ImageView set, wifiTitle;
	private UpdateManager mUpdateManager;
	private TextView tvWifiName, tip;
	private String HotName, ssid = "";
	private List<PartEntity> lstPeZhzw, lstPeSsid;
	private GridView ssidGrd;
	private ImageView iv1, iv2, iv3, iv4;
	private int[] ssidImages = new int[] { R.drawable.app1, R.drawable.app2, R.drawable.app3, R.drawable.app4 };
	private int zposition = 0, sposition = 0;

	List<HashMap<String, Object>> lstSsid = null;

	// 更新intent传过来的值
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}

	@Override
	protected void onResume() {
		tipMessage();
		super.onResume();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0x100:
				String text = (String) msg.obj;
				updateVersion(text);

				break;
			case 110:
				tvWifiName.setText(HotName);
				SimpleAdapter sa = new SimpleAdapter(Wisdom.this, lstSsid, R.layout.home_items,
						new String[] { "ItemImage", "ItemText" }, new int[] { R.id.ItemImage, R.id.ItemText });
				ssidGrd.setAdapter(sa);
				ssidGrd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						sposition = arg2;
						showActivityBySsid();
					}
				});
				break;

			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wisdom);
		ExitApp.getInstance().addActivity(this);

		// 选择切换热点时，跳转到智慧政务页，暂时没用到
		ah = (ApplicationHelper) getApplicationContext();
		Constants.SetTitle(this);
		Bundle bundle = this.getIntent().getExtras();
		if (null != bundle) {
			tag = bundle.getString("tag");
			ssid = bundle.getString("ssid");
			if (tag != null && ssid != null) {
				fs = new FileService(this);
				fs.save("ssid.data", ssid);
				ah.setSsid(ssid);

			}
		}
		initView();
		getData();
		tipMessage();
		new Thread(new MyThread()).start();
	}

	/**
	 * 根据网络状况弹出提示框提示信息,提示网络状况
	 */
	private void tipMessage() {
		switch (Constants.getNetworkType(this)) {
		case 0:
			tip.setVisibility(View.VISIBLE);
			tip.setText("无法连接网络！点击设置！");
			tip.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Settings.ACTION_SETTINGS);
					startActivity(intent);

				}
			});

			break;
		case 1:
			if (!Constants.isProtalSuccess()) {
				tip.setVisibility(View.VISIBLE);
				tip.setText("有可连接WIFI，立即连接！");
				tip.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Wisdom.this, PortalAuth.class);
						intent.putExtra("title", "无线南山");
						intent.putExtra("url", Constants.GovernMent);
						startActivity(intent);
					}
				});
			} else {
				if (tip.getVisibility() == View.VISIBLE) {
					tip.setVisibility(View.GONE);
				}

			}

			break;
		case 2:

			if (tip.getVisibility() == View.VISIBLE) {
				tip.setVisibility(View.GONE);
			}

			break;
		default:
			break;
		}

	}

	private void initView() {

		iv1 = (ImageView) findViewById(R.id.iv_1);
		iv2 = (ImageView) findViewById(R.id.iv_2);
		iv3 = (ImageView) findViewById(R.id.iv_3);
		iv4 = (ImageView) findViewById(R.id.iv_4);
		tvWifiName = (TextView) findViewById(R.id.mainUnitName);
		tip = (TextView) findViewById(R.id.tip);
		ssidGrd = (GridView) findViewById(R.id.ssidGrd);
		wifiTitle = (ImageView) findViewById(R.id.wifiTitle);
		wifiTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Wisdom.this, HotsportActivity.class);
				startActivity(intent);
			}
		});
		set = (ImageView) findViewById(R.id.set);
		set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Wisdom.this, More.class);
				startActivity(intent);

			}
		});

	}

	class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_1:
				zposition = 0;
				break;
			case R.id.iv_2:
				zposition = 1;
				break;
			case R.id.iv_3:
				zposition = 2;
				break;
			case R.id.iv_4:
				zposition = 3;
				break;
			}
			showActivityByZhzw();
		}
	}

	/**
	 * 智慧政务跳转
	 */
	private void showActivityByZhzw() {

		Intent intent = null;
		if (zposition == 3) {
			intent = new Intent(Wisdom.this, SiteNav.class);
			intent.putExtra("title", "南山网");
			intent.putExtra("url", "http://inanshan.sznews.com/");
		} else {
			PartEntity pe = lstPeZhzw.get(zposition);
			intent = new Intent(Wisdom.this, PartList.class);
			intent.putExtra("id", pe.getId());
			intent.putExtra("name", pe.getName());
		}
		startActivity(intent);
	}

	/**
	 * 热点跳转
	 */
	private void showActivityBySsid() {

		Intent intent = null;
		if (sposition == 3) {
			intent = new Intent(Wisdom.this, Dial.class);
			intent.putExtra("ssid", ssid);
		} else {
			PartEntity pe = lstPeSsid.get(sposition);
			intent = new Intent(Wisdom.this, PartList.class);
			intent.putExtra("id", pe.getId());
			intent.putExtra("name", pe.getName());
		}
		startActivity(intent);

	}

	/**
	 * 提示更新提示框
	 * 
	 * @param text
	 */
	private void updateVersion(String text) {
		if (!text.equals("")) {
			JSONObject jsonObject = JSON.parseObject(text);
			apkUrl = jsonObject.getString("url");
			serverVer = jsonObject.getString("version");
			ah.setApkUrl(apkUrl);
			ah.setVersion(serverVer);
		}
		if (!localVer.equals(serverVer) && null != apkUrl) {
			mUpdateManager.checkUpdateInfo(apkUrl);
		}
	};

	class MyThread implements Runnable {

		@Override
		public void run() {
			try {
				initData();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 初始化数据
	 */
	protected void initData() {
		try {
			// 获取最新版本,提示并下载
			if (Constants.isExistNetwork()) {
				localVer = Constants.getVersionName(this);
				mUpdateManager = new UpdateManager(Wisdom.this);
				String text = ConnectorService.getInstance().getVersion();
				serverVer = localVer;
				Message msg = new Message();
				msg.what = 0x100;
				msg.obj = text;
				handler.sendMessage(msg);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void getData() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InitData initData = new InitData(Wisdom.this, ah);
					InitDataSqlite slite = new InitDataSqlite(Wisdom.this);
					initData.getPartByZhzw(slite);
					initData.getPartBySsid(slite);
					if (ah != null) {
						FileService fs = new FileService(Wisdom.this);
						ssid = fs.readFile("ssid.data");
						lstPeZhzw = ah.getLstZhzw();
						lstPeSsid = ah.getLstSsid();
						lstSsid = new ArrayList<HashMap<String, Object>>();
					}
					// 设置热点名称
					if (null == ssid || "".equals(ssid)) {
						ssid = Constants.SSID;
					}
					UnitEntity ue = slite.findUnitBySsid(ssid);
					if (ue != null) {
						HotName = ue.getWifiname();
					} else {
						HotName = "区政府行政服务大厅";
					}
					HashMap<String, Object> map = null;
					// 信息服务
					if (lstPeZhzw != null && lstPeZhzw.size() != 0) {

					} else {
						String text = ConnectorService.getInstance().findPartBySsid("1");
						JSONArray array = JSON.parseArray(text);
						lstPeZhzw = new ArrayList<PartEntity>();
						int size = array.size();
						PartEntity pe = null;
						for (int i = 0; i < size; i++) {
							pe = array.getObject(i, PartEntity.class);
							lstPeZhzw.add(pe);
						}
						// 保存到本地
						slite.savePart(lstPeZhzw);
						// 设置到全部变量中
						ah.setLstZhzw(lstPeZhzw);
					}
					iv1.setOnClickListener(new ClickListener());
					iv2.setOnClickListener(new ClickListener());
					iv3.setOnClickListener(new ClickListener());
					iv4.setOnClickListener(new ClickListener());
					// 热点单位
					if (lstPeSsid != null && lstPeSsid.size() != 0) {

					} else {

						ah.setLstSsid(lstPeSsid);
					}
					
					int j = 0;
					for (PartEntity pe : lstPeSsid) {
						map = new HashMap<String, Object>();
						map.put("ItemText", pe.getName());
						map.put("ItemImage", ssidImages[j]);
						lstSsid.add(map);
						j++;
					}
					handler.sendEmptyMessage(110);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		thread.start();
	}

	/**
	 * 退出操作
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果是返回键,直接返回到桌面
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
			showExitAlert();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 按一下返回键弹出的对话框
	 */
	private void showExitAlert() {
		int width = getStatusWidth();
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		dlg.setCanceledOnTouchOutside(false);
		Window window = dlg.getWindow();
		// 动态设置提示框宽度
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = width;
		window.setAttributes(lp);
		// 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.exit);
		// 为确认按钮添加事件,执行退出应用操作
		TextView ok = (TextView) window.findViewById(R.id.btn_confirm);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExitApp.getInstance().exit(); // 退出应用...
			}
		});

		// 关闭alert对话框架
		TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.cancel();
			}
		});
	}

	/**
	 * 根据屏幕大小获得弹出框的宽度。大小为十分之九屏幕宽度
	 * 
	 * @return
	 */
	public int getStatusWidth() {
		WindowManager wm = this.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		return width / 10 * 9;
	}

}
