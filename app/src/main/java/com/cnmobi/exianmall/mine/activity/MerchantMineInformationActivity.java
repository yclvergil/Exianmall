package com.cnmobi.exianmall.mine.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.R.drawable;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MainActivity;
import com.cnmobi.exianmall.base.MainActivityMerchant;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.cnmobi.exianmall.fragment.MerchantMineFragment;
import com.cnmobi.exianmall.utils.FileUtil;
import com.cnmobi.exianmall.utils.ImageUtils;
import com.cnmobi.exianmall.utils.SelectPictureActivity;
import com.cnmobi.exianmall.widget.MerchantMineInfoTimeButton;
import com.cnmobi.exianmall.widget.RoundImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 卖家-我的资料界面
 */
public class MerchantMineInformationActivity extends BaseActivity {

	@ViewInject(R.id.iv_mine_head)
	private RoundImageView iv_mine_head;// 头像
	@ViewInject(R.id.tv_bank_select)
	EditText tv_bank_select;
	@ViewInject(R.id.iv_uploading_business_license)
	ImageView iv_uploading_business_license;
	@ViewInject(R.id.iv_uploading_idcard_front)
	ImageView iv_uploading_idcard_front;
	@ViewInject(R.id.iv_uploading_idcard_back)
	ImageView iv_uploading_idcard_back;
	@ViewInject(R.id.textView3)
	TextView tv_titlephone;
	@ViewInject(R.id.textView2)
	TextView tv_titlename;
	@ViewInject(R.id.edt_enterprise_name)
	EditText edt_enterprise_name;// 企业名称
	@ViewInject(R.id.edt_regist_number)
	EditText edt_regist_number;// 注册号
	@ViewInject(R.id.edt_code)
	EditText edt_code;// 组织机构代码
	@ViewInject(R.id.edt_brand)
	EditText edt_brand;// 品牌
	@ViewInject(R.id.edt_name)
	EditText edt_name;// 法定人姓名
	@ViewInject(R.id.edt_idnumber)
	EditText edt_idnumber;// 法定人身份证号
	@ViewInject(R.id.edt_valid)
	EditText edt_valid;// 证件有效期
	@ViewInject(R.id.edt_phone)
	public static EditText edt_phone;// 法定人电话
	@ViewInject(R.id.edt_vercode)
	EditText edt_vercode;// 验证码
	@ViewInject(R.id.edt_qqnumber)
	EditText edt_qqnumber;// 法定人QQ
	@ViewInject(R.id.edt_wechatnumber)
	EditText edt_wechatnumber;// 法定人微信
	@ViewInject(R.id.edt_phone1)
	EditText edt_phone1;// 基地联系号码
	@ViewInject(R.id.edt_qqnumber1)
	EditText edt_qqnumber1;// 基地QQ
	@ViewInject(R.id.edt_wechatnumber1)
	EditText edt_wechatnumber1;// 基地微信号
	@ViewInject(R.id.edt_band_account)
	EditText edt_band_account;// 银行账号
	@ViewInject(R.id.edt_account_name)
	EditText edt_account_name;// 银行开户名
	@ViewInject(R.id.edt_last_time)
	EditText edt_last_time;// 订单最后确认时间
	@ViewInject(R.id.edt_deal_time)
	EditText edt_deal_time;// 处理订单所需时间
	@ViewInject(R.id.edt_arrival_time)
	EditText edt_arrival_time;// 最快到货时间
	@ViewInject(R.id.edt_address)
	EditText edt_address;// 地址
	@ViewInject(R.id.edt_band_address)
	EditText edt_band_address;// 银行所在地
	@ViewInject(R.id.edt_name1)
	EditText edt_name1;// 基地联系人
	@ViewInject(R.id.tv_sendcode)
	MerchantMineInfoTimeButton tv_sendcode;
	public static  Boolean isValid = false;// 默认手机号已注册，可以获取验证码
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 1;
	private static final int CAMERA_REQUEST_CODE = 2;
	private static final int RESULT_REQUEST_CODE = 3;
	private static final int business_license = 4;
	private static final int idcard_front = 5;
	private static final int idcard_back = 6;

	private String busiceimg = "";// 营业执照地址
	private String frontimg = "";// 身份证正面地址
	private String backimg = "";// 身份证反面地址

