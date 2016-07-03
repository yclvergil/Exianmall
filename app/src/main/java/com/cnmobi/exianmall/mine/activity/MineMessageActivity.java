package com.cnmobi.exianmall.mine.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.adapter.MessageAdapter;
import com.cnmobi.exianmall.adapter.MessageAdapter.onRightItemClickListener;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MainActivityMerchant;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.bean.MessageBean;
import com.cnmobi.exianmall.widget.SwipeListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * 我的消息界面
 */
public class MineMessageActivity extends BaseActivity implements OnRefreshListener{
	@ViewInject(R.id.list)
	SwipeListView listView;
	@ViewInject(R.id.tv_empty)
	TextView tv_empty;
	List<MessageBean> list;
	MessageAdapter adapter;
	Intent intent;
	@ViewInject(R.id.id_swipe_message)
	SwipeRefreshLayout mSwipeLayout;  
	private boolean isPullDown=false;
	
	/**
	 * 系统消息列表接口标识
	 */
	public static final int messageList = 0;
	/**
	 * 清空消息接口标识
	 */
	public static final int emptyMessage = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_message);
		ViewUtils.inject(this);

		messageList();
		setTitleText("我的消息");
		setRightTextViewText("清空");
		init();
		setRightTextViewClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 对话框
//				LogUtils.i("----------->"+list.size());
				if(list.size()==0){
					showToast("还没有消息！");
				}else{
				LayoutInflater inflaterDl = LayoutInflater.from(getApplicationContext());
				RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
						.inflate(R.layout.dialog_clean_message, null);
				Builder builder = new AlertDialog.Builder(MineMessageActivity.this);
				builder.setView(relativeLayout);
				final Dialog dialog = builder.create();
				dialog.show();
				Button btnCancel = (Button) relativeLayout.findViewById(R.id.btn_clean_message_cancel);
				btnCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				Button btnOK = (Button) relativeLayout.findViewById(R.id.btn_clean_message_ok);
				btnOK.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						boolean iSotherMsg=false; //是否有非系统消息
				
						for(int i=0;i<list.size();i++){
							if(!"0".equals(list.get(i).getMsgtype())
									&& !"1".equals(list.get(i).getMsgtype())){
								iSotherMsg=true;
								break;
							}
						}
						if(iSotherMsg==false){
							showToast("亲爱的用户，系统消息不能删除哦~");
						}else{
							emptyMessage();
						}
						dialog.dismiss();
						
					}
				});
			}
			}
		});
	}
	
	
	void messageList(){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		if(isPullDown){
			doHttp(0, MyConst.messageListAction, params, messageList,false);
		}else{
			doHttp(0, MyConst.messageListAction, params, messageList);
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
		boolean result=false;
		if(MyApplication.getInstance().getMessage()!=null){
			
		
		for(int i=0;i<MyApplication.getInstance().getMessage().size();i++){
			if(!"Y".equals(MyApplication.getInstance().getMessage().get(i).getIsRead())){
				result=true;
			}
		}
		if(!result){
			intent = new Intent();
			if("1".equals(MyApplication.getInstance().getTlr_type())){
				intent.setAction(MainActivityMerchant.update_red_points);
				sendBroadcast(intent);
			}else{
				intent.setAction(MainActivity.update_red_point);
				sendBroadcast(intent);
			}
		}
		}
	}
	
	void emptyMessage(){
		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		doHttp(0, MyConst.emptyMessageAction, params, emptyMessage);
	}
	
	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
		case messageList:
			if(isPullDown){
				list.clear();
				mSwipeLayout.setRefreshing(false);//结束下拉刷新动画
				isPullDown=!isPullDown;
			}
//			List<MessageBean> lists=new Gson().fromJson(jsonString,new TypeToken<List<MessageBean>>() {
//			}.getType());
			list=new Gson().fromJson(jsonString,new TypeToken<List<MessageBean>>() {
			}.getType());
//			for(int i=0;i<lists.size();i++){
//				list.add(lists.get(i));
//			}
			
			String spFileName=MyApplication.getInstance().getIdNumber()+"sysIdMessage";
			SharedPreferences sharedPreferences = getSharedPreferences(spFileName,
					Context.MODE_PRIVATE + Context.MODE_PRIVATE);
			Iterator it;
			String str="";
//			list.get(0).setIdMessage(1222211111);
			if(list.size()!=0){
				
				//后台不能够判断系统消息是否已读     ---把保存在SharedPreferences里面的已读的系统消息id跟消息列表接口的系统消息id想对比
				for(int i=0;i<list.size();i++){
					if("0".equals(list.get(i).getMsgtype())||"1".equals(list.get(i).getMsgtype())){
						it=sharedPreferences.getStringSet("idMessage", new HashSet<String>()).iterator();
						while (it.hasNext()) {
							str = (String) it.next();
							if ((list.get(i).getIdMessage() + "").equals(str)) {
								list.get(i).setIsRead("Y");
							}
						}
					}}
				MyApplication.getInstance().getMessage().clear();
				MyApplication.getInstance().setMessage(list);
			}
			LogUtils.i("------2"+list.size());
			
			init();
			break;
		case emptyMessage:
			// 清空消息-除了系统消息，其它item都清掉
			for(int i=0;i<list.size();i++){
				if (!"0".equals(list.get(i).getMsgtype())
						&& !"1".equals(list.get(i).getMsgtype())) {
					list.remove(i);
					MyApplication.getInstance().getMessage().remove(i);
					--i;
				}
			}
			
//			for(int i=0;i<MyApplication.getInstance().getMessage().size();i++){
//				if ("0".equals(MyApplication.getInstance().getMessage().get(i)
//						.getMsgtype())
//						|| "1".equals(MyApplication.getInstance().getMessage()
//								.get(i).getMsgtype())) {
//					MyApplication.getInstance().getMessage().remove(i);
//					--i;
//				}
//			}
			
			
			if(!MyApplication.getInstance().isReadedMessage()){
//				if(!MyApplication.getInstance().isReadedSysMessage()){
//					intent = new Intent();
					if("1".equals(MyApplication.getInstance().getTlr_type())){
						intent.setAction(MainActivityMerchant.update_red_points);
						sendBroadcast(intent);
					}else{
						intent.setAction(MainActivity.update_red_point);
						sendBroadcast(intent);
					}
//				}
				
			}
//			else{
//				if(!MyApplication.getInstance().isReadedSysMessage()){
//					intent = new Intent();
//					if("1".equals(MyApplication.getInstance().getTlr_type())){
//						intent.setAction(MainActivityMerchant.update_red_points);
//						sendBroadcast(intent);
//					}else{
//						intent.setAction(MainActivity.update_red_point);
//						sendBroadcast(intent);
//					}
//				}
//			}
			adapter.notifyDataSetChanged();
			showToast("清空消息成功");
			break;
		default:
			break;
		}
	}
	
	void init() {
		
		mSwipeLayout.setOnRefreshListener(this);  
		mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,  android.R.color.holo_orange_light, android.R.color.holo_red_light);  
		if(MyApplication.getInstance().getMessage()!=null){
			list=MyApplication.getInstance().getMessage();
		}
		if(list!=null){
		listView.setEmptyView(tv_empty);
		adapter = new MessageAdapter(MineMessageActivity.this, list, listView.getRightViewWidth(), listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String spFileName=MyApplication.getInstance().getIdNumber()+"sysIdMessage";
				SharedPreferences sharedPreferences = getSharedPreferences(spFileName,
						Context.MODE_PRIVATE + Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit(); // 获取编辑器
				HashSet<String> hash=new HashSet<String>();
				Iterator it=sharedPreferences.getStringSet("idMessage", new HashSet<String>()).iterator();;
				while (it.hasNext()) {
					hash.add((String) it.next());
				}
				// msgtype：//消息类型：0=系统文本消息、1=系统图文消息、2=客服回复用户反馈消息、
				// 3=客服回复用户评价消息、4=客服回复用户留言消息
				if("1".equals(list.get(position).getMsgtype())){
					intent=new Intent(MineMessageActivity.this, MineMessageDetailActivity.class);
					intent.putExtra("idMessage", ""+list.get(position).getIdMessage()); 
					startActivity(intent);
					hash.add(list.get(position).getIdMessage()+"");
					editor.putStringSet("idMessage", hash);
					editor.commit();
					hash.clear();
					//把系统消息id保存到SharedPreferences--sysIdMessage
				}else if("0".equals(list.get(position).getMsgtype())){
					intent=new Intent(MineMessageActivity.this, TextMessageActivity.class);
					intent.putExtra("idMessage", ""+list.get(position).getIdMessage()); 
					intent.putExtra("msgtype", ""+list.get(position).getMsgtype()); 
					intent.putExtra("msgtitle", ""+list.get(position).getMsgtitle()); 
					intent.putExtra("replytime", ""+list.get(position).getReplytime()); 
					startActivity(intent);
					hash.add(list.get(position).getIdMessage()+"");
					editor.putStringSet("idMessage", hash);
					editor.commit();
					hash.clear();
					//把系统消息id保存到SharedPreferences--sysIdMessage
				}
				else{
					intent=new Intent(MineMessageActivity.this, TextMessageActivity.class);
					intent.putExtra("idMessage", ""+list.get(position).getIdMessage()); 
					intent.putExtra("msgtype", ""+list.get(position).getMsgtype()); 
					intent.putExtra("msgtitle", ""+list.get(position).getMsgtitle()); 
					intent.putExtra("replytime", ""+list.get(position).getReplytime()); 
					startActivity(intent);
					
				}
				MyApplication.getInstance().getMessage().get(position).setIsRead("Y");
				if(!MyApplication.getInstance().isReadedMessage()){
//					if(!MyApplication.getInstance().isReadedSysMessage()){
//						intent = new Intent();
						if("1".equals(MyApplication.getInstance().getTlr_type())){
							intent.setAction(MainActivityMerchant.update_red_points);
							sendBroadcast(intent);
						}else{
							intent.setAction(MainActivity.update_red_point);
							sendBroadcast(intent);
						}
//					}
					
				}
//				else{
//					if(!MyApplication.getInstance().isReadedSysMessage()){
//						intent = new Intent();
//						if("1".equals(MyApplication.getInstance().getTlr_type())){
//							intent.setAction(MainActivityMerchant.update_red_points);
//							sendBroadcast(intent);
//						}else{
//							intent.setAction(MainActivity.update_red_point);
//							sendBroadcast(intent);
//						}
//					}
//				}
			}
		});
		
		adapter.setOnRightItemClickListener(new onRightItemClickListener() {
			
			@Override
			public void onRightItemClick(View v, final int position) {
				if ("0".equals(list.get(position).getMsgtype())
						|| "1".equals(list.get(position).getMsgtype())) {
					showToast("亲爱的用户，系统消息不能删除哦~");
				}else{
					RequestParams params = new RequestParams();
					params.addQueryStringParameter("idMessage", list.get(position).getIdMessage() + "");
					params.addQueryStringParameter("msgtype", list.get(position).getMsgtype());
					params.addQueryStringParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
					params.addBodyParameter("toKen",MyApplication.getInstance().getToKen());
					HttpUtils httpUtils=new HttpUtils();
//					httpUtils.configCookieStore(MyCookieStore.cookieStore);
					httpUtils.send(HttpMethod.GET, MyConst.deletMessageAction, params, new RequestCallBack<String>() {
//					BaseActivity.httpUtils.send(HttpMethod.GET, MyConst.deletMessageAction, params, new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
						
							try {
								JSONObject jsonObject = new JSONObject(arg0.result);
								int errorCode = jsonObject.getInt("code");
								if (errorCode == 0) {
									list.remove(position);
									adapter.notifyDataSetChanged();
									showToast("删除成功");
									MyApplication.getInstance().getMessage().get(position).setIsRead("Y");
									if(!MyApplication.getInstance().isReadedMessage()){
//										if(!MyApplication.getInstance().isReadedSysMessage()){
//											intent = new Intent();
											if("1".equals(MyApplication.getInstance().getTlr_type())){
												intent.setAction(MainActivityMerchant.update_red_points);
												sendBroadcast(intent);
											}else{
												intent.setAction(MainActivity.update_red_point);
												sendBroadcast(intent);
											}
//										}
										
									}
//									else{
//										if(!MyApplication.getInstance().isReadedSysMessage()){
//											intent = new Intent();
//											if("1".equals(MyApplication.getInstance().getTlr_type())){
//												intent.setAction(MainActivityMerchant.update_red_points);
//												sendBroadcast(intent);
//											}else{
//												intent.setAction(MainActivity.update_red_point);
//												sendBroadcast(intent);
//											}
//										}
//									}
									
									
									
								} else {
									// 错误就统一弹出错误信息
									showToast(jsonObject.getString("message"));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
	}
	}


	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		isPullDown=true;
		mSwipeLayout.setRefreshing(true);
		messageList();
	}

}
