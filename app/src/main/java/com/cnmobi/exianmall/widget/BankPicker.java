package com.cnmobi.exianmall.widget;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.widget.ScrollerNumberPicker.OnSelectListener;

/**
 * 银行Picker
 */
public class BankPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker bankPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempBankIndex = -1;
	private Context context;
	private String city_string;
	
	private ArrayList<String> bankList=new ArrayList<String>();
	
	public void init(){
		for (int i = 0; i < 20; i++) {
			bankList.add("**银行"+i);
		}
	}
	public BankPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		// TODO Auto-generated constructor stub
	}

	public BankPicker(Context context) {
		super(context);
		this.context = context;
//		getaddressinfo();
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.bank_picker, this);
//		citycodeUtil = CitycodeUtil.getSingleton();
		// 获取控件引用
		bankPicker = (ScrollerNumberPicker) findViewById(R.id.bank);
		init();
		
		bankPicker.setData(bankList);
		bankPicker.setDefault(1);
		bankPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				if (text.equals("") || text == null)
					return;
				if (tempBankIndex != id) {
					System.out.println("endselect");
					String selectDay = bankPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					int lastDay = Integer.valueOf(bankPicker.getListSize());
					if (id > lastDay) {
						bankPicker.setDefault(lastDay - 1);
					}
				}
				tempBankIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
				
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub
			}
		});

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public String getBank_string() {
		city_string = bankPicker.getSelectedText();
		return city_string;
	}

	public interface OnSelectingListener {
		public void selected(boolean selected);
	}
}
