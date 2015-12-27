package com.tc.beijingwisdom.base.impl;

import com.tc.beijingwisdom.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
/**
 * 生活
 * @author dream
 *
 */
public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		tvTitle.setText("生活");
		setSlidingMenuEnable(true);
		TextView text = new TextView(mActivity);
		text.setText("智慧服务");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		//向Framlayout中动态添加布局
		flContent.addView(text);
	}

}
