package com.tc.beijingwisdom.utils.bitmap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存
 * 
 * @author dream
 *
 */
public class MemoryCacheUtils {

	// public HashMap<String, SoftReference<Bitmap>> mMemoryCache = new
	// HashMap<String, SoftReference<Bitmap>>();
	public LruCache<String, Bitmap> mMemoryCache;

	public MemoryCacheUtils() {
		long maxMemory = Runtime.getRuntime().maxMemory() / 8;// 模拟器默认16M
		mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory)) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				int byteCount = value.getRowBytes() * value.getHeight();
				return byteCount;
			}
		};
	}

	/**
	 * 从内存读
	 * 
	 * @param url
	 */
	public Bitmap getBitmapFromMemory(String url) {
		// SoftReference<Bitmap> softReference = mMemoryCache.get(url);
		// if(softReference != null)
		// {
		// Bitmap bitmap = softReference.get();
		// return bitmap;
		// }
		return mMemoryCache.get(url);
		

	}

	/**
	 * 从设置内存读
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void setBitmapToMemory(String url, Bitmap bitmap) {
		mMemoryCache.put(url, bitmap);
	}
}
