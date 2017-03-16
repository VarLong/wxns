package com.ytrain.wxns.activity;

import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ytrain.wxns.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SetAbout extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_about);
		ExitApp.getInstance().addActivity(SetAbout.this);
		Constants.SetTitle(this);
		TextView tvBack = (TextView) findViewById(R.id.tvBack);
		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("关于我们");
		tvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetAbout.this.finish();

			}
		});
	}
}
