package com.cnmobi.exianmall.widget;

public class PageViewData {

	private int PageViewValue;
	private String Date;

	public PageViewData(int i, String j, int k) {
		this.Date = j;
		this.PageViewValue = k;
	}

	public int getPageViewValue() {
		return PageViewValue;
	}

	public void setPageViewValue(int pageViewValue) {
		PageViewValue = pageViewValue;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

}
