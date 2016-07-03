package com.cnmobi.exianmall.mine.activity;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.R.drawable;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.fragment.PurchaserCarFragment;
import com.cnmobi.exianmall.utils.FileUtil;
import com.cnmobi.exianmall.utils.ImageUtils;
import com.cnmobi.exianmall.widget.RoundImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的资料界面
 */
public class MineInformationActivity extends BaseActivity {

	@ViewInject(R.id.iv_mine_head)
	private RoundImageView iv_mine_head;// 头像
	@ViewInject(R.id.tv_mine_stallsname)
	private TextView tv_mine_stallsname;
	@ViewInject(R.id.tv_mine_name)
	private TextView tv_mine_name;
	@ViewInject(R.id.tv_mine_phonenumber)
	private TextView tv_mine_phonenumber;
	@ViewInject(R.id.tv_head_phone)
	private TextView tv_head_phone;
	@ViewInject(R.id.tv_head_name)
	private TextView tv_head_name;
	private String stallsName = "";
	private String name = "";
	private String phoneNumber = MyApplication.getInstance().getPhone();
	private Bundle bundle;

	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 3;
	private static final int CAMERA_REQUEST_CODE = 4;
	private static final int RESULT_REQUEST_CODE = 5;

	/** 修改我的资料 */
	public static final int saveInfo = 0;
	/**
	 * 上传头像
	 */
	public static final int uploadhead = 1;
	/**
	 * 保存头像
	 */
	public static final int savehead = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_information);
		ViewUtils.inject(this);

		init();
	}

	@OnClick({ R.id.iv_mine_head, R.id.rl_mine_stallsname, R.id.rl_mine_name, R.id.rl_mine_phonenumber,
			R.id.tv_mine_information_commit, R.id.ly_common_head_return })
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ly_common_head_return:
			finish();
			break;
		case R.id.tv_mine_information_commit:
			saveInfoHttp();
			break;
		case R.id.iv_mine_head:
			new SelectHeadPopupWindows(MineInformationActivity.this, iv_mine_head);
			break;
		case R.id.rl_mine_stallsname:
			intent = new Intent(MineInformationActivity.this, MineInformationStallsActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.rl_mine_name:
			intent = new Intent(MineInformationActivity.this, MineInformationNameActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.rl_mine_phonenumber:
			intent = new Intent(MineInformationActivity.this, MineInformationPhoneNumberActivity.class);
			startActivityForResult(intent, 2);
			break;
		default:
			break;
		}
	}

	void init() {
		if(!"".equals(MyApplication.getInstance().getUserlogo())){
			MyConst.imageLoader.displayImage(MyApplication.getInstance().getUserlogo(), iv_mine_head,
					MyConst.IM_IMAGE_OPTIONS);
		}else{
			iv_mine_head.setImageResource(drawable.logo);
		}	
		tv_mine_stallsname.setText(MyApplication.getInstance().getStallsname());
		tv_head_name.setText(MyApplication.getInstance().getStallsname());
		tv_mine_phonenumber.setText(MyApplication.replaceIndex(5, 8, MyApplication.getInstance().getPhone(), "*"));
		tv_mine_name.setText(MyApplication.getInstance().getTlr_name());
		tv_head_phone.setText(MyApplication.replaceIndex(5, 8, MyApplication.getInstance().getPhone(), "*"));
	}

	// 提交修改资料接口
	// http://121.46.0.219:8080/efreshapp/perfectBuyersInformationAction?idUser=1007&stallsname=武汉江夏区菜市场八号档口&name=wendy&phone=18802093449&imageUrl=
	void saveInfoHttp() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		params.addBodyParameter("stallsname", getStr(tv_mine_stallsname));
		params.addBodyParameter("name", getStr(tv_mine_name));
		params.addBodyParameter("phone", phoneNumber);
		doHttp(1, MyConst.perfectBuyersInformationAction, params, saveInfo);
	}

	@Override
	public void callback(String jsonString, int flag) {
		// TODO Auto-generated method stub
		super.callback(jsonString, flag);
		switch (flag) {
		case saveInfo:
			showToast("您已成功提交买家身份认证，请耐心等待管理员审核，系统将在12小时内审核。未通过审核将不能购买商品哦~");
			Intent intent = new Intent(MineInformationActivity.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
			startActivity(intent);
			finish();
			break;
		case uploadhead:
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				RequestParams params = new RequestParams();
				params.addQueryStringParameter("userId", MyApplication.getInstance().getIdNumber() + "");
				params.addQueryStringParameter("userType", "0");
				params.addQueryStringParameter("imgName", (String) jsonArray.get(0));
				
				doHttp(1, MyConst.modifyHeadPortraitAction, params, savehead);
			//	FileUtil.deleteFile(MineInformationActivity.this, FileUtil.getSDPath() + "/E鲜商城/head.jpg");  
			} catch (JSONException e) {  
				e.printStackTrace();
			}
			break;
		case savehead:
			LogUtils.i(jsonString);
			MyApplication.getInstance().setUserlogo(jsonString);
			showToast("头像保存成功");
			intent = new Intent();
			intent.setAction(MainActivity.update_head);
			sendBroadcast(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == 0 && null != data) {
			bundle = data.getExtras();
			stallsName = bundle.getString("stallsName");
			tv_mine_stallsname.setText(stallsName);
		}
		if (requestCode == 1 && resultCode == 1 && null != data) {
			bundle = data.getExtras();
			name = bundle.getString("name");
			tv_mine_name.setText(name);
		}
		if (requestCode == 2 && resultCode == 2 && null != data) {
			bundle = data.getExtras();
			phoneNumber = bundle.getString("phoneNumber");
			tv_mine_phonenumber.setText(phoneNumber);
		}
		if (resultCode == RESULT_CANCELED) {
			return;
		}   
		switch (requestCode) {
		// 拍照获取图片
		case ImageUtils.GET_IMAGE_BY_CAMERA:
			// uri传入与否影响图片获取方式,以下二选一
			// 方式一,自定义Uri(ImageUtils.imageUriFromCamera),用于保存拍照后图片地址
			if(ImageUtils.imageUriFromCamera != null) {
				// 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
//				iv_mine_head.setImageURI(ImageUtils.imageUriFromCamera);
				// 对图片进行裁剪
				ImageUtils.cropImage(this, ImageUtils.imageUriFromCamera);
				break;
			}
			
			break;
		// 手机相册获取图片
		case ImageUtils.GET_IMAGE_FROM_PHONE:
			if(data != null && data.getData() != null) {
				// 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
				// iv.setImageURI(data.getData());
				// 对图片进行裁剪
				ImageUtils.cropImage(this, data.getData());
				
			}
			break;
		// 裁剪图片后结果
		case ImageUtils.CROP_IMAGE:
			if(ImageUtils.cropImageUri != null) {
				LogUtils.i("ImageUtils.cropImageUri"+ImageUtils.cropImageUri);
				// 可以直接显示图片,或者进行其他处理(如压缩等)
				iv_mine_head.setImageURI(ImageUtils.cropImageUri);
				RequestParams params = new RequestParams();  
				params.addQueryStringParameter("imgType", 0 + "");
//				params.addBodyParameter("file", new File(FileUtil.getSDPath() + "/E鲜商城/head.jpg"));
				params.addBodyParameter("file", new File(ImageUtils.getFileByUri(MineInformationActivity.this, ImageUtils.cropImageUri)+""));
				doHttp(1, MyConst.uploadAction, params, uploadhead);
			}
			break;
		default:
			break;
		}

//			switch (requestCode) {
//			case IMAGE_REQUEST_CODE:
//				startPhotoZoom(data.getData());
//				break;
//			case CAMERA_REQUEST_CODE:
//				boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//				if (hasSDCard) {
//					File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//					File tempFile = new File(path, IMAGE_FILE_NAME);
//					startPhotoZoom(Uri.fromFile(tempFile));
//				} else {
//					Toast.makeText(MineInformationActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
//				}
//				break;
//			case RESULT_REQUEST_CODE: // 图片缩放完成后
//				if (data != null) {
//					getImageToView(data);
//				}
//				break;
//			}
//		}

	}

	public class SelectHeadPopupWindows extends PopupWindow {

		public SelectHeadPopupWindows(Context mContext, View parent) {
			View view = View.inflate(mContext, R.layout.popupwindow_head_select, null);
			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.MATCH_PARENT);

			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			TextView tv_from_takephoto = (TextView) view.findViewById(R.id.tv_from_takephoto);
			tv_from_takephoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
//					Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					// 判断存储卡是否可以用，可用进行存储
//					boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//					if (hasSDCard) {
//						File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//						File file = new File(path, IMAGE_FILE_NAME);
//						intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//					}
//					startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
					ImageUtils.openCameraImage(MineInformationActivity.this);
				}
			});
			TextView tv_from_photoalbum = (TextView) view.findViewById(R.id.tv_from_photoalbum);
			tv_from_photoalbum.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
