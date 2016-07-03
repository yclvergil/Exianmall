package com.cnmobi.exianmall.login.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.cnmobi.exianmall.R;
import com.cnmobi.exianmall.base.MyApplication;
import com.lidroid.xutils.util.LogUtils;

public class Fragment3 extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_3, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Button button = (Button) getView().findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LogUtils.i("点击了！");
				SharedPreferences sharedPreferences = getActivity().getSharedPreferences("eXianIsFirstIn",
						Context.MODE_PRIVATE + Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit(); 
				editor.putBoolean("isFirstIn", false);
				editor.commit();// 提交修改
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
	}

}
