package com.cnmobi.exianmall.bean;

import java.util.List;

/**
 * 提交订单-订单信息
 */
public class OrderInfoBean {

	/** 店铺主键 **/
	private int idStore;
	/** 登录人主键 **/
	private int idUser;
	/** 下单时间，取系统时间 **/
	private String creationordertime;
	/** 要求到货时间 **/
	private String arrivaltime;
	private String isurgent;
	private String sendPrice;
	
	public String getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(String sendPrice) {
		this.sendPrice = sendPrice;
	}

	public String getIsurgent() {
		return isurgent;
	}

	public void setIsurgent(String isurgent) {
		this.isurgent = isurgent;
	}

	/** 订单总价 **/
	private double orderprice;
	private List<OrderDetailedBean> orderdetailed;

	public double getOrderprice() {
		return orderprice;
	}

	public void setOrderprice(double orderprice) {
		this.orderprice = orderprice;
	}

	public int getIdStore() {
		return idStore;
	}

	public void setIdStore(int idStore) {
		this.idStore = idStore;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getCreationordertime() {
		return creationordertime;
	}

	public void setCreationordertime(String creationordertime) {
		this.creationordertime = creationordertime;
	}

	public String getArrivaltime() {
		return arrivaltime;
	}

	public void setArrivaltime(String arrivaltime) {
		this.arrivaltime = arrivaltime;
	}

	

	public List<OrderDetailedBean> getOrderdetailed() {
		return orderdetailed;
	}

	public void setOrderdetailed(List<OrderDetailedBean> orderdetailed) {
		this.orderdetailed = orderdetailed;
	}

}
