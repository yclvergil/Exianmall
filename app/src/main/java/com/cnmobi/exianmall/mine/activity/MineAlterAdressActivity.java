package com.cnmobi.exianmall.mine.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.widget.CityPicker;
import com.cnmobi.exianmall.widget.CityPicker.OnSelectingListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的地址修改界面
 */
public class MineAlterAdressActivity extends BaseActivity {

	@ViewInject(R.id.tv_consigner_name)
	TextView tv_consigner_name;
	@ViewInject(R.id.tv_consigner_phonenumber)
	TextView tv_consigner_phonenumber;
	@ViewInject(R.id.tv_consigner_adress)
	TextView tv_consigner_adress;
	@ViewInject(R.id.rl_alteradress_select)
	RelativeLayout rl_alteradress_select;
	@ViewInject(R.id.tv_alter_selectadress)
	TextView tv_alter_selectadress;
	@ViewInject(R.id.tv_consigner_name1)
	TextView tv_consigner_name1;
	@ViewInject(R.id.tv_consigner_phonenumber1)
	TextView tv_consigner_phonenumber1;
	@ViewInject(R.id.ed_new_consigner_detailadress)
	EditText ed_new_consigner_detailadress;
	@ViewInject(R.id.ly_new_adress)
	LinearLayout ly_new_adress;
	@ViewInject(R.id.btn_commit_alter_adress)
	Button btn_commit_alter_adress;
	private static String cityInfo = "";// 选择的城市信息
	/**修改地址接口标识*/
	public static final int updateAdress = 0;
	/**买家收货地址查询接口标识*/
	public static final int queryProfile=1;
	private String provinceup;
	private String cityup;
	private String areaup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_alter_adress);
		ViewUtils.inject(this);
		
		setTitleText("地址修改");
		init();
		
		
	}

	void init(){
//		
//		tv_consigner_name.setText(MyApplication.getInstance().getTlr_name());
//		tv_consigner_name1.setText(MyApplication.getInstance().getTlr_name());
//		tv_consigner_phonenumber.setText(MyApplication.getInstance().getPhone());
//		tv_consigner_phonenumber1.setText(MyApplication.getInstance().getPhone());
//		tv_consigner_adress.setText(MyApplication.getInstance().getDeliveryaddress());
		// "Y" 是否审核通过，Y为通过，N为没通过,未通过不能进地址管理界面
		if (!"1".equals(MyApplication.getInstance().getExamine())
				| "".equals(MyApplication.getInstance().getExamine())) {
			showToast("您还未通过审核，还不能对地址进行修改哦~");
			btn_commit_alter_adress.setVisibility(View.GONE);
			ly_new_adress.setVisibility(View.GONE);
		}
		queryProfileHttp();
	}
//	买家收货地址查询
//	地址：http://121.46.0.219:8080/efreshapp/queryProfileAction?idUser=1000
	void queryProfileHttp(){
		RequestParams params=new RequestParams();
		params.addBodyParameter("idUser",MyApplication.getInstance().getIdNumber()+"");
		doHttp(1, MyConst.queryProfileAction, params, queryProfile);
	}
	
	//买家申请修改收货地址
	void upDeliveryAddressHttp(){
		RequestParams params = new RequestParams();
		params.addBodyParameter("idUser",MyApplication.getInstance().getIdNumber()+"");
		params.addBodyParameter("provinceup",provinceup);
		params.addBodyParameter("cityup",cityup);
		params.addBodyParameter("areaup",areaup);
		params.addBodyParameter("deliveryaddress2",provinceup+cityup+areaup+getStr(ed_new_consigner_detailadress));
		doHttp(1, MyConst.upDeliveryAddressAction, params, updateAdress);
	}
	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		switch (flag) {
		case updateAdress:
			showToast("修改成功");
			MyApplication.getInstance().setDeliveryaddress(provinceup+cityup+areaup+getStr(ed_new_consigner_detailadress));
			Intent intent = new Intent(MineAlterAdressActivity.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
			startActivity(intent);
			finish();
			break;
		case queryProfile:
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				tv_consigner_name.setText(jsonObject.getString("name"));
				tv_consigner_name1.setText(jsonObject.getString("name"));
				tv_consigner_phonenumber.setText(jsonObject.getString("phone"));
				tv_consigner_phonenumber1.setText(jsonObject.getString("phone"));
				tv_consigner_adress.setText(jsonObject.getString("address")+jsonObject.getString("stallsname"));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	@OnClick({ R.id.rl_alteradress_select, R.id.btn_commit_alter_adress })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_alteradress_select:
			new CityPopupWindows(MineAlterAdressActivity.this,
					rl_alteradress_select);
			break;
		case R.id.btn_commit_alter_adress:
			if(isNull(ed_new_consigner_detailadress)){
				showToast("请输入详细地址");
				return;
			}
			if(isNull(getStr(tv_alter_selectadress))){
				showToast("请选择地区");
				return ;
			}
			upDeliveryAddressHttp();
			break;
		default:
			break;
		}

	}

	public class CityPopupWindows extends PopupWindow {

		public CityPopupWindows(Context mContext, View parent) {
			View view = View.inflate(mContext,
					R.layout.popupwindow_city_select, null);
			setWidth(LayoutParams.WRAP_CONTENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			// setBackgroundDrawable(new BitmapDrawable());

			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.CENTER, 0, 0);
			update();
			final TextView tv_city_info = (TextView) view
					.findViewById(R.id.tv_city_info);
			final CityPicker cityPicker = (CityPicker) view
					.findViewById(R.id.citypicker1);
			cityInfo="广东省-广州市-市辖区";//初始值
			cityPicker.setOnSelectingListener(new OnSelectingListener() {

				@Override
				public void selected(boolean selected) {
					LogUtils.i(""+selected);
					cityInfo = cityPicker.getCity_string();
					tv_city_info.setText(cityInfo);
					
				}
				
				
			});
			tv_city_info.setText(cityPicker.getCity_string());
			TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
			tv_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					tv_alter_selectadress.setText(cityInfo);
					if(cityInfo.split("-").length==1){
						provinceup=cityInfo.split("-")[0];
						cityup=" ";
						areaup=" ";
					}
					else if(cityInfo.split("-").length==2){
						provinceup=cityInfo.split("-")[0];
						cityup=" ";
						areaup=cityInfo.split("-")[1];
					}
					else if(cityInfo.split("-").length==3){
						provinceup=cityInfo.split("-")[0];
						cityup=cityInfo.split("-")[1];
						areaup=cityInfo.split("-")[2];
					}
					
				}
			});
		}
	}

}
