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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SiteNav extends Activity {
	/**
	 * 网址导航页面
	 */
	WebView webView;
	private RelativeLayout rlHome;
	ImageView prev, next, refresh;
	TextView tvTitle, tvBack;
	final String encoding = "UTF-8";
	Intent intent;
	String title, url;
	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.site_nav);
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
			prev = (ImageView) findViewById(R.id.site_nav_prev);
			if (prev.isPressed()) {
				next.setImageResource(R.drawable.site_nav_prev_pressed);
			}
			next = (ImageView) findViewById(R.id.site_nav_next);
			if (next.isPressed()) {
				next.setImageResource(R.drawable.site_nav_next_pressed);
			}

			refresh = (ImageView) findViewById(R.id.site_nav_refresh);
			if (refresh.isPressed()) {
				refresh.setImageResource(R.drawable.site_nav_refresh_pressed);
			}

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
				SiteNav.this.finish();
			}
		});
		prev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (webView.canGoBack()) {
					webView.goBack();
				}
			}
		});
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (webView.canGoForward()) {
					webView.goForward();
				}
			}
		});
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				webView.reload();
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
