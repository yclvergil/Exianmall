package com.cnmobi.exianmall.bean;

import java.util.List;

/**
 * 商品详情
 * 
 */
public class CommodityDetailBean {

	/** 生产地址 **/
	private String address;
	/** 商品品牌 **/
	private String brandname;
	/** 商品名称 **/
	private String commodityname;
	private String idCommodity;
	private String idStore;
	/** 购买人数 **/
	private int sumBuyNumber;
	private String corpname;

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	private List<AllLevel> allLevel;
	private List<CommodityLevel> commodityLevel;
	private List<CommodityDescribe> commoditydescribe;
	private List<CommodityDeatilBannerImage> imageNames;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBrandname() {
		return brandname;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	public String getCommodityname() {
		return commodityname;
	}

	public void setCommodityname(String commodityname) {
		this.commodityname = commodityname;
	}

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

	public int getSumBuyNumber() {
		return sumBuyNumber;
	}

	public void setSumBuyNumber(int sumBuyNumber) {
		this.sumBuyNumber = sumBuyNumber;
	}

	public List<AllLevel> getAllLevel() {
		return allLevel;
	}

	public void setAllLevel(List<AllLevel> allLevel) {
		this.allLevel = allLevel;
	}

	public List<CommodityLevel> getCommodityLevel() {
		return commodityLevel;
	}

	public void setCommodityLevel(List<CommodityLevel> commodityLevel) {
		this.commodityLevel = commodityLevel;
	}

	public List<CommodityDescribe> getCommoditydescribe() {
		return commoditydescribe;
	}

	public void setCommoditydescribe(List<CommodityDescribe> commoditydescribe) {
		this.commoditydescribe = commoditydescribe;
	}

	public List<CommodityDeatilBannerImage> getImageNames() {
		return imageNames;
	}

	public void setImageNames(List<CommodityDeatilBannerImage> imageNames) {
		this.imageNames = imageNames;
	}

}