	/**
	 * 查看卖家信息
	 */
	public static final int checkdata = 7;
	/**
	 * 上传营业执照
	 */
	public static final int uploadbusice = 8;
	/**
	 * 上传正面身份证
	 */
	public static final int uploadfront = 9;
	/**
	 * 上传反面身份证
	 */
	public static final int uploadback = 10;
	/**
	 * 保存卖家信息
	 */
	public static final int savedata = 11;
	/**
	 * 发送验证码
	 */
	public static final int sendcode = 12;
	/**
	 * 上传头像
	 */
	public static final int uploadhead = 13;
	/**
	 * 保存头像信息
	 */
	public static final int savehead = 14;
//	/**
//	 * 验证手机号接口标识
//	 */
//	public static final int checkPhone = 15;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merachant_mine_information);
		ViewUtils.inject(this);
		
		if(!"".equals(MyApplication.getInstance().getUserlogo())){
			MyConst.imageLoader.displayImage(MyApplication.getInstance().getUserlogo(), iv_mine_head, MyConst.IM_IMAGE_OPTIONS);
		}else{
			iv_mine_head.setImageResource(drawable.logo);
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("idUser", MyApplication.getInstance().getIdNumber() + "");
		doHttp(1, MyConst.sellerInformationQueryAction, params, checkdata);
		
		edt_phone.addTextChangedListener(watcher);
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (getStr(edt_phone).length() == 11) {
				if(isMobile(getStr(edt_phone))){
					isValid = true;
				}else{
					isValid = false;
				}
//				checkPhoneHttp();
			} else {
				isValid = false;
			}
		}

	};
    /**
     * 验证手机号是否有效正则    */
