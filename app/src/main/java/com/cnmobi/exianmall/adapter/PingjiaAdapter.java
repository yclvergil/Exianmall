package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.Pingjia;
import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class PingjiaAdapter extends BaseAdapter{
	private Context context;
    private List<Pingjia>pingjias=new ArrayList<Pingjia>();
    private ViewHoldder holder;
    private Pingjia pingjia;
	public PingjiaAdapter(Context context,List<Pingjia> pingjias2) {
		this.context=context;
		this.pingjias=pingjias2;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pingjias.size();
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.pingjia_item, null);
			holder = new ViewHoldder();
			holder.pingjia_iv = (ImageView) convertView
					.findViewById(R.id.pingjia_iv);
			holder.pingjia_name = (TextView) convertView
					.findViewById(R.id.pingjia_name);
			holder.pingjia_time = (TextView) convertView
					.findViewById(R.id.pingjia_time);
			holder.pingjia_msg = (TextView) convertView
					.findViewById(R.id.pingjia_msg);
			holder.room_ratingbar = (RatingBar) convertView
					.findViewById(R.id.room_ratingbar);
			holder.room_ratingbar
					.setIsIndicator(true);// 设置不可拖动
			convertView.setTag(holder);
		} else {
			holder = (ViewHoldder) convertView
					.getTag();
		}
		pingjia = new Pingjia();
		pingjia = pingjias.get(position);
//		MyConst.imageLoader.displayImage(pingjia.getImageurl(),	holder.pingjia_iv,
//						MyConst.IM_IMAGE_OPTIONS, null);// 展示图片
		holder.pingjia_iv
				.setImageResource(R.drawable.icon_touxiang);
		if(pingjia.getEvaluatename().length()>2){
			holder.pingjia_name
			.setText(pingjia.getEvaluatename().substring(0, 1)+"***"+pingjia.getEvaluatename().substring(pingjia.getEvaluatename().length()-1, pingjia.getEvaluatename().length()));
		}else {
			holder.pingjia_name.setText(pingjia.getEvaluatename().substring(0, 1)+"***");
		}
		holder.pingjia_time
				.setText(pingjia.getEvaluatetime());
//		holder.room_ratingbar
//				.setRating(pingjia.getCommentscore());// 设置评分分数
		holder.pingjia_msg
				.setText(pingjia.getEvaluatecontent());
		return convertView;
	}
}

class ViewHoldder {
	ImageView pingjia_iv;
	TextView pingjia_name;
	TextView pingjia_time;
	RatingBar room_ratingbar;
	TextView pingjia_msg;
}
