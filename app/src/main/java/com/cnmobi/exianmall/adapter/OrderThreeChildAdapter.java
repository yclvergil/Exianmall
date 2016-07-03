package com.cnmobi.exianmall.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.OrderDetail;
import com.cnmobi.exianmall.interfaces.IAlterEvaluateClickListen;
import com.cnmobi.exianmall.interfaces.IOrderApplyRefundClickListen;
import com.cnmobi.exianmall.interfaces.OnAddCarItemClickListener;
import com.cnmobi.exianmall.interfaces.onOrderScoreItemClickListener;
import com.cnmobi.exianmall.type.activity.ProductDetailActivity;
import com.lidroid.xutils.util.LogUtils;

/**
 * 底部菜单栏订单页面，待评分适配器
 * 
 */
public class OrderThreeChildAdapter extends BaseAdapter {

	private List<OrderDetail> listChild = new ArrayList<OrderDetail>();
	private Context context;
	long orderTime;
	boolean isEvaluate=false;
	boolean isAlter=false;
	String signtime="";
	public OrderThreeChildAdapter(List<OrderDetail> listChild2, Context context,String signtime) {
		this.listChild = listChild2;
		this.context = context;
		this.signtime= signtime;
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
		final Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater layoutInflator = LayoutInflater.from(parent
					.getContext());
			view = layoutInflator
					.inflate(R.layout.item_order_three_child, null);
			holder.tv_product = (TextView) view.findViewById(R.id.tv_product);
			holder.tv_level = (TextView) view.findViewById(R.id.tv_level);
			holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
			holder.btn_score = (Button) view.findViewById(R.id.btn_score);
			holder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar1);
			holder.btn_apply_refund=(Button) view.findViewById(R.id.btn_apply_refund);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		holder.tv_product.setText(listChild.get(position).getCommodityname());
		holder.tv_level.setText("级别：" + listChild.get(position).getLevelname());
		holder.tv_num.setText("x" + listChild.get(position).getBuynumber());
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
		
		if("Y".equals(listChild.get(position).getIsRefund())){//这里判断商品的退款的状态 暂缺
			
			holder.btn_apply_refund.setText("退款中");
			holder.btn_apply_refund.setEnabled(false);
		}else {
			holder.btn_apply_refund.setText("申请退款");
			holder.btn_apply_refund.setEnabled(true);
			holder.btn_apply_refund.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				年2016月month日03.22 星期二
					try {
						orderTime=format.parse(signtime).getTime();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					long twoHours=2*60*60*1000;
					//判断订单时间是否超出2小时
					if(orderTime>=(new Date().getTime()-twoHours)){
						//在这里做退款处理-暂缺接口
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
						
						if(mListener1!=null){
							mListener1.onApplyRefundItemClick(v, 0, position,1);
						}
					}else{
						Toast.makeText(context, "确认收货后的2小时之内才能申请退款哦~", Toast.LENGTH_LONG).show();
					}
				}
			});
		}
		
		if ("".equals(listChild.get(position).getScore())) {
//			holder.btn_score.setText("查看评价");
//			isEvaluate=true;
			holder.btn_score.setVisibility(View.VISIBLE);
			holder.ratingBar.setVisibility(View.GONE);
			holder.btn_score.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
			// 对话框
			
//			LayoutInflater inflaterDl = LayoutInflater.from(context);
//			RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
//					.inflate(R.layout.dialog_order_evaluate, null);
//			Builder builder = new AlertDialog.Builder(context);
//			builder.setView(relativeLayout);
//			final Dialog dialog = builder.create();
//			dialog.show();
//			holder.rb = (RatingBar) relativeLayout
//					.findViewById(R.id.ratingBar1);
//			Button btnCancel = (Button) relativeLayout
//					.findViewById(R.id.btn_evaluate_cancel);
//			btnCancel.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
				LayoutInflater inflaterDl = LayoutInflater.from(context);
				RelativeLayout relativeLayout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_oldorder_evaluate,
						null);
				Builder builder = new AlertDialog.Builder(context);
				builder.setView(relativeLayout);
				final Dialog dialog = builder.create();
				dialog.show();
				final TextView textView=(TextView) relativeLayout.findViewById(R.id.tv_evaluate_title);
				final EditText et_evaluate_content = (EditText) relativeLayout.findViewById(R.id.et_evaluate_content);
				final RatingBar ratingBar=(RatingBar) relativeLayout.findViewById(R.id.ratingBar1);
				ratingBar.setRating(4);
				Button btnCancel = (Button) relativeLayout.findViewById(R.id.btn_evaluate_cancel);
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
							if(!TextUtils.isEmpty(et_evaluate_content.getText().toString().trim())){
								mListener.onOrderScoreItemClick(v, 0,position,ratingBar.getRating(),et_evaluate_content.getText()
										.toString().trim());
								dialog.dismiss();
								
							}else{
								Toast.makeText(context, "评价内容不能为空！", Toast.LENGTH_LONG).show();
							}
