package com.cnmobi.exianmall.bean;

/**
 * 商品描述
 */
public class CommodityDescribe {

	private String describe;
	private String imagename;
	private String title;
	private int inSales;

	public int getInSales() {
		return inSales;
	}

	public void setInSales(int inSales) {
		this.inSales = inSales;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getImagename() {
		return imagename;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}
}
