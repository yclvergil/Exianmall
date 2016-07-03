package com.cnmobi.exianmall.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.R.color;
import com.cnmobi.exianmall.interfaces.onAreaItemClickListener;

public class AreaAdapter extends BaseAdapter {
	private Context context;
	private List<String> area;
	private List<String> idNumber;
	private String[][] stype;
	private String[]ftype;
	private ViewHolder holder;
	private int fposition = 0;
	private int mSelect = 0;

	public int getFposition() {
		return fposition;
	}

	public void setFposition(int fposition) {
		this.fposition = fposition;
	}

	public List<String> getArea() {
		return area;
	}

	public void setArea(List<String> area) {
		this.area = area;
	}

	
	public List<String> getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(List<String> idNumber) {
		this.idNumber = idNumber;
	}

	public AreaAdapter(Context context, List<String> area,List<String> idNumber) {
		this.context = context;
		this.area = area;
		this.idNumber=idNumber;
	}

	public AreaAdapter(Context context, String[][] stype, int fposition) {
		this.context = context;
		this.stype = stype;
		this.fposition = fposition;
	}

/**
 * 测试构造类
 * @param context
 */
	public AreaAdapter(Context context , String[] ftype) {
		this.context = context;
		this.ftype = ftype;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (ftype!= null) {
			return ftype.length;
		} else if(stype!=null){
			return stype[fposition].length;
		}else{
			return area.size();
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater layoutInflator = LayoutInflater.from(context);
			view = layoutInflator.inflate(R.layout.list_area, null);
			holder.list_area_tv = (TextView) view
					.findViewById(R.id.list_area_tv);
			holder.list_area_iv = (ImageView) view
					.findViewById(R.id.list_area_iv);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// 判断是一个list还是两个list
		if (stype != null) {
			holder.list_area_tv.setText(stype[fposition][position]);
			if (mSelect == position) {
				holder.list_area_tv.setTextColor(context.getResources()
						.getColor(R.color.orange));
				holder.list_area_iv.setVisibility(View.VISIBLE);
			} else {
				holder.list_area_tv.setTextColor(context.getResources()
						.getColor(R.color.text_hui));
				holder.list_area_iv.setVisibility(View.GONE);
			}
		}
		if(ftype != null){
				holder.list_area_tv.setText(ftype[position]);
				holder.list_area_iv.setVisibility(View.GONE);
				if (mSelect == position) {
					view.setBackgroundResource(color.hui); // 选中项背景
				} else {
					view.setBackgroundResource(color.white); // 其他项背景
				}
			}
		if(area!=null){
			holder.list_area_tv.setText(area.get(position));
			holder.list_area_tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mListener != null) {
							mListener.onAreaItemClick(v, position, idNumber.get(position));
					}
				}
			});
			if (mSelect == position) {
				holder.list_area_tv.setTextColor(context.getResources()
						.getColor(R.color.orange));
				holder.list_area_iv.setVisibility(View.VISIBLE);
			} else {
				holder.list_area_tv.setTextColor(context.getResources()
						.getColor(R.color.text_hui));
				view.setBackgroundResource(color.white); // 其他项背景
				holder.list_area_iv.setVisibility(View.GONE);
			}
		}
		return view;
	}

	class ViewHolder {
		TextView list_area_tv;
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
	
	onAreaItemClickListener mListener = null;

	public void setOnAreaItemClickListener(onAreaItemClickListener listener) {
		mListener = listener;
	}
}
