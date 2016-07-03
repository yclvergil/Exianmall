package com.cnmobi.exianmall.bean;

public class ElectronicCouponBean {
	
	/**购物券数额**/
	private String couponmMoney;
	/**购物券类型**/
	private String couponType;
	/**有效开始时间**/
	private String couponBeginDate;
	/**有效结束时间**/
	private String couponEndDate;
	public String getCouponmMoney() {
		return couponmMoney;
	}
	public void setCouponmMoney(String couponmMoney) {
		this.couponmMoney = couponmMoney;
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
	
	public ElectronicCouponBean(String couponmMoney,String couponType,String couponBeginDate,String couponEndDate) {
		this.couponmMoney=couponmMoney;
		this.couponType=couponType;
		this.couponBeginDate=couponBeginDate;
		this.couponEndDate=couponEndDate;
	}
	
	public ElectronicCouponBean() {
		
	}
	
	
	
}
