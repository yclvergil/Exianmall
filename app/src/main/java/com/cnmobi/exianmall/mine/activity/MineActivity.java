package com.cnmobi.exianmall.mine.activity;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.fragment.PurchaserMineJifenExchangeFragment;
import com.cnmobi.exianmall.fragment.PurchaserMineJifenRuleFragment;
import com.cnmobi.exianmall.utils.ActivityCollector;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 我的主界面
 */
public class MineActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mine);
		ViewUtils.inject(this);
		
	}

	@OnClick({ R.id.ly_common_head_return, R.id.iv_mine_edit,
			R.id.ly_mine_wanshang, R.id.rl_mine_jifen, R.id.rl_mine_coupon,
			R.id.rl_mine_coupon, R.id.rl_mine_message, R.id.rl_mine_wallet,
			R.id.rl_mine_history_order, R.id.rl_mine_adress_manage,
			R.id.rl_mine_set })
	
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ly_common_head_return:
			finish();
			break;
		case R.id.iv_mine_edit:
			intent = new Intent(MineActivity.this, MineInformationActivity.class);
			startActivity(intent);
			break;
		case R.id.ly_mine_wanshang:
			intent = new Intent(MineActivity.this, MineInformationActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_jifen:
			intent = new Intent(MineActivity.this, MineJiFenActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_coupon:
			intent = new Intent(MineActivity.this, MineCouponActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_wallet:
			intent = new Intent(MineActivity.this, MineWalletActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_message:
			intent = new Intent(MineActivity.this, MineMessageActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_history_order:
			intent = new Intent(MineActivity.this, MineOldOrderActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_adress_manage:
			intent = new Intent(MineActivity.this, MineAdressManageActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_set:
			intent = new Intent(MineActivity.this, MineSetActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
