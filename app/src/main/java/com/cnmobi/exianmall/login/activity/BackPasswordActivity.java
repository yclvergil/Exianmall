package com.cnmobi.exianmall.login.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.R.drawable;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.widget.BackPasswordTimeButton;
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
 * 找回密码界面
 */
public class BackPasswordActivity extends BaseActivity {

	@ViewInject(R.id.tv_common_head_2_title)
	TextView tv_common_head_2_title;
	@ViewInject(R.id.ed_backpwd_phone)
	public static EditText ed_backpwd_phone;
	@ViewInject(R.id.ed_backpwd_newpassword)
	EditText ed_backpwd_newpassword;// 新密码
	@ViewInject(R.id.ed_backpwd_agpassword)
	EditText ed_backpwd_agpassword;// 确认新密码
	@ViewInject(R.id.iv_old_password_isTrue)
	ImageView iv_old_password_isTrue;
	@ViewInject(R.id.ed_backpwd_yzm)
	EditText ed_backpwd_yzm;// 验证码输入框
	@ViewInject(R.id.btn_get_yzm1)
	BackPasswordTimeButton btn_get_yzm;// 获取验证码按钮
	@ViewInject(R.id.iv_picture_yzm)
	ImageView iv_picture_yzm;
	@ViewInject(R.id.iv_yzm_isTrue)
	ImageView iv_yzm_isTrue;
	@ViewInject(R.id.ed_picture_yzm)
	EditText ed_picture_yzm;// 图形验证码输入框
	@ViewInject(R.id.ly_prcture_yzm)
	LinearLayout ly_prcture_yzm;

	/**
	 * 注册前验证手机号接口标识
	 */
	public static final int checkPhone = 0;
//	/**
//	 * 注册获取短信验证码接口标识
//	 */
//	public static final int sendCode = 1;
	/**
	 * 找回密码接口标识
	 */
	public static final int forgetPwd = 2;

