package com.cnmobi.exianmall.interfaces;

/**
 * 统计价格
 */
public interface PriceItemClickListener {
	/**
	 * 改变数量时候改变价格 也可以理解为num=1
	 * @param ischoose
	 * @param price
	 */
	void toTalPriceClick(boolean ischoose, double price);

	void toTalPriceClick(boolean ischoose, double price, int num);
}