//							mListener.onOrderScoreItemClick(v, position, et_evaluate_content.getText()
//									.toString().trim());
//							dialog.dismiss();
						}

					}
				});
//			Button btnOK = (Button) relativeLayout
//					.findViewById(R.id.btn_evaluate_ok);
//			btnOK.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
////					holder.btn_score.setVisibility(View.GONE);
////					holder.ratingBar.setRating(holder.rb.getRating());
////					holder.ratingBar.setVisibility(View.VISIBLE);
////					dialog.dismiss();
//					if (mListener != null) {
//						mListener.onOrderScoreItemClick(v, 0,position,holder.rb.getRating());
//						holder.btn_score.setVisibility(View.GONE);
//						holder.ratingBar.setRating(holder.rb.getRating());
//						holder.ratingBar.setVisibility(View.VISIBLE);
//						dialog.dismiss();
//					}
//
//				}
//			});
				}
			});
			
		}else{
			holder.btn_score.setText("评价");
			isEvaluate=false;
			holder.btn_score.setVisibility(View.GONE);
			holder.ratingBar.setVisibility(View.VISIBLE);
//			Float f = Float.parseFloat(listChild.get(position).getScore());
			holder.ratingBar.setRating(Float.valueOf(listChild.get(position).getScore()));
		}

		
