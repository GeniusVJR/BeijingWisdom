package com.tc.beijingwisdom.menudetail;

import com.tc.beijingwisdom.base.BaseMenuDetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 菜单详情页－新闻
 * @author dream
 *
 */

public class TopicMenuDetailPager extends BaseMenuDetailPager {

	public TopicMenuDetailPager(Activity activity) {
		super(activity);
		
	}	

	@Override
	public View initView() {
		TextView text = new TextView(mActivity);
		text.setText("菜单详情页－主题");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		return text;
	}

}
