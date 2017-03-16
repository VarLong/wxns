package com.ytrain.wxns.entity;

import java.io.Serializable;

public class MessageSend implements Serializable{
	/**
	 * 短信返回值
	 */
	private static final long serialVersionUID = 1L;
	private String result;
	private String tips;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

}
