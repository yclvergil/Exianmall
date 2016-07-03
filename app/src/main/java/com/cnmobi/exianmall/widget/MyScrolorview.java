package com.cnmobi.exianmall.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;
/**
 * 解决scrollView内布局变化后自动滚动
 * */
public class MyScrolorview extends ScrollView{

	public MyScrolorview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override  
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {  
            //禁止scrollView内布局变化后自动滚动  
            return 0;  
    }  
}
