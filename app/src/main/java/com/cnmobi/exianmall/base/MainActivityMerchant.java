package com.cnmobi.exianmall.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.appmanagement.CnmobiAppManagement;
import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.R.color;
import com.cnmobi.exianmall.R.drawable;
import com.cnmobi.exianmall.fragment.MerchantHomeFragment;
import com.cnmobi.exianmall.fragment.MerchantMineFragment;
import com.cnmobi.exianmall.fragment.MerchantOrderFragment;
import com.cnmobi.exianmall.mine.activity.MerchantMineAlterAdressActivity;
import com.cnmobi.exianmall.mine.activity.MineMessageActivity;
import com.cnmobi.exianmall.mine.activity.MineOldOrderActivity;
import com.cnmobi.exianmall.mine.activity.MineProductionPlaceActivity;
import com.cnmobi.exianmall.mine.activity.MineSetActivity;
import com.cnmobi.exianmall.utils.ActivityCollector;
import com.cnmobi.exianmall.utils.Util;
import com.cnmobi.exianmall.widget.DragLayout;
import com.cnmobi.exianmall.widget.DragLayout.DragListener;
//import com.cnmobi.exianmall.widget.DragLayout.OnLayoutDragingListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

/**
 * 卖家主界面
 * @author peng24
 *
 */
public class MainActivityMerchant extends FragmentActivity{

	@ViewInject(R.id.dl_merchant)
	private DragLayout dl;
	@ViewInject(R.id.m_iv_merchant)
	private ImageView m_iv;
	@ViewInject(R.id.ly_merchant_guide_home)
	private LinearLayout ly_merchant_guide_home;
	@ViewInject(R.id.ly_merchant_guide_order)
	private LinearLayout ly_merchant_guide_order;
	@ViewInject(R.id.ly_merchant_guide_mine)
	private LinearLayout ly_merchant_guide_mine;
	@ViewInject(R.id.fl_merchant_content)
	private FrameLayout fl_merchant_content;
	@ViewInject(R.id.iv_merchant_guide_home)
	private ImageView iv_merchant_guide_home;
	@ViewInject(R.id.tv_merchant_guide_home)
	private TextView tv_merchant_guide_home;
	@ViewInject(R.id.iv_merchant_guide_order)
	private ImageView iv_merchant_guide_order;
	@ViewInject(R.id.tv_merchant_guide_order)
	private TextView tv_merchant_guide_order;
	@ViewInject(R.id.iv_merchant_guide_mine)
	private ImageView iv_merchant_guide_mine;
	@ViewInject(R.id.tv_merchant_guide_mine)
	private TextView tv_merchant_guide_mine;
	@ViewInject(R.id.iv_heads)
	private ImageView iv_heads;
	@ViewInject(R.id.iv_main_red_point)
	private ImageView iv_main_red_point;
	@ViewInject(R.id.iv_slidemenu_red_point)
	private ImageView iv_slidemenu_red_point;
	@ViewInject(R.id.tv_stallsname)
	private TextView tv_stallsname;
	@ViewInject(R.id.tv_phone)
	private TextView tv_phone;
	/** 卖家首页 **/
	private MerchantHomeFragment merchantHomeFragment;
	/** 卖家订单 **/
	private MerchantOrderFragment merchantOrderFragment;
	/** 卖家我**/
	private MerchantMineFragment merchantMineFragment;
	MyBroadcast mybroadcast;
	public static String update_heads = "update_heads";
	MyBroadcast1 mybroadcast1;
	MyBroadcast2 mybroadcast2;
	public static String update_red_points = "update_red_points";
	public static String merchant_finish = "merchant_finish";
	private int dlFlag=-1;//侧滑菜单状态标记  0=open 1=close
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_merchant);
		ViewUtils.inject(this);
		Util.initImageLoader(this);
		initDragLayout();
		init();
		// 初始化默认显示的界面
		if (merchantHomeFragment == null) {
			merchantHomeFragment = new MerchantHomeFragment();
			addFragment(merchantHomeFragment);
			showFragment(merchantHomeFragment);
		} else {
			showFragment(merchantHomeFragment);
		}
		
		
		mybroadcast = new MyBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(update_heads);
		registerReceiver(mybroadcast, filter); // 注册BroadcastReceiver
		mybroadcast1 = new MyBroadcast1();
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction(update_red_points);
		registerReceiver(mybroadcast1, filter1); // 注册BroadcastReceiver
		mybroadcast2 = new MyBroadcast2();
		IntentFilter filter2= new IntentFilter();
		filter2.addAction(merchant_finish);
		registerReceiver(mybroadcast2, filter2); // 注册BroadcastReceiver
		new CnmobiAppManagement("exianmall", this);
		
		
	}
	
	
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
	    	if(dlFlag==0){
	    		//如果侧滑菜单open则close掉
        		dl.close();
        	}else{
        		if((System.currentTimeMillis()-exitTime) > 2000){  
        			Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
        			exitTime = System.currentTimeMillis();   
        		} else {
        			LayoutInflater inflaterDl = LayoutInflater.from(getApplicationContext());
					RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
							.inflate(R.layout.dialog_clean_message, null);
					Builder builder1 = new AlertDialog.Builder(MainActivityMerchant.this);
					builder1.setView(relativeLayout);
					final Dialog dialog1 = builder1.create();
					dialog1.show();
					TextView tv_content=(TextView) relativeLayout.findViewById(R.id.tv_content);
					tv_content.setText("确定退出e鲜商城？");
					Button btn_ok=(Button) relativeLayout.findViewById(R.id.btn_clean_message_ok);
					Button btn_cancel=(Button) relativeLayout.findViewById(R.id.btn_clean_message_cancel);
					btn_cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog1.dismiss();
						}
					});
					btn_ok.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog1.dismiss();
							//finish所有activity，退出应用
							//友盟统计。。
							MobclickAgent.onKillProcess(getApplicationContext());
							ActivityCollector.finishAll();
