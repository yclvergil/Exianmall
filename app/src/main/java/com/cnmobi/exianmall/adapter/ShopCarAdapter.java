package com.cnmobi.exianmall.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.ShopCarBeanGroup;
import com.cnmobi.exianmall.bean.ShopCarsBean;
import com.cnmobi.exianmall.interfaces.NotifyFaterAdapterListener;
import com.cnmobi.exianmall.interfaces.PriceAllItemClickListener;
import com.cnmobi.exianmall.interfaces.PriceItemClickListener;
import com.cnmobi.exianmall.widget.SwipeListView;
import com.lidroid.xutils.util.LogUtils;
import com.ywl5320.pickaddress.ChangeBirthDialog;
import com.ywl5320.pickaddress.ChangeBirthDialog.OnBirthListener;

/**
 * 购物车适配器
 * 
 */
public class ShopCarAdapter extends BaseAdapter {

	private List<ShopCarBeanGroup> listGroup = new ArrayList<ShopCarBeanGroup>();
	private List<ShopCarsBean> listChild = new ArrayList<ShopCarsBean>();
	private ShopCarBeanGroup group;
	private Context context;
	private ShopCarListAdapter adapter;
	private PriceItemClickListener pListener = null;
	private PriceAllItemClickListener aListener = null;
	long buyDtaeSeconds;
	long arrive;
	String price="";
	String minute="";
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
	public void setOnPriceItemClickListener(PriceItemClickListener listener) {
		pListener = listener;
	}

	public void setOnPriceItemAllClickListener(
			PriceAllItemClickListener listener) {
		aListener = listener;
	}

	public ShopCarAdapter(List<ShopCarBeanGroup> listGroup, Context context) {
		this.listGroup = listGroup;
		this.context = context;
		
//		for (int i = 0; i < listGroup.size(); i++) {
//			strUrgentTime.add("");
//		}
	}

	public List<ShopCarBeanGroup> getListGroup() {
		return listGroup;
	}

