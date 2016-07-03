package com.cnmobi.exianmall.type.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.AreaAdapter;
import com.cnmobi.exianmall.adapter.OneTypeListAdapter;
import com.cnmobi.exianmall.adapter.TwoTypeListAdapter;
import com.cnmobi.exianmall.adapter.TypeTwoAdapter;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.AreaBean;
import com.cnmobi.exianmall.bean.BaseBean;
import com.cnmobi.exianmall.bean.ProductBean;
import com.cnmobi.exianmall.bean.ProductDetailBean;
import com.cnmobi.exianmall.bean.ShopCarCommodity;
import com.cnmobi.exianmall.bean.ShopCarsBean;
import com.cnmobi.exianmall.bean.SizeBean;
import com.cnmobi.exianmall.fragment.PurchaserCarFragment;
import com.cnmobi.exianmall.fragment.PurchaserHomeFragment;
import com.cnmobi.exianmall.home.activity.SearchActivity;
import com.cnmobi.exianmall.interfaces.OnAddCarItemClickListener;
import com.cnmobi.exianmall.interfaces.onAreaItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分类二级菜单页面
 * 
 */
public class ShowTypeActivity extends BaseActivity {
	@ViewInject(R.id.activity_show_type_lv_area)
	private ListView lvarea;
	@ViewInject(R.id.activity_show_type_lv_price)
	private ListView lvprice;
	@ViewInject(R.id.activity_show_type_lv_size)
	private ListView lvsize;
	@ViewInject(R.id.activity_show_type_lv_type)
	private ListView lvtype;
	@ViewInject(R.id.activity_show_type_grid)
	private GridView gridView;
//	@ViewInject(R.id.activity_show_type_flv)
//	private ListView activity_show_type_flv;
//	@ViewInject(R.id.activity_show_type_slv)
//	private ListView activity_show_type_slv;
	@ViewInject(R.id.activity_show_type_view)
	private View mv;
	@ViewInject(R.id.activity_show_type_price)
	private TextView tvprice;
	@ViewInject(R.id.activity_show_type_type)
	private TextView tvtype;
	@ViewInject(R.id.activity_show_type_area)
	private TextView tvarea;
	@ViewInject(R.id.activity_show_type_size)
	private TextView tvsize;
	@ViewInject(R.id.tv_second_search)
	private TextView tv_second_search;
	@ViewInject(R.id.activity_show_type_ll)
	private LinearLayout ll;
//	@ViewInject(R.id.activity_show_type_sll)
//	private LinearLayout sll;
	@ViewInject(R.id.iv_price)
	private ImageView ivprice;
	@ViewInject(R.id.iv_add_car)
	private ImageView ivcar;
	private AreaAdapter areaadapter;
	private AreaAdapter sizeadapter;
	private AreaAdapter typeadapter;
	private TypeTwoAdapter adapter;
	private String idCommodity ="";
	private String idLevel = "";
//	private OneTypeListAdapter listAdapter;
//	private TwoTypeListAdapter listAdapter1;
	
	/**
	 * 商品集合
	 */
	private List<ProductBean> shopBeans = new ArrayList<ProductBean>();
	
	/**
	 * 产地列表接口标识
	 */
	public static final int commodityOrigin = 0;
	/**
	 * 规格列表接口标识
	 */
	public static final int commodityNorms = 1;
	/**
	 * 商品列表接口标识
	 */
	public static final int searchCommodity = 2;


	private List<String> area = new ArrayList<String>();// 地区
	private List<String> size = new ArrayList<String>();// 规格
	private List<String> type = new ArrayList<String>();// 种类
	private String commodityname="0";
	private boolean isAsc = true;// 默认升序
	private String price = "asc";// 默认升序
	private String idStore = "0";// 店铺主键
	private String idCategory="0" ;// 分类主键
	private String idCompany = "0";// 规格主键
	private List<String> idStores = new ArrayList<String>();// 店铺主键集合
	private List<String> idCompanys = new ArrayList<String>();// 规格主键集合   
	private List<String> idNumbers = new ArrayList<String>();// 种类主键集合   
	private boolean isClean=true; //fase：下载下一页  true：选择其它搜索条件时清除原来的多页数据，加载第一页
	private int currentPage=1;
	
	private boolean isAll = false;
	
