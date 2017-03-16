package com.ssy.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestNetwork {
	/**
	 * 测试实际网络情况
	 */
	ExecutorService transThread = Executors.newSingleThreadExecutor();
	Future<?> transPending;
	boolean flag = true, isConnection = false;
	private final Runnable mSender = new Runnable() {
		@Override
		public void run() {
			while (flag) {
				try {
					String path = "http://www.szftwifi.com/ftwisdom/ftwdata/version.xml";
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(2 * 1000);
					// conn.setReadTimeout(3 * 1000);
					conn.setRequestMethod("GET");
					InputStream is = conn.getInputStream();
					is.close();
					conn.disconnect();
					isConnection = true;
					if (isConnection) {
						cancel();
					}
				} catch (Exception e) {
					flag = false;
					isConnection = false;
					cancel();
					e.printStackTrace();
				}
			}
		}
	};

	void start() {
		transPending = transThread.submit(mSender);
	}

	void cancel() {
		flag = false;
		transPending.cancel(true);
	}

	boolean getConnectionState() {
		return isConnection;
	}
}
