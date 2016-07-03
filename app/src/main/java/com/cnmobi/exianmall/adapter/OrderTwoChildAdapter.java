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
public class OrderTwoChildAdapter extends BaseAdapter {

	private List<OrderDetail> listChild = new ArrayList<OrderDetail>();
	private Context context;
	int flag;
	public OrderTwoChildAdapter(List<OrderDetail> listChild, Context context) {
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
	public OrderTwoChildAdapter(List<OrderDetail> listChild, Context context,int flag) {
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
			view = layoutInflator.inflate(R.layout.item_order_two_child, null);
			holder.tv_product = (TextView) view.findViewById(R.id.tv_product);
			holder.tv_level = (TextView) view.findViewById(R.id.tv_level);
			holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
			holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			holder.btn_apply_refund=(Button) view.findViewById(R.id.btn_apply_refund);
			holder.btn_apply_refund.setVisibility(View.GONE);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		
		
		if("Y".equals(listChild.get(position).getIsRefund())&& flag==1){//这里判断商品的退款的状态
			holder.btn_apply_refund.setVisibility(View.VISIBLE);
			holder.btn_apply_refund.setText("退款中");
			holder.btn_apply_refund.setEnabled(false);
		}else if(flag==1){
			holder.btn_apply_refund.setText("申请退款");
			holder.btn_apply_refund.setEnabled(true);
			holder.btn_apply_refund.setVisibility(View.VISIBLE);
			holder.btn_apply_refund.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//				if(){如果成功，弹出提示对话框 现接口暂缺
//					
//				}
					if(mListener!=null){
						mListener.onApplyRefundItemClick(v, 0, position, 0);
					}
					
//					LayoutInflater inflaterDl = LayoutInflater.from(context);
//					RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
//							.inflate(R.layout.dialog_apply_refund, null);
//					Builder builder = new AlertDialog.Builder(context);
//					builder.setView(relativeLayout);
//					final Dialog dialog = builder.create();
//					dialog.show();
//					TextView tv=(TextView) relativeLayout.findViewById(R.id.tv_content);
//					tv.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//						}
//					});
					
				}
			});
		}else{
			holder.btn_apply_refund.setVisibility(View.GONE);
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
		Button btn_apply_refund;
	}

}