	private int whitchActivity;//区分那个页面跳转  （0一级分类、1搜索）
	private int whitchOne=-1;//区分那个页面跳转  （0二级分类产地、1二级分类种类、2二级分类规格）

	/**  获取分类名字*/
	public static final int typeName = 4;
	/**  分类查询*/
	public static final int commiditySortAction =5;
	private ArrayList<BaseBean> types = new ArrayList<BaseBean>();
//	private ArrayList<BaseBean> list6 = new ArrayList<BaseBean>();
	private ArrayList<ProductDetailBean> list7 = new ArrayList<ProductDetailBean>();
	/**
	 * 添加至购物车接口标识
	 */
	public static final int addShoppCar= 3;
	private int width;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_type);
		ViewUtils.inject(this);
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		//屏幕的宽-控件间隔的宽
		width = wm.getDefaultDisplay().getWidth()-3*getResources().getDimensionPixelSize(R.dimen.dimen_10_dip);;
		init();
	}
	void init() {
		commodityname = getIntent().getStringExtra("commodityname");
		tv_second_search.setText(commodityname+"");
		idCategory = getIntent().getStringExtra("idCategory");
		if(idCategory==null){
			idCategory="0";
		}
		whitchActivity=getIntent().getIntExtra("whitchActivity",0);
		
		getproductList();
		gridViewInit();//gridView初始化
		sizeInit();//规格初始化
		areaInit();//产地初始化
		commiditySortInit();//种类初始化
//		
	}

	
	/**
	 * 商品显示（搜索）
	 */
	private void getproductList() {
		//参数没值的传0，价格默认升序
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("price", price);
		params.addQueryStringParameter("currentPage", String.valueOf(currentPage));
	
		//这里区分有用？？
		if(whitchActivity==0){//一级分类跳转
			params.addQueryStringParameter("idCategory", idCategory);
			params.addQueryStringParameter("commodityname", commodityname);
			params.addQueryStringParameter("idStore", idStore);
			params.addQueryStringParameter("idCompany", idCompany);
//			if(whitchOne==-1){//显示全部、
//			}
//			if(whitchOne==0){//产地
//				params.addQueryStringParameter("idStore", idStore);
//			}
//			else if(whitchOne==1){//种类
//				
//			}
//			else if(whitchOne==2){//规格
//				params.addQueryStringParameter("idStore", idStore);
//				params.addQueryStringParameter("idCompany", idCompany);
//			}
		}
		else if(whitchActivity==1){//搜索跳转
			params.addQueryStringParameter("idStore", idStore);
			params.addQueryStringParameter("idCategory", idCategory);
			params.addQueryStringParameter("idCompany", idCompany);
			params.addQueryStringParameter("commodityname", commodityname);
			
//			params.addQueryStringParameter("idStore", "0");
//			params.addQueryStringParameter("idCategory", "0");
//			params.addQueryStringParameter("idCompany", "0");
//			params.addQueryStringParameter("commodityname", commodityname);	
//			if(whitchOne==0){
//				params.addQueryStringParameter("idStore", idStore);
//			}
//			else if(whitchOne==1){
//				params.addQueryStringParameter("idCategory", idCategory);
//			}else if(whitchOne==2){
//				params.addQueryStringParameter("idCompany", idCompany);
//			}
		}
		
		doHttp(0, MyConst.searchCommodity, params, searchCommodity);
		
		LogUtils.i("idStore"+idStore+"/idCategory"+idCategory+"/idCompany"+idCompany+"/commodityname"+commodityname+"/currentPage"+currentPage+"/price"+price);
	}
	
	
	/**
	 * 产地列表
	 */
	void getAreaList() {
		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
//		if(whitchActivity==0){//一级分类跳转
//		params.addQueryStringParameter("idCategory", idCategory);
		params.addQueryStringParameter("commodityName", commodityname);
//		}else if(whitchActivity==1){//搜索跳转
//			if(whitchOne==1){
//				params.addQueryStringParameter("idCategory", idCategory);
//			}
//		}
		
		doHttp(0, MyConst.commodityOriginAction, params, commodityOrigin, false);
	}

	/**
	 * 规格列表
	 */
	void getSizeList() {
		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		doHttp(0, MyConst.commodityNormsAction, params, commodityNorms, false);
	}

	/**
	 * 添加至购物车
	 */
	void addShoppCar(String idCommodity, String idLevel) {
		RequestParams params = new RequestParams();
//		params.addBodyParameter("idUser",
//				String.valueOf(MyApplication.getInstance().getId Number()));
		params.addQueryStringParameter("idCommodity", idCommodity);
		params.addQueryStringParameter("idLevel", idLevel);
		params.addQueryStringParameter("buynumber", "" + 1);
		doHttp(1, MyConst.addShoppCartAction, params, addShoppCar);
	}
	
	/** 获取分类名字*/
	void getTypename() {
		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		doHttp(0, MyConst.allCategoryAction, params, typeName, false);
	}
	
	/**分类查询
	 * http://121.46.0.219:8080/efreshapp/categoryListAction?idCategory=1001&currentPage=1*/
