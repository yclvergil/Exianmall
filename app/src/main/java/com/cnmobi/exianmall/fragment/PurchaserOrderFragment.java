package com.cnmobi.exianmall.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
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
import com.cnmobi.exianmall.adapter.OrderOneGroupAdapter;
import com.cnmobi.exianmall.adapter.OrderThreeGroupAdapter;
import com.cnmobi.exianmall.adapter.OrderTwoGroupAdapter;
import com.cnmobi.exianmall.base.BaseFragment;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.OrderGroupBean;
import com.cnmobi.exianmall.bean.OrderIdBean;
import com.cnmobi.exianmall.bean.PayOrderInfo;
import com.cnmobi.exianmall.bean.PreChargeOrder;
import com.cnmobi.exianmall.home.activity.CaptureActivity;
import com.cnmobi.exianmall.interfaces.ICancelOrderClickListener;
import com.cnmobi.exianmall.interfaces.IOrderApplyRefundClickListen;
import com.cnmobi.exianmall.interfaces.onOrderScoreItemClickListener;
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
 * 订单界面
 */
public class PurchaserOrderFragment extends BaseFragment {

	@ViewInject(R.id.tv_order_wait_payment)
	private TextView tv_order_wait_payment;
	@ViewInject(R.id.tv_order_wait_receive)
	private TextView tv_order_wait_receive;
	@ViewInject(R.id.tv_order_wait_grade)
	private TextView tv_order_wait_grade;
	@ViewInject(R.id.view_order_first_line)
	private View view_order_first_line;
	@ViewInject(R.id.tv_msg_pay)
	private View tv_msg_pay;//若list为0时 显示该控件信息
	@ViewInject(R.id.view_order_second_line)
	private View view_order_second_line;
	@ViewInject(R.id.lv_one)
	ListView lv_one;// 待付款listview
	@ViewInject(R.id.lv_two)
	ListView lv_two;// 待收货listview
	@ViewInject(R.id.lv_three)
	ListView lv_three;// 待评分listview
	int radioFlag = 0;// 选择的支付方式，1为支付宝，2为余额,3为微信
	String paypassword = "";// 输入的密码
	private double totalMoney;
	private List<OrderGroupBean> groupOneList = new ArrayList<OrderGroupBean>();
	static Context context;
	private OrderOneGroupAdapter oneGroupAdapter;
	private OrderTwoGroupAdapter twoGroupAdapter;
	private OrderThreeGroupAdapter threeGroupAdapter;
	PayWordPopupWindows payWordPopupWindows;
	/** 所有订单接口标识 */
	public static final int allOrderAction = 0;

	/** 评分按钮接口标识（评分按钮） */
	public static final int score = 1;

	/** 图标按钮评价接口标识（图标按钮） */
	public static final int evaluate = 2;

	/** 确认收货接口标识 */
	public static final int confirm = 3;
	/**
	 * 支付成功后修改订单的状态接口标识
	 */
	public static final int paymentOrder = 4;
	/**
	 * 申请退款接口标识
	 */
	public static final int refundApplyAction=5;
	/**
	 * 取消订单接口标识 
	 */
	public static final int cancelOrder=6;
	
	private int isCut = 0;// 订单状态，默认为待付款

	private int currentPage1 = 1;
	private int currentPage2 = 1;
	private int currentPage3 = 1;

