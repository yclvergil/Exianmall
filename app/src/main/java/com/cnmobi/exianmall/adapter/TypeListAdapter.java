package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.R.color;
import com.cnmobi.exianmall.bean.BaseBean;
import com.cnmobi.exianmall.bean.ProductDetailBean;

/**
 * 底部菜单分类里面的listview
 * 
 */
public class TypeListAdapter extends BaseAdapter {

	private ArrayList<BaseBean> list;
	private Context mContext;
	private int mSelect = 0;

	public TypeListAdapter(Context mContext,
			ArrayList<BaseBean> list) {
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
		Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater layoutInflator = LayoutInflater.from(arg2
					.getContext());
			view = layoutInflator.inflate(R.layout.item_type_list, null);
			holder.textView = (TextView) view.findViewById(R.id.tv_name);
			holder.line = (View) view.findViewById(R.id.line);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		holder.textView.setText(list.get(position).getCategoryname());

		if (mSelect == position) {
			holder.textView.setTextColor(mContext.getResources().getColor(R.color.green));
			view.setBackgroundResource(color.white); // 选中项背景
			holder.line.setVisibility(View.VISIBLE);
		} else {
			holder.textView.setTextColor(mContext.getResources().getColor(R.color.black));
			view.setBackgroundResource(color.hui); // 其他项背景
			holder.line.setVisibility(View.GONE);
		}

		return view;
	}

	public static class Holder {
		TextView textView;
		View line;
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