//		        			finish();
		        			System.exit(0);
						}
					});
        		}
        	}
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mybroadcast);// 取消注册
		unregisterReceiver(mybroadcast1);// 取消注册
		unregisterReceiver(mybroadcast2);// 取消注册
	}
	
	public class MyBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到广播，更新头像
			LogUtils.i("跟新头像");
			MyConst.imageLoader.displayImage(MyApplication.getInstance().getUserlogo(), iv_heads, MyConst.IM_IMAGE_OPTIONS);
			MyConst.imageLoader.displayImage(MyApplication.getInstance().getUserlogo(), m_iv, MyConst.IM_IMAGE_OPTIONS);
		}
	}
	
	public class MyBroadcast1 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到广播，隐藏小红点
			LogUtils.e("onReceive");
			iv_main_red_point.setVisibility(View.GONE);
			iv_slidemenu_red_point.setVisibility(View.GONE);
		}
	}
	
	public class MyBroadcast2 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到广播，finish掉主界面
			finish();
		}
	}   
	void init() {
//		if(MyApplication.getInstance().isReadedMessage()){
//			iv_main_red_point.setVisibility(View.VISIBLE);
//			iv_slidemenu_red_point.setVisibility(View.VISIBLE);
//		}else{
////			if((MyApplication.getInstance().isReadedSysMessage())){
////				iv_main_red_point.setVisibility(View.VISIBLE);
////				iv_slidemenu_red_point.setVisibility(View.VISIBLE);
////			}else{
//				iv_main_red_point.setVisibility(View.GONE);
//				iv_slidemenu_red_point.setVisibility(View.GONE);
////			}
//			
//		}
	if(MyApplication.getInstance().getMessage()!=null){
			
			if(MyApplication.getInstance().isReadedMessage()){
				iv_main_red_point.setVisibility(View.VISIBLE);
				iv_slidemenu_red_point.setVisibility(View.VISIBLE);
				LogUtils.e(">>>>>>>>>1");
			}else{
//			if((MyApplication.getInstance().isReadedSysMessage())){
//				iv_main_red_point.setVisibility(View.VISIBLE);
//				iv_slidemenu_red_point.setVisibility(View.VISIBLE);
//				LogUtils.e(">>>>>>>>>2");
//			}else{
				iv_main_red_point.setVisibility(View.GONE);
				iv_slidemenu_red_point.setVisibility(View.GONE);
//			}
				
			}
		}else{
			iv_main_red_point.setVisibility(View.GONE);
			iv_slidemenu_red_point.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(MyApplication.getInstance().getUserlogo())) {
			LogUtils.i("zheli");
			MyConst.imageLoader.displayImage(MyApplication.getInstance()
					.getUserlogo(), iv_heads, MyConst.IM_IMAGE_OPTIONS);
			MyConst.imageLoader.displayImage(MyApplication.getInstance()
					.getUserlogo(), m_iv, MyConst.IM_IMAGE_OPTIONS);
		} else {
			iv_heads.setImageResource(drawable.logo);
			m_iv.setImageResource(drawable.logo);
		}
		tv_stallsname.setText(MyApplication.getInstance().getStallsname());
		if (MyApplication.getInstance().getPhone() != null) {
			tv_phone.setText(MyApplication.replaceIndex(5, 8, MyApplication.getInstance().getPhone(), "*"));
		}
		m_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dl.open();
			}
		});
	}
	
	private void initDragLayout() {
		dl.setDragListener(new DragListener() {
			@Override
			public void onOpen() {
				
			}

			@Override
			public void onClose() {
			}

			@Override
			public void onDrag(float percent) {
				ViewHelper.setAlpha(m_iv, 1 - percent);
				ViewHelper.setAlpha(iv_main_red_point, 1 - percent);
			}

		});
	}


	@OnClick({ R.id.ly_merchant_guide_home, R.id.ly_merchant_guide_order,
			R.id.ly_merchant_guide_mine, R.id.ly_slidemenu_mine_merchant,
			R.id.rl_slidemenu_message_merchant,
			R.id.rl_slidemenu_history_order_merchant,
			R.id.rl_slidemenu_adress_manage_merchant,
			R.id.rl_slidemenu_adress_production_merchant,
			R.id.rl_slidemenu_set_merchant })
	public void onClick(View v){
		Intent intent;
		switch (v.getId()) {
		case R.id.ly_merchant_guide_home:
			cleanAll();
			iv_merchant_guide_home.setImageResource(R.drawable.btn_guide_home_on);
			tv_merchant_guide_home.setTextColor(Color.parseColor("#B7A315"));
			
			if (merchantHomeFragment == null) {
				merchantHomeFragment = new MerchantHomeFragment();
				// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
				if (!merchantHomeFragment.isHidden()) {
					addFragment(merchantHomeFragment);
					showFragment(merchantHomeFragment);
				}
			} else {
				if (merchantHomeFragment.isHidden()) {
					showFragment(merchantHomeFragment);
				}
			}
			break;
		case R.id.ly_merchant_guide_order:
			cleanAll();
			iv_merchant_guide_order.setImageResource(R.drawable.btn_guide_order_on);
			tv_merchant_guide_order.setTextColor(Color.parseColor("#B7A315"));
			
			if (merchantOrderFragment == null) {
				merchantOrderFragment = new MerchantOrderFragment();
				// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
				if (!merchantOrderFragment.isHidden()) {
					addFragment(merchantOrderFragment);
					showFragment(merchantOrderFragment);
				}
			} else {
				if (merchantOrderFragment.isHidden()) {
					showFragment(merchantOrderFragment);
				}
			}
			break;
		case R.id.ly_merchant_guide_mine:
			cleanAll();
			iv_merchant_guide_mine.setImageResource(R.drawable.btn_guide_mine_on);
			tv_merchant_guide_mine.setTextColor(Color.parseColor("#B7A315"));
			
			if (merchantMineFragment == null) {
				merchantMineFragment = new MerchantMineFragment();
				// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
				if (!merchantMineFragment.isHidden()) {
					addFragment(merchantMineFragment);
					showFragment(merchantMineFragment);
				}
			} else {
				if (merchantMineFragment.isHidden()) {
					showFragment(merchantMineFragment);
				}
			}
			break;
		case R.id.ly_slidemenu_mine_merchant:
			cleanAll();
			iv_merchant_guide_mine.setImageResource(R.drawable.btn_guide_mine_on);
			tv_merchant_guide_mine.setTextColor(Color.parseColor("#B7A315"));
//			slideMenu.scrollTo(0, 0);
			if (merchantMineFragment == null) {
				merchantMineFragment = new MerchantMineFragment();
				// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
				if (!merchantMineFragment.isHidden()) {
					addFragment(merchantMineFragment);
					showFragment(merchantMineFragment);
				}
			} else {
				if (merchantMineFragment.isHidden()) {
					showFragment(merchantMineFragment);
				}
			}
			dl.close();
			break;
		case R.id.rl_slidemenu_message_merchant://消息
			intent = new Intent(MainActivityMerchant.this, MineMessageActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_slidemenu_history_order_merchant://历史订单
			intent = new Intent(MainActivityMerchant.this, MineOldOrderActivity.class);
			intent.putExtra("userRole", "merchant");
			startActivity(intent);
			break;
		case R.id.rl_slidemenu_adress_manage_merchant://地址管理
//			intent = new Intent(MainActivityMerchant.this, MerchantMineAdressManageActivity.class);
			intent = new Intent(MainActivityMerchant.this, MerchantMineAlterAdressActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_slidemenu_adress_production_merchant://产地
			intent = new Intent(MainActivityMerchant.this,MineProductionPlaceActivity.class);
			intent.putExtra("idStore", MyApplication.getInstance().getIdNumber());
			startActivity(intent);
			break;
		case R.id.rl_slidemenu_set_merchant://设置
			intent = new Intent(MainActivityMerchant.this, MineSetActivity.class);
			intent.putExtra("userRole", "merchant");
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	/** 添加fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fl_merchant_content, fragment);
		ft.commit();
	}

	/** 显示fragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();

		// 判断页面是否已经创建，如果已经创建，那么就隐藏掉
		if (merchantHomeFragment != null) {
			ft.hide(merchantHomeFragment);
		}
		if (merchantOrderFragment != null) {
			ft.hide(merchantOrderFragment);
		}
		if (merchantMineFragment != null) {
			ft.hide(merchantMineFragment);
		}

		ft.show(fragment);
		ft.commitAllowingStateLoss();
	}
	
	public void cleanAll() {
		MyApplication.isOnShopCar = false;
		iv_merchant_guide_home.setImageResource(R.drawable.btn_guide_home_normal);
		tv_merchant_guide_home.setTextColor(Color.parseColor("#FFFFFF"));
		iv_merchant_guide_order.setImageResource(R.drawable.btn_guide_order_normal);
		tv_merchant_guide_order.setTextColor(Color.parseColor("#FFFFFF"));
		iv_merchant_guide_mine.setImageResource(R.drawable.btn_guide_mine_normal);
		tv_merchant_guide_mine.setTextColor(Color.parseColor("#FFFFFF"));
	}
}
