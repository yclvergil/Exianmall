package com.cnmobi.exianmall.bean;

import java.util.List;

/**
 * 确认订单
 */
public class ConfirmOrderBean {

	/** 余额 **/
	private double balance;
	/** 地址 **/
	private String deliveryaddress;
	private String idAccount;
	private String idConsignee;
	private String isSetpassword;
	private String phone;
	private String tlr_name;

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getDeliveryaddress() {
		return deliveryaddress;
	}

	public void setDeliveryaddress(String deliveryaddress) {
		this.deliveryaddress = deliveryaddress;
	}

	public String getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(String idAccount) {
		this.idAccount = idAccount;
	}

	public String getIdConsignee() {
		return idConsignee;
	}

	public void setIdConsignee(String idConsignee) {
		this.idConsignee = idConsignee;
	}

	public String getIsSetpassword() {
		return isSetpassword;
	}

	public void setIsSetpassword(String isSetpassword) {
		this.isSetpassword = isSetpassword;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTlr_name() {
		return tlr_name;
	}

	public void setTlr_name(String tlr_name) {
		this.tlr_name = tlr_name;
	}

	public List<PayWayBean> getPayways() {
		return payways;
	}

	public void setPayways(List<PayWayBean> payways) {
		this.payways = payways;
	}

	private List<PayWayBean> payways;

}
