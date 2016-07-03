package com.cnmobi.exianmall.base;

import com.cnmobi.exianmall.login.activity.LoginActivity;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_splash);
		SharedPreferences sharedPreferences = getSharedPreferences(
				"eXianIsFirstIn", Context.MODE_PRIVATE+ Context.MODE_PRIVATE);//第一个参数存储的文件名称,第二个参数存储方式
		boolean isFirstIn = sharedPreferences.getBoolean("isFirstIn", true);//获取isFirshtIn  若为null 则返回true

		if (isFirstIn) {
			Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}
	

}
