package com.cnmobi.exianmall.type.activity;

import java.util.ArrayList;
import java.util.List;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.widget.MatrixImageView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/**
 * 商品图片展示
 * 
 * @author peng24
 *
 */
public class ProductionShowActivity extends BaseActivity{

	private ViewPager viewpager_production_show;
	private TextView tv_productionshow_index;
	private int productionshow_index=0;//当前图片索引
	
	private TextView tv_productionshow_count;//图片数量
	private ArrayList<View> listViews = null;
	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	private MatrixImageView img;
	private List<String> imgsurls;
	private int location;
	private ImageView iv_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.production_show_layout);
//		setTitleText("图片浏览");
		imgsurls=(List<String>) getIntent().getSerializableExtra("imgsurls");
		location=getIntent().getIntExtra("location", 0);
		initUI();
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				tv_productionshow_index.setText(""+msg.getData().getInt("productionshow_index"));
			}
		};
	};
	
	public void initUI(){
		viewpager_production_show=(ViewPager) findViewById(R.id.viewpager_production_show);
		tv_productionshow_index=(TextView) findViewById(R.id.tv_productionshow_index);
		tv_productionshow_count=(TextView) findViewById(R.id.tv_productionshow_count);
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			finish();
			}
		});
		
		tv_productionshow_count.setText(""+imgsurls.size());
		for (int i = 0; i < imgsurls.size(); i++) {
			initListViews(imgsurls.get(i),i);//
		}
		viewpager_production_show.setOnPageChangeListener(pageChangeListener);
		viewpager_production_show.setAdapter(new MyPageAdapter(listViews));
		viewpager_production_show.setCurrentItem(location);
	}
	private void initListViews(String bm,int i) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		img = new MatrixImageView(this);// 构造ImageView对象
		img.setBackgroundColor(0xff000000);
		MyConst.imageLoader.displayImage(bm, img);
	    img.setId(i);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT
				));
		listViews.add(img);// 添加view
	}
	
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int position) {// 页面选择响应函数
			MatrixImageView img1;
			for(int i=0;i<listViews.size();i++){
				img1=(MatrixImageView) listViews.get(i);
				img1.setScaleType(ScaleType.FIT_CENTER); 
			}
			productionshow_index = position+1;
			
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Message msg=new Message();
					Bundle bundle=new Bundle();
					bundle.putInt("productionshow_index", productionshow_index);
					msg.setData(bundle);
					msg.what=1;
					handler.sendMessage(msg);
					
				}
			}).start();
	
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

		}
	};


//	@Override
//	public void onClick(View v) {
//		Intent intent;
//		switch (v.getId()) {
//		case R.id.return_img:
//			finish();
//			break;
//		}
//	}
	
	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;// content

		private int size;// 页数

		public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
															// 初始化viewpager的时候给的一个页面
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {// 返回数量
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {// 返回view对象
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
