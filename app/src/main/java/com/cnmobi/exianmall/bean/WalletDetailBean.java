package com.cnmobi.exianmall.bean;

public class WalletDetailBean {
	/** 收支来源 **/
	private String paymentType;
	/** 日期 **/
	private String paymentTime;
	/** 金额数量 **/
	private String paymentAmount;

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

}
