package com.ytrain.wxns.utils;

import com.ssy.utils.Constants;
import com.ssy.utils.FileService;
import com.ytrain.wxns.database.InitDataSqlite;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

@SuppressLint("HandlerLeak")
public class Wvc extends WebViewClient {

	Context context;
	final String encoding = "UTF-8";
	String ssid = "0000001";
	ApplicationHelper ah = null;
	FileService fs = null;
	InitDataSqlite slite = null;
	RelativeLayout rl = null;
	ImageView back = null;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

		};
	};

	public Wvc() {
	}

	public Wvc(Context context) {
		this.context = context;

	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {

		// 调用拨号程序
		if (url.startsWith("mailto:") || url.startsWith("geo:")
				|| url.startsWith("tel:")) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(intent);
			return true;
		}

		if (!Constants.isExistNetwork(context)) {
			Constants.displayToast((Activity) context,
					"shouldOverrideUrlLoading当前没有网络!");
			return false;
		} else {
			view.loadUrl(url);
		}
		return super.shouldOverrideUrlLoading(view, url);

	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		if (!Constants.isExistNetwork(context)) {
			Constants.displayToast((Activity) context, "当前没有网络!");
		} else {

		}
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		if (!Constants.isExistNetwork(context)) {
			Constants.displayToast((Activity) context, "当前没有网络!");
		} else {
			ah = (ApplicationHelper) context.getApplicationContext();
			if (ah != null) {
				fs = new FileService(context);
				int cNum = url.lastIndexOf("portalid=");
				Constants.SSID = ah.getSsid();
				if (cNum > -1) {
					Constants.SSID = url
							.substring(url.lastIndexOf("=") + 1, url.length());
					fs.save("ssid.data", Constants.SSID);
					ah.setSsid(Constants.SSID);
				}
			}
			super.onPageFinished(view, url);

		}
	}

}
