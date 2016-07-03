package com.cnmobi.exianmall.mine.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.pay.demo.ExternalPartner;
import com.alipay.sdk.pay.demo.Keys;
import com.alipay.sdk.pay.demo.PayResult;
import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.PreChargeOrder;
import com.cnmobi.exianmall.utils.ConstontUtils;
import com.cnmobi.exianmall.utils.EditInputFilter;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 预充值页面
 * 
 */
public class PreChargeActivity extends BaseActivity {
	@ViewInject(R.id.radio_weixin)
	RadioButton radio_weixin;
	@ViewInject(R.id.radio_zhifubao)
	RadioButton radio_zhifubao;
	@ViewInject(R.id.edt_money)
	EditText edt_money;
	@ViewInject(R.id.ly_pre_charge)
	LinearLayout ly_pre_charge;
	@ViewInject(R.id.textView1)
	public static TextView textView1;
	// 商户PID
	public static final String PARTNER = Keys.DEFAULT_PARTNER;
	// 商户收款账号
	public static final String SELLER = Keys.DEFAULT_SELLER;
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = Keys.PRIVATE;
	// 支付宝公钥
	public static final String RSA_PUBLIC = Keys.PUBLIC;

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	
	private static final int PREChARGE_REQUESTCODE = 1;
	private int payFlag = 0;// 充值方式，1为支付宝，2为微信

	private String orderid = "";// 订单号

	/** 充值接口标识 */
	public static final int rechargeAction = 0;
	/** 保存充值信息接口标识 */
	public static final int rechargeRecordAction = 1;
	
	private Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_pre_charge);
		setTitleText("预充值");
		ViewUtils.inject(this);
		checkSP();
		InputFilter[] filters = { new EditInputFilter() };
		edt_money.setFilters(filters);
		setRadio();
	}
	
	void saveRechargeInfo(String tradingNo,String payWay){
		RequestParams params = new RequestParams();
		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addQueryStringParameter("phone", MyApplication.getInstance().getPhone());
		params.addQueryStringParameter("rechargeamount",getStr(edt_money));
		params.addQueryStringParameter("tradingNo", tradingNo);
		params.addQueryStringParameter("payway", payWay);
		params.addQueryStringParameter("rechargetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "");
		doHttp(0, MyConst.rechargeRecordAction, params, rechargeRecordAction,false);
	}
	
	void checkSP(){
		SharedPreferences sharedPreferences = getSharedPreferences("orderInfoSP",Context.MODE_PRIVATE + Context.MODE_PRIVATE);
		String jsonString=sharedPreferences.getString("orderInfoSP", "[]");
		LogUtils.i("checkSP"+jsonString);
		List<PreChargeOrder> list=new Gson().fromJson(jsonString,new TypeToken<List<PreChargeOrder>>() {
		}.getType());
		for(int i=0;i<list.size();i++){
			if(!list.get(i).isSuccess()){
				//该次充值完成支付，没有成功，重新提交
				rechargeActionHttp(list.get(i).getAmount(),list.get(i).getTradingtime(),list.get(i).getTradingNo());
			}
		}
	}
	
	void setRadio() {
		radio_weixin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					radio_zhifubao.setChecked(false);
					payFlag = 2;
				}
			}
		});
		radio_zhifubao.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					if (ConstontUtils.checkAPP(PreChargeActivity.this, "com.eg.android.AlipayGphone")) {
						radio_weixin.setChecked(false);
						payFlag = 1;
					} else {
						radio_zhifubao.setChecked(false);
						showToast("你手机没有安装支付宝，不能选择支付宝充值");
					}
				}
			}
		});
	}
	
	@OnClick(R.id.ly_pre_charge)
	public void gotoCharge(View view) {
		if (isNull(edt_money)) {
			showToast("请输入充值金额");
			
		} else if(Double.parseDouble(getStr(edt_money))==0){
			showToast("充值金额不能为0");
		}else {
			
			if (payFlag == 1) {
				orderid = UUID.randomUUID().toString();// 随机生成订单号
				saveRechargeInfo(orderid,0+"");
			} else if (payFlag == 2) {
				orderid= PayActivity.genOutTradNo();
				saveRechargeInfo(orderid,1+"");
			} else {
				showToast("请选择充值方式");
			}
		}

	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		msg.what== 0 ：表示支付成功
//				msg.what== -1 ：表示支付失败
//				msg.what== -2 ：表示取消支付
		if(requestCode==PREChARGE_REQUESTCODE&& resultCode==PayActivity.PREChARGE_RESULTCODE &&data!=null){
			if("800".equals(data.getExtras())){
				Toast.makeText(PreChargeActivity.this, "商户订单号重复或生成错误", Toast.LENGTH_SHORT).show();
			}else if("0".equals(data.getExtras().getString("payResultCode"))){
				Toast.makeText(PreChargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				rechargeActionHttp(getStr(edt_money),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "",orderid);
			}else if("-1".equals(data.getExtras().getString("payResultCode"))){
				Toast.makeText(PreChargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
			}else if("-2".equals(data.getExtras().getString("payResultCode"))){
				Toast.makeText(PreChargeActivity.this, "取消支付", Toast.LENGTH_SHORT).show();
			}
		}
	}
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: 
				PayResult payResult = new PayResult((String) msg.obj);
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PreChargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
					rechargeActionHttp(getStr(edt_money),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "",orderid);
				} else {
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PreChargeActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(PreChargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
						LogUtils.i(resultInfo);
					}
				}
				break;
			
			case SDK_CHECK_FLAG:
				Toast.makeText(PreChargeActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;
			
			default:
				break;
			}
		};
	};

	
	
	// 充值接口
	void rechargeActionHttp(final String amount,String tradingtime,final String tradingNo) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addBodyParameter("idAccount", getIntent().getStringExtra("idAccount"));
		LogUtils.i("----amount-----"+amount);
