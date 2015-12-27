package com.tc.beijingwisdom.utils;

import android.content.Context;

public class DensityUtils {
	/**
	 * dp转px
	 */
	public static int dp2px(Context ctx,float dp)
	{
		float density = ctx.getResources().getDisplayMetrics().density;
		int px = (int)(dp * density + 0.5f);
		return px;
	}
	public static float px2dp(Context ctx,float px)
	{
		float density = ctx.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
	}
}
