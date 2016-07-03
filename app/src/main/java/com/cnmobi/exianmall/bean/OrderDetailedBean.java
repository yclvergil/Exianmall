package com.cnmobi.exianmall.bean;

public class OrderDetailedBean {

	/** 购买数量 **/
	private int buynumber;
	/** 单价 **/
	private float buyprice;
	/** 商品主键 **/
	private int idCommodity;
	/** 级别主键 **/
	private int idLevel;
	
	private String idShopCart;
	
	
	public String getIdShopCart() {
		return idShopCart;
	}

	public void setIdShopCart(String idShopCart) {
		this.idShopCart = idShopCart;
	}

	public int getBuynumber() {
		return buynumber;
	}

	public void setBuynumber(int buynumber) {
		this.buynumber = buynumber;
	}

	public float getBuyprice() {
		return buyprice;
	}

	public void setBuyprice(float buyprice) {
		this.buyprice = buyprice;
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
