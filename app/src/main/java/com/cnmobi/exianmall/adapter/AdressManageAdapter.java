package com.cnmobi.exianmall.adapter;

import java.util.List;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.AdressBean;
import com.cnmobi.exianmall.mine.activity.MerchantMineAlterAdressActivity;
import com.cnmobi.exianmall.widget.SwipeListView;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 卖家-我的地址管理适配器
 */
public class AdressManageAdapter extends BaseAdapter {

	private int mRightWidth = 0;
	private List<AdressBean> data;
	private Context context;
	private SwipeListView listView;

	public AdressManageAdapter(Context context, List<AdressBean> data,
			int mRightWidth, SwipeListView listView) {
		this.mRightWidth = mRightWidth;
		this.context = context;
		this.data = data;
		this.listView = listView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		LayoutInflater inflater = LayoutInflater.from(context);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.item_merchant_adress_manage, null);
			holder.item_right = (RelativeLayout) convertView
					.findViewById(R.id.item_right);
			holder.tv_merchant_name = (TextView) convertView
					.findViewById(R.id.tv_merchant_name);
			holder.tv_consigner_adress = (TextView) convertView
					.findViewById(R.id.tv_consigner_adress);
			holder.iv_alter_adress = (ImageView) convertView
					.findViewById(R.id.iv_alter_adress);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		android.view.ViewGroup.LayoutParams lm = holder.item_right
				.getLayoutParams();
		lm.width = mRightWidth;
		lm.height = LayoutParams.MATCH_PARENT;
		holder.item_right.setLayoutParams(lm);
		listView.hiddenRight(convertView);

		holder.tv_merchant_name.setText(data.get(position).getName());
		holder.tv_consigner_adress.setText(data.get(position).getAdress());
		holder.iv_alter_adress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ivListener != null) {
					ivListener.onRightImageViewClick(v, position);
				}
			}
		});
		holder.item_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mListener != null) {
					mListener.onRightItemClick(v, position);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		private RelativeLayout item_right;
		private TextView tv_merchant_name;
		private TextView tv_consigner_adress;
		private ImageView iv_alter_adress;
	}

	onRightItemClickListener mListener = null;
	onRightImageViewClickListener ivListener = null;

	public void setOnRightItemClickListener(onRightItemClickListener listener) {
		mListener = listener;
	}

	public void setOnRightImageViewClickListener(
			onRightImageViewClickListener listener) {
		ivListener = listener;
	}

	public interface onRightItemClickListener {
		void onRightItemClick(View v, int position);
	}

	public interface onRightImageViewClickListener {
		void onRightImageViewClick(View v, int position);
	}
}
