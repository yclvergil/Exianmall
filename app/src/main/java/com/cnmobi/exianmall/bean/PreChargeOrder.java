package com.cnmobi.exianmall.bean;

public class PreChargeOrder {
	
	private String amount;
	private String tradingtime;
	private String tradingNo;
	private boolean isSuccess;
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTradingtime() {
		return tradingtime;
	}
	public void setTradingtime(String tradingtime) {
		this.tradingtime = tradingtime;
	}
	public String getTradingNo() {
		return tradingNo;
	}
	public void setTradingNo(String tradingNo) {
		this.tradingNo = tradingNo;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
