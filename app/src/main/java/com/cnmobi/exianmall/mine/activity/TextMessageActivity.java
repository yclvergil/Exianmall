package com.cnmobi.exianmall.mine.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.CommonAdapter;
import com.cnmobi.exianmall.adapter.ViewHolder;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.SystemTextMessageBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 文本消息界面
 * @author peng24
 *
 */
public class TextMessageActivity extends BaseActivity{

	@ViewInject(R.id.tv_msgtitle)
	TextView tv_msgtitle;
	@ViewInject(R.id.tv_releasetime)
	TextView tv_releasetime;
	@ViewInject(R.id.tv_msgcontent)
	TextView tv_msgcontent;
	@ViewInject(R.id.ly_msgcontent)
	
	ListView lv_msgcontent;
	CommonAdapter<SystemTextMessageBean> adapter;
	
	List<SystemTextMessageBean> systemTextMessageBeans;
	
	/**
	 * 系统文本消息接口标识
	 */
	public static final int sysTextMessage = 0;
	/**
	 * 客服回复用户反馈消息接口标识
	 */
	public static final int feedbackMessage = 1;
	/**
	 * 客服回复用户评价消息接口标识
	 */
	public static final int commentMessage = 2;
	/**
	 * 客服回复用户留言消息接口标识
	 */
	public static final int leaveMessage = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_sys_text_message);
		ViewUtils.inject(this);
		init();
	}
	
	void init(){
		switch (Integer.parseInt(getIntent().getStringExtra("msgtype"))) {
		case 0:// 系统文文本消息
			setTitleText("系统消息");
			getSysTextMessage(0);
			break;
		case 2:// 客服回复用户反馈消息、
			setTitleText("意见反馈");
			getSysTextMessage(2);
			break;
		case 3:// 客服回复用户评价消息
			setTitleText("用户评价");
			getSysTextMessage(3);
			break;
		case 4:// 客服回复用户留言消息
			setTitleText("留言");
			getSysTextMessage(4);
			break;
		default:
			break;
		}
	}
	
	void initSysTextMessage(){
		tv_msgcontent.setVisibility(View.GONE);
		tv_msgtitle.setText(getIntent().getStringExtra("msgtitle"));
		tv_releasetime.setText(getIntent().getStringExtra("replytime"));
		adapter=new CommonAdapter<SystemTextMessageBean>(TextMessageActivity.this,systemTextMessageBeans,R.layout.item_sys_text_message) {
			
			@Override
			public void convert(ViewHolder helper, SystemTextMessageBean item) {
				helper.setText(R.id.tv_subTitle, item.getSubTitle());
				helper.setText(R.id.tv_msgcontent, item.getMsgcontent());
			}
		};
		lv_msgcontent.setAdapter(adapter);
	}
	
	
	/**
	 * @param msgtype 消息类型 ：0=系统文本消息 2=客服回复用户反馈消息、3=客服回复用户评价消息、4=客服回复用户留言消息
	 */
	void getSysTextMessage(int msgtype){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("idMessage",getIntent().getStringExtra("idMessage"));
		params.addQueryStringParameter("msgtype",""+msgtype);// 消息类型
		params.addQueryStringParameter("idUser",MyApplication.getInstance().getIdNumber()+"");
		
		if (msgtype == 0) {
			doHttp(0, MyConst.systemMessageDetailAction, params, sysTextMessage);
		} else if (msgtype == 2) {
			doHttp(0, MyConst.systemMessageDetailAction, params, feedbackMessage);
		} else if (msgtype == 3) {
			doHttp(0, MyConst.systemMessageDetailAction, params, commentMessage);
		} else if (msgtype == 4) {
			doHttp(0, MyConst.systemMessageDetailAction, params, leaveMessage);
		}
		
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		JSONObject jsonObject;
		switch (flag) {
		case sysTextMessage:
			systemTextMessageBeans=new Gson().fromJson(jsonString,new TypeToken<List<SystemTextMessageBean>>() {
			}.getType());
			initSysTextMessage();
			break;
		case feedbackMessage:
			lv_msgcontent.setVisibility(View.GONE);
			try {
				jsonObject = new JSONObject(jsonString);
				tv_msgtitle.setText("反馈内容："+jsonObject.getString("feedcontent"));
				tv_msgcontent.setText("客服回复："+jsonObject.getString("replycontent"));
				tv_releasetime.setText(getIntent().getStringExtra("replytime"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			break;
		case commentMessage:
			lv_msgcontent.setVisibility(View.GONE);
			try {
				jsonObject = new JSONObject(jsonString);
				tv_msgtitle.setText("评价内容："+jsonObject.getString("evaluatecontent"));
				tv_msgcontent.setText("客服回复："+jsonObject.getString("replycontent"));
				tv_releasetime.setText("订单号:"+jsonObject.getString("orderNo"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case leaveMessage:
			lv_msgcontent.setVisibility(View.GONE);
			try {
				jsonObject = new JSONObject(jsonString);
				tv_msgtitle.setText("留言内容："+jsonObject.getString("advcontent"));
				tv_msgcontent.setText("客服回复："+jsonObject.getString("replycontent"));
				tv_releasetime.setText(getIntent().getStringExtra("replytime"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
}
