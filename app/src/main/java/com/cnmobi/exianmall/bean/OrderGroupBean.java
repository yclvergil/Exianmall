package com.cnmobi.exianmall.bean;

import java.util.List;

public class OrderGroupBean {

	private String creationordertime;

	private int idFirstOrder;
	private String firstlevelorderNo;
	private int idNumber;
	private String secondlevelorderNo;
	private String subtotal;
	private String isurgent;
	private int orderstatus;
	private List<OrderDetail> orderDetail;
	private List<FirstOrderDetail> firstOrderDetail;
	private String orderprice;
	private String signtime;

	public String getSigntime() {
		return signtime;
	}

	public void setSigntime(String signtime) {
		this.signtime = signtime;
	}

	public String getSecondlevelorderNo() {
		return secondlevelorderNo;
	}

	public void setSecondlevelorderNo(String secondlevelorderNo) {
		this.secondlevelorderNo = secondlevelorderNo;
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}

	public List<FirstOrderDetail> getFirstOrderDetail() {
		return firstOrderDetail;
	}

	public void setFirstOrderDetail(List<FirstOrderDetail> firstOrderDetail) {
		this.firstOrderDetail = firstOrderDetail;
	}

	public int getIdFirstOrder() {
		return idFirstOrder;
	}

	public void setIdFirstOrder(int idFirstOrder) {
		this.idFirstOrder = idFirstOrder;
	}

	public String getFirstlevelorderNo() {
		return firstlevelorderNo;
	}

	public void setFirstlevelorderNo(String firstlevelorderNo) {
		this.firstlevelorderNo = firstlevelorderNo;
	}

	public int getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(int orderstatus) {
		this.orderstatus = orderstatus;
	}

	public void setCreationordertime(String creationordertime) {
		this.creationordertime = creationordertime;
	}

	public String getCreationordertime() {
		return this.creationordertime;
	}

	public void setOrderDetail(List<OrderDetail> orderDetail) {
		this.orderDetail = orderDetail;
	}

	public List<OrderDetail> getOrderDetail() {
		return this.orderDetail;
	}

	public void setOrderprice(String orderprice) {
		this.orderprice = orderprice;
	}

	public String getOrderprice() {
		return this.orderprice;
	}

	public String getIsurgent() {
		return isurgent;
	}

	public void setIsurgent(String isurgent) {
		this.isurgent = isurgent;
	}

}