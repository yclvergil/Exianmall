package com.cnmobi.exianmall.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 购物车一级分类
 * @author Administrator
 *
 */
public class ShopCarBeanGroup implements Serializable{
	private boolean istrue;//check是否被选中
	private boolean orderistrue;//订单中子项是否有选中项
	private int idStore;
//	private List<ShoppCartCommodity> shoppCartCommodity;
	private List<ShopCarsBean> shoppCartCommodity;
	private String storeAddress;
	private String buyDate;
	
	public String getBuyDate() {
		return buyDate;
	}

	public List<ShopCarsBean> getShoppCartCommodity() {
		return shoppCartCommodity;
	}

	public void setShoppCartCommodity(List<ShopCarsBean> shoppCartCommodity) {
		this.shoppCartCommodity = shoppCartCommodity;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public boolean isOrderistrue() {
		return orderistrue;
	}

	public void setOrderistrue(boolean orderistrue) {
		this.orderistrue = orderistrue;
	}

	public void setIdStore(int idStore) {
		this.idStore = idStore;
	}

	public int getIdStore() {
		return this.idStore;
	}

//	public List<ShoppCartCommodity> getShoppCartCommodity() {
//		return shoppCartCommodity;
//	}
//
//	public void setShoppCartCommodity(List<ShoppCartCommodity> shoppCartCommodity) {
//		this.shoppCartCommodity = shoppCartCommodity;
//	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreAddress() {
		return this.storeAddress;
	}

	public boolean isIstrue() {
		return istrue;
	}

	public void setIstrue(boolean istrue) {
		this.istrue = istrue;
	}

}

