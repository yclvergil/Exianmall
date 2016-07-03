package com.cnmobi.exianmall.mine.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.OriginProductGridAdapter;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.OriginImages;
import com.cnmobi.exianmall.bean.OriginProduct;
import com.cnmobi.exianmall.bean.PresenceOfOriginAction;
import com.cnmobi.exianmall.type.activity.ProductDetailActivity;
import com.cnmobi.exianmall.widget.ImageCycleView;
import com.cnmobi.exianmall.widget.ImageCycleView.ImageCycleViewListener;
import com.cnmobi.exianmall.widget.MyGridView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;

/**产地*/
public class MineProductionPlaceActivity extends BaseActivity{
	@ViewInject(R.id.gv_type)
	MyGridView typeGv;
	@ViewInject(R.id.tv_address)
	TextView tv_address;
	@ViewInject(R.id.tv_frofile)
	TextView tv_frofile;
	@ViewInject(R.id.cycleView)
	ImageCycleView cycleView;
	@ViewInject(R.id.lv_main)
	LinearLayout lv_main;
	PresenceOfOriginAction originAction;

	private String idStore="";//店铺id
	private ArrayList<String> mImageUrl = new ArrayList<String>();// 轮播图url集合
	private ArrayList<OriginProduct> oProducts=new ArrayList<OriginProduct>();
	OriginProduct product;
	/**
	 * 卖家产地接口标识
	 */
	public static final int productionPlace=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_merchant_prodution_place);
		setTitleText("产地");
		ViewUtils.inject(this);
		
		idStore=getIntent().getStringExtra("idStore");
		if(isNull(idStore)){
			idStore=MyApplication.getInstance().getIdStore();
		}
		productionPlaceHttp();
	}
	
	/**
	 * 卖家产地接口
	 */
	void productionPlaceHttp(){
		RequestParams params=new RequestParams();
		params.addBodyParameter("idStore", idStore);
		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addQueryStringParameter("toKen",MyApplication.getInstance().getToKen());
		HttpUtils httpUtils=new HttpUtils();
		httpUtils.send(HttpMethod.POST, MyConst.presenceOfOriginAction, params, new RequestCallBack<String>() {
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				showToast("服务器繁忙，请稍后再试~");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Log.i("", arg0.result);
				try {
					JSONObject jsonObject = new JSONObject(arg0.result);
					int errorCode = jsonObject.getInt("code");
					if (errorCode == 0) {
						lv_main.setVisibility(View.VISIBLE);
						// 在这里统一解析json外层，成功就解析接口内容
						if (jsonObject.has("response")) {
							Gson gson=new Gson();
							originAction=new PresenceOfOriginAction();
							originAction=gson.fromJson(jsonObject.getString("response"), PresenceOfOriginAction.class);
							tv_address.setText(originAction.getAddress());
							tv_frofile.setText(originAction.getFrofile());
							
							//轮播
							OriginImages images;
							mImageUrl.clear();
							for(int i=0;i<originAction.getOriginImages().size();i++){
								images=new OriginImages();
								images=originAction.getOriginImages().get(i);
								mImageUrl.add(images.getImageName());
							}
							if(mImageUrl.size()!=0&&mImageUrl!=null){
								cycleView.setImageResources(mImageUrl, null, mAdCycleViewListener);
							}
							
							//主营产品
							for(int i=0;i<originAction.getOriginProduct().size();i++){
								product=new OriginProduct();
								product=originAction.getOriginProduct().get(i);
								oProducts.add(product);
							}
							typeGv.setFocusable(false);
							typeGv.setAdapter(new OriginProductGridAdapter(MineProductionPlaceActivity.this, oProducts, R.layout.item_product_place));
						}
					} else {
						// 错误就统一弹出错误信息
						lv_main.setVisibility(View.GONE);
//						showToast(jsonObject.getString("message"));
						showToast("暂无产地介绍数据");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
				
			}
		});
		
//		doHttp(1, MyConst.presenceOfOriginAction, params, productionPlace);
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		Gson gson;
		switch(flag){
		case productionPlace:
			gson=new Gson();
			originAction=new PresenceOfOriginAction();
			originAction=gson.fromJson(jsonString, PresenceOfOriginAction.class);
			tv_address.setText(originAction.getAddress());
			tv_frofile.setText(originAction.getFrofile());
			
			//轮播
			OriginImages images;
			mImageUrl.clear();
			for(int i=0;i<originAction.getOriginImages().size();i++){
				images=new OriginImages();
				images=originAction.getOriginImages().get(i);
				mImageUrl.add(images.getImageName());
			}
			if(mImageUrl.size()!=0&&mImageUrl!=null){
				cycleView.setImageResources(mImageUrl, null, mAdCycleViewListener);
			}
			
			//主营产品
			for(int i=0;i<originAction.getOriginProduct().size();i++){
				product=new OriginProduct();
				product=originAction.getOriginProduct().get(i);
				oProducts.add(product);
			}
			typeGv.setFocusable(false);
			typeGv.setAdapter(new OriginProductGridAdapter(MineProductionPlaceActivity.this, oProducts, R.layout.item_product_place));

//			typeGv.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//					Intent intent=new Intent(MineProductionPlaceActivity.this, ProductDetailActivity.class);
//					intent.putExtra("idCommodity", oProducts.get(arg2).getIdCommodity());
//					intent.putExtra("idLevel", oProducts.get(arg2).getIdLevel());
////					intent.putExtra("idLevel", "1000");
//					intent.putExtra("imageUrl", oProducts.get(arg2).getImageName());
//					startActivity(intent);
//				}
//			});
			
			break;
			default:
				break;
		}
	}
	
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(int position, View imageView) {
			// TODO 单击图片处理事件
		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView, MyConst.IM_IMAGE_OPTIONS);
		}
	};
}
