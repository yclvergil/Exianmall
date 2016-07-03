package com.cnmobi.exianmall.mineset.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextPaint;
import android.widget.ListView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.AboutEXianAdapter;
import com.cnmobi.exianmall.adapter.CommonAdapter;
import com.cnmobi.exianmall.adapter.ViewHolder;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.AboutEXianBean;
import com.cnmobi.exianmall.bean.MessageBean;
import com.cnmobi.exianmall.bean.coupons;
import com.cnmobi.exianmall.bean.integralRule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 我的设置-关于界面
 */
public class MineSetAboutActivity extends BaseActivity {

	@ViewInject(R.id.lv_about)
	ListView listView;
	@ViewInject(R.id.tv_address)
	TextView tv_address;
	@ViewInject(R.id.tv_email)
	TextView tv_email;
	@ViewInject(R.id.tv_phone)
	TextView tv_phone;
	@ViewInject(R.id.tv_title)
	TextView tv_title;
	List<AboutEXianBean> list=new ArrayList<AboutEXianBean>();
	public static final int aboutE=0;
	AboutEXianAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_set_about);
		ViewUtils.inject(this);

		setTitleText("关于e鲜");
		aboutEfreshHttp();
	}

	
	void aboutEfreshHttp(){
		RequestParams params=new RequestParams();
//		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		doHttp(1, MyConst.aboutEfreshAction, params, aboutE);
	}
//	
	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		switch (flag) {
		case aboutE:
//			tv_email.setText("a"+"&nbsp"+"b\nc");
			try {
				JSONObject jsonObject=new JSONObject(jsonString);
				tv_title.setText(jsonObject.getString("corporateName"));
				tv_address.setText("公司地址:"+jsonObject.getString("address"));
				tv_email.setText("联系邮箱:"+jsonObject.getString("email"));
				tv_phone.setText("联系电话:"+jsonObject.getString("phone"));
				list=new Gson().fromJson(jsonObject.getString("about"),new TypeToken<List<AboutEXianBean>>() {
				}.getType());
				adapter=new AboutEXianAdapter(MineSetAboutActivity.this, list, jsonObject.getString("corporateName"));
				listView.setAdapter(adapter);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}
}
