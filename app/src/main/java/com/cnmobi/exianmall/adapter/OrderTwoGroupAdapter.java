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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.bean.OrderDetail;
import com.cnmobi.exianmall.bean.OrderGroupBean;
import com.cnmobi.exianmall.interfaces.IOrderApplyRefundClickListen;
import com.cnmobi.exianmall.interfaces.onOrderScoreItemClickListener;
import com.cnmobi.exianmall.widget.MyListView;
import com.lidroid.xutils.util.LogUtils;

/**
 * 底部菜单栏订单页面，待收货适配器
 * 
 */
public class OrderTwoGroupAdapter extends BaseAdapter {

	private List<OrderGroupBean> listGroup = new ArrayList<OrderGroupBean>();
	private List<OrderDetail> listChild = new ArrayList<OrderDetail>();
	private Context context;
	private OrderTwoChildAdapter adapter;
	private ArrayList<HashMap<Object, Boolean>> flagList = new ArrayList<HashMap<Object, Boolean>>();

	public OrderTwoGroupAdapter(List<OrderGroupBean> listGroup, Context context) {
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
			view = layoutInflator.inflate(R.layout.item_order_two_group, null);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_creattime = (TextView) view.findViewById(R.id.tv_creattime);
			holder.tv_count = (TextView) view.findViewById(R.id.tv_count);
			holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
			holder.tv_state = (TextView) view.findViewById(R.id.tv_state);
			holder.listView = (MyListView) view.findViewById(R.id.lv_product);
			holder.rv_title = (RelativeLayout) view.findViewById(R.id.rv_title);
			holder.iv_flag = (ImageView) view.findViewById(R.id.iv_flag);
			holder.iv_scan = (ImageView) view.findViewById(R.id.iv_scan);
			holder.ly_list = (LinearLayout) view.findViewById(R.id.ly_list);
			holder.btn_ok = (Button) view.findViewById(R.id.btn_ok);
			holder.tv_xiaoji=(TextView) view.findViewById(R.id.tv_xiaoji);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

			holder.tv_xiaoji.setVisibility(View.VISIBLE);
			holder.tv_count.setVisibility(View.VISIBLE);
			holder.tv_title.setText("订单号:" + listGroup.get(position).getSecondlevelorderNo());
			holder.tv_creattime.setText("下单时间：" + listGroup.get(position).getCreationordertime());
			holder.tv_money.setText("￥" + listGroup.get(position).getOrderprice() + "元");
			holder.tv_count.setText("共" + listGroup.get(position).getSubtotal() + "件");
			
//		}
		// 订单状态，0=待支付，1=待处理，5=已接单，2=已发货，3=已签收，4=已取消
		switch (listGroup.get(position).getOrderstatus()) {
		case 0:
			holder.tv_state.setText("待支付");
			break;
		case 1:
			holder.tv_state.setText("待处理");
			break;
		case 2:
			holder.tv_state.setText("已发货");
			break;
		case 3:
			holder.tv_state.setText("已签收");
			break;
		case 4:
			holder.tv_state.setText("已取消");
			break;
		case 5:
			holder.tv_state.setText("已接单");
			break;
		default:
			break;
		}
		listChild = listGroup.get(position).getOrderDetail();
		adapter = new OrderTwoChildAdapter(listChild, context,1);
		holder.listView.setAdapter(adapter);
		adapter.setonOrderApplyRefundItemClickListener(new IOrderApplyRefundClickListen() {
			
			@Override
			public void onApplyRefundItemClick(View v, int position1, int position2,
					int flag) {
				// TODO Auto-generated method stub
				if(mListener1!=null){
					mListener1.onApplyRefundItemClick(v, position, position2, flag);
				}
			}
		});
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

		holder.btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// 对话框
				LayoutInflater inflaterDl = LayoutInflater.from(context);
				RelativeLayout relativeLayout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_order_evaluate,
						null);
				Builder builder = new AlertDialog.Builder(context);
				builder.setView(relativeLayout);
				final Dialog dialog = builder.create();
				dialog.show();
				Button btnCancel = (Button) relativeLayout.findViewById(R.id.btn_evaluate_cancel);
				relativeLayout.findViewById(R.id.ll_score).setVisibility(View.GONE);
				((TextView) relativeLayout.findViewById(R.id.tv_title)).setText("亲爱的用户！请您真的收到货物之后，才点击确认收货哦！");
				btnCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				Button btnOK = (Button) relativeLayout.findViewById(R.id.btn_evaluate_ok);
				btnOK.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (mListener != null) {
							mListener.onOrderScoreItemClick(v, 0, listGroup.get(position).getIdNumber()+"");
							dialog.dismiss();
						}
					}
				});

			}
		});

		holder.iv_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(context, CaptureActivity.class);
				// context.startActivity(intent);
				if (mListener != null) {
					mListener.onOrderScoreItemClick(v, 1, listGroup.get(position).getIdNumber()+"");
				}
			}
		});

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
		TextView tv_count;
		TextView tv_money;
		TextView tv_state;
		TextView tv_xiaoji;
		MyListView listView;
		RelativeLayout rv_title;
		ImageView iv_flag;
		ImageView iv_scan;
		LinearLayout ly_list;
		Button btn_ok;
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
