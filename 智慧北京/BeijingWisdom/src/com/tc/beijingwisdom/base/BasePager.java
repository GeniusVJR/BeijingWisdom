package com.tc.beijingwisdom.base;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tc.beijingwisdom.R;
import com.tc.beijingwisdom.activity.MainActivity;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 主页下5个子页面的基类
 * @author dream
 *
 */
public class BasePager {
	
	public Activity mActivity;
	public View mRootView;
	
	public TextView tvTitle;
	
	public FrameLayout flContent;
	
	public ImageButton btnMenu;
	public ImageButton btnPhoto; // 组图切换按钮
	
	public BasePager(Activity activity)
	{
		this.mActivity = activity;
		initViews();
	}
	/**
	 * 初始化布局
	 */
	public void initViews(){
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
		flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
		btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
		btnPhoto = (ImageButton)mRootView.findViewById(R.id.btn_photo);
		btnMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggleSlidingMenu();
			}
		});
	}
	
	private void toggleSlidingMenu() {
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();//切换状态，显示时隐藏，隐藏时显示
	}
	
	/**
	 * 初始化数据
	 */
	public void initData(){
		
	}
	
	/**
	 * 设置侧边栏开启或关闭
	 * @param enable
	 */
	public void setSlidingMenuEnable(boolean enable)
	{
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		if(enable)
		{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}
		else
		{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		
		
	}
	
	
}
