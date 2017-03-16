package com.ytrain.wxns.adapter;

import java.util.ArrayList;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class WelcomePagerAdapter extends PagerAdapter{
	
	private ArrayList<View> views;
	@SuppressWarnings("unused")
	private ArrayList<String> titles;
	
	
	public WelcomePagerAdapter(ArrayList<View> views,ArrayList<String> titles){
		this.views = views;
		this.titles = titles;
	}
	
	@Override
	public int getCount() {
		return this.views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(views.get(position));
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(views.get(position));
		return views.get(position);
	}
}
