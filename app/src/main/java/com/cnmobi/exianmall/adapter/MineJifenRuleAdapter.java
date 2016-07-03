package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.Balance;
import com.cnmobi.exianmall.bean.Rechargerules;
import com.cnmobi.exianmall.bean.subIntegralrule;

/** 积分规则适配器*/
public class MineJifenRuleAdapter extends BaseAdapter {
	private List<subIntegralrule> listChild = new ArrayList<subIntegralrule>();
	private Context context;

	public MineJifenRuleAdapter(List<subIntegralrule> listGroup, Context context) {
		this.listChild = listGroup;
		this.context = context;

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
			LayoutInflater layoutInflator = LayoutInflater.from(parent
					.getContext());
			view = layoutInflator.inflate(R.layout.fragment_mine_wallet_wallet_item, null);
			holder.tv_title=(TextView) view.findViewById(R.id.tv_title);
			holder.tv_content=(TextView) view.findViewById(R.id.tv_content);
			
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		holder.tv_title.setText(listChild.get(position).getTitle());
		holder.tv_content.setText(" "+listChild.get(position).getNote());

		return view;
	}

	class Holder {
		TextView tv_title;
		TextView tv_content;
	}

}
