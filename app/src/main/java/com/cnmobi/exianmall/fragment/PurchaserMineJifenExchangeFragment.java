package com.cnmobi.exianmall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.CommonAdapter;
import com.cnmobi.exianmall.adapter.ViewHolder;
import com.cnmobi.exianmall.base.BaseFragment;
import com.cnmobi.exianmall.bean.ElectronicCouponBean;
import com.lidroid.xutils.ViewUtils;

/**
 * 我的积分-兑换界面
 */
public class PurchaserMineJifenExchangeFragment extends BaseFragment {

	private ListView lv_jifen_exchange;
	private List<ElectronicCouponBean> electronicCouponDatas = new ArrayList<ElectronicCouponBean>();
	CommonAdapter<ElectronicCouponBean> electronicCouponAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.fragment_purchaser_mine_jifenexchange, container,
				false);
		ViewUtils.inject(this,view);
		initUi(view);
		return view;
	}

	public void initUi(View view) {
		initDatas();
		lv_jifen_exchange = (ListView) view
				.findViewById(R.id.lv_jifen_exchange);
		electronicCouponAdapter = new CommonAdapter<ElectronicCouponBean>(
				getActivity().getApplicationContext(), electronicCouponDatas,
				R.layout.adapter_jifen_exchange_1) {

			@Override
			public void convert(ViewHolder helper, ElectronicCouponBean item) {
				helper.setText(R.id.tv_coupon_money, item.getCouponmMoney());
				helper.setText(R.id.tv_coupon_type, item.getCouponType());
				helper.setText(R.id.tv_coupon_begin_date,
						item.getCouponBeginDate());
				helper.setText(R.id.tv_coupon_end_date, item.getCouponEndDate());
				helper.getView(R.id.tv_to_exchange).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// 对话框
								LayoutInflater inflaterDl = LayoutInflater
										.from(getActivity()
												.getApplicationContext());
								RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
										.inflate(
												R.layout.dialog_jifen_exchange,
												null);
								Builder builder = new AlertDialog.Builder(
										getActivity());
								builder.setView(relativeLayout);
								final Dialog dialog = builder.create();
								dialog.show();
								Button btnCancel = (Button) relativeLayout
										.findViewById(R.id.btn_jifen_dialog_cancel);
								btnCancel
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												dialog.dismiss();
											}
										});
								Button btnOK = (Button) relativeLayout
										.findViewById(R.id.btn_jifen_dialog_ok);
								btnOK.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.dismiss();
										Toast.makeText(getActivity(),
												"您已成功兑换，可在购物车中使用",
												Toast.LENGTH_SHORT).show();

									}
								});
							}
						});
			}

		};
		lv_jifen_exchange.setAdapter(electronicCouponAdapter);
	}

	public void initDatas() {
		ElectronicCouponBean electronicCouponBean = null;
		electronicCouponBean = new ElectronicCouponBean("200", "电子购物券",
				"2015-12-07", "2016-02-07");
		electronicCouponDatas.add(electronicCouponBean);
		electronicCouponBean = new ElectronicCouponBean("250", "电子购物券",
				"2016-1-07", "2016-03-07");
		electronicCouponDatas.add(electronicCouponBean);
		electronicCouponBean = new ElectronicCouponBean("300", "电子购物券",
				"2015-12-07", "2015-12-27");
		electronicCouponDatas.add(electronicCouponBean);

	}

}
