package com.cnmobi.exianmall.adapter;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.ShopCarAdapter.onCalendarPriceClickListener;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.ShopCarsBean;
import com.cnmobi.exianmall.fragment.PurchaserCarFragment;
import com.cnmobi.exianmall.fragment.PurchaserHomeFragment;
import com.cnmobi.exianmall.interfaces.NotifyFaterAdapterListener;
import com.cnmobi.exianmall.interfaces.PriceItemClickListener;
import com.cnmobi.exianmall.type.activity.ProductDetailActivity;
import com.cnmobi.exianmall.widget.SwipeListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopCarListAdapter extends BaseAdapter {

	private int mRightWidth = 0;
	private List<ShopCarsBean> childs;
	private ShopCarsBean child;
	private Context context;
	private SwipeListView listView;
	private PriceItemClickListener mListener = null;
	private NotifyFaterAdapterListener nListener = null;
	private List<ShopCarsBean> shopCarCommodities=new ArrayList<ShopCarsBean>();//购物车商品
	private Intent intent;

	/**
	 * 监听子项的价格总和
	 * 
	 * @param listener
	 */
	public void setOnPriceItemClickListener(PriceItemClickListener listener) {
		mListener = listener;
	}

	/**
	 * 监听子项是否全部删除
	 * 
	 * @param listener
	 */
	public void setOnNotifyFaterAdapterListener(
			NotifyFaterAdapterListener listener) {
		nListener = listener;
	}

	public ShopCarListAdapter(Context context, List<ShopCarsBean> childs,
			int mRightWidth, SwipeListView listView) {
		this.mRightWidth = mRightWidth;
		this.context = context;
		this.childs = childs;
		this.listView = listView;

	}

	@Override
	public int getCount() {
		return childs.size();
	}

	@Override
	public Object getItem(int arg0) {
		return childs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		LayoutInflater inflater = LayoutInflater.from(context);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_shopcarlist, null);
			holder.iv_item_shopcarlist_checkbox = (CheckBox) convertView
					.findViewById(R.id.iv_item_shopcarlist_checkbox);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_product);
			holder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			holder.btn_add = (Button) convertView.findViewById(R.id.btn_add);
			holder.btn_cut = (Button) convertView.findViewById(R.id.btn_cut);
			holder.item_right = (RelativeLayout) convertView
					.findViewById(R.id.item_right);
			holder.item_shopcarlist_ll = (LinearLayout) convertView
					.findViewById(R.id.item_shopcarlist_ll);
			holder.item_shopcarlist_iv_product = (ImageView) convertView
					.findViewById(R.id.item_shopcarlist_iv_product);

			holder.tv_product = (TextView) convertView
					.findViewById(R.id.tv_product);
			holder.tv_stock = (TextView) convertView
					.findViewById(R.id.tv_stock);
			holder.tv_sumBuyNumber = (TextView) convertView
					.findViewById(R.id.tv_sumBuyNumber);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.tv_levelname = (TextView) convertView
					.findViewById(R.id.tv_levelname);
			holder.tv_guige=(TextView) convertView.findViewById(R.id.tv_guige);
//			holder.item_shopcar_iv_sendouttime = (ImageView) convertView.findViewById(R.id.item_shopcar_iv_sendouttime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		child = childs.get(position);
		holder.tv_product.setText(child.getCommodityname());
		holder.tv_stock.setText("库存 " + child.getStock());
		holder.tv_sumBuyNumber.setText("已有" + child.getSumBuyNumber() + "人购买");
		holder.tv_count.setText(child.getBuynumber() + "");
		holder.tv_price.setText(String.format("%.2f", child.getPrice()));
		holder.tv_levelname.setText(child.getLevelname());
//		holder.item_shopcarlist_iv_product.disp
		holder.tv_guige.setText(child.getCompanyname());
		MyConst.imageLoader.displayImage(child.getImagename(), holder.item_shopcarlist_iv_product, MyConst.IM_IMAGE_OPTIONS);
		holder.iv_item_shopcarlist_checkbox.setChecked(child.isIstrue());
