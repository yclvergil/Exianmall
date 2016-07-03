package com.cnmobi.exianmall.base;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.ViewPagerAdapter;
import com.cnmobi.exianmall.login.activity.Fragment1;
import com.cnmobi.exianmall.login.activity.Fragment2;
import com.cnmobi.exianmall.login.activity.Fragment3;
import com.lidroid.xutils.ViewUtils;

public class GuideActivity extends FragmentActivity {

	private ViewPager viewPage;
	private Fragment1 mFragment1;
	private Fragment2 mFragment2;
	private Fragment3 mFragment3;
	private PagerAdapter mPgAdapter;
	private List<Fragment> mListFragment = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		ViewUtils.inject(this);

		viewPage = (ViewPager) findViewById(R.id.viewpager);
		mFragment1 = new Fragment1();
		mFragment2 = new Fragment2();
		mFragment3 = new Fragment3();
		mListFragment.add(mFragment1);
		mListFragment.add(mFragment2);
		mListFragment.add(mFragment3);
		mPgAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mListFragment);
		viewPage.setAdapter(mPgAdapter);

	}

}
