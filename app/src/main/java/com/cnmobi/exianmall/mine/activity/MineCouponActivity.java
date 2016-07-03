package com.cnmobi.exianmall.mine.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.CommonAdapter;
import com.cnmobi.exianmall.adapter.ViewHolder;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.bean.CouponBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的优惠券界面
 */
public class MineCouponActivity extends BaseActivity {

	@ViewInject(R.id.lv_coupon_exchange)
	private ListView lv_coupon_exchange;
	private List<CouponBean> couponDatas = new ArrayList<CouponBean>();
	private CommonAdapter<CouponBean> couponsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_coupon);
		ViewUtils.inject(this);
		initDatas();

		couponsAdapter = new CommonAdapter<CouponBean>(getApplicationContext(),
				couponDatas, R.layout.adapter_coupon_exchange) {

			@Override
			public void convert(ViewHolder helper, CouponBean item) {
				helper.setText(R.id.tv_coupons_out_money,
						item.getCouponOutMoney());
				helper.setText(R.id.tv_coupons_reduce_money,
						item.getCouponReduceMoney());
				helper.setText(R.id.tv_coupons_type2, item.getCouponType());
				helper.setText(R.id.tv_coupon_mark, item.getCouponMark());
				helper.setText(R.id.tv_coupon_limit, item.getCouponLimit());
				helper.setText(R.id.tv_coupons_begin_date,
						item.getCouponBeginDate());
				helper.setText(R.id.tv_coupons_end_date,
						item.getCouponEndDate());
				helper.setText(R.id.tv_coupon_count, item.getCouponCount());
			}

		};
		lv_coupon_exchange.setAdapter(couponsAdapter);
	}

	@OnClick({ R.id.ly_common_head_4_return, R.id.iv_common_head_4_right })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ly_common_head_4_return:
			finish();
			break;
		case R.id.iv_common_head_4_right:
			intent = new Intent(MineCouponActivity.this,
					MineCouponRuleActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void initDatas() {
		CouponBean couponBean = null;
		couponBean = new CouponBean("100", "100", "优惠券", "2015-12-08",
				"2015-12-28", "“e鲜” 商城", "平台内均可使用", "3");
		couponDatas.add(couponBean);
		couponBean = new CouponBean("100", "5", "优惠券", "2015-12-08",
				"2015-12-28", "“e鲜” 商城", "平台内均可使用", "4");
		couponDatas.add(couponBean);
		couponBean = new CouponBean("100", "30", "优惠券", "2015-12-08",
				"2015-12-28", "“e鲜” 商城", "平台内均可使用", "3");
		couponDatas.add(couponBean);

	}

}
