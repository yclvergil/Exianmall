package com.cnmobi.exianmall.type.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.pay.demo.ExternalPartner;
import com.alipay.sdk.pay.demo.PayResult;
import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.ConfirmOrderAdapter;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.ConfirmOrderBean;
import com.cnmobi.exianmall.bean.ConfirmOrderProductBean;
import com.cnmobi.exianmall.bean.OrderIdBean;
import com.cnmobi.exianmall.bean.PayOrderInfo;
import com.cnmobi.exianmall.bean.PayWayBean;
import com.cnmobi.exianmall.bean.SubimtOrderInfos;
import com.cnmobi.exianmall.fragment.PurchaserCarFragment;
import com.cnmobi.exianmall.fragment.PurchaserHomeFragment;
import com.cnmobi.exianmall.mine.activity.PreChargeActivity;
import com.cnmobi.exianmall.mineset.activity.KeyboardEnum;
import com.cnmobi.exianmall.mineset.activity.KeyboardEnum.ActionEnum;
import com.cnmobi.exianmall.utils.ConstontUtils;
import com.cnmobi.exianmall.wxapi.PayActivity;
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

/**
 * 提交单个订单页面
 * 
 */
public class SingleOrderActivity extends BaseActivity {

	@ViewInject(R.id.rl_zhifubao)
	RelativeLayout rl_zhifubao;
	@ViewInject(R.id.rl_weixin)
	RelativeLayout rl_weixin;
	@ViewInject(R.id.radio_weixin)
	RadioButton radio_weixin;
	@ViewInject(R.id.radio_zhifubao)
	RadioButton radio_zhifubao;
	@ViewInject(R.id.radio_platform_send)
	RadioButton radio_platform_send;
	@ViewInject(R.id.radio_thrid_send)
	RadioButton radio_thrid_send;
	@ViewInject(R.id.check_balance)
	CheckBox check_balance;
	@ViewInject(R.id.tv_name_and_phone)
	TextView tv_name_and_phone;
	@ViewInject(R.id.tv_detail_adress)
	TextView tv_detail_adress;
	@ViewInject(R.id.tv_balance)
	TextView tv_balance;
	@ViewInject(R.id.tv_products_price)
	TextView tv_products_price;
	@ViewInject(R.id.tv_send_price)
	TextView tv_send_price;
	@ViewInject(R.id.tv_money)
	TextView tv_money;
	@ViewInject(R.id.lv_confirmorder)
	ListView lv_confirmorder;
	@ViewInject(R.id.btn_submitorder)
	Button btn_submitorder;
	@ViewInject(R.id.ly_urgent)
	LinearLayout ly_urgent;
	@ViewInject(R.id.tv_date)
	TextView tv_date;
	@ViewInject(R.id.tv_expected_time)
	TextView tv_expected_time;
	@ViewInject(R.id.et_message)
	EditText et_message;
	private static final int SUBMIT_ORDER_REQUESTCODE = 1;
	private ConfirmOrderAdapter adapter;
	private List<PayWayBean> payWayList = new ArrayList<PayWayBean>();
//	private List<OrderInfoBean> orderInfoList = new ArrayList<OrderInfoBean>();
	private List<SubimtOrderInfos> orderInfoList = new ArrayList<SubimtOrderInfos>();
//	private List<OrderDetailedBean> OrderDetaileList = new ArrayList<OrderDetailedBean>();
	private List<Integer>idstoreList=new ArrayList<Integer>();
	private ConfirmOrderBean confirmOrderBean;
	private List<ConfirmOrderProductBean> orderProductList;
	private List<OrderIdBean> orderIdList; // 订单编号
	private double sendPrice = 0; // 配送费
	private double sendPrice1=0;
	private double totalMoney = 0;
	String paypassword = "";// 输入的密码
	private String orderid = "";// 订单号
	private String message="";//订单留言
	
	private Gson gson=new Gson();
	String arrivaltime="";
	String creationordertimes="";
	String isurgent="N";       
	/**
	 * 确认订单接口标识
	 */
	public static final int confirmOrder = 0;
	/**
	 * 提交订单接口标识
	 */
	public static final int submitOrder = 1;
	/**
	 * 支付成功后修改订单的状态接口标识
	 */
	public static final int paymentOrder = 2;
	
	/**
	 * 支付成功后修改订单的状态接口标识
	 */
	public static final int passwordValidation = 3;

	public static final int queryProfile=4;
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	int radioFlag = 0;// 选择的支付方式，1为支付宝，2为微信，3为余额
	int showRadio=0; //1支付宝，2为银联
	int oldChooseRadio=0; //记录选择的支付方式1支付宝，2为微信
	PayWordPopupWindows payWordPopupWindows;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_single_order);
		ViewUtils.inject(this);
		setTitleText("提交订单");
		queryProfileHttp();
		initData();
		init();
		setRadio();
