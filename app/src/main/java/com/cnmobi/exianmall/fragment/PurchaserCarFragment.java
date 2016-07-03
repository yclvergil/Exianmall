package com.cnmobi.exianmall.fragment;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.ShopCarAdapter;
import com.cnmobi.exianmall.adapter.ShopCarAdapter.onCalendarPriceClickListener;
import com.cnmobi.exianmall.base.BaseFragment;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.Commodity;
import com.cnmobi.exianmall.bean.CommodityPrice;
import com.cnmobi.exianmall.bean.ConfirmOrderProductBean;
import com.cnmobi.exianmall.bean.ShopCarBeanGroup;
import com.cnmobi.exianmall.bean.ShopCarsBean;
import com.cnmobi.exianmall.home.activity.CaptureActivity;
import com.cnmobi.exianmall.interfaces.PriceAllItemClickListener;
import com.cnmobi.exianmall.interfaces.PriceItemClickListener;
import com.cnmobi.exianmall.type.activity.SendOutDateActivity;
import com.cnmobi.exianmall.type.activity.SingleOrderActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 购物车界面
 */
public class PurchaserCarFragment extends BaseFragment {

	@ViewInject(R.id.lv_carlist)
	ListView listView;
	@ViewInject(R.id.checkbox)
	CheckBox checkbox;
	@ViewInject(R.id.tv_total)
	TextView tv_total;
	@ViewInject(R.id.ly_bottom)
	RelativeLayout ly_bottom;
	double totalprice = 0;// 总价
	private List<ShopCarBeanGroup> grouplist;
//	private List<ShoppCartCommodity> childsList = new ArrayList<ShoppCartCommodity>();
	private List<ShopCarsBean> childsList ;
	private List<Commodity> commodityList;
	private ShopCarsBean ShopCarsBean;
	private ShopCarBeanGroup beanGroup;
	private ShopCarAdapter adapter;
	private int position;
	private String buyDate;
	private int hour=0;
	private boolean isShowCarNull=true;
	/**
	 * 购物车接口标识
	 */
	public static final int shoppcar = 1;
	/** 价格日历 */
	public static final int calendarPrice = 2;
	private View view;

	private String result_value;// 返回的日期时间

