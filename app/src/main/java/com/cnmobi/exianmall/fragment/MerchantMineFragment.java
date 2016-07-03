package com.cnmobi.exianmall.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.R.drawable;
import com.cnmobi.exianmall.base.BaseFragment;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.base.MainActivityMerchant.MyBroadcast;
import com.cnmobi.exianmall.mine.activity.MerchantMineAlterAdressActivity;
import com.cnmobi.exianmall.mine.activity.MerchantMineInformationActivity;
import com.cnmobi.exianmall.mine.activity.MineMessageActivity;
import com.cnmobi.exianmall.mine.activity.MineOldOrderActivity;
import com.cnmobi.exianmall.mine.activity.MineProductionPlaceActivity;
import com.cnmobi.exianmall.mine.activity.MineSetActivity;
import com.cnmobi.exianmall.widget.RoundImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 卖家-我界面
 */
public class MerchantMineFragment extends BaseFragment {

	@ViewInject(R.id.imageView2)
	RoundImageView iv_head;
	@ViewInject(R.id.textView2)
	TextView tv_titlename;
	@ViewInject(R.id.textView3)
	TextView tv_titlephone;
	@ViewInject(R.id.iv_heads)
	ImageView iv_heads;
	MyBroadcast mybroadcast;
	public static String updates_heads = "updates_heads";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_merchant_mine, container, false);
		ViewUtils.inject(this, view);
		init();
		
		mybroadcast = new MyBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(updates_heads);
		getActivity().registerReceiver(mybroadcast, filter); // 注册BroadcastReceiver
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(mybroadcast);// 取消注册
	}
	
	public class MyBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到广播，更新头像
			LogUtils.i("跟新头像");
			MyConst.imageLoader.displayImage(MyApplication.getInstance().getUserlogo(), iv_heads, MyConst.IM_IMAGE_OPTIONS);
			
		}
	}
	void init() {
		if (!"".equals(MyApplication.getInstance().getUserlogo())) {
			MyConst.imageLoader.displayImage(MyApplication.getInstance().getUserlogo(), iv_heads, MyConst.IM_IMAGE_OPTIONS);
		}else{
			iv_heads.setImageResource(drawable.logo);
		}
		tv_titlename.setText(MyApplication.getInstance().getStallsname());
		tv_titlephone.setText(MyApplication.getInstance().getPhone());
	}

	@OnClick({ R.id.iv_home_left_top, R.id.iv_mine_edit, R.id.rl_mine_message, R.id.rl_mine_adress_manage,
			R.id.rl_mine_productionplace, R.id.rl_mine_history_order, R.id.rl_mine_set })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_home_left_top:
			break;
		case R.id.iv_mine_edit:
			intent = new Intent(getActivity(), MerchantMineInformationActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_message:
			intent = new Intent(getActivity(), MineMessageActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_set:
			intent = new Intent(getActivity(), MineSetActivity.class);
			intent.putExtra("userRole", "merchant");
			startActivity(intent);
			break;
		case R.id.rl_mine_adress_manage:
//			intent = new Intent(getActivity(), MerchantMineAdressManageActivity.class);
			intent = new Intent(getActivity(), MerchantMineAlterAdressActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_productionplace:
			intent = new Intent(getActivity(), MineProductionPlaceActivity.class);
			intent.putExtra("idStore", MyApplication.getInstance().getIdNumber());
			startActivity(intent);
			break;
		case R.id.rl_mine_history_order:
			intent = new Intent(getActivity(), MineOldOrderActivity.class);
			intent.putExtra("userRole", "merchant");
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
