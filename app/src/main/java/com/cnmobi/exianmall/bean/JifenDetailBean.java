package com.cnmobi.exianmall.bean;

public class JifenDetailBean {

	/** 清除时间 **/
	private String clearTime;
	/** 获得时间 **/
	private String getTime;
	/** 获得积分 **/
	private int integral;
	/** 获得积分途径 **/
	private int source;
	/** 有效截止日期 **/
	private String validTime;

	public String getClearTime() {
		return clearTime;
	}

	public void setClearTime(String clearTime) {
		this.clearTime = clearTime;
	}

	public String getGetTime() {
		return getTime;
	}

	public void setGetTime(String getTime) {
		this.getTime = getTime;
	}

	public int getIntegral() {
		return integral;
	}	

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

}