//		confirmOrder();  
	}

	@OnClick({ R.id.btn_submitorder,R.id.rl_coupon })
	void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submitorder:
			LogUtils.i("传来的配送费是"+sendPrice);
			int hour=0;
			hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			message=getStr(et_message);
			if(14<=hour && hour<=23){
				//加急单 下午14点   至0点    14点到20点(80元一单)  20到0点(150元)  按正常到货时间（所购商品不能保证数量）
				message=message+"(所购商品不能保证数量)";
				if(14<=hour && hour<20){
					sendPrice1=80;
				}else if(20<=hour && hour<0){
					sendPrice1=150;
				}
				if(sendPrice1!=sendPrice && sendPrice1>sendPrice&& isurgent!="Y"){
					//根据配送费是否一样来判断购买和下单的时间区分，弹框提示费用增加
					LayoutInflater inflaterDl = LayoutInflater.from(getApplicationContext());
					RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
							.inflate(R.layout.dialog_clean_message, null);
					Builder builder1 = new AlertDialog.Builder(SingleOrderActivity.this);
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
							sendPrice=sendPrice1;
							tv_send_price.setText("¥" +String.format("%.2f", sendPrice) );
							dialog1.dismiss();
							toSubmitOrder(message);
						}
					});
				}else{
					toSubmitOrder(message);
				}
				
			}else if(10<=hour && hour<14){
				//正常下单：上午10点   至下午14点 
				toSubmitOrder(message);
			}else if(0<=hour && hour<10){
				//不可下单：00点到上午10点
				showToast("0点-上午10点为系统维护时间，不能购买商品！");	
			}
			LogUtils.i("实际配送费是"+sendPrice);
//			if (radioFlag == 1) {
////				submitOrder(orderInfoList);
//				submitOrder(orderInfoList, arrivaltime, creationordertimes, isurgent, totalMoney,message);
//			} else if (radioFlag == 2) {
////				Intent intent = new Intent(SingleOrderActivity.this, PayBanActivity.class);
////				startActivity(intent);
//				submitOrder(orderInfoList, arrivaltime, creationordertimes, isurgent, totalMoney,message);
//			} else if(radioFlag == 3){
//				if(!"Y".equals(MyApplication.getInstance().getIsSetpassword())){
//					showToast("请先设置您的支付密码才能使用余额支付");
//				}else if (!(totalMoney >Double.valueOf(MyApplication.getInstance().getBalance()))) {
//					submitOrder(orderInfoList, arrivaltime, creationordertimes, isurgent, totalMoney,message);
//				}else{
//					showToast("账户余额不足，请前往充值或选择其它付款方式!");
//				}
//			} else {
//				showToast("请选择支付方式");
//			}
			
			break;

		default:
			break;
		}
	}

	void init() {
		
		
		if(isurgent=="N"){
//			holder.tv_date.setVisibility(View.GONE);
			ly_urgent.setVisibility(View.GONE);
		}else{
			ly_urgent.setVisibility(View.VISIBLE);
//			holder.tv_date.setVisibility(View.VISIBLE);
			tv_date.setText(arrivaltime.substring(0, arrivaltime.length()-3));
		}
		check_balance.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					radioFlag=3;
					rl_weixin.setVisibility(View.GONE);
					rl_zhifubao.setVisibility(View.GONE);
				}else{
					rl_zhifubao.setVisibility(View.VISIBLE);
					rl_weixin.setVisibility(View.VISIBLE);
					if(oldChooseRadio==1){
						radioFlag=1;
					}else if(oldChooseRadio==2){
						radioFlag=2;
					}
					}
				
			}
		});
		
		if (orderProductList != null) {

			adapter = new ConfirmOrderAdapter(orderProductList, SingleOrderActivity.this);
			lv_confirmorder.setAdapter(adapter);
			lv_confirmorder.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(SingleOrderActivity.this, ProductDetailActivity.class);
					intent.putExtra("idCommodity", orderProductList.get(position).getIdCommodity()+"");
					intent.putExtra("idLevel", orderProductList.get(position).getIdLevel()+"");
					LogUtils.e("idCommodity:"+orderProductList.get(position).getIdCommodity()+""+";idLevel"+orderProductList.get(position).getIdLevel());
					startActivity(intent);
				}
			});
			double productsPrice = 0;
			for (int i = 0; i < orderProductList.size(); i++) {
				float price = Float.valueOf(orderProductList.get(i).getPrice());
				int count = orderProductList.get(i).getBuyNum();
				productsPrice += price * count;
			}
			sendPrice = orderProductList.get(0).getSendPrice();
			totalMoney = productsPrice + sendPrice;
			tv_products_price.setText("¥" + String.format("%.2f", productsPrice));
			tv_send_price.setText("¥" +String.format("%.2f", sendPrice) );
			tv_money.setText("¥" +String.format("%.2f", totalMoney));
		}
		et_message.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(getStr(et_message).length()>50){
					showToastShort("留言内容不能超过50字！");
				}
			}
		});
	}

