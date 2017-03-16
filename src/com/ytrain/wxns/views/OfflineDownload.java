package com.ytrain.wxns.views;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ssy.utils.Constants;
import com.ssy.utils.FileService;
import com.ytrain.wxns.R;
import com.ytrain.wxns.database.InitDataSqlite;
import com.ytrain.wxns.entity.PartEntity;
import com.ytrain.wxns.entity.UnitEntity;
import com.ytrain.wxns.utils.ApplicationHelper;
import com.ytrain.wxns.utils.ConnectorService;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OfflineDownload extends Activity {
	private TextView downContext, completedProgress;
	private ProgressBar proBar;
	private TextView bgDownload;
	int size1 = 0, size2 = 0, size3 = 0;
	int i = 0, j = 0, k = 0;
	ApplicationHelper ah;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				downContext.setText("正在下载热点单位");
				proBar.setMax(size1);
				break;
			case 1:
				proBar.setMax(size1);
				proBar.setProgress(i);
				int x = i * 100 / size1;
				completedProgress.setText(x + "%");
				break;
			case 2:
				downContext.setText("热点单位下载完毕!");
				break;
			case 3:
				downContext.setText("正在下载栏目信息");
				proBar.setMax(size2);
				break;
			case 4:
				proBar.setMax(size2);
				proBar.setProgress(j);
				int y = j * 100 / size2;
				completedProgress.setText(y + "%");
				break;
			case 5:
				downContext.setText("栏目信息下载完毕!");
				break;
			case 6:
				bgDownload.setText("点击退出");
				break;
			case 7:
				downContext.setText("正在下载智慧政务栏目");
				proBar.setMax(size3);
				break;
			case 8:
				proBar.setMax(size3);
				proBar.setProgress(k);
				int z = k * 100 / size3;
				completedProgress.setText(z + "%");
				break;
			case 9:
				downContext.setText("智慧政务栏目下载完毕!");
				break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_dialog);

		initView();
		new Thread(new MyRunnable()).start();
		initListener();
	}

	private void initView() {
		// 完成进度
		completedProgress = (TextView) findViewById(R.id.tv_completed_progress);
		// 下载内容说明
		downContext = (TextView) findViewById(R.id.tvOffline);
		// 进度条
		proBar = (ProgressBar) findViewById(R.id.download_progress_bar);
		// 后台下载
		bgDownload = (TextView) findViewById(R.id.background_download);

	}

	private void initListener() {
		bgDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OfflineDownload.this.finish();

			}
		});

	}

	private class MyRunnable implements Runnable {

		@Override
		public void run() {
			InitDataSqlite idSqlite = new InitDataSqlite(OfflineDownload.this);
			try {
				ah = new ApplicationHelper();
				// 删除之前数据
				idSqlite.deleteAll();

				// 下载热点单位
				DownloadUnit(idSqlite);
				Thread.sleep(1000);
				// 下载获取智慧政务栏目信息
				DownloadZhzw(idSqlite);
				Thread.sleep(1000);
				// 根据ssid获取下载栏目信息
				DownloadPart(idSqlite);
				handler.sendEmptyMessage(6);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void DownloadUnit(InitDataSqlite idSqlite) throws Exception {
		handler.sendEmptyMessage(0);
		String text = ConnectorService.getInstance().findUnit();
		UnitEntity ue = null;
		List<UnitEntity> lstUnit = new ArrayList<UnitEntity>();
		JSONArray array = JSON.parseArray(text);
		size1 = array.size();

		while (i < size1) {
			ue = array.getObject(i, UnitEntity.class);
			lstUnit.add(ue);
			handler.sendEmptyMessage(1);
			i++;
		}
		// 保存到本地
		idSqlite.saveUnit(lstUnit);
		// 设置成全局
		ah.setLstUnit(lstUnit);
		handler.sendEmptyMessage(2);
	}

	private void DownloadZhzw(InitDataSqlite idSqlite) throws Exception {
		// 本地没有从服务器中抓取
		handler.sendEmptyMessage(7);
		String text = ConnectorService.getInstance().findPartBySsid("1");
		JSONArray array = JSON.parseArray(text);
		size3 = array.size();
		PartEntity pe = null;
		List<PartEntity> lstZhzw = new ArrayList<PartEntity>();
		while (k < size3) {
			pe = array.getObject(k, PartEntity.class);
			lstZhzw.add(pe);
			handler.sendEmptyMessage(8);
			k++;
		}
		// 保存到本地
		idSqlite.savePart(lstZhzw);
		// 设置到全部变量中
		ah.setLstZhzw(lstZhzw);
		handler.sendEmptyMessage(9);
	}

	private void DownloadPart(InitDataSqlite idSqlite) throws Exception {
		handler.sendEmptyMessage(3);
		FileService fs = new FileService(this);
		String ssid = fs.readFile("ssid.data");		
		if (ssid == null || "".equals(ssid)) {
			ssid = Constants.SSID;
		}

		String text = ConnectorService.getInstance().findPartBySsid(ssid);
		JSONArray array = JSON.parseArray(text);
		size2 = array.size();
		PartEntity pe = null;
		List<PartEntity> lstSsid = new ArrayList<PartEntity>();
		while (j < size2) {
			pe = array.getObject(j, PartEntity.class);
			lstSsid.add(pe);
			handler.sendEmptyMessage(4);
			j++;
		}
		// 保存到本地
		idSqlite.savePart(lstSsid);
		// 设置到全部变量
		ah.setLstSsid(lstSsid);
		handler.sendEmptyMessage(5);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.finish();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
