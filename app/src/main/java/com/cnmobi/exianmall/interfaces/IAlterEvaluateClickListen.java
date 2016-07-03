package com.cnmobi.exianmall.interfaces;

import android.view.View;

/**
 * 
 *修改评价
 */
public interface IAlterEvaluateClickListen {
	/**
	 * 
	 * @param v
	 * @param position1 
	 * @param position2 
	 * @param str 评价内容
	 * @param score 星级
	 */
	void onAlterEvaluate(View v, int position1,int position2,String str,float score);
}
