package com.cnmobi.exianmall.type.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.PingjiaAdapter;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.ConfirmOrderProductBean;
import com.cnmobi.exianmall.bean.Pingjia;
import com.cnmobi.exianmall.widget.MyListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ProductPingJiaActivity extends BaseActivity implements OnRefreshListener{
	
	@ViewInject(R.id.lv_product_pingjia)
	MyListView lv_product_pingjia;
	@ViewInject(R.id.id_swipe_pingjia)
	SwipeRefreshLayout mSwipeLayout;  
	private boolean isPullDown=false;
	private List<Pingjia> list;
	private PingjiaAdapter adapter;
	private String idCommodity = "";
	private String idLevel1 = "";
	
	/**
	 * 商品评价查询
	 */
	public static final int productionPingjias = 0;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_product_pingjia);
		ViewUtils.inject(this);
		init();
		
	}
	   
	void init(){
		idCommodity = getIntent().getStringExtra("idCommodity");
		idLevel1= getIntent().getStringExtra("idLevel");
		list= (List<Pingjia>) getIntent().getSerializableExtra("pingjiasList");
		mSwipeLayout.setOnRefreshListener(this);  
		mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,  android.R.color.holo_orange_light, android.R.color.holo_red_light);  
		adapter=new PingjiaAdapter(this,list);
		lv_product_pingjia.setAdapter(adapter);
		
	}
	/**
	 * 评价详情
	 */
	void getEvaluateDetail() {
		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
		params.addQueryStringParameter("idLevel", idLevel1);
		params.addQueryStringParameter("currentPage", "1");
		params.addQueryStringParameter("idCommodity", idCommodity);
		doHttp(0, MyConst.commodityEvaluateAction, params, productionPingjias,false);
	}
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
		case productionPingjias:
			List<Pingjia> lists = new ArrayList<Pingjia>();
			lists = new Gson().fromJson(jsonString, new TypeToken<List<Pingjia>>() {
			}.getType());
			list.clear();
			mSwipeLayout.setRefreshing(false);
			for (int i = 0; i < lists.size(); i++) {
				list.add(lists.get(i));
			}
			adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}

	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mSwipeLayout.setRefreshing(true);
		getEvaluateDetail();
		
	}
}
