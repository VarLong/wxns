package com.ytrain.wxns.utils;

import com.ytrain.wxns.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 加载显示的dilog
 * 
 * @author Administrator
 * 
 */
public class ShowLoading {
	LinearLayout layout;
	Context context;
	public ShowLoading(Context context) {
		this.context = context;
	}

	/**
	 * 加载框
	 */
	public Dialog loading() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialogview, null);

		layout = (LinearLayout) v.findViewById(R.id.dialog_view);

		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.color.animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		Dialog dialog = new android.app.Dialog(context,
				R.style.loading_dialog);
		dialog.setCancelable(true);
		dialog.setContentView(layout, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		return dialog;
	}
}
