package com.cnmobi.exianmall.type.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.PingjiaAdapter;
import com.cnmobi.exianmall.adapter.ProductDetailAdapter;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.AllLevel;
import com.cnmobi.exianmall.bean.CommodityDeatilBannerImage;
import com.cnmobi.exianmall.bean.CommodityDescribe;
import com.cnmobi.exianmall.bean.CommodityDetailBean;
import com.cnmobi.exianmall.bean.CommodityLevel;
import com.cnmobi.exianmall.bean.ConfirmOrderProductBean;
import com.cnmobi.exianmall.bean.HisttoryOrderBean;
import com.cnmobi.exianmall.bean.Pingjia;
import com.cnmobi.exianmall.bean.ShopCarCommodity;
import com.cnmobi.exianmall.bean.ShopCarsBean;
import com.cnmobi.exianmall.fragment.PurchaserCarFragment;
import com.cnmobi.exianmall.fragment.PurchaserHomeFragment;
import com.cnmobi.exianmall.mine.activity.MineProductionPlaceActivity;
import com.cnmobi.exianmall.widget.ImageCycleView;
import com.cnmobi.exianmall.widget.ImageCycleView.ImageCycleViewListener;
import com.cnmobi.exianmall.widget.MyListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商品详情页
 * 
 */
public class ProductDetailActivity extends BaseActivity {
	@ViewInject(R.id.cycleView)
	ImageCycleView cycleView;
	@ViewInject(R.id.iv_a)
	ImageView iv_a;
	@ViewInject(R.id.iv_b)
	ImageView iv_b;
	@ViewInject(R.id.iv_c)
	ImageView iv_c;
	@ViewInject(R.id.view1)
	View view1;
	@ViewInject(R.id.view2)
	View view2;
	@ViewInject(R.id.product_detail_jianjie)
	TextView tv_jianjie;
	@ViewInject(R.id.product_detail_pingjia)
	TextView tv_pingjia;   
	@ViewInject(R.id.tv_no_pingjia)
	TextView tv_no_pingjia;   
	@ViewInject(R.id.ll_jianjie)
	LinearLayout ll_jianjie;   //简介模块
	@ViewInject(R.id.lv_pingjia)
	MyListView lv_pingjia;      //评价模块
	@ViewInject(R.id.tv_num)
	TextView tv_num;
	@ViewInject(R.id.tv_sumbuynumber)
	TextView tv_sumbuynumber;
	@ViewInject(R.id.tv_price)
	TextView tv_price;
	@ViewInject(R.id.tv_commodityname)
	TextView tv_commodityname;
	@ViewInject(R.id.tv_product_address)
	TextView tv_product_address;
	@ViewInject(R.id.tv_stock)
	TextView tv_stock;
	@ViewInject(R.id.tv_levelname)
	TextView tv_levelname;
	@ViewInject(R.id.tv_grossweight)
	TextView tv_grossweight;
	@ViewInject(R.id.tv_select_all)
	TextView tv_select_all;
	@ViewInject(R.id.tv_cleanweight)
	TextView tv_cleanweight;
	@ViewInject(R.id.tv_outerpacking)
	TextView tv_outerpacking;
	@ViewInject(R.id.tv_product_place)
	TextView tv_product_place;
	@ViewInject(R.id.tv_packageweight)
	TextView tv_packageweight;
	@ViewInject(R.id.tv_product_brandname)
	TextView tv_product_brandname;
	@ViewInject(R.id.lv_product)
	MyListView listView;
	@ViewInject(R.id.lay_bottom)
	LinearLayout lay_bottom;
	@ViewInject(R.id.rl_bottom)
	LinearLayout rl_numaddanddel;
//	@ViewInject(R.id.scrollView1)
//	ScrollView scrollView;
//	@ViewInject(R.id.id_swipe_pingjia)
//	SwipeRefreshLayout mSwipeLayout; 
	@ViewInject(R.id.rl_banner)
	RelativeLayout rl_banner;
	
