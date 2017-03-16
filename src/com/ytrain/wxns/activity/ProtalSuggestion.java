package com.ytrain.wxns.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ytrain.wxns.R;
import com.ytrain.wxns.entity.ProtalIntroduce;
import com.ytrain.wxns.utils.ConnectorService;
import com.ytrain.wxns.utils.Wvc;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

public class ProtalSuggestion extends Activity {
	private String tag, text;
	private TextView tvTitle, tvBack;
	private int flag;
	private WebView webView;
	final String mimeType = "text/html";
	final String encoding = "UTF-8";
	WebSettings ws = null;

	Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
			webView.setWebViewClient(new Wvc(ProtalSuggestion.this));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.protal_suggestion);
		ExitApp.getInstance().addActivity(this);
		Constants.SetTitle(this);
		Bundle bundle = this.getIntent().getExtras();
		if (null != bundle) {
			tag = bundle.getString("tag");
			flag = bundle.getInt("flag");
		}
		initView();
		initData();
	}

	/**
	 * 加载数据soap协议
	 */
	private void initData() {
		String result = null;
		try {
			result = ConnectorService.getInstance().findWifiInfoAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (result != null) {
			ProtalIntroduce ue = null;
			JSONArray array = JSON.parseArray(result);
			List<ProtalIntroduce> list = new ArrayList<ProtalIntroduce>();
			int size = array.size();
			for (int i = 0; i < size; i++) {
				ue = array.getObject(i, ProtalIntroduce.class);
				list.add(ue);
			}
			switch (flag) {
			case 3:
				text = list.get(0).getContent();
				break;
			case 1:
				text = list.get(2).getContent();
				break;
			case 2:
				text = list.get(1).getContent();

				break;

			default:
				break;
			}
		}
		hander.sendEmptyMessage(100);
	}

	private void initView() {
		tvBack = (TextView) findViewById(R.id.tvBack);
		webView = (WebView) findViewById(R.id.webView1);

		tvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProtalSuggestion.this.finish();
			}
		});
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(tag);

		ws = webView.getSettings();
		ws.setDefaultTextEncodingName(encoding);
		ws.setJavaScriptEnabled(true);
		// 自适应手机屏幕
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// 设置缩放控件
		ws.setBuiltInZoomControls(true);
		// 隐藏控件
		setZoomControlGone(webView);

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
			ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
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
}
