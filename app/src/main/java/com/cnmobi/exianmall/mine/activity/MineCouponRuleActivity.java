package com.cnmobi.exianmall.mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 我的优惠券-规则界面
 */
public class MineCouponRuleActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_coupon_rule);
		setTitleText("e鲜优惠券规则");
	}

}
