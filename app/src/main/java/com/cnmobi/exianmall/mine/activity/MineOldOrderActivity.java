package com.cnmobi.exianmall.mine.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.MerchantOldOrderAdapter;
import com.cnmobi.exianmall.adapter.OldOrderAdapter;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.OrderDetail;
import com.cnmobi.exianmall.bean.OrderGroupBean;
import com.cnmobi.exianmall.interfaces.IAlterEvaluateClickListen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 历史订单
 * 
 */
public class MineOldOrderActivity extends BaseActivity {
	@ViewInject(R.id.listview)
	ExpandableListView listView;

	private List<OrderGroupBean> groupList = new ArrayList<OrderGroupBean>();
	OrderGroupBean groupBean = new OrderGroupBean();
//	private List<List<OrderDetail>> childList = new ArrayList<List<OrderDetail>>();
	private String userRole = "";

	/**
	 * 历史订单接口标识
	 */
	public static final int historicalOrder = 0;
	/**
	 * 修改商品评价接口详情
	 */
	public static final int alterEvaluate = 1;
	/** 订单当前页 */
	private int currentPage = 1;

	private OldOrderAdapter oldOrderAdapter;
	private MerchantOldOrderAdapter merchantOldOrderAdapter;
//	private boolean isAll = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_oldorder);
		ViewUtils.inject(this);

		setTitleText("历史订单");

		userRole = getIntent().getStringExtra("userRole");
		init();
		historicalOrderhttp();
	}

	void init() {
		listView.setGroupIndicator(null);// 将控件默认的左边箭头去掉
//		listView.setOnScrollListener(new OnScrollListener() {
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				// TODO Auto-generated method stub
//				// 当不滚动时
//				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//					// 判断是否滚动到底部
//					if (view.getLastVisiblePosition() == view.getCount() - 1) {
//						// 加载更多功能的代码
//						if (!isAll) {
//							currentPage++;
//							historicalOrderhttp();
//						}
//					}
//				}
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//				// TODO Auto-generated method stub
//			}
//		});

//		oldOrderAdapter = new OldOrderAdapter(MineOldOrderActivity.this, groupList, childList);
		oldOrderAdapter = new OldOrderAdapter(MineOldOrderActivity.this, groupList);
//		merchantOldOrderAdapter = new MerchantOldOrderAdapter(MineOldOrderActivity.this, groupList, childList);
		merchantOldOrderAdapter = new MerchantOldOrderAdapter(MineOldOrderActivity.this, groupList);
		if ("purchaser".equals(userRole)) {
			listView.setAdapter(oldOrderAdapter);
		}
		if ("merchant".equals(userRole)) {
			listView.setAdapter(merchantOldOrderAdapter);
		}
		oldOrderAdapter.setOnAlterEvaluateClickListen(new IAlterEvaluateClickListen() {
			
			@Override
			public void onAlterEvaluate(View v, int position1, int position2,
					String str, float score) {
				// TODO Auto-generated method stub
				LogUtils.i("position1"+position1+"------position2"+position2+"--------"+"str"+str+"------score"+score);
						alterEvaluate(
								groupList.get(position1).getOrderDetail()
										.get(position2).getIdScore(),str,score);
						LogUtils.i("idscore:"+groupList.get(position1).getOrderDetail()
								.get(position2).getIdScore()+";Evaluatecontent"+str);
			}
		});
	}

	// 历史订单接口
	void historicalOrderhttp() {
		RequestParams params = new RequestParams();
//		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
//		params.addBodyParameter("currentPage", currentPage + "");
		doHttp(1, MyConst.historicalOrderAction, params, historicalOrder);
	}

	/*
	 * 修改评价接口
	 */
	void alterEvaluate(String idScore,String evaluatecontent,float score){
		RequestParams params = new RequestParams();
		params.addBodyParameter("idScore", idScore);
		params.addBodyParameter("evaluatecontent", evaluatecontent);
		params.addBodyParameter("score", score+"");
		doHttp(1, MyConst.updateEvaluateAction, params, alterEvaluate);
	}
	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		Gson gson;
		switch (flag) {
		case historicalOrder:
			gson = new Gson();
			ArrayList<OrderGroupBean> orderGroupBeans = new ArrayList<OrderGroupBean>();
			orderGroupBeans = gson.fromJson(jsonString, new TypeToken<List<OrderGroupBean>>() {
			}.getType());
			
//			if (orderGroupBeans.size() == 0) {
//				isAll = true;
//				showToast("加载完毕");
//			}
			if(orderGroupBeans.size()==0){
				showToast("您还没有历史订单记录！");
			}
			for (int i = 0; i < orderGroupBeans.size(); i++) {
				groupList.add(orderGroupBeans.get(i));
			}
//			for (int i = 0; i < groupList.size(); i++) {
//				List<OrderDetail> list = groupList.get(i).getOrderDetail();
//				for (int j = 0; j < list.size(); j++) {
//					childList.add(list);
//				}
//			}
			if ("purchaser".equals(userRole)) {
				oldOrderAdapter.notifyDataSetChanged();
			}
			if ("merchant".equals(userRole)) {
				merchantOldOrderAdapter.notifyDataSetChanged();
			}
			break;
		case alterEvaluate:
			showToast("修改商品评价成功");
			oldOrderAdapter.notifyDataSetChanged();
			historicalOrderhttp();
			break;
		default:
			break;
		}
	}
}