//	void categoryListHttp(int currentPage){
//		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idCategory",idCategory);
//		params.addQueryStringParameter("currentPage",currentPage+"");
//		doHttp(0, MyConst.categoryListAction, params, categoryListAction,false);
//		
//	}

	/**一级分类查询
	http://121.46.0.219:8080/efreshapp/commiditySortAction?idCategory=1001*/
	void commiditySortHttp(){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("idCategory",idCategory);
		doHttp(0, MyConst.commiditySortAction, params, commiditySortAction,false);
	}

	public void cleanAll(){
		currentPage=1;
		isAll=false;
	}
	/**
	 * 网络请求统一处理
	 */
	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		switch (flag) {
		case commodityOrigin:// 产地
			ArrayList<AreaBean> list = new ArrayList<AreaBean>();
			list = new Gson().fromJson(jsonString,
					new TypeToken<List<AreaBean>>() {
					}.getType());

			for (int i = 0; i < list.size(); i++) {
				area.add(list.get(i).getAddress());
				idStores.add(list.get(i).getIdNumber());
			}
			areaadapter.notifyDataSetChanged();
			break;
		case commodityNorms:// 规格
			ArrayList<SizeBean> list1 = new ArrayList<SizeBean>();
			list1 = new Gson().fromJson(jsonString,
					new TypeToken<List<SizeBean>>() {
					}.getType());

			for (int i = 0; i < list1.size(); i++) {
				size.add(list1.get(i).getCompanyname());
				idCompanys.add(list1.get(i).getIdNumber());
			}
					
			sizeadapter.notifyDataSetChanged();
			break;
		case searchCommodity:// 商品搜索(所有商品)
				
			ArrayList<ProductBean> list2 = new ArrayList<ProductBean>();
			list2 = new Gson().fromJson(jsonString,
					new TypeToken<List<ProductBean>>() {
					}.getType());

			if(list2.size()==0){
				isAll=true;
				if(currentPage==1){
					showToast("没有搜索到相关商品信息");
				}else{
					showToast("加载完毕");
				}
			}else{
				//判断是否是加载下一页，不是就清除原来的数据
				if(isClean){
					shopBeans.clear();
					isClean=!isClean;
				}   
			}
			for (int i = 0; i < list2.size(); i++) {
				shopBeans.add(list2.get(i));
			}

			adapter.notifyDataSetChanged();
			break;

		case addShoppCar:
			showToast("添加至购物车成功");
			MyApplication.getInstance().getShopCarCommodities().add(new ShopCarsBean(Integer.parseInt(idCommodity), Integer.parseInt(idLevel)));
		
			adapter.notifyDataSetChanged();
			
			Intent intent = new Intent();
			intent.setAction(PurchaserCarFragment.update_car);
			ShowTypeActivity.this.sendBroadcast(intent);
			Intent intent1 = new Intent();
			intent1.setAction(PurchaserHomeFragment.update_list);
			ShowTypeActivity.this.sendBroadcast(intent1);
			break;
			
		case typeName:
			type.clear();
			ArrayList<BaseBean> list4 =  new Gson().fromJson(jsonString, new TypeToken<List<BaseBean>>() {
			}.getType());
			type.add(0, "全部");
			idNumbers.add(0, "");
			for(int i=0;i<list4.size();i++){
				type.add(list4.get(i).getCategoryname());//添加种类名称集合
				idNumbers.add(list4.get(i).getIdNumber());
//				type.add(list4.get(i));
			}
//			idCategory = type.get(0).getIdNumber();
			
//			commiditySortHttp();
			typeadapter.notifyDataSetChanged();
			break;

		case commiditySortAction:
			ArrayList<ProductDetailBean> list5 = new ArrayList<ProductDetailBean>();
			list5 = new Gson().fromJson(jsonString,
					new TypeToken<List<ProductDetailBean>>() {
					}.getType());

			for (int i = 0; i < list5.size(); i++) {
				list7.add(list5.get(i));
			}
			
			
//			listAdapter1.notifyDataSetChanged();
			break;
			
			
		default:
			break;
		}
	}
	

	@OnClick({ R.id.ly_back, R.id.rl_search,
			R.id.activity_show_type_area, R.id.activity_show_type_price,
			R.id.activity_show_type_view, R.id.activity_show_type_type,
			R.id.activity_show_type_size })
	public void back(View view) {
		Intent intent;
		switch (view.getId()) {
//		case R.id.ly_back:
//			finish();
//			break;
//		case R.id.tv_back:
//			// Toast.makeText(this, "tv_back", Toast.LENGTH_SHORT).show();
//			finish();
//			break;
		case R.id.ly_back:
//			// Toast.makeText(this, "tv_back", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case R.id.rl_search:
			intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.activity_show_type_area:// 地区
			cleanAll();
			closeaLLListview();
			if(area.size()==0){
				area.add(0, "全部");
				idStores.add(0," ");
				getAreaList();
				showListview(lvarea);// 打开一级目录
			}
			else {
				showListview(lvarea);// 打开一级目录
			}
			break;
		case R.id.activity_show_type_price:// 价格
			isClean=true; 
			cleanAll();
			closeaLLListview();
			if (isAsc) {
				price = "desc";
				getproductList();
				isAsc = false;
				ivprice.setImageResource(R.drawable.sort_up);
			} else {
				price = "asc";
				getproductList();
				isAsc = true;
				ivprice.setImageResource(R.drawable.sort_down);
			}

			break;
		case R.id.activity_show_type_type:// 种类
			cleanAll();
			closeaLLListview();
			if(type.size()==0){
				getTypename();
				showListview(lvtype);// 打开一级目录
			}
			else {
				showListview(lvtype);// 打开一级目录
			}
//			if(list6.size()==0){
//				getTypename();
//				showsListview();// 打开二级目录
//			}
//			else {
//				showsListview();// 打开二级目录
//			}
			break;
		case R.id.activity_show_type_size:// 规格
			cleanAll();
			closeaLLListview();
			if(size.size()==0){
				size.add(0, "全部");
				idCompanys.add(0," ");
				getSizeList();
				showListview(lvsize);// 打开一级目录
			}
			else {
				showListview(lvsize);// 打开一级目录
			}
			break;
		case R.id.activity_show_type_view:
			// 关闭所有listview
			closeaLLListview();
			break;

		default:
			break;
		}
	}

	private void closeaLLListview() {
		ll.setVisibility(View.GONE);
//		sll.setVisibility(View.GONE);
		mv.setVisibility(View.GONE);
	}


	private void showsListview() {
//		sll.setVisibility(View.VISIBLE);
		mv.setVisibility(View.VISIBLE);
	}

	private void showListview(ListView lv) {
		ll.setVisibility(View.VISIBLE);
		lvarea.setVisibility(View.GONE);
		lvtype.setVisibility(View.GONE);
		lvprice.setVisibility(View.GONE);
		lvsize.setVisibility(View.GONE);
		lv.setVisibility(View.VISIBLE);
		mv.setVisibility(View.VISIBLE);
	}

	
	
	
	
	//--------------------------------------------gridView初始化
	void gridViewInit(){
		//商品显示
		adapter=new TypeTwoAdapter(shopBeans, ShowTypeActivity.this,width);
		gridView.setAdapter(adapter);
		adapter.setOnAddCarItemClickListener(new OnAddCarItemClickListener() {
			
			@Override
			public void addCarItemClick(View v, int position) {
				if(!"未标价".equals(shopBeans.get(position).getPrice()) && Integer.parseInt(shopBeans.get(position).getStock())>0){
					idCommodity=shopBeans.get(position).getIdNumber();
					idLevel=shopBeans.get(position).getIdLevel();
					addShoppCar(idCommodity, idLevel);
				}else{
					if("未标价".equals(shopBeans.get(position).getPrice())){
						showToast("未标价的商品无法购买哦！");
					}else{
						showToast("商品库存不足！");
					}
				}
			}
		});

		//滚动事件
		gridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 判断是否滚动到底部
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						// 加载更多功能的代码
						if(!isAll){
							currentPage++;
							isClean=false;
							getproductList();
							LogUtils.i("//////////////////////"+currentPage);
						}
					}
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		
		//点击事件
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(ShowTypeActivity.this,ProductDetailActivity.class);
				intent.putExtra("idCommodity", shopBeans.get(arg2).getIdNumber());
				intent.putExtra("idLevel", shopBeans.get(arg2).getIdLevel());
				intent.putExtra("imageUrl", shopBeans.get(arg2).getImagename());
				startActivity(intent);
			}
		});
		
		
	}
	             
	
	//--------------------------------------------种类初始化
	void commiditySortInit(){
//		listAdapter = new OneTypeListAdapter(this, list6);
		if (typeadapter == null) {
			typeadapter = new AreaAdapter(this, type,idNumbers);
			lvtype.setAdapter(typeadapter);
		}
		
		typeadapter
				.setOnAreaItemClickListener(new onAreaItemClickListener() {

					@Override
					public void onAreaItemClick(View v, int position,
							String str) {
						// TODO Auto-generated method stub
						// 改变颜色并且设置二级Adapter
						if(position==0){
							whitchOne=-1;
							idCategory=0+"";
						}else{
							whitchOne=0;
							idCategory = str;
							
						}
						cleanAll();
						typeadapter.changeSelected(position);
						shopBeans.clear();
						closeaLLListview();// 关闭二级目录
						getproductList();
					}
				});
	
//		activity_show_type_flv.setAdapter(listAdapter);
//		activity_show_type_flv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				
//				list7.clear();
//				listAdapter.changeSelected(arg2);
//				idCategory = list6.get(arg2).getIdNumber();
//				commiditySortHttp();
//			}
//		});
//		listAdapter1 = new TwoTypeListAdapter(this, list7);
//		activity_show_type_slv.setAdapter(listAdapter1);
//		activity_show_type_slv.setOnItemClickListener(new OnItemClickListener() {
//			
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				whitchOne=1;
//				cleanAll();
//				listAdapter1.changeSelected(arg2);
//				commodityname=list7.get(arg2).getCommodityName();
//				shopBeans.clear();
//				closeaLLListview();
//				getproductList();
//				
//			}
//		});
	}
	
	//--------------------------------------------产地初始化
	void areaInit(){
		if (areaadapter == null) {
			areaadapter = new AreaAdapter(this, area, idStores);
			lvarea.setAdapter(areaadapter);
		}
		
		areaadapter
				.setOnAreaItemClickListener(new onAreaItemClickListener() {

					@Override
					public void onAreaItemClick(View v, int position,
							String str) {
						// TODO Auto-generated method stub
						// 改变颜色并且设置二级Adapter
						if(position==0){
							whitchOne=-1;
							idStore=0+"";
						}else{
							whitchOne=0;
							idStore = str;
						}
						cleanAll();
						areaadapter.changeSelected(position);
						shopBeans.clear();
						closeaLLListview();// 关闭二级目录
						getproductList();
					}
				});
	}
	
	
	//--------------------------------------------规格初始化
	void sizeInit(){
		if (sizeadapter == null) {
			sizeadapter = new AreaAdapter(this, size, idCompanys);
			lvsize.setAdapter(sizeadapter);
		}
		
		sizeadapter.setOnAreaItemClickListener(new onAreaItemClickListener() {

			@Override
			public void onAreaItemClick(View v, int position,
					String str) {
				// TODO Auto-generated method stub
				// 改变颜色并且设置二级Adapter
				if(position==0){
					whitchOne=-1;
					idCompany=0+"";
				}else{
					whitchOne=2;
					idCompany = str;
				}
				cleanAll();
				sizeadapter.changeSelected(position);
				shopBeans.clear();
				closeaLLListview();// 关闭二级目录
				getproductList();

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (ll.getVisibility() == View.VISIBLE
//					|| sll.getVisibility() == View.VISIBLE
					|| mv.getVisibility() == View.VISIBLE) {
				closeaLLListview();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
