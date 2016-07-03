package com.cnmobi.exianmall.wxapi;

import java.util.Random;

import net.sourceforge.simcpux.MD5;
import net.sourceforge.simcpux.WxPayUtile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.mine.activity.PreChargeActivity;
import com.cnmobi.exianmall.type.activity.SingleOrderActivity;
import com.lidroid.xutils.util.LogUtils;

public class PayActivity extends Activity {

	TextView show;
	public static final int PREChARGE_RESULTCODE = 0;
	public static final int SUBMIT_ORDER_RESULTCODE = 1;
	public static final int WAIT_PAY_RESULTCODE = 2;
	private static final int PAY_RESULT_SUCCESS=0;
	private static final int PAY_RESULT_FAIL=-1;
	private static final int PAY_RESULT_CANCEL=-2;
	private static final int PAY_RESULT_ORDER_ERROR=800;
	double price=0;
	static Activity context;
	static String whichActivity="";//0=充值页面，1=提交订单页面，2=待付款页面
	String orderId="";
	public static Handler handler = new Handler(new Callback() {
		
//		msg.what== 0 ：表示支付成功
//		msg.what== -1 ：表示支付失败
//		msg.what== -2 ：表示取消支付
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			
			case 800://商户订单号重复或生成错误
				if ("0".equals(whichActivity)) {
					Intent intent = new Intent(context, PreChargeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_ORDER_ERROR+ "");
					intent.putExtras(bundle);
					context.setResult(PREChARGE_RESULTCODE, intent);
					context.finish();
				} else if ("1".equals(whichActivity)) {
					Intent intent = new Intent(context,SingleOrderActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_ORDER_ERROR	+ "");
					intent.putExtras(bundle);
					context.setResult(SUBMIT_ORDER_RESULTCODE,intent);
					context.finish();
				} else if ("2".equals(whichActivity)) {
					Intent intent = new Intent(context, MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_ORDER_ERROR+ "");
					intent.putExtras(bundle);
					context.setResult(WAIT_PAY_RESULTCODE, intent);
					context.finish();
				}
				break;
			case 0://支付成功
				if ("0".equals(whichActivity)) {
					Intent intent = new Intent(context, PreChargeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_SUCCESS+ "");
					intent.putExtras(bundle);
					context.setResult(PREChARGE_RESULTCODE, intent);
					context.finish();
				} else if ("1".equals(whichActivity)) {
					Intent intent = new Intent(context,SingleOrderActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_SUCCESS	+ "");
					intent.putExtras(bundle);
					 context.setResult(SUBMIT_ORDER_RESULTCODE,intent);
					context.finish();
				} else if ("2".equals(whichActivity)) {
					Intent intent = new Intent(context, MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_SUCCESS+ "");
					intent.putExtras(bundle);
					context.setResult(WAIT_PAY_RESULTCODE, intent);
					context.finish();
				}
				break;
			case -1://支付失败
				if ("0".equals(whichActivity)) {
					Intent intent = new Intent(context, PreChargeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_FAIL+ "");
					intent.putExtras(bundle);
					context.setResult(PREChARGE_RESULTCODE, intent);
					context.finish();
				} else if ("1".equals(whichActivity)) {
					Intent intent = new Intent(context,SingleOrderActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_FAIL	+ "");
					intent.putExtras(bundle);
					context.setResult(SUBMIT_ORDER_RESULTCODE,intent);
					context.finish();
				} else if ("2".equals(whichActivity)) {
					Intent intent = new Intent(context, MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_FAIL+ "");
					intent.putExtras(bundle);
					context.setResult(WAIT_PAY_RESULTCODE, intent);
					context.finish();
				}
				break;
			case -2://取消支付
				if ("0".equals(whichActivity)) {
					Intent intent = new Intent(context, PreChargeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_CANCEL+ "");
					intent.putExtras(bundle);
					context.setResult(PREChARGE_RESULTCODE, intent);
					context.finish();
				} else if ("1".equals(whichActivity)) {
					Intent intent = new Intent(context,SingleOrderActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_CANCEL	+ "");
					intent.putExtras(bundle);
					context.setResult(SUBMIT_ORDER_RESULTCODE,intent);
					context.finish();
				} else if ("2".equals(whichActivity)) {
					Intent intent = new Intent(context, MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payResultCode", PAY_RESULT_CANCEL+ "");
					intent.putExtras(bundle);
					context.setResult(WAIT_PAY_RESULTCODE, intent);
					context.finish();
				}
				break;
			default:
				break;
			}
			return false;
		}
	});

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pay);
//		show = (TextView) findViewById(R.id.editText_prepay_id);
		context=PayActivity.this;
		price=Double.parseDouble(getIntent().getStringExtra("price"));
		whichActivity=getIntent().getStringExtra("whichActivity");
		orderId=getIntent().getStringExtra("orderId");
		// 生成prepay_id
		String orderPrice=(int)(Double.parseDouble(String.format("%.2f", price))*100)+"";//转化下格式，微信支付金额单位为分
//		LogUtils.e("传的金额是:"+orderPrice+";whichActivity："+whichActivity);
		WxPayUtile.getInstance(PayActivity.this, orderPrice,
				"http://121.40.35.3/test", "支付",orderId).doPay();
//		context=(Activity) getApplicationContext();

	}
	
	public static String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
}
