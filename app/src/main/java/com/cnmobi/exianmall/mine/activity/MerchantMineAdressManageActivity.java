package com.cnmobi.exianmall.mine.activity;

import java.util.ArrayList;
import java.util.List;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.AdressManageAdapter;
import com.cnmobi.exianmall.adapter.AdressManageAdapter.onRightImageViewClickListener;
import com.cnmobi.exianmall.adapter.AdressManageAdapter.onRightItemClickListener;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.bean.AdressBean;
import com.cnmobi.exianmall.bean.MessageBean;
import com.cnmobi.exianmall.widget.SwipeListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * 商家-地址管理界面
 */
public class MerchantMineAdressManageActivity extends BaseActivity {

	@ViewInject(R.id.ly_merchant_adressmanage)
	private SwipeListView ly_merchant_adressmanage;
	
	List<AdressBean> list=new ArrayList<AdressBean>();
	AdressManageAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_merchant_adress_manage);
		ViewUtils.inject(this);
		
		init();
		setTitleText("地址管理");
	}

	
	@OnClick({ R.id.ly_common_head_return,R.id.btn_add_production_adress })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ly_common_head_return:
			finish();
			break;
		case R.id.btn_add_production_adress:
			openActivity(MerchantMineAddAdressActivity.class);
			break;
		default:
			break;
		}
	}
	
	void init() {

		for (int i = 0; i < 3; i++) {
			AdressBean bean = new AdressBean();
			bean.setName("李向东"+i);
			bean.setAdress("广东省广州市白云区生产基地"+i);
			list.add(bean);
		}
		adapter = new AdressManageAdapter(MerchantMineAdressManageActivity.this, list, ly_merchant_adressmanage.getRightViewWidth(), ly_merchant_adressmanage);
		ly_merchant_adressmanage.setAdapter(adapter);
		adapter.setOnRightItemClickListener(new onRightItemClickListener() {
			
			@Override
			public void onRightItemClick(View v, int position) {
				showToast("删除");
				list.remove(position);
				adapter.notifyDataSetChanged();
				
			}
		});
		adapter.setOnRightImageViewClickListener(new onRightImageViewClickListener() {
			
			@Override
			public void onRightImageViewClick(View v, int position) {
				Intent intent=new Intent(MerchantMineAdressManageActivity.this, MerchantMineAlterAdressActivity.class);
				startActivity(intent);
			}
		});
	}
	
}