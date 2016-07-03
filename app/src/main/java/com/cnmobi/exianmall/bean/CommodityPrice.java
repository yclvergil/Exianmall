package com.cnmobi.exianmall.bean;

/** 商品价格**/
public class CommodityPrice {

	private int idCommodity;
	private int idLevel;
	private double price;
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getIdCommodity() {
		return idCommodity;
	}
	public void setIdCommodity(int idCommodity) {
		this.idCommodity = idCommodity;
	}
	public int getIdLevel() {
		return idLevel;
	}
	public void setIdLevel(int idLevel) {
		this.idLevel = idLevel;
	}
	
	
}
