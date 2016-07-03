package com.cnmobi.exianmall.base;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnmobi.exianmall.R;
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

import org.json.JSONException;
import org.json.JSONObject;

public class BaseFragment extends Fragment {

	ProgressDialog progressDialog;
	HttpUtils httpUtils;
	protected void doHttp(int method, String url, RequestParams params, int tag, boolean showLoading) {
		if (showLoading) {
			progressDialog = ProgressDialog.show(getActivity(), null, "加载中...");
		}
		httpUtils=new HttpUtils();
//		 if(MyCookieStore.cookieStore!=null){
//			 LogUtils.e("session:"+MyCookieStore.cookieStore);
//			 httpUtils.configCookieStore(MyCookieStore.cookieStore);
//		 }
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
//			DefaultHttpClient dh = (DefaultHttpClient) httpUtils.getHttpClient();
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
					if (jsonObject.has("response")) {
						callback(jsonObject.getString("response"), tag);
					} else {
						callback("", tag);
					}
				} else {
					ShowToast(jsonObject.getString("message"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(HttpException arg0, java.lang.String arg1) {
			ShowToast(arg1);
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}

	}

	public void callback(String jsonString, int flag) {

	}

	public void ShowToast(String string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
	}

	public void showImg(ImageView imageView, String url) {
		BitmapUtils mBitmapUtils = new BitmapUtils(getActivity());
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
		mConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(getActivity()));
		mConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		mBitmapUtils.display(imageView, url, mConfig);
	}

	/**
	 * 保存sp数据
	 * 
	 * @param key
	 * @param object
	 */
	public void putSp(String key, Object object) {
		SpUtils.put(getActivity(), key, object);
	}

	/**
	 * 获取sp数据
	 * 
	 * @param key
	 * @param object
	 * @return
	 */
	public Object getSp(String key, Object object) {
		return SpUtils.get(getActivity(), key, object);
	}

	/**
	 * 清除Sp数据
	 */
	public void clearSp() {
		SpUtils.clear(getActivity());
	}

	/**
	 * 弹出Toast
	 */
	public void showToast(String string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 弹出Toast
	 */
	public void showToastShort(String string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
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
}
