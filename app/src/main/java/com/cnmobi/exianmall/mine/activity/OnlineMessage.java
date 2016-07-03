package com.cnmobi.exianmall.mine.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.CommonAdapter;
import com.cnmobi.exianmall.adapter.ViewHolder;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.HistoryOnlineMessageBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/** 在线留言 */
public class OnlineMessage extends BaseActivity {
	@ViewInject(R.id.et_message)
	EditText et_message;
	@ViewInject(R.id.rl)
	RelativeLayout rl;
//	@ViewInject(R.id.ll_message)
//	LinearLayout ll_message;
	@ViewInject(R.id.sv_message)
	ScrollView sv_message;
	@ViewInject(R.id.lv_history_message)
	ListView lv_history_message;

	/**
	 * 在线留言接口标识
	 */
	public static final int onlinemessage = 0;
	private LayoutInflater layoutInflater;
	private List<HistoryOnlineMessageBean> historyOnlineMessageBean = new ArrayList<HistoryOnlineMessageBean>();
	private CommonAdapter<HistoryOnlineMessageBean> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_onlinemessage);
		ViewUtils.inject(this);

		layoutInflater = getLayoutInflater();
		setTitleText("在线留言");
		setRightTextViewText("清空");
		setRightTextViewClick(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				historyOnlineMessageBean.clear();
				clearSp();
				adapter.notifyDataSetChanged();
			}
		});
		initList();
	}
	
	void initList() {
		String historyString = getSp(MyConst.onlineMsgSp, "") + "";
		if (!isNull(historyString)) {
			historyOnlineMessageBean = new Gson().fromJson(historyString, new TypeToken<List<HistoryOnlineMessageBean>>() {
			}.getType());
		}
		adapter = new CommonAdapter<HistoryOnlineMessageBean>(OnlineMessage.this, historyOnlineMessageBean,
				R.layout.activity_mine_onlinemessage_item) {

			@Override
			public void convert(ViewHolder helper, HistoryOnlineMessageBean item) {
				
				helper.setText(R.id.tv_currentTime, item.getMessageTime());
				helper.setText(R.id.tv_messagecontent, item.getMessageContent());
			}

		};
		lv_history_message.setAdapter(adapter);
	}

	// 在线留言接口
	void onlinemessageHttp() {
		// http://121.46.0.219:8080/efreshapp/advisoryAction?idAdvisory=1007&advcontent=I
		// LOVE MYLOVE
		RequestParams params = new RequestParams();
		params.addBodyParameter("idUser", MyApplication.getInstance()
				.getIdNumber() + "");
		params.addBodyParameter("advcontent", getStr(et_message));
		doHttp(1, MyConst.advisoryAction, params, onlinemessage);
	}

	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
		case onlinemessage:
			showToast("我们会尽快对您的留言回复，谢谢！！");
			
			HistoryOnlineMessageBean historySearchBean = new HistoryOnlineMessageBean();
			historySearchBean.setMessageTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			historySearchBean.setMessageContent(getStr(et_message));
			historyOnlineMessageBean.add(historySearchBean);
			adapter = new CommonAdapter<HistoryOnlineMessageBean>(OnlineMessage.this, historyOnlineMessageBean,
					R.layout.activity_mine_onlinemessage_item) {

				@Override
				public void convert(ViewHolder helper, HistoryOnlineMessageBean item) {
					
					helper.setText(R.id.tv_currentTime, item.getMessageTime());
					helper.setText(R.id.tv_messagecontent, item.getMessageContent());
				}

			};
			lv_history_message.setAdapter(adapter);
			et_message.setText("");
			
//			LinearLayout ll = (LinearLayout) layoutInflater.inflate(R.layout.activity_mine_onlinemessage_item, null);
//			TextView tv_currentTime = (TextView) ll.findViewById(R.id.tv_currentTime);
//			tv_currentTime.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
//			TextView tv_messagecontent = (TextView) ll.findViewById(R.id.tv_messagecontent);
//			tv_messagecontent.setText(getStr(et_message));
//			ll_message.addView(ll);
//			et_message.setText("");
			new Handler().post(new Runnable() {
			    @Override
			    public void run() {
			        sv_message.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
			    }
			});
			break;

		default:
			break;
		}
	}

	@OnClick(R.id.btn_send)
	private void onSendClick(View v) {
		// TODO Auto-generated method stub
		if(isNull(et_message)){
			showToast("请先输入留言！！");
			return;
		}
		InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
		onlinemessageHttp();
        
	}


	@OnClick(R.id.lv_history_message)
	private void hide(View v) {
		InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		putSp(MyConst.onlineMsgSp, new Gson().toJson(historyOnlineMessageBean));
	}
}
