package com.ytrain.wxns.utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.ssy.utils.Constants;

/**
 * 数据获取接口
 * 
 * @author cofey
 */
public class ConnectorService {
	final static String PART_SSID = "findPartBySsid";// 根据ssid获取栏目
	final static String PART_PARENT = "findPartByParentId";// 根据父级栏目id获取栏目
	final static String ARTICLE_PART = "findArticleByPartId";// 根据栏目id获取内容列表
	final static String ARTICLE_ID = "findArticleById";// 根据id获取内容
	final static String UNIT = "findUnit";// 获取所有单位
	final static String PART_ID = "findPartById";// 根据父级栏目id获取栏目
	final static String ARTICLE = "findArticle";// 站内查询内容
	final static String VERSION = "getVersion";// 获取版本信息
	final static String NEW_UNIT = "findNewUnit";// 获取新的单位信息
	final static String NEW_PART_TIME = "findPartByParentIdAndTime";// 根据parentId和时间获取新的数据
	final static String PROT_SUGGESTION = "findWifiInfoAll";// 根据parentId和时间获取新的数据

	final static String NAMESPACE = Constants.targetNamespace;

	public static ConnectorService instance = null;

	private ConnectorService() {
	}

	public static ConnectorService getInstance() {
		if (null == instance) {
			instance = new ConnectorService();
		}
		return instance;
	}

	/**
	 * 获取服务器数据
	 * 
	 * @param rpc
	 * @return
	 */
	private String getServerData(SoapObject rpc) throws Exception {
		// soap协议版本必须用SoapEnvelope.VER11
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.bodyOut = rpc;
		// 设置请求参数
		envelope.dotNet = false;
		envelope.setOutputSoapObject(rpc);
		// 创建HttpTransportSE对象
		String url = Constants.soapAddress;
		HttpTransportSE ht = new HttpTransportSE(url, 1000);
		String data = "";
		ht.call(null, envelope);
		Object object = envelope.getResponse();
		if (null != object) {
			data = envelope.getResponse().toString();
		}
		return data;
	}

	/**
	 * protal界面获取意见信息
	 * 
	 * @param rpc
	 * @return
	 */
	private String getProtalData(SoapObject rpc) throws Exception {
		// soap协议版本必须用SoapEnvelope.VER11
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.bodyOut = rpc;
		// 设置请求参数
		envelope.dotNet = false;
		envelope.setOutputSoapObject(rpc);
		// 创建HttpTransportSE对象
		String url = Constants.SOAPProt;
		HttpTransportSE ht = new HttpTransportSE(url, 1000);
		String data = "";
		ht.call(null, envelope);
		Object object = envelope.getResponse();
		if (null != object) {
			data = envelope.getResponse().toString();
		}
		return data;
	}

	private SoapObject getSoapObject(String name) {
		return new SoapObject(Constants.targetNamespace, name);
	}

	/**
	 * protal认证界面获取意见反馈的信息
	 * 
	 * @param partId
	 * @return
	 */
	public String findWifiInfoAll() throws Exception {
		SoapObject rpc = this.getSoapObject(PROT_SUGGESTION);
		return this.getProtalData(rpc);
	}

	/**
	 * 根据栏目编号获取列表信息
	 * 
	 * @param partId
	 * @return
	 */
	public String findArticleByPartId(int partId) throws Exception {
		SoapObject rpc = this.getSoapObject(ARTICLE_PART);
		rpc.addProperty("partId", partId);
		return this.getServerData(rpc);
	}

	/**
	 * 根据栏目编号获取列表信息,并分页
	 * 
	 * @param partId
	 * @return
	 */
	public String findArticlePageByPartId(int partId, int pageIndex) throws Exception {
		SoapObject rpc = this.getSoapObject("findArticlePageByPartId");
		rpc.addProperty("partId", partId);
		rpc.addProperty("pageIndex", pageIndex);// 当前页
		return this.getServerData(rpc);
	}

	/**
	 * 根据单位的唯一标识SSID获取一级栏目信息
	 * 
	 * @param ssid
	 * @return
	 */
	public String findPartBySsid(String ssid) throws Exception {
		SoapObject rpc = this.getSoapObject(PART_SSID);
		rpc.addProperty("ssid", ssid);
		return this.getServerData(rpc);
	}

	/**
	 * 根据上级栏目编号获取栏目信息
	 * 
	 * @param parentId
	 * @return
	 */
	public String findPartByParentId(int parentId) throws Exception {
		SoapObject rpc = this.getSoapObject(PART_PARENT);
		rpc.addProperty("parentId", parentId);
		return this.getServerData(rpc);
	}

	/**
	 * 根据parentId获取新的数据
	 * 
	 * @param parentId
	 * @return
	 */
	public String findPartByParentIdAndTime(int parentId) throws Exception {
		SoapObject rpc = this.getSoapObject(NEW_PART_TIME);
		rpc.addProperty("parentId", parentId);
		return this.getServerData(rpc);
	}

	/**
	 * 获取所有单位信息
	 * 
	 * @return
	 */
	public String findUnit() throws Exception {
		SoapObject rpc = this.getSoapObject(UNIT);
		return this.getServerData(rpc);
	}

	/**
	 * 获取新的单位信息
	 * 
	 * @return
	 */
	public String findNewUnit() throws Exception {
		SoapObject rpc = this.getSoapObject(NEW_UNIT);
		return this.getServerData(rpc);
	}

	/**
	 * 查询模块方法： 根据关键字、栏目编号获取内容列表信息
	 * 
	 * @param partId
	 * @param key
	 * @return
	 */
	public String findArticle(Integer partId, String key) throws Exception {
		SoapObject rpc = this.getSoapObject(ARTICLE);
		rpc.addProperty("partId", partId);
		rpc.addProperty("key", key);
		return this.getServerData(rpc);
	}

	/**
	 * 查询模块方法：根据关键字获取内容列表信息，并分頁
	 * 
	 * @param partId
	 * @param key
	 * @return
	 */
	public String findArticlePageByKey(String key, int pageIndex) throws Exception {
		SoapObject rpc = this.getSoapObject("findArticlePageByKey");
		rpc.addProperty("key", key);
		rpc.addProperty("pageIndex", pageIndex);
		return this.getServerData(rpc);
	}

	/**
	 * 获取版本信息
	 * 
	 * @return
	 */
	public String getVersion() throws Exception {
		SoapObject rpc = this.getSoapObject(VERSION);
		return this.getServerData(rpc);
	}
}