//					Intent intentFromGallery = new Intent();
//					intentFromGallery.setType("image/*"); // 设置文件类型
//					intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
					ImageUtils.openLocalImage(MineInformationActivity.this);
//					startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
					
				}
			});
			TextView tv_popupwindow_cancel = (TextView) view.findViewById(R.id.tv_popupwindow_cancel);
			tv_popupwindow_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}

//	/**
//	 * 裁剪图片方法实现
//	 * 
//	 * @param uri
//	 */
//	public void startPhotoZoom(Uri uri) {
//
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		// 设置裁剪
//		intent.putExtra("crop", "true");
//		// aspectX aspectY 是宽高的比例
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		// outputX outputY 是裁剪图片宽高
//		intent.putExtra("outputX", 150);
//		intent.putExtra("outputY", 150);
//		intent.putExtra("return-data", true);
//		startActivityForResult(intent, RESULT_REQUEST_CODE);
//	}
//
//	/**
//	 * 保存裁剪之后的图片数据
//	 * 
//	 * @param picdata
//	 */
//	private void getImageToView(Intent data) {
//		Bundle extras = data.getExtras();
//		if (extras != null) {
//			Bitmap photo = extras.getParcelable("data");
//			ImageUtils.SaveBitmap(photo, "head.jpg");
//			Drawable drawable = new BitmapDrawable(this.getResources(), photo);
//			iv_mine_head.setImageDrawable(drawable);
//			RequestParams params = new RequestParams();
//			params.addQueryStringParameter("imgType", 0 + "");
////			params.addBodyParameter("file", new File(FileUtil.getSDPath() + "/E鲜商城/head.jpg"));
//			params.addBodyParameter("file", new File(ImageUtils.getFileByUri(MineInformationActivity.this, ImageUtils.cropImageUri)+""));
//			doHttp(1, MyConst.uploadAction, params, uploadhead);
//		}
//	}

}
