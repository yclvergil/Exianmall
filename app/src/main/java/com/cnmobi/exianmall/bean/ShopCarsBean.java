package com.cnmobi.exianmall.bean;

import java.io.Serializable;

public class ShopCarsBean implements Serializable{

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
	public int getSumBuyNumber() {
		return sumBuyNumber;
	}
	public void setSumBuyNumber(int sumBuyNumber) {
		this.sumBuyNumber = sumBuyNumber;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	private String arrivalTime;
	public boolean isIstrue() {
		return istrue;
	}
	public void setIstrue(boolean istrue) {
		this.istrue = istrue;
	}
	public int getBuynumber() {
		return buynumber;
	}
	public void setBuynumber(int buynumber) {
		this.buynumber = buynumber;
	}
	public String getCommodityname() {
		return commodityname;
	}
	public void setCommodityname(String commodityname) {
		this.commodityname = commodityname;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
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
	public int getIdShoppCart() {
		return idShoppCart;
	}
	public void setIdShoppCart(int idShoppCart) {
		this.idShoppCart = idShoppCart;
	}
	public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	public String getImagename() {
		return imagename;
	}
	public void setImagename(String imagename) {
		this.imagename = imagename;
	}
	public String getLevelname() {
		return levelname;
	}
	public void setLevelname(String levelname) {
		this.levelname = levelname;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getIdStore() {
		return idStore;
	}
	public void setIdStore(int idStore) {
		this.idStore = idStore;
	}
	public double getSendPrice() {
		return sendPrice;
	}
	public void setSendPrice(double sendPrice) {
		this.sendPrice = sendPrice;
	}
	public ShopCarsBean(int idCommodity,int idLevel) {
		// TODO Auto-generated constructor stub
		this.idCommodity=idCommodity;
		this.idLevel=idLevel;
	}
}
