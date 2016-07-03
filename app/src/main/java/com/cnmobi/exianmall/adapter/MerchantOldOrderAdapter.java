package com.cnmobi.exianmall.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.OldOrderBean;
import com.cnmobi.exianmall.bean.OrderDetail;
import com.cnmobi.exianmall.bean.OrderGroupBean;
import com.lidroid.xutils.util.LogUtils;

/**
 * 卖家-历史订单适配器
 * 
 */
public class MerchantOldOrderAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<OrderGroupBean> group;
//	private List<List<OrderDetail>> child;

	public MerchantOldOrderAdapter(Context context, List<OrderGroupBean> group) {
		this.context = context;
		this.group = group;
//		this.child = child;
	}

	@Override
	public int getGroupCount() {
		return group.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
//		return child.get(groupPosition).size();
		return group.get(groupPosition).getOrderDetail().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return group.get(groupPosition).getOrderDetail().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * 显示：group
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_merchant_old_order_group, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.tv_title);
			holder.iv_flag = (ImageView) convertView.findViewById(R.id.iv_flag);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView.setText("订单号："+group.get(groupPosition).getFirstlevelorderNo()+"\n下单时间："+group.get(groupPosition).getCreationordertime());
		if (!isExpanded) {
			holder.iv_flag.setImageResource(R.drawable.ic_down);
		} else {
			holder.iv_flag.setImageResource(R.drawable.ic_up);
		}
		return convertView;

	}

	/**
	 * 显示：child
	 */
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_old_order_child, null);
			holder = new ViewHolder();
			holder.tv_product = (TextView) convertView.findViewById(R.id.tv_product);
			holder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_product.setText(group.get(groupPosition).getOrderDetail().get(childPosition).getCommodityname());
		holder.tv_level.setText("级别："+group.get(groupPosition).getOrderDetail().get(childPosition).getLevelname());
		holder.tv_num.setText("x"+group.get(groupPosition).getOrderDetail().get(childPosition).getBuynumber());
//		holder.ratingBar.setRating((float) 4.0);
		if ("".equals(group.get(groupPosition).getOrderDetail().get(childPosition).getScore())) {
			holder.ratingBar.setVisibility(View.GONE);
		} else {
			Float f = Float.parseFloat(group.get(groupPosition).getOrderDetail().get(childPosition).getScore());
			holder.ratingBar.setRating(f);
		}
		
		return convertView;
	}

	class ViewHolder {
		TextView textView;
		TextView tv_product;
		TextView tv_level;
		TextView tv_num;
		RatingBar ratingBar;
		ImageView iv_flag;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
