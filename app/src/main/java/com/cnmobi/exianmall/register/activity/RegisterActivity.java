package com.cnmobi.exianmall.register.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.R.drawable;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.widget.CityPicker;
import com.cnmobi.exianmall.widget.CityPicker.OnSelectingListener;
import com.cnmobi.exianmall.widget.RegisterTimeButton;
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
 * 注册界面
 */
public class RegisterActivity extends BaseActivity {

	@ViewInject(R.id.tv_im_businesses)
	private TextView tv_im_businesses;
	@ViewInject(R.id.tv_im_purchaser)
	private TextView tv_im_purchaser;
	@ViewInject(R.id.tv_register_address)
	private TextView tv_register_address;
	@ViewInject(R.id.ed_register_detailaddress)
	private EditText ed_register_detailaddress;
	@ViewInject(R.id.rl_select_diqu)
	private RelativeLayout rl_select_diqu;
	@ViewInject(R.id.btn_register_next)
	private Button btn_register_next;
	@ViewInject(R.id.iv_return_register)
	private ImageView iv_return_register;
	@ViewInject(R.id.ed_register_name)
	public static EditText ed_register_name;// 手机号
	@ViewInject(R.id.iv_old_password_isTrue)
	ImageView iv_old_password_isTrue;
	@ViewInject(R.id.ed_register_yzm)
	EditText ed_register_yzm;// 验证码输入框
	@ViewInject(R.id.btn_get_yzm)
	RegisterTimeButton btn_get_yzm;// 获取验证码按钮
	@ViewInject(R.id.iv_picture_yzm)
	ImageView iv_picture_yzm;
	@ViewInject(R.id.iv_yzm_isTrue)
	ImageView iv_yzm_isTrue;
	@ViewInject(R.id.ed_picture_yzm)
	EditText ed_picture_yzm;// 图形验证码输入框
	@ViewInject(R.id.ly_prcture_yzm)
	LinearLayout ly_prcture_yzm;
	private static String cityInfo = "";// 选择的城市信息

	/**
	 * 注册前验证手机号接口标识
	 */
	public static final int checkPhone = 0;
	/**
	 * 注册获取短信验证码接口标识
	 */
	public static final int sendCode = 1;

	private int type = 0;// 用户类型 0买家 1商家
	public static  String code;
	public static Boolean isValid = false;
	
