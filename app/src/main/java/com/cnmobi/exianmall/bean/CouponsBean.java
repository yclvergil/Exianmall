package com.cnmobi.exianmall.bean;

public class CouponsBean {
	
	/**购物券-满**/
	private String couponOutMoney;
	/**购物券-减**/
	private String couponReduceMoney;
	/**购物券类型**/
	private String couponType;
	/**有效开始时间**/
	private String couponBeginDate;
	/**有效结束时间**/
	private String couponEndDate;
	
	
	public String getCouponOutMoney() {
		return couponOutMoney;
	}

	public void setCouponOutMoney(String couponOutMoney) {
		this.couponOutMoney = couponOutMoney;
	}

	public String getCouponReduceMoney() {
		return couponReduceMoney;
	}

	public void setCouponReduceMoney(String couponReduceMoney) {
		this.couponReduceMoney = couponReduceMoney;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getCouponBeginDate() {
		return couponBeginDate;
	}

	public void setCouponBeginDate(String couponBeginDate) {
		this.couponBeginDate = couponBeginDate;
	}

	public String getCouponEndDate() {
		return couponEndDate;
	}

	public void setCouponEndDate(String couponEndDate) {
		this.couponEndDate = couponEndDate;
	}

	public CouponsBean(String couponOutMoney,String couponReduceMoney,String couponType,String couponBeginDate,String couponEndDate) {
		this.couponOutMoney=couponOutMoney;
		this.couponReduceMoney=couponReduceMoney;
		this.couponType=couponType;
		this.couponBeginDate=couponBeginDate;
		this.couponEndDate=couponEndDate;
		
	}
	
	public CouponsBean() {
		
	}
	
	
	
}
