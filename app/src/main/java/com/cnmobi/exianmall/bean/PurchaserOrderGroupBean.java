package com.cnmobi.exianmall.bean;

import java.util.List;

public class PurchaserOrderGroupBean {
	private String creationordertime;

	private int idNumber;

	private List<OrderDetail> orderDetail;

	private String orderprice;
	
	private List<SellerorderDetail> sellerorderDetail;
	
	private String isurgent;

	private int orderstatus;

	private String secondlevelorderNo;
//
	private String subtotal;

	
	public int getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(int orderstatus) {
		this.orderstatus = orderstatus;
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public void setCreationordertime(String creationordertime) {
		this.creationordertime = creationordertime;
	}

	public String getCreationordertime() {
		return this.creationordertime;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}

	public int getIdNumber() {
		return this.idNumber;
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

	public void setSecondlevelorderNo(String secondlevelorderNo) {
		this.secondlevelorderNo = secondlevelorderNo;
	}

	public String getSecondlevelorderNo() {
		return this.secondlevelorderNo;
	}

	public List<SellerorderDetail> getSellerorderDetails() {
		return sellerorderDetail;
	}

	public void setSellerorderDetails(List<SellerorderDetail> sellerorderDetails) {
		this.sellerorderDetail = sellerorderDetails;
	}

	public String getIsurgent() {
		return isurgent;
	}

	public void setIsurgent(String isurgent) {
		this.isurgent = isurgent;
	}
}
