package com.ytrain.wxns.entity;

import java.io.Serializable;

public class ProtalMsg implements Serializable {
	/**
	 * 电信认证结构返回值
	 */
	private static final long serialVersionUID = 1L;
	private Integer resultCode;
	private String description;

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
