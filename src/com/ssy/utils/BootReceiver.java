package com.ssy.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String packageName = intent.getDataString();
		FileService fs = new FileService(context);
		if (packageName.equals("package:com.ytrain.ftwapp")) {
			if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
				fs.save("version.data", "0");
			} else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
				fs.save("version.data", "0");
			} else if (Intent.ACTION_PACKAGE_CHANGED.equals(intent.getAction())) {
				fs.save("version.data", "0");
			} else if (Intent.ACTION_PACKAGE_REPLACED
					.equals(intent.getAction())) {
				fs.save("version.data", "0");
			} else if (Intent.ACTION_PACKAGE_INSTALL.equals(intent.getAction())) {
				fs.save("version.data", "0");
			}
		}
	}
}