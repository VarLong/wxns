package com.ytrain.wxns.entity;

import java.io.Serializable;

public class ProtalIntroduce implements Serializable{
	/**
	 * 用户信息，常见问题 ，说明介绍
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String content;
	private String create;
	private int createTime;

	public String getCreate() {
		return create;
	}

	public void setCreate(String create) {
		this.create = create;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
