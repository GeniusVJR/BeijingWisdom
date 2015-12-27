package com.tc.beijingwisdom.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * 页签水平滑动的viewpager
 * @author dream
 *
 */
public class HorizontalViewPager extends ViewPager{

	public HorizontalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HorizontalViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/**
     * 事件分发, 请求父控件及祖宗控件不要拦截事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	if(getCurrentItem() != 0)
    	{
    		getParent().requestDisallowInterceptTouchEvent(true);//不拦截
    	}
    	else{//如果是第一个页面，需要显示侧边栏请求父控件
    		getParent().requestDisallowInterceptTouchEvent(false);// 用getParent去请求,拦截
    	}
    	
        return super.dispatchTouchEvent(ev);
    }

}
