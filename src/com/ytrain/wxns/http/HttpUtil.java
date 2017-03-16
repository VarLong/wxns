package com.ytrain.wxns.http;


import android.content.Context;

public class HttpUtil {

	private static AsyncHttpClient client = new AsyncHttpClient();


	// ===============================================
	/*
	 * 全路径访
	 */
	public static void postFullUrl(String fullUrl, RequestParams params,
			AsyncHttpResponseHandler responseHandler, Context context) {
		if (params == null) {
			params = new RequestParams();
		}		
		client.post(fullUrl, params, responseHandler);
	}

	public static void getFullUrl(String fullUrl, RequestParams params,
			AsyncHttpResponseHandler responseHandler, Context context) {
		
		client.get(fullUrl, params, responseHandler);
	}

}
