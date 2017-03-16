package com.ytrain.wxns.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ytrain.wxns.R;
import com.ytrain.wxns.adapter.AbstractSpinerAdapter.IOnItemSelectListener;
import com.ytrain.wxns.widget.SpinerPopWindow;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SuggestionActivity extends Activity
		implements OnClickListener, IOnItemSelectListener {
	private Button button;
	private EditText edittext1, edittext2, edittext3;
	private TextView tvBack, tvTitle, mTView;;
	private String type="网络问题";
	private List<String> nameList = new ArrayList<String>();
	private String[] names={"网络问题","APP使用问题","其他"};
	private SpinerPopWindow mSpinerPopWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestion);
		ExitApp.getInstance().addActivity(this);
		Constants.SetTitle(this);
		initView();
	}

	private void initView() {
		button = (Button) findViewById(R.id.button1);
		edittext1 = (EditText) findViewById(R.id.editText1);
		edittext2 = (EditText) findViewById(R.id.editText2);
		edittext3 = (EditText) findViewById(R.id.editText3);
		tvBack = (TextView) findViewById(R.id.tvBack);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("意见反馈");
		mTView = (TextView) findViewById(R.id.spinner1);
		mTView.setText("网络问题");
		button.setOnClickListener(this);
		tvBack.setOnClickListener(this);
		mTView.setOnClickListener(this);
		for(int i = 0; i < names.length; i++){
			nameList.add(names[i]);
		}		
		mSpinerPopWindow = new SpinerPopWindow(this);
		mSpinerPopWindow.refreshData(nameList, 0);
		mSpinerPopWindow.setItemListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			sendMessage();
			break;
		case R.id.tvBack:
			SuggestionActivity.this.finish();
			break;
		case R.id.spinner1:
			showSpinWindow();
			break;

		default:
			break;
		}

	}

	

	private void sendMessage() {
		if (edittext1.getText().toString() != null && edittext1.getText().toString().length() != 0) {
			if (edittext2.getText().toString() != null && edittext2.getText().toString().length() != 0) {
				if (edittext3.getText().toString() != null && edittext3.getText().toString().length() != 0) {
					if (Constants.isMobileNO(edittext3.getText().toString())) {
						List<NameValuePair> list = new ArrayList<NameValuePair>();
						list.add(new BasicNameValuePair("contactWay", edittext3.getText().toString()));
						list.add(new BasicNameValuePair("type", type));
						list.add(new BasicNameValuePair("content", edittext1.getText().toString()));
						list.add(new BasicNameValuePair("wifiSite", edittext2.getText().toString()));

						// 1.创建请求对象
						HttpPost httpPost = new HttpPost(Constants.SuggestionSave);
						// post请求方式数据放在实体类中
						HttpEntity entity = null;
						try {
							entity = new UrlEncodedFormEntity(list, HTTP.UTF_8);
							httpPost.setEntity(entity);
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						}
						// 2.创建客户端对象
						HttpClient httpClient = new DefaultHttpClient();
						// 3.客户端带着请求对象请求服务器端
						try {
							// 服务器端返回请求的数据
							HttpResponse httpResponse = httpClient.execute(httpPost);
							int id = httpResponse.getStatusLine().getStatusCode();
							
							// 解析请求返回的数据
							if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
								String element = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
								
								if (element.equals("ok")) {
									Toast.makeText(this, "提交成功！", Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(this, "提交失败！", Toast.LENGTH_SHORT).show();
								}

							}

						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {
						Toast.makeText(this, "手机号码错误！", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "请输入WIFI所在位置！", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "请输入反馈信息！", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onItemClick(int pos) {
		if (pos >= 0 && pos <= nameList.size()){
			String value = nameList.get(pos);	
			mTView.setText(value);
			type=value;
		}
		
	}
	private void showSpinWindow() {
		
		mSpinerPopWindow.setWidth(mTView.getWidth());
		mSpinerPopWindow.showAsDropDown(mTView);
		
	}
	
}
