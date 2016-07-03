package com.cnmobi.exianmall.mine.activity;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 我的资料-姓名界面
 */
public class MineInformationNameActivity extends BaseActivity {

	@ViewInject(R.id.ed_mine_info_name)
	private EditText ed_mine_info_name;// 姓名
	private String name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_info_name);
		ViewUtils.inject(this);

		setTitleText("姓名");
		setRightTextViewText("保存");
		setRightTextViewClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				name = getStr(ed_mine_info_name);
				Intent intent = new Intent(MineInformationNameActivity.this,
						MineInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("name", name);
				intent.putExtras(bundle);
				setResult(1, intent);
				// startActivity(intent);
				finish();
			}
		});
	}

}
