package com.cnmobi.exianmall.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 我的资料-手机号码界面
 */
public class MineInformationPhoneNumberActivity extends BaseActivity {
	
	@ViewInject(R.id.ed_mine_info_phonenumber)
	private EditText ed_mine_info_phonenumber;// 手机号码
	private String phoneNumber = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_info_phonenumber);
		ViewUtils.inject(this);

		setTitleText("手机号码");
		setRightTextViewText("保存");
		setRightTextViewClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				phoneNumber =getStr(ed_mine_info_phonenumber); 
				Intent intent = new Intent(
						MineInformationPhoneNumberActivity.this,
						MineInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("phoneNumber", phoneNumber);
				intent.putExtras(bundle);
				setResult(2, intent);
				// startActivity(intent);
				finish();
			}
		});
	}

}
