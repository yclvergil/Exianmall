package com.cnmobi.exianmall.bean;

import java.util.List;

public class PayOrderInfo {
	private List<OrderIdBean> orderId;
	private String payTime;
	private boolean isSuccess;
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public List<OrderIdBean> getOrderId() {
		return orderId;
	}
	public void setOrderId(List<OrderIdBean> orderId) {
		this.orderId = orderId;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
}
