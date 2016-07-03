package com.cnmobi.exianmall.bean;

public class CouponBean {
	
	/**优惠券-满**/
	private String couponOutMoney;
	/**优惠券-减**/
	private String couponReduceMoney;
	/**优惠券类型**/
	private String couponType;
	/**有效开始时间**/
	private String couponBeginDate;
	/**有效结束时间**/
	private String couponEndDate;
	/**优惠券标志**/
	private String couponMark;
	/**优惠券使用限制**/
	private String couponLimit;
	/**优惠券数量**/
	private String couponCount;
	public String getCouponMark() {
		return couponMark;
	}

	public void setCouponMark(String couponMark) {
		this.couponMark = couponMark;
	}

	public String getCouponLimit() {
		return couponLimit;
	}

	public void setCouponLimit(String couponLimit) {
		this.couponLimit = couponLimit;
	}

	public String getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(String couponCount) {
		this.couponCount = couponCount;
	}

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

	public CouponBean(String couponOutMoney,String couponReduceMoney,String couponType,String couponBeginDate,String couponEndDate
			,String couponMark,String couponLimit,String couponCount) {
		this.couponOutMoney=couponOutMoney;
		this.couponReduceMoney=couponReduceMoney;
		this.couponType=couponType;
		this.couponBeginDate=couponBeginDate;
		this.couponEndDate=couponEndDate;
		this.couponMark=couponMark;
		this.couponLimit=couponLimit;
		this.couponCount=couponCount;
		
	}
	
	public CouponBean() {
		
	}
	
	
	
}
