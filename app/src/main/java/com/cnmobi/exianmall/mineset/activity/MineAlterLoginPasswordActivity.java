package com.cnmobi.exianmall.mineset.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MainActivityMerchant;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.login.activity.LoginActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 修改登录密码界面
 */
public class MineAlterLoginPasswordActivity extends BaseActivity {
	@ViewInject(R.id.ed_old_password)
	private EditText ed_old_password;
	@ViewInject(R.id.ed_new_password)
	private EditText ed_new_password;
	@ViewInject(R.id.ed_new_password_check)
	private EditText ed_new_password_check;
	@ViewInject(R.id.iv_old_password_isTrue)
	private ImageView iv_new_password_isTrue;
	private String oldPassword = "";
	private String newPassword = "";
	private String newPasswordCheck = "";
	/**
	 * 修改密码接口标识
	 */
	public static final int modifyPwd = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_set_alter_password);
		ViewUtils.inject(this);

		setTitleText("修改登录密码");

	}

	@OnClick({ R.id.btn_sure_alter_password, })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_sure_alter_password:
			if (isNull(ed_old_password)) {
				showToast("请填写原密码");
				return;
			} else if (isNull(ed_new_password)) {
				showToast("请填写新密码");
				return;
			} else if (isNull(ed_new_password_check)) {
				showToast("请再次填写新密码");
				return;
			}
			if (!getStr(ed_new_password).equals(getStr(ed_new_password_check))) {
				showToast("新密码不一致");
			} else {
				updatePassWord();
			}
			break;
		default:
			break;
		}
	}

	void updatePassWord() {
		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
		params.addQueryStringParameter("oldpwd", getStr(ed_old_password));
		params.addQueryStringParameter("newpwd", getStr(ed_new_password));
		params.addQueryStringParameter("okpwd", getStr(ed_new_password_check));
		doHttp(1, MyConst.updatePassWordAction, params, modifyPwd);
	}

	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
//		Intent intent;
		switch (flag) {
		case modifyPwd:
			showToast("修改成功！！");
			clearSp();
			if (MyApplication.getInstance().getTlr_type().equals("0")) {// 0买家
				Intent intent = new Intent();
				intent.setAction(MainActivity.purchaser_finish);
				sendBroadcast(intent);
			} else {// 1卖家
				Intent intent = new Intent();
				intent.setAction(MainActivityMerchant.merchant_finish);
				sendBroadcast(intent);
			}
			Intent intent = new Intent(MineAlterLoginPasswordActivity.this, LoginActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
			
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

}