	public static  String code;
	public static  Boolean isValid = false;// 默认手机号已注册，可以获取验证码
//	public static  Boolean isPhoneTrue = false;// 默认手机号已注册，可以获取验证码
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_back_password);
		ViewUtils.inject(this);

		tv_common_head_2_title.setText("找回密码");
		ed_backpwd_phone.addTextChangedListener(watcher);
		ed_picture_yzm.addTextChangedListener(watcher1);
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (getStr(ed_backpwd_phone).length() == 11) {
				checkPhoneHttp();
			} else {
				iv_old_password_isTrue.setVisibility(View.GONE);
//				btn_get_yzm.setTextColor(getResources().getColor(
//						R.color.graytext));
				isValid = false;
				ly_prcture_yzm.setVisibility(View.GONE);
				ed_picture_yzm.setText("");
			}
		}

	};
	private TextWatcher watcher1 = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			if (getStr(ed_picture_yzm).length() == 4) {
				checkImgVerificationcode(getStr(ed_backpwd_phone),getStr(ed_picture_yzm));
				
			} else {
				iv_yzm_isTrue.setVisibility(View.GONE);
				isValid = false;
			}
		}

	};
	
	void checkImgVerificationcode(String phone,String code){
		LogUtils.i("phone"+phone+";code"+code);
		RequestParams params = new RequestParams();
		params.addBodyParameter("phone", phone);
		params.addQueryStringParameter("imageCode", code);
		HttpUtils httpUtils=new HttpUtils();
		httpUtils.send(HttpMethod.POST,MyConst.validateImageCodeAction,params,new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Log.i("json", arg0.result);
				try {
					JSONObject jsonObject = new JSONObject(arg0.result);
					int errorCode = jsonObject.getInt("code");
					if (errorCode == 0) {
						iv_yzm_isTrue.setVisibility(View.VISIBLE);
						iv_yzm_isTrue.setImageDrawable(getResources().getDrawable(drawable.ic_right));
						isValid = true;
					} else {
						MyConst.imageLoader.displayImage(MyConst.imageVerifyAction+"phone="+getStr(ed_backpwd_phone), iv_picture_yzm, MyConst.IM_IMAGE_OPTIONS_NO_CACHE);
//						MyConst.imageLoader.displayImage(MyConst.imageVerifyAction+"phone="+getStr(ed_register_name), iv_picture_yzm, MyConst.IM_IMAGE_OPTIONS);
						
						iv_yzm_isTrue.setVisibility(View.VISIBLE);
						iv_yzm_isTrue.setImageDrawable(getResources().getDrawable(drawable.ic_error));
						isValid = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
}
	
	void checkPhoneHttp() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("phone", getStr(ed_backpwd_phone));
		HttpUtils httpUtils=new HttpUtils();
//		BaseActivity.httpUtils.send(HttpMethod.POST,"http://121.46.0.219:8080/efreshapp/verifyAction?phone="+ getStr(ed_backpwd_phone),new RequestCallBack<String>() {
		httpUtils.send(HttpMethod.POST,MyConst.verifyAction,params,new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
//						showToast("网，请稍后再试");
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						Log.i("json", arg0.result);
						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							int errorCode = jsonObject.getInt("code");
							if (errorCode == 0) {
								showToast(jsonObject.getString("message"));
								iv_old_password_isTrue.setImageDrawable(getResources().getDrawable(drawable.ic_error));
								iv_old_password_isTrue.setVisibility(View.VISIBLE);
								ly_prcture_yzm.setVisibility(View.GONE);
//								btn_get_yzm.setTextColor(getResources().getColor(R.color.graytext));
//								isValid = false;
							} else if(errorCode == 2){
								showToast(jsonObject.getString("message"));
								iv_old_password_isTrue.setImageDrawable(getResources().getDrawable(drawable.ic_error));
								iv_old_password_isTrue.setVisibility(View.VISIBLE);
								ly_prcture_yzm.setVisibility(View.GONE);
//								isPhoneTrue=false;
							}else if(errorCode == 1){
								iv_old_password_isTrue.setImageDrawable(getResources().getDrawable(drawable.ic_right));
								iv_old_password_isTrue.setVisibility(View.VISIBLE);
//								btn_get_yzm.setTextColor(getResources().getColor(R.color.yzmorange));
//								isValid = true;
//								isPhoneTrue=true;
								ly_prcture_yzm.setVisibility(View.VISIBLE);
								MyConst.imageLoader.displayImage(MyConst.imageVerifyAction+"phone="+getStr(ed_backpwd_phone), iv_picture_yzm, MyConst.IM_IMAGE_OPTIONS_NO_CACHE);
								MyConst.imageLoader.clearDiskCache();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
	}

//	void sendCodeHttp() {
//		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("phone", getStr(ed_backpwd_phone));
//		doHttp(1, MyConst.sendCodeAction, params, sendCode);
//	}

	void forgetpwdHttp() {
		// http://121.46.0.219:8080/efreshapp/forgetPassWordAction?checkcode=&idNumber=1007&phone=18802093449&newpwd=123456&okpwd=123456
		RequestParams params = new RequestParams();
		params.addBodyParameter("checkcode", code);
		params.addBodyParameter("idNumber", MyApplication.getInstance().getIdNumber() + "");
		params.addBodyParameter("phone", getStr(ed_backpwd_phone));
		params.addBodyParameter("newpwd", getStr(ed_backpwd_newpassword));
		params.addBodyParameter("okpwd", getStr(ed_backpwd_agpassword));
		doHttp(1, MyConst.forgetPassWordAction, params, forgetPwd);
	}

	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
//		case sendCode:
//			// showToast(jsonString);
//			code = jsonString;
////			ed_backpwd_yzm.setText(code);
//			break;

		case forgetPwd:
			clearSp();
			Toast.makeText(BackPasswordActivity.this, "已成功修改密码，马上去登录吧~",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(BackPasswordActivity.this,
					LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@OnClick({ R.id.iv_common_head_2_return, R.id.btn_backpwd_sure,R.id.btn_exchange })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_common_head_2_return:
			finish();
			break;
		case R.id.btn_backpwd_sure:
			if (isNull(getStr(ed_backpwd_phone))
					|| isNull(getStr(ed_backpwd_yzm))
					|| isNull(getStr(ed_backpwd_newpassword))
					|| isNull(ed_backpwd_agpassword)) {
				showToast("请确保信息不为空！！");
				return;
			}
			if (!getStr(ed_backpwd_yzm).equals(code)) {
				showToast("请输入正确的短信验证码！！");
				return;
			}
			forgetpwdHttp();
			break;
//		case R.id.btn_get_yzm:
//			if (isValid) {
//				sendCodeHttp();
//			} else {
//				showToast("该手机号还未注册！！");
//				iv_old_password_isTrue.setImageDrawable(getResources()
//						.getDrawable(drawable.ic_error));
//				iv_old_password_isTrue.setVisibility(View.VISIBLE);
//			}
//			break;
		case R.id.btn_exchange:
			MyConst.imageLoader.displayImage(MyConst.imageVerifyAction+"phone="+getStr(ed_backpwd_phone), iv_picture_yzm, MyConst.IM_IMAGE_OPTIONS_NO_CACHE);
			ed_picture_yzm.setText("");
			break;
		default:
			break;
		}
	}

}
