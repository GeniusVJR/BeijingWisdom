package com.tc.beijingwisdom.activity;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tc.beijingwisdom.R;
import com.tc.beijingwisdom.fragment.ContentFragment;
import com.tc.beijingwisdom.fragment.LeftMenuFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
/**
 * 主页面
 * @author dream
 *
 */
public class MainActivity extends SlidingFragmentActivity {
	
	private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
	private static final String FRAGMENT_CONTENT = "fragment_content";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		//设置全屏触摸
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		slidingMenu.setSecondaryMenu(R.layout.right_menu);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindOffset(200);//设置预留的宽度
		initFragment();
	}
	
	/**
	 * 初始化fragment，将fragment填充到布局文件
	 */
	private void initFragment()
	{
		android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();//开启事务
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), FRAGMENT_LEFT_MENU);
		transaction.replace(R.id.fl_content, new ContentFragment(),FRAGMENT_CONTENT);
		
		transaction.commit();
	}
	//获取侧边栏对象
	public LeftMenuFragment getLeftMenuFragment()
	{
		android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
		return fragment;
	}
	//获取主页面fragment
	public ContentFragment getContentFragment()
	{
		android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
		return fragment;
	}
	
}
