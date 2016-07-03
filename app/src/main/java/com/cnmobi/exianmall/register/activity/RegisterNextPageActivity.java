package com.cnmobi.exianmall.register.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.login.activity.BackPasswordActivity;
import com.cnmobi.exianmall.login.activity.LoginActivity;
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
 * 下一步注册界面
 */
public class RegisterNextPageActivity extends BaseActivity {

	@ViewInject(R.id.cb_agree)
	CheckBox cb_agree;
	@ViewInject(R.id.ed_register_password)
	EditText ed_register_password;
	@ViewInject(R.id.ed_register_password_check)
	EditText ed_register_password_check;
	@ViewInject(R.id.rl_stallsname)
	RelativeLayout rl_stallsname;
	@ViewInject(R.id.rl_name)
	RelativeLayout rl_name;
	@ViewInject(R.id.view_white)
	View view_white;
	@ViewInject(R.id.ed_stallsname)
	EditText ed_stallsname;
	@ViewInject(R.id.ed_name)
	EditText ed_name;
	private Boolean iSAgree = true;
	private String type = "";// 用户类型 0买家 1商家
	/**
	 * 注册获接口标识
	 */
	public static final int register = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_nextpage);
		ViewUtils.inject(this);
		
		type=getIntent().getStringExtra("type");
		if(TextUtils.equals(type, "0")){
			rl_stallsname.setVisibility(View.VISIBLE);
			rl_name.setVisibility(View.VISIBLE);
			view_white.setVisibility(View.GONE);
		}else{
			rl_stallsname.setVisibility(View.GONE);
			rl_name.setVisibility(View.GONE);
			view_white.setVisibility(View.VISIBLE);
		}
		cb_agree.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					iSAgree = false;
				}
				if (!isChecked) {
					iSAgree = true;
				}
			}
		});
	}

//	void registerActionHttp() {
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("code", getIntent().getStringExtra("code"));
//		params.addBodyParameter("type", getIntent().getStringExtra("type"));
//		params.addBodyParameter("phone", getIntent().getStringExtra("phone"));
//		params.addBodyParameter("password", getStr(ed_register_password));
//		params.addBodyParameter("confimPassword", getStr(ed_register_password_check));
//		params.addBodyParameter("province", getIntent().getStringExtra("province"));
//		params.addBodyParameter("city", getIntent().getStringExtra("city"));
//		params.addBodyParameter("area", getIntent().getStringExtra("area"));
//		params.addBodyParameter("address", getIntent().getStringExtra("address"));
//		params.addBodyParameter("agree", 0 + "");// 是否同意（易鲜商城注册协议）0同意 1不同意
//		LogUtils.e("" + getIntent().getStringExtra("code") + ";"
//				+ getIntent().getStringExtra("type") + ";"
//				+ getIntent().getStringExtra("phone") + ";"
//				+ getStr(ed_register_password) + ";"
//				+ getStr(ed_register_password_check) + ";"
//				+ getIntent().getStringExtra("province") + ";"
//				+ getIntent().getStringExtra("city")+";"+getIntent().getStringExtra("area")+";"+getIntent().getStringExtra("address")+";");
//		doHttp(1, MyConst.registerAction, params, register);
//	}

	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		Intent intent;
		switch (flag) {
//		case register:
//			showToast("注册成功！！");
//			intent = new Intent(RegisterNextPageActivity.this, LoginActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
//			startActivity(intent);
//			finish();
//			break;
		default:
			break;
		}
	}

	@OnClick({ R.id.tv_agreement, R.id.btn_register, R.id.iv_common_head_2_return })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.tv_agreement:
			intent = new Intent(RegisterNextPageActivity.this, RegisterAgreementActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_register:
			if (iSAgree) {
				if(type=="0"){
					if(isNull(ed_stallsname)||isNull(ed_name)||isNull(ed_register_password) || isNull(ed_register_password_check)){
						showToast("请确保信息不为空！！");
					}
				}else{
					if (isNull(ed_register_password) || isNull(ed_register_password_check)) {
						showToast("请确保信息不为空！！");
						return;
					}
				}
				if (!getStr(ed_register_password_check).equals(getStr(ed_register_password))) {
					showToast("新密码不一致！！");
					return;
				}
				RequestParams params = new RequestParams();
				params.addBodyParameter("code", getIntent().getStringExtra("code"));
				params.addBodyParameter("type", type);
				params.addBodyParameter("phone", getIntent().getStringExtra("phone"));
				params.addBodyParameter("password", getStr(ed_register_password));
				params.addBodyParameter("confimPassword", getStr(ed_register_password_check));
				params.addBodyParameter("province", getIntent().getStringExtra("province"));
				params.addBodyParameter("city", getIntent().getStringExtra("city"));
				params.addBodyParameter("area", getIntent().getStringExtra("area"));
				params.addBodyParameter("address", getIntent().getStringExtra("address"));
				params.addBodyParameter("agree", 0 + "");// 是否同意（易鲜商城注册协议）0同意 1不同意
				if(TextUtils.equals("0", type)){
					//买家增加了两个参数stallsname  档口名称 name  姓名
					params.addBodyParameter("stallsname", getStr(ed_stallsname));
					params.addBodyParameter("name", getStr(ed_name));
				}
				HttpUtils httpUtils=new HttpUtils();
				httpUtils.send(HttpMethod.POST, MyConst.registerAction, params,new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						
						
						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							int errorCode = jsonObject.getInt("code");
							if (errorCode == 0) {
								showToast("注册成功！！");
								Intent intent = new Intent(RegisterNextPageActivity.this, LoginActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
								startActivity(intent);
								finish();
							} else {
								
								// 错误就统一弹出错误信息
								Toast.makeText(RegisterNextPageActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						
					}
				});
				

			} else {
				Toast.makeText(RegisterNextPageActivity.this, "请先同意e鲜商城协议", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.iv_common_head_2_return:
			finish();
		default:
			break;
		}

	}
}
