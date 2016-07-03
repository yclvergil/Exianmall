package com.cnmobi.exianmall.bean;

public class OrderDetail {
	private String buynumber;// 购买数量

	private String buyprice;// 购买价格

	private String commodityname;// 商品名称

	private String companyname;// 商品单位名称

	private String idCommodity;// 商品主键

	private String idLevel;// 商品级别id

	private String levelname;// 商品级别

	private String score;// 商品评分星级
	private String evaluatecontent;
	private String evaluatetime;
	private String idScore;

	private String  isRefund;
	
	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
	}

	public String getIdScore() {
		return idScore;
	}

	public void setIdScore(String idScore) {
		this.idScore = idScore;
	}

	public String getEvaluatecontent() {
		return evaluatecontent;
	}

	public void setEvaluatecontent(String evaluatecontent) {
		this.evaluatecontent = evaluatecontent;
	}

	public String getEvaluatetime() {
		return evaluatetime;
	}

	public void setEvaluatetime(String evaluatetime) {
		this.evaluatetime = evaluatetime;
	}

	/** 产地 **/
	private String address;
	/** 产地id **/
	private String addressId;
	/** 店铺id **/
	private int idStore;
	/** 图片URl **/
	private String imagename;
	/** 商品库存 **/
	private int stock;
	/** 商品购买人数 **/
	private int sumBuyNum;

	public String getBuynumber() {
		return buynumber;
	}

	public void setBuynumber(String buynumber) {
		this.buynumber = buynumber;
	}

	public String getBuyprice() {
		return buyprice;
	}

	public void setBuyprice(String buyprice) {
		this.buyprice = buyprice;
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

	public String getIdCommodity() {
		return idCommodity;
	}

	public void setIdCommodity(String idCommodity) {
		this.idCommodity = idCommodity;
	}

	public String getIdLevel() {
		return idLevel;
	}

	public void setIdLevel(String idLevel) {
		this.idLevel = idLevel;
	}

	public String getLevelname() {
		return levelname;
	}

	public void setLevelname(String levelname) {
		this.levelname = levelname;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public int getIdStore() {
		return idStore;
	}

	public void setIdStore(int idStore) {
		this.idStore = idStore;
	}

	public String getImagename() {
		return imagename;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
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

}