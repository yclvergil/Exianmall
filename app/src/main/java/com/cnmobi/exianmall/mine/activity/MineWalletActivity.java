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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.fragment.MineWalletFormFrgment;
import com.cnmobi.exianmall.fragment.MineWalletFragemnt;
import com.cnmobi.exianmall.utils.ActivityCollector;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的钱包界面
 */
public class MineWalletActivity extends FragmentActivity {

	@ViewInject(R.id.ly_common_head_3_return)
	private LinearLayout ly_common_head_3_return;//返回
	@ViewInject(R.id.tv_common_head_3_lefttitle)
	private TextView tv_common_head_3_lefttitle;//钱包
	@ViewInject(R.id.tv_common_head_3_righttitle)
	private TextView tv_common_head_3_righttitle;//报表
	@ViewInject(R.id.iv_common_head_3_details)
	private ImageView iv_common_head_3_details;//收支明细
	@ViewInject(R.id.fl_mine_wallet)
	private FrameLayout fl_mine_wallet;
	
	String walletForm="";
	/**钱包界面**/
	private MineWalletFragemnt mineWalletFragemnt;
	/**报表界面**/
	private MineWalletFormFrgment mineWalletFormFrgment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mine_wallet);
		ViewUtils.inject(this);
		
		tv_common_head_3_lefttitle.setText("钱包");
		tv_common_head_3_righttitle.setText("收益");
		// 初始化默认显示的界面
		if (mineWalletFragemnt == null) {
			mineWalletFragemnt = new MineWalletFragemnt();
			addFragment(mineWalletFragemnt);
			showFragment(mineWalletFragemnt);
		} else {
			showFragment(mineWalletFragemnt);
		}
	}
	
	
	@OnClick({ R.id.ly_common_head_3_return, R.id.tv_common_head_3_lefttitle,
			R.id.tv_common_head_3_righttitle, R.id.iv_common_head_3_details })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ly_common_head_3_return:
			finish();
			break;
		case R.id.tv_common_head_3_lefttitle:
			cleanAll();
			tv_common_head_3_lefttitle.setTextColor(Color.parseColor("#FDE000"));
			tv_common_head_3_lefttitle.setBackgroundResource(R.drawable.bg_order_smal_slider);
			// 钱包界面
			if (mineWalletFragemnt == null) {
				mineWalletFragemnt = new MineWalletFragemnt();
				// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
				
				addFragment(mineWalletFragemnt);
				showFragment(mineWalletFragemnt);
			} else {
				if (mineWalletFragemnt.isHidden()) {
					
					showFragment(mineWalletFragemnt);
				}
			}
			break;
		case R.id.tv_common_head_3_righttitle:
			cleanAll();
			tv_common_head_3_righttitle.setTextColor(Color.parseColor("#FDE000"));
			tv_common_head_3_righttitle.setBackgroundResource(R.drawable.bg_order_smal_slider);
			// 报表界面
			// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
			if (mineWalletFormFrgment == null) {
				
				mineWalletFormFrgment = new MineWalletFormFrgment();
				RequestParams params = new RequestParams();
				params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
				params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
				params.addBodyParameter("toKen",MyApplication.getInstance().getToKen());
				HttpUtils httpUtils=new HttpUtils();
//				httpUtils.configCookieStore(MyCookieStore.cookieStore);
//				BaseActivity.httpUtils.send(HttpMethod.POST, MyConst.earningsReportAction, params, new RequestCallBack<String>() {
				httpUtils.send(HttpMethod.POST, MyConst.earningsReportAction, params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
							LogUtils.i(arg0.result);
							Bundle bundle=new Bundle();
							bundle.putString("walletForm", arg0.result);
							mineWalletFormFrgment.setArguments(bundle);
							addFragment(mineWalletFormFrgment);
							showFragment(mineWalletFormFrgment);
						}
				});
			} else {
				if (mineWalletFormFrgment.isHidden()) {
					showFragment(mineWalletFormFrgment);
				}
			}
			break;
		case R.id.iv_common_head_3_details:
			intent=new Intent(MineWalletActivity.this, MineWalletDetailActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	public void cleanAll(){
		tv_common_head_3_lefttitle.setBackgroundResource(0);
		tv_common_head_3_righttitle.setBackgroundResource(0);
		tv_common_head_3_lefttitle.setTextColor(Color.parseColor("#FFFFFF"));
		tv_common_head_3_righttitle.setTextColor(Color.parseColor("#FFFFFF"));
	}
	
	/**添加fragment**/
	public void addFragment(Fragment fragment){
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fl_mine_wallet, fragment);
		ft.commit();
	}
	
	/**删除fragment**/
	public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}
	
	/**显示fragment**/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		
		// 判断页面是否已经创建，如果已经创建，那么就隐藏掉
		if(mineWalletFragemnt!=null){
			ft.hide(mineWalletFragemnt);
		}
		if(mineWalletFormFrgment!=null){
			ft.hide(mineWalletFormFrgment);
		}   
		
		ft.show(fragment);
		ft.commitAllowingStateLoss();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}
}
