package com.ssy.utils;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/**
 * 退出app方法
 * 
 * @author Administrator
 * 
 */
public class ExitApp extends Application {
	private static ExitApp instance;
	private List<Activity> activityList = new LinkedList<Activity>();
	private ExitApp() {
	}

	public static ExitApp getInstance() {
		if (instance == null) {
			instance = new ExitApp();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}
