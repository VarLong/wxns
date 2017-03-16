package com.ytrain.wxns.activity;

import java.util.ArrayList;

import com.ssy.utils.Constants;
import com.ytrain.wxns.R;
import com.ytrain.wxns.adapter.WelcomePagerAdapter;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class Guide extends Activity {
	/**
	 * 引导界面
	 */
	private ViewPager mViewPager;
	private ImageView mPage0, mPage1, mPage2, mPage3, mPage4;
	WelcomePagerAdapter mPagerAdapter;
	private Button btn;
	private int TYPE = 0;
	private String SSID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		TYPE = Constants.getNetworkType(this);
		initView();
		initListener();

	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);
		mPage0 = (ImageView) findViewById(R.id.page0);
		mPage1 = (ImageView) findViewById(R.id.page1);
		mPage2 = (ImageView) findViewById(R.id.page2);
		mPage3 = (ImageView) findViewById(R.id.page3);
		mPage4 = (ImageView) findViewById(R.id.page4);
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.gallery_one, null);
		View view2 = mLi.inflate(R.layout.gallery_two, null);
		View view3 = mLi.inflate(R.layout.gallery_three, null);
		View view4 = mLi.inflate(R.layout.gallery_four, null);
		View view5 = mLi.inflate(R.layout.gallery_five, null);
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view5);
		final ArrayList<String> titles = new ArrayList<String>();
		titles.add("tab1");
		titles.add("tab2");
		titles.add("tab3");
		titles.add("tab4");
		titles.add("tab5");
		mPagerAdapter = new WelcomePagerAdapter(views, titles);
		mViewPager.setAdapter(mPagerAdapter);
		btn = (Button) view5.findViewById(R.id.whats_new_start_btn);

	}

	private void initListener() {
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showActivity();
			}
		});

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int page) {
			switch (page) {
			case 0:
				mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 1:
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 2:
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 3:
				mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
				mPage4.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 4:
				mPage4.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	@SuppressWarnings("unused")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

		}
	};

	private void showActivity() {
		new Thread() {
			@Override
			public void run() {
				switch (TYPE) {
				case 0:
					Intent intent = new Intent();
					intent.setClass(Guide.this, Wisdom.class);
					startActivity(intent);
					Guide.this.finish();
					break;
				case 1:
					SSID = Constants.getWiFiSSID(Guide.this);
					if (SSID.contains("SZ-WLAN(free)")) {
						// 如果是SZ-WLAN(free)热点，进入到认证界面，如果不是进入到主界面
						if (Constants.isProtalSuccess()) {
							Intent intentMain = new Intent();
							intentMain.setClass(Guide.this, Wisdom.class);
							startActivity(intentMain);
							Guide.this.finish();	
						}else {
							Intent intentMain = new Intent();
							intentMain.setClass(Guide.this, MainProtal.class);
							startActivity(intentMain);
							Guide.this.finish();
						}
						
					} else {
						Intent intent2 = new Intent();
						intent2.setClass(Guide.this, Wisdom.class);
						startActivity(intent2);
						Guide.this.finish();
					}
					break;
				case 2:
					Intent intent3 = new Intent();
					intent3.setClass(Guide.this, Wisdom.class);
					startActivity(intent3);
					Guide.this.finish();
					break;
				default:
					break;
				}
			}
		}.start();
	};
}
