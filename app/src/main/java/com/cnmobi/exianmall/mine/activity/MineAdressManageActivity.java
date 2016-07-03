package com.cnmobi.exianmall.mine.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的地址管理界面
 */
public class MineAdressManageActivity extends BaseActivity {

	@ViewInject(R.id.tv_consigner_name)
	TextView tv_consigner_name;
	@ViewInject(R.id.tv_consigner_phonenumber)
	TextView tv_consigner_phonenumber;
	@ViewInject(R.id.tv_consigner_adress)
	TextView tv_consigner_adress;
	
	/**买家收货地址查询接口标识*/
	public static final int queryProfile=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_adress_manage);
		ViewUtils.inject(this);
		
		setTitleText("地址管理");
		init();

	}
	

	void init(){
		tv_consigner_name.setText(MyApplication.getInstance().getTlr_name());
		tv_consigner_phonenumber.setText(MyApplication.getInstance().getPhone());
		tv_consigner_adress.setText(MyApplication.getInstance().getDeliveryaddress());
		
		queryProfileHttp();
	}
	
//	买家收货地址查询
//	地址：http://121.46.0.219:8080/efreshapp/queryProfileAction?idUser=1000
	void queryProfileHttp(){
		RequestParams params=new RequestParams();
		params.addBodyParameter("idUser",MyApplication.getInstance().getIdNumber()+"");
		doHttp(1, MyConst.queryProfileAction, params, queryProfile);
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
		case queryProfile:
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				tv_consigner_name.setText(jsonObject.getString("name"));
				tv_consigner_phonenumber.setText(jsonObject.getString("phone"));
				tv_consigner_adress.setText(jsonObject.getString("address"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}
	@OnClick({ R.id.ly_common_head_return, R.id.btn_alter_consigner_adress })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ly_common_head_return:
			finish();
			break;
		case R.id.btn_alter_consigner_adress:
			intent = new Intent(MineAdressManageActivity.this,
					MineAlterAdressActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

}
