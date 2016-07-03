package com.cnmobi.exianmall.interfaces;

import android.view.View;

public interface ICancelOrderClickListener {
	
	//取消订单 idOrder：订单主键    orderStatus：订单状态
	void onCancelOrderClickListener(View v,int position,int idOrder,int orderStatus);
}
