package com.cnmobi.exianmall.interfaces;

import android.view.View;

/**
 * 
 *申请退款
 */
public interface IOrderApplyRefundClickListen {
	/**
	 * 
	 * @param v
	 * @param position1
	 * @param position2
	 * @param flag =0 确认收货 =1 待评分
	 */
	void onApplyRefundItemClick(View v, int position1,int position2,int flag);
}
