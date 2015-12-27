package com.tc.beijingwisdom.activity;

import java.util.ArrayList;

import com.tc.beijingwisdom.R;
import com.tc.beijingwisdom.R.drawable;
import com.tc.beijingwisdom.R.id;
import com.tc.beijingwisdom.R.layout;
import com.tc.beijingwisdom.utils.DensityUtils;
import com.tc.beijingwisdom.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {
	
	private ViewPager vpGuide;
	private static final int[] mImageIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
	ArrayList<ImageView> mImageViewList;
	
	private LinearLayout llPointGroup;
	
	private int mPointWidth;
	private View viewRedPoint;
	
	private Button btnStart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
		viewRedPoint = findViewById(R.id.view_red_point);
		btnStart = (Button) findViewById(R.id.btn_start);
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//更新sp，表示已经展示了新手引导
				PrefUtils.setBoolean(GuideActivity.this, "is_user_guide_showed", true);
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
				finish();
			}
		});
		initViews();
		vpGuide.setAdapter(new GuideAdapter());
		vpGuide.setOnPageChangeListener(new GuidePageListener());
	}
	
	private void initViews()
	{
		mImageViewList = new ArrayList<ImageView>();
		//初始化引导页的3个页面
		for (int i =0; i<mImageIds.length; i++) {
			ImageView image = new ImageView(this);
			//设置背景页
			image.setBackgroundResource(mImageIds[i]);
			mImageViewList.add(image);
			
		}
		//初始化引导页的小圆点
		for (int i =0; i<mImageIds.length; i++) {
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_gray);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dp2px(this, 10),DensityUtils.dp2px(this, 10));
			if(i>0)
			{
				params.leftMargin = DensityUtils.dp2px(this, 10);
			}
			point.setLayoutParams(params);
			
			llPointGroup.addView(point);
			
		}
		
		//获取视图树
		llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			//layout执行结束后回调此方法
			@Override
			public void onGlobalLayout() {
				
				llPointGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mPointWidth = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
				
			}
		});
	}
	
	
	class GuideAdapter extends PagerAdapter
	{

		@Override
		public int getCount() {
			return mImageIds.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewGroup) container).addView(mImageViewList.get(position));
			return mImageViewList.get(position);
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View)object);
		}
		
	}
	
	/**
	 * viewpager的滑动监听
	 * @author dream
	 *
	 */
	class GuidePageListener implements OnPageChangeListener
	{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int arg2) {
			int len = (int) (mPointWidth * positionOffset) + position * mPointWidth;
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();
			params.leftMargin = len;//设置左边距
			viewRedPoint.setLayoutParams(params);//重新给小红点设置布局
			
		}

		@Override
		public void onPageSelected(int position) {
			if(position == mImageIds.length - 1)
			{
				btnStart.setVisibility(View.VISIBLE);
			}
			else{
				btnStart.setVisibility(View.INVISIBLE);
			}
		}
		
	}
}
