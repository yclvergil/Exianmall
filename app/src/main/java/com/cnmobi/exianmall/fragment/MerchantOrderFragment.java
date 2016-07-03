package com.cnmobi.exianmall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.MerchantOrderOneGroupAdapter;
import com.cnmobi.exianmall.adapter.MerchantOrderTwoGroupAdapter;
import com.cnmobi.exianmall.base.BaseFragment;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.OrderGroupBean;
import com.cnmobi.exianmall.interfaces.onOrderScoreItemClickListener;
import com.cnmobi.exianmall.mine.activity.MineMessageActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 卖家-订单界面
 */
public class MerchantOrderFragment extends BaseFragment implements OnScrollListener {

	@ViewInject(R.id.tv_order_wait_payment)
	TextView tv_order_wait_payment;
	@ViewInject(R.id.tv_order_wait_send)
	TextView tv_order_wait_send;
	@ViewInject(R.id.lv_one)
	ListView lv_one;// 待付款listview
	@ViewInject(R.id.lv_two)
	ListView lv_two;// 待收货listview
	@ViewInject(R.id.iv_home_right_top1)
	public static ImageView iv_home_right_top1;
	
	private List<OrderGroupBean> groupOneList = new ArrayList<OrderGroupBean>();

	private MerchantOrderOneGroupAdapter merchantOrderOneGroupAdapter;
	private MerchantOrderTwoGroupAdapter merchantOrderTwoGroupAdapter;

	/** 所有订单接口标识 */
	public static final int sellerOrderAction = 0;
	/** 确认接单接口标识 */
	public static final int receiveOrderAction = 1;

	private int isCut = 0;// isCut="0"查询未结算订单；isCut="1"查询待发货订单

	private int currentPage0 = 1;// 未结算当前页
	private int currentPage1 = 1;// 待发货当前页

	private boolean isAll0 = false;
	private boolean isAll1 = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_merchant_order, container, false);
		ViewUtils.inject(this, view);

		init();

		return view;
	}

	@OnClick({ R.id.tv_order_wait_payment, R.id.tv_order_wait_send, R.id.iv_home_left_top, R.id.iv_home_right_top,R.id.iv_home_right_top1 })
	void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_home_left_top:
			break;
		case R.id.iv_home_right_top1:// 消息
			intent = new Intent(getActivity(), MineMessageActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_order_wait_payment:// 未结算
			cleanAll();
			tv_order_wait_payment.setTextColor(Color.parseColor("#FDE000"));
			tv_order_wait_payment.setBackgroundResource(R.drawable.bg_order_smal_slider);
			lv_one.setVisibility(View.VISIBLE);
			lv_two.setVisibility(View.GONE);

			isCut = 0;
			currentPage0 = 1;
			isAll0 = false;
			merchantOrderOneGroupAdapter.clearFlag();
			orderHttp(currentPage0);
			break;
		case R.id.tv_order_wait_send:// 待发货
			cleanAll();
			tv_order_wait_send.setTextColor(Color.parseColor("#FDE000"));
			tv_order_wait_send.setBackgroundResource(R.drawable.bg_order_smal_slider);
			lv_one.setVisibility(View.GONE);
			lv_two.setVisibility(View.VISIBLE);

			isCut = 1;
			currentPage1 = 1;
			isAll1 = false;
			merchantOrderTwoGroupAdapter.clearFlag();
			orderHttp(currentPage1);
			break;
		default:
			break;
		}
	}

	void init() {

		orderHttp(currentPage0);

		lv_one.setOnScrollListener(this);
		lv_two.setOnScrollListener(this);

		merchantOrderOneGroupAdapter = new MerchantOrderOneGroupAdapter(groupOneList, getActivity());
		merchantOrderTwoGroupAdapter = new MerchantOrderTwoGroupAdapter(groupOneList, getActivity());
		lv_one.setAdapter(merchantOrderOneGroupAdapter);
		lv_two.setAdapter(merchantOrderTwoGroupAdapter);

//		merchantOrderTwoGroupAdapter.setonOrderScoreItemClickListenerListener(new onOrderScoreItemClickListener() {
//			@Override
//			public void onOrderScoreItemClick(View v, int position, String str) {
//				// TODO Auto-generated method stub
//				// 确认接单接口
//				receiveOrderHttp(str);
//			}
//
//			@Override
//			public void onOrderScoreItemClick(View v, int position1, int position2, float rating) {
//			}
//
//			@Override
//			public void onOrderScoreItemClick(View v, int position1,
//					int position2, float rating, String str) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

	}

	// 确认接单接口
	// http://121.46.0.219:8080/efreshapp/receiveOrderAction?secondlevelorderNo=20160201110455000002
	void receiveOrderHttp(String secondlevelorderNo) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("secondlevelorderNo", secondlevelorderNo);
		doHttp(1, MyConst.receiveOrderAction, params, receiveOrderAction);
	}

	// 获取订单数据接口
	void orderHttp(int currentPage) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addBodyParameter("currentPage", currentPage + "");
		params.addBodyParameter("isCut", isCut + "");
		doHttp(1, MyConst.sellerOrderAction, params, sellerOrderAction);
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		Gson gson;
		switch (flag) {
		case sellerOrderAction:
			gson = new Gson();
			ArrayList<OrderGroupBean> list = new ArrayList<OrderGroupBean>();
			list = gson.fromJson(jsonString, new TypeToken<List<OrderGroupBean>>() {
			}.getType());
			for (int i = 0; i < list.size(); i++) {
				groupOneList.add(list.get(i));
			}
			if (list.size() == 0) {
				if (isCut == 0 ) {
					isAll0 = true;
					if(currentPage0==1){
						showToast("没有未结算订单！");
					}else{
						showToast("加载完毕");
					}
				} else if (isCut == 1) {
					isAll1 = true;
					if(currentPage1==1){
						showToast("没有待发货订单！");
					}else{
						showToast("加载完毕");
					}
				}
			}
			if (isCut == 0) {
				merchantOrderOneGroupAdapter.updateFlag(list.size());
				merchantOrderOneGroupAdapter.notifyDataSetChanged();
			} else if (isCut == 1) {
				merchantOrderTwoGroupAdapter.updateFlag(list.size());
				merchantOrderTwoGroupAdapter.notifyDataSetChanged();
			}
			break;
		case receiveOrderAction:
			showToast("您已成功确认接单，请及时处理货物并发货哦~");
			cleanAll();
			tv_order_wait_payment.setTextColor(Color.parseColor("#ffffff"));
			tv_order_wait_payment.setBackgroundResource(R.drawable.bg_jifen_on);
			lv_one.setVisibility(View.VISIBLE);
			lv_two.setVisibility(View.GONE);

			isCut = 0;
			currentPage0 = 1;
			orderHttp(currentPage0);
			break;
		default:
			break;
		}
	}

	public void cleanAll() {
		tv_order_wait_payment.setTextColor(Color.parseColor("#FFFFFF"));
		tv_order_wait_payment.setBackgroundResource(0);
		tv_order_wait_send.setTextColor(Color.parseColor("#FFFFFF"));
		tv_order_wait_send.setBackgroundResource(0);

		groupOneList.clear();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		// 当不滚动时
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			// 判断是否滚动到底部
			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				if (isCut == 0 && !isAll0) {
					currentPage0++;
					orderHttp(currentPage0);
				} else if (isCut == 1 && !isAll1) {
					currentPage1++;
					orderHttp(currentPage1);
				}
			}
		}
	}
}