	public void setListGroup(List<ShopCarBeanGroup> listGroup) {
		this.listGroup = listGroup;
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
	public View getView(final int position, View view, final ViewGroup parent) {
		final Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater layoutInflator = LayoutInflater.from(parent
					.getContext());
			view = layoutInflator.inflate(R.layout.item_shopcar, null);
			holder.tv_sendouttime_urgent = (TextView) view.findViewById(R.id.tv_sendouttime_urgent);
//			holder.tv_address = (TextView) view.findViewById(R.id.tv_address);
			holder.item_shopcar_tv_sendouttime = (ImageView) view.findViewById(R.id.item_shopcar_tv_sendouttime);
			holder.listView = (SwipeListView) view.findViewById(R.id.list);
			holder.checkbox = (CheckBox) view.findViewById(R.id.item_checkbox);
			holder.ly_urgent = (LinearLayout) view.findViewById(R.id.ly_sendouttime_urgent);
			holder.rl_car_top=(RelativeLayout) view.findViewById(R.id.rl_car_top);
			holder.rl_car_top.setVisibility(View.GONE);
			holder.ly_urgent.setVisibility(View.GONE);
			holder.tv_cancel= (TextView) view.findViewById(R.id.tv_cancel);
			holder.tv_cancel.setVisibility(View.GONE);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		group = listGroup.get(position);
		
		listChild = group.getShoppCartCommodity();
		
//		holder.tv_address.setText(group.getStoreAddress());
		holder.checkbox.setChecked(group.isIstrue());
		Log.d("listChild.size():"+listChild.size(), "listChild.size()");
		if(listChild.size()!=0){
			holder.rl_car_top.setVisibility(View.VISIBLE);
			holder.ly_urgent.setVisibility(View.VISIBLE);
		}else{
			holder.rl_car_top.setVisibility(View.GONE);
			holder.ly_urgent.setVisibility(View.GONE);
		}
		adapter = new ShopCarListAdapter(context, listChild,
				holder.listView.getRightViewWidth(), holder.listView);
		holder.listView.setAdapter(adapter);
		
		adapter.setOnPriceItemClickListener(new PriceItemClickListener() {

			@Override
			public void toTalPriceClick(boolean ischoose, double price, int num) {
				if (pListener != null) {
					pListener.toTalPriceClick(ischoose, price, num);
				}
			}

			@Override
			public void toTalPriceClick(boolean isadd, double price) {
				if (pListener != null) {
					pListener.toTalPriceClick(isadd, price);
				}
			}
		});
		
		
		UiclilistenUpdate(view, group, listChild, adapter, position);
//		if(!strUrgentTime.get(position).equals("")){
//			holder.tv_sendouttime_urgent.setText(strUrgentTime.get(position));
//			holder.tv_sendouttime_urgent.setTextSize(13);
//			holder.tv_sendouttime_urgent.setTextColor(Color.RED);
//		}else {
//			holder.tv_sendouttime_urgent.setText("(如急需送货，请选择紧急到货时间)");
//			holder.tv_sendouttime_urgent.setTextSize(13);
//			holder.tv_sendouttime_urgent.setTextColor(Color.GRAY);
//		}
//		if(strUrgentTime.size()>0){
//			holder.tv_sendouttime_urgent.setText(strUrgentTime.get(0));
//			holder.tv_sendouttime_urgent.setTextSize(13);
//			holder.tv_sendouttime_urgent.setTextColor(Color.RED);
//		}
		return view;
	}

	private void UiclilistenUpdate(final View view, final ShopCarBeanGroup group,
			final List<ShopCarsBean> listChild,
			final ShopCarListAdapter adapter, final int position) {
		
		final TextView tv=(TextView) view.findViewById(R.id.tv_sendouttime_urgent);
		final TextView tv_cancel=(TextView) view.findViewById(R.id.tv_cancel);
		adapter.setOnNotifyFaterAdapterListener(new NotifyFaterAdapterListener() {
			@Override
			public void notifyAdapterClick(boolean update) {
				// 这里把position移除
				if (update) {
					listGroup.remove(position);
					notifyDataSetChanged();
				}
			}
		});
	view.findViewById(R.id.ly_sendouttime_urgent).setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {

			// TODO Auto-generated method stub
			final ChangeBirthDialog mChangeBirthDialog = new ChangeBirthDialog(
					context);
			mChangeBirthDialog.show();
			mChangeBirthDialog.setBirthdayListener(new OnBirthListener() {

				@Override
				public void onClick(final String year, String month, final String day, final String hour, final String minutes) {
//					年2016月month日03.22 星期二
					String date1=year+"-"+day.substring(0, 2)+"-"+day.substring(3, 5)+" "+hour+":"+minutes+":00";//选中的时间
//					LogUtils.e("选择的时间："+date1+"系统时间"+format1.format(new Date())+":00");
					if(minutes.length()==1){
						minute="00";
					}else{
						minute=minutes;
					}
					try {
						arrive=format.parse(date1).getTime();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					long sixHours=6*60*60*1000;
					String buyDtaeDetaile="";
					if(listGroup.get(position).getBuyDate()!=null &&!format2.format(new Date()).equals(listGroup.get(position).getBuyDate())){
						//虽然需求改了之后不会满足这个条件，还是码一下
						try {
							//价格日历默认时分为：选择的日期+当前时间的时分+:00
							buyDtaeDetaile=format.format(new Date());
//							buyDtaeSeconds = format.parse(listGroup.get(position).getBuyDate()+"  "+buyDtaeDetaile.substring(buyDtaeDetaile.length()-8, buyDtaeDetaile.length()-3)+":00").getTime();
							buyDtaeSeconds = format.parse(listGroup.get(position).getBuyDate()+"  "+"00:00:00").getTime()+sixHours;
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
						//这里有价格日历选中的时间，跟价格日历时间比较
						if(arrive<(buyDtaeSeconds+sixHours)){
							LogUtils.i("价格日历"+listGroup.get(position).getBuyDate()+";");
							//选择的到货时间与价格日历时间只差小于6小时
							Toast.makeText(context, "很抱歉，超出配送时间，无法送达！", Toast.LENGTH_SHORT).show();
							tv.setText("");
//							tv.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.dimen_5_sp));
							tv.setTextColor(Color.RED);
							for(int i=0;i<listChild.size();i++){
								listChild.get(i).setArrivalTime("");
								listChild.get(i).setSendPrice(0.00);
							}
							
						}else{
							
							if((arrive-buyDtaeSeconds)/(60*60*1000)<=6){
								price="50.00";
							}else if((arrive-buyDtaeSeconds)/(60*60*1000)<=7){
								price="40.00";
							}else if((arrive-buyDtaeSeconds)/(double)(60*60*1000)<=8){
								price="30.00";
							}else{
								price="0.00";
							}
//							if(showDialog(price)){
							LayoutInflater inflaterDl = LayoutInflater.from(context);
							RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
									.inflate(R.layout.dialog_clean_message, null);
							Builder builder = new AlertDialog.Builder(context);
							builder.setView(relativeLayout);
							final Dialog dialog = builder.create();
							dialog.show();
							TextView tv1=(TextView) relativeLayout.findViewById(R.id.tv_content);
							if("0.00".equals(price)){
								tv1.setText("您当前选择的紧急到货时间超过8小时，配送费为"+price+"元，将不会作为紧急订单处理！");
							}else{
								tv1.setText("您当前选择的紧急到货时间配送费为"+price+"元,您确定选择吗？");
							}
							Button btnCancel = (Button) relativeLayout.findViewById(R.id.btn_clean_message_cancel);
							btnCancel.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
							Button btnOK = (Button) relativeLayout.findViewById(R.id.btn_clean_message_ok);
							btnOK.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									tv_cancel.setVisibility(View.VISIBLE);
									if("0.00".equals(price)){
										tv.setText("");
										tv_cancel.setVisibility(View.GONE);
									}else{
										
										tv.setText(year + "-" + day+ "     " + hour+ ":" + minute);
//										tv.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.dimen_5_sp));
										
										tv.setTextColor(Color.RED);
										tv_cancel.setVisibility(View.VISIBLE);
									}
									for(int i=0;i<listChild.size();i++){
										listChild.get(i).setArrivalTime(year+"-"+day.substring(0, 2)+"-"+day.substring(3, 5)+" "+hour+":"+minute);
										if((arrive-buyDtaeSeconds)/(60*60*1000)<=6){
											listChild.get(i).setSendPrice(50.00);
										}else if((arrive-buyDtaeSeconds)/(60*60*1000)<=7){
											listChild.get(i).setSendPrice(40.00);
										}else if((arrive-buyDtaeSeconds)/(double)(60*60*1000)<=8){
										listChild.get(i).setSendPrice(30.00);
										}else{
											listChild.get(i).setSendPrice(0.00);
											listChild.get(i).setArrivalTime("");
											tv_cancel.setVisibility(View.GONE);
										}
									}
									dialog.dismiss();
									
								}
							});
						
						
						}
					}else{
						//这里没有价格日历选中的时间，跟系统当前时间比较
						if(arrive<getSysTime(new Date())+sixHours){
							//到货时间与系统当前时间只差小于6小时
							Toast.makeText(context, "亲爱的用户，6小时内无法送达哦~", Toast.LENGTH_SHORT).show();
						}else{
							if((arrive-getSysTime(new Date()))/(60*60*1000)<=6){
								price="50.00";
							}else if((arrive-getSysTime(new Date()))/(60*60*1000)<=7){
								price="40.00";
							}else if((arrive-getSysTime(new Date()))/(double)(60*60*1000)<=8){
								price="30.00";
							}else{
								price="0.00";
								
							}
							LayoutInflater inflaterDl = LayoutInflater.from(context);
							RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
									.inflate(R.layout.dialog_clean_message, null);
							Builder builder = new AlertDialog.Builder(context);
							builder.setView(relativeLayout);
							final Dialog dialog = builder.create();
							dialog.show();
							TextView tv1=(TextView) relativeLayout.findViewById(R.id.tv_content);
							if("0.00".equals(price)){
								tv1.setText("您当前选择的紧急到货时间配超过8小时，配送费为"+price+"元，将不会作为紧急订单处理！");
							}else{
								tv1.setText("您当前选择的紧急到货时间配送费为"+price+"元,您确定选择吗？");
							}
							Button btnCancel = (Button) relativeLayout.findViewById(R.id.btn_clean_message_cancel);
							btnCancel.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
							Button btnOK = (Button) relativeLayout.findViewById(R.id.btn_clean_message_ok);
							btnOK.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									tv_cancel.setVisibility(View.VISIBLE);
									if("0.00".equals(price)){
										tv.setText(""); 
										tv_cancel.setVisibility(View.GONE);
									}else{
										LogUtils.e("minute:"+minute+"长度："+minute.length());
										tv.setText(year + "-" + day+ "     " + hour+ ":" + minute);
//										tv.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.dimen_5_sp));
										LogUtils.i("dimen_5_sp:"+context.getResources().getDimensionPixelSize(R.dimen.dimen_5_sp));
										tv.setTextColor(Color.RED);
									}
									for(int i=0;i<listChild.size();i++){
										listChild.get(i).setArrivalTime(year+"-"+day.substring(0, 2)+"-"+day.substring(3, 5)+" "+hour+":"+minute);
										if((arrive-getSysTime(new Date()))/(60*60*1000)<=6){
											listChild.get(i).setSendPrice(50.00);
										}else if((arrive-getSysTime(new Date()))/(60*60*1000)<=7){
											listChild.get(i).setSendPrice(40.00);
										}else if((arrive-getSysTime(new Date()))/(double)(60*60*1000)<=8){
										listChild.get(i).setSendPrice(30.00);
										}else{
											listChild.get(i).setArrivalTime("");
											listChild.get(i).setSendPrice(0.00);
											tv_cancel.setVisibility(View.GONE);
										}
										
										
									}
								
									dialog.dismiss();
									
								}
							});
						
						}
					}
					
				}
				
			});
		
			
		}
	});
	view.findViewById(R.id.item_shopcar_tv_sendouttime).setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			if (mListener != null) {
				mListener.onCalendarPriceClick(arg0, position);
				tv.setText("");
//				tv.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.dimen_5_sp));
				tv.setTextColor(Color.GRAY);
				price="";
				tv_cancel.setVisibility(View.GONE);
				for(int i=0;i<listChild.size();i++){
					listChild.get(i).setArrivalTime("");
					listChild.get(i).setSendPrice(0.00);
					
				}
			}
		}
	});
		
	view.findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			tv.setText("");
