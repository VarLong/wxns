package com.ytrain.wxns.utils;

import java.util.ArrayList;
import java.util.List;

import com.ssy.utils.Constants;
import com.ytrain.wxns.entity.PartEntity;
import com.ytrain.wxns.entity.UnitEntity;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

public class ApplicationHelper extends Application {
	private static ApplicationHelper mInstance = null;
	private HandlerException handlerException;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		// 全局的异常处理
		handlerException = HandlerException.getInstance();
		handlerException.init(getApplicationContext());
	}

	public static ApplicationHelper getInstance() {
		return mInstance;
	}

	/**
	 * 判断是否模拟器。如果返回TRUE，则当前是模拟器
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isEmulator(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (imei == null || imei.equals("000000000000000")) {
			return true;
		}
		return false;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	private boolean fisrtLoadData = false;

	private List<UnitEntity> lstUnit = new ArrayList<UnitEntity>();
	private List<PartEntity> lstZhzw = new ArrayList<PartEntity>();
	private List<PartEntity> lstSsid = new ArrayList<PartEntity>();
	private String ssid = Constants.SSID;
	private String version = "1.0";
	private String apkUrl = "";

	public List<UnitEntity> getLstUnit() {
		return lstUnit;
	}

	public void setLstUnit(List<UnitEntity> lstUnit) {
		this.lstUnit = lstUnit;
	}

	public List<PartEntity> getLstZhzw() {
		return lstZhzw;
	}

	public void setLstZhzw(List<PartEntity> lstZhzw) {
		this.lstZhzw = lstZhzw;
	}

	public List<PartEntity> getLstSsid() {
		return lstSsid;
	}

	public void setLstSsid(List<PartEntity> lstSsid) {
		this.lstSsid = lstSsid;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public boolean isFisrtLoadData() {
		return fisrtLoadData;
	}

	public void setFisrtLoadData(boolean fisrtLoadData) {
		this.fisrtLoadData = fisrtLoadData;
	}

}
