package com.cnmobi.exianmall.login.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MainActivityMerchant;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.MessageBean;
import com.cnmobi.exianmall.register.activity.RegisterActivity;
import com.cnmobi.exianmall.utils.ActivityCollector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.umeng.analytics.MobclickAgent;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.ed_login_name)
	EditText ed_user_name;
	@ViewInject(R.id.ed_login_password)
	EditText ed_user_password;
	@ViewInject(R.id.btn_login)
	Button btn_login;
	@ViewInject(R.id.iv_splash)
	ImageView iv_splash;
	@ViewInject(R.id.iv_login_bottom)
	ImageView iv_login_bottom;
	@ViewInject(R.id.ly_login)
	LinearLayout ly_login;

	/**
	 * 用户登录接口标识
	 */
	public static final int login = 0;
	/**
	 * 消息列表接口标识
	 */
	public static final int messageList = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);

		init();
	}

	@OnClick({ R.id.btn_login, R.id.btn_to_register, R.id.tv_forget_password })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_login:

			if (isNull(ed_user_name)) {
				showToast("请填写手机号码");
				return;
			}
			if (isNull(ed_user_password)) {
				showToast("请填写密码");
				return;
			}

			http(true);
			break;
		case R.id.btn_to_register:
			intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_forget_password:
			intent = new Intent(LoginActivity.this, BackPasswordActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {  
	    	MobclickAgent.onKillProcess(getApplicationContext());
	    	ActivityCollector.finishAll();
	    	System.exit(0);
            return false;  
       }else {  
           return super.onKeyDown(keyCode, event);  
       }  
	}
	
	void init() {
		ed_user_name.setText(getSp(MyConst.userName, "") + "");
		ed_user_password.setText(getSp(MyConst.userPwd, "") + "");
		// 自动登陆
		if ((Boolean) getSp(MyConst.isLogin, false)) {
			iv_splash.setVisibility(View.VISIBLE);
			ly_login.setVisibility(View.GONE);
			iv_login_bottom.setVisibility(View.GONE);
			CountDownTimer timer = new CountDownTimer(1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onFinish() {
					http(false);
				}
			}.start();
		}else{
			iv_splash.setVisibility(View.GONE);
			ly_login.setVisibility(View.VISIBLE);
			iv_login_bottom.setVisibility(View.VISIBLE);
		}
	}   

	/**
	 * 登录请求
	 */
	void http(boolean isShow) {
		if (isShow) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("phone", getStr(ed_user_name));
			params.addQueryStringParameter("passWord", getStr(ed_user_password));
			doHttp(1, MyConst.userLoginAction, params, login);
		} else {
			autoLogin();
		}
	}
	/**
	 * 免登录处理-密码错误要取消自动登录,不然一直停留在启动页面
	 */
	void autoLogin(){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("phone", getStr(ed_user_name));
		params.addQueryStringParameter("passWord", getStr(ed_user_password));
		HttpUtils httpUtils=new HttpUtils();
		httpUtils.send(HttpMethod.POST, MyConst.userLoginAction, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				showToast("服务器繁忙，请稍后再试！");
				iv_splash.setVisibility(View.GONE);
				ly_login.setVisibility(View.VISIBLE);
				iv_login_bottom.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					JSONObject jsonObject1 = new JSONObject(arg0.result);
					int errorCode = jsonObject1.getInt("code");
					LogUtils.i(arg0.result);
					if(errorCode==0){
						// 在这里统一解析json外层，成功就解析接口内容
						if (jsonObject1.has("response")) {
							try {
								JSONObject jsonObject = new JSONObject(jsonObject1.getString("response"));
								MyApplication.getInstance().setCreationtime(jsonObject.getString("creationtime"));
								MyApplication.getInstance().setPhone(jsonObject.getString("phone"));
								MyApplication.getInstance().setDeliveryaddress(jsonObject.getString("deliveryaddress"));
								MyApplication.getInstance().setExamine(jsonObject.getString("examine"));
								MyApplication.getInstance().setIdNumber(Integer.parseInt(jsonObject.getString("idNumber")));
								MyApplication.getInstance().setStallsname(jsonObject.getString("stallsname"));
								MyApplication.getInstance().setTlr_name(jsonObject.getString("tlr_name"));
								MyApplication.getInstance().setTlr_pwd(jsonObject.getString("tlr_pwd"));
								MyApplication.getInstance().setTlr_type(jsonObject.getString("tlr_type"));
								MyApplication.getInstance().setUserlogo(jsonObject.getString("userlogo"));
								MyApplication.getInstance().setIsSetpassword(jsonObject.getString("isSetPayPwd"));
								MyApplication.getInstance().setIdStore(jsonObject.getString("idStore"));
								MyApplication.getInstance().setBalance(jsonObject.getString("balance"));
								MyApplication.getInstance().setToKen(jsonObject.getString("toKen"));
								LogUtils.i("toKen:"+MyApplication.getInstance().getToKen());
								putSp(MyConst.isLogin, true);
								putSp(MyConst.userName, getStr(ed_user_name));
								putSp(MyConst.userPwd, getStr(ed_user_password));
								if(!TextUtils.isEmpty(MyApplication.getInstance().getToKen())){
									messageList();
								}else{
									showToast("登录异常，请稍后重试！");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} 
					}else{
						showToast(jsonObject1.getString("message"));
						clearSp();
						iv_splash.setVisibility(View.GONE);
						ly_login.setVisibility(View.VISIBLE);
						iv_login_bottom.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
	void messageList() {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("idUser", MyApplication.getInstance()
				.getIdNumber() + "");
		params.addQueryStringParameter("toKen", MyApplication.getInstance()
				.getToKen());// 每个接口都要传的字段可以放在这里
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, MyConst.messageListAction, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						showToast("服务器繁忙，请稍后再试！");
						iv_splash.setVisibility(View.GONE);
						ly_login.setVisibility(View.VISIBLE);
						iv_login_bottom.setVisibility(View.VISIBLE);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							int errorCode = jsonObject.getInt("code");
							if (errorCode == 0) {

								List<MessageBean> list = new ArrayList<MessageBean>();
								list = new Gson().fromJson(
										jsonObject.getString("response"),
										new TypeToken<List<MessageBean>>() {
										}.getType());
								String spFileName = MyApplication.getInstance()
										.getIdNumber() + "sysIdMessage";
								SharedPreferences sharedPreferences = getSharedPreferences(
										spFileName, Context.MODE_PRIVATE
												+ Context.MODE_PRIVATE);
								Iterator it;
								String str = "";
								// list.get(0).setIdMessage(1222211111);
								if (list.size() != 0) {

									// 后台不能够判断系统消息是否已读
									// ---把保存在SharedPreferences里面的已读的系统消息id跟消息列表接口的系统消息id想对比
									for (int i = 0; i < list.size(); i++) {
										if ("0".equals(list.get(i).getMsgtype())
												|| "1".equals(list.get(i)
														.getMsgtype())) {
											it = sharedPreferences
													.getStringSet(
															"idMessage",
															new HashSet<String>())
													.iterator();
											while (it.hasNext()) {
												str = (String) it.next();
												if ((list.get(i).getIdMessage() + "")
														.equals(str)) {
													list.get(i).setIsRead("Y");
												}
											}
										}
									}
									MyApplication.getInstance()
											.setMessage(list);
								}
								if (MyApplication.getInstance().getTlr_type()
										.equals("0")) {// 0买家
									Intent intent = new Intent(
											LoginActivity.this,
											MainActivity.class);
									startActivity(intent);
									finish();
								} else if (MyApplication.getInstance()
										.getTlr_type().equals("1")) {// 1卖家
									Intent intent = new Intent(
											LoginActivity.this,
											MainActivityMerchant.class);
									startActivity(intent);
									finish();
								}

							} else {
								LogUtils.i(arg0.result);
								if (MyApplication.getInstance().getTlr_type()
										.equals("0")) {// 0买家
									Intent intent = new Intent(
											LoginActivity.this,
											MainActivity.class);
									startActivity(intent);
									finish();
								} else if (MyApplication.getInstance()
										.getTlr_type().equals("1")) {// 1卖家
									Intent intent = new Intent(
											LoginActivity.this,
											MainActivityMerchant.class);
									startActivity(intent);
									finish();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		Intent intent;
		switch (flag) {
		case login:
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				MyApplication.getInstance().setCreationtime(jsonObject.getString("creationtime"));
				MyApplication.getInstance().setPhone(jsonObject.getString("phone"));
				MyApplication.getInstance().setDeliveryaddress(jsonObject.getString("deliveryaddress"));
				MyApplication.getInstance().setExamine(jsonObject.getString("examine"));
				MyApplication.getInstance().setIdNumber(Integer.parseInt(jsonObject.getString("idNumber")));
				MyApplication.getInstance().setStallsname(jsonObject.getString("stallsname"));
				MyApplication.getInstance().setTlr_name(jsonObject.getString("tlr_name"));
				MyApplication.getInstance().setTlr_pwd(jsonObject.getString("tlr_pwd"));
				MyApplication.getInstance().setTlr_type(jsonObject.getString("tlr_type"));
				MyApplication.getInstance().setUserlogo(jsonObject.getString("userlogo"));
				MyApplication.getInstance().setIsSetpassword(jsonObject.getString("isSetPayPwd"));
				MyApplication.getInstance().setIdStore(jsonObject.getString("idStore"));
				MyApplication.getInstance().setBalance(jsonObject.getString("balance"));
				MyApplication.getInstance().setToKen(jsonObject.getString("toKen"));
				LogUtils.i("toKen:"+MyApplication.getInstance().getToKen());
				putSp(MyConst.isLogin, true);
				putSp(MyConst.userName, getStr(ed_user_name));
				putSp(MyConst.userPwd, getStr(ed_user_password));
				if(!TextUtils.isEmpty(MyApplication.getInstance().getToKen())){
					messageList();
				}else{
					showToast("登录异常，请稍后重试！");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			break;

		case messageList:
			List<MessageBean> list = new ArrayList<MessageBean>();
			list=new Gson().fromJson(jsonString,new TypeToken<List<MessageBean>>() {
			}.getType());
			String spFileName=MyApplication.getInstance().getIdNumber()+"sysIdMessage";
			SharedPreferences sharedPreferences = getSharedPreferences(spFileName,
					Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
			Iterator it;
			String str="";
//			list.get(0).setIdMessage(1222211111);
			//后台不能够判断系统消息是否已读     ---把保存在SharedPreferences里面的已读的系统消息id跟消息列表接口的系统消息id想对比
			if(list.size()!=0){
				
				for(int i=0;i<list.size();i++){
					if("0".equals(list.get(i).getMsgtype())||"1".equals(list.get(i).getMsgtype())){
						it=sharedPreferences.getStringSet("idMessage", new HashSet<String>()).iterator();
						while (it.hasNext()) {
							str = (String) it.next();
							if ((list.get(i).getIdMessage() + "").equals(str)) {
								list.get(i).setIsRead("Y");
							}
						}
					}}
				MyApplication.getInstance().setMessage(list);
			}
			if (MyApplication.getInstance().getTlr_type().equals("0")) {// 0买家
				intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			} else if (MyApplication.getInstance().getTlr_type().equals("1")) {// 1卖家
				intent = new Intent(LoginActivity.this, MainActivityMerchant.class);
				startActivity(intent);
				finish();
			}
			break;

		default:
			break;
		}
	}
	
}
