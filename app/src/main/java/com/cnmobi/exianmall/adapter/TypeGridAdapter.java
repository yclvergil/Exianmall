package com.cnmobi.exianmall.adapter;

import java.util.List;

import android.content.Context;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.ProductDetailBean;
import com.cnmobi.exianmall.interfaces.OnAddCarItemClickListener;

/**
 * 分类页面，gridView
 *
 */
public class TypeGridAdapter extends CommonAdapter<ProductDetailBean> {
	
	private int width;
	public TypeGridAdapter(Context context, List<ProductDetailBean> mDatas,
			int itemLayoutId,int width) {
		super(context, mDatas, itemLayoutId);
		this.width=width;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder helper, ProductDetailBean item) {
		helper.setHeight(R.id.img_product, (int) (width*0.75/2*0.75));
		helper.setImgImageLoder(R.id.img_product, item.getImagename());
		helper.setText(R.id.tv_name, item.getCommodityname());
//		helper.setText(R.id.tv_type_product_count,item.getStock());
//		helper.setText(R.id.tv_type_product_unit,item.getCompanyname());
		helper.setText(R.id.tv_type_product_price,"¥"+item.getPrice());
//		helper.setText(R.id.tv_type_product_unit_1,"包装："+item.getCompanyname());
		
	}
	

}
