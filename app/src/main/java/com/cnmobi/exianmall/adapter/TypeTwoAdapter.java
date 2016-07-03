package com.cnmobi.exianmall.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.ProductBean;
import com.cnmobi.exianmall.interfaces.OnAddCarItemClickListener;

/**
 * 分类二级菜单
 * 
 */
public class TypeTwoAdapter extends BaseAdapter {

	private List<ProductBean> listChild = new ArrayList<ProductBean>();
	private Context context;
	private int width;

	public TypeTwoAdapter(List<ProductBean> listChild, Context context,int width) {
		this.listChild = listChild;
		this.context = context;
		this.width=width;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listChild.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		final int commodityId;
		final int levelId;
		if (convertView == null) {
			holder = new Holder();
			LayoutInflater layoutInflator = LayoutInflater.from(parent
					.getContext());
			convertView = layoutInflator.inflate(R.layout.item_typetwo, null);
			holder.tv_product = (TextView) convertView
					.findViewById(R.id.tv_product);
//			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.iv_product = (ImageView) convertView
					.findViewById(R.id.iv_product);
			holder.iv_add_car = (ImageView) convertView
					.findViewById(R.id.iv_add_car);
//			holder.tv_guige = (TextView) convertView
//					.findViewById(R.id.tv_guige);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		LayoutParams params=holder.iv_product.getLayoutParams();
		params.height=(int) (width/2*0.75);
		holder.iv_product.setLayoutParams(params);
		commodityId = Integer.parseInt(listChild.get(position).getIdNumber());
		levelId = Integer.parseInt(listChild.get(position).getIdLevel());
		if (MyApplication.isInShopCar(commodityId, levelId)) {
			holder.iv_add_car.setImageResource(R.drawable.iv_shouye_car_on);
		} else {
			holder.iv_add_car.setImageResource(R.drawable.iv_shouye_car_nm);
		}

		holder.iv_add_car.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MyApplication.isInShopCar(commodityId, levelId)) {
					Toast.makeText(context, "商品已在购物车", Toast.LENGTH_SHORT)
							.show();
				} else {
					if (mListener != null) {
						mListener.addCarItemClick(v, position);
					}
				}
			}
		});
		holder.tv_product.setText(listChild.get(position).getCommodityname());
//		holder.tv_num.setText("库存 " + listChild.get(position).getStock());
		holder.tv_price.setText("¥"+ listChild.get(position).getPrice());
//		holder.tv_guige.setText(listChild.get(position).getCompanyname());
		MyConst.imageLoader.displayImage(
				listChild.get(position).getImagename(), holder.iv_product,
				MyConst.IM_IMAGE_OPTIONS);

		return convertView;
	}

	OnAddCarItemClickListener mListener;

	public void setOnAddCarItemClickListener(OnAddCarItemClickListener listener) {
		mListener = listener;
	}

	public static class Holder {
		TextView tv_product;
//		TextView tv_num;
		TextView tv_price;
		ImageView iv_add_car;
		ImageView iv_product;
//		TextView tv_guige;
	}
}
