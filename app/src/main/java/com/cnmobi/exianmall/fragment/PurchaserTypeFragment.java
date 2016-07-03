package com.cnmobi.exianmall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.TypeGridAdapter;
import com.cnmobi.exianmall.adapter.TypeListAdapter;
import com.cnmobi.exianmall.base.BaseFragment;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.BaseBean;
import com.cnmobi.exianmall.bean.ProductDetailBean;
import com.cnmobi.exianmall.home.activity.CaptureActivity;
import com.cnmobi.exianmall.home.activity.SearchActivity;
import com.cnmobi.exianmall.interfaces.OnAddCarItemClickListener;
import com.cnmobi.exianmall.type.activity.ProductDetailActivity;
import com.cnmobi.exianmall.type.activity.ShowTypeActivity;
import com.cnmobi.exianmall.widget.ImageCycleView;
import com.cnmobi.exianmall.widget.ImageCycleView.ImageCycleViewListener;
import com.cnmobi.exianmall.widget.MyGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 分类界面
 */
public class PurchaserTypeFragment extends BaseFragment implements OnRefreshListener{

	@ViewInject(R.id.lv_type)
	ListView typeLv;
	@ViewInject(R.id.gv_type)
	GridView typeGv;
	@ViewInject(R.id.cycleView)
	ImageCycleView cycleView;
	@ViewInject(R.id.tv_type_name)
	TextView tv_type_name;
	@ViewInject(R.id.id_swipe_type)
	SwipeRefreshLayout mSwipeLayout;  
	@ViewInject(R.id.rl)
	RelativeLayout rl;

	private TypeListAdapter listAdapter;
	private TypeGridAdapter gridAdapter;
	private ArrayList<BaseBean> list = new ArrayList<BaseBean>();
	
	/**  获取分类名字*/
	public static final int typeName = 0;
	/**  分类查询*/
	public static final int commiditySortAction = 1;
	private List<ProductDetailBean> shopBeans = new ArrayList<ProductDetailBean>();
	private String idCategory;
	private boolean isPullDown=false;
	/**
	 * 商品列表页数
	 */
	private int page = 1;
	/**
	 * 商品列表加载完毕
	 */
	private boolean isAll = false;
	private int width;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_purchaser_type, container, false);
		ViewUtils.inject(this, view);
		WindowManager wm = (WindowManager) getActivity()
				.getSystemService(Context.WINDOW_SERVICE);
		//屏幕的宽-控件间隔的宽
		width = wm.getDefaultDisplay().getWidth()-2*getResources().getDimensionPixelSize(R.dimen.dimen_10_dip);
		cycleView.setVisibility(View.GONE);
//		rl.setVisibility(View.GONE);
		getTypename();
		init();
		initImg();

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		showSlideMenuListener = (IShowSlideMenuClickListen) activity;
	}

	@OnClick({ R.id.rl_search, R.id.iv_home_right_top, R.id.iv_home_left_top })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_search:// 搜索
			intent = new Intent(getActivity(), SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_home_right_top:// 二维码扫描
			intent = new Intent(getActivity(), CaptureActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_home_left_top:
			break;
		default:
			break;
		}
	}

	/** 获取分类名字*/
	void getTypename() {
		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		doHttp(0, MyConst.allCategoryAction, params, typeName);
	}
	

	/**一级分类查询
	http://121.46.0.219:8080/efreshapp/commiditySortAction?idCategory=1001*/
	void commiditySortHttp(){
		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addQueryStringParameter("idCategory",idCategory);
		params.addQueryStringParameter("currentPage",page+"");
		doHttp(0, MyConst.commiditySortAction, params, commiditySortAction,false);
	}
	/*
	 * 网络请求统一处理
	 */
	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		switch (flag) {
		case typeName:
			ArrayList<BaseBean> list1 =  new Gson().fromJson(jsonString, new TypeToken<List<BaseBean>>() {
			}.getType());
			
			for(int i=0;i<list1.size();i++){
				list.add(list1.get(i));
			}
			listAdapter.notifyDataSetChanged();
			tv_type_name.setText(list.get(0).getCategoryname());
			idCategory=list.get(0).getIdNumber();
			commiditySortHttp();
			break;

		case commiditySortAction:
			ArrayList<ProductDetailBean> list2 = new ArrayList<ProductDetailBean>();
			list2 = new Gson().fromJson(jsonString,
					new TypeToken<List<ProductDetailBean>>() {
					}.getType());
			if(isPullDown){
				shopBeans.clear();
				mSwipeLayout.setRefreshing(false);//结束下拉刷新动画
				isPullDown=!isPullDown;
				isAll = false;
			}
			for (int i = 0; i < list2.size(); i++) {
				shopBeans.add(list2.get(i));
			}
			
			if (list2.size() == 0) {
				if(page==1){
					showToast("没有相关商品信息");
				}else{
					showToast("加载完毕");
				}
				isAll = true;
			}
			gridAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	void init() {
		mSwipeLayout.setOnRefreshListener(this);  
		mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,  android.R.color.holo_orange_light, android.R.color.holo_red_light);  
		
		listAdapter = new TypeListAdapter(getActivity(), list);
		typeLv.setAdapter(listAdapter);
		typeLv.setFocusable(false);
		typeLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				shopBeans.clear();
				listAdapter.changeSelected(arg2);
				tv_type_name.setText(list.get(arg2).getCategoryname());
				idCategory = list.get(arg2).getIdNumber();
				page=1;
				commiditySortHttp();
			}
		});
		
		typeGv.setFocusable(false);
		gridAdapter=new TypeGridAdapter(getActivity(), shopBeans, R.layout.item_type_grid,width);
		typeGv.setAdapter(gridAdapter);
		typeGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				Intent intent=new Intent(getActivity(), ShowTypeActivity.class);
//				intent.putExtra("commodityname", shopBeans.get(arg2).getCommodityName());//商品名称
//				intent.putExtra("idCategory", idCategory);//分类主键
//				intent.putExtra("whitchActivity", 0);//区分那个页面跳转
				Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
				intent.putExtra("idCommodity", shopBeans.get(arg2).getIdCommodity());
				intent.putExtra("idLevel", shopBeans.get(arg2).getIdLevel());
				intent.putExtra("imageUrl", shopBeans.get(arg2).getImagename());
				startActivity(intent);
			}
		});
		
		
		typeGv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 判断是否滚动到底部
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						// 加载更多功能的代码
						if (!isAll) {
							page++;
							commiditySortHttp();
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
	 * 加载轮播图
	 */
	void initImg() {
		if(PurchaserHomeFragment.mImageUrl.size()!=0){
			cycleView.setImageResources(PurchaserHomeFragment.mImageUrl, null, mAdCycleViewListener);
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
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		isPullDown=true;
		page=1;
		mSwipeLayout.setRefreshing(true);//开始下拉刷新动画
		commiditySortHttp();
	}

}
