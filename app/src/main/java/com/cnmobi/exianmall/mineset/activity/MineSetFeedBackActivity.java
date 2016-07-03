package com.cnmobi.exianmall.mineset.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.BaseActivity;
import com.cnmobi.exianmall.base.MyApplication;
import com.cnmobi.exianmall.base.MyConst;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的设置-反馈界面
 */
public class MineSetFeedBackActivity extends BaseActivity {
	@ViewInject(R.id.et_feedback)
	EditText et_feedback;

	/**
	 * 反馈接口标识
	 */
	public static final int feedback = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_mine_set_feedback);
		ViewUtils.inject(this);

		setTitleText("意见反馈");
		setRightTextViewText("清空");
		setRightTextViewClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_feedback.setText("");
			}
		});
	}

	@OnClick(R.id.btn_feedback_commit)
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_feedback_commit:
			if (isNull(getStr(et_feedback))) {
				showToast("请输入您的宝贵意见！！");
				return;
			}
			feedbackHttp();
			break;

		default:
			break;
		}
	}

	void feedbackHttp() {
		RequestParams params = new RequestParams();
//		params.addBodyParameter("idFeedback", MyApplication.getApp().getIdNumber() + "");
		params.addBodyParameter("feedcontent", getStr(et_feedback));
		doHttp(1, MyConst.userFeedbackAction, params, feedback);
	}

	@Override
	public void callback(String jsonString, int flag) {
		super.callback(jsonString, flag);
		switch (flag) {
		case feedback:
			finish();
			showToast("亲爱的用户，您的宝贵意见已成功提交，我们会尽快优化的哦~谢谢您！");
			break;

		default:
			break;
		}
	}
}
