package com.ytrain.wxns.activity;

import java.lang.reflect.Field;

import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ytrain.wxns.R;
import com.ytrain.wxns.utils.Wvc;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.ZoomButtonsController;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;

@SuppressLint("SetJavaScriptEnabled")
public class Article extends Activity {

	private WebView webView;
	final String mimeType = "text/html";
	final String encoding = "UTF-8";
	private WebSettings ws = null;
	private TextView tvTitle,tvBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article);
		ExitApp.getInstance().addActivity(this);
		Constants.SetTitle(this);
		initView();
		initListener();

	}

	private void initView() {
		tvBack = (TextView) findViewById(R.id.tvBack);
		webView = (WebView) findViewById(R.id.webView1);
		ws = webView.getSettings();
		ws.setDefaultTextEncodingName(encoding);
		ws.setJavaScriptEnabled(true);
		// 自适应手机屏幕
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// 设置缩放控件
		ws.setBuiltInZoomControls(true);
		// 隐藏控件
		setZoomControlGone(webView);
		Bundle bundle = this.getIntent().getExtras();
		String name = bundle.getString("name");
		String content = bundle.getString("content");
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(name);
		webView.loadDataWithBaseURL(null, content, mimeType, encoding, null);
		webView.setWebViewClient(new Wvc(this));
		webView.setBackgroundColor(Color.rgb(240, 240, 240));

	}

	// 实现放大缩小控件隐藏
	@SuppressWarnings("rawtypes")
	public void setZoomControlGone(View view) {
		Class classType;
		Field field;
		try {
			classType = WebView.class;
			field = classType.getDeclaredField("mZoomButtonsController");
			field.setAccessible(true);
			ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
					view);
			mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
			try {
				field.set(view, mZoomButtonsController);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	private void initListener() {
		tvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Article.this.finish();
			}
		});

	}

}