	private String province;
	private String city;
	private String area;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ViewUtils.inject(this);
		ed_register_name.addTextChangedListener(watcher);
		ed_picture_yzm.addTextChangedListener(watcher1);
		
	}   

	private TextWatcher watcher = new TextWatcher() {

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
			if (getStr(ed_register_name).length() == 11) {
				checkPhoneHttp();
			} else {
				isValid=false;
				ly_prcture_yzm.setVisibility(View.GONE);
				iv_old_password_isTrue.setVisibility(View.GONE);
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
				checkImgVerificationcode(getStr(ed_register_name),getStr(ed_picture_yzm));
			} else {
				iv_yzm_isTrue.setVisibility(View.GONE);
				isValid = false;
			}
		}

	};
	

	void checkImgVerificationcode(String phone,String code){
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
				Log.i("json1", arg0.result);
				try {
					JSONObject jsonObject = new JSONObject(arg0.result);
					int errorCode = jsonObject.getInt("code");
					if (errorCode == 0) {
						iv_yzm_isTrue.setVisibility(View.VISIBLE);
						iv_yzm_isTrue.setImageDrawable(getResources().getDrawable(drawable.ic_right));
						isValid = true;
					} else {
						MyConst.imageLoader.displayImage(MyConst.imageVerifyAction+"phone="+getStr(ed_register_name), iv_picture_yzm, MyConst.IM_IMAGE_OPTIONS_NO_CACHE);
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
		params.addQueryStringParameter("phone", getStr(ed_register_name));
		HttpUtils httpUtils=new HttpUtils();
//		BaseActivity.httpUtils.send(HttpMethod.POST,"http://121.46.0.219:8080/efreshapp/verifyActionphone="+ getStr(ed_backpwd_phone),new RequestCallBack<String>() {
		httpUtils.send(HttpMethod.POST,MyConst.verifyAction,params,new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						showToast("服务器异常，请稍后再试");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						Log.i("json", arg0.result);
						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							int errorCode = jsonObject.getInt("code");
							if (errorCode == 0) {//不存在
//								showToast(jsonObject.getString("message"));
//								iv_old_password_isTrue.setImageDrawable(getResources().getDrawable(drawable.ic_error));
//								iv_old_password_isTrue.setVisibility(View.VISIBLE);
//								btn_get_yzm.setTextColor(getResources().getColor(R.color.graytext));
								iv_old_password_isTrue.setImageDrawable(getResources().getDrawable(drawable.ic_right));
								iv_old_password_isTrue.setVisibility(View.VISIBLE);
//								btn_get_yzm.setTextColor(getResources().getColor(R.color.yzmorange));
								ly_prcture_yzm.setVisibility(View.VISIBLE);
								
								MyConst.imageLoader.displayImage(MyConst.imageVerifyAction+"phone="+getStr(ed_register_name), iv_picture_yzm, MyConst.IM_IMAGE_OPTIONS_NO_CACHE);
//								MyConst.imageLoader.displayImage(MyConst.imageVerifyAction+"phone="+getStr(ed_register_name), iv_picture_yzm, MyConst.IM_IMAGE_OPTIONS);
								MyConst.imageLoader.clearDiskCache();
							} else {
								showToast(jsonObject.getString("message"));
								iv_old_password_isTrue.setImageDrawable(getResources().getDrawable(drawable.ic_error));
								iv_old_password_isTrue.setVisibility(View.VISIBLE);
								ly_prcture_yzm.setVisibility(View.GONE);
//								btn_get_yzm.setTextColor(getResources().getColor(R.color.yzmorange));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
	}
	
	
//	void checkPhoneHttp() {
//		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("phone", getStr(ed_register_name));
//		doHttp(1, MyConst.verifyAction, params, checkPhone);
//	}

//	void sendCodeHttp() {
//		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("phone", getStr(ed_register_name));
//		doHttp(1, MyConst.sendCodeAction, params, sendCode);
//	}

	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		switch (flag) {
//		case checkPhone:
//			iv_old_password_isTrue.setImageDrawable(getResources().getDrawable(drawable.ic_right));
//			iv_old_password_isTrue.setVisibility(View.VISIBLE);
////			btn_get_yzm.setTextColor(getResources().getColor(R.color.yzmorange));
//			isValid = true;
//			break;

//		case sendCode:
////			code = jsonString;
////			ed_register_yzm.setText(jsonString);
//			break;

		default:
			break;
		}
	}

	@OnClick({ R.id.iv_return_register, R.id.tv_im_businesses, R.id.tv_im_purchaser, R.id.btn_register_next,
			R.id.rl_select_diqu,R.id.btn_exchange})
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_return_register:
			finish();
			break;
		case R.id.tv_im_businesses:
			type = 1;
			tv_im_businesses.setTextColor(Color.parseColor("#FDE000"));
			tv_im_purchaser.setTextColor(Color.parseColor("#FFFFFF"));
			tv_register_address.setText("请选择公司注册地区");
			ed_register_detailaddress.setHint("请输入详细公司注册地址");
			break;
		case R.id.tv_im_purchaser:
			type = 0;
			tv_im_purchaser.setTextColor(Color.parseColor("#FDE000"));
			tv_im_businesses.setTextColor(Color.parseColor("#FFFFFF"));
			tv_register_address.setText("请选择收货地区");
			ed_register_detailaddress.setHint("请输入详细收货地址");
			break;
		case R.id.btn_register_next:
			if (isNull(ed_register_name) || isNull(ed_register_yzm) || isNull(getStr(tv_register_address))
					|| isNull(ed_register_detailaddress)) {
				showToast("请确保信息不为空！！");
				return;
			}
			if (!getStr(ed_register_yzm).equals(code)) {
				showToast("请输入正确的短信验证码！！");
				return;
			}
			intent = new Intent(RegisterActivity.this, RegisterNextPageActivity.class);
			intent.putExtra("code", getStr(ed_register_yzm));
			intent.putExtra("type", type + "");
			intent.putExtra("phone", getStr(ed_register_name));
			intent.putExtra("province", province);
			intent.putExtra("city", city);
			intent.putExtra("area", area);
			intent.putExtra("address", getStr(ed_register_detailaddress));
			startActivity(intent);
			break;
		case R.id.rl_select_diqu:
			new CityPopupWindows(RegisterActivity.this, rl_select_diqu);
			break;
		case R.id.btn_exchange:
			MyConst.imageLoader.displayImage(MyConst.imageVerifyAction+"phone="+getStr(ed_register_name), iv_picture_yzm, MyConst.IM_IMAGE_OPTIONS_NO_CACHE);
			ed_picture_yzm.setText("");
			break;
//		case R.id.btn_get_yzm:
//			if (isValid) {
//				sendCodeHttp();
//			} else {
//				showToast("该手机号已被注册");
//				iv_old_password_isTrue.setImageDrawable(getResources().getDrawable(drawable.ic_error));
//				iv_old_password_isTrue.setVisibility(View.VISIBLE);
//			}
//			break;
		default:
			break;
		}

	}
	
	public class CityPopupWindows extends PopupWindow {

		public CityPopupWindows(Context mContext, View parent) {
			View view = View.inflate(mContext, R.layout.popupwindow_city_select, null);
			setWidth(LayoutParams.WRAP_CONTENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			// setBackgroundDrawable(new BitmapDrawable());

			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.CENTER, 0, 0);
			update();
			final TextView tv_city_info = (TextView) view.findViewById(R.id.tv_city_info);
			final CityPicker cityPicker = (CityPicker) view.findViewById(R.id.citypicker1);
			tv_city_info.setText(cityPicker.getCity_string());
			cityInfo="广东省-广州市-市辖区";//初始值
			cityPicker.setOnSelectingListener(new OnSelectingListener() {

				@Override
				public void selected(boolean selected) {
					cityInfo = cityPicker.getCity_string();
					tv_city_info.setText(cityInfo);
				}
			});
			TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
			tv_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					cityInfo = cityPicker.getCity_string();
					tv_register_address.setText(cityInfo);
					if(cityInfo.split("-").length==1){
						province=cityInfo.split("-")[0];
						city=" ";
						area=" ";
					}
					else if(cityInfo.split("-").length==2){
						province=cityInfo.split("-")[0];
						city=" ";
						area=cityInfo.split("-")[1];
					}
					else if(cityInfo.split("-").length==3){
						province=cityInfo.split("-")[0];
						city=cityInfo.split("-")[1];
						area=cityInfo.split("-")[2];
					}
				}
			});
		}
	}

}
