package com.cnmobi.exianmall.interfaces;

import android.view.View;

//订单评分
public interface onOrderScoreItemClickListener {

	//评分按钮
		void onOrderScoreItemClick(View v, int position1,int position2,float rating,String str);
		void onOrderScoreItemClick(View v, int position1,int position2,float rating);
		//图标按钮评价、确认收货、确认接单....
		void onOrderScoreItemClick(View v, int position,String str);
		
		
}
