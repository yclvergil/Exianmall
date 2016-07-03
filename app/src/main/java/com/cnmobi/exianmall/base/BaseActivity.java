package com.cnmobi.exianmall.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.utils.ActivityCollector;
import com.cnmobi.exianmall.utils.SpUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by peng24 on 2015/12/24.
 */
public class BaseActivity extends Activity {

	private TextView tv_base_titleText;
	private TextView tv_base_edit;
	private LinearLayout ly_base_back;
	private RelativeLayout rv_base_titleView;
	private LinearLayout contentLv;
	private ProgressDialog progressDialog;
	HttpUtils httpUtils;
	/**
	 * 因要与服务器保持同一session，所有的httpUtils使用同一个，不使用的话在请求之前需要设置Cookie
	 */
//	public static HttpUtils httpUtils=new HttpUtils();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//对于没有滚动控件的布局来说，采用的是adjustPan方式，而对于有滚动控件的布局，则是采用的adjustResize方式
		setBase();
		setContentView(R.layout.baseactivity_layout);
		contentLv = (LinearLayout) findViewById(R.id.lv_base_content);
		ly_base_back = (LinearLayout) findViewById(R.id.ly_base_back);
		rv_base_titleView = (RelativeLayout) findViewById(R.id.rv_base_titleView);
		tv_base_titleText = (TextView) findViewById(R.id.tv_base_titleText);
		tv_base_edit = (TextView) findViewById(R.id.tv_base_edit);
		ly_base_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 可能全屏或者没有ActionBar
	 **/
	private void setBase() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 用于获取状态栏的高度。
	 */
	public static int getStatusBarHeight(Context context) {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 沉浸式标题栏
	 */
	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			int statusBarHeight = getStatusBarHeight(this.getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);
		}
	}

	/**
	 * 加载布局
	 */
	public void setBaseContentView(int layoutResId) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutResId, null);
		contentLv.addView(v);
		// setImmerseLayout(rv_base_titleView);
	}

	/**
	 * 隐藏标题栏
	 */
	public void hideTitleView() {
		rv_base_titleView.setVisibility(View.GONE);
	}

	/**
	 * 设置标题栏信息
	 */
	public void setTitleText(String string) {
		tv_base_titleText.setText(string);
	}

	/**
	 * 设置标题栏右边按钮点击事件
	 */
	public void setRightTextViewClick(OnClickListener onClickListener) {
		tv_base_edit.setVisibility(View.VISIBLE);
		tv_base_edit.setOnClickListener(onClickListener);
	}

	/**
	 * 设置标题栏右边按钮文字
	 */
	public void setRightTextViewText(String string) {
		tv_base_edit.setVisibility(View.VISIBLE);
		tv_base_edit.setText(string);
	}

	/**
	 * 弹出Toast
	 */
	public void showToast(String string) {
		Toast.makeText(BaseActivity.this, string, Toast.LENGTH_LONG).show();
	}

	/**
	 * 弹出Toast
	 */
	public void showToastShort(String string) {
		Toast.makeText(BaseActivity.this, string, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 获取EditView的文字
	 */
	public String getStr(EditText editText) {
		return editText.getText().toString();
	}

	/**
	 * 获取TextView的文字
	 */
	public String getStr(TextView textView) {
		return textView.getText().toString();
	}

	/**
	 * 获取string的文字
	 */
	public String getStr(int id) {
		return getResources().getString(id);
	}

	/**
	 * 保存sp数据
	 * 
	 * @param key
	 * @param object
	 */
	public void putSp(String key, Object object) {
		SpUtils.put(BaseActivity.this, key, object);
	}

	/**
	 * 清除Sp数据
	 */
	public void clearSp() {
		SpUtils.clear(BaseActivity.this);
	}

	/**
	 * 获取sp数据
	 * 
	 * @param key
	 * @param object
	 * @return
	 */
	public Object getSp(String key, Object object) {
		return SpUtils.get(BaseActivity.this, key, object);
	}

	/**
	 * 检查字符串是否是空对象或空字符串
	 */
	public boolean isNull(String str) {
		if (null == str || "".equals(str) || "null".equalsIgnoreCase(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查字符串是否是空对象或空字符串
	 */
	public boolean isNull(EditText str) {
		if (null == str.getText().toString() || "".equals(str.getText().toString())
				|| "null".equalsIgnoreCase(str.getText().toString())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 网络请求
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数集合
	 * @param tag
	 *            flag标志 ，用于区分一个页面有多个网络请求
	 * @param showLoading
	 *            是否显示加载对话框
	 */
	protected void doHttp(int method, String url, RequestParams params, int tag, boolean showLoading) {
		if (showLoading) {
			progressDialog = ProgressDialog.show(BaseActivity.this, null, "加载中...");
		}
		httpUtils=new HttpUtils();
//		if(MyCookieStore.cookieStore!=null){
//			 LogUtils.e("session:"+MyCookieStore.cookieStore);
//			 httpUtils.configCookieStore(MyCookieStore.cookieStore);
//		}else{
//			 LogUtils.e("session:"+MyCookieStore.cookieStore);
//		}
//		httpUtils.configCookieStore(MyCookieStore.cookieStore);
		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addQueryStringParameter("toKen",MyApplication.getInstance().getToKen());// 每个接口都要传的字段可以放在这里
		if (method == 0) {
			httpUtils.send(HttpMethod.GET, url, params, new MyRequestCallBack(tag));
		} else {
			httpUtils.send(HttpMethod.POST, url, params, new MyRequestCallBack(tag));
		}
	}

	protected void doHttp(int method, String url, RequestParams params, int tag) {
		doHttp(method, url, params, tag, true);
	}

	class MyRequestCallBack extends RequestCallBack<String> {
		public int tag;

		public MyRequestCallBack(int tag) {
			this.tag = tag;
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
//			DefaultHttpClient dh = (DefaultHttpClient) httpUtils
//					.getHttpClient();
//			MyCookieStore.cookieStore = dh.getCookieStore();
//			CookieStore cs = dh.getCookieStore();
//			List<Cookie> cookies = cs.getCookies();
//			String aa = null;
//			for (int i = 0; i < cookies.size(); i++) {
//				if ("JSESSIONID".equals(cookies.get(i).getName())) {
//					aa = cookies.get(i).getValue();
////					MyCookieStore.cookieStore = aa;
//					Log.i("aa=", cookies.get(i).getValue());
//					break;
//				}
//			}
//			LogUtils.i("dh"+ dh);
//			LogUtils.i("cookieStore="+dh.getCookieStore());
			Log.i("jsonFlag=" + tag, arg0.result);
			try {
				JSONObject jsonObject = new JSONObject(arg0.result);
				int errorCode = jsonObject.getInt("code");
				if (errorCode == 0) {
					// 在这里统一解析json外层，成功就解析接口内容
					if (jsonObject.has("response")) {
						callback(jsonObject.getString("response"), tag);
					} else {
						callback("", tag);
					}
				} else {
					// 错误就统一弹出错误信息
					showToast(jsonObject.getString("message"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(HttpException arg0, java.lang.String arg1) {
			showToast(arg1);
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

	/**
	 * 网络请求回调
	 * 
	 * @param jsonString
	 *            解析成功后的data
	 * @param flag
	 *            标志，用于区分同页面多个网络请求
	 */
	public void callback(String jsonString, int flag) {

	}

	/**
	 * 显示网络照片或者本地照片 ，本地照片url前面加上 file://
	 * 
	 * @param imageView
	 *            要显示的图片控件
	 * @param url
	 *            照片地址
	 */
	public void showImg(ImageView imageView, String url) {
		BitmapUtils mBitmapUtils = new BitmapUtils(BaseActivity.this);
		BitmapDisplayConfig mConfig = new BitmapDisplayConfig();
		AnimationSet set = new AnimationSet(true);
		AlphaAnimation alpha = new AlphaAnimation(0.3f, 1);
		ScaleAnimation scale = new ScaleAnimation(0.7f, 1f, 0.7f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		set.addAnimation(alpha);
		set.addAnimation(scale);
		set.setDuration(350);
		mConfig.setAnimation(set);
		mConfig.setLoadFailedDrawable(getResources().getDrawable(R.drawable.iv_imageloader_failure));
		mConfig.setLoadingDrawable(getResources().getDrawable(R.drawable.iv_imageloader_failure));
		mConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(BaseActivity.this));
		mConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		mBitmapUtils.display(imageView, url, mConfig);
	}
	
	/**
	 * 启动Activity
	 */
	public void openActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		 Log.i(this.getClass().getSimpleName(), "------>onStart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Log.i(this.getClass().getSimpleName(), "------>onStop");
	}


	@Override
	protected void onRestart() {
		super.onRestart();
		// Log.i(this.getClass().getSimpleName(), "------>onRestart");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		 Log.i(this.getClass().getSimpleName(), "------>onDestroy");
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
		ActivityCollector.removeActivity(this);
	}

}
