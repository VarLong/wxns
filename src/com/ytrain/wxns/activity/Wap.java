package com.ytrain.wxns.activity;

import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ssy.utils.FileService;
import com.ytrain.wxns.R;
import com.ytrain.wxns.utils.Wvc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Wap extends Activity {
	private TextView tvBack;
	WebView webView;
	RelativeLayout rlHome;
	final String encoding = "UTF-8";
	String url = Constants.wapUrl;
	Dialog dialog;
	FileService fs;
	PopupWindow pop = null;
	ProgressBar pb;
	protected boolean IsProtal = false;
	protected boolean IsOver = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panoramic);
		ExitApp.getInstance().addActivity(this);
		Constants.SetTitle(this);
		webView = (WebView) findViewById(R.id.wvHome);
		rlHome = (RelativeLayout) findViewById(R.id.rlHome);
		tvBack = (TextView) findViewById(R.id.tvBack);
		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);
		pb.setVisibility(View.VISIBLE);

		Context context = getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();

		if (null == ni) {
			webView.setVisibility(View.GONE);
			rlHome.setVisibility(View.VISIBLE);
			pb.setVisibility(View.GONE);
		}
		loadHomeData();
		initListener();
	}

	private void loadHomeData() {
		WebSettings ws = webView.getSettings();
		ws.setDefaultTextEncodingName(encoding);
		ws.setJavaScriptEnabled(true);
		ws.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存
		ws.setJavaScriptCanOpenWindowsAutomatically(true);

		if (Constants.isExistNetwork(this)) {
			FileService fs = new FileService(this);
			String ssid = fs.readFile("ssid.data");
			if (!"".equals(ssid) && !ssid.equals(Constants.SSID)) {
				url = url + "?portalid=" + ssid;
			}
		}
		webView.loadUrl(url);
		webView.setWebViewClient(new Wvc(this));
		webView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
					long contentLength) {
				// 实现下载代码
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				pb.setProgress(newProgress);
				if (newProgress == 100) {
					// pb.setVisibility(View.GONE);

				}
			}

		});

	}

	private void initListener() {
		tvBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Wap.this.finish();
			}
		});
		rlHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadData();

			}
		});
	}

	protected void loadData() {
		if (!Constants.isExistNetwork(this)) {
			Constants.displayToast(this, "当前没有网络!");
		} else {
			webView.setVisibility(View.VISIBLE);
			pb.setVisibility(View.VISIBLE);
			rlHome.setVisibility(View.GONE);
			webView.loadUrl(url);
			webView.setWebViewClient(new Wvc(this));
			webView.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					pb.setProgress(newProgress);
					if (newProgress == 100) {
						pb.setVisibility(View.GONE);

					}
				}

			});
		}
	}
}
