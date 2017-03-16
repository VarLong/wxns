package com.ytrain.wxns.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ssy.utils.Constants;
import com.ssy.utils.FileService;
import com.ytrain.wxns.database.InitDataSqlite;
import com.ytrain.wxns.entity.PartEntity;
import com.ytrain.wxns.entity.UnitEntity;
import com.ytrain.wxns.utils.ApplicationHelper;
import com.ytrain.wxns.utils.ConnectorService;

public class InitData {
	Context context;
	ApplicationHelper ah;

	public InitData(Context context, ApplicationHelper ah) {
		this.context = context;
		this.ah = ah;
	}

	/**
	 * 每天只更新一次热点单位信息
	 * 
	 */
	public void updateData() {
		InitDataSqlite slite = new InitDataSqlite(context);
		try {
			if (!slite.findUnitCacheByDate()) {
				List<UnitEntity> lstUnit = ah.getLstUnit();
				if (Constants.isExistNetwork()) {
					// 本地没有从服务器中抓取
					String text = ConnectorService.getInstance().findNewUnit();
					UnitEntity ue = null;
					JSONArray array = JSON.parseArray(text);
					List<UnitEntity> lstNewUnit = new ArrayList<UnitEntity>();
					int size = array.size();
					for (int i = 0; i < size; i++) {
						ue = array.getObject(i, UnitEntity.class);
						lstNewUnit.add(ue);
						lstUnit.add(ue);
					}
					if (lstNewUnit != null && lstNewUnit.size() > 0) {
						// 保存到本地
						slite.saveUnit(lstNewUnit);
						// 保存更新时间
						slite.updateUnitRecord();
						// 保存到全局变量
						ah.setLstUnit(lstUnit);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化热点单位
	 * 
	 * @param slite
	 * @throws Exception
	 */
	public void getUnitAll(InitDataSqlite slite) throws Exception {
		// 初始化热点单位 -- 开始
		// 第一步从本地中取
		List<UnitEntity> lstUnit = slite.findCache();
		if (lstUnit != null && lstUnit.size() == 1
				&& Constants.isExistNetwork()) {
			this.delFirstData(slite);
		}
		lstUnit = ah.getLstUnit();
		if (null == lstUnit || lstUnit.size() < 1) {
			if (Constants.isExistNetwork()) {
				// 本地没有从服务器中抓取
				String text = ConnectorService.getInstance().findUnit();
				UnitEntity ue = null;
				JSONArray array = JSON.parseArray(text);
				int size = array.size();
				for (int i = 0; i < size; i++) {
					ue = array.getObject(i, UnitEntity.class);
					lstUnit.add(ue);
				}
				// 保存到本地
				slite.saveUnit(lstUnit);
			}
		}
		ah.setLstUnit(lstUnit);
		// 初始化热点单位 -- 结束
	}

	/**
	 * 获取智慧政务栏目信息
	 * 
	 * @param slite
	 * @throws Exception
	 */
	public void getPartByZhzw(InitDataSqlite slite) throws Exception {
		// 初始化智慧政务栏目 -- 开始
		List<PartEntity> lstZhzw = ah.getLstZhzw();
		if (null == lstZhzw || lstZhzw.size() < 1) {
			// 第一步从本地中取
			lstZhzw = slite.findCacheBySsid("1");
			if (null == lstZhzw || lstZhzw.size() < 1) {
				if (Constants.isExistNetwork()) {
					// 本地没有从服务器中抓取
					String text = ConnectorService.getInstance()
							.findPartBySsid("1");
					JSONArray array = JSON.parseArray(text);
					int size = array.size();
					PartEntity pe = null;
					for (int i = 0; i < size; i++) {
						pe = array.getObject(i, PartEntity.class);
						lstZhzw.add(pe);
					}
					// 保存到本地
					slite.savePart(lstZhzw);
				}
			}
			// 设置到全部变量中
			ah.setLstZhzw(lstZhzw);
		}
		// 初始化智慧政务栏目 -- 结束
	}

	/**
	 * 根据ssid获取栏目信息
	 * 
	 * @param slite
	 * @throws Exception
	 */
	public void getPartBySsid(InitDataSqlite slite) throws Exception {
		// 初始化热点单位栏目 -- 开始
		FileService fs = new FileService(context);
		String ssid = fs.readFile("ssid.data");
		if (ssid == null || "".equals(ssid)) {
			ssid = Constants.SSID;
		}
		// 第一步从本地中取
		List<PartEntity> lstSsid = slite.findCacheBySsid(ssid);
		if (null == lstSsid || lstSsid.size() < 1) {
			if (Constants.isExistNetwork()) {
				// 本地没有从服务器中抓取
				String text = ConnectorService.getInstance().findPartBySsid(
						ssid);
				JSONArray array = JSON.parseArray(text);
				int size = array.size();
				PartEntity pe = null;
				for (int i = 0; i < size; i++) {
					pe = array.getObject(i, PartEntity.class);
					lstSsid.add(pe);
				}
				// 保存到本地
				slite.savePart(lstSsid);
			}
		}
		// 设置到全部变量中
		ah.setLstSsid(lstSsid);
		// 初始化热点单位栏目 -- 结束
	}

	public void delFirstData(InitDataSqlite slite) {
		ah.setLstUnit(new ArrayList<UnitEntity>());
		try {
			slite.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addFirstData(InitDataSqlite slite) {
		try {
			List<PartEntity> lstPeZhzw = new ArrayList<PartEntity>();
			PartEntity pe = new PartEntity();
			pe.setId(1);
			pe.setName("南山动态");
			pe.setParentId(0);
			pe.setLevel(1);
			pe.setSsid("1");
			lstPeZhzw.add(pe);

			pe = new PartEntity();
			pe.setId(2);
			pe.setName("南山概况");
			pe.setParentId(0);
			pe.setLevel(1);
			pe.setSsid("1");
			lstPeZhzw.add(pe);

			pe = new PartEntity();
			pe.setId(3);
			pe.setName("办事指南");
			pe.setParentId(0);
			pe.setLevel(1);
			pe.setSsid("1");
			lstPeZhzw.add(pe);

			pe = new PartEntity();
			pe.setId(4);
			pe.setName("南山网");
			pe.setParentId(0);
			pe.setLevel(1);
			pe.setSsid("1");
			lstPeZhzw.add(pe);

			ah.setLstZhzw(lstPeZhzw);
			slite.savePart(lstPeZhzw);

			List<PartEntity> lstPeSsid = new ArrayList<PartEntity>();
			pe = new PartEntity();
			pe.setId(5);
			pe.setName("信息服务");
			pe.setParentId(0);
			pe.setLevel(1);
			pe.setSsid("384102488@vlan");
			lstPeSsid.add(pe);

			pe = new PartEntity();
			pe.setId(6);
			pe.setName("服务指南");
			pe.setParentId(0);
			pe.setLevel(1);
			pe.setSsid("384102488@vlan");
			lstPeSsid.add(pe);

			pe = new PartEntity();
			pe.setId(7);
			pe.setName("交通指引");
			pe.setParentId(0);
			pe.setLevel(1);
			pe.setSsid("384102488@vlan");
			lstPeSsid.add(pe);

			pe = new PartEntity();
			pe.setId(8);
			pe.setName("一键拨号");
			pe.setParentId(0);
			pe.setLevel(1);
			pe.setSsid("384102488@vlan");
			lstPeSsid.add(pe);
			ah.setLstSsid(lstPeSsid);
			slite.savePart(lstPeSsid);

			List<UnitEntity> lstUnit = new ArrayList<UnitEntity>();
			UnitEntity ue = new UnitEntity();
			ue.setId(3);
			ue.setDescn("南山区是广东省深圳市辖区。于1990年1月经国务院批准成立，地处深圳经济特区西部，东临深圳湾，西濒珠江口，北靠羊台山，南至内伶仃岛和大铲岛，与香港元朗隔海相望。面积约182平方公里，下辖8个街道办事处和98个社区居委会。 ");
			ue.setDomain("qzfxzfud.wxns.com");
			ue.setFlag(1);
			ue.setImgUrl("/unit/1422253518908.jpg");
			ue.setPhones("075526669911");
			ue.setSsid("384102488@vlan");
			ue.setUrlPanoramic("/3d/1422253112324.jpg");
			ue.setWifiname("区政府行政服务大厅");
			lstUnit.add(ue);
			ah.setLstUnit(lstUnit);
			slite.saveUnit(lstUnit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
