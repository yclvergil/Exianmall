package com.cnmobi.exianmall.mine.activity;

import android.os.Bundle;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;

/**
 * 银联支付
 *
 */
public class PayBanActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_pay_ban);
		setTitleText("添加银行卡");
		ViewUtils.inject(this);
	}

}
