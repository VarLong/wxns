package com.ytrain.wxns.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ssy.utils.FileService;
import com.ssy.utils.UserSharedPreferences;
import com.ytrain.wxns.R;
import com.ytrain.wxns.entity.MacMessage;
import com.ytrain.wxns.entity.MessageSend;
import com.ytrain.wxns.entity.ProtalMessage;
import com.ytrain.wxns.entity.ProtalMsg;
import com.ytrain.wxns.http.AsyncHttpCilentHandler;
import com.ytrain.wxns.http.HttpUtil;
import com.ytrain.wxns.http.RequestParams;
import com.ytrain.wxns.utils.ShowLoading;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainProtal extends Activity implements OnClickListener {
	private ImageView homeicon;
	private WebView webView;
	private IntentFilter filter;
	private String patternCoder = "(?<!\\d)\\d{4}(?!\\d)";
	private LinearLayout linearLayout1, linearLayout2, linearLayout3;
	private Dialog dialog;
	/**
	 * 第一次登录的界面
	 */
	private LinearLayout linearlayout01;
	private CheckBox checkBox01;
	private EditText phoneUser;
	private EditText passWord;
	private Button getPassword;
	private Button loginone;
	private TextView usertext01;
	/**
	 * 第二次登录的界面
	 */
	private LinearLayout linearlayout02;
	private TextView usertext02;
	private CheckBox checkBox02;
	private Button logintwo;
	String phonenumber, password;
	String Ip, RedirectURL = "RedirectURL", nasPortId;

	/**
	 * 用来判断重定向是否完毕
	 */
	// protected boolean IsOver = false;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x02:
				initOnceView();
				break;
			case 0x03:
				initTweiceVIew();
				break;
			default:
				break;
			}
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
		}

	};

	private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				byte[] pdu = (byte[]) obj;
				SmsMessage sms = SmsMessage.createFromPdu(pdu);
				String message = sms.getMessageBody();
				
				// 短息的手机号。。+86开头？
				String from = sms.getOriginatingAddress();
				
				if (!TextUtils.isEmpty(from)) {
					String code = patternCode(message);
					if (!TextUtils.isEmpty(code)) {
						passWord.setText(code);
					}
				}
			}
		}
	};

	/**
	 * 获取短信中的验证码
	 * 
	 * @param patternContent
	 * @return
	 */
	private String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile(patternCoder);
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(smsReceiver);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_login);
		ExitApp.getInstance().addActivity(this);
		Constants.SetTitle(this);
		Ip = Constants.getWiFiIP(this);		
		filter = new IntentFilter();
		// 注册广播，设置短信拦截参数
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(smsReceiver, filter);
		filter.setPriority(Integer.MAX_VALUE);
		initView();
		showDialog();
		getData();
		loadURLData();
	}

	/**
	 * 用一个webview获取重定向地址
	 */

	private void loadURLData() {
		webView = (WebView) findViewById(R.id.webView1);
		WebSettings ws = webView.getSettings();
		ws.setDefaultTextEncodingName("UTF-8");
		ws.setJavaScriptEnabled(true);
		ws.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.loadUrl("http://www.baidu.com");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
				
				super.onLoadResource(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				RedirectURL = url;
				
				handler.sendEmptyMessage(0x10);
				super.onPageFinished(view, url);
			}
		});
	}

	private void initView() {
		linearlayout01 = (LinearLayout) findViewById(R.id.LinearLayout01);
		linearlayout02 = (LinearLayout) findViewById(R.id.onekeylogin);
		homeicon = (ImageView) findViewById(R.id.home_icon);
		linearLayout1 = (LinearLayout) findViewById(R.id.lineralayout001);
		linearLayout2 = (LinearLayout) findViewById(R.id.lineralayout002);
		linearLayout3 = (LinearLayout) findViewById(R.id.lineralayout003);
		usertext02 = (TextView) findViewById(R.id.usertext02);
		checkBox02 = (CheckBox) findViewById(R.id.chexkbos02);
		logintwo = (Button) findViewById(R.id.logintwo);
		checkBox01 = (CheckBox) findViewById(R.id.chexkbos);
		phoneUser = (EditText) findViewById(R.id.phpneNumber);
		passWord = (EditText) findViewById(R.id.passWord);
		getPassword = (Button) findViewById(R.id.getPassword);
		loginone = (Button) findViewById(R.id.loginone);
		usertext01 = (TextView) findViewById(R.id.usertext01);
		getPassword.setOnClickListener(this);
		usertext01.setOnClickListener(this);
	
		
		
		linearLayout1.setOnClickListener(this);
		linearLayout2.setOnClickListener(this);
		linearLayout3.setOnClickListener(this);
		homeicon.setOnClickListener(this);
		usertext02.setOnClickListener(this);
		loginone.setOnClickListener(MainProtal.this);
		logintwo.setOnClickListener(MainProtal.this);

	}

	private void initOnceView() {
		linearlayout01.setVisibility(View.VISIBLE);
		linearlayout02.setVisibility(View.GONE);

	}

	private void initTweiceVIew() {
		linearlayout01.setVisibility(View.GONE);
		linearlayout02.setVisibility(View.VISIBLE);

	}

	/**
	 * 根据存储的手机号判断否登录过，以便加载不同的登录界面，如果没有就默认第一次登录。
	 */
	private void getData() {
		String mobileNumber = UserSharedPreferences.getParam(this,
				"phoneNumber", "").toString();
		if (mobileNumber != null) {
			// 如果帐号被删除，则当成新用户处理。
			RequestParams params;
			params = new RequestParams();
			params.put("mobile", mobileNumber);

			HttpUtil.postFullUrl(Constants.MacOneKey, params,
					new AsyncHttpCilentHandler<MacMessage>(MainProtal.this,
							MacMessage.class) {

						@Override
						public void onFailure(Throwable paramThrowable,
								String paramString) {
							
							
						}

						@Override
						public void onSuccess(String paramString) {
							MacMessage info = null;
							Gson gson = new Gson();
							info = gson.fromJson(paramString, MacMessage.class);
						
							if (info.getCode() == 1) {
								// 如果是老用户
								phonenumber = info.getMobile();
								password = info.getPassword();
								handler.sendEmptyMessage(0x03);
							} else {
								// 如果是新用户
								handler.sendEmptyMessage(0x02);
							}
						}
						@Override
						public void process(MacMessage paramT) {

						}
					}, MainProtal.this);
		} else {
			handler.sendEmptyMessage(0x02);
		}

	}

	/**
	 * 获取短信
	 */
	private void getPassWord(String username, String pswd) {
		HttpUtil.getFullUrl(Constants.PortMessage + "?pwd" + pswd + "&mobile="
				+ username, null, new AsyncHttpCilentHandler<MessageSend>(
				MainProtal.this, MessageSend.class) {
			@Override
			public void onFailure(Throwable paramThrowable, String paramString) {
				
			}

			@Override
			public void onSuccess(String paramString) {
				MessageSend info = null;
				Gson gson = new Gson();
				info = gson.fromJson(paramString, MessageSend.class);
				
			}

			@Override
			public void process(MessageSend paramT) {

			}
		}, MainProtal.this);
	}

	/**
	 * 登录
	 */
	private void login(String usr, String pwd, String ip) {

		// 调用移动acip端接口,根据地址是否含有参数，判断重定向地址是否加载完成&wlanacip=120.196.170.154
		if (RedirectURL.contains("120.24.79.29")) {
			if (RedirectURL.contains("8084")) {
				String getURL = "?usr=" + usr + "&pwd=" + pwd + "&ip=" + ip;
				final String BasIp = Constants.getURLRequestValue(RedirectURL,
						"wlanacip");
				HttpUtil.getFullUrl(Constants.PORTBase + getURL + "&basip="
						+ BasIp, null,
						new AsyncHttpCilentHandler<ProtalMessage>(
								MainProtal.this, ProtalMessage.class) {

							@Override
							public void onFailure(Throwable paramThrowable,
									String paramString) {
								handler.sendEmptyMessage(0x84);
								showTip("信息获取失败！");
//								super.onFailure(paramThrowable, paramString);
							}

							@Override
							public void onSuccess(String paramString) {
								ProtalMessage info = null;
								Gson gson = new Gson();
								info = gson.fromJson(paramString,
										ProtalMessage.class);
								
								if (info.getRet() == 0) {
									UserSharedPreferences.setParam(MainProtal.this, "RedirectURL",
											RedirectURL);
									UserSharedPreferences.setParam(MainProtal.this, "phoneNumber",
											phonenumber);
									UserSharedPreferences.setParam(MainProtal.this, "BasIp",
											BasIp);
									
									Intent intent = new Intent(MainProtal.this,
											Wisdom.class);
									MainProtal.this.startActivity(intent);
									finish();
									handler.sendEmptyMessage(0x84);
									// 成功
								} else {
									handler.sendEmptyMessage(0x84);
									showTip("认证失败！");
								}
							}

							@Override
							public void process(ProtalMessage paramT) {
								
							}
						}, MainProtal.this);

			} else if (RedirectURL.contains("8083")) {
				// 调用电信baseip接口http://120.24.79.29:8083/wxnsportal/?nasIp=119.145.43.10&nasPortId=SZ-HMG-BAS-7-06000384102325@vlan
				nasPortId = Constants.getURLRequestValue(RedirectURL,
						"nasportid");
				nasPortId = nasPortId.substring(nasPortId.length() - 14);
				FileService fs = new FileService(this);
				fs.save("ssid.data", nasPortId);
				RequestParams params;
				params = new RequestParams();
				params.put("mobile", usr);
				params.put("password", pwd);
				params.put("userip", ip);
				params.put("portalid", nasPortId);
				HttpUtil.postFullUrl(Constants.PORT, params,
						new AsyncHttpCilentHandler<ProtalMsg>(MainProtal.this,
								ProtalMsg.class) {
							@Override
							public void onFailure(Throwable paramThrowable,
									String paramString) {
								showTip("信息获取失败！");
								handler.sendEmptyMessage(0x84);
//								super.onFailure(paramThrowable, paramString);
							}

							@Override
							public void onSuccess(String paramString) {
								ProtalMsg info = null;
								Gson gson = new Gson();
								info = gson.fromJson(paramString,
										ProtalMsg.class);
								
								if (info.getResultCode() == 0) {
									UserSharedPreferences.setParam(MainProtal.this, "RedirectURL",
											RedirectURL);

									UserSharedPreferences.setParam(MainProtal.this, "phoneNumber",
											phonenumber);
									UserSharedPreferences.setParam(MainProtal.this, "passWord",
											password);
									// 成功
									Intent intent = new Intent(MainProtal.this,
											Wisdom.class);
									intent.putExtra("ssid", nasPortId);
									MainProtal.this.startActivity(intent);
									finish();
									handler.sendEmptyMessage(0x84);
								} else {
									handler.sendEmptyMessage(0x84);
									showTip("认证失败！");
								}
							}

							@Override
							public void process(ProtalMsg paramT) {
								
							}
						}, MainProtal.this);

			} else {
				Toast.makeText(this, "连接WIFI错误！", Toast.LENGTH_SHORT).show();
			}

		} else {
			
			showDialog();
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.lineralayout001:
			startSugActivity("常见问题", 2);
			break;
		case R.id.lineralayout002:
			startSugActivity("使用说明", 3);
			break;
		case R.id.lineralayout003:
			intent = new Intent(MainProtal.this, SuggestionActivity.class);
			startActivity(intent);
			break;
		case R.id.home_icon:
			intent = new Intent(MainProtal.this, Wisdom.class);
			startActivity(intent);
			MainProtal.this.finish();
			break;
		case R.id.loginone:
			phonenumber = phoneUser.getText().toString();
			password = passWord.getText().toString();
			if (checkBox01.isChecked()) {
				if (phonenumber.length() != 0 && password.length() != 0) {

					login(phonenumber, password, Ip);
				} else {
					showTip("请输入帐号或密码！");
				}

			} else {
				showTip("请勾选用户协议！");
			}
			break;
		case R.id.usertext01:
			startSugActivity("用户协议", 1);

			break;
		case R.id.usertext02:
			startSugActivity("用户协议", 1);
			break;
		case R.id.logintwo:
			if (checkBox02.isChecked()) {
				if (phonenumber.length() != 0 && password.length() != 0) {

					
					
					login(phonenumber, password, Ip);
				} else {
					showTip("请输入帐号或密码！");
				}
			} else {
				showTip("请勾选用户协议！");
			}
			break;
		case R.id.getPassword:
			if (Constants.isMobileNO(phoneUser.getText().toString())) {
				TimerCount timer = new TimerCount(60000, 1000, getPassword);
				timer.start();
				getPassWord(phoneUser.getText().toString(),
						Constants.createDynamicCode());
			} else {
				showTip("手机号格式错误！");
			}

			break;
		default:
			break;
		}

	}

	private void startSugActivity(String tag, int flag) {
		Intent intent = new Intent(MainProtal.this, ProtalSuggestion.class);
		intent.putExtra("tag", tag);
		intent.putExtra("flag", flag);
		startActivity(intent);
	}

	/**
	 * 用于显示网络状态的黄色圆角提示框，继承自Toast
	 * 
	 * @param string
	 */
	private void showTip(String string) {

		// 获取LayoutInflater对象，该对象可以将布局文件转换成与之一致的view对象
		LayoutInflater inflater = getLayoutInflater();
		// 将布局文件转换成相应的View对象
		View layout = inflater.inflate(R.layout.custome_toast_layout,
				(ViewGroup) findViewById(R.id.toast_layout_root));

		// 从layout中按照id查找TextView对象
		TextView textView = (TextView) layout.findViewById(R.id.tip);
		// 设置TextView的text内容

		textView.setText(string);
		// 实例化一个Toast对象
		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
		toast.setView(layout);
		toast.show();

	}

	private void showDialog() {
		ShowLoading sl = new ShowLoading(MainProtal.this);
		dialog = sl.loading();
		dialog.show();
	}

	/**
	 * 退出操作
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果是返回键,直接返回到桌面
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 用于倒计时的button计时器。
	 * @author HelloKetty
	 *
	 */
	public class TimerCount extends CountDownTimer {
		/**
		 * 用于倒计时的button，点击一次进入倒计时。
		 */
		private Button bnt;

		public TimerCount(long millisInFuture, long countDownInterval, Button bnt) {
			super(millisInFuture, countDownInterval);
			this.bnt = bnt;
		}

		public TimerCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			bnt.setClickable(true);
			bnt.setText("获取密码\nGet Password");
		}

		@Override
		public void onTick(long arg0) {
			// TODO Auto-generated method stub
			bnt.setClickable(false);
			bnt.setText( "获取密码\n"+arg0 / 1000+"秒" );
		}
	}
}