//		holder.item_shopcar_iv_sendouttime.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (listener != null) {
//					listener.onCalendarPriceClick(v, 0,position);
//				}
//			}
//		});
		UIListenupdate(convertView, child);
		android.view.ViewGroup.LayoutParams lm = holder.item_right
				.getLayoutParams();
		lm.width = mRightWidth;
		lm.height = LayoutParams.MATCH_PARENT;
		holder.item_right.setLayoutParams(lm);
		listView.hiddenRight(convertView);
		holder.tv_name.setText(child.getCommodityname());
		delShoppCartAction(convertView, holder.item_right,
				child.getIdCommodity(),child.getIdLevel(), child.getIdShoppCart(), position);
		
		return convertView;
	}

	private void UIListenupdate(final View convertView,
			final ShopCarsBean child) {
		final CheckBox cb = (CheckBox) convertView
				.findViewById(R.id.iv_item_shopcarlist_checkbox);
		cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				child.setIstrue(!child.isIstrue());
				if (mListener != null) {
					mListener.toTalPriceClick(child.isIstrue(),
							child.getPrice(), child.getBuynumber());
				}
			}
		});
		convertView.findViewById(R.id.btn_add).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						TextView tv = (TextView) convertView
								.findViewById(R.id.tv_count);
						int num = Integer.parseInt(tv.getText().toString());
						if(num<child.getStock()){
							num++;
							tv.setText(num + "");
							modifyBuyNumber(child, cb.isChecked(), true, num);
						}else{
							Toast.makeText(context, "超出商品库存！", Toast.LENGTH_SHORT).show();
						}
					}
				});
		convertView.findViewById(R.id.btn_cut).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						TextView tv = (TextView) convertView
								.findViewById(R.id.tv_count);
						int num = Integer.parseInt(tv.getText().toString());
						if (num != 1) {
							num--;
							tv.setText(num + "");
							modifyBuyNumber(child, cb.isChecked(), false, num);
						}
					}
				});
		convertView.findViewById(R.id.item_shopcarlist_iv_product)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						intent = new Intent(context,
								ProductDetailActivity.class);
						intent.putExtra("idCommodity", child.getIdCommodity()
								+ "");
						intent.putExtra("idLevel", child.getIdLevel()
								+ "");
						intent.putExtra("imageUrl", child.getImagename());
						context.startActivity(intent);
					}
				});
		convertView.findViewById(R.id.item_shopcarlist_ll).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(context,
								ProductDetailActivity.class);
						intent.putExtra("idCommodity", child.getIdCommodity()
								+ "");
						intent.putExtra("idLevel", child.getIdLevel()
								+ "");
						intent.putExtra("imageUrl", child.getImagename());
						context.startActivity(intent);

					}
				});
	}

	class ViewHolder {
		private LinearLayout item_shopcarlist_ll;
		private RelativeLayout item_right;
		private TextView tv_name;
		private Button btn_add;
		private Button btn_cut;
		private TextView tv_count;
		private CheckBox iv_item_shopcarlist_checkbox;
		private ImageView item_shopcarlist_iv_product;
		private TextView tv_product;
		private TextView tv_stock;
		private TextView tv_sumBuyNumber;
		private TextView tv_price;
		private TextView tv_levelname;
		private TextView tv_guige;
//		private ImageView item_shopcar_iv_sendouttime;
	}

	/**
	 * 修改商品购买数量接口
	 * 
	 * @param cbIscheck
	 *            是否选中
	 * @param num
	 *            购买数量
	 * @param idShoppCart
	 *            购物车主键
	 * @param isadd
	 *            true代表数量增加false代表减少
	 */
	public void modifyBuyNumber(final ShopCarsBean child,
			final boolean cbIscheck, final boolean isadd, final int num) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("idShoppCart", child.getIdShoppCart() + "");
		params.addBodyParameter("buyNumber", num + "");
		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addBodyParameter("toKen",MyApplication.getInstance().getToKen());
		
		HttpUtils httpUtils=new HttpUtils();
		httpUtils.send(HttpMethod.POST, MyConst.modifyBuyNumber, params,
//		BaseActivity.httpUtils.send(HttpMethod.POST, MyConst.modifyBuyNumber, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						Log.i("json", arg0.result);
						// Toast.makeText(context, num+"修改成功",
						// Toast.LENGTH_SHORT).show();
						// 这里要修改总价,还要传一个checkbox 若未选中不改变价格
						if (mListener != null && cbIscheck) {
							mListener.toTalPriceClick(isadd, child.getPrice());
						}
						child.setBuynumber(num);
						notifyDataSetChanged();
					}
				});
	}

	/**
	 * 删除购物车商品
	 * 
	 * @param idShoppCart
	 *            购物车主键
	 * @param position
	 */
	public void delShoppCartAction(final View convertView,
			RelativeLayout right, final int idCommodity, final int idLevel,final int idShoppCart,
			final int position) {
		final CheckBox cb = (CheckBox) convertView
				.findViewById(R.id.iv_item_shopcarlist_checkbox);
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cb.isChecked()) {
					Toast.makeText(context, "不能删除选中项", Toast.LENGTH_SHORT)
							.show();
				} else {
					RequestParams params = new RequestParams();
					params.addBodyParameter("idShoppCart", idShoppCart + "");
					params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
					params.addBodyParameter("toKen",MyApplication.getInstance().getToKen());
					HttpUtils httpUtils=new HttpUtils();
//					httpUtils.configCookieStore(MyCookieStore.cookieStore);
//					BaseActivity.httpUtils.send(HttpMethod.POST, MyConst.delShoppCartAction,
					httpUtils.send(HttpMethod.POST, MyConst.delShoppCartAction,
							params, new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									// TODO Auto-generated method stub
									Log.i("json", arg0.result);
									try {
										JSONObject jsonObject = new JSONObject(
												arg0.result);
										int errorCode = jsonObject
												.getInt("code");
										if (errorCode == 0) {
											
											childs.remove(position);
//											shopid = MyApplication
//													.getInstance().getShopid();
											shopCarCommodities=MyApplication.getInstance().getShopCarCommodities();
											for (int i = 0; i < shopCarCommodities.size(); i++) {
												
												if(idCommodity==shopCarCommodities.get(i).getIdCommodity()&&idLevel==shopCarCommodities.get(i).getIdLevel()){
													shopCarCommodities.remove(i);
													MyApplication.getInstance().setShopCarCommodities(shopCarCommodities);
													break;
												}
											}
											Intent intent = new Intent();
											intent.setAction(PurchaserHomeFragment.update_list);
											context.sendBroadcast(intent);
											Toast.makeText(context, "成功删除",
													Toast.LENGTH_SHORT).show();
											// 这里加入一个该删除项是否是最后一项 若是
											// 则组项也要删除(或者说更新父Adapter)
											if (nListener != null
													&& childs.size() == 0) {
												Intent intent1 = new Intent();
												intent1.setAction(PurchaserCarFragment.gone_ly_bottom);
												context.sendBroadcast(intent1);
												nListener
														.notifyAdapterClick(true);
											}
											notifyDataSetChanged();
										} else {
											Toast.makeText(context, "删除失败，请重试",
													Toast.LENGTH_SHORT).show();
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
				}
			}
		});
	}
	

	onCalendarPriceClickListener listener = null;

	public void setOnCalendarPriceClickListener(
			onCalendarPriceClickListener listener) {
		this.listener = listener;
	}	

	// }
}
