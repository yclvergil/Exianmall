package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.AboutEXianBean;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AboutEXianAdapter extends BaseAdapter{

	private List<AboutEXianBean> list = new ArrayList<AboutEXianBean>();
	private Context context;
	private String corporateName;
	
	public AboutEXianAdapter(Context context,List<AboutEXianBean> list,String corporateName) {
		// TODO Auto-generated constructor stub
		this.list=list;
		this.context=context;
		this.corporateName=corporateName;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		LayoutInflater inflater = LayoutInflater.from(context);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.item_about_exian, null);
			holder.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
//			holder.tv_content_first.setVisibility(View.GONE);
			holder.tv_content=(TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(position==0){
			//指定字体加粗、黑色显示
			String str="     "+Html.fromHtml("<b>"+corporateName+"</b>"+list.get(position).getContent()+"");
			int fstart=str.indexOf(""+corporateName+"");
			int fend=fstart+corporateName.length();
			SpannableStringBuilder style=new SpannableStringBuilder(str);
			style.setSpan(new ForegroundColorSpan(Color.BLACK),fstart,fend,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
			holder.tv_content.setText(style);
		}else{
			holder.tv_content.setText(list.get(position).getContent());
		}
		holder.tv_title.setText(list.get(position).getTitle());
		return convertView;
	}
	
	class ViewHolder {
		TextView tv_title;
		TextView tv_content_first;
		TextView tv_content;
	}
}
