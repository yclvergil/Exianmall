package com.cnmobi.exianmall.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.mine.activity.MerchantMineInformationActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MerchantMineInfoTimeButton extends Button implements OnClickListener {
	
	Bitmap bitmap;
	private long lenght = 60 * 1000;// 倒计时长度,这里给了默认60秒
	private String textafter = "已发送";
	private String textbefore = "发送验证码";
	private final String TIME = "time";
	private final String CTIME = "ctime";
	private OnClickListener mOnclickListener;
	private Timer t;
	private TimerTask tt;
	private long time;
	boolean veryficationissend =false;
//    /**
//     * 验证手机号是否有效正则    */
//    public static final String REGEX_MOBILE = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
//    
//    public static boolean isMobile(String mobile) {
//        return Pattern.matches(REGEX_MOBILE, mobile);
//    }
 
	public MerchantMineInfoTimeButton(Context context) {
		super(context);
		setOnClickListener(this);

	}

	public MerchantMineInfoTimeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	Handler han = new Handler() {
		public void handleMessage(android.os.Message msg) {
			MerchantMineInfoTimeButton.this.setText(textafter+"("+time / 1000 +")");
			MerchantMineInfoTimeButton.this.setTextColor(Color.parseColor("#999999"));
			time -= 1000;
			if (time < 0) {
				MerchantMineInfoTimeButton.this.setEnabled(true);
				MerchantMineInfoTimeButton.this.setText(textbefore);
				MerchantMineInfoTimeButton.this.setTextColor(Color.parseColor("#e8a12e"));
				clearTimer();
			}
		};
	};

	private void initTimer() {
		time = lenght;
		t = new Timer();
		tt = new TimerTask() {

			@Override
			public void run() {
				han.sendEmptyMessage(0x01);
			}
		};
	}

	private void clearTimer() {
		if (tt != null) {
			tt.cancel();
			tt = null;
		}
		if (t != null)
			t.cancel();
		t = null;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		if (l instanceof MerchantMineInfoTimeButton) {
			super.setOnClickListener(l);
		} else
			this.mOnclickListener = l;
	}
	
	/**
	 * 倒计时
	 */
	void countDown(){
		initTimer();// 开始倒计时
		this.setText(time / 1000 + textafter);
		this.setEnabled(false);// 倒计时期间按钮不可点击
		t.schedule(tt, 0, 1000);
	}
	
	@Override
	public void onClick(final View v) {
		String phoneNo=MerchantMineInformationActivity.edt_phone.getText().toString();
//		LogUtils.e("--------------"+isMobile("18107901879"));
		if (mOnclickListener != null) {
			initTimer();// 开始倒计时
			mOnclickListener.onClick(v);
		}else if("".equals(phoneNo)){
			Toast.makeText(getContext(), "请输入手机号！", Toast.LENGTH_SHORT).show();
		}else if(!MerchantMineInformationActivity.isValid){
			Toast.makeText(getContext(), "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
		}else {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("phone", phoneNo);
//			HttpUtils httpUtils=new HttpUtils();
			LogUtils.e("手机 "+phoneNo);
			HttpUtils httpUtils=new HttpUtils();
//			httpUtils.configCookieStore(MyCookieStore.cookieStore);
			httpUtils.send(HttpMethod.POST, MyConst.sendCodeAction, params, new RequestCallBack<String>() {
//			BaseActivity.httpUtils.send(HttpMethod.POST, MyConst.sendCodeAction, params, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					LogUtils.e(arg0.result);
					// TODO Auto-generated method stub
					try {
						JSONObject jsonObject = new JSONObject(arg0.result);
						int errorCode = jsonObject.getInt("code");
						if (errorCode == 0) {
							// 在这里统一解析json外层，成功就解析接口内容
							if (jsonObject.has("response")) {
								countDown();
//								BackPasswordActivity.code=jsonObject.getString("response");
							} else {
							}
						} else {
							
							// 错误就统一弹出错误信息
							Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 和activity的onDestroy()方法同步
	 */
	public void onDestroy() {
		if (MyApplication.map == null)
			MyApplication.map = new HashMap<String, Long>();
		MyApplication.map.put(TIME, time);
		MyApplication.map.put(CTIME, System.currentTimeMillis());
		clearTimer();
	}

	/**
	 * 和activity的onCreate()方法同步
	 */
	public void onCreate(Bundle bundle) {
		if (MyApplication.map == null)
			return;
		if (MyApplication.map.size() <= 0)// 这里表示没有上次未完成的计时
			return;
		long time = System.currentTimeMillis() - MyApplication.map.get(CTIME)
				- MyApplication.map.get(TIME);
		MyApplication.map.clear();
		if (time > 0)
			return;
		else {
			initTimer();
			this.time = Math.abs(time);
			t.schedule(tt, 0, 1000);
			this.setText(time + textafter);
			this.setEnabled(false);
		}
	}

	/** * 设置计时时候显示的文本 */
	public MerchantMineInfoTimeButton setTextAfter(String text1) {
		this.textafter = text1;
		return this;
	}

	/** * 设置点击之前的文本 */
	public MerchantMineInfoTimeButton setTextBefore(String text0) {
		this.textbefore = text0;
		this.setText(textbefore);
		return this;
	}

	/**
	 * 设置到计时长度
	 * 
	 * @param lenght
	 *            时间 默认毫秒
	 * @return
	 */
	public MerchantMineInfoTimeButton setLenght(long lenght) {
		this.lenght = lenght;
		return this;
	}
}