//	买家收货地址查询
//	地址：http://121.46.0.219:8080/efreshapp/queryProfileAction?idUser=1000
	void queryProfileHttp(){
		RequestParams params=new RequestParams();
		params.addBodyParameter("idUser",MyApplication.getInstance().getIdNumber()+"");
		doHttp(1, MyConst.queryProfileAction, params, queryProfile);
	}
	
	void initData(){
		Date now = new Date();
		SimpleDateFormat creationordertime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		orderProductList = (List<ConfirmOrderProductBean>) getIntent().getSerializableExtra("orderProductList");
		SubimtOrderInfos subimtOrderInfos;
		String timeStr="";
		String buyDate="";
		if(orderProductList.size()!=0){
			
			timeStr=orderProductList.get(0).getArrivaltime();
			buyDate=orderProductList.get(0).getBuyDate();
			LogUtils.e("buyDate"+buyDate);
			for(int i=0;i<orderProductList.size();i++){
				subimtOrderInfos=new SubimtOrderInfos();
				subimtOrderInfos.setBuynumber(orderProductList.get(i).getBuyNum());
				subimtOrderInfos.setBuyprice(Double.valueOf(orderProductList.get(i).getPrice()));
				subimtOrderInfos.setIdCommodity(orderProductList.get(i).getIdCommodity());
				subimtOrderInfos.setIdLevel(orderProductList.get(i).getIdLevel());
				subimtOrderInfos.setIdStore(orderProductList.get(i).getIdStore());
				subimtOrderInfos.setIdShopCart(orderProductList.get(i).getIdShopCart());
				orderInfoList.add(subimtOrderInfos);
			}
		//这里处理下日期格式
		LogUtils.e("timeStr"+timeStr);
		
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tomorrow=format.format(new Date().getTime()+24*60*60*1000);
//		String afterTomorrow=format.format(new Date().getTime()+2*24*60*60*1000);
		Calendar calendar = Calendar.getInstance();
		int hourOfDay=calendar.get(Calendar.HOUR_OF_DAY);
		tv_expected_time.setVisibility(View.VISIBLE);
		tv_expected_time.setText("预计明天("+tomorrow.substring(0, 2)+"月"+tomorrow.substring(3, 5)+"日"+")送达");
		if(TextUtils.isEmpty(timeStr)){
			//不是紧急订单
			isurgent="N";
//			tv_expected_time.setVisibility(View.VISIBLE);
//			tv_expected_time.setText("预计明天("+tomorrow.substring(0, 2)+"月"+tomorrow.substring(3, 5)+"日"+")送达");
			if(!TextUtils.isEmpty(buyDate)){
				//有选择价格日历
//					LogUtils.e("arrivaltime"+arrivaltime);
//					if(format1.format(new Date()).equals(buyDate)){
//						//如果是当天
						arrivaltime=buyDate+" 00:00:00";
//						if(hourOfDay<11){
//							tv_expected_time.setText("11:00前完成下单，预计明天("+tomorrow.substring(0, 2)+"月"+tomorrow.substring(3, 5)+"日"+")送达");
//						}else{
//							tv_expected_time.setText("预计后天("+afterTomorrow.substring(0, 2)+"月"+afterTomorrow.substring(3, 5)+"日"+")送达");
//						}
//					}else{
//						//如果不是当天，预计到货时间加一天
//						try {
////							2016-4-26 13:50:02
//							arrivaltime=format2.format(format2.parse(buyDate+" 00:00:00").getTime()+24*60*60*1000);
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
////						tv_expected_time.setText("预计"+arrivaltime.substring(5, 7)+"月"+arrivaltime.substring(8, 10)+"日"+"送达");
//					}
			}else{
				//没有选择价格日历
				arrivaltime=creationordertime.format(now);
//				if(hourOfDay<11){
//					//04-24
//					tv_expected_time.setText("11:00前完成下单，预计明天("+tomorrow.substring(0, 2)+"月"+tomorrow.substring(3, 5)+"日"+")送达");
//				}else{
//					tv_expected_time.setText("预计后天("+afterTomorrow.substring(0, 2)+"月"+afterTomorrow.substring(3, 5)+"日"+")送达");
//				}
			}
		}else{
			//是紧急订单
			isurgent="Y";
			//时间字符长度长度为15是这种2016-03-30 23:9，需改为2016-03-30 23:09
			if(timeStr.length()==15){
				timeStr=timeStr.substring(0, timeStr.length()-1)+"0"+timeStr.substring(timeStr.length()-1, timeStr.length());
			}
			arrivaltime=timeStr+":00";
		}
		creationordertimes=creationordertime.format(now);
//		OrderInfoBean bean;
////		Date now = new Date();
////		SimpleDateFormat creationordertime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		//这里把传过来的数据重新整理下，符合提交订单参数的结构
//		List<Integer> list = new ArrayList<Integer>();
//		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//		for (int i = 0; i < orderProductList.size(); i++) {
//			list.add(orderProductList.get(i).getIdStore());
//		}
//		
//		for (Integer i:list) {
//			map.put(i, map.get(i) == null ? 1 : map.get(i) + 1);
//		}
//
//	    for(Integer key:map.keySet()){
//	    	bean = new OrderInfoBean();
//	    	List<OrderDetailedBean> OrderDetaileList = new ArrayList<OrderDetailedBean>();
//			for (int i = 0; i < orderProductList.size(); i++) {
//				if(key==orderProductList.get(i).getIdStore()){
//					LogUtils.i("------k:"+key+";--------i:"+i);
//				//这里可以拿到配送费的
//				sendPrice=orderProductList.get(i).getSendPrice();
//				bean.setIdStore(orderProductList.get(i).getIdStore());
//				bean.setIdUser(MyApplication.getInstance().getIdNumber());
//				bean.setIdStore(orderProductList.get(i).getIdStore());
////				bean.setCreationordertime(creationordertime.format(now));
////				String timeStr=orderProductList.get(i).getArrivaltime();
////				//这里处理下日期格式
////				if(timeStr!=null){
////					bean.setIsurgent("Y");
////					//时间字符长度长度为15是这种2016-03-30 23:9，需改为2016-03-30 23:09
////					if(timeStr.length()==15){
////						timeStr=timeStr.substring(0, timeStr.length()-1)+"0"+timeStr.substring(timeStr.length()-1, timeStr.length());
////					}
////					bean.setArrivaltime(timeStr+":00");
////					
////				}else{
////					bean.setIsurgent("N");
////					bean.setArrivaltime(creationordertime.format(now));
////				}
//			 		OrderDetailedBean bean2 = new OrderDetailedBean();
//			 		bean2.setBuynumber(orderProductList.get(i).getBuyNum());
//			 		bean2.setIdShopCart(orderProductList.get(i).getIdShopCart());
//			 		bean2.setBuyprice(Float.valueOf(orderProductList.get(i).getPrice()));
//			 		bean2.setIdCommodity(orderProductList.get(i).getIdCommodity());
//			 		bean2.setIdLevel(orderProductList.get(i).getIdLevel());
////			 		OrderDetaileList.add(bean2);
////			 		bean.setOrderdetailed(OrderDetaileList);
//			 		
//			}
//			}
//			double price=0;
//			for(int i=0;i<OrderDetaileList.size();i++){
//				price+=OrderDetaileList.get(i).getBuyprice()*OrderDetaileList.get(i).getBuynumber();
//			}
////			bean.setOrderprice(Double.valueOf(String.format("%.2f", price)));
//			orderInfoList.add(bean);
//			
//        }
//	    LogUtils.i("orderInfoList:"+gson.toJson(orderInfoList));
	}
	}
	
	
	void confirmOrder() {
//		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
//		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
//		params.addQueryStringParameter("toKen", MyApplication.getInstance().getToKen());
//		HttpUtils httpUtils=new HttpUtils();
////		httpUtils.configCookieStore(MyCookieStore.cookieStore);
////		BaseActivity.httpUtils.send(HttpMethod.GET, MyConst.confirmOrderAction, params,new RequestCallBack<String>() {
//		httpUtils.send(HttpMethod.GET, MyConst.confirmOrderAction, params,new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> arg0) {
//				// TODO Auto-generated method stub
//
//				// TODO Auto-generated method stub
//				try {
//					JSONObject jsonObject = new JSONObject(arg0.result);
//					int errorCode = jsonObject.getInt("code");
//					String messgae=jsonObject.getString("message");
//					if (errorCode == 0) {
//						confirmOrderBean = gson.fromJson(jsonObject.getString("response"), ConfirmOrderBean.class);
//						tv_name_and_phone.setText(confirmOrderBean.getTlr_name() + "  " + confirmOrderBean.getPhone());
//						tv_detail_adress.setText("详细地址： " + confirmOrderBean.getDeliveryaddress());
//						tv_balance.setText("余额： " + confirmOrderBean.getBalance());
						tv_name_and_phone.setText(MyApplication.getInstance().getTlr_name() + "  " + MyApplication.replaceIndex(5, 8, MyApplication.getInstance().getPhone(), "*"));
						tv_detail_adress.setText("详细地址： " + MyApplication.getInstance().getDeliveryaddress());
						
						if(MyApplication.getInstance().getBalance()!=null){
							tv_balance.setText("余额： " + String.format("%.2f", Double.valueOf(MyApplication.getInstance().getBalance())));
						}else{
							tv_balance.setVisibility(View.GONE);
							check_balance.setVisibility(View.GONE);
						}
//						PayWayBean payWayBean;
//						for (int i = 0; i < confirmOrderBean.getPayways().size(); i++) {
//							payWayBean = new PayWayBean();
//							payWayBean = confirmOrderBean.getPayways().get(i);
//							payWayList.add(payWayBean);
//						}
//
//						if (payWayList.size() == 1) {
//							if (payWayList.get(0).getIdPayway().equals("1000")) {
//								// 1000=支付宝
//								rl_yinlian.setVisibility(View.GONE);
//								showRadio=1;
//							} else {
//								rl_zhifubao.setVisibility(View.GONE);
//								showRadio=2;
//							}
//						}
//
//					}else if (errorCode == 3){
//						//用户信息和 
//						showToast(messgae);
//						finish();
//						
//					}else{
//						showToast(messgae);
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			
//			}
//		} );
		
		
		
	}

	void submitOrder(List<SubimtOrderInfos> list,String arrivaltime,String creationordertime,String isurgent,double orderprice,String message ) {
		RequestParams params = new RequestParams();
		LogUtils.e(gson.toJson(list));
		params.addBodyParameter("orderInfo", gson.toJson(list));
		params.addQueryStringParameter("arrivaltime", arrivaltime);
		params.addQueryStringParameter("creationordertime", creationordertime);
		params.addQueryStringParameter("isurgent", isurgent);
		params.addQueryStringParameter("orderprice",String.format("%.2f", orderprice));
		params.addQueryStringParameter("message",message);
		LogUtils.e("价格"+String.format("%.2f", orderprice));
		LogUtils.e("价格1"+orderprice+"");
		doHttp(1, MyConst.submitOrderAction, params, submitOrder);
	}
	
	void updateOrderState(List<OrderIdBean> list,String date){
		RequestParams params = new RequestParams();
		params.addBodyParameter("idOrder", gson.toJson(list));
		params.addBodyParameter("paytime", date);
		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addQueryStringParameter("toKen",MyApplication.getInstance().getToKen());// 每个接口都要传的字段可以放在这里
		LogUtils.e(""+gson.toJson(list));
//		doHttp(1, MyConst.paymentOrderAction, params, paymentOrder);
		HttpUtils httpUtils=new HttpUtils();
		httpUtils.send(HttpMethod.POST, MyConst.paymentOrderAction, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				saveSp();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Log.i("",arg0.result);
				try {
					JSONObject jsonObject = new JSONObject(arg0.result);
					int errorCode = jsonObject.getInt("code");
					if (errorCode == 0) {
							showToast("支付订单成功");
							if (radioFlag == 3) {
								payWordPopupWindows.dismiss();
								tv_balance.setText("余额： " + (Double.valueOf(MyApplication.getInstance().getBalance())-totalMoney));
								MyApplication.getInstance().setBalance(""+(Double.valueOf(MyApplication.getInstance().getBalance())-totalMoney));
							}
							Intent intent=new Intent(SingleOrderActivity.this, MainActivity.class);
							intent.putExtra("flag", "toOrder");
							startActivity(intent);
							finish();
//							saveSp();
					} else {
						//支付失败，把订单信息先写入sp
						saveSp();
						showToast(jsonObject.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			
			}
		});
		
		
		
	}
	
	void passwordValidation(String payPassword){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addBodyParameter("paymentpwd", payPassword);
		doHttp(1, MyConst.confirmOrderAction, params, passwordValidation);
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		Intent intent;
		switch (flag) {
//		case confirmOrder:
//			gson = new Gson();
//			confirmOrderBean = gson.fromJson(jsonString, ConfirmOrderBean.class);
//			tv_name_and_phone.setText(confirmOrderBean.getTlr_name() + "  " + confirmOrderBean.getPhone());
//			tv_detail_adress.setText("详细地址： " + confirmOrderBean.getDeliveryaddress());
//			tv_balance.setText("余额： " + confirmOrderBean.getBalance());
//			PayWayBean payWayBean;
//			for (int i = 0; i < confirmOrderBean.getPayways().size(); i++) {
//				payWayBean = new PayWayBean();
//				payWayBean = confirmOrderBean.getPayways().get(i);
//				payWayList.add(payWayBean);
//			}
//
//			if (payWayList.size() == 1) {
//				if (payWayList.get(0).getIdPayway().equals("1000")) {
//					// 1000=支付宝
//					rl_yinlian.setVisibility(View.GONE);
//					showRadio=1;
//				} else {
//					rl_zhifubao.setVisibility(View.GONE);
//					showRadio=2;
//				}
//			}
//
//			break;
		case submitOrder:
			Toast.makeText(SingleOrderActivity.this, "提交订单成功", Toast.LENGTH_SHORT).show();
			intent = new Intent();
			Intent intent1 = new Intent();
			intent.setAction(PurchaserCarFragment.update_car);
			intent1.setAction(PurchaserHomeFragment.update_list);
			sendBroadcast(intent);
			sendBroadcast(intent1);
			orderIdList=new ArrayList<OrderIdBean>();
			OrderIdBean bean=new OrderIdBean();
			bean.setIdOrder(jsonString);
//			orderIdList = gson.fromJson(jsonString, new TypeToken<List<OrderIdBean>>() {
//			}.getType());
			orderIdList.add(bean);
			if (radioFlag == 1) {
				//支付宝 
				
				orderid = UUID.randomUUID().toString();// 随机生成订单号
				ExternalPartner.getInstance(SingleOrderActivity.this, "支付", orderid, mHandler,String.format("%.2f", totalMoney))
						.doOrder();
				LogUtils.e("支付金额"+String.format("%.2f", totalMoney));
//				ExternalPartner.getInstance(SingleOrderActivity.this, "支付", orderid, mHandler,"0.01")
//				.doOrder();
				
			} else if (radioFlag == 2) {
				//微信
				orderid=PayActivity.genOutTradNo();
				Intent intent2=new Intent(SingleOrderActivity.this,PayActivity.class);
				intent2.putExtra("price",totalMoney+"");
				intent2.putExtra("whichActivity", "1");
				intent2.putExtra("orderId", orderid);
				startActivityForResult(intent2, SUBMIT_ORDER_REQUESTCODE);
			} else if(radioFlag == 3){
				//余额支付
				LayoutInflater inflaterDl = LayoutInflater.from(getApplicationContext());
				RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
						.inflate(R.layout.dialog_balancepay_detail, null);
				final Dialog dialog = new Dialog(SingleOrderActivity.this, R.style.Fullscreen_dialog);
				dialog.setContentView(relativeLayout);  
				
				Window win = dialog.getWindow();
				win.setGravity(Gravity.BOTTOM);
				dialog.show();
				
				WindowManager windowManager = getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
				lp.width = (int)(display.getWidth()); //设置宽度
				lp.height = (int)(display.getHeight()*0.5); //设置高度
				dialog.getWindow().setAttributes(lp);
				
				TextView tv_balance=(TextView) relativeLayout.findViewById(R.id.tv_balance);
				TextView tv_payment_amount=(TextView) relativeLayout.findViewById(R.id.tv_payment_amount);
				TextView tv_return=(TextView) relativeLayout.findViewById(R.id.tv_return);
				tv_balance.setText(String.format("%.2f", Double.valueOf(MyApplication.getInstance().getBalance()))+"元");
				tv_payment_amount.setText(String.format("%.2f", totalMoney)+"元");
				Button btn_paysure=(Button) relativeLayout.findViewById(R.id.btn_paysure);
				tv_return.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Intent intent=new Intent(SingleOrderActivity.this, MainActivity.class);
						intent.putExtra("flag", "toOrder");
						startActivity(intent);
						finish();
					}
				});
				btn_paysure.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						payWordPopupWindows=new PayWordPopupWindows();
						payWordPopupWindows.showPayWordPopupWindows(SingleOrderActivity.this, btn_submitorder);
					}
				});
			}
			
			
			// for(int i=0;i<orderIdList.size();i++){
			// showToast("订单编号："+orderIdList.get(i).getIdOrder());
			// }
