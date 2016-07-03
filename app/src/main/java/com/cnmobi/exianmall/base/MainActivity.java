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
import com.cnmobi.exianmall.fragment.PurchaserCarFragment;
import com.cnmobi.exianmall.fragment.PurchaserHomeFragment;
import com.cnmobi.exianmall.fragment.PurchaserOrderFragment;
import com.cnmobi.exianmall.fragment.PurchaserTypeFragment;
import com.cnmobi.exianmall.mine.activity.MineAlterAdressActivity;
import com.cnmobi.exianmall.mine.activity.MineCouponActivity;
import com.cnmobi.exianmall.mine.activity.MineInformationActivity;
import com.cnmobi.exianmall.mine.activity.MineJiFenActivity;
import com.cnmobi.exianmall.mine.activity.MineMessageActivity;
import com.cnmobi.exianmall.mine.activity.MineOldOrderActivity;
import com.cnmobi.exianmall.mine.activity.MineSetActivity;
import com.cnmobi.exianmall.mine.activity.MineWalletActivity;
import com.cnmobi.exianmall.type.activity.ProductDetailActivity;
import com.cnmobi.exianmall.utils.ActivityCollector;
import com.cnmobi.exianmall.utils.Util;
import com.cnmobi.exianmall.widget.DragLayout;
import com.cnmobi.exianmall.widget.DragLayout.DragListener;
import com.cnmobi.exianmall.widget.RoundImageView;
//import com.cnmobi.exianmall.widget.DragLayout.OnLayoutDragingListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

/**
 * 主界面
 */
public class MainActivity extends FragmentActivity {

	@ViewInject(R.id.dl)
	public static  DragLayout dl;
	@ViewInject(R.id.m_iv)
	private RoundImageView m_iv;
	@ViewInject(R.id.ly_guide_home)
	private LinearLayout ly_guide_home;
	@ViewInject(R.id.ly_guide_type)
	private LinearLayout ly_guide_type;
	@ViewInject(R.id.ly_guide_order)
	private LinearLayout ly_guide_order;
	@ViewInject(R.id.ly_guide_car)
	private LinearLayout ly_guide_car;
	@ViewInject(R.id.fl_content)
	private FrameLayout fl_content;
	@ViewInject(R.id.iv_guide_home)
	private ImageView iv_guide_home;
	@ViewInject(R.id.iv_guide_type)
	private ImageView iv_guide_type;
	@ViewInject(R.id.iv_guide_order)
	private ImageView iv_guide_order;
	@ViewInject(R.id.iv_guide_car)
	private ImageView iv_guide_car;
	@ViewInject(R.id.iv_main_red_point)
	private ImageView iv_main_red_point;
	@ViewInject(R.id.iv_slidemenu_red_point)
	private ImageView iv_slidemenu_red_point;
	@ViewInject(R.id.tv_guide_home)
	private TextView tv_guide_home;
	@ViewInject(R.id.tv_guide_type)
	private TextView tv_guide_type;
	@ViewInject(R.id.tv_guide_order)
	private TextView tv_guide_order;
	@ViewInject(R.id.tv_guide_car)
	private TextView tv_guide_car;
	@ViewInject(R.id.iv_heads)
	private ImageView iv_heads;
	@ViewInject(R.id.tv_stallsname)
	private TextView tv_stallsname;
	@ViewInject(R.id.tv_phone)
	private TextView tv_phone;
	@ViewInject(R.id.rl_slidemenu_coupon)
	private RelativeLayout rl_slidemenu_coupon;
	

	/** 主界面 **/
	private PurchaserHomeFragment purchaserHomeFragment;
	/** 分类界面 **/
	private PurchaserTypeFragment purchaserTypeFragment;
	/** 订单界面 **/
	private PurchaserOrderFragment purchaserOrderFragment;
	/** 购物车界面 **/
	private PurchaserCarFragment purchaserCarFragment;

