package com.cnmobi.exianmall.home.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.CommonAdapter;
import com.cnmobi.exianmall.adapter.ViewHolder;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.HistorySearchBean;
import com.cnmobi.exianmall.type.activity.ShowTypeActivity;
import com.cnmobi.exianmall.widget.FlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 搜索界面
 */
public class SearchActivity extends BaseActivity {

	@ViewInject(R.id.lv_history_search)
	private ListView lv_history_search;
	@ViewInject(R.id.ed_search)
	EditText edt_searvh;
	@ViewInject(R.id.flowlayout)
	FlowLayout flowLayout;
	private List<HistorySearchBean> historySearchBeans = new ArrayList<HistorySearchBean>();
	private CommonAdapter<HistorySearchBean> adapter;

	public static final int typeName = 0;

	private String mNames[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		ViewUtils.inject(this);

		//热门搜索，历史搜索功能不要了！！
//		initList();
//		getTypename();

	}

	@OnClick({ R.id.tv_clean_history_search, R.id.iv_back, R.id.tv_back, R.id.btn_search })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_clean_history_search:
			historySearchBeans.clear();
			clearSp();
			adapter.notifyDataSetChanged();
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_back:
			finish();
			break;
		case R.id.btn_search:
			if (isNull(edt_searvh)) {
				showToast("请输入内容");
			} else {
				// 先隐藏键盘
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
						SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				HistorySearchBean historySearchBean = new HistorySearchBean();
				historySearchBean.setProductName(getStr(edt_searvh));
				historySearchBeans.add(historySearchBean);
				// adapter.notifyDataSetChanged();
				// search();
				Intent intent = new Intent(SearchActivity.this, ShowTypeActivity.class);
				intent.putExtra("commodityname", getStr(edt_searvh));// 搜索关键字
				intent.putExtra("whitchActivity", 1);// 区分那个页面跳转
				startActivity(intent);
				finish();
				edt_searvh.setText("");
			}

			break;
		default:
			break;
		}
	}

	void initList() {
		String historyString = getSp(MyConst.serachSp, "") + "";
		if (!isNull(historyString)) {
			historySearchBeans = new Gson().fromJson(historyString, new TypeToken<List<HistorySearchBean>>() {
			}.getType());
		}
		adapter = new CommonAdapter<HistorySearchBean>(SearchActivity.this, historySearchBeans,
				R.layout.adapter_history_search) {

			@Override
			public void convert(ViewHolder helper, HistorySearchBean item) {
				helper.setText(R.id.tv_history_search_productname, item.getProductName());
			}

		};
		lv_history_search.setAdapter(adapter);
		lv_history_search.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(SearchActivity.this, ShowTypeActivity.class);
				intent.putExtra("commodityname", historySearchBeans.get(arg2).getProductName());// 搜索关键字
				intent.putExtra("whitchActivity", 1);// 区分那个页面跳转
				startActivity(intent);
				finish();
			}
		});
	}

	/**
	 * 获取分类名字
	 */
	void getTypename() {
		RequestParams params = new RequestParams();
		doHttp(0, MyConst.hotSearchAction, params, typeName);
	}

	@SuppressWarnings("deprecation")
	void init() {
		MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 10;
		lp.rightMargin = 10;
		lp.topMargin = 10;
		lp.bottomMargin = 10;
		for (int i = 0; i < mNames.length; i++) {
			final TextView view = new TextView(this);
			view.setText(mNames[i]);
			view.setTextSize(18);
			view.setTag(i);
			view.setTextColor(Color.parseColor("#999999"));
			view.setBackgroundDrawable(getResources().getDrawable(R.drawable.serach_tvbg));
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LogUtils.i(mNames[(Integer) view.getTag()]);
					Intent intent = new Intent(SearchActivity.this, ShowTypeActivity.class);
					intent.putExtra("commodityname", mNames[(Integer) view.getTag()]);// 搜索关键字
					intent.putExtra("whitchActivity", 1);// 区分那个页面跳转
					startActivity(intent);
					finish();
				}
			});
			flowLayout.addView(view, lp);
		}
	}

	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		switch (flag) {
		case typeName:
			ArrayList<String> list = new ArrayList<String>();
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					list.add(jsonObject.getString("commodityname"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mNames = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				mNames[i] = list.get(i);
			}
			init();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		putSp(MyConst.serachSp, new Gson().toJson(historySearchBeans));
	}

}