	private boolean isAll1 = false;
	private boolean isAll2 = false;
	private boolean isAll3 = false;

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	private static final int WAIT_PAY_REQUESTCODE = 2;
	private Gson gson=new Gson();
	myScrollListener scrollListener = new myScrollListener();
	private List<OrderIdBean> orderIdList=new ArrayList<OrderIdBean>(); // 订单编号
	private String orderid = "";// 订单号
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_purchaser_order, container, false);
		ViewUtils.inject(this, view);
		
		init();
		orderHttp(currentPage1);
		return view;
	}

	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	void checkSP(){
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("payOrderInfoSP",Context.MODE_PRIVATE + Context.MODE_PRIVATE);
		String jsonString=sharedPreferences.getString("payOrderInfoSP", "[]");
		LogUtils.i("checkSP"+jsonString);
		List<PayOrderInfo> list=new Gson().fromJson(jsonString,new TypeToken<List<PayOrderInfo>>() {
		}.getType());
		for(int i=0;i<list.size();i++){
			if(!list.get(i).isSuccess()){
				//该次充值完成支付，没有成功，重新提交
				LogUtils.i("订单"+list.get(i).getOrderId().get(0).getIdOrder()+"没有完成");
				updateOrderState(list.get(i).getOrderId(),list.get(i).getPayTime());
			}
		}
	}
	
	void updateOrderState(final List<OrderIdBean> list,String date){
		RequestParams params = new RequestParams();
		Gson gson = new Gson();
		params.addBodyParameter("idOrder", gson.toJson(list));
		params.addBodyParameter("paytime", date);
		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addQueryStringParameter("toKen",MyApplication.getInstance().getToKen());// 每个接口都要传的字段可以放在这里
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
						ShowToast("订单支付完成！");
						if (radioFlag == 2) {
							payWordPopupWindows.dismiss();
							MyApplication.getInstance().setBalance(""+(Double.valueOf(MyApplication.getInstance().getBalance())-totalMoney));
						}
						groupOneList.clear();
						isCut=0;
						currentPage1=1;
						orderHttp(currentPage1);
						deleteSpOrder(list.get(0).getIdOrder());
//						saveSp();
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
	 void saveSp(){
			SharedPreferences sharedPreferences = getActivity().getSharedPreferences("payOrderInfoSP",Context.MODE_PRIVATE + Context.MODE_PRIVATE);
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
			SharedPreferences sharedPreferences = getActivity().getSharedPreferences("payOrderInfoSP",Context.MODE_PRIVATE + Context.MODE_PRIVATE);
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
		
	
	void refundApply(String idOrder,String orderNo,String idCommodity,String idLevel,String price){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		RequestParams params = new RequestParams();
		params.addBodyParameter("idOrder", idOrder);
		params.addBodyParameter("orderNo", orderNo);
		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber()+"");
		params.addBodyParameter("idCommodity", idCommodity);
		params.addBodyParameter("idLevel", idLevel);
		params.addBodyParameter("price", price);
		params.addBodyParameter("applydate", format.format(new Date()));
		doHttp(1, MyConst.refundApplyAction, params, refundApplyAction);
	}
	
	@OnClick({ R.id.tv_order_wait_payment, R.id.tv_order_wait_receive, R.id.tv_order_wait_grade,
			R.id.iv_common_head_3_details, R.id.iv_home_left_top })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_home_left_top:
			break;
		case R.id.tv_order_wait_payment:// 待付款
			cleanAll();
			oneGroupAdapter.clearFlag();
			tv_order_wait_payment.setBackgroundResource(R.drawable.bg_order_smal_slider);
			tv_order_wait_payment.setTextColor(Color.parseColor("#FDE000"));
			view_order_second_line.setVisibility(View.VISIBLE);
			view_order_second_line.setBackgroundColor(Color.parseColor("#FFFFFF"));
			lv_one.setVisibility(View.VISIBLE);
			lv_two.setVisibility(View.GONE);
			lv_three.setVisibility(View.GONE);

			isCut = 0;
			isAll1 = false;
			currentPage1 = 1;
			orderHttp(currentPage1);
			break;
		case R.id.tv_order_wait_receive:// 待收货
			cleanAll();
			twoGroupAdapter.clearFlag();
			tv_order_wait_receive.setBackgroundResource(R.drawable.bg_order_smal_slider);
			tv_order_wait_receive.setTextColor(Color.parseColor("#FDE000"));
			lv_one.setVisibility(View.GONE);
			lv_two.setVisibility(View.VISIBLE);
			lv_three.setVisibility(View.GONE);
			isCut = 1;
			isAll2 = false;
			currentPage2 = 1;
			orderHttp(currentPage2);
			break;
		case R.id.tv_order_wait_grade:// 待评分
			cleanAll();
			threeGroupAdapter.clearFlag();
			tv_order_wait_grade.setBackgroundResource(R.drawable.bg_order_smal_slider);
			tv_order_wait_grade.setTextColor(Color.parseColor("#FDE000"));
			view_order_first_line.setVisibility(View.VISIBLE);
			view_order_first_line.setBackgroundColor(Color.parseColor("#FFFFFF"));
			lv_one.setVisibility(View.GONE);
			lv_two.setVisibility(View.GONE);
			lv_three.setVisibility(View.VISIBLE);

			isCut = 2;
			isAll3 = false;
			currentPage3 = 1;
			orderHttp(currentPage3);
			break;
		case R.id.iv_common_head_3_details:// 二维码扫描
			intent = new Intent(getActivity(), CaptureActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 按钮背景，文字颜色,竖线初始化
	 * 
	 */
	public void cleanAll() {
		tv_order_wait_payment.setBackgroundResource(0);
		tv_order_wait_receive.setBackgroundResource(0);
		tv_order_wait_grade.setBackgroundResource(0);
		tv_order_wait_payment.setTextColor(Color.parseColor("#FFFFFF"));
		tv_order_wait_receive.setTextColor(Color.parseColor("#FFFFFF"));
		tv_order_wait_grade.setTextColor(Color.parseColor("#FFFFFF"));
		view_order_first_line.setVisibility(View.GONE);
		view_order_second_line.setVisibility(View.GONE);

		groupOneList.clear();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				String resultStatus = payResult.getResultStatus();
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
					updateOrderState(orderIdList,df.format(curDate));
					
				} else {
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(getActivity(), "支付结果确认中", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(getActivity(), "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		msg.what== 0 ：表示支付成功
//				msg.what== -1 ：表示支付失败
//				msg.what== -2 ：表示取消支付
		if(requestCode==WAIT_PAY_REQUESTCODE&& resultCode==PayActivity.WAIT_PAY_RESULTCODE &&data!=null){
			if("800".equals(data.getExtras().getString("payResultCode"))){
				Toast.makeText(getActivity(), "商户订单号重复或生成错误", Toast.LENGTH_SHORT).show();
			}else if("0".equals(data.getExtras().getString("payResultCode"))){
				Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
				DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date curDate1 = new Date(System.currentTimeMillis());// 获取当前时间
				updateOrderState(orderIdList,df1.format(curDate1));
				
			}else if("-1".equals(data.getExtras().getString("payResultCode"))){
				Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
			}else if("-2".equals(data.getExtras().getString("payResultCode"))){
				Toast.makeText(getActivity(), "取消支付", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	void init() {

		oneGroupAdapter = new OrderOneGroupAdapter(groupOneList, getActivity());
		lv_one.setAdapter(oneGroupAdapter);
		oneGroupAdapter.setCancelOrderClickListener(new ICancelOrderClickListener() {
			
			@Override
			public void onCancelOrderClickListener(View v, int position, int idOrder,
					int orderStatus) {
				// TODO Auto-generated method stub
				cancelOrder(idOrder, orderStatus);
			}
		});
		
		oneGroupAdapter.setonOrderScoreItemClickListenerListener(new onOrderScoreItemClickListener() {

			@Override
			public void onOrderScoreItemClick(View v, final int position, final String str) {
				// TODO Auto-generated method stub
			
//				//这里弹一个框选择支付方式
				LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
				LinearLayout linearLayout = (LinearLayout) inflaterDl
						.inflate(R.layout.dialog_select_payway, null);
				Builder builder = new AlertDialog.Builder(getActivity());
				builder.setView(linearLayout);
				final Dialog dialog = builder.create();
				dialog.show();
//				TextView to_pay=(TextView) linearLayout.findViewById(R.id.to_pay);
//				final RadioButton radio_zhifubao=(RadioButton) linearLayout.findViewById(R.id.radio_zhifubao);
//				final RadioButton radio_weixin=(RadioButton) linearLayout.findViewById(R.id.radio_weixin);
//				final RadioButton radio_balance=(RadioButton) linearLayout.findViewById(R.id.radio_balance);
//				RelativeLayout rl_balance= (RelativeLayout) linearLayout.findViewById(R.id.rl_balance);
//					if(!"Y".equals(MyApplication.getInstance().getIsSetpassword())){
//						rl_balance.setVisibility(View.GONE);
//				}
				final LinearLayout ly_zhifubao=(LinearLayout) linearLayout.findViewById(R.id.ly_zhifubao);
				final LinearLayout ly_weixin=(LinearLayout) linearLayout.findViewById(R.id.ly_weixin);
				final LinearLayout ly_wallet=(LinearLayout) linearLayout.findViewById(R.id.ly_wallet);
				
				ly_zhifubao.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						radioFlag=1;
						dialog.dismiss();
						OrderIdBean bean=new OrderIdBean();
						bean.setIdOrder(groupOneList.get(position).getIdNumber()+"");
						orderIdList.add(bean);
						if (ConstontUtils.checkAPP(getActivity(), "com.eg.android.AlipayGphone")) {
							//支付宝 
							//先跳过支付宝，更改订单状态
							orderid = UUID.randomUUID().toString();// 随机生成订单号
//							ExternalPartner.getInstance(SingleOrderActivity.this, "支付", orderid, mHandler,totalMoney+"")
//									.doOrder();
							ExternalPartner.getInstance(getActivity(), "支付", orderid, mHandler,groupOneList.get(position).getOrderprice())
							.doOrder();
						} else {
							showToast("你手机没有安装支付宝，不能选择支付宝支付");
						}
						
					}
				});
				
				ly_weixin.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						radioFlag=3;
						dialog.dismiss();
						OrderIdBean bean=new OrderIdBean();
						bean.setIdOrder(groupOneList.get(position).getIdNumber()+"");
						orderIdList.add(bean);
						if (ConstontUtils.checkAPP(getActivity(), "com.tencent.mm")) {
							Intent intent=new Intent(getActivity(),PayActivity.class);
							intent.putExtra("price",groupOneList.get(position).getOrderprice());
							intent.putExtra("whichActivity", "2");
							intent.putExtra("orderId", PayActivity.genOutTradNo());
							startActivityForResult(intent, WAIT_PAY_REQUESTCODE);
						}else{
							showToast("你手机没有安装微信，不能选择微信支付");
						}
					}
				});
				
				ly_wallet.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						radioFlag=2;
						dialog.dismiss();
						OrderIdBean bean=new OrderIdBean();
						bean.setIdOrder(groupOneList.get(position).getIdNumber()+"");
						orderIdList.add(bean);
						totalMoney=Double.valueOf(groupOneList.get(position).getOrderprice());
						if(Double.valueOf(MyApplication.getInstance().getBalance())>=totalMoney){
							
						//在这里判断下余额是否充足	
							LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
							RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
									.inflate(R.layout.dialog_balancepay_detail, null);
							final Dialog dialog = new Dialog(getActivity(), R.style.Fullscreen_dialog);
							dialog.setContentView(relativeLayout);  
							
							Window win = dialog.getWindow();
							win.setGravity(Gravity.BOTTOM);
							dialog.show();
							
							WindowManager windowManager =getActivity().getWindowManager();
							Display display = windowManager.getDefaultDisplay();
							WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
							lp.width = (int)(display.getWidth()); //设置宽度
							lp.height = (int)(display.getHeight()*0.5); //设置高度
							dialog.getWindow().setAttributes(lp);
							
							TextView tv_balance=(TextView) relativeLayout.findViewById(R.id.tv_balance);
							TextView tv_payment_amount=(TextView) relativeLayout.findViewById(R.id.tv_payment_amount);
							TextView tv_return=(TextView) relativeLayout.findViewById(R.id.tv_return);
							LogUtils.i("余额"+MyApplication.getInstance().getBalance());
							tv_balance.setText(String.format("%.2f", Double.valueOf(MyApplication.getInstance().getBalance()))+"元");
							tv_payment_amount.setText(String.format("%.2f", Double.valueOf(groupOneList.get(position).getOrderprice()))+"元");
							Button btn_paysure=(Button) relativeLayout.findViewById(R.id.btn_paysure);
							tv_return.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
							btn_paysure.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									payWordPopupWindows=new PayWordPopupWindows();
									payWordPopupWindows.showPayWordPopupWindows(getActivity(), tv_order_wait_payment);
								}
							});
						}else{
							showToast("账户余额不足，请前往充值或选择其它付款方式!");
						}
						
					
						
					}
				});
				