//			holder.btn_score.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					if (!"".equals(listChild.get(position).getScore()+"")) {
//						//已评价
//						isEvaluate=true;
//					}else{
//						isEvaluate=false;
//					}
//					// TODO Auto-generated method stub
//					LayoutInflater inflaterDl = LayoutInflater.from(context);
//					RelativeLayout relativeLayout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_oldorder_evaluate,
//							null);
//					Builder builder = new AlertDialog.Builder(context);
//					builder.setView(relativeLayout);
//					final Dialog dialog = builder.create();
//					dialog.show();
//					final TextView textView=(TextView) relativeLayout.findViewById(R.id.tv_evaluate_title);
//					final EditText et_evaluate_content = (EditText) relativeLayout.findViewById(R.id.et_evaluate_content);
////					textView.setText("请输入您对此次购物的评价：");
//					et_evaluate_content.setFocusable(true);
//					final RatingBar ratingBar=(RatingBar) relativeLayout.findViewById(R.id.ratingBar1);
////						ratingBar.setRating(4);
//					Button btnCancel = (Button) relativeLayout.findViewById(R.id.btn_evaluate_cancel);
//					final Button btnOK = (Button) relativeLayout.findViewById(R.id.btn_evaluate_ok);
//					LogUtils.e("-----isEvaluate-----"+isEvaluate);
//					if(!isEvaluate){//未评价
//						ratingBar.setIsIndicator(false);
//						textView.setText("请输入您对此次购物的评价：");
//						et_evaluate_content.setFocusable(true);
//					}else{//已评价-查看
//						et_evaluate_content.setText(listChild.get(position).getEvaluatecontent());
//						ratingBar.setRating(Float.valueOf(listChild.get(position).getScore()));
//						textView.setText("您对此次购物的评价：");
//						ratingBar.setIsIndicator(true);
//						et_evaluate_content.setFocusable(false);
//						btnOK.setText("修改评价");
//					}
//					btnCancel.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							dialog.dismiss();
//						}
//					});
//					
//					btnOK.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							if(!isEvaluate){//未评价
////								btnOK.setText("确认");
////								textView.setText("请输入您对此次购物的评价：");
//								ratingBar.setIsIndicator(false);
//								et_evaluate_content.setFocusable(true);
//								if (mListener != null) {
//									mListener.onOrderScoreItemClick(v, 0,position,ratingBar.getRating(),et_evaluate_content.getText()
//											.toString().trim());
////									holder.btn_score.setVisibility(View.GONE);
////									holder.ratingBar.setRating(ratingBar.getRating());
////									holder.ratingBar.setVisibility(View.VISIBLE);
////									mListener.onOrderScoreItemClick(v, position, et_evaluate_content.getText()
////											.toString().trim());
//									dialog.dismiss();
//								}
//							}else{//已评价-查看
//								ratingBar.setIsIndicator(true);
//								btnOK.setText("修改评价");
////								textView.setText("您对此次购物的评价：");
////								et_evaluate_content.setText(listChild.get(position).getEvaluatecontent());
////								et_evaluate_content.setFocusable(false);
////								ratingBar.setRating(Float.valueOf(listChild.get(position).getScore()));
//								if(!isAlter){
////								Toast.makeText(context, "缺接口", Toast.LENGTH_SHORT).show();
//									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//									String date1=listChild.get(position).getEvaluatetime();//订单时间 -暂未返回
//									//年2016月month日03.22 星期二
//									try {
//										orderTime=format.parse(date1).getTime();
//									} catch (ParseException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//									long oneDay=24*60*60*1000;
//									if(orderTime>=(new Date().getTime()-oneDay)){
//										isAlter=true;
//										ratingBar.setRating(4);
//										et_evaluate_content.setText("");
//										btnOK.setText("确认");
//										textView.setText("请输入您对此次购物的评价：");
//										ratingBar.setIsIndicator(false);
//										et_evaluate_content.setFocusableInTouchMode(true);
//									}else{
//										ratingBar.setIsIndicator(true);
//										Toast.makeText(context, "评价之后的24小时内才可以修改您的评价哦~", Toast.LENGTH_LONG).show();
//									}
//								}else{
//									ratingBar.setIsIndicator(false);
//									Toast.makeText(context, "缺接口~", Toast.LENGTH_LONG).show();
//									isAlter=false;
//									if(mListener2!=null){
//										mListener2.onAlterEvaluate(v, 0, position, et_evaluate_content.getText().toString().trim(), ratingBar.getRating());
//									}
//									dialog.dismiss();
//								}
////								isAlter=false;
//							}
//							
//							
////							holder.btn_score.setVisibility(View.GONE);
////							holder.ratingBar.setRating(holder.rb.getRating());
////							holder.ratingBar.setVisibility(View.VISIBLE);
////							dialog.dismiss();
////							Toast.makeText(context, "111", Toast.LENGTH_LONG).show();
////							if (mListener != null) {
////								mListener.onOrderScoreItemClick(v, 0,position,ratingBar.getRating(),et_evaluate_content.getText()
////										.toString().trim());
//////								holder.btn_score.setVisibility(View.GONE);
//////								holder.ratingBar.setRating(ratingBar.getRating());
//////								holder.ratingBar.setVisibility(View.VISIBLE);
//////								mListener.onOrderScoreItemClick(v, position, et_evaluate_content.getText()
//////										.toString().trim());
////								dialog.dismiss();
////							}
//
//						}
//					});
//					
////					LayoutInflater inflaterDl = LayoutInflater.from(context);
////					RelativeLayout relativeLayout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_oldorder_evaluate,
////							null);
////					Builder builder = new AlertDialog.Builder(context);
////					builder.setView(relativeLayout);
////					final Dialog dialog = builder.create();
////					dialog.show();
////					final TextView textView=(TextView) relativeLayout.findViewById(R.id.tv_evaluate_title);
////					final EditText et_evaluate_content = (EditText) relativeLayout.findViewById(R.id.et_evaluate_content);
////					final RatingBar ratingBar=(RatingBar) relativeLayout.findViewById(R.id.ratingBar1);
//////					ratingBar.setRating(4);
////					Button btnCancel = (Button) relativeLayout.findViewById(R.id.btn_evaluate_cancel);
////					btnCancel.setOnClickListener(new OnClickListener() {
////
////						@Override
////						public void onClick(View v) {
////							dialog.dismiss();
////						}
////					});
////					Button btnOK = (Button) relativeLayout.findViewById(R.id.btn_evaluate_ok);
////					btnOK.setOnClickListener(new OnClickListener() {
////
////						@Override
////						public void onClick(View v) {
////							if (mListener != null) {
////								mListener.onOrderScoreItemClick(v, position, holder.et_evaluate_content.getText()
////										.toString().trim());
////								dialog.dismiss();
////							}
////
////							dialog.dismiss();
////						}
////					});
//				
////					LayoutInflater inflaterDl = LayoutInflater.from(context);
////					RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
////							.inflate(R.layout.dialog_order_evaluate, null);
////					Builder builder = new AlertDialog.Builder(context);
////					builder.setView(relativeLayout);
////					final Dialog dialog = builder.create();
////					dialog.show();
////					holder.rb = (RatingBar) relativeLayout
////							.findViewById(R.id.ratingBar1);
////					Button btnOK = (Button) relativeLayout
////							.findViewById(R.id.btn_evaluate_ok);
////					btnOK.setOnClickListener(new OnClickListener() {
////
////						@Override
////						public void onClick(View v) {
//////							holder.btn_score.setVisibility(View.GONE);
//////							holder.ratingBar.setRating(holder.rb.getRating());
//////							holder.ratingBar.setVisibility(View.VISIBLE);
//////							dialog.dismiss();
////							
////							if (mListener != null) {
////								mListener.onOrderScoreItemClick(v, 0,position,ratingBar.getRating(),et_evaluate_content.getText()
////										.toString().trim());
////								holder.btn_score.setVisibility(View.GONE);
//////								holder.ratingBar.setRating(ratingBar.getRating());
//////								holder.ratingBar.setVisibility(View.VISIBLE);
//////								mListener.onOrderScoreItemClick(v, position, et_evaluate_content.getText()
//////										.toString().trim());
////								dialog.dismiss();
////							}
////
////						}
////					});
//				}
//			});
//		} 
//		else {
//			holder.btn_score.setText("查看评价");
//		}

		return view;
	}

	onOrderScoreItemClickListener mListener;

	public void setonOrderScoreItemClickListenerListener(
			onOrderScoreItemClickListener listener) {
		mListener = listener;
	}
	
	IOrderApplyRefundClickListen mListener1;
	
	public void setonOrderApplyRefundItemClickListener(IOrderApplyRefundClickListen listener){
		mListener1=listener;
	}
	IAlterEvaluateClickListen mListener2;
	
	public void setOnAlterEvaluateClickListen(IAlterEvaluateClickListen listener){
		mListener2=listener;
	}
	public static class Holder {
		TextView tv_product;
		TextView tv_level;
		TextView tv_num;
		Button btn_score;
		RatingBar ratingBar;
		RatingBar rb;
		Button btn_apply_refund;
	}

}
