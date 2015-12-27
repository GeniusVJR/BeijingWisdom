package com.tc.beijingwisdom.menudetail;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tc.beijingwisdom.R;
import com.tc.beijingwisdom.activity.MainActivity;
import com.tc.beijingwisdom.base.BaseMenuDetailPager;
import com.tc.beijingwisdom.base.TabDetailPager;
import com.tc.beijingwisdom.domain.NewsData.NewsTabData;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

/**
 * 菜单详情页－互动
 * @author dream
 *
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener{

	
	private ViewPager mViewPager;
	
	private ArrayList<TabDetailPager> mPagerList;
	private ArrayList<NewsTabData> mNewsTabDatas;
	
	private TabPageIndicator mIndicator;
	
	public NewsMenuDetailPager(Activity activity, ArrayList<NewsTabData> children) {
		super(activity);
		mNewsTabDatas = children;
	}	

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);
		ViewUtils.inject(this, view);
		mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
//		mViewPager.setOnPageChangeListener(this);
		mIndicator.setOnPageChangeListener(this);
		return view;
	}
	
	@Override
	public void initData() {
		mPagerList = new ArrayList<TabDetailPager>();
		for(int i=0; i<mNewsTabDatas.size(); i++)
		{
			TabDetailPager pager = new TabDetailPager(mActivity, mNewsTabDatas.get(i));
//			TabDetailPager pager = new TabDetailPager(mActivity, mNewsTabDatas.get(i));
			mPagerList.add(pager);
		}
		mViewPager.setAdapter(new MenuDetailAdapter());
		mIndicator.setViewPager(mViewPager);
	}
	//跳转到下一个页面
	@OnClick(R.id.btn_next)
	public void nextPage(View view)
	{
		int currentItem = mViewPager.getCurrentItem();
		mViewPager.setCurrentItem(++currentItem);
	}
	
	class MenuDetailAdapter extends PagerAdapter
	{

		@Override
		public CharSequence getPageTitle(int position) {
			return mNewsTabDatas.get(position).title;
		}
		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			TabDetailPager pager = mPagerList.get(position);
			((ViewGroup) container).addView(pager.mRootView);
			pager.initData();
			return pager.mRootView;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View)object);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		if(arg0 == 0)
		{
			slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_FULLSCREEN);
		}
		else
		{
			slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_NONE);
		}
		
	}

}
