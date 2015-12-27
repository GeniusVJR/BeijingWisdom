package com.tc.beijingwisdom.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页
 * @author dream
 *
 */
public abstract class BaseMenuDetailPager {
	
	public Activity mActivity;
	
	public View mRootView;
	
	public BaseMenuDetailPager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}
	
	public abstract View initView();
	
	public void initData(){
		
	}
}
