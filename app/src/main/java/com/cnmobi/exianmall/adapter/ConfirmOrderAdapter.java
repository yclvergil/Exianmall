package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.ConfirmOrderProductBean;
import com.cnmobi.exianmall.mine.activity.MineProductionPlaceActivity;
import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConfirmOrderAdapter extends BaseAdapter{

	private Context context;
	private List<ConfirmOrderProductBean> dataList = new ArrayList<ConfirmOrderProductBean>();
	
	public ConfirmOrderAdapter(List<ConfirmOrderProductBean> dataList, Context context) {
		this.dataList = dataList;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
			convertView = layoutInflator.inflate(R.layout.item_confirmorder_list, null);
			holder.rl_production_place = (RelativeLayout) convertView.findViewById(R.id.rl_production_place);
			holder.tv_confirmorder_adress = (TextView) convertView.findViewById(R.id.tv_confirmorder_adress);
			holder.tv_confirmorder_name = (TextView) convertView.findViewById(R.id.tv_confirmorder_name);
			holder.tv_confirmorder_stock = (TextView) convertView.findViewById(R.id.tv_confirmorder_stock);
			holder.tv_confirmorder_sumbuynum = (TextView) convertView.findViewById(R.id.tv_confirmorder_sumbuynum);
			holder.tv_confirmorder_price = (TextView) convertView.findViewById(R.id.tv_confirmorder_price);
			holder.tv_confirmorder_level = (TextView) convertView.findViewById(R.id.tv_confirmorder_level);
			holder.tv_confirmorder_buynum = (TextView) convertView.findViewById(R.id.tv_confirmorder_buynum);
			holder.iv_product=(ImageView) convertView.findViewById(R.id.iv_product);
			holder.tv_date=(TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_confirmorder_guige= (TextView) convertView.findViewById(R.id.tv_confirmorder_guige);
//			holder.ly_urgent=(LinearLayout) convertView.findViewById(R.id.ly_urgent);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		
		holder.tv_confirmorder_adress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context, MineProductionPlaceActivity.class);
				intent.putExtra("idStore",dataList.get(position).getAdressId());
				context.startActivity(intent);
			}
		});
//		if(position!=0){
//			if(dataList.get(position).getAdressId()==dataList.get(position-1).getAdressId()){
//				holder.rl_production_place.setVisibility(View.GONE);
//				LogUtils.e("aaaaaaaaaaaaaaaaaaaaaaaaa");
//				
//			}else{
//				holder.rl_production_place.setVisibility(View.VISIBLE);
//				holder.tv_confirmorder_adress.setText(dataList.get(position).getAdress());
//			}
//		}else{
//			
//		holder.tv_confirmorder_adress.setText(dataList.get(position).getAdress());
//		}
//		if(position!=0){
//			if(dataList.get(position).getAdressId()==dataList.get(position-1).getAdressId()){
//				holder.rl_production_place.setVisibility(View.GONE);
//				
//			}else{
//				holder.rl_production_place.setVisibility(View.VISIBLE);
//				holder.tv_confirmorder_adress.setText(dataList.get(position).getAdress());
//			}
//		}else{
//			
		holder.tv_confirmorder_adress.setText(dataList.get(position).getAdress());
//		}
		holder.tv_confirmorder_name.setText(dataList.get(position).getName());
		holder.tv_confirmorder_stock.setText("库存 "+dataList.get(position).getStock());
		holder.tv_confirmorder_sumbuynum.setText("已有"+dataList.get(position).getSumBuyNum()+"人购买");
		holder.tv_confirmorder_price.setText(String.format("%.2f", Double.valueOf(dataList.get(position).getPrice())));
		holder.tv_confirmorder_level.setText(dataList.get(position).getLevel());
		holder.tv_confirmorder_guige.setText(dataList.get(position).getCompanyName());
		holder.tv_confirmorder_buynum.setText("x"+dataList.get(position).getBuyNum());
		MyConst.imageLoader.displayImage(dataList.get(position).getImageUrl(), holder.iv_product,
						MyConst.IM_IMAGE_OPTIONS);
		return convertView;
	}

	public static class Holder {
		RelativeLayout rl_production_place;
		TextView tv_confirmorder_adress;
		TextView tv_confirmorder_name;
		TextView tv_confirmorder_stock;
		TextView tv_confirmorder_sumbuynum;
		TextView tv_confirmorder_price;
		TextView tv_confirmorder_level;
		TextView tv_confirmorder_buynum;
		TextView tv_date;
		ImageView iv_product;
		TextView tv_confirmorder_guige;
//		LinearLayout ly_urgent;
		
		
	}
	
}
