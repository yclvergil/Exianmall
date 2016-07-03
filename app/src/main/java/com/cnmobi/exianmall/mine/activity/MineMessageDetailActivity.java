package com.cnmobi.exianmall.mine.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.CommonAdapter;
import com.cnmobi.exianmall.adapter.MessageDetailAdapter;
import com.cnmobi.exianmall.adapter.ViewHolder;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.MessageContentBean;
import com.cnmobi.exianmall.bean.MessageDetailBean;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 系统消息-图文混编
 * @author peng24
 *
 */
public class MineMessageDetailActivity extends BaseActivity{

	@ViewInject(R.id.tv_msgtitle)
	TextView tv_msgtitle;
	@ViewInject(R.id.tv_releasetime)
	TextView tv_releasetime;
	@ViewInject(R.id.ly_msgcontent)
	ListView ly_msgcontent;
	
	private List<MessageContentBean> messageContentList=new ArrayList<MessageContentBean>();
	private MessageDetailBean messageDetailBean;
	private MessageDetailAdapter adapter;
	/**
	 * 系统消息详情接口标识
	 */
	public static final int systemMessageDetail = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_message_detail);
		ViewUtils.inject(this);
		setTitleText("系统消息");
		getMessageDetail();
	}
	
	void getMessageDetail(){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("idMessage",getIntent().getStringExtra("idMessage"));
		params.addQueryStringParameter("msgtype","1");// 1=系统图文消息
		params.addQueryStringParameter("idUser",MyApplication.getInstance().getIdNumber()+"");
		doHttp(0, MyConst.systemMessageDetailAction, params, systemMessageDetail);
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		Gson gson;
		switch (flag) {
		case systemMessageDetail:
			gson = new Gson();
			LogUtils.i(jsonString);
			messageDetailBean=gson.fromJson(jsonString, MessageDetailBean.class);
			tv_msgtitle.setText(messageDetailBean.getMsgtitle());
			tv_releasetime.setText(messageDetailBean.getReleasetime());
			MessageContentBean messageContentBean;
			for(int i=0;i<messageDetailBean.getSysmsgcontent().size();i++){
				messageContentBean=new MessageContentBean();
				messageContentBean=messageDetailBean.getSysmsgcontent().get(i);
				messageContentList.add(messageContentBean);
			}
			adapter=new MessageDetailAdapter(messageContentList, MineMessageDetailActivity.this);
			ly_msgcontent.setAdapter(adapter);
			break;

		default:
			break;
		}
	}
}