//		params.addBodyParameter("idAccount", "100010");
		params.addBodyParameter("amount", amount);
		params.addBodyParameter("idPayway", "1000");
		params.addBodyParameter("tradingtype", "0");
		params.addBodyParameter("tradingtime", tradingtime);
		params.addBodyParameter("tradingNo", tradingNo);
		params.addBodyParameter("note", "充值");
		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addQueryStringParameter("toKen",MyApplication.getInstance().getToKen());
		HttpUtils httpUtils=new HttpUtils();
	
		httpUtils.send(HttpMethod.POST, MyConst.rechargeAction, params, new RequestCallBack<String>() {

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
						if (jsonObject.has("response")) {
							showDialog(jsonObject.getString("response"),amount);
							setResult(1);
							deleteSpOrder(tradingNo);
//							LogUtils.i("just test!");
//							saveSp();
						} 
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
		
		
		
//		doHttp(1, MyConst.rechargeAction, params, rechargeAction, false);
	}

	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		switch (flag) {
//		case rechargeAction:
//			showDialog(jsonString);
//			setResult(1);
//			break;
		case rechargeRecordAction:
			if(payFlag==1){
				ExternalPartner.getInstance(PreChargeActivity.this, "充值", orderid, mHandler, getStr(edt_money))
				.doOrder();
			}else if(payFlag==2){
				Intent intent=new Intent(PreChargeActivity.this,PayActivity.class);
				intent.putExtra("price",getStr(edt_money));
				intent.putExtra("whichActivity", "0");
				intent.putExtra("orderId", orderid);
				startActivityForResult(intent, PREChARGE_REQUESTCODE);
			}
			break;
		
		default:
			break;
		}
	}
	
	/**
	 * 弹出红包对话框
	 */
	void showDialog(final String idTradingrecords,final String amout) {
		LogUtils.i("红包参数"+idTradingrecords);
		View view = View.inflate(PreChargeActivity.this, R.layout.popupwindow_redpackage, null);
		final Dialog dialog = new Dialog(PreChargeActivity.this, R.style.testDialog);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
		final ImageView iv_redpackage_bottom = (ImageView) view.findViewById(R.id.iv_redpackage_bottom1);
		final ImageView iv_redpackage_top = (ImageView) view.findViewById(R.id.iv_redpackage_top1);
		final TextView tv_redpackage_top_close = (TextView) view.findViewById(R.id.tv_redpackage_top_close1);
		final TextView tv_redpackage_bottom_close = (TextView) view.findViewById(R.id.tv_redpackage_bottom_close1);
		final TextView tv_redpackage_title = (TextView) view.findViewById(R.id.tv_redpackage_title1);
		final TextView tv_redpackage_content = (TextView) view.findViewById(R.id.tv_redpackage_content1);
		tv_redpackage_top_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				finish();
			}
		});
		tv_redpackage_bottom_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				finish();
			}
		});
		tv_redpackage_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LogUtils.i("点击了呀");
				RequestParams params = new RequestParams();
				params.addBodyParameter("rechargeamount", amout);
				params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
				params.addBodyParameter("idAccount",  getIntent().getStringExtra("idAccount"));
				params.addBodyParameter("idTradingrecords", idTradingrecords);
				params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
				params.addBodyParameter("toKen",MyApplication.getInstance().getToKen());
				HttpUtils httpUtils=new HttpUtils();
