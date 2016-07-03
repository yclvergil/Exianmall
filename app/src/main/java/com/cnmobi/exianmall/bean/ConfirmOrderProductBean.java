package com.cnmobi.exianmall.bean;

import java.io.Serializable;

/**
 * 确认订单-订单
 */
public class ConfirmOrderProductBean implements Serializable {

	/** 产地 **/
	private String adress;
	/** 产地id **/
	private String adressId;
	/** 店铺id **/
	private int idStore;
	/** 图片URl **/
	private String imageUrl;
	/** 商品名称 **/
	private String name;
	/** 商品价格 **/
	private String price;
	/** 商品等级 **/
	private String level;
	/** 商品等级id **/
	private int idLevel;
	/** 商品id **/
	private int idCommodity;
	/** 商品库存 **/
	private int stock;
	/** 商品购买人数 **/
	private int sumBuyNum;
	/** 商品购买数量 **/
	private int buyNum;
	/** 商品购买数量 **/
	private String  companyName;
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/** 到货日期 **/
	private String arrivaltime;
	private String idShopCart;
	private double sendPrice;
	private String buyDate;

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public double getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(double sendPrice) {
		this.sendPrice = sendPrice;
	}

	public String getIdShopCart() {
		return idShopCart;
	}

	public void setIdShopCart(String idShopCart) {
		this.idShopCart = idShopCart;
	}

	public String getArrivaltime() {
		return arrivaltime;
	}

	public void setArrivaltime(String arrivaltime) {
		this.arrivaltime = arrivaltime;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{ConfirmOrderProductBean : adress=" + adress + ",adressId="
				+ adressId + ",idStore=" + idStore + ",imageUrl=" + imageUrl
				+ ",name=" + name + ",price=" + price + "" + ",level=" + level
				+ ",idLevel=" + idLevel + ",idCommodity=" + idCommodity
				+ ",stock=" + stock + ",sumBuyNum=" + sumBuyNum + ",buyNum="
				+ buyNum + "}";
	}

	public int getIdCommodity() {
		return idCommodity;
	}

	public void setIdCommodity(int idCommodity) {
		this.idCommodity = idCommodity;
	}

	public int getIdStore() {
		return idStore;
	}

	public void setIdStore(int idStore) {
		this.idStore = idStore;
	}

	public int getIdLevel() {
		return idLevel;
	}

	public void setIdLevel(int idLevel) {
		this.idLevel = idLevel;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getAdressId() {
		return adressId;
	}

	public void setAdressId(String adressId) {
		this.adressId = adressId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getSumBuyNum() {
		return sumBuyNum;
	}

	public void setSumBuyNum(int sumBuyNum) {
		this.sumBuyNum = sumBuyNum;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

}
