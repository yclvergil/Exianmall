package com.cnmobi.exianmall.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.CommodityDescribe;
import com.cnmobi.exianmall.type.activity.ProductionShowActivity;

/**
 * 商品详情列表
 * 
 */
public class ProductDetailAdapter extends BaseAdapter {

	private List<CommodityDescribe> listChild = new ArrayList<CommodityDescribe>();
	private Context context;
	private Intent intent;
	private List<String> imgsurls=new ArrayList<String>();

	public ProductDetailAdapter(List<CommodityDescribe> listChild, Context context) {
		this.listChild = listChild;
		this.context = context;
		for(int i=0;i<listChild.size();i++){
			imgsurls.add(listChild.get(i).getImagename());
		}
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
			view = layoutInflator.inflate(R.layout.item_product_detail, null);
			holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
			holder.tv_message = (TextView) view.findViewById(R.id.tv_message);
//			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.iv_left = (ImageView) view.findViewById(R.id.iv_photo1);
			holder.iv_right = (ImageView) view.findViewById(R.id.iv_photo2);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
//		holder.tv_num.setText(position + 1 + "");
//		holder.tv_title.setText(listChild.get(position).getTitle());
		holder.tv_message.setText(listChild.get(position).getDescribe());

		if (position % 2 == 0) {
			holder.iv_left.setVisibility(View.VISIBLE);
			holder.iv_right.setVisibility(View.GONE);
			MyConst.imageLoader.displayImage(listChild.get(position).getImagename(), holder.iv_left,
					MyConst.IM_IMAGE_OPTIONS);
		} else {
			holder.iv_left.setVisibility(View.GONE);
			holder.iv_right.setVisibility(View.VISIBLE);
			MyConst.imageLoader.displayImage(listChild.get(position).getImagename(), holder.iv_right,
					MyConst.IM_IMAGE_OPTIONS);
		}
		holder.iv_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent=new Intent();
				intent.setClass(context, ProductionShowActivity.class);
				intent.putExtra("imgsurls", (Serializable)imgsurls);
				intent.putExtra("location", position);
				context.startActivity(intent);
				
			}
		});
		holder.iv_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent=new Intent();
				intent.setClass(context, ProductionShowActivity.class);
				intent.putExtra("imgsurls", (Serializable)imgsurls);
				intent.putExtra("location", position);
				context.startActivity(intent);;
			}
		});
		return view;
	}

	public static class Holder {
		TextView tv_num;
//		TextView tv_title;
		TextView tv_message;
		ImageView iv_left;
		ImageView iv_right;
	}

}
