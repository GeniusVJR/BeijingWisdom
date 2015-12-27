package com.tc.beijingwisdom.utils.bitmap;

import com.tc.beijingwisdom.R;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * 自定义图片加载工具
 * @author dream
 *
 */
public class MyBitmapUtils {
	
	NetCacheUtils mNetCacheUtils;
	LocalCacheUtils mLocalCacheUtils;
	MemoryCacheUtils mMemoryCacheUtils;
	
	public MyBitmapUtils() {
		mMemoryCacheUtils = new MemoryCacheUtils();
		mLocalCacheUtils = new LocalCacheUtils();
		mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
		
	}

	public void display(ImageView ivPic, String url) {
		ivPic.setImageResource(R.drawable.news_pic_default);
		Bitmap bitmap = null;
		bitmap = mMemoryCacheUtils.getBitmapFromMemory(url);
		if(bitmap != null)
		{
			ivPic.setImageBitmap(bitmap);
			return;
		}
		//从本地读
		bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
		if(bitmap != null)
		{
			ivPic.setImageBitmap(bitmap);
			mMemoryCacheUtils.setBitmapToMemory(url, bitmap);   //将图片保存在内存
			return;
		}
		//从过网络读
		mNetCacheUtils.getBitmapFromNet(ivPic, url);
	}
	
}
