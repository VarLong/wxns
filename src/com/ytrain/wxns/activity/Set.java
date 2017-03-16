package com.ytrain.wxns.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ssy.utils.FileService;
import com.ssy.utils.UpdateManager;
import com.ytrain.wxns.R;
import com.ytrain.wxns.utils.ApplicationHelper;
import com.ytrain.wxns.utils.ConnectorService;
import com.ytrain.wxns.views.OfflineDownload;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Set extends Activity {
	private RelativeLayout rlAppDesc, rlShare, rlOffDown, rlCache, rlAbout;
	private TextView tvVersion, tvCheckVersion, tvBack,tvTitle;
	String localVer = "", serverVer = "", url = "", apkUrl = "";;
	private ApplicationHelper ah = null;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x100:
				updateVersion();
				break;
			case 0x200:
				Constants.displayToast(Set.this, "清除完成!");
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);
		ExitApp.getInstance().addActivity(this);
		ah = (ApplicationHelper) getApplicationContext();
		Constants.SetTitle(this);
		initView();
		initListener();
	}

	private void initView() {
		tvBack = (TextView) findViewById(R.id.tvBack);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("设置");
		rlAppDesc = (RelativeLayout) findViewById(R.id.set_introduce);
		rlShare = (RelativeLayout) findViewById(R.id.set_share);
		rlOffDown = (RelativeLayout) findViewById(R.id.set_offdown);
		rlCache = (RelativeLayout) findViewById(R.id.set_cache);
		rlAbout = (RelativeLayout) findViewById(R.id.set_about);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvCheckVersion = (TextView) findViewById(R.id.tv_check_version);
		localVer = Constants.getVersionName(Set.this);
		tvVersion.setText("客户端版本 V " + localVer);
	}

	private void initListener() {
		rlAppDesc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Set.this, Guide.class));
			}
		});
		tvCheckVersion.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new MyRunnable()).start();
			}
		});
		rlShare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				url = ah.getApkUrl();
				Intent it = new Intent(Intent.ACTION_VIEW);
				it.putExtra("sms_body", url);
				it.setType("vnd.android-dir/mms-sms");
				startActivity(it);
			}
		});
		rlOffDown.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// 有网络，并且是wifi的情况下
				if ((Constants.isExistNetwork(Set.this)) && (Constants.getNetworkType(Set.this) == 1)) {
					Intent intent = new Intent(Set.this, OfflineDownload.class);
					Set.this.startActivity(intent);
				} else {
					Constants.displayToast(Set.this, "当前不是WiFi网络，不能下载!");
				}
			}
		});
		rlCache.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DeleteCache();

			}
		});
		rlAbout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Set.this, SetAbout.class);
				startActivity(intent);

			}
		});
		tvBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Set.this.finish();

			}
		});

	}

	class MyRunnable implements Runnable {
		@Override
		public void run() {
			Message msg = new Message();
			msg.what = 0x100;
			mHandler.sendMessage(msg);
		}
	}

	void updateVersion() {
		try {
			// 获取最新版本,并下载
			if (Constants.isExistNetwork()) {
				localVer = Constants.getVersionName(this);
				UpdateManager mUpdateManager = new UpdateManager(Set.this);
				String text = ConnectorService.getInstance().getVersion();
				serverVer = localVer;

				if (!text.equals("")) {
					JSONObject jsonObject = JSON.parseObject(text);
					apkUrl = jsonObject.getString("url");
					serverVer = jsonObject.getString("version");
					ah.setApkUrl(apkUrl);
					ah.setVersion(serverVer);
				}
				if (null != apkUrl) {
					if (!localVer.equals(serverVer)) {
						mUpdateManager.checkUpdateInfo(apkUrl);
					} else {
						Constants.displayToast(Set.this, "当前已是最新版本！");
					}
				}

			} else {
				Constants.displayToast(Set.this, "当前无网络！");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void DeleteCache() {

		int width = getStatusWidth();
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		// 在外面点击是否让对话框消失
		dlg.setCanceledOnTouchOutside(false);
		Window window = dlg.getWindow();
		// 动态设置提示框宽度
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = width;
		window.setAttributes(lp);

		// 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.dialog_cache);

		// 为确认按钮添加事件,执行退出应用操作
		TextView title = (TextView) window.findViewById(R.id.dialog_title);
		TextView con = (TextView) window.findViewById(R.id.dialog_conent);
		Button cancle = (Button) window.findViewById(R.id.cache_cancle);
		Button ok = (Button) window.findViewById(R.id.cache_ok);
		title.setText("温馨提示");
		con.setText("缓存文件可以用来帮您节省流量，但较大时会占用较多磁盘空间。\n确定开始清理吗？");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String path = Constants.DIR_PATH;
				try {
					FileService.delAllFile(path);

					mHandler.sendEmptyMessage(0x200);
				} catch (Exception e) {
					e.printStackTrace();
				}
				dlg.cancel();
			}
		});
		cancle.setOnClickListener(new View.OnClickListener() {
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
