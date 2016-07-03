package com.cnmobi.exianmall.mine.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.widget.CityPicker;
import com.cnmobi.exianmall.widget.CityPicker.OnSelectingListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 卖家-新增产地地址界面
 */
public class MerchantMineAddAdressActivity extends BaseActivity {

	@ViewInject(R.id.rl_alteradress_select)
	private RelativeLayout rl_alteradress_select;
	@ViewInject(R.id.tv_alter_selectadress)
	private TextView tv_alter_selectadress;
	private static String cityInfo = "";// 选择的城市信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_merchant_addadress);
		ViewUtils.inject(this);

		setTitleText("新增产地地址");
		setRightTextViewText("提交");
		setRightTextViewClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showToast("您已成功提交新增产地地址申请，请耐心等待管理员审核，审核未通过，产地地址任然不变~");
				finish();
			}
		});
	}

	@OnClick({ R.id.btn_commit_add_adress, R.id.rl_alteradress_select })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit_add_adress:
			showToast("您已成功提交新增产地地址申请，请耐心等待管理员审核，审核未通过，产地地址任然不变~");
			finish();
			break;
		case R.id.rl_alteradress_select:
			new CityPopupWindows(MerchantMineAddAdressActivity.this, rl_alteradress_select);
			break;
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
					tv_alter_selectadress.setText(cityInfo);
				}
			});

		}
	}
}