	MyBroadcast mybroadcast;
	MyBroadcast1 mybroadcast1;
	MyBroadcast2 mybroadcast2;  
	MyBroadcast3 mybroadcast3;
	public static String update_head = "update_head";
	public static String update_red_point = "update_red_point";
	public static String purchaser_finish = "purchaser_finish";
	public static String canopen_cehua = "canopen_cehua";
	private int dlFlag=-1;//侧滑菜单状态标记  0=open 1=close
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
//		Util.initImageLoader(this);
		initDragLayout();
		init();
		if("toOrder".equals(getIntent().getStringExtra("flag"))){
			
			if (purchaserOrderFragment == null) {
				purchaserOrderFragment = new PurchaserOrderFragment();
				addFragment(purchaserOrderFragment);
			}
			cleanAll();
			iv_guide_order.setImageResource(R.drawable.btn_guide_order_on);
			tv_guide_order.setTextColor(Color.parseColor("#B7A315"));
			showFragment(purchaserOrderFragment);
		}else if("toShopCar".equals(getIntent().getStringExtra("flag"))){
			
			if (purchaserCarFragment == null) {
				purchaserCarFragment = new PurchaserCarFragment();
				addFragment(purchaserCarFragment);
			}
			cleanAll();
			iv_guide_car.setImageResource(R.drawable.btn_guide_car_on);
			tv_guide_car.setTextColor(Color.parseColor("#B7A315"));
			showFragment(purchaserCarFragment);
		}
		else{
			// 初始化默认显示的界面
			if (purchaserHomeFragment == null) {
				purchaserHomeFragment = new PurchaserHomeFragment();
				addFragment(purchaserHomeFragment);
			}
			showFragment(purchaserHomeFragment);
		}
		
		mybroadcast = new MyBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(update_head);
		registerReceiver(mybroadcast, filter); // 注册BroadcastReceiver
		
		mybroadcast1 = new MyBroadcast1();
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction(update_red_point);
		registerReceiver(mybroadcast1, filter1); // 注册BroadcastReceiver
		
		mybroadcast2 = new MyBroadcast2();
		IntentFilter filter2= new IntentFilter();
		filter2.addAction(purchaser_finish);
		registerReceiver(mybroadcast2, filter2); // 注册BroadcastReceiver
		
		mybroadcast3 = new MyBroadcast3();
		IntentFilter filter3= new IntentFilter();
		filter3.addAction(canopen_cehua);
		registerReceiver(mybroadcast3, filter3); // 注册BroadcastReceiver
		
		
		new CnmobiAppManagement("exianmall", this);
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
	    	//如果侧滑菜单open则close掉
	    	if(dlFlag==0){
        		dl.close();
        	}else{
        		if((System.currentTimeMillis()-exitTime) > 2000){  
        			Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
        			exitTime = System.currentTimeMillis();   
        		} else {
        			LayoutInflater inflaterDl = LayoutInflater.from(getApplicationContext());
					RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
							.inflate(R.layout.dialog_clean_message, null);
					Builder builder1 = new AlertDialog.Builder(MainActivity.this);
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
		unregisterReceiver(mybroadcast3);// 取消注册
	}
	
	public class MyBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到广播，更新头像
			LogUtils.i("更新头像");
//			if(MyApplication.getInstance().getUserlogo()!=null){
				MyConst.imageLoader.displayImage(MyApplication.getInstance().getUserlogo(), iv_heads, MyConst.IM_IMAGE_OPTIONS);
				MyConst.imageLoader.displayImage(MyApplication.getInstance().getUserlogo(), m_iv, MyConst.IM_IMAGE_OPTIONS);
//			}
		}
	}
	
	public class MyBroadcast1 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到广播，隐藏小红点
			LogUtils.i("收到广播，隐藏小红点");
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
	public class MyBroadcast3 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
