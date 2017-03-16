package com.ssy.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("DefaultLocale")
public class Constants {
	// 移动端下线接口
	public final static String PORTEXITBase = "http://120.24.79.29:8084/wxnsportal/nsydportal/ajax_LoginOut.action";
	// 电信下线接口
	public final static String PORTEXIT = "http://120.24.79.29:8083/wxnsportal/logout";
	// 发送短信接口
	public final static String PortMessage = "http://120.24.79.29:8083/wxnsportal/get_pwd";
	// 移动端认证接口
	public final static String PORTBase = "http://120.24.79.29:8084/wxnsportal/nsydportal/ajax_Login.action";
	// 电信端认证接口
	public final static String PORT = "http://120.24.79.29:8083/wxnsportal/login_verify3";
	// 意见反馈接口
	public final static String SuggestionSave = "http://120.24.79.29:8083/wxnsportal/feedback/save";
	// Prot认证界面的意见数据获取
	public final static String SOAPProt = "http://www.szwxns.com/wxns/services/wisdomService?WSDL";
	// 根据MAC物理地址判断新老用户
	public final static String MacOneKey = "http://120.24.79.29:8083/wxnsportal/updateUserPwd";

	public final static String soapAddress = "http://www.szwxns.com/wxns/services/wisdomService?wsdl";
	public final static String targetNamespace = "http://www.szwxns.com";
	public final static String wapUrl = "http://wap.szwxns.com/";
	public final static String dataUrl = "http://www.szwxns.com/wxnswisdom";
	// 南山区政府主页
	public final static String GovernMent = "http://www.szns.gov.cn";

	public final static String SDCARD = Environment.getExternalStorageDirectory().toString();

	public final static String DIR_PATH = SDCARD + "/szwxns";

	public static String SSID = "384102488@vlan";

	public final static Integer PAGE_SIZE = 20;

	/**
	 * 验证手机号是否合法，只验证前两位，号码段增加17 和14开头
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13)|(15)|(18)|(17)|(14))\\d{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 截取连续相连的四位随机数字
	 * 
	 * @param mobiles
	 * @return
	 */
	public static String getPassword(String word) {
		Pattern p = Pattern.compile("^\\d{4}$");
		Matcher m = p.matcher(word);
		String result = m.replaceAll("").trim().toString();
		return result;
	}

