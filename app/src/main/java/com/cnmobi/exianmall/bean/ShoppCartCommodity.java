package com.cnmobi.exianmall.bean;

import java.io.Serializable;

/**
 * 购物车二级分类
 * @author Administrator
 *
 */
public class ShoppCartCommodity implements Serializable{
	private boolean istrue;

	private int buynumber;

	private String commodityname;

	private String companyname;

	private int idCommodity;

	private int idLevel;

	private int idShoppCart;

	private int idUser;

	private String imagename;

	private String levelname;

	private double price;

	private int stock;
	
	private int idStore;
	private double sendPrice;
	private int sumBuyNumber;
	private String arrivalTime;
	
	public double getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(double sendPrice) {
		this.sendPrice = sendPrice;
	}

	public int getIdStore() {
		return idStore;
	}

	public void setIdStore(int idStore) {
		this.idStore = idStore;
	}



	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public void setBuynumber(int buynumber) {
		this.buynumber = buynumber;
	}

	public int getBuynumber() {
		return this.buynumber;
	}

	public void setCommodityname(String commodityname) {
		this.commodityname = commodityname;
	}

	public String getCommodityname() {
		return this.commodityname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getCompanyname() {
		return this.companyname;
	}

	public void setIdCommodity(int idCommodity) {
		this.idCommodity = idCommodity;
	}

	public int getIdCommodity() {
		return this.idCommodity;
	}

	public void setIdLevel(int idLevel) {
		this.idLevel = idLevel;
	}

	public int getIdLevel() {
		return this.idLevel;
	}

	public void setIdShoppCart(int idShoppCart) {
		this.idShoppCart = idShoppCart;
	}

	public int getIdShoppCart() {
		return this.idShoppCart;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public int getIdUser() {
		return this.idUser;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}

	public String getImagename() {
		return this.imagename;
	}

	public void setLevelname(String levelname) {
		this.levelname = levelname;
	}

	public String getLevelname() {
		return this.levelname;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return this.price;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getStock() {
		return this.stock;
	}

	public void setSumBuyNumber(int sumBuyNumber) {
		this.sumBuyNumber = sumBuyNumber;
	}

	public int getSumBuyNumber() {
		return this.sumBuyNumber;
	}

	public boolean isIstrue() {
		return istrue;
	}

	public void setIstrue(boolean istrue) {
		this.istrue = istrue;
	}
	
	

}