package com.ytrain.wxns.entity;

public class PartEntity {
	Integer id;
	Integer parentId;
	String name;//标题
	String imageUrl;
	String ssid;
	Integer artCount;//内容详情
	Integer isShowIndex;
	Integer sortIndex;
	Integer level;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public Integer getArtCount() {
		return artCount;
	}

	public void setArtCount(Integer artCount) {
		this.artCount = artCount;
	}

	public Integer getIsShowIndex() {
		return isShowIndex;
	}

	public void setIsShowIndex(Integer isShowIndex) {
		this.isShowIndex = isShowIndex;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
