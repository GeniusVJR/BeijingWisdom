package com.tc.beijingwisdom.fragment;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tc.beijingwisdom.R;
import com.tc.beijingwisdom.base.BaseFragment;
import com.tc.beijingwisdom.base.BasePager;
import com.tc.beijingwisdom.base.impl.GovAffairsPager;
import com.tc.beijingwisdom.base.impl.HomePager;
import com.tc.beijingwisdom.base.impl.NewsCenterPager;
import com.tc.beijingwisdom.base.impl.SettingPager;
import com.tc.beijingwisdom.base.impl.SmartServicePager;

import android.R.mipmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 主页内容
 * 
 * @author dream
 *
 */
public class ContentFragment extends BaseFragment {

	@ViewInject(R.id.rg_group)
	private RadioGroup rgGroup;
	@ViewInject(R.id.vp_content)
	private ViewPager mViewPager;

	private ArrayList<BasePager> mPagerList;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		// rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
		ViewUtils.inject(this, view);

		return view;
	}

	@Override
	public void initData() {
		rgGroup.check(R.id.rb_home);// 默认勾选首页
		// 初始化五个子页面
		mPagerList = new ArrayList<BasePager>();
//		for (int i = 0; i < 5; i++) {
//			BasePager pager = new BasePager(mActivity);
//			mPagerList.add(pager);
//		}
		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new NewsCenterPager(mActivity));
		mPagerList.add(new SmartServicePager(mActivity));
		mPagerList.add(new GovAffairsPager(mActivity));
		mPagerList.add(new SettingPager(mActivity));
		

		mViewPager.setAdapter(new ContentAdapter());
		//监听RadioGroup的选择事件
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					mViewPager.setCurrentItem(0,false);
					break;
				case R.id.rb_news:
					mViewPager.setCurrentItem(1,false);
					break;
				case R.id.rb_smart:
					mViewPager.setCurrentItem(2,false);
					break;
				case R.id.rb_gov:
					mViewPager.setCurrentItem(3,false);
					break;
				case R.id.rb_setting:
					mViewPager.setCurrentItem(4,false);
					break;
				default:
					break;
				}
			}
		});
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				mPagerList.get(arg0).initData();// 获取当前被选中的页面，初始化该页面数据

			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mPagerList.get(0).initData();
	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg1 == arg0;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			BasePager pager = mPagerList.get(position);
			
			((ViewGroup) container).addView(pager.mRootView);
//			pager.initData();
			return pager.mRootView;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}
	}
	/**
	 * 获取新闻中心页面
	 * @return
	 */
	public NewsCenterPager getNewsCenterPager()
	{
		return (NewsCenterPager) mPagerList.get(1);
	}

}
