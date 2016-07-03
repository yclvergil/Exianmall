package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.R.color;
import com.cnmobi.exianmall.bean.BaseBean;

/**
 * 底部菜单分类里面的listview
 * 
 */
public class OneTypeListAdapter extends BaseAdapter {

	private ArrayList<BaseBean> list;
	private Context mContext;
	private int mSelect = 0;

	public OneTypeListAdapter(Context mContext,
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
		Holder1 holder1;
		if (view == null) {
			holder1 = new Holder1();
			LayoutInflater layoutInflator = LayoutInflater.from(arg2
					.getContext());
			view = layoutInflator.inflate(R.layout.list_area, null);
			holder1.textView = (TextView) view.findViewById(R.id.list_area_tv);
			view.setTag(holder1);
		} else {
			holder1 = (Holder1) view.getTag();
		}
		holder1.textView.setText(list.get(position).getCategoryname());

		if (mSelect == position) {
			holder1.textView.setTextColor(mContext.getResources().getColor(
					R.color.orange));
			view.setBackgroundResource(color.white); // 选中项背景
		} else {
			holder1.textView.setTextColor(mContext.getResources().getColor(
					R.color.black));
			view.setBackgroundResource(color.hui); // 其他项背景
		}

		return view;
	}

	public static class Holder1 {
		TextView textView;
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