	/**
	 * 验证电话号码是否合法
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isTelepNumberValid(String phoneNumber) {
		boolean isValid = false;
		String expression = "^//(?(//d{3})//)?[- ]?(//d{3})[- ]?(//d{5})$";
		String expression2 = "^//(?(//d{3})//)?[- ]?(//d{4})[- ]?(//d{4})$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		Pattern pattern2 = Pattern.compile(expression2);
		Matcher matcher2 = pattern2.matcher(inputStr);
		if (matcher.matches() || matcher2.matches()) {
			isValid = true;
		}
		return isValid;

	}

	/**
	 * 解析出url参数中的参数value的值
	 * 
	 * @param URL
	 *            url地址
	 * @return url请求参数部分
	 */
	public static String getURLRequestValue(String URL, String value) {
		String[] arrSplit = null;
		String strUrlParam = TruncateUrlPage(URL);
		if (strUrlParam == null) {
			return null;
		}
		// 每个键值为一组
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");

			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				if (arrSplitEqual[0].equals(value)) {
					return arrSplitEqual[1];
				}

			}
		}
		return null;
	}

	/**
	 * 去掉url中的路径，留下请求参数部分
	 * 
	 * @param strURL
	 *            url地址
	 * @return url请求参数部分
	 */
	private static String TruncateUrlPage(String strURL) {
		String strAllParam = null;
		String[] arrSplit = null;
		strURL = strURL.trim().toLowerCase();
		arrSplit = strURL.split("[?]");
		if (strURL.length() > 1) {
			if (arrSplit.length > 1) {
				if (arrSplit[1] != null) {
					strAllParam = arrSplit[1];
				}
			}
		}

		return strAllParam;
	}

	/**
	 * 透明式样状态栏需要在xml文档属性中添加android:background="@color/app_top_bg"
	 * android:clipToPadding="true" android:fitsSystemWindows="true"
	 * 
	 * @param context
	 */
	@SuppressLint("InlinedApi")
	public static void SetTitle(Context context) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

	}

	/**
	 * 隐藏状态栏
	 * 
	 * @param view
	 */
	@SuppressLint("NewApi")
	public static void hideSystemUI(View view) {
		view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

	/**
	 * 显示状态栏
	 * 
	 * @param view
	 */
	@SuppressLint("NewApi")
	public static void showSystemUI(View view) {
		view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	/**
	 * 获状态栏高度
	 *
	 * @param context
	 *            上下文
	 * @return 通知栏高度
	 */
	public int getStatusBarHeight(Context context) {
		int statusBarHeight = 0;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object obj = clazz.newInstance();
			Field field = clazz.getField("status_bar_height");
			int temp = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 获取文件类型
	 * 
	 * @param f
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg")
				|| end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")
				|| end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}

	/**
	 * 得到网络类型
	 * 
	 * @param act
	 * @return
	 */
	public static int getNetworkType(Activity act) {
		Context context = act.getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (null != ni) {
				int type = ni.getType();
				// 手机网络
				if (ConnectivityManager.TYPE_MOBILE == type) {
					return 2;
				}
				// wifi网络
				if (ConnectivityManager.TYPE_WIFI == type) {
					return 1;
				}
			}
		}
		return 0;
	}

	/**
	 * 获取wifi名称
	 * 
	 * @param con
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getWiFiSSID(Context con) {
		Context context = con.getApplicationContext();
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wm.getConnectionInfo();
		return wifiInfo.getSSID();

	}

	/**
	 * 获取用户ip
	 * 
	 * @param con
	 * @return
	 */
	public static String getWiFiIP(Context con) {
		Context context = con.getApplicationContext();
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wm.getConnectionInfo();
		int i = wifiInfo.getIpAddress();
		String ip = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
		return ip;
	}

	/**
	 * 获取MAC地址
	 * 
	 * @param con
	 * @return
	 */
	public static String getWiFiMAC(Context con) {
		Context context = con.getApplicationContext();
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wm.getConnectionInfo();
		return wifiInfo.getMacAddress();
	}

	/**
	 * 生成动态码
	 * 
	 * @return
	 */
	public static String createDynamicCode() {
		StringBuffer sb = new StringBuffer();
		int r = 0;
		for (int i = 0; i < 4; i++) {
			r = (int) (Math.random() * (10));
			sb.append(r);
		}
		return sb.toString();
	}

	/**
	 * 判断是否连接到网络
	 * 
	 * @param con
	 * @return
	 */
	public static boolean isExistNetwork(Context con) {
		Context context = con.getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (null != ni) {
			return ni.isAvailable();
		}
		return false;
	}

	/**
	 * 判断网络是否真的可用
	 * 
	 * @return
	 */
	public static boolean isExistNetwork() {
		boolean isConnect = false;
		try {
			TestNetwork test = new TestNetwork();
			test.start();
			boolean isConnection = true;
			while (isConnection) {
				isConnection = test.flag;
				isConnect = test.getConnectionState();
			}

		} catch (Exception e) {
			e.printStackTrace();
			isConnect = false;
		}
		return isConnect;
	}

	/**
	 * 在认证wifi下判断是否成功
	 */
	public static boolean isProtalSuccess() {
		HttpURLConnection urlConnection = null;
		// mUrl实际访问的地址是：
		String mUrl = "http://www.baidu.com";
		/*
		 * Captive Portal的测试非常简单，就是向mUrl发送一个HTTP GET请求。如果无线网络提供商没有设置Portal
		 * Check，则HTTP GET请求将返回204。204表示请求chǔ理成功，但没有数据返回。如果无线网络提供商设置了 Portal
		 * Check，则它一定会重定向到某个特定网页。这样，HTTP GET的返回值就不是204
		 */
		URL url = null;
		try {
			url = new URL(mUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setInstanceFollowRedirects(false);
			urlConnection.setConnectTimeout(3000);
			urlConnection.setReadTimeout(3000);
			urlConnection.setUseCaches(false);
			urlConnection.getInputStream();

			if (urlConnection.getResponseCode() == 200) {
				// 如果返回码为200，能正常上网，返回码为302protal为未认证的网络
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 获取apk配置文件里面的版本号
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getVersionName(Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "szwxns1.0.0";
	}

	@SuppressLint("ShowToast")
	public static void displayToast(Activity act, String msg) {
		Toast.makeText(act, msg, 600).show();
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	/**
	 * 内容摘要：判断属性是否为null 流程说明：
	 * 
	 * 
	 * 1、 如果Object对象为空，返回true . 2、 如果不为空 . 3、 字类可以调用该方法判断一个对象
	 * 
	 * @param object
	 *            操作对象
	 * @return 布尔值（true OR false）
	 */
	public static boolean isNullOfProperty(Object object) {
		try {
			if (object == null) {
				return true;
			}
			if (object instanceof String) {
				String target = (String) object;
				if (target.trim().length() == 0)
					return true;
			}
		} catch (Exception ex) {
			return true;
		}
		return false;
	}

	public static Object nullOfProperty(Object object) {
		if (isNullOfProperty(object)) {
			if (object instanceof String) {
				String target = (String) object;
				if (target.trim().length() == 0)
					return "";
			} else if (object instanceof Double) {
				return 0.0;
			} else if (object instanceof Integer) {
				return 0;
			} else {
				return "0";
			}
		}

		return object;
	}

	/**
	 * 密度转换像素
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 像素转换密度
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