//			
//			ExternalPartner.getInstance(SingleOrderActivity.this, "充值", "123213", mHandler, 0.01+"").doOrder();
			

			break;
		case paymentOrder:
			showToast("支付订单成功");
			if (radioFlag == 3) {
				payWordPopupWindows.dismiss();
				tv_balance.setText("余额： " + (Double.valueOf(MyApplication.getInstance().getBalance())-totalMoney));
				MyApplication.getInstance().setBalance(""+(Double.valueOf(MyApplication.getInstance().getBalance())-totalMoney));
			}
			intent=new Intent(SingleOrderActivity.this, MainActivity.class);
		
			intent.putExtra("flag", "toOrder");
			startActivity(intent);
			finish();
			break;
		case queryProfile:
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				tv_name_and_phone.setText(jsonObject.getString("name")+ "  " + MyApplication.replaceIndex(5, 8, jsonObject.getString("phone"), "*"));
				tv_detail_adress.setText(jsonObject.getString("address")+jsonObject.getString("stallsname"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			tv_name_and_phone.setText(MyApplication.getInstance().getTlr_name() + "  " + MyApplication.getInstance().getPhone());
//			tv_detail_adress.setText("详细地址： " + MyApplication.getInstance().getDeliveryaddress());
			
			if(MyApplication.getInstance().getBalance()!=null){
				tv_balance.setText("余额： " + String.format("%.2f", Double.valueOf(MyApplication.getInstance().getBalance())));
			}else{
				tv_balance.setVisibility(View.GONE);
				check_balance.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}
	
	void toSubmitOrder(String message){
		if (radioFlag == 1) {
//			submitOrder(orderInfoList);
			submitOrder(orderInfoList, arrivaltime, creationordertimes, isurgent, totalMoney,message);
		} else if (radioFlag == 2) {
//			Intent intent = new Intent(SingleOrderActivity.this, PayBanActivity.class);
//			startActivity(intent);
			submitOrder(orderInfoList, arrivaltime, creationordertimes, isurgent, totalMoney,message);
		} else if(radioFlag == 3){
			if(!"Y".equals(MyApplication.getInstance().getIsSetpassword())){
				showToast("请先设置您的支付密码才能使用余额支付");
			}else if (!(totalMoney >Double.valueOf(MyApplication.getInstance().getBalance()))) {
				submitOrder(orderInfoList, arrivaltime, creationordertimes, isurgent, totalMoney,message);
			}else{
				showToast("账户余额不足，请前往充值或选择其它付款方式!");
			}
		} else {
			showToast("请选择支付方式");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		msg.what== 0 ：表示支付成功
//				msg.what== -1 ：表示支付失败
//				msg.what== -2 ：表示取消支付
		if(requestCode==SUBMIT_ORDER_REQUESTCODE&& resultCode==PayActivity.SUBMIT_ORDER_RESULTCODE &&data!=null){
			if("800".equals(data.getExtras().getString("payResultCode"))){
				Toast.makeText(SingleOrderActivity.this, "商户订单号重复或生成错误", Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(SingleOrderActivity.this, MainActivity.class);
				intent.putExtra("flag", "toOrder");
				startActivity(intent);
				LogUtils.e("800");
				finish();
			}else if("0".equals(data.getExtras().getString("payResultCode"))){
				Toast.makeText(SingleOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date curDate1 = new Date(System.currentTimeMillis());// 获取当前时间
				updateOrderState(orderIdList,df1.format(curDate1));
			}else if("-1".equals(data.getExtras().getString("payResultCode"))){
				Toast.makeText(SingleOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(SingleOrderActivity.this, MainActivity.class);
				intent.putExtra("flag", "toOrder");
				startActivity(intent);
				LogUtils.e("-1");
				finish();
			}else if("-2".equals(data.getExtras().getString("payResultCode"))){
				Toast.makeText(SingleOrderActivity.this, "取消支付", Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(SingleOrderActivity.this, MainActivity.class);
				intent.putExtra("flag", "toOrder");
				startActivity(intent);
				finish();
			}
		}
	}
	
	 void saveSp(){
		 LogUtils.i("失败，保存sp");
		SharedPreferences sharedPreferences = getSharedPreferences("payOrderInfoSP",Context.MODE_PRIVATE + Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit(); // 获取编辑器
		String jsonString=sharedPreferences.getString("payOrderInfoSP", "[]");
		LogUtils.i("saveSp"+jsonString);
		//可能有存在多次支付完成，改变订单状态接口失败或无法访问的情况，把订单add到list，以 json格式保存到sp
		List<PayOrderInfo> list=new Gson().fromJson(jsonString,new TypeToken<List<PayOrderInfo>>() {
		}.getType());
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate1 = new Date(System.currentTimeMillis());// 获取当前时间
		PayOrderInfo payOrderInfo=new PayOrderInfo();
		payOrderInfo.setOrderId(orderIdList);
		payOrderInfo.setSuccess(false);
		payOrderInfo.setPayTime(df1.format(curDate1));
		if(orderIdList.size()!=0){
			if(!checkOrderSP(list, orderIdList.get(0).getIdOrder())){
				list.add(payOrderInfo);
				editor.putString("payOrderInfoSP", new Gson().toJson(list));
				editor.commit();
			}
		}
	}
	
	boolean checkOrderSP(List<PayOrderInfo> list,String orderNo){
		boolean result=false;
		for(int i=0;i<list.size();i++){
			if(list.get(i).getOrderId().size()!=0){
				if(orderNo.equals(list.get(i).getOrderId().get(0).getIdOrder())){
					result=true;
					break;
				}
			}
		}
		return result;
	}
	
	void deleteSpOrder(String orderNo){
		SharedPreferences sharedPreferences = getSharedPreferences("payOrderInfoSP",Context.MODE_PRIVATE + Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit(); // 获取编辑器
		String jsonString=sharedPreferences.getString("payOrderInfoSP", "[]");
		LogUtils.i("deleteSpOrder"+jsonString);
		List<PayOrderInfo> list=new Gson().fromJson(jsonString,new TypeToken<List<PayOrderInfo>>() {
		}.getType());
		
		for(int i=0;i<list.size();i++){
			if(list.get(i).getOrderId().size()!=0){
				if(orderNo.equals(list.get(i).getOrderId().get(0).getIdOrder())){
					list.remove(i);
					--i;
				}
			}
		}
		editor.putString("payOrderInfoSP", new Gson().toJson(list));
		editor.commit();
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(SingleOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
					updateOrderState(orderIdList,df.format(curDate));
				} else {
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(SingleOrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
						
					} else {
						Toast.makeText(SingleOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
						Intent intent=new Intent(SingleOrderActivity.this, MainActivity.class);
						intent.putExtra("flag", "toOrder");
						startActivity(intent);
						finish();
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(SingleOrderActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	void setRadio() {
		radio_weixin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					if (ConstontUtils.checkAPP(SingleOrderActivity.this, "com.tencent.mm")) {
						radio_zhifubao.setChecked(false);
						radioFlag = 2;
						oldChooseRadio=2;
					} else {
						radio_weixin.setChecked(false);
						showToast("你手机没有安装微信，不能选择微信支付");
					}
					
					
					
				}
			}
		});
		radio_zhifubao.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					if (ConstontUtils.checkAPP(SingleOrderActivity.this, "com.eg.android.AlipayGphone")) {
						radio_weixin.setChecked(false);
						radioFlag = 1;
						oldChooseRadio=1;
					} else {
						radio_zhifubao.setChecked(false);
						showToast("你手机没有安装支付宝，不能选择支付宝支付");
					}
				}
			}
		});
		
		/*
		radio_platform_send.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
		});
		radio_thrid_send.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// 对话框
				LayoutInflater inflaterDl = LayoutInflater.from(getApplicationContext());
				RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
						.inflate(R.layout.dialog_thrid_distribution, null);
				Builder builder = new AlertDialog.Builder(SingleOrderActivity.this);
				builder.setView(relativeLayout);
				final Dialog dialog = builder.create();
				dialog.show();
				TextView tv=(TextView) relativeLayout.findViewById(R.id.tv_i_know);
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			}
		});
		 */
	}
	
	public class PayWordPopupWindows extends PopupWindow {
		
		@ViewInject(R.id.pay_box1)
		TextView box1;
		@ViewInject(R.id.pay_box2)
		TextView box2;
		@ViewInject(R.id.pay_box3)
		TextView box3;
		@ViewInject(R.id.pay_box4)
		TextView box4;
		@ViewInject(R.id.pay_box5)
		TextView box5;
		@ViewInject(R.id.pay_box6)
		TextView box6;
		private ArrayList<String> mList = new ArrayList<String>();
		
		@OnClick({ R.id.pay_keyboard_one, R.id.pay_keyboard_two,
			R.id.pay_keyboard_three, R.id.pay_keyboard_four,
			R.id.pay_keyboard_five, R.id.pay_keyboard_six,
			R.id.pay_keyboard_seven, R.id.pay_keyboard_eight,
			R.id.pay_keyboard_nine, R.id.pay_keyboard_zero,
			R.id.pay_keyboard_del ,R.id.iv_return})
		
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.pay_keyboard_one:
				parseActionType(KeyboardEnum.one);
				break;
			case R.id.pay_keyboard_two:
				parseActionType(KeyboardEnum.two);
				break;
			case R.id.pay_keyboard_three:
				parseActionType(KeyboardEnum.three);
				break;
			case R.id.pay_keyboard_four:
				parseActionType(KeyboardEnum.four);
				break;
			case R.id.pay_keyboard_five:
				parseActionType(KeyboardEnum.five);
				break;
			case R.id.pay_keyboard_six:
				parseActionType(KeyboardEnum.six);
				break;
			case R.id.pay_keyboard_seven:
				parseActionType(KeyboardEnum.seven);
				break;
			case R.id.pay_keyboard_eight:
				parseActionType(KeyboardEnum.eight);
				break;
			case R.id.pay_keyboard_nine:
				parseActionType(KeyboardEnum.nine);
				break;
			case R.id.pay_keyboard_zero:
				parseActionType(KeyboardEnum.zero);
				break;
			case R.id.pay_keyboard_del:
				parseActionType(KeyboardEnum.del);
				break;
			case R.id.iv_return:
				dismiss();
				Intent intent=new Intent(SingleOrderActivity.this, MainActivity.class);
				intent.putExtra("flag", "toOrder");
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		
	}
	
	public void showPayWordPopupWindows(Context mContext, View parent) {
			
			View view = View.inflate(mContext, R.layout.popupwindow_payword, null);
			ViewUtils.inject(this,view);
			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.MATCH_PARENT);
			
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			
			update();
			
		}
		
	
		private void parseActionType(KeyboardEnum type) {
			if (type.getType() == ActionEnum.add) {
				if (mList.size() < 6) {
					mList.add(type.getValue());
					updateUi();
				}
			} else if (type.getType() == ActionEnum.delete) {
				if (mList.size() > 0) {
					mList.remove(mList.get(mList.size() - 1));
					updateUi();
				}
			}

			if (mList.size() == 6) {
				for (int i = 0; i < mList.size(); i++) {
					paypassword += mList.get(i);
					
				}
				RequestParams params=new RequestParams();
				params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
				params.addBodyParameter("paymentpwd", paypassword);
				params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
				params.addBodyParameter("toKen", MyApplication.getInstance().getToKen());
				HttpUtils httpUtils=new HttpUtils();
//				httpUtils.configCookieStore(MyCookieStore.cookieStore);
				httpUtils.send(HttpMethod.POST, MyConst.passwordValidationAction, params,new RequestCallBack<String>() {
//				BaseActivity.httpUtils.send(HttpMethod.POST, MyConst.passwordValidationAction, params,new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							int errorCode = jsonObject.getInt("code");
							if (errorCode == 0) {
								DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date curDate1 = new Date(System.currentTimeMillis());// 获取当前时间
								RequestParams params=new RequestParams();
								params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber()+"");
								params.addBodyParameter("payPrice", totalMoney+"");
								params.addBodyParameter("idOrder",gson.toJson(orderIdList));
								params.addBodyParameter("paytime", curDate1+"");
								params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber()+"");
								params.addBodyParameter("toKen",MyApplication.getInstance().getToKen());
								HttpUtils httpUtils=new HttpUtils();
//								httpUtils.configCookieStore(MyCookieStore.cookieStore);
								httpUtils.send(HttpMethod.POST, MyConst.balancePaidAction, params,new RequestCallBack<String>() {
//								BaseActivity.httpUtils.send(HttpMethod.POST, MyConst.balancePaidAction, params,new RequestCallBack<String>() {
									@Override
									public void onFailure(HttpException arg0,String arg1) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onSuccess(ResponseInfo<String> arg0) {
										// TODO Auto-generated method stub
										try {
											JSONObject jsonObject = new JSONObject(arg0.result);
											int errorCode = jsonObject.getInt("code");
											if(errorCode==0){
												paypassword="";
//												showToast("支付订单成功");
												//支付成功，调支付订单接口，改变订单状态
												DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												Date curDate1 = new Date(System.currentTimeMillis());// 获取当前时间
												updateOrderState(orderIdList,df1.format(curDate1));
												
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
									}
								});
								 
							}else {
								// 错误就统一弹出错误信息
								paypassword="";
								mList.clear();
								updateUi();
								showToast(jsonObject.getString("message"));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} );
				
			}
			
		}
		
		void updateUi() {
			if (mList.size() == 0) {
				box1.setText("");
				box2.setText("");
				box3.setText("");
				box4.setText("");
				box5.setText("");
				box6.setText("");
			} else if (mList.size() == 1) {
				box1.setText(mList.get(0));
				box2.setText("");
				box3.setText("");
				box4.setText("");
				box5.setText("");
				box6.setText("");
			} else if (mList.size() == 2) {
				box1.setText(mList.get(0));
				box2.setText(mList.get(1));
				box3.setText("");
				box4.setText("");
				box5.setText("");
				box6.setText("");
			} else if (mList.size() == 3) {
				box1.setText(mList.get(0));
				box2.setText(mList.get(1));
				box3.setText(mList.get(2));
				box4.setText("");
				box5.setText("");
				box6.setText("");
			} else if (mList.size() == 4) {
				box1.setText(mList.get(0));
				box2.setText(mList.get(1));
				box3.setText(mList.get(2));
				box4.setText(mList.get(3));
				box5.setText("");
				box6.setText("");
			} else if (mList.size() == 5) {
				box1.setText(mList.get(0));
				box2.setText(mList.get(1));
				box3.setText(mList.get(2));
				box4.setText(mList.get(3));
				box5.setText(mList.get(4));
				box6.setText("");
			} else if (mList.size() == 6) {
				box1.setText(mList.get(0));
				box2.setText(mList.get(1));
				box3.setText(mList.get(2));
				box4.setText(mList.get(3));
				box5.setText(mList.get(4));
				box6.setText(mList.get(5));
			}
		}
	}
	
	
	

}
