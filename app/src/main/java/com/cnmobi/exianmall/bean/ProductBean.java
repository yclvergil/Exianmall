package com.cnmobi.exianmall.bean;

/**
 * 二级商品
 * 
 * @author Administrator
 * 
 */
public class ProductBean {
	private String commodityname;

	private String companyname;

	private String idCommodity;

	private String idLevel;

	private String idStore;

	public String getIdCommodity() {
		return idCommodity;
	}

	public void setIdCommodity(String idCommodity) {
		this.idCommodity = idCommodity;
	}

	public String getIdStore() {
		return idStore;
	}

	public void setIdStore(String idStore) {
		this.idStore = idStore;
	}

	private String imagename;

	private String price;

	private String stock;
	
	private String idNumber;

	public String getIdLevel() {
		return idLevel;
	}

	public void setIdLevel(String idLevel) {
		this.idLevel = idLevel;
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

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getImagename() {
		return imagename;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

}
