package com.ytrain.wxns.entity;

import java.io.Serializable;

public class ProtalMessage implements Serializable {
	/**
	 * 移动认证结构返回值
	 */
	private static final long serialVersionUID = 1L;
	private Integer ret;
	private String msg;

	public Integer getRet() {
		return ret;
	}

	public void setRet(Integer ret) {
		this.ret = ret;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
