package com.tc.beijingwisdom.base.impl;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tc.beijingwisdom.activity.MainActivity;
import com.tc.beijingwisdom.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
/**
 * 首页
 * @author dream
 *
 */
public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		tvTitle.setText("智慧北京");
		btnMenu.setVisibility(View.GONE);
		setSlidingMenuEnable(false);//关闭侧边栏
		TextView text = new TextView(mActivity);
		text.setText("首页");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		//向Framlayout中动态添加布局
		flContent.addView(text);
	}
	
	

}
