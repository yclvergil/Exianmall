package com.cnmobi.exianmall.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MainActivityMerchant;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.widget.CityPicker;
import com.cnmobi.exianmall.widget.CityPicker.OnSelectingListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 卖家产地修改界面
 */
public class MerchantMineAlterAdressActivity extends BaseActivity {

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
	
	private static String cityInfo = "";// 选择的城市信息
	/**修改地址接口标识*/
	public static final int updateAdress = 0;
	
	private String provinceup;
	private String cityup;
	private String areaup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_merchant_alter_adress);
		ViewUtils.inject(this);

		setTitleText("产地修改");
//		setRightTextViewText("提交");
//		setRightTextViewClick(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				showToast("您已成功提交修改收货地址申请，请耐心等待管理员审核，审核未通过，收货地址任然是旧的收货地哦~");
//				finish();
//			}
//		});
		
		init();
	}
	
	void init(){
		tv_consigner_name.setText(MyApplication.getInstance().getTlr_name());
		tv_consigner_name1.setText(MyApplication.getInstance().getTlr_name());
//		tv_consigner_phonenumber.setText(MyApplication.getInstance().getPhone());
//		tv_consigner_phonenumber1.setText(MyApplication.getInstance().getPhone());
		tv_consigner_adress.setText(MyApplication.getInstance().getDeliveryaddress());
	}
	
	//卖家申请修改产地地址
	//http://121.46.0.219:8080/efreshapp/upAddressAction?addressup=浙江省宁波市堇州区风格雅事&provinceup=浙江省&cityup=宁波市&areaup=堇州区&idUser=2
	void upAddressHttp(){
		RequestParams params = new RequestParams();
		params.addBodyParameter("addressup",provinceup+cityup+areaup+getStr(ed_new_consigner_detailadress));
		params.addBodyParameter("provinceup",provinceup);
		params.addBodyParameter("cityup",cityup);
		params.addBodyParameter("areaup",areaup);
		params.addBodyParameter("idUser",MyApplication.getInstance().getIdNumber()+"");
		doHttp(1, MyConst.upAddressAction, params, updateAdress);
	}
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
		case updateAdress:
			showToast("修改成功");
			MyApplication.getInstance().setDeliveryaddress(provinceup+cityup+areaup+getStr(ed_new_consigner_detailadress));
//			Intent intent = new Intent(MerchantMineAlterAdressActivity.this, MainActivityMerchant.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
//			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}
	@OnClick({ R.id.rl_alteradress_select, R.id.btn_commit_alter_adress })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_alteradress_select:
			new CityPopupWindows(MerchantMineAlterAdressActivity.this,
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
			upAddressHttp();
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
