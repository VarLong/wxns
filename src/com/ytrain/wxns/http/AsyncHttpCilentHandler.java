package com.ytrain.wxns.http;

import com.google.gson.Gson;

import android.content.Context;
import android.widget.Toast;

public abstract class AsyncHttpCilentHandler<T> extends
		AsyncHttpResponseHandler {
	protected Class<T> clazz;
	protected Context context;

	public AsyncHttpCilentHandler(Context paramContext, Class<T> paramClass) {
		this.context = paramContext;
		this.clazz = paramClass;
	}
	@Override
	public void onFailure(Throwable paramThrowable, String paramString) {
		if (this.context == null)
			return;
		if (paramString.equals("can't resolve host")) {
			Toast.makeText(context, "网络错误,请检查你的网络！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (paramString != null) {
			Toast.makeText(context, paramString, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onSuccess(String paramString) {
		if (this.context == null)
			return;

		if (paramString != null) {

		}
		try {
			Gson gson = new Gson();
			T paramT = gson.fromJson(paramString, clazz);
			process(paramT);
		} catch (Exception e) {
			Toast.makeText(context, "数据解析失败", Toast.LENGTH_SHORT).show();
		}

	}

	public abstract void process(T paramT);
}