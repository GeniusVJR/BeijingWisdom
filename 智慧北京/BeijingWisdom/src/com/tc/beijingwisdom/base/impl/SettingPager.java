package com.tc.beijingwisdom.base.impl;

import com.tc.beijingwisdom.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
/**
 * 设置
 * @author dream
 *
 */
public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		tvTitle.setText("设置");
		btnMenu.setVisibility(View.GONE);
		setSlidingMenuEnable(false);
		TextView text = new TextView(mActivity);
		text.setText("设置");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		//向Framlayout中动态添加布局
		flContent.addView(text);
	}

}
