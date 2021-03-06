package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.FirstOrderDetail;
import com.cnmobi.exianmall.bean.OrderGroupBean;
import com.cnmobi.exianmall.interfaces.onOrderScoreItemClickListener;
import com.cnmobi.exianmall.widget.MyListView;

/**
 * 底部菜单栏卖家-订单页面，待发货适配器
 * 
 */
public class MerchantOrderTwoGroupAdapter extends BaseAdapter {

	private List<OrderGroupBean> listGroup = new ArrayList<OrderGroupBean>();
	private List<FirstOrderDetail> listChild = new ArrayList<FirstOrderDetail>();
	private Context context;
	private MerchantOrderOneChildAdapter adapter;
	private ArrayList<HashMap<Object, Boolean>> flagList = new ArrayList<HashMap<Object, Boolean>>();

	public MerchantOrderTwoGroupAdapter(List<OrderGroupBean> listGroup, Context context) {
		this.listGroup = listGroup;
		this.context = context;
		
	}

	@Override
	public int getCount() {
		return listGroup.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listGroup.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return listGroup.size();
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
			view = layoutInflator.inflate(R.layout.item_merchant_order_one_group, null);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_creattime = (TextView) view.findViewById(R.id.tv_creattime);
			holder.tv_count = (TextView) view.findViewById(R.id.tv_count);
			holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
			holder.tv_state = (TextView) view.findViewById(R.id.tv_state);
			holder.listView = (MyListView) view.findViewById(R.id.lv_product);
			holder.rv_title = (RelativeLayout) view.findViewById(R.id.rv_title);
			holder.iv_flag = (ImageView) view.findViewById(R.id.iv_flag);
			holder.iv_urgent = (ImageView) view.findViewById(R.id.iv_urgent);
			holder.ly_list = (LinearLayout) view.findViewById(R.id.ly_list);
//			holder.btn_ok = (Button) view.findViewById(R.id.btn_ok);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		
		holder.tv_title.setText("订单号:"+listGroup.get(position).getFirstlevelorderNo());
		holder.tv_creattime.setText("下单时间："+listGroup.get(position).getCreationordertime());
//		holder.tv_count.setText("总计：");
		holder.tv_money.setText("￥"+listGroup.get(position).getOrderprice()+"元");
//		订单状态总共以下几钟 0待处理，1=已下单,2=已接单，3=已拒单，4=已取消，5=已签收,6=已发货
//		未结算： 已发货，已接单，已签收，
//		待发货：已接单，	
		switch (listGroup.get(position).getOrderstatus()) {
		case 0:
			holder.tv_state.setText("待处理");
			break;
		case 1:
			holder.tv_state.setText("已下单");
			break;
		case 2:
			holder.tv_state.setText("已接单");
			break;
		case 3:
			holder.tv_state.setText("已拒单");
			break;
		case 4:
			holder.tv_state.setText("已取消");
			break;
		case 5:
			holder.tv_state.setText("已签收");
			break;
		case 6:
			holder.tv_state.setText("已发货");
			break;
		default:
			break;
		}
		if("Y".equals(listGroup.get(position).getIsurgent())){
			holder.iv_urgent.setVisibility(View.VISIBLE);
		}else if("N".equals(listGroup.get(position).getIsurgent())){
			holder.iv_urgent.setVisibility(View.GONE);
		}
//		listChild=listGroup.get(position).getSellerorderDetails();
		listChild=listGroup.get(position).getFirstOrderDetail();
		adapter = new MerchantOrderOneChildAdapter(listChild, context);
		holder.listView.setAdapter(adapter);

		// 设置列表展开和隐藏
		if(flagList.size()== listGroup.size()){
		if (flagList.get(position).get(position)) {
			holder.ly_list.setVisibility(View.VISIBLE);
			holder.iv_flag.setImageResource(R.drawable.ic_up);
		} else {
			holder.ly_list.setVisibility(View.GONE);
			holder.iv_flag.setImageResource(R.drawable.ic_down);
		}
		}

		// 设置点击展开收缩
		holder.rv_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean flag = flagList.get(position).get(position);
				HashMap<Object, Boolean> map = new HashMap<Object, Boolean>();
				map.put(position, !flag);
				flagList.set(position, map);
				notifyDataSetChanged();
			}
		});
		
//		holder.btn_ok.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View view) {
//				// TODO Auto-generated method stub
//				if(mListener!=null){
//					mListener.onOrderScoreItemClick(view, position, listGroup.get(position).getFirstlevelorderNo());
//					
//				}
//			}
//		});

		return view;
	}

	public static class Holder {
		TextView tv_title;
		TextView tv_creattime;
		TextView tv_count;
		TextView tv_money;
		TextView tv_state;
		MyListView listView;
		RelativeLayout rv_title;
		ImageView iv_flag;
		ImageView iv_urgent;
		LinearLayout ly_list;
//		Button btn_ok;
	}
	public void updateFlag(int size) {
		// 初始化，所有列表默认展开
		for (int i = 0; i < size; i++) {
			HashMap<Object, Boolean> map = new HashMap<Object, Boolean>();
			map.put(flagList.size(), true);
			flagList.add(map);
		}
	}

	public void clearFlag() {
		flagList.clear();
	}
	
	onOrderScoreItemClickListener mListener;

	public void setonOrderScoreItemClickListenerListener(
			onOrderScoreItemClickListener listener) {
		mListener = listener;
	}
}