//			boolean isture=  getIntent().getBooleanExtra("isture", true);
//		   dl.setDrag(true);
			LogUtils.d(""+intent.getStringExtra("isture"));
			if(intent.getStringExtra("isture").equals("canmove")){
				dl.setDrag(true);
			}else if(intent.getStringExtra("isture").equals("cannotmove")){
				dl.setDrag(false);
			}
			
		}
	} 
	
	
	
	private void initDragLayout() {
		dl.setDragListener(new DragListener() {
			@Override
			public void onOpen() {
				dlFlag=0;
			}

			@Override
			public void onClose() {
				dlFlag=1;
			}

			@Override
			public void onDrag(float percent) {
				dlFlag=-1;
				ViewHelper.setAlpha(m_iv, 1 - percent);
				ViewHelper.setAlpha(iv_main_red_point, 1 - percent);
			}

		});
	}

	@OnClick({ R.id.ly_guide_home, R.id.ly_guide_type, R.id.ly_guide_order, R.id.ly_guide_car, R.id.ly_slidemenu_mine,
			R.id.rl_slidemenu_jifen, R.id.rl_slidemenu_coupon, R.id.rl_slidemenu_wallet, R.id.rl_slidemenu_message,
			R.id.rl_slidemenu_history_order, R.id.rl_slidemenu_adress_manage, R.id.rl_slidemenu_set })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ly_guide_home:// --------------首页
			cleanAll();
			iv_guide_home.setImageResource(R.drawable.btn_guide_home_on);
			tv_guide_home.setTextColor(Color.parseColor("#B7A315"));
			// 主界面
			if (purchaserHomeFragment == null) {
				purchaserHomeFragment = new PurchaserHomeFragment();
				// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
				addFragment(purchaserHomeFragment);
			}
			showFragment(purchaserHomeFragment);
//			purchaserHomeFragment.onResume();// 由于show不会触发fragment
			break;
		case R.id.ly_guide_type:// --------------分类
			cleanAll();
			iv_guide_type.setImageResource(R.drawable.btn_guide_type_on);
			tv_guide_type.setTextColor(Color.parseColor("#B7A315"));
			// 分类界面
			if (purchaserTypeFragment == null) {
				purchaserTypeFragment = new PurchaserTypeFragment();
				addFragment(purchaserTypeFragment);
			}
			showFragment(purchaserTypeFragment);
//			purchaserTypeFragment.onResume();// 由于show不会触发fragment
			break;
		case R.id.ly_guide_order:// --------------订单
			cleanAll();
			iv_guide_order.setImageResource(R.drawable.btn_guide_order_on);
			tv_guide_order.setTextColor(Color.parseColor("#B7A315"));
			// 订单界面
			if (purchaserOrderFragment == null) {
				purchaserOrderFragment = new PurchaserOrderFragment();
				addFragment(purchaserOrderFragment);
			}
			showFragment(purchaserOrderFragment);
//			purchaserOrderFragment.onResume();// 由于show不会触发fragment
			break;
		case R.id.ly_guide_car:// --------------购物车
			cleanAll();
			MyApplication.isOnShopCar = true;
			iv_guide_car.setImageResource(R.drawable.btn_guide_car_on);
			tv_guide_car.setTextColor(Color.parseColor("#B7A315"));
			// 购物车界面
			if (purchaserCarFragment == null) {
				purchaserCarFragment = new PurchaserCarFragment();
				addFragment(purchaserCarFragment);
			}else{
//			purchaserCarFragment.onResume();
			}
			showFragment(purchaserCarFragment);
