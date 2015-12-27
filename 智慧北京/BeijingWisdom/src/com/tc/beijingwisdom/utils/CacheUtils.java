package com.tc.beijingwisdom.utils;

import android.content.Context;

/**
 * 缓存工具类
 * @author dream
 *
 */
public class CacheUtils {
	/**
	 * 设置缓存
	 */
	public static void setCache(String key,String value, Context ctx){
		PrefUtils.setString(ctx, key, value);
		//可以将缓存
	}
	/**
	 * 获取缓存
	 * @return 
	 */
	public static String getCache(String key, Context ctx)
	{
		return PrefUtils.getString(ctx, key, null);
	}
}
