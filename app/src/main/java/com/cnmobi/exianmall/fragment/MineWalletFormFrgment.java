package com.cnmobi.exianmall.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseFragment;
import com.cnmobi.exianmall.mine.activity.PreChargeActivity;
import com.cnmobi.exianmall.widget.LineChartView;
import com.cnmobi.exianmall.widget.PageViewData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的钱包-表单界面
 */
public class MineWalletFormFrgment extends BaseFragment {
	@ViewInject(R.id.lineChartView)
	LineChartView chartView;
	@ViewInject(R.id.tv_wallet_balance)
	TextView tv_balance;// 累计收益
	@ViewInject(R.id.textView3)
	TextView tv_tip;// 提示
	@ViewInject(R.id.textView6)
	TextView tv_charge;// 充值收益
	@ViewInject(R.id.tv_shopping)
	TextView tv_shopping;// 购物消费收益
	@ViewInject(R.id.tv_recommend)
	TextView tv_recommend;// 推荐收益

	private HashMap<Integer, PageViewData> mDataPageView;
	private List<Integer> moneyList = new ArrayList<Integer>();
	private List<String> dateList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine_wallet_form, container, false);
		ViewUtils.inject(this, view);

		initDataPageView();
		return view;
	}

	private void initDataPageView() {

		try {
			
			JSONObject jsonObject = new JSONObject(getArguments().getString("walletForm"));
			JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
			tv_recommend.setText(jsonObject1.get("recommendEarnings") + "");
			tv_charge.setText(jsonObject1.get("topUupBenefit") + "");
			tv_shopping.setText(jsonObject1.get("shoppingRevenue") + "");
			tv_tip.setText(jsonObject1.get("summary") + "");
			tv_balance.setText(jsonObject1.get("revenueamounts") + "");
			String accumulatedEarnings = jsonObject1.getString("accumulatedEarnings");
			JSONArray jsonArray = new JSONArray(accumulatedEarnings);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				moneyList.add((int) jsonObject2.getDouble("accumulatedEarnings"));
				dateList.add(jsonObject2.getString("ts"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (moneyList.size() > 0) {
			chartView.setVisibility(View.VISIBLE);
		} else {
			chartView.setVisibility(View.INVISIBLE);
		}

		mDataPageView = new HashMap<Integer, PageViewData>();

		for (int i = 0; i < moneyList.size(); i++) {
			mDataPageView.put(i + 1, new PageViewData(i + 1, dateList.get(i).substring(5, dateList.get(i).length()),
					moneyList.get(i)));
		}

		chartView.setDataTotal(mDataPageView);
		chartView.setPaints(Color.argb(255, 225, 250, 250), Color.argb(255, 234, 234, 250),
				Color.argb(255, 74, 208, 204), Color.argb(255, 105, 210, 249), Color.argb(255, 203, 203, 203),
				Color.argb(255, 255, 255, 255), Color.argb(255, 105, 210, 249), Color.argb(255, 105, 210, 249));
		chartView.refreshDrawableState();
		// 要锁屏，解锁才会看到图表，好奇怪
	}

	@OnClick(R.id.ly_mine_wallet_topup)
	public void preCharge(View view) {
		startActivity(new Intent(getActivity(), PreChargeActivity.class));
	}

}
