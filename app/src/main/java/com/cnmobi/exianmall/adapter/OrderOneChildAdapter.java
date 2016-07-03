package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.OrderDetail;
import com.cnmobi.exianmall.interfaces.IOrderApplyRefundClickListen;
import com.cnmobi.exianmall.mine.activity.MineMessageActivity;
import com.cnmobi.exianmall.type.activity.ProductDetailActivity;
import com.cnmobi.exianmall.widget.MyListView;

/**
 * 底部菜单栏订单页面，待付款适配器
 * 
 */
public class OrderOneChildAdapter extends BaseAdapter {

	private List<OrderDetail> listChild = new ArrayList<OrderDetail>();
	private Context context;
	int flag;
	public OrderOneChildAdapter(List<OrderDetail> listChild, Context context) {
		this.listChild = listChild;
		this.context = context;
	}
	
	/**
	 * 
	 * @param listChild
	 * @param context
	 * @param flag  =0待付款，1确认收货
	 * 
	 */
	public OrderOneChildAdapter(List<OrderDetail> listChild, Context context,int flag) {
		this.listChild = listChild;
		this.context = context;
		this.flag=flag;
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
			view = layoutInflator.inflate(R.layout.item_order_one_child, null);
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
			Intent intent = new Intent(context, ProductDetailActivity.class);
			intent.putExtra("idCommodity",listChild.get(position).getIdCommodity());
			intent.putExtra("idLevel", listChild.get(position).getIdLevel());
			intent.putExtra("imageUrl", listChild.get(position).getImagename());
			context.startActivity(intent);
			}
		});

		return view;
	}
	IOrderApplyRefundClickListen mListener;
	
	public void setonOrderApplyRefundItemClickListener(IOrderApplyRefundClickListen listener){
		mListener=listener;
	}
	public static class Holder {
		TextView tv_product;
		TextView tv_level;
		TextView tv_num;
		TextView tv_price;
	}

}
