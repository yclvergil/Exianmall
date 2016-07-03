package com.cnmobi.exianmall.mine.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.cnmobi.exianmall.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 版本更新
 */
public class AppVersionManager implements OnActivityResultListener{

	private static final String TAG = "AppVersionManager";
	private static final int SHOW_DOWNLOAD_DIALOG = 1;

	private static final int DISMISS_DOWNLOAD_DIALOG = 2;
	/* 下载中 */
	private static final int DOWNLOADING = 3;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 4;
	/* 检查更新结束 */
	private static final int CHECK_VERSION_FINISH = 5;
	/* 取消更新 */
	private static final int CANCEL_UPDATE_VERSION = 6;

	private static final int APK_SIZE = 7;

	private static final int REQ_INSALL_APK = 8;

	private AppVersionManager manager;

	private Activity context;

	private boolean autoUpdate = false;// 自动升级
	

	// private boolean downloading = false;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private TextView tvInfo;
	private TextView tvSize;

	private Dialog downloadDialog;
	private StringBuffer sbInfo = new StringBuffer();
	private IVersionCallback mCallBacks;
	
	//下载安装路径
	private static final String savaPath=Environment.getExternalStorageDirectory() + "/e鲜商城/";
	
	private static final String savaFileName=savaPath+"exianmall.apk";
	private int progress=0;
	private static final int DOWN_UPDATE=1;
	private static final int DOWN_OVER=2;
	private TextView updateText;
	private boolean interceptFlag = false;
	private boolean isDownOver = false;
	private Thread downLoadThread;
	private String downLoadUrl="";
	public AppVersionManager(Activity context) {
		this.context = context;
	}

	public void registerCallback(IVersionCallback callback) {
		mCallBacks = callback;

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOW_DOWNLOAD_DIALOG:
				showDownLoadDialog();
				break;
			case DISMISS_DOWNLOAD_DIALOG:
				dismissDownloadDialog();
				break;
			case DOWNLOADING:
				// 设置进度条
				int progress = msg.arg1;
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				File file = (File) msg.obj;
				installApk(file);
				break;
			case APK_SIZE:
				String size = (String) msg.obj;
				tvInfo.setText(sbInfo.toString());
				tvSize.setText(size);
				break;
			case CHECK_VERSION_FINISH:
				if (mCallBacks != null) {
					mCallBacks.checkVersionFinish();
				}
				break;
			case CANCEL_UPDATE_VERSION:
				if (mCallBacks != null) {
					mCallBacks.checkVersionCanceled();
				}
				break;

			default:
				break;
			}
		}

	};

	public void setUpdateType(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

//	public boolean isNetworkEnable() {
//		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
//		try {
////			ConnectivityManager connectivity = (ConnectivityManager) context
////					.getSystemService(Context.CONNECTIVITY_SERVICE);
////			if (connectivity != null) {
////				// 获取网络连接管理的对象
////				NetworkInfo info = connectivity.getActiveNetworkInfo();
////				if (info != null && info.isConnected()) {
////					// 判断网络是否连接
////					if (info.getState() == NetworkInfo.State.CONNECTED) {
////						return true;
////					}
////				}
//
//			return NetUtils.isConnected(context)||NetUtils.isWifi(context);
//			
//		} catch (Exception e) {
//			Log.d(TAG, "没有发现可用的网络");
//		}
//		return false;
//
//	}

//	/**
//	 * 获取客户端ID
//	 */
//	private String getAppId() {
//		String appId = null;
//		try {
//			PackageInfo info = context.getPackageManager().getPackageInfo(
//					context.getPackageName(), 0);
//			appId = info.packageName;
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		return appId;
//	}

	/**
	 * 获取客户端版本信息-版本code
	 */
	public int getAppVersion() {
		int versionCode = -1;
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;

	}
	
	/**
	 * 获取客户端版本信息-版本名
	 */
	public String getAppVersionName() {
		String versionName ="";
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;

	}

	private void dismissDownloadDialog() {
		if (downloadDialog != null && downloadDialog.isShowing()) {
			downloadDialog.dismiss();
		}
	}

	public interface IVersionCallback {
		public void checkVersionFinish();
		public void checkVersionCanceled();
	}

	private void downLoadApk(){
		downLoadThread=new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk(File apkfile) {
		if (!apkfile.exists()) {
			return;
		}
		System.out.println(apkfile.toString());
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivityForResult(intent, REQ_INSALL_APK);
		dismissDownloadDialog();
	}
	
	private void installApk(){
		File apkfile = new File(savaFileName);
        if (!apkfile.exists()) {
            return;
        }    
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        context.startActivity(intent);
	
	}
	
	
	public void upDateAppVersion(String url,final String versionDesc){
		downLoadUrl=url;
		handler.post(new Runnable() {
			@Override
			public void run() {
				LayoutInflater inflaterDl = LayoutInflater.from(context);
				RelativeLayout relativeLayout = (RelativeLayout) inflaterDl
						.inflate(R.layout.dialog_clean_message, null);
				Builder builder1 = new AlertDialog.Builder(context);
				builder1.setView(relativeLayout);
				final Dialog dialog1 = builder1.create();
				dialog1.setCancelable(false);
				dialog1.show();
				TextView tv_content=(TextView) relativeLayout.findViewById(R.id.tv_content);
				tv_content.setText("检测到有新版本，是否更新？\n更新内容：\n"+versionDesc);
				Button btn_ok=(Button) relativeLayout.findViewById(R.id.btn_clean_message_ok);
				Button btn_cancel=(Button) relativeLayout.findViewById(R.id.btn_clean_message_cancel);
				btn_cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog1.dismiss();
						handler.sendEmptyMessage(CANCEL_UPDATE_VERSION);
					}
				});
				btn_ok.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog1.dismiss();
						handler.sendEmptyMessage(SHOW_DOWNLOAD_DIALOG);
					}
				});
