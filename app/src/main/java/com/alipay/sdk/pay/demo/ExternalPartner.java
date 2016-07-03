package com.alipay.sdk.pay.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;


public class ExternalPartner {
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	public static final String TAG = "alipay-sdk";

	private static ExternalPartner externalPartner;

	private Context context = null;
	private String orderNO = null;
	private String orderName = null;
	private Handler mHandler = null;
	private String orderFEE = "";

	/**
	 * ֧�������֧��
	 * 
	 * @param context
	 *            ������
	 * @param orderNO
	 *            ������
	 * @param mHandler
	 *            �ص���Ӧ��� "9000", "�����ɹ�"; "4000", "ϵͳ�쳣" "4001","���ݸ�ʽ����ȷ"
	 *            "4003","���û��󶨵�֧�����˻������������֧��" "4004", "���û��ѽ����" "4005",
	 *            "��ʧ�ܻ�û�а�" "4006", "����֧��ʧ��" "4010","���°��˻�" "6000",
	 *            "֧���������ڽ�����������" "6001", "�û���;ȡ��֧������" "7001", "��ҳ֧��ʧ��"
	 * @param orderFEE
	 *            �۸�
	 * @param orderName
	 * 			      ��Ʒ��
	 */
	private ExternalPartner(Context context, String orderName, String orderNO,
			Handler mHandler, String orderFEE) {
		this.context = context;
		this.orderNO = orderNO;
		this.mHandler = mHandler;
		this.orderFEE = orderFEE;
		this.orderName = orderName;
	}

	/**
	 * ֧�������֧��������
	 * 
	 * @param context
	 *            ������
	 * @param orderNO
	 *            ������
	 * @param mHandler
	 *            �ص���Ӧ���
	 * @param orderFEE
	 *            �۸�
	 */
	public static ExternalPartner getInstance(Context context, String orderName, String orderNO,
			Handler mHandler, String orderFEE) {
		externalPartner = new ExternalPartner(context,orderName, orderNO, mHandler,
				orderFEE);
		return externalPartner;
	}

	/**
	 * ִ��֧������
	 */
	public void doOrder() {
		// ����
		String info = getNewOrderInfo(orderNO, orderFEE);
		// �Զ�����RSA ǩ��
		String sign = sign(info);
		sign = URLEncoder.encode(sign);
		info += "&sign=\"" + sign + "\"&" + getSignType();

		try {
			// �����sign ��URL����
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// �����ķ���֧���������淶�Ķ�����Ϣ
		final String payInfo = info;
		System.out.println("----->"+payInfo);
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask alipay = new PayTask((Activity) context);
				// ����֧���ӿڣ���ȡ֧�����
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// �����첽����
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	private String getNewOrderInfo(String ordeNO, String orderFEE) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(ordeNO);
		sb.append("\"&subject=\"");
		sb.append(orderName);
		sb.append("\"&body=\"");
		sb.append("body");
		sb.append("\"&total_fee=\"");
		sb.append(orderFEE);
		sb.append("\"&notify_url=\"");

		// ��ַ��Ҫ��URL����
		sb.append(URLEncoder.encode("http://qa.memorieslab.com/callback/mobileAlipayReturn"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// ���show_urlֵΪ�գ��ɲ���
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/**
	 * sign the order info. �Զ�����Ϣ����ǩ��
	 * 
	 * @param content
	 *            ��ǩ��������Ϣ
	 */
	public String sign(String content) {
		return SignUtils.sign(content, Keys.PRIVATE);
	}

	public static class Product {
		public String subject;
		public String body;
		public String price;
	}

	public static Product[] sProducts;

	/**
	 * check whether the device has authentication alipay account.
	 * ��ѯ�ն��豸�Ƿ����֧������֤�˻�
	 * 
	 */
	public void check() {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask payTask = new PayTask((Activity) context);
				// ���ò�ѯ�ӿڣ���ȡ��ѯ���
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}
}