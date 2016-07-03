package com.cnmobi.exianmall.widget;

import com.cnmobi.exianmall.base.MainActivity;
import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPage extends ViewPager{
     private Context mContext;
	public MyViewPage(Context context) {
		super(context);
		this.mContext=context;
	}

	public MyViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
	}
	
 @Override
public boolean dispatchTouchEvent(MotionEvent ev) {
	// TODO Auto-generated method stub
		Intent intent1 = new Intent();
		intent1.setAction(MainActivity.canopen_cehua);
		intent1.putExtra("isture", "cannotmove");
		LogUtils.e("关闭");
		mContext.sendBroadcast(intent1);
	return super.dispatchTouchEvent(ev);
}

}
