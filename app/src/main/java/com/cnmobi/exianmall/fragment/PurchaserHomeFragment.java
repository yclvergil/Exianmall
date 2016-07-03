package com.cnmobi.exianmall.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.HistoryOrderApapter;
import com.cnmobi.exianmall.base.BaseFragment;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.BaseBean;
import com.cnmobi.exianmall.bean.HisttoryOrderBean;
import com.cnmobi.exianmall.bean.ShopCarBeanGroup;
import com.cnmobi.exianmall.bean.ShopCarsBean;
import com.cnmobi.exianmall.home.activity.CaptureActivity;
import com.cnmobi.exianmall.home.activity.SearchActivity;
import com.cnmobi.exianmall.interfaces.OnAddCarItemClickListener;
import com.cnmobi.exianmall.login.activity.LoginActivity;
import com.cnmobi.exianmall.mine.activity.AppVersionManager;
import com.cnmobi.exianmall.mine.activity.MineSetActivity;
import com.cnmobi.exianmall.type.activity.ProductDetailActivity;
import com.cnmobi.exianmall.widget.ImageCycleView;
import com.cnmobi.exianmall.widget.ImageCycleView.ImageCycleViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 买家-首页界面
 */
public class PurchaserHomeFragment extends BaseFragment implements OnRefreshListener {

	@ViewInject(R.id.mgv_shouye)
	GridView mgv_shouye;
	@ViewInject(R.id.cycleView)
	ImageCycleView cycleView;
//	@ViewInject(R.id.tv_title_order)
//	TextView tv_title;
	@ViewInject(R.id.id_swipe_main)
	SwipeRefreshLayout mSwipeLayout; 
	HistoryOrderApapter historyOrderApapter;
//	private boolean updateIsShowed=false;
	private List<HisttoryOrderBean> historyOrderDatas = new ArrayList<HisttoryOrderBean>();

	public static ArrayList<String> mImageUrl = new ArrayList<String>();// 轮播图url集合
	private String idCommodity = "";
	private String idLevel = "";
	MyBroadcast mybroadcast;
	public static String update_list = "update";

	/**
	 * 轮播图接口标识
	 */
	public static final int runImg = 0;
	/**
	 * 买家首页历史订单标识
	 */
	public static final int orderList = 1;
	/**
	 * 产品列表标识
	 */
	public static final int productList = 2;
	/**
	 * 添加至购物车接口标识
	 */
	public static final int addShoppCar = 3;
	/**
	 * 购物车接口标识
	 */
	public static final int shoppcar = 4;
	/**
	 * 版本更新接口标识
	 */
	public static final int checkUpDate=5;
	/**
	 * 商品列表页数
	 */
	private int page = 1;

	/**
	 * 商品列表加载完毕
	 */
	private boolean isAll = false;

	/**
	 * 是否有历史订单
	 */
	private boolean hasHistory = true;
	
	private boolean isPullDown=false;
	int width;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_purchaser_home, container, false);
		ViewUtils.inject(this, view);
		if(!TextUtils.isEmpty(MyApplication.getInstance().getToKen())){
			
//			MyApplication.getInstance().setBalance("111111");//测试用 
			WindowManager wm = (WindowManager) getActivity()
					.getSystemService(Context.WINDOW_SERVICE);
			//屏幕的宽-控件间隔的宽
			width = wm.getDefaultDisplay().getWidth()-3*getResources().getDimensionPixelSize(R.dimen.dimen_10_dip);
			initUi();
			shoppCarHttp();
			getImgs();
			getOrderList(); 
			if(!MyApplication.isUpdateDialogShowed){
				checkUpDate();
			}
			mybroadcast = new MyBroadcast();
			IntentFilter filter = new IntentFilter();
			filter.addAction(update_list);
			getActivity().registerReceiver(mybroadcast, filter); 
		}else{
			clearSp();
			showToast("登录已失效，请重新登录！");
			Intent intent=new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
			getActivity().finish();
		}
		  
		return view;
	}

	
	public class MyBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到广播，更新
