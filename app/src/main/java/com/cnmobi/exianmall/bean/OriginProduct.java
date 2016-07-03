package com.cnmobi.exianmall.bean;

public class OriginProduct {

	private String idNumber;

	private String imageName;

	private String productName;

	private String idCommodity;
	
	private String idLevel;
	
	public String getIdLevel() {
		return idLevel;
	}

	public void setIdLevel(String idLevel) {
		this.idLevel = idLevel;
	}

	public String getIdCommodity() {
		return idCommodity;
	}

	public void setIdCommodity(String idCommodity) {
		this.idCommodity = idCommodity;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getIdNumber() {
		return this.idNumber;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageName() {
		return this.imageName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return this.productName;
	}

}