//				httpUtils.configCookieStore(MyCookieStore.cookieStore);
				httpUtils.send(HttpMethod.POST, MyConst.rebateRedAction, params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						showToast(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							LogUtils.i("点击了呀成功了"+arg0.result);
							JSONObject jsonObject = new JSONObject(arg0.result);
							iv_redpackage_bottom.setImageResource(R.drawable.bg_redpackage_bottom);
							iv_redpackage_top.setVisibility(View.VISIBLE);
							tv_redpackage_top_close.setVisibility(View.VISIBLE);
							tv_redpackage_bottom_close.setVisibility(View.GONE);
							tv_redpackage_title.setText("随机红包返利");
							tv_redpackage_content.setText("¥" + jsonObject.getDouble("response") + "元");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						tv_redpackage_title.setClickable(false);
					}
				});
				
			}
		});
	}

	boolean checkOrderSP(List<PreChargeOrder> list,String orderNo){
		boolean result=false;
		for(int i=0;i<list.size();i++){
			if(orderNo.equals(list.get(i).getTradingNo())){
				result=true;
				break;
			}
		}
		return result;
	}
	
	void saveSp(){
		SharedPreferences sharedPreferences = getSharedPreferences("orderInfoSP",Context.MODE_PRIVATE + Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit(); // 获取编辑器
		String jsonString=sharedPreferences.getString("orderInfoSP", "[]");
		LogUtils.i("saveSp"+jsonString);
		//可能有存在多次支付完成，充值接口失败或无法访问的情况，把订单add到list，以 json格式保存到sp
		List<PreChargeOrder> list=new Gson().fromJson(jsonString,new TypeToken<List<PreChargeOrder>>() {
		}.getType());
		PreChargeOrder chargeOrder=new PreChargeOrder();
		chargeOrder.setAmount(getStr(edt_money));
		chargeOrder.setSuccess(false);
		chargeOrder.setTradingNo(orderid);
		chargeOrder.setTradingtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "");
//		editor.putString("orderInfo", "");
		if(!checkOrderSP(list, orderid)){
			list.add(chargeOrder);
			editor.putString("orderInfoSP", new Gson().toJson(list));
			editor.commit();
		}
	}
	
	void deleteSpOrder(String orderNo){
		SharedPreferences sharedPreferences = getSharedPreferences("orderInfoSP",Context.MODE_PRIVATE + Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit(); // 获取编辑器
		String jsonString=sharedPreferences.getString("orderInfoSP", "[]");
		LogUtils.i("deleteSpOrder"+jsonString);
		List<PreChargeOrder> list=new Gson().fromJson(jsonString,new TypeToken<List<PreChargeOrder>>() {
		}.getType());
		
		for(int i=0;i<list.size();i++){
			if(orderNo.equals(list.get(i).getTradingNo())){
				list.remove(i);
				--i;
			}
		}
		editor.putString("orderInfoSP", new Gson().toJson(list));
		editor.commit();
	}
}
