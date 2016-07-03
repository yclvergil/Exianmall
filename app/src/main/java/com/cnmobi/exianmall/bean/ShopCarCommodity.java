package com.cnmobi.exianmall.bean;

public class ShopCarCommodity {

	private int idCommodity;
	private int idLevel;

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

	public ShopCarCommodity(int idCommodity,int idLevel) {
		// TODO Auto-generated constructor stub
		this.idCommodity=idCommodity;
		this.idLevel=idLevel;
	}
	

	
	
}