//				radio_zhifubao.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//					
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//						// TODO Auto-generated method stub
//						if(isChecked){
//							radio_balance.setChecked(false);
//							radio_weixin.setChecked(false);
//							radioFlag=1;
//						}
//					}
//				});

//				radio_weixin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//					
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//						// TODO Auto-generated method stub
//						if(isChecked){
//							radio_balance.setChecked(false);
//							radio_zhifubao.setChecked(false);
//							radioFlag=3;
//						}
//					}
//				});
				
//				radio_balance.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//					
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//						// TODO Auto-generated method stub
//						if(isChecked){
//							radio_zhifubao.setChecked(false);
//							radio_weixin.setChecked(false);
//							radioFlag=2;
//						}
//					}
//				});
				
//				to_pay.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						if(radioFlag==0){
//							showToast("请选择付款方式");
//						}else{
//						dialog.dismiss();
//						OrderIdBean bean=new OrderIdBean();
//						bean.setIdOrder(groupOneList.get(position).getIdNumber()+"");
//						orderIdList.add(bean);
//						if(radioFlag==1){
//							
//							if (ConstontUtils.checkAPP(getActivity(), "com.eg.android.AlipayGphone")) {
//								//支付宝 
//								//先跳过支付宝，更改订单状态
//								orderid = UUID.randomUUID().toString();// 随机生成订单号
////								ExternalPartner.getInstance(SingleOrderActivity.this, "支付", orderid, mHandler,totalMoney+"")
////										.doOrder();
//								ExternalPartner.getInstance(getActivity(), "支付", orderid, mHandler,groupOneList.get(position).getOrderprice())
//								.doOrder();
//							} else {
//								ShowToast("该设备上未安装支付宝");
//							}
//							
////
////							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////							Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
//////							OrderIdBean bean=new OrderIdBean();
//////							bean.setIdOrder(groupOneList.get(position).getIdNumber()+"");
//////							orderIdList.add(bean);
////							updateOrderState(orderIdList,df.format(curDate));
//							
//						}else if(radioFlag==2){
//							totalMoney=Double.valueOf(groupOneList.get(position).getOrderprice());
//							if(Double.valueOf(MyApplication.getInstance().getBalance())>=totalMoney){
//								
//							//在这里判断下余额是否充足	
//								LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
//								RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
//										.inflate(R.layout.dialog_balancepay_detail, null);
//								final Dialog dialog = new Dialog(getActivity(), R.style.Fullscreen_dialog);
//								dialog.setContentView(relativeLayout);  
//								
//								Window win = dialog.getWindow();
//								win.setGravity(Gravity.BOTTOM);
//								dialog.show();
//								
//								WindowManager windowManager =getActivity().getWindowManager();
//								Display display = windowManager.getDefaultDisplay();
//								WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//								lp.width = (int)(display.getWidth()); //设置宽度
//								lp.height = (int)(display.getHeight()*0.5); //设置高度
//								dialog.getWindow().setAttributes(lp);
//								
//								TextView tv_balance=(TextView) relativeLayout.findViewById(R.id.tv_balance);
//								TextView tv_payment_amount=(TextView) relativeLayout.findViewById(R.id.tv_payment_amount);
//								TextView tv_return=(TextView) relativeLayout.findViewById(R.id.tv_return);
//								LogUtils.e("余额"+MyApplication.getInstance().getBalance());
//								tv_balance.setText(String.format("%.2f", Double.valueOf(MyApplication.getInstance().getBalance()))+"元");
//								tv_payment_amount.setText(String.format("%.2f", Double.valueOf(groupOneList.get(position).getOrderprice()))+"元");
//								Button btn_paysure=(Button) relativeLayout.findViewById(R.id.btn_paysure);
//								tv_return.setOnClickListener(new OnClickListener() {
//									
//									@Override
//									public void onClick(View v) {
//										// TODO Auto-generated method stub
//										dialog.dismiss();
//									}
//								});
//								btn_paysure.setOnClickListener(new OnClickListener() {
//									
//									@Override
//									public void onClick(View v) {
//										// TODO Auto-generated method stub
//										dialog.dismiss();
//										payWordPopupWindows=new PayWordPopupWindows();
//										payWordPopupWindows.showPayWordPopupWindows(getActivity(), tv_order_wait_payment);
//									}
//								});
//							}else{
//								showToast("账户余额不足，请前往充值或选择其它付款方式!");
//							}
//							
//						}else if(radioFlag==3){
//							//微信支付
//							Intent intent=new Intent(getActivity(),PayActivity.class);
//							intent.putExtra("price",groupOneList.get(position).getOrderprice());
//							intent.putExtra("whichActivity", "2");
//							intent.putExtra("orderId", PayActivity.genOutTradNo());
//							startActivityForResult(intent, WAIT_PAY_REQUESTCODE);
//						}
////						else{
////							showToast("请选择付款方式");
////						}
//					}
//					}
//				});
				
				// List<ConfirmOrderProductBean> orderProductList=new
				// ArrayList<ConfirmOrderProductBean>();
				// // 这里把结算的订单带过去就好。
				// groupBean = groupOneList.get(position);
				// List<OrderDetail> childs = groupBean.getOrderDetail();
				// for (int j = 0; j < childs.size(); j++) {
				// childBean = childs.get(j);
				// //被选中就把该项信息添加到订单
				// ConfirmOrderProductBean bean=new
				// ConfirmOrderProductBean();//一个订单对象
				// bean.setAdress(childBean.getAddress());
				// bean.setIdStore(childBean.getIdStore());
				// bean.setBuyNum(Integer.valueOf(childBean.getBuynumber()));
				// bean.setImageUrl(childBean.getImagename());
				// bean.setLevel(childBean.getLevelname());
				// bean.setIdLevel(Integer.valueOf(childBean.getIdLevel()));
				// bean.setName(childBean.getCommodityname());
				// bean.setStock(childBean.getStock());
				// bean.setSumBuyNum(childBean.getSumBuyNum());
				// bean.setAdressId(childBean.getAddressId());
				// bean.setIdCommodity(Integer.valueOf(childBean.getIdCommodity()));
				// bean.setPrice(childBean.getBuyprice()+"");
				// orderProductList.add(bean);
				// LogUtils.i(bean.toString());
				// }
				// Intent intent = new Intent(getActivity(),
				// SingleOrderActivity.class);
				// intent.putExtra("orderProductList",(Serializable)orderProductList);
				// startActivity(intent);

