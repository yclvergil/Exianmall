package com.cnmobi.exianmall.register.activity;

import android.os.Bundle;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;

/**
 * 注册协议界面
 */
public class RegisterAgreementActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_register_agreement);
		ViewUtils.inject(this);

		setTitleText("注册协议");
	}

}
