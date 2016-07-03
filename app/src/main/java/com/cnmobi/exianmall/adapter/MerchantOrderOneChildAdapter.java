package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.bean.FirstOrderDetail;
import com.cnmobi.exianmall.type.activity.ProductDetailActivity;

/**
 * 底部菜单栏订单页面，待付款适配器
 * 
 */
public class MerchantOrderOneChildAdapter extends BaseAdapter {

	private List<FirstOrderDetail> listChild = new ArrayList<FirstOrderDetail>();
	private Context context;

	public MerchantOrderOneChildAdapter(List<FirstOrderDetail> listChild, Context context) {
		this.listChild = listChild;
		this.context = context;
	}

	@Override
	public int getCount() {
		return listChild.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listChild.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return listChild.size();
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
			view = layoutInflator.inflate(R.layout.item_order_two_child, null);
			holder.tv_product = (TextView) view.findViewById(R.id.tv_product);
			holder.tv_level = (TextView) view.findViewById(R.id.tv_level);
			holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
			holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			holder.btn_apply_refund=(Button) view.findViewById(R.id.btn_apply_refund);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		if("1".equals(MyApplication.getInstance().getTlr_type())){
			holder.btn_apply_refund.setVisibility(View.GONE);
		}
		holder.tv_product.setText(listChild.get(position).getCommodityName());
		holder.tv_level.setText("级别："+listChild.get(position).getLevelName());
		holder.tv_num.setText("x"+listChild.get(position).getBuynumber());
		holder.tv_price.setText("￥"+listChild.get(position).getPrice()+"/"+listChild.get(position).getCompanyname());
		holder.tv_product.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			Intent intent=new Intent(context,ProductDetailActivity.class);
			intent.putExtra("idCommodity",listChild.get(position).getIdCommodity());
			intent.putExtra("idLevel", listChild.get(position).getIdLevel());
			context.startActivity(intent);
			}
		});

		return view;
	}

	public static class Holder {
		TextView tv_product;
		TextView tv_level;
		TextView tv_num;
		TextView tv_price;
		Button btn_apply_refund;
	}

}
