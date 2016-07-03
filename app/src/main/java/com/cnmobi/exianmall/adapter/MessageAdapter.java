package com.cnmobi.exianmall.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.MessageBean;
import com.cnmobi.exianmall.widget.RoundImageView;
import com.cnmobi.exianmall.widget.SwipeListView;
import com.lidroid.xutils.util.LogUtils;

public class MessageAdapter extends BaseAdapter {

	private int mRightWidth = 0;
	private List<MessageBean> data;
	private Context context;
	private SwipeListView listView;

	public MessageAdapter(Context context, List<MessageBean> data, int mRightWidth, SwipeListView listView) {
		this.mRightWidth = mRightWidth;
		this.context = context;
		this.data = data;
		this.listView = listView;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		LayoutInflater inflater = LayoutInflater.from(context);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_message, null);
			holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
			holder.imegaView = (ImageView) convertView.findViewById(R.id.iv_head);
			holder.item_right = (RelativeLayout) convertView.findViewById(R.id.item_right);
			holder.iv_messagelist_red_point=(ImageView) convertView.findViewById(R.id.iv_messagelist_red_point);
			holder.iv_messagelist_red_point.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		android.view.ViewGroup.LayoutParams lm = holder.item_right.getLayoutParams();
		lm.width = mRightWidth;
		lm.height = LayoutParams.MATCH_PARENT;
		holder.item_right.setLayoutParams(lm);
		listView.hiddenRight(convertView);
		if("0".equals(data.get(position).getMsgtype())){
			holder.tv_type.setText("系统消息");
			if("Y".equals(data.get(position).getIsRead())){
				//已读
				holder.iv_messagelist_red_point.setVisibility(View.GONE);
			}else{
				//未读
				holder.iv_messagelist_red_point.setVisibility(View.VISIBLE);
			}
		}else if("1".equals(data.get(position).getMsgtype())){
			holder.tv_type.setText("系统消息");
			if("Y".equals(data.get(position).getIsRead())){
				//已读
				holder.iv_messagelist_red_point.setVisibility(View.GONE);
			}else{
				//未读
				holder.iv_messagelist_red_point.setVisibility(View.VISIBLE);
			}
			
		}else{
			holder.tv_type.setText("e鲜商城客服");
			if("Y".equals(data.get(position).getIsRead())){
				//已读
				holder.iv_messagelist_red_point.setVisibility(View.GONE);
			}else{
				//未读
				holder.iv_messagelist_red_point.setVisibility(View.VISIBLE);
			}
		}
		
		holder.tv_message.setText(data.get(position).getMsgtitle());
//		MyConst.imageLoader.displayImage(data.get(position).getHeadurl(), holder.imegaView, MyConst.IM_IMAGE_OPTIONS);

		holder.item_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightItemClick(v, position);
				}
			}
		});

		return convertView;
	}

	class ViewHolder {
		private RelativeLayout item_right;
		private TextView tv_type;
		private TextView tv_message;
		private ImageView imegaView;
		private ImageView iv_messagelist_red_point;
	}

	onRightItemClickListener mListener = null;

	public void setOnRightItemClickListener(onRightItemClickListener listener) {
		mListener = listener;
	}

	public interface onRightItemClickListener {
		void onRightItemClick(View v, int position);
	}

}