	private ArrayList<String> mImageUrl = new ArrayList<String>();// 轮播图url集合
	private List<CommodityDescribe> commodityDescribes = new ArrayList<CommodityDescribe>();
	private ArrayList<AllLevel> allLevels = new ArrayList();
	private AllLevel allLevel;
	private List<CommodityLevel> commodityLevels = new ArrayList<CommodityLevel>();
	private List<ConfirmOrderProductBean> orderProductList = new ArrayList<ConfirmOrderProductBean>();
	private List<Pingjia> pingjias = new ArrayList<Pingjia>();
	private List<Pingjia> pingjiasAll = new ArrayList<Pingjia>();
	private PingjiaAdapter adapter;
	private CommodityDetailBean commodityDetailBean;
	private int count = 1;// 商品数量
	private String idCommodity = "";
	private String imageUrl = "";
	private String idLevel = "";
	private String idLevel1 = "";
	private Bundle bundle;
	private int hour;
	/**
	 * 评价列表页数
	 */
	private int page = 1;

	/**
	 * 评价列表加载完毕
	 */
	private boolean isAll = false;
	public static final int getLevelWeight = 0;
	/**
	 * 商品详情接口标识
	 */
	public static final int productionDetail = 0;
	/**
	 * 商品等级切换接口标识
	 */
	public static final int cutProductionLevel = 1;
	/**
	 * 添加至购物车接口标识
	 */
	public static final int addShoppCar = 2;
	/**
	 * 购物车接口标识
	 */
	public static final int shoppcar = 3;
	/**
	 * 商品评价查询
	 */
	public static final int productionPingjia = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_product_detail);
		ViewUtils.inject(this);
		lv_pingjia.setVisibility(View.VISIBLE);
		setTitleText("商品详情");
		if (!TextUtils.isEmpty(MyApplication.getInstance().getTlr_type())&&MyApplication.getInstance().getTlr_type().equals("1")) {
//			lay_bottom.setVisibility(View.GONE);
			rl_numaddanddel.setVisibility(View.GONE);
		}
		
		
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		LayoutParams params=rl_banner.getLayoutParams();
		params.width=width;
		params.height=(int) (width*0.75);
		rl_banner.setLayoutParams(params);
		
		idCommodity = getIntent().getStringExtra("idCommodity");
		idLevel1= getIntent().getStringExtra("idLevel");
		imageUrl = getIntent().getStringExtra("imageUrl");
		getProductionDetail();
			
		
		LogUtils.i("idCommodity:"+idCommodity);
		LogUtils.i("idLevel1:"+idLevel1);
		initEvaluateDetail();
