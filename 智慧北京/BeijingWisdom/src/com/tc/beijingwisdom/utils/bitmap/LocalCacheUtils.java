package com.tc.beijingwisdom.utils.bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.tc.beijingwisdom.utils.MD5Encoder;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 本地缓存
 * @author dream
 *
 */
public class LocalCacheUtils {
	
	public static final String CACHE_PATH = Environment.getExternalStorageDirectory().
			getAbsolutePath() + "/beijingwisdom";
	/**
	 * 从本地sdcard读图片
	 * @param url
	 */
	public Bitmap getBitmapFromLocal(String url)
	{
		try {
			String fileName = MD5Encoder.encode(url);
			File file = new File(CACHE_PATH,fileName);
			if(file.exists())
			{
				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
				return bitmap;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 向sdcard写图片
	 */
	public void setBitmapToLocal(String url,Bitmap bitmap)
	{
		try {
			String fileName = MD5Encoder.encode(url);
			File file = new File(CACHE_PATH,fileName);
			File parentFile = file.getParentFile();
			if(!parentFile.exists())
			{
				parentFile.mkdirs();
			}
			
			//将图片保存在本地
			bitmap.compress(CompressFormat.JPEG
					, 100, new FileOutputStream(parentFile) );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