//				if (ConstontUtils.checkAPP(getActivity(), "com.eg.android.AlipayGphone")) {
//					ExternalPartner.getInstance(getActivity(), "充值", str, mHandler, 0.01 + "").doOrder();
//				} else {
//					ShowToast("该设备上未安装支付宝");
//				}
			}

			@Override
			public void onOrderScoreItemClick(View v, int position1, int position2, float rating) {
			}

			@Override
			public void onOrderScoreItemClick(View v, int position1,
					int position2, float rating, String str) {
				// TODO Auto-generated method stub
				
			}
		});

		twoGroupAdapter = new OrderTwoGroupAdapter(groupOneList, getActivity());
		lv_two.setAdapter(twoGroupAdapter);
		twoGroupAdapter.setonOrderScoreItemClickListenerListener(new onOrderScoreItemClickListener() {

			@Override
			public void onOrderScoreItemClick(View v, int position, String str) {// 收货/二维码
				// TODO Auto-generated method stub
				if (position == 0) {
					confirmGoodsAction(str);
				} else if (position == 1) {
					Intent intent = new Intent(getActivity(), CaptureActivity.class);
					getActivity().startActivityForResult(intent, 1);
				}
			}

			@Override
			public void onOrderScoreItemClick(View v, int position1, int position2, float rating) {
			}

			@Override
			public void onOrderScoreItemClick(View v, int position1,
					int position2, float rating, String str) {
				// TODO Auto-generated method stub
				
			}
		});
		twoGroupAdapter.setonOrderApplyRefundItemClickListener(new IOrderApplyRefundClickListen() {
			
			@Override
			public void onApplyRefundItemClick(View v, int position1, int position2,
					int flag) {
				// TODO Auto-generated method stub
				if(flag==0){
//					LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
//					RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
//							.inflate(R.layout.dialog_apply_refund, null);
//					Builder builder = new AlertDialog.Builder(getActivity());
//					builder.setView(relativeLayout);
//					final Dialog dialog = builder.create();
//					dialog.show();
//					TextView tv=(TextView) relativeLayout.findViewById(R.id.tv_content);
//					tv.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//						}
//					});
					String idOrder=groupOneList.get(position1).getIdNumber()+"";
					String orderNo= groupOneList.get(position1).getSecondlevelorderNo()+"";
					String idCommodity=groupOneList.get(position1).getOrderDetail().get(position2).getIdCommodity();
					String idLevel=groupOneList.get(position1).getOrderDetail().get(position2).getIdLevel();
					String price = String.format("%.2f",
									Double.valueOf(groupOneList.get(position1)
											.getOrderDetail().get(position2)
											.getBuynumber())
											* Double.valueOf(groupOneList
													.get(position1)
													.getOrderDetail()
													.get(position2)
													.getBuyprice()));
					refundApply(idOrder,orderNo, idCommodity,idLevel , price);
				}
				
			}
		});
		threeGroupAdapter = new OrderThreeGroupAdapter(groupOneList, getActivity());
		lv_three.setAdapter(threeGroupAdapter);

		threeGroupAdapter.setonOrderScoreItemClickListenerListener(new onOrderScoreItemClickListener() {

			@Override
			public void onOrderScoreItemClick(View v, int position1, int position2, float rating) {// 评分按钮
				// TODO Auto-generated method stub
//				scoreHttp(rating, position1, position2);
//				LogUtils.i(rating + "/////" + position1 + "/////" + position2);
			}

			@Override
			public void onOrderScoreItemClick(View v, int position, String str) {// //图标评价按钮
				// TODO Auto-generated method stub
				evaluateHttp(position, str);
			}

			@Override
			public void onOrderScoreItemClick(View v, int position1,
					int position2, float rating, String str) {
				// TODO Auto-generated method stub
				
					scoreHttp(rating, position1, position2,str);
				
				LogUtils.i(rating + "/////" + position1 + "/////" + position2);
			}
		});
		threeGroupAdapter.setonOrderApplyRefundItemClickListener(new IOrderApplyRefundClickListen() {
			
			@Override
			public void onApplyRefundItemClick(View v, int position1, int position2,
					int flag) {
				// TODO Auto-generated method stub
				if(flag==1){
					//在这里做退款处理-暂缺接口
//					LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
//					RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
//							.inflate(R.layout.dialog_apply_refund, null);
//					Builder builder = new AlertDialog.Builder(getActivity());
//					builder.setView(relativeLayout);
//					final Dialog dialog = builder.create();
//					dialog.show();
//					TextView tv=(TextView) relativeLayout.findViewById(R.id.tv_content);
//					tv.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//						}
//					});
//					refundApply(idOrder, orderNo, idCommodity, idLevel, price)
					String idOrder=groupOneList.get(position1).getIdNumber()+"";
					String orderNo= groupOneList.get(position1).getSecondlevelorderNo()+"";
					String idCommodity=groupOneList.get(position1).getOrderDetail().get(position2).getIdCommodity();
					String idLevel=groupOneList.get(position1).getOrderDetail().get(position2).getIdLevel();
					String price = String.format("%.2f",
									Double.valueOf(groupOneList.get(position1)
											.getOrderDetail().get(position2)
											.getBuynumber())
											* Double.valueOf(groupOneList
													.get(position1)
													.getOrderDetail()
													.get(position2)
													.getBuyprice()));
					refundApply(idOrder,orderNo, idCommodity,idLevel , price);
				}
				
			}
		});
		lv_one.setOnScrollListener(scrollListener);
		lv_two.setOnScrollListener(scrollListener);
		lv_three.setOnScrollListener(scrollListener);

	}

	/** 获取订单接口 */
	void orderHttp(int currentPage) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("isCut", isCut + "");
		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addBodyParameter("currentPage", currentPage + "");
