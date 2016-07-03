package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.MessageDetailAdapter.Holder;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.MessageContentBean;
import com.cnmobi.exianmall.bean.MessageDetailImageBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 消息详情图片适配器
 */
public class MessageDetailImageAdapter extends BaseAdapter{

	
	private Context context;
	private List<MessageDetailImageBean> dataList = new ArrayList<MessageDetailImageBean>();
	
	public MessageDetailImageAdapter(List<MessageDetailImageBean> dataList, Context context) {
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
			convertView = layoutInflator.inflate(R.layout.item_message_detail_image, null);
			holder.iv_imagename=(ImageView) convertView.findViewById(R.id.iv_imagename);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		MyConst.imageLoader.displayImage(dataList.get(position).getImagename(),
				holder.iv_imagename, MyConst.IM_IMAGE_OPTIONS);
		return convertView;
	}
	
	public static class Holder {
		ImageView iv_imagename;
	}

}
