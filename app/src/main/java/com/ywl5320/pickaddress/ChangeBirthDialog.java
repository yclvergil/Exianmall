package com.ywl5320.pickaddress;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.lidroid.xutils.util.LogUtils;
import com.ywl5320.pickaddress.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.ywl5320.pickaddress.wheel.widget.views.OnWheelChangedListener;
import com.ywl5320.pickaddress.wheel.widget.views.OnWheelScrollListener;
import com.ywl5320.pickaddress.wheel.widget.views.WheelView;

/**
 * 日期选择对话框
 * 
 * @author ywl
 * 
 */
public class ChangeBirthDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	private WheelView wvDay;
	private WheelView wvHour;
	private WheelView wvMinute;

	private View vChangeBirthChild;
	private TextView btnSure;
	private TextView btnCancel;

	private ArrayList<String> arry_days = new ArrayList<String>();
	private ArrayList<String> arry_hours = new ArrayList<String>();
	private ArrayList<String> arry_minutes = new ArrayList<String>();
	private CalendarTextAdapter mDaydapter;
	private CalendarTextAdapter mHourdapter;
	private CalendarTextAdapter mMinutedapter;

	private int month;
	private int day;
	private int hour;
	private int minute;

	private int currentYear = getYear();//当前年份
	private int currentMonth = getMonth();//当前月份
	private int currentDay = getDay();//当前日期
	private int currentHour = getHour();//当前小时
	private int currentMinute = getMinute();//当前分钟

	private int maxTextSize = 18;//最大字体大小
	private int minTextSize = 12;//最小字体大小


	private String selectYear = getYear() + "";//当前选择的年份
	private String selectMonth = getMonth() + "";//当前选择的月份
	private String selectDay = DateFormat.format("MM.dd EEEE",
	Calendar.getInstance().getTimeInMillis()+24*60*60*1000)+"";//当前选择的日期
//	private String selectDay = getDay() + "";

	private String selectHour = getHour() + "";//当前选择的小时
	private String selectMinute = getMinute() + "";//当前选择的分钟

	private OnBirthListener onBirthListener;

	public ChangeBirthDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changebirth);
		wvDay = (WheelView) findViewById(R.id.wv_birth_day);
		wvHour = (WheelView) findViewById(R.id.wv_birth_hour);
		wvMinute = (WheelView) findViewById(R.id.wv_birth_minute);

		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		initDays();
		mDaydapter = new CalendarTextAdapter(context, arry_days,
				0, maxTextSize, minTextSize);
		wvDay.setVisibleItems(5);
		wvDay.setViewAdapter(mDaydapter);
		wvDay.setCyclic(true);// 设置是否循环滑动
		wvDay.setCurrentItem(0);
		setHours();
		mHourdapter = new CalendarTextAdapter(context, arry_hours,
				currentHour, maxTextSize, minTextSize);
		wvHour.setVisibleItems(5);//设置可见条目数
		wvHour.setViewAdapter(mHourdapter);
		wvHour.setCyclic(true);// 设置是否循环滑动
		wvHour.setCurrentItem(currentHour);

		initMinutes();
		mMinutedapter = new CalendarTextAdapter(context, arry_minutes,
				currentMinute, maxTextSize, minTextSize);
		wvMinute.setVisibleItems(5);
		wvMinute.setViewAdapter(mMinutedapter);
		wvMinute.setCyclic(true);// 设置是否循环滑动
		wvMinute.setCurrentItem(currentMinute);

		wvDay.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mDaydapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mDaydapter);
				selectDay = currentText;
//				mHourdapter.notify();跟新Adapter暂时没有方法
				if(selectDay.equals(DateFormat.format("MM.dd EEEE",
						Calendar.getInstance().getTimeInMillis()+24*60*60*1000)+"")){
					//如果日期是当天 应该限制时间
					if(Integer.parseInt(selectHour)<getHour()){
						//如果选择时间小于当前时间 应该滚动小时到当前时间
						wvHour.setCurrentItem(getHour());
						wvMinute.setCurrentItem(getMinute());
					}
				}
			}
		});

		wvDay.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mDaydapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mDaydapter);
				selectDay = currentText;
			}
		});
		wvHour.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mHourdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mHourdapter);
				selectHour = currentText;
				if(selectDay.equals(DateFormat.format("MM.dd EEEE",
						Calendar.getInstance().getTimeInMillis()+24*60*60*1000)+"")){
					//如果日期是当天 应该限制时间
					if(Integer.parseInt(selectHour)<getHour()){
						//如果时间比当前时间小。则滑动到当前时间
						wvHour.setCurrentItem(getHour());
					}
				}
				
			}
		});

		wvHour.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mHourdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mHourdapter);
			}
		});
		wvMinute.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mMinutedapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mMinutedapter);
				selectMinute = currentText;

				if(selectDay.equals(DateFormat.format("MM.dd EEEE",
						Calendar.getInstance().getTimeInMillis()+24*60*60*1000)+"")){
					//如果日期是当天 应该限制时间
					if(selectHour.equals(getHour()+"")){
						//如果选择时间等于当前时间
						wvMinute.setCurrentItem(getMinute());
					}
				}
			}
		});

		wvMinute.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mMinutedapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mMinutedapter);
			}
		});

	}
	public void initDays() {
		arry_days.clear();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(cal.getTimeInMillis()+24*60*60*1000);
		// cal.add(Calendar.DAY_OF_YEAR, -7 / 2 - 1);
		cal.add(Calendar.DAY_OF_YEAR, 0);
		arry_days.add((String) DateFormat.format("MM.dd EEEE", cal));
		for (int i = 0; i < 6; ++i) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			arry_days.add((String) DateFormat.format("MM.dd EEEE", cal));
		}
	}

	public void setHours() {
		arry_hours.clear();
			for (int i = 0; i < 10; i++) {
				arry_hours.add("0"+i);
			}
			for (int i = 10; i < 24; i++) {
				arry_hours.add(""+i);
			}
	}

	public void initMinutes() {
		if (arry_minutes.size() == 0) {
			for (int i = 0; i<10; i++) {
				arry_minutes.add("0"+i);
			}
			for (int i = 10; i<60; i++) {
				arry_minutes.add(""+i);
			}
		}
	}

	private class CalendarTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected CalendarTextAdapter(Context context, ArrayList<String> list,
				int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	public void setBirthdayListener(OnBirthListener onBirthListener) {
		this.onBirthListener = onBirthListener;
	}

	@Override
	public void onClick(View v) {

		if (v == btnSure) {
			if (onBirthListener != null) {
				onBirthListener.onClick(selectYear, selectMonth, selectDay,
						selectHour, selectMinute);
			}
		} else if (v == btnSure) {

		} else if (v == vChangeBirthChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();

	}

	public interface OnBirthListener {
		public void onClick(String year, String month, String day, String hour,
				String minute);
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText,
			CalendarTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxTextSize);
			} else {
				textvew.setTextSize(minTextSize);
			}
		}
	}

	public int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

	public int getDay() {
		Calendar c = Calendar.getInstance();
//		return c.get(Calendar.DAY_OF_MONTH);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	public int getWeek() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DAY_OF_WEEK);
	}

	public int getHour() {
//		Calendar c = Calendar.getInstance();
//		return c.get(Calendar.HOUR_OF_DAY)+1;
		return 6;
	}

	public int getMinute() {
//		Calendar c = Calendar.getInstance();
//		return c.get(Calendar.MINUTE);
		return 0;
	}
}