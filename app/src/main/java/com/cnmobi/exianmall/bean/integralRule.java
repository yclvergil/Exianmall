package com.cnmobi.exianmall.bean;

import java.util.List;

public class integralRule {
	private List<subIntegralrule> subIntegralrule;
	
	private List<coupons> coupons;

	private String summary;

	private String usableIntegral;

	public void setSubIntegralrule(List<subIntegralrule> subIntegralrule) {
		this.subIntegralrule = subIntegralrule;
	}

	public List<subIntegralrule> getSubIntegralrule() {
		return this.subIntegralrule;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setUsableIntegral(String usableIntegral) {
		this.usableIntegral = usableIntegral;
	}

	public String getUsableIntegral() {
		return this.usableIntegral;
	}

	public List<coupons> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<coupons> coupons) {
		this.coupons = coupons;
	}

	
}