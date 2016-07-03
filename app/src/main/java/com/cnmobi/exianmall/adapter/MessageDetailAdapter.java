package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.ConfirmOrderAdapter.Holder;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.MessageContentBean;
import com.cnmobi.exianmall.mine.activity.MineProductionPlaceActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 消息详情设配器
 *
 */
public class MessageDetailAdapter extends BaseAdapter{

	private Context context;
	private List<MessageContentBean> dataList = new ArrayList<MessageContentBean>();
	private MessageDetailImageAdapter adapter;
	public MessageDetailAdapter(List<MessageContentBean> dataList, Context context) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
			convertView = layoutInflator.inflate(R.layout.item_message_detail, null);
			holder.tv_subTitle=(TextView) convertView.findViewById(R.id.tv_subTitle);
			holder.tv_msgcontent=(TextView) convertView.findViewById(R.id.tv_msgcontent);
			holder.ly_sysmsgimages=(ListView) convertView.findViewById(R.id.ly_sysmsgimages);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tv_subTitle.setText(dataList.get(position).getSubTitle());
		holder.tv_msgcontent.setText(dataList.get(position).getMsgcontent());
		adapter=new MessageDetailImageAdapter(dataList.get(position).getSysmsgimages(), context);
		holder.ly_sysmsgimages.setAdapter(adapter);
		return convertView;
	}
	
	public static class Holder {
		TextView tv_subTitle;
		TextView tv_msgcontent;
		ListView ly_sysmsgimages;
	}

}
