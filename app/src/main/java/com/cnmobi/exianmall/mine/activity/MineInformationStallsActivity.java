package com.cnmobi.exianmall.mine.activity;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 我的资料档口名称界面
 */
public class MineInformationStallsActivity extends BaseActivity {

	@ViewInject(R.id.ed_mine_stallsname)
	private EditText ed_mine_stallsname;// 档口名称
	private String stallsName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_stalls_name);
		ViewUtils.inject(this);

		setTitleText("档口名称");
		setRightTextViewText("保存");
		setRightTextViewClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stallsName =getStr(ed_mine_stallsname);  
				Intent intent = new Intent(MineInformationStallsActivity.this,
						MineInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("stallsName", stallsName);
				intent.putExtras(bundle);
				setResult(0, intent);
				// startActivity(intent);
				finish();
			}
		});
	}

}
