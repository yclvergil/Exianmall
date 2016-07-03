package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.OrderDetail;
import com.cnmobi.exianmall.type.activity.ProductDetailActivity;

/**
 * 底部菜单栏卖家-订单页面，待发货适配器
 * 
 */
public class MerchantOrderTwoChildAdapter extends BaseAdapter {

	private List<OrderDetail> listChild = new ArrayList<OrderDetail>();
	private Context context;

	public MerchantOrderTwoChildAdapter(List<OrderDetail> listChild, Context context) {
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
	public View getView(int position, View view, ViewGroup parent) {
		Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
			view = layoutInflator.inflate(R.layout.item_merchant_order_one_child, null);
			holder.tv_product = (TextView) view.findViewById(R.id.tv_product);
			holder.tv_level = (TextView) view.findViewById(R.id.tv_level);
			holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
			holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		holder.tv_product.setText(listChild.get(position).getCommodityname());
		holder.tv_level.setText("级别："+listChild.get(position).getLevelname());
		holder.tv_num.setText("x"+listChild.get(position).getBuynumber());
		holder.tv_price.setText("￥"+listChild.get(position).getBuyprice()+"/"+listChild.get(position).getCompanyname());
		holder.tv_product.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			Intent intent=new Intent(context,ProductDetailActivity.class);
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
	}

}
