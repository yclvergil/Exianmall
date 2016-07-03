package com.cnmobi.exianmall.bean;

public class HisttoryOrderBean {
	/** 商品图片 **/
	private String imagename;
	/** 购物车 ResourceId **/
	private int carResourceId;
	/** 商品名称 **/
	private String commodityname;
	/** 库存数量 **/
	private String stock;
	/** 单价 **/
	private String price;
	/** 包装规格 **/
	private String companyname;
	/**商品品级**/
	private String idLevel;
	
	public String getIdLevel() {
		return idLevel;
	}

	public void setIdLevel(String idLevel) {
		this.idLevel = idLevel;
	}

	private String idNumber;

	public String getImagename() {
		return imagename;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}

	public int getCarResourceId() {
		return carResourceId;
	}

	public void setCarResourceId(int carResourceId) {
		this.carResourceId = carResourceId;
	}

	public String getCommodityname() {
		return commodityname;
	}

	public void setCommodityname(String commodityname) {
		this.commodityname = commodityname;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

}