//			if (historyOrderApapter != null) {
//				historyOrderApapter.notifyDataSetChanged();
//			}
			shoppCarHttp();
			
		}
	}

	private void shoppCarHttp() {
		RequestParams params = new RequestParams();
		doHttp(1, MyConst.shoppCartAction, params, shoppcar, false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mybroadcast!=null){
			getActivity().unregisterReceiver(mybroadcast);// 取消注册
		}
	}

	public void initUi() {
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		
		mgv_shouye.setFocusable(false);
		historyOrderApapter = new HistoryOrderApapter(historyOrderDatas, getActivity(),width);
		mgv_shouye.setAdapter(historyOrderApapter);
		historyOrderApapter.setOnAddCarItemClickListener(new OnAddCarItemClickListener() {

			@Override
			public void addCarItemClick(View v, int position) {
				// TODO Auto-generated method stub
				if(!"未标价".equals(historyOrderDatas.get(position).getPrice())&& Integer.parseInt(historyOrderDatas.get(position).getStock())>0){
						idCommodity = historyOrderDatas.get(position).getIdNumber();
						idLevel=historyOrderDatas.get(position).getIdLevel();
						addShoppCar(idCommodity, idLevel);
				}else{
					if("未标价".equals(historyOrderDatas.get(position).getPrice())){
						showToast("未标价的商品无法购买哦！");
					}else{
						showToast("商品库存不足！");
					}
				}
			}
		});   
		mgv_shouye.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
				intent.putExtra("idCommodity", historyOrderDatas.get(position).getIdNumber());
				intent.putExtra("idLevel", historyOrderDatas.get(position).getIdLevel());
				intent.putExtra("imageUrl", historyOrderDatas.get(position).getImagename());
				startActivity(intent);
			}
		});

		mgv_shouye.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 判断是否滚动到底部
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						// 加载更多功能的代码
						if (!isAll) {
							page++;
							getProductList();
						}else{
							showToastShort("已加载完毕");
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * 获取轮播图
	 */
	void getImgs() {
		RequestParams params = new RequestParams();
		doHttp(0, MyConst.carouselAction, params, runImg, false);
	}

	/**
	 * 买家首页历史订单
	 */
	void getOrderList() {
		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("currentPage", page + "");
		LogUtils.i("token:"+MyApplication.getInstance().getToKen());
		doHttp(0, MyConst.historicalAction, params, orderList, false);
	}

	/**
	 * 商品列表
	 */
	void getProductList() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("currentPage", page + "");
		doHttp(0, MyConst.commodityListAction, params, productList, false);
	}

	/**
	 * 添加至购物车
	 */
	void addShoppCar(String idCommodity, String idLevel) {
		RequestParams params = new RequestParams();
//		params.addBodyParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
		params.addQueryStringParameter("idCommodity", idCommodity);
		params.addQueryStringParameter("idLevel", idLevel);
		params.addQueryStringParameter("buynumber", "" + 1);
		doHttp(1, MyConst.addShoppCartAction, params, addShoppCar);
	}
	void checkUpDate(){
		RequestParams params=new RequestParams();
		doHttp(0, MyConst.newVersion, params,checkUpDate);
		
	}
	
	/*
	 * 网络请求统一处理
	 */
	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		switch (flag) {
		case runImg:
			ArrayList<BaseBean> list = new ArrayList<BaseBean>();
			list = new Gson().fromJson(jsonString, new TypeToken<List<BaseBean>>() {
			}.getType());
			mImageUrl.clear();
			for (int i = 0; i < list.size(); i++) {
				mImageUrl.add(list.get(i).getImagename());
			}
			if (mImageUrl.size() != 0 && mImageUrl != null) {
				cycleView.setImageResources(mImageUrl, null, mAdCycleViewListener);
			}
			break;
		case orderList:
			List<HisttoryOrderBean> listOrder = new ArrayList<HisttoryOrderBean>();
			listOrder = new Gson().fromJson(jsonString, new TypeToken<List<HisttoryOrderBean>>() {
			}.getType());
			//如果有历史订单	
			if(isPullDown){
				historyOrderDatas.clear();
			}
			if(listOrder.size()!=0){
				for (int i = 0; i < listOrder.size(); i++) {
					historyOrderDatas.add(listOrder.get(i));
				}
				
//				historyOrderApapter.notifyDataSetChanged();
				getProductList();
			}
			
			// 如果没有历史订单就加载商品订单列表
			if (historyOrderDatas.size() == 0) {
				hasHistory = false;
				isAll = false;
//				tv_title.setText("商品列表");
				getProductList();
			} else {
				LogUtils.e("11");
				if (listOrder.size() == 0) {
					ShowToast("加载完毕");
					isAll = true;
				}
			}
			historyOrderApapter.notifyDataSetChanged();
			break;
		case productList:
			if(isPullDown){
				mSwipeLayout.setRefreshing(false);//结束下拉刷新动画
				isPullDown=!isPullDown;
				isAll = false;
			}
			List<HisttoryOrderBean> listOrder1 = new ArrayList<HisttoryOrderBean>();
			listOrder1 = new Gson().fromJson(jsonString, new TypeToken<List<HisttoryOrderBean>>() {
			}.getType());
			for (int i = 0; i < listOrder1.size(); i++) {
				historyOrderDatas.add(listOrder1.get(i));
			}
			historyOrderApapter.notifyDataSetChanged();
			if (listOrder1.size() == 0) {
				ShowToast("加载完毕");
				isAll = true;
			}
			break;
		case addShoppCar:
			MyApplication.getInstance().getShopCarCommodities().add(new ShopCarsBean(Integer.parseInt(idCommodity), Integer.parseInt(idLevel)));
			historyOrderApapter.notifyDataSetChanged();
			ShowToast("添加至购物车成功");
			Intent intent = new Intent();
			intent.setAction(PurchaserCarFragment.update_car);
			getActivity().sendBroadcast(intent);
			break;
		case shoppcar:
//			List<ShopCarBeanGroup> grouplist = new ArrayList<ShopCarBeanGroup>();
			List<ShopCarsBean> carCommodities=new ArrayList<ShopCarsBean>();
//			grouplist = new Gson().fromJson(jsonString, new TypeToken<List<ShopCarBeanGroup>>() {
//			}.getType());
			carCommodities = new Gson().fromJson(jsonString, new TypeToken<List<ShopCarsBean>>() {
				}.getType());
//			LogUtils.e(">>>"+grouplist.size());
//			for (int i = 0; i < grouplist.size(); i++) {
//				List<ShopCarsBean> childlist = grouplist.get(i).getShoppCartCommodity();
//				LogUtils.e(">>><<"+childlist.size());
////				for (int j = 0; j < childlist.size(); j++) {
////					carCommodities.add(new ShopCarsBean(childlist.get(j).getIdCommodity(), childlist.get(j).getIdLevel()));
////				}
//			}
			MyApplication.getInstance().setShopCarCommodities(carCommodities);
			historyOrderApapter.notifyDataSetChanged();
			List<ShopCarBeanGroup> grouplist = new ArrayList<ShopCarBeanGroup>();
//			List<ShopCarCommodity> carCommodities=new ArrayList<ShopCarCommodity>();
			grouplist = new Gson().fromJson(jsonString, new TypeToken<List<ShopCarBeanGroup>>() {
			}.getType());
			
//			for (int i = 0; i < grouplist.size(); i++) {
//				List<ShoppCartCommodity> childlist = grouplist.get(i).getShoppCartCommodity();
//				for (int j = 0; j < childlist.size(); j++) {
//					carCommodities.add(new ShopCarCommodity(childlist.get(j).getIdCommodity(), childlist.get(j).getIdLevel()));
//				}
//			}
//			MyApplication.getInstance().setShopCarCommodities(carCommodities);
//			historyOrderApapter.notifyDataSetChanged();
			break;
		case checkUpDate:
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				if(!TextUtils.isEmpty(jsonObject.getString("version")) && !TextUtils.equals(jsonObject.getString("version"), new AppVersionManager(getActivity()).getAppVersionName())){
//					String url="http://download.taobaocdn.com/wireless/taobao4android/latest/701483.apk";
					MyApplication.isUpdateDialogShowed=true;
					new AppVersionManager(getActivity()).upDateAppVersion(jsonObject.getString("location"),jsonObject.getString("versionDesc"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 设置轮播图
	 */
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(int position, View imageView) {
		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView, MyConst.IM_IMAGE_OPTIONS);
		}
	};

	@OnClick({ R.id.rl_search, R.id.iv_home_left_top, R.id.iv_home_right_top })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_search:
			intent = new Intent(getActivity(), SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_home_left_top:
			// showSlideMenuListener.OnShowSlideMenuClickListener();

			break;
		case R.id.iv_home_right_top:
			intent = new Intent(getActivity(), CaptureActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		cycleView.startImageCycle();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		cycleView.pushImageCycle();
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		cycleView.pushImageCycle();
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		isPullDown=true;
		page=1;
		mSwipeLayout.setRefreshing(true);//开始下拉刷新动画
		getOrderList();
	}
	
	
}
