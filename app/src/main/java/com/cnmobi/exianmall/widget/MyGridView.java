package com.cnmobi.exianmall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/***
 * 自动设配高度的GridView
 * 解决ScrollorView嵌套Gridview滑动冲突
 * @author Administrator
 *
 */
public class MyGridView extends GridView {  
  
    public MyGridView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    
    public MyGridView(Context context) {  
        super(context);  
    }  
 
   public MyGridView(Context context, AttributeSet attrs, int defStyle) {  
       super(context, attrs, defStyle);  
   }  

   @Override  
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	   //不可滚动
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
   }  
   
   
  
}