//		getEvaluateDetail();
		
	}
	
	
	
	void initEvaluateDetail(){
//		mSwipeLayout.setOnRefreshListener(this);  
//		mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,  android.R.color.holo_orange_light, android.R.color.holo_red_light);  
		adapter=new PingjiaAdapter(this,pingjias);
		lv_pingjia.setAdapter(adapter);
		//这里暂没有分页----
//		lv_pingjia.setOnScrollListener(new OnScrollListener() {
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				// 当不滚动时
//				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//					// 判断是否滚动到底部
//					if (view.getLastVisiblePosition() == view.getCount() - 1) {
//						// 加载更多功能的代码
//						LogUtils.i("-------" + isAll);
//						if (!isAll) {
//							page++;
							getEvaluateDetail();
//						}
//					}
//				}
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//				// TODO Auto-generated method stub
//			}
//		});
	} 
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case getLevelWeight:
				allLevels = (ArrayList<AllLevel>) bundle.getSerializable("list");
				String levelInfo = bundle.getString("leve");
				refreshLeve(allLevels, levelInfo);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 商品详情
	 */
	void getProductionDetail() {
		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
		params.addQueryStringParameter("idCommodity", idCommodity);
		params.addQueryStringParameter("idLevel", idLevel1);
		doHttp(0, MyConst.commodityDetailsAction, params, productionDetail);
		
//		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
//		params.addQueryStringParameter("idCommodity", idCommodity);
//		params.addQueryStringParameter("idCommodity", idCommodity);
//		params.addQueryStringParameter("idLevel", idLevel1);
//		HttpUtils httpUtils=new HttpUtils();
	}
	
	
	/**
	 * 评价详情
	 */
	void getEvaluateDetail() {
		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
		params.addQueryStringParameter("idCommodity", idCommodity);
		params.addQueryStringParameter("idLevel", idLevel1);   
		params.addQueryStringParameter("currentPage", "1");
		doHttp(0, MyConst.commodityEvaluateAction, params, productionPingjia,false);
	}

	/**
	 * 商品等级切换
	 */
	void cutProductionLevel(String idLevel) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("idCommodity", idCommodity);
		params.addQueryStringParameter("idLevel", idLevel);
		doHttp(0, MyConst.cutLevelAction, params, cutProductionLevel);
	}

	/**
	 * 添加至购物车
	 */
	void addShoppCar() {
		RequestParams params = new RequestParams();
//		params.addBodyParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
		params.addQueryStringParameter("idCommodity", idCommodity);
		params.addQueryStringParameter("idLevel", idLevel);
		params.addQueryStringParameter("buynumber", getStr(tv_num));
		doHttp(1, MyConst.addShoppCartAction, params, addShoppCar);
	}
   
	/*
	 * 网络请求统一处理
	 */
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		Gson gson;
		switch (flag) {
		case productionDetail:
//			iv_a.setVisibility(View.VISIBLE);
//			iv_a.setVisibility(View.VISIBLE);
//			iv_a.setVisibility(View.VISIBLE);
			
			gson = new Gson();
			commodityDetailBean = gson.fromJson(jsonString, CommodityDetailBean.class);
			if (jsonString.equals("null"))
				return;
			// 轮播
			CommodityDeatilBannerImage commodityDeatilBannerImage;
			if (commodityDetailBean != null) {
				for (int i = 0; i < commodityDetailBean.getImageNames().size(); i++) {
					commodityDeatilBannerImage = new CommodityDeatilBannerImage();
					commodityDeatilBannerImage = commodityDetailBean.getImageNames().get(i);
					mImageUrl.add(commodityDeatilBannerImage.getImageName());
				}
				// 商品品级
				for (int i = 0; i < commodityDetailBean.getAllLevel().size(); i++) {
					allLevel = new AllLevel();
					allLevel = commodityDetailBean.getAllLevel().get(i);
					allLevels.add(allLevel);
				}
				commodityDetailBean.setAllLevel(allLevels);
				if(commodityDetailBean.getCommodityLevel().size()!=0&&!TextUtils.isEmpty(commodityDetailBean.getCommodityLevel().get(0).getIdLevel())){
					idLevel = commodityDetailBean.getCommodityLevel().get(0).getIdLevel();
					Message msg = new Message();
					msg.what = getLevelWeight;
					bundle = new Bundle();
					bundle.putString("leve", commodityDetailBean.getCommodityLevel().get(0).getLevelname());
					bundle.putSerializable("list", allLevels);
					handler.sendMessage(msg);
				}

				// 描述
				CommodityDescribe commodityDescribe;
				for (int i = 0; i < commodityDetailBean.getCommoditydescribe().size(); i++) {
					commodityDescribe = new CommodityDescribe();
					commodityDescribe = commodityDetailBean.getCommoditydescribe().get(i);
					commodityDescribes.add(commodityDescribe);
				}

				if (mImageUrl.size() != 0 && mImageUrl != null) {
					cycleView.setImageResources(mImageUrl, null, mAdCycleViewListener);
				}
				tv_commodityname.setText(commodityDetailBean.getCommodityname());
				tv_sumbuynumber.setText("月销量" + commodityDetailBean.getCommodityLevel().get(0).getInSales() );
				tv_product_address.setText(commodityDetailBean.getCorpname());
				tv_price.setText("¥" + commodityDetailBean.getCommodityLevel().get(0).getPrice() + "/"
						+ commodityDetailBean.getCommodityLevel().get(0).getCompanyname());
				if(Integer.parseInt(commodityDetailBean.getCommodityLevel().get(0).getStock())<0){
					tv_stock.setText("库存0");
				}else{
					tv_stock.setText("库存" + commodityDetailBean.getCommodityLevel().get(0).getStock());
				}
				tv_product_brandname.setText(commodityDetailBean.getBrandname());
				tv_grossweight.setText(commodityDetailBean.getCommodityLevel().get(0).getGrossweight() + "斤");
				tv_cleanweight.setText(commodityDetailBean.getCommodityLevel().get(0).getCleanweight() + "斤");
				tv_levelname.setText(commodityDetailBean.getCommodityLevel().get(0).getLevelname());
				tv_outerpacking.setText(commodityDetailBean.getCommodityLevel().get(0).getOuterpacking() + "斤");
				tv_outerpacking.setText(commodityDetailBean.getCommodityLevel().get(0).getCompanyname());
				tv_packageweight.setText(commodityDetailBean.getCommodityLevel().get(0).getOuterpacking() + "斤");
				if(!TextUtils.isEmpty(commodityDetailBean.getCommodityLevel().get(0).getProducaddress())){
					tv_product_place.setText(commodityDetailBean.getCommodityLevel().get(0).getProducaddress());
				}else{
					tv_product_place.setText(commodityDetailBean.getCorpname());
				}
				listView.setFocusable(false);
				listView.setAdapter(new ProductDetailAdapter(commodityDescribes, ProductDetailActivity.this));
			}

			break;
		case cutProductionLevel:
			ArrayList<CommodityLevel> commodityLevels = new ArrayList<CommodityLevel>();
			commodityLevels = new Gson().fromJson(jsonString, new TypeToken<List<CommodityLevel>>() {
			}.getType());
			commodityDetailBean.setCommodityLevel(commodityLevels);
			tv_price.setText("¥" + commodityLevels.get(0).getPrice() + "/" + commodityLevels.get(0).getCompanyname());
			tv_stock.setText("库存" + commodityLevels.get(0).getStock() );
			tv_grossweight.setText(commodityLevels.get(0).getGrossweight() + "斤");
			tv_sumbuynumber.setText("月销量" + commodityLevels.get(0).getInSales() );
			tv_cleanweight.setText(commodityLevels.get(0).getCleanweight() + "斤");
			tv_levelname.setText(commodityLevels.get(0).getLevelname());
			tv_outerpacking.setText(commodityLevels.get(0).getCompanyname());
			tv_packageweight.setText(commodityLevels.get(0).getOuterpacking() + "斤");
			tv_product_brandname.setText(commodityDetailBean.getBrandname());
			idLevel = commodityLevels.get(0).getIdLevel();
			
			refreshLeve(allLevels, commodityLevels.get(0).getLevelname());
//			if ("A级".equals(commodityLevels.get(0).getLevelname())) {
//				cleanAllLevel();
//				iv_a.setImageResource(R.drawable.ic_lv_a);
//				LogUtils.i("1");
//				refreshLeve(levelSize, level)
//			} else if ("A-级".equals(commodityLevels.get(0).getLevelname())) {
//				cleanAllLevel();
//				iv_b.setImageResource(R.drawable.ic_lv_b);
//				LogUtils.i("2");
//			} else if ("A+级".equals(commodityLevels.get(0).getLevelname())) {
//				cleanAllLevel();
//				iv_c.setImageResource(R.drawable.ic_lv_c);
//				LogUtils.i("1");
//			}
			break;
		case addShoppCar:
			showToast("添加至购物车成功");
//			MyApplication.getInstance().getShopid().add(Integer.parseInt(idCommodity));
			MyApplication.getInstance().getShopCarCommodities().add(new ShopCarsBean(Integer.parseInt(idCommodity), Integer.parseInt(idLevel)));
			Intent intent = new Intent();
			Intent intent1 = new Intent();
			intent.setAction(PurchaserCarFragment.update_car);
			intent1.setAction(PurchaserHomeFragment.update_list);
			sendBroadcast(intent);
			sendBroadcast(intent1);
			break;
		case productionPingjia:
//			ArrayList<Pingjia> pingjias = new ArrayList<Pingjia>();
//			pingjias = new Gson().fromJson(jsonString, new TypeToken<List<Pingjia>>() {
//			}.getType());
//			lv_pingjia.setAdapter(new PingjiaAdapter(this,pingjias));
			List<Pingjia> lists = new ArrayList<Pingjia>();
			lists = new Gson().fromJson(jsonString, new TypeToken<List<Pingjia>>() {
			}.getType());
			
//			if(isPullDown){
//				pingjias.clear();
//				mSwipeLayout.setRefreshing(false);//结束下拉刷新动画
//				isPullDown=!isPullDown;
//			}
			if(lists.size()==0){
				tv_select_all.setVisibility(View.GONE);
				tv_no_pingjia.setVisibility(View.VISIBLE);
			}else{
				tv_no_pingjia.setVisibility(View.GONE);
				tv_select_all.setVisibility(View.VISIBLE);
				for (int i = 0; i < lists.size(); i++) {
					pingjias.add(lists.get(i));
					if(i>1){
						break;
					}
				}
				for (int i = 0; i < lists.size(); i++) {
					pingjiasAll.add(lists.get(i));
				}
				adapter.notifyDataSetChanged();
			}
//			pingjias.get(0).setEvaluatecontent("List<Pingjia> aaaa阿卡丽发货发货的风景KHDFK和划分及11ffffffffffffffffffflists = new ArrayList<Pingjia>();lists = new Gson().fromJson(jsonString, new TypeToken<List<Pingjia>>() {}");
//			if (pingjias.size() == 0) {
//				showToast("没有评价信息！");
////				isAll = true;
//			}
			break;
		default:
			break;
		}
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(int position, View imageView) {
			// TODO 单击图片处理事件
		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView, MyConst.IM_IMAGE_OPTIONS);
		}
	};

	@OnClick({ R.id.btn_add_shoppcar, R.id.btn_buy,
			R.id.product_detail_jianjie, R.id.product_detail_pingjia,
			R.id.iv_a, R.id.iv_b, R.id.iv_c ,R.id.tv_select_all,R.id.iv_toshopcar})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_select_all:
			Intent intent1=new Intent(ProductDetailActivity.this, ProductPingJiaActivity.class);
			intent1.putExtra("idCommodity", idCommodity);
			intent1.putExtra("idLevel", idLevel);
			intent1.putExtra("pingjiasList", (Serializable) pingjiasAll);
			startActivity(intent1);
			break;
		case R.id.iv_a:
			// A-级
			for(int i=0;i<allLevels.size();i++){
				if(TextUtils.equals("A-级", allLevels.get(i).getLevelName())){
					cutProductionLevel(allLevels.get(i).getIdLevel());
					break;
				}
			}
			break;
		case R.id.iv_b:
			// A级
			for(int i=0;i<allLevels.size();i++){
				if(TextUtils.equals("A级", allLevels.get(i).getLevelName())){
					cutProductionLevel(allLevels.get(i).getIdLevel());
					break;
				}
			}
			break;
		case R.id.iv_c:
			// A+级 
			for(int i=0;i<allLevels.size();i++){
				if(TextUtils.equals("A+级", allLevels.get(i).getLevelName())){
					cutProductionLevel(allLevels.get(i).getIdLevel());
					break;
				}
			}
			break;
		case R.id.product_detail_jianjie:
			// 隐藏评价,显示简介 
