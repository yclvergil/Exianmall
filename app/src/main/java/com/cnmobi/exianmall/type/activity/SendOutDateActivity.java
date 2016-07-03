package com.cnmobi.exianmall.type.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.utils.CalendarViewBuilder;
import com.cnmobi.exianmall.utils.CustomDate;
import com.cnmobi.exianmall.widget.CalendarView;
import com.cnmobi.exianmall.widget.CalendarView.CallBack;
import com.lidroid.xutils.util.LogUtils;

public class SendOutDateActivity extends BaseActivity implements CallBack {

	private ViewPager viewPager;
	private CalendarView[] views;
	private CalendarViewBuilder builder = new CalendarViewBuilder();
	private TextView showYearView;
	private TextView showMonthView;
	private TextView showWeekView;

	private final int DATE_DIALOG = 1;
	private final int TIME_DIALOG = 2;
	private int h;
	private int m;
	Calendar calendar = Calendar.getInstance();
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private Boolean flag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_sendout_time);
		
		setTitleText("价格日历");
		initUI();
		LogUtils.i(calendar.get(Calendar.MONTH) + 1+"");
	}

	private void initUI() {
		// TODO Auto-generated method stub
		h = calendar.get(Calendar.HOUR_OF_DAY);
		m = calendar.get(Calendar.MINUTE);
		// 初始化控件
		showMonthView = (TextView) this.findViewById(R.id.show_month_view);
		showYearView = (TextView) this.findViewById(R.id.show_year_view);
		// showWeekView = (TextView) this.findViewById(R.id.show_week_view);
		viewPager = (ViewPager) this.findViewById(R.id.viewpager2);
		views = builder.createMassCalendarViews(this, 5, this);
		// 产生多个CalendarView
		this.findViewById(R.id.contentPager2);
		// 设置控件
		CustomViewPagerAdapter<CalendarView> viewPagerAdapter = new CustomViewPagerAdapter<CalendarView>(
				views);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(498); // views可翻动498页
		viewPager.setOnPageChangeListener(new CalendarViewPagerLisenter(
				viewPagerAdapter));

	}

	public void setShowDateViewText(int year, int month) {
		showYearView.setText(year + "");
		showMonthView.setText(month + "月");
		// showWeekView.setText(DateUtil.weekName[DateUtil.getWeekDay() - 1]);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void clickDate(CustomDate date) {
//		if("time1".equals(str)){
//			dialog(date);
//		}else{
//			showToast("请注意选择日期的价格变动");
			StringBuilder time=new StringBuilder().append(date.year)
					.append("-").append(date.month).append("-")
					.append(date.day).append(" ");
			Intent intent =new Intent();
			intent.putExtra("time",time.toString());
			setResult(101, intent);
			SendOutDateActivity.this.finish();
//		}
	}

	protected void dialog(final CustomDate date) {
		final Calendar calendar = Calendar.getInstance();
		final LinearLayout layout = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.dialog_sendout_time, null);
		// 对话框
		final Builder mBuilder = new AlertDialog.Builder(this);
		mBuilder.setView(layout);// 对话框绑定布局

		final TimePicker timePicker = (TimePicker) layout
				.findViewById(R.id.timePicker1);
		final TextView textView = (TextView) layout.findViewById(R.id.content);
		textView.setText(new StringBuilder().append(date.year).append("-")
				.append(date.month).append("-").append(date.day).append(" ")
				.append(timePicker.getCurrentHour()).append(":")
				.append(timePicker.getCurrentMinute()).append(" "));
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {//时间选择器监听

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				if(date.year==calendar.get(Calendar.YEAR) && date.month==calendar.get(Calendar.MONTH)+1 && date.day==calendar.get(Calendar.DAY_OF_MONTH) && timePicker.getCurrentHour()<calendar.get(Calendar.HOUR_OF_DAY)){
					Toast.makeText(getApplicationContext(), "请选择大于当前时间", 0).show();
				}
				else{
					textView.setText(new StringBuilder().append(date.year)
							.append("-").append(date.month).append("-")
							.append(date.day).append(" ")
							.append(timePicker.getCurrentHour()).append(":")
							.append(timePicker.getCurrentMinute()).append(""));
				}

			}
		});

		mBuilder.setTitle(
				new StringBuilder().append(date.year).append("-")
						.append(date.month).append("-").append(date.day)
						.append(" ")).setView(layout)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String time=textView.getText().toString().trim();
						Intent intent =new Intent();
						intent.putExtra("time",time);
						setResult(100, intent);
						SendOutDateActivity.this.finish();
					}
				}).setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		mBuilder.show();
	}


	@Override
	public void onMesureCellHeight(int cellSpace) {

	}

	@Override
	public void changeDate(CustomDate date) {
		setShowDateViewText(date.year, date.month);
	}

}

/**
 * 监听器
 * 
 */
class CalendarViewPagerLisenter implements OnPageChangeListener {

	private SildeDirection mDirection = SildeDirection.NO_SILDE;
	int mCurrIndex = 498;
	private CalendarView[] mShowViews;

	public CalendarViewPagerLisenter(
			CustomViewPagerAdapter<CalendarView> viewAdapter) {
		super();
		this.mShowViews = viewAdapter.getAllItems();
	}

	@Override
	public void onPageSelected(int arg0) {
		measureDirection(arg0);
		updateCalendarView(arg0);
	}

	private void updateCalendarView(int arg0) {
		if (mDirection == SildeDirection.RIGHT) {
			mShowViews[arg0 % mShowViews.length].rightSilde();
		} else if (mDirection == SildeDirection.LEFT) {
			mShowViews[arg0 % mShowViews.length].leftSilde();
		}
		mDirection = SildeDirection.NO_SILDE;
	}

	/**
	 * 判断滑动方向
	 * 
	 * @param arg0
	 */
	private void measureDirection(int arg0) {

		if (arg0 > mCurrIndex) {
			mDirection = SildeDirection.RIGHT;

		} else if (arg0 < mCurrIndex) {
			mDirection = SildeDirection.LEFT;
		}
		mCurrIndex = arg0;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	enum SildeDirection {
		RIGHT, LEFT, NO_SILDE;
	}
}

/**
 * 适配器
 * 
 */
class CustomViewPagerAdapter<V extends View> extends PagerAdapter {

	private V[] views;

	public CustomViewPagerAdapter(V[] views) {
		super();
		this.views = views;
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		if (((ViewPager) arg0).getChildCount() == views.length) {
			((ViewPager) arg0).removeView(views[arg1 % views.length]);
		}
		((ViewPager) arg0).addView(views[arg1 % views.length], 0);

		return views[arg1 % views.length];
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startUpdate(View arg0) {
	}

	public V[] getAllItems() {
		return views;
	}
}
