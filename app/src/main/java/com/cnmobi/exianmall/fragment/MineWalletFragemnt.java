package com.cnmobi.exianmall.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.MineWalletAdapter;
import com.cnmobi.exianmall.base.BaseFragment;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.Balance;
import com.cnmobi.exianmall.bean.OrderGroupBean;
import com.cnmobi.exianmall.bean.Rechargerules;
import com.cnmobi.exianmall.mine.activity.PreChargeActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的钱包-钱包界面
 */
public class MineWalletFragemnt extends BaseFragment {
	@ViewInject(R.id.tv_wallet_balance)
	TextView tv_wallet_balance;
	@ViewInject(R.id.tv_description)
	TextView tv_description;
	@ViewInject(R.id.lv_rule)
	ListView lvRule;
	
	private Balance balance;
	private List<Rechargerules> listChild = new ArrayList<Rechargerules>();
	
	/**
	 * 钱包-充值规则接口标识
	 */
	public static final int rechargerules=0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine_wallet_wallet, container, false);
		ViewUtils.inject(this, view);
		
		rechargeruleHttp();
		
		return view;
	}

	@OnClick(R.id.ly_mine_wallet_topup)
	public void preCharge(View view) {
		Intent intent = new Intent(getActivity(), PreChargeActivity.class);
		intent.putExtra("idAccount", balance.getIdAccount());
		startActivityForResult(intent, 0);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==0 && resultCode==1){
			rechargeruleHttp();
		}
	}
	
	//充值规则接口
	void rechargeruleHttp(){
		RequestParams params=new RequestParams();
		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber()+"");
		doHttp(1, MyConst.rechargeruleAction, params, rechargerules);
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		Gson gson;
		switch (flag) {
		case rechargerules:
			gson=new Gson();
			String balances;
			balance=gson.fromJson(jsonString,Balance.class);
			if("".equals(balance.getBalance())){
				balances="0";
			}else{
				balances=balance.getBalance();
			}
			tv_wallet_balance.setText(String.format("%.2f", Double.valueOf(balances)));
			tv_description.setText(balance.getSummary());
			listChild.clear();
			for(int i=0;i<balance.getRechargerules().size();i++){
				listChild.add(balance.getRechargerules().get(i));
			}
			lvRule.setAdapter(new MineWalletAdapter(listChild, getActivity()));
			break;

		default:
			break;
		}
	}

}
