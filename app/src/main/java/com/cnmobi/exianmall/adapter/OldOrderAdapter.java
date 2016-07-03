package com.cnmobi.exianmall.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.OldOrderBean;
import com.cnmobi.exianmall.bean.OrderDetail;
import com.cnmobi.exianmall.bean.OrderGroupBean;
import com.cnmobi.exianmall.interfaces.IAlterEvaluateClickListen;
import com.cnmobi.exianmall.mine.activity.MineMessageActivity;
import com.cnmobi.exianmall.type.activity.ProductDetailActivity;
import com.cnmobi.exianmall.type.activity.ShowTypeActivity;
import com.lidroid.xutils.util.LogUtils;

/**
 * 历史订单适配器
 * 
 */
public class OldOrderAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<OrderGroupBean> group;
//	private List<List<OrderDetail>> child;
	long orderTime;
	boolean isAlter=false;
	public OldOrderAdapter(Context context, List<OrderGroupBean> group) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_old_order_group, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.tv_title);
			holder.iv_flag = (ImageView) convertView.findViewById(R.id.iv_flag);
			holder.iv_evaluate = (ImageView) convertView.findViewById(R.id.iv_evaluate);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView.setText("订单号："+group.get(groupPosition).getSecondlevelorderNo()+"\n下单时间："+group.get(groupPosition).getCreationordertime());
		if (!isExpanded) {
			holder.iv_flag.setImageResource(R.drawable.ic_down);
		} else {
			holder.iv_flag.setImageResource(R.drawable.ic_up);
		}
		holder.iv_evaluate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 对话框
				LayoutInflater inflaterDl = LayoutInflater.from(context);
				RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
						.inflate(R.layout.dialog_oldorder_evaluate, null);
				Builder builder = new AlertDialog.Builder(context);
				builder.setView(relativeLayout);
				final Dialog dialog = builder.create();
				dialog.show();
				Button btnCancel = (Button) relativeLayout.findViewById(R.id.btn_evaluate_cancel);
				btnCancel.setOnClickListener(new OnClickListener() {//取消

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				Button btnOK = (Button) relativeLayout.findViewById(R.id.btn_evaluate_ok);
				btnOK.setOnClickListener(new OnClickListener() {//确认

					@Override
					public void onClick(View v) {
						//以下调评价接口
						Toast.makeText(context,"无调用接口", Toast.LENGTH_SHORT).show();
						dialog.dismiss();

					}
				});
			}
		});
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
			holder.btn_select_evaluate=(Button) convertView.findViewById(R.id.btn_select_evaluate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.btn_select_evaluate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				// 对话框

				// TODO Auto-generated method stub
				// 对话框
				LayoutInflater inflaterDl = LayoutInflater.from(context);
				RelativeLayout relativeLayout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_oldorder_evaluate,
						null);
				Builder builder = new AlertDialog.Builder(context);
				builder.setView(relativeLayout);
				final Dialog dialog = builder.create();
				dialog.show();
				final TextView textView=(TextView) relativeLayout.findViewById(R.id.tv_evaluate_title);
				final EditText et_evaluate_content = (EditText) relativeLayout.findViewById(R.id.et_evaluate_content);
				et_evaluate_content.setFocusable(false);
				et_evaluate_content.setText(group.get(groupPosition).getOrderDetail().get(childPosition).getEvaluatecontent());
				textView.setText("您对此次购物的评价：");
				final RatingBar ratingBar=(RatingBar) relativeLayout.findViewById(R.id.ratingBar1);
				if ("".equals(group.get(groupPosition).getOrderDetail().get(childPosition).getScore())) {
//					ratingBar.setVisibility(View.GONE);
					ratingBar.setRating(4);
				} else {
					Float f = Float.parseFloat(group.get(groupPosition).getOrderDetail().get(childPosition).getScore());
					ratingBar.setRating(f);
				}
				Button btnCancel = (Button) relativeLayout.findViewById(R.id.btn_evaluate_cancel);
				btnCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
//	
				final Button btnOK = (Button) relativeLayout
						.findViewById(R.id.btn_evaluate_ok);
					btnOK.setText("修改评价");
					ratingBar.setIsIndicator(true);
					btnOK.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(!isAlter){
//							Toast.makeText(context, "缺接口", Toast.LENGTH_SHORT).show();
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String date1=group.get(groupPosition).getOrderDetail().get(childPosition).getEvaluatetime();//订单时间 -暂未返回
								try {
									orderTime=format.parse(date1).getTime();
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								long oneDay=24*60*60*1000;
								if(orderTime>=(new Date().getTime()-oneDay)){
									isAlter=true;
									ratingBar.setRating(4);
									et_evaluate_content.setText("");
									btnOK.setText("确认");
									ratingBar.setIsIndicator(false);
									textView.setText("请输入您对此次购物的评价：");
									et_evaluate_content.setFocusableInTouchMode(true);
								}else{
									Toast.makeText(context, "评价之后的24小时内才可以修改您的评价哦~", Toast.LENGTH_LONG).show();
								}
							}else{
//								Toast.makeText(context, "缺接口~", Toast.LENGTH_LONG).show();
								isAlter=false;
								if(mListener!=null){
									if(!TextUtils.isEmpty(et_evaluate_content.getText().toString().trim())){
										mListener.onAlterEvaluate(v, groupPosition, childPosition, et_evaluate_content.getText().toString().trim(), ratingBar.getRating());
										dialog.dismiss();
									}else{
										Toast.makeText(context, "评价内容不能为空！", Toast.LENGTH_SHORT).show();
									}
								}
							}
//							isAlter=true;
						}
					});
			
			}
		});
		holder.tv_product.setText(group.get(groupPosition).getOrderDetail().get(childPosition).getCommodityname());
		holder.tv_product.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ProductDetailActivity.class);
				intent.putExtra("idCommodity",group.get(groupPosition).getOrderDetail().get(childPosition).getIdCommodity());
				intent.putExtra("idLevel", group.get(groupPosition).getOrderDetail().get(childPosition).getIdLevel());
				intent.putExtra("imageUrl",group.get(groupPosition).getOrderDetail().get(childPosition).getImagename());
				context.startActivity(intent);
				
			}
		});
		holder.tv_level.setText("级别："+group.get(groupPosition).getOrderDetail().get(childPosition).getLevelname());
		holder.tv_num.setText("x "+group.get(groupPosition).getOrderDetail().get(childPosition).getBuynumber());
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
		ImageView iv_evaluate;//评价
		Button btn_select_evaluate;
	}

	IAlterEvaluateClickListen mListener;
	
	public void setOnAlterEvaluateClickListen(IAlterEvaluateClickListen listener){
		mListener=listener;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
