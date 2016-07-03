package com.cnmobi.exianmall.mine.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MainActivityMerchant;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.fragment.PurchaserCarFragment;
import com.cnmobi.exianmall.login.activity.LoginActivity;
import com.cnmobi.exianmall.mineset.activity.MineAlterLoginPasswordActivity;
import com.cnmobi.exianmall.mineset.activity.MineAlterPayPasswordActivity;
import com.cnmobi.exianmall.mineset.activity.MineSetAboutActivity;
import com.cnmobi.exianmall.mineset.activity.MineSetFeedBackActivity;
import com.cnmobi.exianmall.mineset.activity.MineSetPayPasswordActivity;
import com.cnmobi.exianmall.utils.DataCleanManager;
import com.cnmobi.exianmall.utils.ImageUtils;
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
 * 我的设置界面
 */
public class MineSetActivity extends BaseActivity {

	@ViewInject(R.id.rl_mine_set_alter_paypassword)
	private RelativeLayout rl_mine_set_alter_paypassword;
	@ViewInject(R.id.tv_alter_paypwd)
	TextView tv_alter_paypwd;
	@ViewInject(R.id.tv_clean_cache)
	TextView tv_clean_cache;
	@ViewInject(R.id.tv_version_name)
	TextView tv_version_name;
	
	private String userRole = "";
	String cacheSize="";

	/*
	 * 退出登录接口标识
	 */
	public static final int userLogoutAction=0;
	/*
	 * 检查更新接口标识
	 */
	public static final int checkUpDate=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_set);
		ViewUtils.inject(this);

		userRole = getIntent().getStringExtra("userRole");
		setTitleText("设置");
		try {
			cacheSize=DataCleanManager.getCacheSize(getApplicationContext().getExternalCacheDir());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		tv_clean_cache.setText(cacheSize);
		tv_version_name.setText("版本号:"+new AppVersionManager(MineSetActivity.this).getAppVersionName());
	}
	
	void checkUpDate(){
		RequestParams params=new RequestParams();
		doHttp(0, MyConst.newVersion, params,checkUpDate);
		
	}
//	void loginOut(){
//		RequestParams params=new RequestParams();
//		doHttp(0, MyConst.userLogoutAction, params, userLogoutAction);
//	}
	
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
		case checkUpDate:
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				if(!TextUtils.isEmpty(jsonObject.getString("version")) && !TextUtils.equals(jsonObject.getString("version"), new AppVersionManager(MineSetActivity.this).getAppVersionName())){
					new AppVersionManager(MineSetActivity.this).upDateAppVersion(jsonObject.getString("location"),jsonObject.getString("versionDesc"));
				}else{
					LayoutInflater inflaterDl = LayoutInflater.from(getApplicationContext());
				RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
							.inflate(R.layout.dialog_check_noupdate, null);
					Builder builder1 = new AlertDialog.Builder(MineSetActivity.this);
					builder1.setView(relativeLayout);
					final Dialog dialog1 = builder1.create();
					dialog1.show();
					Button btnOk=(Button) relativeLayout.findViewById(R.id.btn_no_update_ok);
					btnOk.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog1.dismiss();
						}
					});
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if ("merchant".equals(userRole)) {
			rl_mine_set_alter_paypassword.setVisibility(View.GONE);
		}else if("Y".equals(MyApplication.getInstance().getIsSetpassword())){
			tv_alter_paypwd.setText("修改支付密码");
		}else{
			tv_alter_paypwd.setText("设置支付密码");
		}
	}
	
	@OnClick({ R.id.ly_common_head_return, R.id.rl_mine_set_about, R.id.rl_mine_set_feedback,
			R.id.rl_mine_set_servicer, R.id.rl_mine_set_twodimensioncode, R.id.rl_mine_set_alter_loginpassword,
			R.id.rl_mine_set_alter_paypassword, R.id.rl_mine_set_exitlogin ,R.id.rl_mine_set_clean_cache,R.id.rl_mine_check_update})
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ly_common_head_return:
			finish();
			break;
		case R.id.rl_mine_set_about:// 关于e鲜
			intent = new Intent(MineSetActivity.this, MineSetAboutActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_set_feedback:// 意见反馈
			intent = new Intent(MineSetActivity.this, MineSetFeedBackActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_mine_set_servicer:// 联系客服
			LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
			LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_servicer, null);
			Builder builder = new AlertDialog.Builder(this);
			builder.setView(linearLayout);
			final Dialog dialog = builder.create();
			dialog.show();
			LinearLayout ly_online_leavemessage = (LinearLayout) linearLayout.findViewById(R.id.ly_online_leavemessage);
			ly_online_leavemessage.setOnClickListener(new OnClickListener() {// 在线留言

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(MineSetActivity.this, OnlineMessage.class);
							startActivity(intent);
							dialog.dismiss();
						}
					});
			LinearLayout ly_call = (LinearLayout) linearLayout.findViewById(R.id.ly_call);
			ly_call.setOnClickListener(new OnClickListener() {// 拨打电话
								
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "020-85269257"));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			});
			break;
		case R.id.rl_mine_set_twodimensioncode:// 生成二维码
			View view = LayoutInflater.from(MineSetActivity.this).inflate(R.layout.dialog_qrcode, null);
			ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
			ImageView iv_qrcode = (ImageView) view.findViewById(R.id.iv_qrcode);
			Button btn_save = (Button) view.findViewById(R.id.btn_save);
			final Bitmap bitmap = ImageUtils.createQRImage("生成的文字内容");
			iv_qrcode.setImageBitmap(bitmap);
			Builder builder2 = new Builder(MineSetActivity.this);
			builder2.setView(view);
			final Dialog dialog2 = builder2.create();
			dialog2.show();
			iv_close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog2.dismiss();
				}
			});
			btn_save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ImageUtils.SaveBitmap(bitmap, "qrcode.png");
					dialog2.dismiss();
					showToast("保存成功");
				}
			});

			break;
		case R.id.rl_mine_set_alter_loginpassword:// 修改登录密码
			intent = new Intent(MineSetActivity.this, MineAlterLoginPasswordActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl_mine_set_alter_paypassword:// 设置支付密码
			// 这里先判断是否有设置过支付密码
			if ("Y".equals(MyApplication.getInstance().getIsSetpassword())) {
				intent = new Intent(MineSetActivity.this, MineAlterPayPasswordActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(MineSetActivity.this, MineSetPayPasswordActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.rl_mine_check_update://检查更新
			checkUpDate();
			break;
		case R.id.rl_mine_set_exitlogin:// 退出当前账户
			clearSp();
			if (MyApplication.getInstance().getTlr_type().equals("0")) {// 0买家
				Intent intent1 = new Intent();
				intent1.setAction(MainActivity.purchaser_finish);
				sendBroadcast(intent1);
			} else {// 1卖家
				Intent intent1 = new Intent();
				intent1.setAction(MainActivityMerchant.merchant_finish);
				sendBroadcast(intent1);
			}
			
			intent = new Intent(MineSetActivity.this,LoginActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
			startActivity(intent);
			finish();
			break;
		case R.id.rl_mine_set_clean_cache:
			DataCleanManager.cleanInternalCache(getApplicationContext()) ;
			DataCleanManager.cleanExternalCache(getApplicationContext());
			try {
				 cacheSize=DataCleanManager.getCacheSize(getApplicationContext().getExternalCacheDir());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			tv_clean_cache.setText(cacheSize);
			break;
		default:
			break;
		}

	}
	
	
}
