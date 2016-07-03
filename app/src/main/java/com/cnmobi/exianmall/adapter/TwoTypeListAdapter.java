package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.R.color;
import com.cnmobi.exianmall.bean.ProductDetailBean;

/**
 * 底部菜单分类里面的listview
 * 
 */
public class TwoTypeListAdapter extends BaseAdapter {

	private ArrayList<ProductDetailBean> list;
	private Context mContext;
	private int mSelect = 0;

	public TwoTypeListAdapter(Context mContext,
			ArrayList<ProductDetailBean> list) {
		this.mContext = mContext;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return list.size();
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		Holder2 holder2;
		if (view == null) {
			holder2 = new Holder2();
			LayoutInflater layoutInflator = LayoutInflater.from(arg2
					.getContext());
			view = layoutInflator.inflate(R.layout.list_area, null);
			holder2.textView = (TextView) view.findViewById(R.id.list_area_tv);
			holder2.list_area_iv = (ImageView) view.findViewById(R.id.list_area_iv);
			view.setTag(holder2);
		} else {
			holder2 = (Holder2) view.getTag();
		}
		holder2.textView.setText(list.get(position).getCommodityName());

		if (mSelect == position) {
			holder2.textView.setTextColor(mContext.getResources().getColor(
					R.color.orange));
			holder2.list_area_iv.setVisibility(View.VISIBLE);
		} else {
			holder2.textView.setTextColor(mContext.getResources().getColor(
					R.color.black));
			holder2.list_area_iv.setVisibility(View.GONE);
		}

		return view;
	}

	public static class Holder2 {
		TextView textView;
		ImageView list_area_iv;
	}

	/**
	 * 刷新选中背景
	 */
	public void changeSelected(int positon) {
		if (positon != mSelect) {
			mSelect = positon;
			notifyDataSetChanged();
		}
	}

}