//			tv.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.dimen_6_sp));
			tv.setTextColor(Color.GRAY);
			v.setVisibility(View.GONE);
			price="";
			for(int i=0;i<listChild.size();i++){
				listChild.get(i).setArrivalTime("");
				listChild.get(i).setSendPrice(0.00);
				
			}
		}
	});
		view.findViewById(R.id.item_checkbox).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						double price = 0;
						group.setIstrue(!group.isIstrue());
						boolean groupIsCheck = group.isIstrue();
						for (int i = 0; i < listChild.size(); i++) {
							ShopCarsBean child = listChild.get(i);
							if (child.isIstrue() != groupIsCheck) {
								child.setIstrue(groupIsCheck);
								price += child.getPrice()
										* child.getBuynumber();
							}
						}
						if (!groupIsCheck) {
							price = -price;
						}
						if (aListener != null) {
							aListener.toTalPriceClick(price);
						}
						adapter.notifyDataSetChanged();
					}
				});

	}

	class Holder {
		TextView tv_sendouttime_urgent;
//		TextView tv_address;
		SwipeListView listView;
		CheckBox checkbox;
		LinearLayout ly_urgent;
		ImageView item_shopcar_tv_sendouttime;
		RelativeLayout rl_car_top;
		LinearLayout ly_sendouttime_urgent;
		TextView tv_cancel;
	}
	
	public interface onCalendarPriceClickListener {
		void onCalendarPriceClick(View v, int position1);//日历价格变动
	}

	onCalendarPriceClickListener mListener = null;

	public void setOnCalendarPriceClickListener(
			onCalendarPriceClickListener listener) {
		mListener = listener;
	}
	
	/**
	 * 转换去秒的时间
	 * @param date
	 * @return
	 */
	long getSysTime(Date date){
		try {
			return format.parse((format2.format(date)+"  06:00:00")).getTime()+24*60*60*1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
}