//		
		doHttp(1, MyConst.allOrderAction, params, allOrderAction);
	}
	
	
	

	/** 评分按钮接口 */
	// http://121.46.0.219:8080/efreshapp/userScoreAction?idScore=1007&orderNo=1007&score=5&commodity=小白菜&idLevel=1008&idcommodity=1022
	void scoreHttp(float rating, int position1, int position2,String str) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
//		params.addBodyParameter("orderId", groupOneList.get(position1).getIdNumber()+"");
		params.addBodyParameter("orderNo", groupOneList.get(position1).getSecondlevelorderNo()+"");
		params.addBodyParameter("score", rating + "");
		params.addBodyParameter("commodity", groupOneList.get(position1).getOrderDetail().get(position2)
				.getCommodityname());
		params.addBodyParameter("idLevel", groupOneList.get(position1).getOrderDetail().get(position2).getIdLevel());
		params.addBodyParameter("idcommodity", groupOneList.get(position1).getOrderDetail().get(position2)
				.getIdCommodity());
		params.addBodyParameter("idStore", groupOneList.get(position1).getOrderDetail().get(position2)
				.getIdStore()+"");
		params.addBodyParameter("evaluatecontent", str);
		
		doHttp(1, MyConst.userScoreAction, params, score);

		// LogUtils.i(MyApplication.getInstance().getIdNumber()+"///"+groupOneList.get(position1).getSecondlevelorderNo()+"///"+rating+"///"+groupOneList.get(position1).getOrderDetail().get(position2).getCommodityname()
		// +"///"+groupOneList.get(position1).getOrderDetail().get(position2).getIdLevel()+"///"+groupOneList.get(position1).getOrderDetail().get(position2).getIdCommodity());
	}

	/** 图标按钮评价接口 */
	// http://121.46.0.219:8080/efreshapp/userEvaluateAction?idUser=1007&orderNo=1007&content=这是用户对此次订单的评价。
	void evaluateHttp(int position, String strContent) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