	MyBroadcast mybroadcast;
	public static String update_car = "update_car";
	MyBroadcast1 mybroadcast1;
	public static String gone_ly_bottom = "gone_ly_bottom";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_purchaser_car, container, false);
		ViewUtils.inject(this, view);
		shoppCarHttp();

		mybroadcast = new MyBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(update_car);
		getActivity().registerReceiver(mybroadcast, filter); // 注册BroadcastReceiver
								
		mybroadcast1 = new MyBroadcast1();
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction(gone_ly_bottom);
		getActivity().registerReceiver(mybroadcast1, filter1); // 注册BroadcastReceiver
		return view;
	}

	public class MyBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到广播，更新
			shoppCarHttp();
			LogUtils.i("-------更新购物车");
			isShowCarNull=false;
		}
	}
	
	public class MyBroadcast1 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到广播，购物车已全部删除，隐藏底部view ly_bottom
			ly_bottom.setVisibility(View.GONE);
			
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mybroadcast);// 取消注册
		getActivity().unregisterReceiver(mybroadcast1);// 取消注册
	}

	@OnClick({ R.id.iv_home_left_top, R.id.checkbox, R.id.btn_balance_fragment_purchaser,
			R.id.iv_common_head_3_details, R.id.iv_common_head_3_details, R.id.ly_sendouttime_urgent })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_home_left_top:
			break;
		case R.id.checkbox:// 全选
			double price = 0;
			boolean boxischeck = checkbox.isChecked();
			for (int i = 0; i < grouplist.size(); i++) {
				ShopCarBeanGroup group = grouplist.get(i);
				group.setIstrue(boxischeck);
				List<ShopCarsBean> childs = group.getShoppCartCommodity();
				for (int j = 0; j < childs.size(); j++) {
					ShopCarsBean child = childs.get(j);
					child.setIstrue(boxischeck);
					price += child.getPrice() * child.getBuynumber();
				}
			}
			adapter.notifyDataSetChanged();
			if (boxischeck) {
				totalprice = price;
			} else {
				totalprice = 0;// 总价清0
			}
			tv_total.setText("¥" + String.format("%.2f", totalprice));
			break;

		case R.id.btn_balance_fragment_purchaser:// 结算
			hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if(isCanSubmitOrder()){
				if(14<=hour && hour<=23){
					//加急单 下午14点   至0点    14点到20点(80元一单)  20到0点(150元)  按正常到货时间（所购商品不能保证数量）
					if(isChoseUrgency()){
						//选了紧急到货时间，配送费按紧急到货时间规则算
						toOrder(0);
					}else{
						//未选紧急到货时间，配送费按加急订单规则算
						LayoutInflater inflaterDl = LayoutInflater.from(getActivity().getApplicationContext());
						RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
								.inflate(R.layout.dialog_clean_message, null);
						Builder builder1 = new AlertDialog.Builder(getActivity());
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
								}else{
									sendPrice=0;
								}
								toOrder(sendPrice);
							}
						});
				}
					
				}else if(10<=hour && hour<14){
					//正常下单：上午10点   至下午14点 
					toOrder(0);
				}else if(0<=hour && hour<10){
					//不可下单：00点到上午10点
					showToast("0点-上午10点为系统维护时间，不能购买商品！");	
				}
			}else{
				showToast("请等待工作人员审核通过后才能购买");
			}
			break;
		case R.id.iv_common_head_3_details:// 二维码扫描
			
			intent = new Intent(getActivity(), CaptureActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	void toOrder(double sendPrice){
		boolean isArrivalTime=true;
		List<ConfirmOrderProductBean> orderProductList = new ArrayList<ConfirmOrderProductBean>();
		// 这里把结算的订单带过去就好。
		for (int i = 0; i < grouplist.size(); i++) {
			ShopCarBeanGroup group = grouplist.get(i);
			String address = group.getStoreAddress();
			int idStore = group.getIdStore();
			List<ShopCarsBean> childs = group.getShoppCartCommodity();
			for (int j = 0; j < childs.size(); j++) {
				ShopCarsBean child = childs.get(j);
				if (child.isIstrue()) {
					LogUtils.i("到货日期"+child.getArrivalTime());      
					if (child.getArrivalTime()==null) {
						isArrivalTime=false;
					}
					// 被选中就把该项信息添加到订单
					ConfirmOrderProductBean bean = new ConfirmOrderProductBean();// 一个订单对象
					bean.setAdress("  e鲜商城  ");
					bean.setIdStore(child.getIdStore());
					bean.setBuyNum(child.getBuynumber());
					bean.setImageUrl(child.getImagename());
					bean.setArrivaltime(child.getArrivalTime());
					bean.setIdShopCart(child.getIdShoppCart()+"");
					bean.setBuyDate(buyDate);
//					 bean.setArrivaltime("2016-3-16 11:42:58");
					bean.setLevel(child.getLevelname());
					bean.setIdLevel(child.getIdLevel());
					bean.setName(child.getCommodityname());
					bean.setCompanyName(child.getCompanyname());
					bean.setStock(child.getStock());
					bean.setSumBuyNum(child.getSumBuyNumber());
					bean.setAdressId(idStore + "");
					bean.setIdCommodity(child.getIdCommodity());
					bean.setPrice(child.getPrice() + "");
					if(!TextUtils.isEmpty(child.getSendPrice()+"")&&child.getSendPrice()!=0 ){
						bean.setSendPrice(child.getSendPrice());
					}else{
						bean.setSendPrice(sendPrice);
					}
					orderProductList.add(bean);
				}
			}
		}
		if(!isArrivalTime){
			showToast("您未选择紧急到货日期，商品将会以正常速度送达");
		}
		if (orderProductList.size() == 0) {
			Toast.makeText(getActivity(), "你还没有选择商品哦！", Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent(getActivity(), SingleOrderActivity.class);
			intent.putExtra("orderProductList", (Serializable) orderProductList);
			startActivity(intent);
			
		}
	}
	
	boolean isChoseUrgency(){
		boolean result=false;
		for (int i = 0; i < grouplist.size(); i++) {
			ShopCarBeanGroup group = grouplist.get(i);
			List<ShopCarsBean> childs = group.getShoppCartCommodity();
			for (int j = 0; j < childs.size(); j++) {
				if(!TextUtils.isEmpty(childs.get(j).getSendPrice()+"")&&childs.get(j).getSendPrice()!=0 ){
					result= true;
					break;
				}
			
			}
		}
		return result;
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		result_value = data.getStringExtra("time");
		if (requestCode == 103 && resultCode == 101) {// 到货价格变动
//			2013-3-23-2013-03-23
//			这里拿到的日期格式不符，需转换
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try {
				date=format.parse(result_value);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			buyDate=format.format(date);
			calendarPriceHttp(commodityList,buyDate);
		}
	}

	// 购物车显示接口
	private void shoppCarHttp() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
//		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		doHttp(1, MyConst.shoppCartAction, params, shoppcar);
	}

	// 价格日历接口
	// http://121.46.0.219:8080/efreshapp/calendarPriceAction?idCommodity=1000&idLevel=1000&buyDate=2016-01-08
	void calendarPriceHttp(List<Commodity> list,String time) {
		RequestParams params = new RequestParams();
		Gson gson=new Gson();
		params.addBodyParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
//		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addBodyParameter("calendarPrice", gson.toJson(list));
		LogUtils.i("time"+time);
		params.addBodyParameter("buyDate", time);
		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		doHttp(1, MyConst.calendarPriceAction, params, calendarPrice);
	}

	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		Gson gson;
		switch (flag) {
		case calendarPrice:
			LogUtils.i(jsonString);
			gson = new Gson();
			
//			commodity = new ShoppCartCommodity();
//			commodity.setPrice(Double.valueOf(jsonString));
			List<CommodityPrice> list;
			list=gson.fromJson(jsonString,new TypeToken<List<CommodityPrice>>() {}.getType());
			// childsList.get(position).setPrice(Double.valueOf(jsonString+"(价格日历："+result_value+")"));
//			childsList.get(position).setPrice(Double.valueOf(jsonString));
			String idCommodity="";
			
			LogUtils.i("list:"+list.size()+"childsList:"+childsList.size());
			//如果返回不为空或者返回的商品集长度与购物车商品集长度一样
			if(list!=null&&list.size()!=0&&childsList.size()==list.size()){
			
				grouplist.get(position).setBuyDate(buyDate);
				showToast("价格已刷新，请注意选择日期的价格变化！");
				for(int i=0;i<childsList.size();i++){
//					for(int j=0;j<list.size();j++){
//						idCommodity=childsList.get(i).getIdCommodity()+"";
//						if(idCommodity.equals(list.get(j).getIdCommodity())){
							childsList.get(i).setPrice(list.get(i).getPrice());
//						}
//					}
				}
				adapter.notifyDataSetChanged();
			}else{
				showToast("没有当天的商品信息！");
			}
			break;
		case shoppcar:
			gson = new Gson();
			beanGroup=new ShopCarBeanGroup();
			grouplist=new ArrayList<ShopCarBeanGroup>();
//			grouplist = gson.fromJson(jsonString,
//					new TypeToken<List<ShopCarBeanGroup>>() {
//					}.getType());
			childsList = new ArrayList<ShopCarsBean>();
			childsList = gson.fromJson(jsonString,
					new TypeToken<List<ShopCarsBean>>() {
					}.getType());
//			if(){
//				
//			}
			beanGroup.setShoppCartCommodity(childsList);
			LogUtils.i("childsList:"+gson.toJson(childsList));
			
			grouplist.add(beanGroup);
//			LogUtils.e("购物车："+jsonString);
//			LogUtils.e("购物车商品数量："+grouplist.get(0).getShoppCartCommodity().size());
//			if(adapter!=null){
//			 grouplist=new ArrayList<ShopCarBeanGroup>();
//			grouplist = gson.fromJson(jsonString, new TypeToken<List<ShopCarBeanGroup>>() {
//			}.getType());
			if(childsList.size()!=0){
				ly_bottom.setVisibility(View.VISIBLE);
			}else{
				listView.setVisibility(View.GONE);
				if(isShowCarNull){
					showToast("您的购物车还是空空的，赶紧去购买商品吧~");
				}
				ly_bottom.setVisibility(View.GONE);
				
			}
			if (adapter != null) {
				adapter.setListGroup(grouplist);
				adapter.notifyDataSetChanged();
				checkbox.setChecked(false);// 设置为不选中并且清空价格
				totalprice = 0;
				tv_total.setText("¥" + 	String.format("%.2f", totalprice));
			
			} else {
				adapter = new ShopCarAdapter(grouplist, getActivity());
				listView.setAdapter(adapter);

				adapter.setOnCalendarPriceClickListener(new onCalendarPriceClickListener() {
					@Override
					public void onCalendarPriceClick(View v, int position1) {
						// TODO Auto-generated method stub
						position=position1;
						childsList = grouplist.get(position1).getShoppCartCommodity();
						Commodity bean;
						commodityList=new ArrayList<Commodity>();
						for(int i=0;i<childsList.size();i++){
							bean=new Commodity();
							bean.setIdCommodity(childsList.get(i).getIdCommodity());
							bean.setIdLevel(childsList.get(i).getIdLevel());
							commodityList.add(bean);
						}
//						idCommodity = childsList.get(position2).getIdCommodity() + "";
//						idLevel = childsList.get(position2).getIdLevel() + "";
//						position = position2;
						Intent intent = new Intent(getActivity(), SendOutDateActivity.class);
//						LogUtils.i(position + "////" + position2);
						getActivity().startActivityForResult(intent, 103);
					}
				});

				adapter.setOnPriceItemClickListener(new PriceItemClickListener() {
					@Override
					public void toTalPriceClick(boolean ischoose, double price, int num) {
						if (ischoose) {
							totalprice += price * num;
						} else {
							totalprice -= price * num;
						}
						tv_total.setText("¥" + String.format("%.2f", totalprice));
					}

					@Override
					public void toTalPriceClick(boolean isadd, double price) {
						if (isadd) {
							totalprice += price;
						} else {
							totalprice -= price;
						}
						tv_total.setText("¥" + String.format("%.2f", totalprice));
					}
				});
				adapter.setOnPriceItemAllClickListener(new PriceAllItemClickListener() {
					@Override
					public void toTalPriceClick(double price) {
						totalprice += price;
						tv_total.setText("¥" + String.format("%.2f", totalprice));
					}
				});
			}
			break;
		default:
			break;
	}
	}
	
	/**
	 * 判断下买家信息是否完善，不完善不能购买商品    //改，只判断是否通过审核
	 * @return
	 */
	public static boolean isCanSubmitOrder(){
		if ("1".equals(MyApplication.getInstance().getExamine())
//				&& !"".equals(MyApplication.getInstance().getDeliveryaddress())
//				&& !"".equals(MyApplication.getInstance().getStallsname())
//				&& !"".equals(MyApplication.getInstance().getTlr_name())
//				&& !"".equals(MyApplication.getInstance().getPhone())
				) 
		{
			return true;
		}else{
			return false;
		}
		
	}
}
