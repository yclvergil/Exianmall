package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.OrderDetail;
import com.cnmobi.exianmall.bean.OrderGroupBean;
import com.cnmobi.exianmall.interfaces.IOrderApplyRefundClickListen;
import com.cnmobi.exianmall.interfaces.onOrderScoreItemClickListener;
import com.cnmobi.exianmall.widget.MyListView;

/**
 * 底部菜单栏订单页面，待评分适配器
 * 
 */
public class OrderThreeGroupAdapter extends BaseAdapter {

	private List<OrderGroupBean> listGroup = new ArrayList<OrderGroupBean>();
	private List<OrderDetail> listChild = new ArrayList<OrderDetail>();
	private Context context;
	public OrderThreeChildAdapter adapter;
	private ArrayList<HashMap<Object, Boolean>> flagList = new ArrayList<HashMap<Object, Boolean>>();
	String signtime="";
	public OrderThreeGroupAdapter(List<OrderGroupBean> listGroup, Context context) {
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
		final Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
			view = layoutInflator.inflate(R.layout.item_order_three_group, null);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.listView = (MyListView) view.findViewById(R.id.lv_product);
			holder.rv_title = (RelativeLayout) view.findViewById(R.id.rv_title);
			holder.iv_flag = (ImageView) view.findViewById(R.id.iv_flag);
//			holder.iv_score = (ImageView) view.findViewById(R.id.iv_score);
			holder.ly_list = (LinearLayout) view.findViewById(R.id.ly_list);
			holder.tv_creattime = (TextView) view.findViewById(R.id.tv_creattime);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		// holder.tv_title.setText(listGroup.get(position).getCreationordertime());
		// holder.listView.setAdapter(adapter);

//		holder.tv_title.setText("订单号:" + listGroup.get(position).getFirstlevelorderNo());
		holder.tv_title.setText("订单号:" + listGroup.get(position).getSecondlevelorderNo());
		holder.tv_creattime.setText("下单时间：" + listGroup.get(position).getCreationordertime());
		// holder.tv_count.setText("10");
		// holder.tv_money.setText("￥"+listGroup.get(position).getOrderprice()+"元");
		listChild = listGroup.get(position).getOrderDetail();
		if(listGroup.size()!=0){
			signtime=listGroup.get(position).getSigntime();
			adapter = new OrderThreeChildAdapter(listChild, context,signtime);
			holder.listView.setAdapter(adapter);
			adapter.setonOrderScoreItemClickListenerListener(new onOrderScoreItemClickListener() {
				
				@Override
				public void onOrderScoreItemClick(View v, int position, String str) {
					// TODO Auto-generated method stub
					
				}
				
				
				@Override
				public void onOrderScoreItemClick(View v, int position1,
						int position2, float rating, String str) {
					// TODO Auto-generated method stub
					if (mListener != null) {
						mListener.onOrderScoreItemClick(v, position, position2, rating,str);
					}
				}
				
				
				@Override
				public void onOrderScoreItemClick(View v, int position1,
						int position2, float rating) {
					// TODO Auto-generated method stub
					
				}
			});
			
			adapter.setonOrderApplyRefundItemClickListener(new IOrderApplyRefundClickListen() {
				
				
				@Override
				public void onApplyRefundItemClick(View v, int position1,
						int position2, int flag) {
					// TODO Auto-generated method stub
					if(mListener1!=null){
						mListener1.onApplyRefundItemClick(v, position, position2, flag);
					}
				}
			});
		}

		// 设置列表展开和隐藏
		if (flagList.size() == listGroup.size()) {

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
//		holder.iv_score.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// 对话框
//				LayoutInflater inflaterDl = LayoutInflater.from(context);
//				RelativeLayout relativeLayout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_oldorder_evaluate,
//						null);
//				Builder builder = new AlertDialog.Builder(context);
//				builder.setView(relativeLayout);
//				final Dialog dialog = builder.create();
//				dialog.show();
//				holder.et_evaluate_content = (EditText) relativeLayout.findViewById(R.id.et_evaluate_content);
//				Button btnCancel = (Button) relativeLayout.findViewById(R.id.btn_evaluate_cancel);
//				btnCancel.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						dialog.dismiss();
//					}
//				});
//				Button btnOK = (Button) relativeLayout.findViewById(R.id.btn_evaluate_ok);
//				btnOK.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						if (mListener != null) {
//							mListener.onOrderScoreItemClick(v, position, holder.et_evaluate_content.getText()
//									.toString().trim());
//							dialog.dismiss();
//						}
//
//						dialog.dismiss();
//					}
//				});
//			}
//		});

		return view;
	}

	onOrderScoreItemClickListener mListener;

	public void setonOrderScoreItemClickListenerListener(onOrderScoreItemClickListener listener) {
		mListener = listener;
	}

	IOrderApplyRefundClickListen mListener1;
	
	public void setonOrderApplyRefundItemClickListener(IOrderApplyRefundClickListen listener){
		mListener1=listener;
	}
	
	public static class Holder {
		TextView tv_title;
		TextView tv_creattime;
		MyListView listView;
		RelativeLayout rv_title;
		ImageView iv_flag;
//		ImageView iv_score;
		LinearLayout ly_list;
		EditText et_evaluate_content;
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

}