//		params.addBodyParameter("orderId", groupOneList.get(position).getIdNumber()+"");
		params.addBodyParameter("orderNo", groupOneList.get(position).getSecondlevelorderNo()+"");
		params.addBodyParameter("content", strContent);
		doHttp(1, MyConst.userEvaluateAction, params, evaluate);
	}

	/** 确认收货接口 */
	// http://121.46.0.219:8080/efreshapp/deliveryAction?secondlevelorderNo=20160218035144000002&idUser=1000
	void confirmGoodsAction(String secondlevelorderNo) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("secondlevelorderId", secondlevelorderNo);
		doHttp(1, MyConst.confirmGoodsAction, params, confirm);
	}
	
	/**取消订单接口**/
	void cancelOrder(int orderId,int orderStatus){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("idOrder", orderId+"");
		params.addQueryStringParameter("orderStatus", orderStatus+"");
		
		doHttp(1, MyConst.cancelOrder, params, cancelOrder);
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		Gson gson;
		switch (flag) {
		case allOrderAction:
			gson = new Gson();
			ArrayList<OrderGroupBean> orderGroupBeans = new ArrayList<OrderGroupBean>();
			orderGroupBeans = gson.fromJson(jsonString, new TypeToken<List<OrderGroupBean>>() {
			}.getType());
//            if(orderGroupBeans.size()==0&& cu){
//            	tv_msg_pay.setVisibility(View.VISIBLE);
//            }else{
//            	tv_msg_pay.setVisibility(View.GONE);
//            }
			if (orderGroupBeans.size() == 0) {
				if (isCut == 0) {
					isAll1 = true;
					oneGroupAdapter.notifyDataSetChanged();
					if(currentPage1==1){
						showToast("没有待付款订单！");
					}else{
						showToast("加载完毕");
					}
				} else if (isCut == 1) {
					isAll2 = true;
					twoGroupAdapter.notifyDataSetChanged();
					if(currentPage2==1){
						showToast("没有待收货订单！");
					}else{
						showToast("加载完毕");
					}
				} else if (isCut == 2) {
					isAll3 = true;
					threeGroupAdapter.notifyDataSetChanged();
					if(currentPage3==1){
						showToast("没有待评分订单！");
					}else{
						showToast("加载完毕");
					}
				}
				
				
				return;
			}
			for (int i = 0; i < orderGroupBeans.size(); i++) {
				groupOneList.add(orderGroupBeans.get(i));
			}
			if (isCut == 0) {
				oneGroupAdapter.updateFlag(orderGroupBeans.size());
				oneGroupAdapter.notifyDataSetChanged();
			} else if (isCut == 1) {
				twoGroupAdapter.updateFlag(orderGroupBeans.size());
				twoGroupAdapter.notifyDataSetChanged();
			} else if (isCut == 2) {
				threeGroupAdapter.updateFlag(orderGroupBeans.size());
				threeGroupAdapter.notifyDataSetChanged();
			}
			checkSP();
			break;

		case score:
			ShowToast("评分成功！！");
			groupOneList.clear();
			currentPage3 = 1;
			orderHttp(currentPage3);
			break;
		case evaluate:
			ShowToast("已成功评价此次购物，非常感谢您！！");
			groupOneList.clear();
			currentPage3 = 1;
			orderHttp(currentPage3);
			break;
		case confirm:
			confirmAction();
			
			break;
		case cancelOrder:
			groupOneList.clear();
			showToast("取消订单成功！");
			isCut = 0;
			currentPage1 = 1;
			orderHttp(currentPage1);
			break;
//		case paymentOrder:
//			ShowToast("订单支付完成！");
//			if (radioFlag == 2) {
//				payWordPopupWindows.dismiss();
//				MyApplication.getInstance().setBalance(""+(Double.valueOf(MyApplication.getInstance().getBalance())-totalMoney));
//			}
//			groupOneList.clear();
//			isCut=0;
//			currentPage1=1;
//			orderHttp(currentPage1);
//		
//		break;
		case refundApplyAction:
			LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
			RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
					.inflate(R.layout.dialog_apply_refund, null);
			Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(relativeLayout);
			final Dialog dialog = builder.create();
			dialog.show();
			TextView tv=(TextView) relativeLayout.findViewById(R.id.tv_content);
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					
				}
			});
			if(isCut==1){
				currentPage1=1;
				groupOneList.clear();
				orderHttp(currentPage1);
			}else{
				currentPage2=1;
				groupOneList.clear();
				orderHttp(currentPage2);
			}
			break;
		default:
			break;
		}
	}

	// 确认收货处理
	public void confirmAction() {
		ShowToast("您已确认收货成功，可以在待评分对商品进行评分哦~");
		cleanAll();
		tv_order_wait_grade.setBackgroundResource(R.drawable.bg_order_smal_slider);
		tv_order_wait_grade.setTextColor(Color.parseColor("#FDE000"));
		view_order_first_line.setVisibility(View.VISIBLE);
		view_order_first_line.setBackgroundColor(Color.parseColor("#FFFFFF"));
		lv_one.setVisibility(View.GONE);
		lv_two.setVisibility(View.GONE);
		lv_three.setVisibility(View.VISIBLE);

		isCut = 2;
		currentPage3 = 1;
		orderHttp(currentPage3);
	}

	class myScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			// 当不滚动时
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				// 判断是否滚动到底部
				if (view.getLastVisiblePosition() == view.getCount() - 1) {
					if (isCut == 0 && !isAll1) {
						currentPage1++;
						orderHttp(currentPage1);
					} else if (isCut == 1 && !isAll2) {
						currentPage2++;
						orderHttp(currentPage2);
					} else if (isCut == 2 && !isAll3) {
						currentPage3++;
						orderHttp(currentPage3);
					}
				}
			}
		}
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
				params.addBodyParameter("toKen",MyApplication.getInstance().getToKen());
				HttpUtils httpUtils=new HttpUtils();
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
								//密码验证正确后调余额支付接口，现缺一个余额支付的接口   
								DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date curDate1 = new Date(System.currentTimeMillis());// 获取当前时间
								RequestParams params=new RequestParams();
								params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber()+"");
								params.addBodyParameter("payPrice", totalMoney+"");
								params.addBodyParameter("idOrder",gson.toJson(orderIdList));
								params.addBodyParameter("paytime", curDate1+"");
								params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
								params.addBodyParameter("toKen",MyApplication.getInstance().getToKen());
								HttpUtils httpUtils=new HttpUtils();
								httpUtils.send(HttpMethod.POST, MyConst.balancePaidAction, params,new RequestCallBack<String>() {
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
//												showToast("支付订单成功");
												
//												oneGroupAdapter.notifyDataSetChanged();
												paypassword="";
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
								mList.clear();
								paypassword="";
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