//			mSwipeLayout.setVisibility(View.GONE);
			lv_pingjia.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			view1.setBackgroundResource(R.color.yellow);
			view2.setBackgroundResource(R.color.gray);
			tv_jianjie.setTextColor(Color.parseColor("#FEC91B"));
			tv_pingjia.setTextColor(Color.GRAY);
			break;
		case R.id.product_detail_pingjia:
//			getEvaluateDetail();
			// 隐藏简介,显示评价 
			listView.setVisibility(View.GONE);
//			mSwipeLayout.setVisibility(View.VISIBLE);
			lv_pingjia.setVisibility(View.VISIBLE);
			view1.setBackgroundResource(R.color.gray);
			view2.setBackgroundResource(R.color.yellow);
			tv_jianjie.setTextColor(Color.GRAY);
			tv_pingjia.setTextColor(Color.parseColor("#FEC91B"));
			break;
		case R.id.btn_add_shoppcar:
			hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			LogUtils.i("hour:"+hour);
			if (!MyApplication.isInShopCar(Integer.parseInt(idCommodity),Integer.parseInt(idLevel))) {
				if(!"未标价".equals(commodityDetailBean.getCommodityLevel().get(0).getPrice())&&Integer.parseInt(commodityDetailBean.getCommodityLevel().get(0).getStock())>0){
					//不可下单：00点到上午10点
					 if(0<=hour && hour<10){
						showToast("0点-上午10点为系统维护时间，不能购买商品！");	
					}else{
						addShoppCar();
					}
				}else{
					if("未标价".equals(commodityDetailBean.getCommodityLevel().get(0).getPrice())){
						showToast("未标价的商品无法购买哦！");
					}else{
						showToast("商品库存不足！");
					}
				}
			} else {
				showToast("商品已在购物车");
			}
			break;
		case R.id.btn_buy:
			hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if(PurchaserCarFragment.isCanSubmitOrder()){
				if(!"未标价".equals(commodityDetailBean.getCommodityLevel().get(0).getPrice())&& Integer.parseInt(commodityDetailBean.getCommodityLevel().get(0).getStock())>0){
					//加急单 下午14点   至0点    14点到20点(80元一单)  20到0点(150元)  按正常到货时间（所购商品不能保证数量）
					if(14<=hour && hour<=23){
						LayoutInflater inflaterDl = LayoutInflater.from(getApplicationContext());
						RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
								.inflate(R.layout.dialog_clean_message, null);
						Builder builder1 = new AlertDialog.Builder(ProductDetailActivity.this);
						builder1.setView(relativeLayout);
						final Dialog dialog1 = builder1.create();
						dialog1.show();
						TextView tv_content=(TextView) relativeLayout.findViewById(R.id.tv_content);
						tv_content.setText("下午14点至0点为加急订单,14点到20点加收80元一单,20点到0点加收150元一单,按正常到货时间送达(所购商品不能保证数量),您确定要下单购买吗？");
						Button btn_ok=(Button) relativeLayout.findViewById(R.id.btn_clean_message_ok);
						Button btn_cancel=(Button) relativeLayout.findViewById(R.id.btn_clean_message_cancel);
						btn_cancel.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog1.dismiss();
							}
						});
						btn_ok.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog1.dismiss();
								double sendPrice=0;
								if(14<=hour && hour<20){
									sendPrice=80;
								}else if(20<=hour && hour<0){
									sendPrice=150;
								}
								ConfirmOrderProductBean bean = new ConfirmOrderProductBean();
								bean.setAdress(commodityDetailBean.getAddress());
								bean.setIdStore(Integer.parseInt(commodityDetailBean.getIdStore()));
								bean.setBuyNum(Integer.parseInt(getStr(tv_num)));
								bean.setImageUrl(imageUrl);
								bean.setLevel(commodityDetailBean.getCommodityLevel().get(0).getLevelname());
								bean.setIdLevel(Integer.parseInt(commodityDetailBean.getCommodityLevel().get(0).getIdLevel()));
								bean.setName(commodityDetailBean.getCommodityname());
								bean.setStock(Integer.parseInt(commodityDetailBean.getCommodityLevel().get(0).getStock()));
								bean.setSumBuyNum(commodityDetailBean.getSumBuyNumber());
								bean.setAdressId(commodityDetailBean.getIdStore());
								bean.setIdShopCart("");
								bean.setSendPrice(sendPrice);
								bean.setCompanyName(commodityDetailBean.getCommodityLevel().get(0).getCompanyname());
								bean.setIdCommodity(Integer.parseInt(idCommodity));
								bean.setPrice(commodityDetailBean.getCommodityLevel().get(0).getPrice());
								orderProductList.clear();//这里要先clear下，不然SingleOrderActivity接收的时候会有问题
								orderProductList.add(bean);
								Intent intent = new Intent(ProductDetailActivity.this, SingleOrderActivity.class);
								intent.putExtra("orderProductList", (Serializable) orderProductList);
								startActivity(intent);
								
							}
						});
					}else
						if(10<=hour && hour<14){
						//正常下单：上午10点   至下午14点 
						ConfirmOrderProductBean bean = new ConfirmOrderProductBean();
						bean.setAdress(commodityDetailBean.getAddress());
						bean.setIdStore(Integer.parseInt(commodityDetailBean.getIdStore()));
						bean.setBuyNum(Integer.parseInt(getStr(tv_num)));
						bean.setImageUrl(imageUrl);
						bean.setLevel(commodityDetailBean.getCommodityLevel().get(0).getLevelname());
						bean.setIdLevel(Integer.parseInt(commodityDetailBean.getCommodityLevel().get(0).getIdLevel()));
						bean.setName(commodityDetailBean.getCommodityname());
						bean.setStock(Integer.parseInt(commodityDetailBean.getCommodityLevel().get(0).getStock()));
						bean.setSumBuyNum(commodityDetailBean.getSumBuyNumber());
						bean.setAdressId(commodityDetailBean.getIdStore());
						bean.setIdShopCart("");
						bean.setSendPrice(0);
						bean.setCompanyName(commodityDetailBean.getCommodityLevel().get(0).getCompanyname());
						bean.setIdCommodity(Integer.parseInt(idCommodity));
						bean.setPrice(commodityDetailBean.getCommodityLevel().get(0).getPrice());
						orderProductList.clear();//这里要先clear下，不然SingleOrderActivity接收的时候会有问题
						orderProductList.add(bean);
						Intent intent = new Intent(ProductDetailActivity.this, SingleOrderActivity.class);
						intent.putExtra("orderProductList", (Serializable) orderProductList);
						startActivity(intent);
					}else if(0<=hour && hour<10){
					    //不可下单：00点到上午10点
						showToast("0点-上午10点为系统维护时间，不能购买商品！");	
					}

					
				}else{
					if("未标价".equals(commodityDetailBean.getCommodityLevel().get(0).getPrice())){
						showToast("未标价的商品无法购买哦！");
					}else{
						showToast("商品库存不足！");
					}
				}
			}else{
				showToast("请等待工作人员审核通过后才能购买");
			}
			break;
		case R.id.iv_toshopcar:
			Intent intent=new Intent(ProductDetailActivity.this, MainActivity.class);
			intent.putExtra("flag", "toShopCar");
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	
//	/**
//	 * 提交订单
//	 */
//	@OnClick(R.id.btn_buy)
//	public void buy(View view) {
//		Intent intent = new Intent(ProductDetailActivity.this, SingleOrderActivity.class);
//		startActivity(intent);
//	}

	/**
	 * 跳转到产地
	 */
	@OnClick(R.id.tv_product_address)
	public void address(View view) {
		Intent intent = new Intent(ProductDetailActivity.this, MineProductionPlaceActivity.class);
		intent.putExtra("idStore", commodityDetailBean.getIdStore());
		startActivity(intent);
	}

	/**
	 * 商品数量加一
	 */
	@OnClick(R.id.btn_addnum)
	public void add(View view) {
		if(count<Integer.parseInt(commodityDetailBean.getCommodityLevel().get(0).getStock())){
			count++;
			tv_num.setText(count + "");
		}else{
			showToast("超出商品库存！");
		}
	}

	/**
	 * 商品数量减一
	 */
	@OnClick(R.id.btn_cutnum)
	public void cut(View view) {
		if (count > 1) {
			count--;
			tv_num.setText(count + "");
		}
	}

	/**
	 * 显示品级
	 */
	void refreshLeve(List<AllLevel>list, String level) {
		if (list.size() == 1) {
			if ("A-级".equals(level)) {
				iv_a.setVisibility(View.VISIBLE);
				iv_b.setVisibility(View.GONE);
				iv_c.setVisibility(View.GONE);
				iv_a.setImageResource(R.drawable.ic_lv_b);
			} else if ("A级".equals(level)) {
				iv_a.setVisibility(View.GONE);
				iv_b.setVisibility(View.VISIBLE);
				iv_c.setVisibility(View.GONE);
				iv_b.setImageResource(R.drawable.ic_lv_a);
			} else {
				iv_a.setVisibility(View.GONE);
				iv_b.setVisibility(View.GONE);
				iv_c.setVisibility(View.VISIBLE);
				iv_c.setImageResource(R.drawable.ic_lv_c);
			}
		}
		if (list.size() == 2) {
			if ("A-级".equals(level)) {
				LogUtils.i("----->>>>>1");
				iv_a.setVisibility(View.VISIBLE);
				iv_a.setImageResource(R.drawable.ic_lv_b);
				for(int i=0;i<2;i++){
					if(list.get(i).getLevelName()!=level){
						if("A级".equals(list.get(i).getLevelName())){
							iv_b.setVisibility(View.VISIBLE);
							iv_c.setVisibility(View.GONE);
							iv_b.setImageResource(R.drawable.ic_lv_a_nor);
						}else{
							iv_b.setVisibility(View.GONE);
							iv_c.setVisibility(View.VISIBLE);
							iv_c.setImageResource(R.drawable.ic_lv_c_nor);
						}
					}
				}
			} else if ("A级".equals(level)) {
				LogUtils.i("----->>>>>2");
				iv_b.setVisibility(View.VISIBLE);
				iv_b.setImageResource(R.drawable.ic_lv_a);
				for(int i=0;i<2;i++){
					if(list.get(i).getLevelName()!=level){
						if("A-级".equals(list.get(i).getLevelName())){
							iv_a.setVisibility(View.VISIBLE);
							iv_c.setVisibility(View.GONE);
							iv_a.setImageResource(R.drawable.ic_lv_b_nor);
						}else{
							iv_a.setVisibility(View.GONE);
							iv_c.setVisibility(View.VISIBLE);
							iv_c.setImageResource(R.drawable.ic_lv_c_nor);
						}
					}
				}
			} else {
				LogUtils.i("----->>>>>3");
				iv_c.setVisibility(View.VISIBLE);
				iv_c.setImageResource(R.drawable.ic_lv_c);
				for(int i=0;i<2;i++){
					if(list.get(i).getLevelName()!=level){
						if("A-级".equals(list.get(i).getLevelName())){
							iv_a.setVisibility(View.VISIBLE);
							iv_b.setVisibility(View.GONE);
							iv_a.setImageResource(R.drawable.ic_lv_b_nor);
						}else{
							iv_a.setVisibility(View.GONE);
							iv_b.setVisibility(View.VISIBLE);
							iv_b.setImageResource(R.drawable.ic_lv_a_nor);
						}
					}
				}
				
				
			}

		}
		if (list.size() == 3) {
			if ("A-级".equals(level)) {
				iv_a.setVisibility(View.VISIBLE);
				iv_b.setVisibility(View.VISIBLE);
				iv_c.setVisibility(View.VISIBLE);
				iv_a.setImageResource(R.drawable.ic_lv_b);
				iv_b.setImageResource(R.drawable.ic_lv_a_nor);
				iv_c.setImageResource(R.drawable.ic_lv_c_nor);
			} else if ("A级".equals(level)) {
				iv_a.setVisibility(View.VISIBLE);
				iv_b.setVisibility(View.VISIBLE);
				iv_c.setVisibility(View.VISIBLE);
				iv_a.setImageResource(R.drawable.ic_lv_b_nor);
				iv_b.setImageResource(R.drawable.ic_lv_a);
				iv_c.setImageResource(R.drawable.ic_lv_c_nor);
			} else {
				iv_a.setVisibility(View.VISIBLE);
				iv_b.setVisibility(View.VISIBLE);
				iv_c.setVisibility(View.VISIBLE);
				iv_a.setImageResource(R.drawable.ic_lv_a_nor);
				iv_b.setImageResource(R.drawable.ic_lv_b_nor);
				iv_c.setImageResource(R.drawable.ic_lv_c);
			}

		}
	}

	void cleanAllLevel() {
		iv_a.setImageResource(R.drawable.ic_lv_a_nor);
		iv_b.setImageResource(R.drawable.ic_lv_b_nor);
		iv_c.setImageResource(R.drawable.ic_lv_c_nor);
	}

	@Override
	protected void onResume() {
		super.onResume();
		cycleView.startImageCycle();
		
	};

	@Override
	protected void onPause() {
		super.onPause();
		cycleView.pushImageCycle();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cycleView.pushImageCycle();
	}

//
//	@Override
//	public void onRefresh() {
//		// TODO Auto-generated method stub
//		LogUtils.i("onRefresh");
//		isPullDown=true;
//		mSwipeLayout.setRefreshing(true);//开始下拉刷新动画
//		getEvaluateDetail();
//	}
}
