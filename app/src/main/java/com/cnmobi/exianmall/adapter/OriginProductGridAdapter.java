package com.cnmobi.exianmall.adapter;

import java.util.List;

import android.content.Context;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.bean.OriginProduct;
import com.cnmobi.exianmall.bean.ProductDetailBean;

/**
 * 产地，主营产品
 *
 */
public class OriginProductGridAdapter extends CommonAdapter<OriginProduct> {

	public OriginProductGridAdapter(Context context, List<OriginProduct> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder helper, OriginProduct item) {
		helper.setImgImageLoder(R.id.img_product, item.getImageName());
		helper.setText(R.id.tv_name, item.getProductName());
	}


}