//			purchaserCarFragment.onResume();// 由于show不会触发fragment
			break;
		case R.id.ly_slidemenu_mine:// 侧滑--头像
			// intent = new Intent(MainActivity.this, MineActivity.class);
			// startActivity(intent);
			intent = new Intent(MainActivity.this, MineInformationActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_slidemenu_jifen:// 侧滑--积分
			intent = new Intent(MainActivity.this, MineJiFenActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_slidemenu_coupon:// 侧滑--优惠券
			intent = new Intent(MainActivity.this, MineCouponActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_slidemenu_wallet:// 侧滑--钱包
			intent = new Intent(MainActivity.this, MineWalletActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_slidemenu_message:// 侧滑--消息
			intent = new Intent(MainActivity.this, MineMessageActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_slidemenu_history_order:// 侧滑--历史订单
			intent = new Intent(MainActivity.this, MineOldOrderActivity.class);
			intent.putExtra("userRole", "purchaser");
			startActivity(intent);
			break;
		case R.id.rl_slidemenu_adress_manage:// 侧滑--地址管理
			// "Y" 是否审核通过，Y为通过，N为没通过,未通过不能进地址管理界面
//			if ("Y".equals(MyApplication.getInstance().getExamine())
//					| "".equals(MyApplication.getInstance().getExamine())) {}
				intent = new Intent(MainActivity.this, MineAlterAdressActivity.class);
				startActivity(intent);
//			} else {
//				Toast.makeText(MainActivity.this, "审核还未通过，请耐心等待哦~", Toast.LENGTH_SHORT).show();
//
//			}
			break;
		case R.id.rl_slidemenu_set:// 侧滑--设置
			intent = new Intent(MainActivity.this, MineSetActivity.class);
			intent.putExtra("userRole", "purchaser");
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	void init() {
		// 等数据
		if(MyApplication.getInstance().getMessage()!=null){
			
			if(MyApplication.getInstance().isReadedMessage()){
				iv_main_red_point.setVisibility(View.VISIBLE);
				iv_slidemenu_red_point.setVisibility(View.VISIBLE);
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
		if(!TextUtils.isEmpty(MyApplication.getInstance().getUserlogo())){
			LogUtils.e("userlog"+MyApplication.getInstance().getUserlogo());
			MyConst.imageLoader.displayImage(MyApplication.getInstance().getUserlogo(), iv_heads, MyConst.IM_IMAGE_OPTIONS);
			MyConst.imageLoader.displayImage(MyApplication.getInstance().getUserlogo(), m_iv, MyConst.IM_IMAGE_OPTIONS);
		}else{
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

		rl_slidemenu_coupon.setVisibility(View.GONE);// 优惠券隐藏（暂时不需要此功能）
	}

	/** 添加fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fl_content, fragment);
		ft.commit();
	}

	/** 显示fragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();

		// 判断页面是否已经创建，如果已经创建，那么就隐藏掉
		if (purchaserHomeFragment != null) {
			ft.hide(purchaserHomeFragment);
		}
		if (purchaserTypeFragment != null) {
			ft.hide(purchaserTypeFragment);
		}
		if (purchaserOrderFragment != null) {
			ft.hide(purchaserOrderFragment);
		}
		if (purchaserCarFragment != null) {
			ft.hide(purchaserCarFragment);
		}
		// 多个fragment只是隐藏掉了
		// show的时候不会触发onresume以至于不能在onresume里面更新Adapter这里通过接口或其他方法强制更新
		ft.show(fragment);
		ft.commitAllowingStateLoss();
	}

	public void cleanAll() {
		MyApplication.isOnShopCar = false;
		iv_guide_home.setImageResource(R.drawable.btn_guide_home_normal);
		tv_guide_home.setTextColor(Color.parseColor("#FFFFFF"));
		iv_guide_type.setImageResource(R.drawable.btn_guide_type_normal);
		tv_guide_type.setTextColor(Color.parseColor("#FFFFFF"));
		iv_guide_order.setImageResource(R.drawable.btn_guide_order_normal);
		tv_guide_order.setTextColor(Color.parseColor("#FFFFFF"));
		iv_guide_car.setImageResource(R.drawable.btn_guide_car_normal);
		tv_guide_car.setTextColor(Color.parseColor("#FFFFFF"));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 2) {
			purchaserOrderFragment.onActivityResult(requestCode, resultCode, data);
		} else if (requestCode == 103 && resultCode == 101) {
			purchaserCarFragment.onActivityResult(requestCode, resultCode, data);
		}
	}

}
