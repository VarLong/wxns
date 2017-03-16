package com.ytrain.wxns.activity;

import com.ssy.utils.Constants;
import com.ytrain.wxns.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PortalAuth extends Activity {
	/**
	 * 网址导航页面
	 */
	WebView webView;
	private RelativeLayout rlHome;
	TextView tvTitle, tvBack;
	final String encoding = "UTF-8";
	Intent intent;
	String title, url;
	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.portal_auth);
		Constants.SetTitle(this);
		intent = getIntent();
		url = intent.getStringExtra("url");
		initView();
		initListener();
	}

	private void initView() {
		tvBack = (TextView) findViewById(R.id.tvBack);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(intent.getStringExtra("title"));
		webView = (WebView) findViewById(R.id.wvHome);
		rlHome = (RelativeLayout) findViewById(R.id.rlHome);
		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);
		if (Constants.isExistNetwork(this)) {
			WebSettings ws = webView.getSettings();
			ws.setDefaultTextEncodingName(encoding);
			ws.setJavaScriptEnabled(true);
			ws.setJavaScriptCanOpenWindowsAutomatically(true);
			webView.loadUrl(url);
			webView.setWebViewClient(new WebViewClient());
			webView.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					pb.setProgress(newProgress);
					if (newProgress == 100) {
						pb.setVisibility(View.GONE);
					}
				}

			});

		} else {
			webView.setVisibility(View.GONE);
			rlHome.setVisibility(View.VISIBLE);
			pb.setVisibility(View.GONE);
		}

	}

	protected void loadData() {
		if (!Constants.isExistNetwork(this)) {
			Constants.displayToast(this, "当前没有网络!");
		} else {
			webView.setVisibility(View.VISIBLE);
			pb.setVisibility(View.VISIBLE);
			rlHome.setVisibility(View.GONE);
			webView.loadUrl(url);
			webView.setWebViewClient(new WebViewClient());
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

	private void initListener() {
		tvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PortalAuth.this.finish();
			}
		});
		rlHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadData();
			}
		});

	}

}
