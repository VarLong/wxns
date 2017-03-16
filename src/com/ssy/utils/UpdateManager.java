package com.ssy.utils;

import com.ytrain.wxns.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class UpdateManager {
	/**
	 * 更新升级
	 */
	private Context mContext;
	private String apkUrl = "";

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	// 外部接口让主Activity调用
	public void checkUpdateInfo(String url) {
		apkUrl = url;
		showNoticeDialog();
	}

	private void showNoticeDialog() {
		int width = getStatusWidth(mContext);
		final AlertDialog dlg = new AlertDialog.Builder(mContext).create();
		dlg.show();
		dlg.setCanceledOnTouchOutside(false);
		Window window = dlg.getWindow();
		// 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		// 动态设置提示框宽度
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = width;
		window.setAttributes(lp);
		window.setContentView(R.layout.dialog_cache);
		// 为确认按钮添加事件,执行退出应用操作
		TextView title = (TextView) window.findViewById(R.id.dialog_title);
		TextView con = (TextView) window.findViewById(R.id.dialog_conent);
		Button cancle = (Button) window.findViewById(R.id.cache_ok);
		Button ok = (Button) window.findViewById(R.id.cache_cancle);
		cancle.setText(R.string.update_cancel);
		ok.setText(R.string.update_ok);
		title.setText(R.string.update_title);
		con.setText(R.string.update_content);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.cancel();
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(apkUrl));
				mContext.startActivity(intent);

			}
		});
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.cancel();

			}
		});

	}

	/**
	 * 根据屏幕大小获得弹出框的宽度。大小为十分之九屏幕宽度
	 * 
	 * @return
	 */
	public int getStatusWidth(Context context) {

		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth();
		return width / 10 * 9;
	}


}
