//package com.cnmobi.exianmall.type.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.cnmobi.exianmall.R;
//import com.cnmobi.exianmall.base.BaseActivity;
//import com.cnmobi.exianmall.base.MyConst;
//import com.cnmobi.exianmall.mine.activity.MineProductionPlaceActivity;
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.lidroid.xutils.view.annotation.event.OnClick;
//
//public class ProductJianjieActivity extends BaseActivity{
//	@ViewInject(R.id.iv_a)
//	ImageView iv_a;
//	@ViewInject(R.id.iv_b)
//	ImageView iv_b;
//	@ViewInject(R.id.iv_c)
//	ImageView iv_c;
//	@ViewInject(R.id.tv_commodityname)
//	TextView tv_commodityname;
//	@ViewInject(R.id.tv_product_address)
//	TextView tv_product_address;
//	@ViewInject(R.id.tv_grossweight)
//	TextView tv_grossweight;
//	@ViewInject(R.id.tv_cleanweight)
//	TextView tv_cleanweight;
//	@ViewInject(R.id.tv_outerpacking)
//	TextView tv_outerpacking;
//	
//	private String idCommodity = "";
//	private String imageUrl = "";
//	private String idLevel = "";
//	private String idLevel1 = "";
//	/**
//	 * 商品详情接口标识
//	 */
//	public static final int productionDetail = 0;
//	/**
//	 * 商品等级切换接口标识
//	 */
//	public static final int cutProductionLevel = 1;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setBaseContentView(R.layout.activity_product_jianjie);
//		ViewUtils.inject(this);
//		}
//	
//	/**
//	 * 商品详情
//	 */
//	void getProductionDetail() {
//		RequestParams params = new RequestParams();
////		params.addQueryStringParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
//		params.addQueryStringParameter("idCommodity", idCommodity);
//		params.addQueryStringParameter("idLevel", idLevel1);
//		doHttp(0, MyConst.commodityDetailsAction, params, productionDetail);
//	}
//
//	/**
//	 * 商品等级切换
//	 */
//	void cutProductionLevel(String idLevel) {
//		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idCommodity", idCommodity);
//		params.addQueryStringParameter("idLevel", idLevel);
//		doHttp(0, MyConst.cutLevelAction, params, cutProductionLevel);
//	}
////	/**
////	 * 跳转到产地
////	 */
////	@OnClick(R.id.tv_product_address)
////	public void address(View view) {
////		Intent intent = new Intent(this, MineProductionPlaceActivity.class);
////		intent.putExtra("idStore", commodityDetailBean.getIdStore());
////		startActivity(intent);
////	}
//}
