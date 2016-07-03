package com.cnmobi.exianmall.mine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.fragment.PurchaserMineJifenExchangeFragment;
import com.cnmobi.exianmall.fragment.PurchaserMineJifenRuleFragment;
import com.cnmobi.exianmall.utils.ActivityCollector;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的积分界面
 */
public class MineJiFenActivity extends FragmentActivity {

	@ViewInject(R.id.tv_common_head_3_lefttitle)
	private TextView tv_common_head_3_lefttitle;// 积分规则
	@ViewInject(R.id.tv_common_head_3_righttitle)
	private TextView tv_common_head_3_righttitle;// 积分兑换
	@ViewInject(R.id.ly_big_bg)
	LinearLayout ly_big_bg;
	@ViewInject(R.id.fl_mine_content)
	private FrameLayout fl_mine_content;

	/** 积分规则界面 **/
	private PurchaserMineJifenRuleFragment jifenRuleFragment;
	/** 积分兑换界面 **/
	private PurchaserMineJifenExchangeFragment jifenExchangeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mine_jifen);
		ViewUtils.inject(this);

		// 初始化默认显示的界面
		if (jifenRuleFragment == null) {
			jifenRuleFragment = new PurchaserMineJifenRuleFragment();
			addFragment(jifenRuleFragment);
			showFragment(jifenRuleFragment);
		} else {
			showFragment(jifenRuleFragment);
		}
		ly_big_bg.setBackgroundResource(0);
		tv_common_head_3_righttitle.setVisibility(View.GONE);
		tv_common_head_3_lefttitle.setBackgroundResource(0);
		tv_common_head_3_lefttitle.setTextColor(Color.parseColor("#FFFFFF"));
	}
	@OnClick({ R.id.ly_base_back, R.id.tv_common_head_3_lefttitle,
			R.id.tv_common_head_3_righttitle, R.id.iv_common_head_3_details })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ly_base_back:
			finish();
			break;
		case R.id.tv_common_head_3_lefttitle:
//			cleanAll();
//			tv_common_head_3_lefttitle
//					.setTextColor(Color.parseColor("#ffffff"));
//			tv_common_head_3_lefttitle
//					.setBackgroundResource(R.drawable.bg_jifen_on);
//			// 积分规则界面
//			if (jifenRuleFragment == null) {
//				jifenRuleFragment = new PurchaserMineJifenRuleFragment();
//				// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
//				addFragment(jifenRuleFragment);
//				showFragment(jifenRuleFragment);
//			} else {
//				if (jifenRuleFragment.isHidden()) {
//					showFragment(jifenRuleFragment);
//				}
//			}
			break;
		case R.id.tv_common_head_3_righttitle:
			cleanAll();
			tv_common_head_3_righttitle.setTextColor(Color
					.parseColor("#ffffff"));
			tv_common_head_3_righttitle
					.setBackgroundResource(R.drawable.bg_jifen_on);
			// 积分兑换界面
			if (jifenExchangeFragment == null) {
				jifenExchangeFragment = new PurchaserMineJifenExchangeFragment();
				// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
				addFragment(jifenExchangeFragment);
				showFragment(jifenExchangeFragment);
			} else {
				if (jifenExchangeFragment.isHidden()) {
					showFragment(jifenExchangeFragment);
				}
			}
			break;
		case R.id.iv_common_head_3_details:
			intent = new Intent(MineJiFenActivity.this,
					MineJifenDetailActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void cleanAll() {
		tv_common_head_3_lefttitle.setTextColor(Color.parseColor("#323232"));
		tv_common_head_3_lefttitle
				.setBackgroundResource(0);
		tv_common_head_3_righttitle.setTextColor(Color.parseColor("#323232"));
		tv_common_head_3_righttitle
				.setBackgroundResource(0);
	}

	/** 添加fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.add(R.id.fl_mine_content, fragment);
		ft.commit();
	}

	/** 删除fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}

	/** 显示fragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();

		// 判断页面是否已经创建，如果已经创建，那么就隐藏掉
		if (jifenRuleFragment != null) {
			ft.hide(jifenRuleFragment);
		}
		if (jifenExchangeFragment != null) {
			ft.hide(jifenExchangeFragment);
		}

		ft.show(fragment);
		ft.commitAllowingStateLoss();
	}

}