//    public static final String REGEX_MOBILE = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$";
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }
//	void checkPhoneHttp() {
//		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("phone", getStr(edt_phone));
//		doHttp(1, MyConst.verifyAction, params, checkPhone);
//	}
	@OnClick({ R.id.ly_common_head_return, R.id.iv_mine_head, R.id.tv_mine_information_commit,
			R.id.btn_merchant_info_commit, R.id.iv_uploading_business_license, R.id.iv_uploading_idcard_front,
			R.id.iv_uploading_idcard_back })
	void onClick(View v) {
		switch (v.getId()) {
		case R.id.ly_common_head_return:
			finish();
			break;
		case R.id.tv_mine_information_commit:
			saveDate();
			break;
		case R.id.iv_mine_head:
			new SelectHeadPopupWindows(MerchantMineInformationActivity.this, iv_mine_head);
			break;
		case R.id.btn_merchant_info_commit:
			saveDate();
			break;
		case R.id.iv_uploading_business_license:
			Intent i = new Intent(MerchantMineInformationActivity.this, SelectPictureActivity.class);
			i.putExtra("intent_max_num", 1);
			startActivityForResult(i, business_license);
			break;
		case R.id.iv_uploading_idcard_front:
			Intent i1 = new Intent(MerchantMineInformationActivity.this, SelectPictureActivity.class);
			i1.putExtra("intent_max_num", 1);
			startActivityForResult(i1, idcard_front);
			break;
		case R.id.iv_uploading_idcard_back:
			Intent i2 = new Intent(MerchantMineInformationActivity.this, SelectPictureActivity.class);
			i2.putExtra("intent_max_num", 1);
			startActivityForResult(i2, idcard_back);
			break;
//		case R.id.tv_sendcode:
//			RequestParams params = new RequestParams();
//			params.addQueryStringParameter("phone", getStr(edt_phone));
//			doHttp(1, MyConst.sendCodeAction, params, sendcode);
//			break;
		
		default:
			break;
		}
	}


	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		RequestParams params;
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
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
//					Toast.makeText(MerchantMineInformationActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
//				}
//				break;
//			case RESULT_REQUEST_CODE: // 图片缩放完成后
//				if (data != null) {
//					getImageToView(data, iv_mine_head);
//				}
//				break;

			// 拍照获取图片
			case ImageUtils.GET_IMAGE_BY_CAMERA:
				// uri传入与否影响图片获取方式,以下二选一
				// 方式一,自定义Uri(ImageUtils.imageUriFromCamera),用于保存拍照后图片地址
				if(ImageUtils.imageUriFromCamera != null) {
					// 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
//					iv.setImageURI(ImageUtils.imageUriFromCamera);
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
					// 可以直接显示图片,或者进行其他处理(如压缩等)
					iv_mine_head.setImageURI(ImageUtils.cropImageUri);
					RequestParams params1 = new RequestParams();
					params1.addQueryStringParameter("imgType", 1 + "");
//					params.addBodyParameter("file", new File(FileUtil.getSDPath() + "/E鲜商城/head.jpg"));
					params1.addBodyParameter("file", new File(ImageUtils.getFileByUri(MerchantMineInformationActivity.this, ImageUtils.cropImageUri)+""));
					doHttp(1, MyConst.uploadAction, params1, uploadhead);
				}
				break;
			
			case business_license:
				ArrayList<String> list = new ArrayList<String>();
				list = (ArrayList<String>) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
				showImg(iv_uploading_business_license, "file://" + list.get(0));
				params = new RequestParams();
				params.addQueryStringParameter("imgType", 1 + "");
				params.addBodyParameter("file", new File(list.get(0)));
				doHttp(1, MyConst.uploadAction, params, uploadbusice);
				break;
			case idcard_front:
				ArrayList<String> list1 = new ArrayList<String>();
				list1 = (ArrayList<String>) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
				showImg(iv_uploading_idcard_front, "file://" + list1.get(0));
				params = new RequestParams();
				params.addQueryStringParameter("imgType", 1 + "");
				params.addBodyParameter("file", new File(list1.get(0)));
				doHttp(1, MyConst.uploadAction, params, uploadfront);
				break;
			case idcard_back:
				ArrayList<String> list2 = new ArrayList<String>();
				list2 = (ArrayList<String>) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
				showImg(iv_uploading_idcard_back, "file://" + list2.get(0));
				params = new RequestParams();
				params.addQueryStringParameter("imgType", 1 + "");
				params.addBodyParameter("file", new File(list2.get(0)));
				doHttp(1, MyConst.uploadAction, params, uploadback);
				break;
			}
		}

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
					
					ImageUtils.openCameraImage(MerchantMineInformationActivity.this);
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
//					startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
					ImageUtils.openLocalImage(MerchantMineInformationActivity.this);
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
//	private void getImageToView(Intent data, ImageView imageView) {
//		Bundle extras = data.getExtras();
//		if (extras != null) {
//			Bitmap photo = extras.getParcelable("data");
//			ImageUtils.SaveBitmap(photo, "head.jpg");
//			Drawable drawable = new BitmapDrawable(this.getResources(), photo);
//			imageView.setImageDrawable(drawable);
//			RequestParams params = new RequestParams();
//			params.addQueryStringParameter("imgType", 1 + "");
//			params.addBodyParameter("file", new File(FileUtil.getSDPath() + "/E鲜商城/head.jpg"));
//			doHttp(1, MyConst.uploadAction, params, uploadhead);
//		}
//	}

	void saveDate() {
		if (isNull(busiceimg)) {
			showToast("请上传营业执照照片");
			return;
		}
		if (isNull(frontimg)) {
			showToast("请上传身份证正面照片");
			return;
		}
		if (isNull(busiceimg)) {
			showToast("请上传身份证反面照片");
			return;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("corpname", getStr(edt_enterprise_name));
		params.addBodyParameter("creationNo", getStr(edt_regist_number));
		params.addBodyParameter("organization", getStr(edt_code));
		params.addBodyParameter("brandname", getStr(edt_brand));
		params.addBodyParameter("legalname", getStr(edt_name));
		params.addBodyParameter("id", getStr(edt_idnumber));
		params.addBodyParameter("validtime", getStr(edt_valid));
		params.addBodyParameter("legalphoneNo", getStr(edt_phone));
		params.addBodyParameter("code", getStr(edt_vercode));
		params.addBodyParameter("legalqq", getStr(edt_qqnumber));
		params.addBodyParameter("legalwechat", getStr(edt_wechatnumber));
		params.addBodyParameter("accountname", getStr(edt_account_name));
		params.addBodyParameter("accountbank", getStr(tv_bank_select));
		params.addBodyParameter("bankaccount", getStr(edt_band_account));
		params.addBodyParameter("validordertime", getStr(edt_last_time));
		params.addBodyParameter("handleordertime", getStr(edt_deal_time));
		params.addBodyParameter("deliverytime", getStr(edt_arrival_time));
		params.addBodyParameter("contactqq", getStr(edt_qqnumber1));
		params.addBodyParameter("contactwechat", getStr(edt_wechatnumber1));
		params.addBodyParameter("contactphoneNo", getStr(edt_phone1));
		params.addBodyParameter("deliverytime", getStr(edt_arrival_time));
		params.addBodyParameter("businesslic", busiceimg);
		params.addBodyParameter("idpositive", frontimg);
		params.addBodyParameter("idopposite", backimg);
		params.addBodyParameter("address", getStr(edt_address));
		params.addBodyParameter("contactname", getStr(edt_name1));
		params.addBodyParameter("bankaddress", getStr(edt_band_address));
		params.addBodyParameter("idNumber", MyApplication.getInstance().getIdNumber() + "");
		doHttp(1, MyConst.sellerMsgAction, params, savedata);
	}

	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		switch (flag) {
		case checkdata:
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				edt_enterprise_name.setText(jsonObject.getString("corpname"));
				edt_regist_number.setText(jsonObject.getString("creationNo"));
				edt_code.setText(jsonObject.getString("organization"));
				edt_brand.setText(jsonObject.getString("brand"));
				edt_name.setText(jsonObject.getString("legalname"));
				edt_idnumber.setText(jsonObject.getString("id"));
				edt_valid.setText(jsonObject.getString("validtime"));
				edt_phone.setText(jsonObject.getString("legalphoneNo"));
				edt_qqnumber.setText(jsonObject.getString("legalqq"));
				edt_wechatnumber.setText(jsonObject.getString("legalwechat"));
				edt_phone1.setText(jsonObject.getString("contactphoneNo"));
				edt_qqnumber1.setText(jsonObject.getString("contactqq"));
				edt_wechatnumber1.setText(jsonObject.getString("contactwechat"));
				edt_account_name.setText(jsonObject.getString("accountname"));
				tv_bank_select.setText(jsonObject.getString("accountbank"));
				edt_band_account.setText(jsonObject.getString("bankaccount"));
				edt_last_time.setText(jsonObject.getString("validordertime"));
				edt_deal_time.setText(jsonObject.getString("handleordertime"));
				edt_arrival_time.setText(jsonObject.getString("deliverytime"));
				edt_address.setText(jsonObject.getString("address"));
				edt_band_address.setText(jsonObject.getString("bankaddress"));
				edt_name1.setText(jsonObject.getString("contactname"));
				tv_titlephone.setText(jsonObject.getString("contactphoneNo"));
				tv_titlename.setText(jsonObject.getString("corpname"));

				if (!isNull(jsonObject.getString("businesslic"))) {
					showImg(iv_uploading_business_license, jsonObject.getString("businesslic"));
					String data[] = jsonObject.getString("businesslic").split("/");
					busiceimg = data[data.length - 1];
				}
				if (!isNull(jsonObject.getString("idopposite"))) {
					showImg(iv_uploading_idcard_back, jsonObject.getString("idopposite"));
					String data[] = jsonObject.getString("idopposite").split("/");
					backimg = data[data.length - 1];
				}
				if (!isNull(jsonObject.getString("idpositive"))) {
					showImg(iv_uploading_idcard_front, jsonObject.getString("idpositive"));
					String data[] = jsonObject.getString("idpositive").split("/");
					frontimg = data[data.length - 1];
				}
				if (!isNull(jsonObject.getString("corplogo"))) {
					showImg(iv_mine_head, jsonObject.getString("corplogo"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;

		case uploadbusice:
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				busiceimg = (String) jsonArray.get(0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case uploadfront:
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				frontimg = (String) jsonArray.get(0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case uploadback:
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				backimg = (String) jsonArray.get(0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;

		case savedata:
			showToast("您已成功提交商家身份认证，请耐心等待管理员审核，系统将在1个工作日内审核。未通过审核将不能进行其他操作！");
			finish();
			break;
		case sendcode:
			edt_vercode.setText(jsonString);
			break;
		case uploadhead:   
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				RequestParams params = new RequestParams();
				params.addQueryStringParameter("userId", MyApplication.getInstance().getIdNumber() + "");
				params.addQueryStringParameter("userType", "1");
				params.addQueryStringParameter("imgName", (String) jsonArray.get(0));
				doHttp(1, MyConst.modifyHeadPortraitAction, params, savehead);
//				FileUtil.deleteFile(MerchantMineInformationActivity.this, FileUtil.getSDPath() + "/E鲜商城/head.jpg");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case savehead:
			showToast("头像保存成功");
			LogUtils.i(jsonString);
			MyApplication.getInstance().setUserlogo(jsonString);
			showToast("头像保存成功");
			Intent intent = new Intent();
			intent.setAction(MainActivityMerchant.update_heads);
			sendBroadcast(intent);	
			Intent intent1 = new Intent();
			intent1.setAction(MerchantMineFragment.updates_heads);
			sendBroadcast(intent1);	
			break;
//		case checkPhone:
//			isValid = true;
//			break;
		default:
			break;
		}
	}
}
