package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.HisttoryOrderBean;
import com.cnmobi.exianmall.interfaces.OnAddCarItemClickListener;
import com.lidroid.xutils.util.LogUtils;

public class HistoryOrderApapter extends BaseAdapter {

	private List<HisttoryOrderBean> listChild = new ArrayList<HisttoryOrderBean>();
	private Context context;
	private int width;

	public HistoryOrderApapter(List<HisttoryOrderBean> listChild, Context context,int width) {
		this.listChild = listChild;
		this.context = context;
		this.width=width;
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
		final int commodityId;
		final int levelId;
		if (view == null) {
			holder = new Holder();
			LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
			view = layoutInflator.inflate(R.layout.adapter_gv_shouye, null);
			holder.tv_shouye_product_name = (TextView) view.findViewById(R.id.tv_shouye_product_name);
			holder.tv_shouye_product_count = (TextView) view.findViewById(R.id.tv_shouye_product_count);
//			holder.tv_shouye_product_unit = (TextView) view.findViewById(R.id.tv_shouye_product_unit);
			holder.tv_shouye_product_unit_1 = (TextView) view.findViewById(R.id.tv_shouye_product_unit_one);
			holder.tv_shouye_product_price = (TextView) view.findViewById(R.id.tv_shouye_product_price);
			holder.iv_shouye_product = (ImageView) view.findViewById(R.id.iv_shouye_product);
			holder.iv_shouye_car=(ImageView) view.findViewById(R.id.iv_shouye_car);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		LayoutParams params=holder.iv_shouye_product.getLayoutParams();
		params.height=(int) (width/2*0.75);//高设为宽的3/4，实际高为3点多一点点，未计算中间space
		holder.iv_shouye_product.setLayoutParams(params);
	    commodityId=Integer.parseInt(listChild.get(position).getIdNumber());
		levelId=Integer.parseInt(listChild.get(position).getIdLevel());
		
		if (MyApplication.isInShopCar(commodityId, levelId)) {
			holder.iv_shouye_car.setImageResource(R.drawable.iv_shouye_car_on);
		} else {
			holder.iv_shouye_car.setImageResource(R.drawable.iv_shouye_car_nm);
		} 
	
		holder.iv_shouye_car.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(MyApplication.isInShopCar(commodityId, levelId)){
					Toast.makeText(context, "商品已存购物车", Toast.LENGTH_SHORT).show();
				}else{
					if (mListener != null) {
						mListener.addCarItemClick(v, position);
					}
				}
			}
		});
		
		holder.tv_shouye_product_name.setText(listChild.get(position).getCommodityname());
		holder.tv_shouye_product_count.setText(listChild.get(position).getStock());
//		holder.tv_shouye_product_unit.setText(listChild.get(position).getCompanyname());
		holder.tv_shouye_product_unit_1.setText(listChild.get(position).getCompanyname());
		holder.tv_shouye_product_price.setText("¥"+listChild.get(position).getPrice());
		MyConst.imageLoader.displayImage(listChild.get(position).getImagename(), holder.iv_shouye_product,
				MyConst.IM_IMAGE_OPTIONS);
		
		return view;
	}

	public static class Holder {
		TextView tv_shouye_product_name;
		TextView tv_shouye_product_count;
//		TextView tv_shouye_product_unit;
		TextView tv_shouye_product_unit_1;
		TextView tv_shouye_product_price;
		ImageView iv_shouye_product;
		ImageView iv_shouye_car;
	}
	
	OnAddCarItemClickListener mListener=null;
	public void setOnAddCarItemClickListener(OnAddCarItemClickListener listener){
		mListener=listener;
	}
	
}
