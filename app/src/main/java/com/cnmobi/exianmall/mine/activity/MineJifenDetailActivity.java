package com.cnmobi.exianmall.mine.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.CommonAdapter;
import com.cnmobi.exianmall.adapter.ViewHolder;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.JifenDetailBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 我的积分详细界面
 */
public class MineJifenDetailActivity extends BaseActivity {

	@ViewInject(R.id.lv_jifen_detail)
	ListView lv_jifen_detail;

	private List<JifenDetailBean> jifenDetailDatas = new ArrayList<JifenDetailBean>();
	CommonAdapter<JifenDetailBean> jifenDetailAdapter;

	/**
	 * 积分明细接口标识
	 */
	public static final int integralSubsidiary = 0;
	/**
	 * 积分列表页数
	 */
	private int page = 1;
	/**
	 * 积分列表加载完毕
	 */
	private boolean isAll = false;
	private boolean ishasJifen=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_jifen_detail);
		ViewUtils.inject(this);

		setTitleText("积分明细");
		getIntegralSubsidiary();
		init();

	}

	void getIntegralSubsidiary() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addQueryStringParameter("currentPage", page + "");
		doHttp(0, MyConst.integralSubsidiaryAction, params, integralSubsidiary);
	}

	void init() {

		jifenDetailAdapter = new CommonAdapter<JifenDetailBean>(getApplicationContext(), jifenDetailDatas,
				R.layout.adapter_jifen_detail) {

			@Override
			public void convert(ViewHolder helper, JifenDetailBean item) {
				switch (item.getSource()) {
				// source:获得积分来源，0=充值获得积分，1=购物获得积分，2=宣传获得积分，
				case 0:
					helper.setText(R.id.tv_jifen_source, "充值送积分");
					break;
				case 1:
					helper.setText(R.id.tv_jifen_source, "购物送积分");
					break;
				case 2:
					helper.setText(R.id.tv_jifen_source, "推荐好友送积分");
					break;
				default:
					break;
				}

				helper.setText(R.id.tv_order_number, "");
				helper.setText(R.id.tv_jifen_date, item.getClearTime());
				helper.setText(R.id.tv_jifen_detaildate, "");
				if (item.getIntegral() > 0) {
					helper.setText(R.id.tv_jifen_count, "+" + item.getIntegral());
				} else {
					helper.setText(R.id.tv_jifen_count, "" + item.getIntegral());
				}
			}

		};
		lv_jifen_detail.setFocusable(false);
		lv_jifen_detail.setAdapter(jifenDetailAdapter);
		lv_jifen_detail.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				// 当不滚动时
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 判断是否滚动到底部
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						// 加载更多功能的代码
						if (!isAll) {
							page++;
							getIntegralSubsidiary();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
		case integralSubsidiary:
			List<JifenDetailBean> orderDetailedList = new ArrayList<JifenDetailBean>();
			orderDetailedList = new Gson().fromJson(jsonString, new TypeToken<List<JifenDetailBean>>() {
			}.getType());
			if(orderDetailedList.size()==0){
				if(!ishasJifen){
					showToast("您还没有积分哦~");
				}
			}else{
				ishasJifen=true;
			}
			
			for (int i = 0; i < orderDetailedList.size(); i++) {
				jifenDetailDatas.add(orderDetailedList.get(i));
			}
			jifenDetailAdapter.notifyDataSetChanged();
			if (jifenDetailDatas.size() == 0) {
				isAll = false;
			} else {
				if (orderDetailedList.size() == 0) {
					showToast("加载完毕");
					isAll = true;
				}
			}
			break;
		default:
			break;
		}
	}

}
