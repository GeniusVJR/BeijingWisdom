package com.tc.beijingwisdom.base.impl;

import com.tc.beijingwisdom.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
/**
 * 人口管理
 * @author dream
 *
 */
public class GovAffairsPager extends BasePager {

	public GovAffairsPager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		tvTitle.setText("人口管理");
		setSlidingMenuEnable(true);
		TextView text = new TextView(mActivity);
		text.setText("政务");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		//向Framlayout中动态添加布局
		flContent.addView(text);
	}

}