//				new AlertDialog.Builder(context)
//						
//						.setMessage("检测到有新版本，是否更新？\n"+versionDesc)
//						.setCancelable(false)
//						.setPositiveButton("确认",
//								new OnClickListener() {
//
//									@Override
//									public void onClick(
//											DialogInterface dialog,
//											int which) {
//										handler.sendEmptyMessage(SHOW_DOWNLOAD_DIALOG);
//									}
//								})
//						.setNegativeButton("取消", new OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								handler.sendEmptyMessage(CANCEL_UPDATE_VERSION);
//							}
//						}).show();
			}
		});


	}
	
	private String getDownLoadUrl(){
		return  downLoadUrl;
//		return  "http://download.taobaocdn.com/wireless/taobao4android/latest/701483.apk";
	}
	
	private void showDownLoadDialog(){
		LayoutInflater inflaterDl = LayoutInflater.from(context);
		LinearLayout linearLayout = (LinearLayout) inflaterDl
				.inflate(R.layout.progress, null);
		Builder builder1 = new AlertDialog.Builder(context);
		builder1.setView(linearLayout).setCancelable(false);
		downloadDialog = builder1.create();
		downloadDialog.show();
		mProgress=(ProgressBar) linearLayout.findViewById(R.id.progress);
		updateText=(TextView) linearLayout.findViewById(R.id.updateText);
		Button btn_cancel=(Button) linearLayout.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				downloadDialog.dismiss();
				interceptFlag=true;
			}
		});
//		
//		AlertDialog.Builder builder=new Builder(context);
//		final LayoutInflater inflater=LayoutInflater.from(context);
//		View view=inflater.inflate(R.layout.progress, null);
//		mProgress=(ProgressBar) view.findViewById(R.id.progress);
//		updateText=(TextView) view.findViewById(R.id.updateText);
//		Button btn_cancel=(Button) view.findViewById(R.id.btn_cancel);
//		builder.setView(view).setCancelable(false);
////		builder.setNegativeButton("取消", new OnClickListener() {
////			
////			@Override
////			public void onClick(DialogInterface dialog, int which) {
////				dialog.dismiss();
////				interceptFlag=true;
////			}
////		});
//		
//		downloadDialog =builder.create();
//		downloadDialog.show();
		
		downLoadApk();
		
		
	}
	
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				updateText.setText("正在下载更新包"+progress+"%");
				break;
			case DOWN_OVER:
				mProgress.setProgress(100);
				updateText.setText("下载完成");
				dismissDownloadDialog();
				installApk();
				break;
			default:
				break;
			}
		}
		
	};
	
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				URL url = new URL(getDownLoadUrl());
			
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				
				File file = new File(savaPath);
				if(!file.exists()){
					file.mkdir();
				}
				String apkFile = savaFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    	    progress =(int)(((float)count / length) * 100);
		    	    //更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){	
		    			//下载完成通知安装
		    			isDownOver = true;
		    			mHandler.sendEmptyMessage(DOWN_OVER);
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//点击取消就停止下载.
				
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				handler.post(new Runnable() {
					@Override
					public void run() {
						dismissDownloadDialog();
						Toast.makeText(context, "对不起，更新失败！", Toast.LENGTH_LONG).show();
						handler.sendEmptyMessage(CHECK_VERSION_FINISH);
					}
				});
			} catch(IOException e){
				e.printStackTrace();
				handler.post(new Runnable() {
					@Override
					public void run() {
						dismissDownloadDialog();
						Toast.makeText(context, "对不起，更新失败！", Toast.LENGTH_LONG).show();
						handler.sendEmptyMessage(CHECK_VERSION_FINISH);
					}
				});
			}
			
		}
	};
	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_INSALL_APK:
			handler.sendEmptyMessage(CHECK_VERSION_FINISH);
			break;

		default:
			break;
		}
		return true;
	}

}
