package com.cnmobi.exianmall.bean;

import java.util.List;

public class PresenceOfOriginAction {
	private String address;

	private String frofile;

	private int idNumber;

	private List<OriginImages> originImages;

	private List<OriginProduct> originProduct;

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setFrofile(String frofile) {
		this.frofile = frofile;
	}

	public String getFrofile() {
		return this.frofile;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}

	public int getIdNumber() {
		return this.idNumber;
	}

	public void setOriginImages(List<OriginImages> originImages) {
		this.originImages = originImages;
	}

	public List<OriginImages> getOriginImages() {
		return this.originImages;
	}

	public void setOriginProduct(List<OriginProduct> originProduct) {
		this.originProduct = originProduct;
	}

	public List<OriginProduct> getOriginProduct() {
		return this.originProduct;
	}

}
