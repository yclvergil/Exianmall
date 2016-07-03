package com.cnmobi.exianmall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.MineJifenRuleAdapter;
import com.cnmobi.exianmall.adapter.MineWalletAdapter;
import com.cnmobi.exianmall.base.BaseFragment;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.Balance;
import com.cnmobi.exianmall.bean.Rechargerules;
import com.cnmobi.exianmall.bean.integralRule;
import com.cnmobi.exianmall.bean.subIntegralrule;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 我的积分-规则界面
 */
public class PurchaserMineJifenRuleFragment extends BaseFragment {
	@ViewInject(R.id.tv_jifen)
	TextView tv_jifen;
	@ViewInject(R.id.tv_description)
	TextView tv_description;
	@ViewInject(R.id.lv_rule)
	ListView lvRule;

	private integralRule rule;
	private List<subIntegralrule> listChild = new ArrayList<subIntegralrule>();

	/** 我的积分-积分规则接口标识 **/
	public static final int rules = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.fragment_purchaser_mine_jifenrule, container, false);
		ViewUtils.inject(this, view);
		rechargeruleHttp();
		return view;
	}

	// 积分规则接口
	void rechargeruleHttp() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("idUser",MyApplication.getInstance().getIdNumber()+"");
		doHttp(1, MyConst.integralRuleAction, params, rules);
	}

	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		Gson gson;
		switch (flag) {
		case rules:
			gson = new Gson();
			rule = gson.fromJson(jsonString, integralRule.class);
			tv_jifen.setText(rule.getUsableIntegral());
			tv_description.setText(rule.getSummary());
			for(int i=0;i<rule.getSubIntegralrule().size();i++){
				listChild.add(rule.getSubIntegralrule().get(i));
			}
			lvRule.setFocusable(false);
			lvRule.setAdapter(new MineJifenRuleAdapter(listChild, getActivity()));
			break;

		default:
			break;
		}
	}
}
