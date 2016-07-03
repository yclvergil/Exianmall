package com.cnmobi.exianmall.bean;

import java.io.Serializable;

/**
 * 商品等级
 */
public class AllLevel implements Serializable{

	private String idLevel;
	private String levelName;

	public String getIdLevel() {
		return idLevel;
	}

	public void setIdLevel(String idLevel) {
		this.idLevel = idLevel;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
}
