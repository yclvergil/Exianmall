package com.cnmobi.exianmall.mineset.activity;

import java.util.ArrayList;


import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.mineset.activity.KeyboardEnum.ActionEnum;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 设置支付密码界面
 */
public class MineSetPayPasswordActivity extends BaseActivity {
	
	@ViewInject(R.id.tv_title_content)
	TextView tv_title_content;
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
	
	private boolean isFirst = true;//跳转标记
	private String firstPaypassword = null;
	private ArrayList<String> mList = new ArrayList<String>();
	
	/**
	 * 设置支付密码接口标识
	 */
	public static final int setPayPwd = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_set_paypassword);
		ViewUtils.inject(this);

		setTitleText("设置支付密码");
		
	}
	
	 Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				tv_title_content.setText("请再次确认支付密码");
				isFirst = false;
			}
		};
	};
	
	@OnClick({ R.id.pay_keyboard_one, R.id.pay_keyboard_two,
			R.id.pay_keyboard_three, R.id.pay_keyboard_four,
			R.id.pay_keyboard_five, R.id.pay_keyboard_six,
			R.id.pay_keyboard_seven, R.id.pay_keyboard_eight,
			R.id.pay_keyboard_nine, R.id.pay_keyboard_zero,
			R.id.pay_keyboard_del })
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

			default:
				break;
			}
		
	}
	
	void setPayPwd(String payPwd,String confirmPayPwd){
		RequestParams params=new RequestParams();
//		params.addQueryStringParameter("idUser", String.valueOf(MyApplication.getInstance().getIdNumber()));
		params.addQueryStringParameter("payPwd",payPwd);
		params.addQueryStringParameter("confirmPayPwd",confirmPayPwd);
		doHttp(1, MyConst.setUpPayPwdAction, params, setPayPwd);
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
		case setPayPwd:
			showToast("设置支付密码成功！");
			MyApplication.getInstance().setIsSetpassword("Y");
			finish();
			break;

		default:
			break;
		}
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
			String paypassword = "";// 输入的密码
			for (int i = 0; i < mList.size(); i++) {
				paypassword += mList.get(i);

			}
			if (isFirst) {
				mList.clear();
				updateUi();
				firstPaypassword = paypassword;
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("firstPaypassword", firstPaypassword);
				msg.setData(bundle);
				msg.what = 1;
				handler.sendMessage(msg);
			} else 
				if (paypassword.equals(firstPaypassword)) {
					setPayPwd(firstPaypassword, paypassword);
				} else {
					mList.clear();
					updateUi();
					Toast.makeText(MineSetPayPasswordActivity.this,"2次输入的密码不一致，请重新输入", Toast.LENGTH_LONG).show();
				}
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
