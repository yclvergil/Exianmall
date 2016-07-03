package com.cnmobi.exianmall.mine.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.CommonAdapter;
import com.cnmobi.exianmall.adapter.ViewHolder;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.WalletDetailBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 我的钱包详细界面
 */
public class MineWalletDetailActivity extends BaseActivity {

	@ViewInject(R.id.lv_mine_wallet_detail)
	private ListView lv_mine_wallet_detail;
	private List<WalletDetailBean> walletDetailDatas = new ArrayList<WalletDetailBean>();
	private CommonAdapter<WalletDetailBean> adapter;

	/**
	 * 收支明细接口标识
	 */
	public static final int paymentDetailAction = 0;
	/**
	 * 积分列表页数
	 */
	private int page = 1;
	/**
	 * 积分列表加载完毕
	 */
	private boolean isAll = false;
	private boolean ishasData=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_wallet_detail);
		ViewUtils.inject(this);

		setTitleText("收支明细");
		getPaymentDetail();
		init();
	}

	void getPaymentDetail() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addQueryStringParameter("currentPage", page + "");
		doHttp(0, MyConst.paymentDetailAction, params, paymentDetailAction);
	}

	void init() {
		adapter = new CommonAdapter<WalletDetailBean>(getApplicationContext(), walletDetailDatas,
				R.layout.adapter_wallet_detail) {

			@Override
			public void convert(ViewHolder helper, WalletDetailBean item) {
				// paymentType：收支类型 0=购物消费；1=预充值；2=预充值赠送；3=推荐好友赠送；4=购物赠送
//				if (item.getPaymentType().equals("0")) {
//					helper.setText(R.id.tv_wallet_source, "购物消费");
//				} else if (item.getPaymentType().equals("1")) {
//					helper.setText(R.id.tv_wallet_source, "预充值");
//				} else if (item.getPaymentType().equals("2")) {
//					helper.setText(R.id.tv_wallet_source, "预充值赠送");
//				} else if (item.getPaymentType().equals("3")) {
//					helper.setText(R.id.tv_wallet_source, "推荐好友赠送");
//				} else if (item.getPaymentType().equals("4")) {
//					helper.setText(R.id.tv_wallet_source, "购物赠送");
//				}
				if (item.getPaymentType().equals("0")) {
					helper.setText(R.id.tv_wallet_source, "充值");
				} else if (item.getPaymentType().equals("1")) {
					helper.setText(R.id.tv_wallet_source, "支出");
//				} else if (item.getPaymentType().equals("2")) {
//					helper.setText(R.id.tv_wallet_source, "预充值赠送");
//				} else if (item.getPaymentType().equals("3")) {
//					helper.setText(R.id.tv_wallet_source, "推荐好友赠送");
				} else if (item.getPaymentType().equals("4")) {
					helper.setText(R.id.tv_wallet_source, "充值赠送");
				}
				
				
				helper.setText(R.id.tv_wallet_date, item.getPaymentTime());
				if (Float.valueOf(item.getPaymentAmount()) >= 0) {
					helper.setText(R.id.tv_wallet_count, "+" + item.getPaymentAmount());
				} else {
					helper.setText(R.id.tv_wallet_count, item.getPaymentAmount());
				}
			}
		};
		lv_mine_wallet_detail.setAdapter(adapter);
		lv_mine_wallet_detail.setFocusable(false);
		lv_mine_wallet_detail.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				// 当不滚动时
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 判断是否滚动到底部
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						// 加载更多功能的代码
						if (!isAll) {
							page++;
							getPaymentDetail();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
		case paymentDetailAction:
			List<WalletDetailBean> walletDetailedList = new ArrayList<WalletDetailBean>();
			walletDetailedList = new Gson().fromJson(jsonString, new TypeToken<List<WalletDetailBean>>() {
			}.getType());
			if(walletDetailedList.size()==0){
				if(!ishasData){
					showToast("您还没有收支记录哦~");
				}
			}else{
				ishasData=true;
			}
			for (int i = 0; i < walletDetailedList.size(); i++) {
				walletDetailDatas.add(walletDetailedList.get(i));
			}
			adapter.notifyDataSetChanged();
			if (walletDetailDatas.size() == 0) {
				isAll = false;
			} else {
				if (walletDetailedList.size() == 0) {
					showToast("加载完毕");
					isAll = true;
				}
			}
			break;

		default:
			break;
		}
	}

}
