package com.cnmobi.exianmall.mineset.activity;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


/**
 * 修改支付密码界面
 */
public class MineAlterPayPasswordActivity extends BaseActivity {

	@ViewInject(R.id.ed_old_password)
	EditText ed_old_password;
	@ViewInject(R.id.ed_new_password)
	EditText ed_new_password;
	@ViewInject(R.id.ed_new_password_check)
	EditText ed_new_password_check;
	/**
	 * 修改支付密码接口标识
	 */
	public static final int alterPayPwd=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_set_alter_password);
		ViewUtils.inject(this);

		setTitleText("修改支付密码");
	}
	
	@OnClick(R.id.btn_sure_alter_password)
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ly_common_head_return:
			finish();
			break;
		case R.id.btn_sure_alter_password:
			if(isNull(ed_old_password)){
				showToast("请填写原密码");
				return;
			}else if (isNull(ed_new_password)) {
				showToast("请填写新密码");
				return;
			}else if (isNull(ed_new_password_check)) {
				showToast("请再次填写新密码");
				return;
			}
			if(!getStr(ed_new_password).equals(getStr(ed_new_password_check))){
				showToast("新密码不一致");
			}
			else{
				alterPayPwd();
			}
			break;
		default:
			break;
		}
	}

	void alterPayPwd(){
		RequestParams params=new RequestParams();
//		params.addQueryStringParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
		params.addQueryStringParameter("oldPayPwd", getStr(ed_old_password));
		params.addQueryStringParameter("newPayPwd", getStr(ed_new_password));
		params.addQueryStringParameter("confirmPayPwd", getStr(ed_new_password_check));
		doHttp(1,MyConst.updatePaymentPwdAction, params, alterPayPwd);
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
		case alterPayPwd:
			showToast("支付密码已修改成功，请牢记哦~");
			MyApplication.getInstance().setIsSetpassword("Y");
			finish();
			break;

		default:
			break;
		}
	}
}
