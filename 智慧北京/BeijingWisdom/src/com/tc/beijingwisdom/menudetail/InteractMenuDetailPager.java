package com.tc.beijingwisdom.menudetail;

import com.tc.beijingwisdom.base.BaseMenuDetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 菜单详情页－组图
 * @author dream
 *
 */

public class InteractMenuDetailPager extends BaseMenuDetailPager {

	public InteractMenuDetailPager(Activity activity) {
		super(activity);
		
	}	

	@Override
	public View initView() {
		TextView text = new TextView(mActivity);
		text.setText("菜单详情页－互动");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		return text;
	}

}
