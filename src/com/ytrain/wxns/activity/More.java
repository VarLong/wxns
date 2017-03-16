package com.ytrain.wxns.activity;

import com.google.gson.Gson;
import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ssy.utils.FileService;
import com.ssy.utils.UserSharedPreferences;
import com.ytrain.wxns.R;
import com.ytrain.wxns.entity.ProtalMessage;
import com.ytrain.wxns.entity.ProtalMsg;
import com.ytrain.wxns.http.AsyncHttpCilentHandler;
import com.ytrain.wxns.http.HttpUtil;
import com.ytrain.wxns.http.RequestParams;
import com.ytrain.wxns.utils.ShowLoading;
import com.ytrain.wxns.views.ExitLine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class More extends Activity implements OnClickListener {
	private RelativeLayout set_app_name, set_exitnet, tv_wap, set_gudie, set_suggestion, set_set, set_exitapp;
	private TextView text_username, text_message, tvBack, tvTitle;
	private final int EXITOK = 0X01, EXITFAIL = 0X02;
	
	Dialog dialog;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			switch (msg.what) {
			case EXITOK:
				Log.e("EXITOK", "EXITOK");
				showTip("您已经离线成功！");
				break;
			case EXITFAIL:
				showTip("下线失败！");
				Log.e("EXITFAIL", "EXITFAIL");
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
		setContentView(R.layout.more_activity);
		
		ExitApp.getInstance().addActivity(this);
		Constants.SetTitle(this);
		initView();
	}

	private void initView() {
		set_app_name = (RelativeLayout) findViewById(R.id.set_app_name);
		set_exitnet = (RelativeLayout) findViewById(R.id.set_exitnet);
		tv_wap = (RelativeLayout) findViewById(R.id.tv_wap);
		set_gudie = (RelativeLayout) findViewById(R.id.set_gudie);
		set_suggestion = (RelativeLayout) findViewById(R.id.set_suggestion);
		set_set = (RelativeLayout) findViewById(R.id.set_set);
		set_exitapp = (RelativeLayout) findViewById(R.id.set_exitapp);
		text_username = (TextView) findViewById(R.id.text_username);
		text_message = (TextView) findViewById(R.id.text_message);
		tvBack = (TextView) findViewById(R.id.tvBack);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("设置");
		text_username.setText(UserSharedPreferences.getParam(this, "phoneNumber", "").toString());
		set_app_name.setOnClickListener(this);
		set_exitnet.setOnClickListener(this);
		tv_wap.setOnClickListener(this);
		set_gudie.setOnClickListener(this);
		set_suggestion.setOnClickListener(this);
		set_set.setOnClickListener(this);
		set_exitapp.setOnClickListener(this);
		tvBack.setOnClickListener(this);

	}

	private void showTip(String string) {
		Intent intent = new Intent(More.this, ExitLine.class);
		intent.putExtra("message", string);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tvBack:
			More.this.finish();
			break;
		case R.id.set_app_name:

			break;
		case R.id.set_exitnet:
			
			exitWifi();
			break;
		case R.id.tv_wap:
			intent = new Intent(More.this, Wap.class);
			startActivity(intent);
			break;
		case R.id.set_gudie:
			intent = new Intent(More.this, ProtalSuggestion.class);
			intent.putExtra("tag", "WIFI使用指南");
			intent.putExtra("flag", 3);
			startActivity(intent);
			break;
		case R.id.set_suggestion:
			intent = new Intent(More.this, SuggestionActivity.class);
			startActivity(intent);
			break;
		case R.id.set_set:
			intent = new Intent(More.this, Set.class);
			startActivity(intent);
			break;
		case R.id.set_exitapp:
			showExitAlert();
			break;

		default:
			break;
		}

	}

	private void exitWifi() {
		String SSID = Constants.getWiFiSSID(this);
		if (SSID.contains("SZ-WLAN(free)")) {
			if (Constants.isProtalSuccess()) {
				ShowLoading sl = new ShowLoading(More.this);
				dialog = sl.loading();
				dialog.show();
				String temp_name = UserSharedPreferences.getParam(this, "RedirectURL", "").toString();
				if (temp_name.contains("8083")) {
					// 电信接口
					RequestParams params;
					params = new RequestParams();
					params.put("mobile", UserSharedPreferences.getParam(this, "phoneNumber", "").toString());			
					//密码不可以没有，需要传一个空格
					params.put("password", " ");
					params.put("userip", Constants.getWiFiIP(this));
					HttpUtil.postFullUrl(Constants.PORTEXIT, params,
							new AsyncHttpCilentHandler<ProtalMsg>(More.this, ProtalMsg.class) {
								@Override
								public void onFailure(Throwable paramThrowable, String paramString) {
									Log.e(paramString, paramString);
									handler.sendEmptyMessage(EXITFAIL);
								}

								@Override
								public void onSuccess(String paramString) {
									ProtalMsg info = null;
									Gson gson = new Gson();
									info = gson.fromJson(paramString, ProtalMsg.class);
									if (info.getResultCode() == 0) {
										handler.sendEmptyMessage(EXITOK);

										// 成功
									} else {
										handler.sendEmptyMessage(EXITFAIL);
									}
									Log.e(paramString, paramString);
								}

								@Override
								public void process(ProtalMsg paramT) {

								}
							}, More.this);

				} else if (temp_name.contains("8084")) {
					// 移动接口
					String getURL = "?ip=" + Constants.getWiFiIP(this) + "&basip="
							+ UserSharedPreferences.getParam(this, "BasIp", "").toString();
					HttpUtil.getFullUrl(Constants.PORTEXITBase + getURL, null,
							new AsyncHttpCilentHandler<ProtalMessage>(More.this, ProtalMessage.class) {

								@Override
								public void onFailure(Throwable paramThrowable, String paramString) {
									handler.sendEmptyMessage(EXITFAIL);
									Log.e("onFailure", "onFailure");
								}

								@Override
								public void onSuccess(String paramString) {
									ProtalMessage info = null;
									Gson gson = new Gson();
									info = gson.fromJson(paramString, ProtalMessage.class);
									if (info.getRet() == 0) {
										handler.sendEmptyMessage(EXITOK);
										// 成功
									} else {
										handler.sendEmptyMessage(EXITFAIL);
									}
									Log.e(paramString, paramString);
								}
								@Override
								public void process(ProtalMessage paramT) {

								}
							}, More.this);

				}
			} else {
				Toast.makeText(this, "请进行认证！", Toast.LENGTH_SHORT).show();
			}
		} else {

		}

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
