package com.tc.beijingwisdom.activity;

import com.tc.beijingwisdom.R;
import com.tc.beijingwisdom.R.id;
import com.tc.beijingwisdom.R.layout;
import com.tc.beijingwisdom.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

	RelativeLayout rlRoot;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		startAnim();
	}
	
	/**
	 * 开启动画
	 */
	private void startAnim()
	{
		//动画集合
		AnimationSet set = new AnimationSet(false);
		RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF
				, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(2000);//动画时间
		rotate.setFillAfter(true);//保持动画状态
		
		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF
				, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(2000);//动画时间
		scale.setFillAfter(true);//保持动画状态
		
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(2000);//动画时间
		alpha.setFillAfter(true);//保持动画状态
		
		set.addAnimation(rotate);
		set.addAnimation(scale);
		set.addAnimation(alpha);
		
		//设置动画监听
		set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				jumpNextPage();
			}
		});
		rlRoot.startAnimation(set);
	}
	
	private void jumpNextPage()
	{
		//判断之前有没有显示过新手引导
		boolean userGuide = PrefUtils.getBoolean(this, "is_user_guide_showed", false);
		
		if(!userGuide)
		{
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));
			
			finish();
		}
		else
		{
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
		}
		
	}
}
