package com.ytrain.wxns.views;

import com.ssy.utils.FileService;
import com.ytrain.wxns.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ExitLine extends Activity {
	private String message;
	private TextView content, button;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		this.finish();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		if (null != bundle) {
			message = bundle.getString("message");
		} else {
			message = "下线成功!";
		}
		setContentView(R.layout.exit_offline);
		initView();
	}

	private void initView() {
		content = (TextView) findViewById(R.id.textView1);
		button = (TextView) findViewById(R.id.sure);
		content.setText(message);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();	
			}
		});

	}

}
