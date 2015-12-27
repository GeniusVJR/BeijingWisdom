package com.tc.beijingwisdom.utils.bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 网络缓存
 * @author dream
 *
 */
public class NetCacheUtils {

	private HttpURLConnection conn = null;
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	public NetCacheUtils(LocalCacheUtils mLocalCacheUtils, MemoryCacheUtils mMemoryCacheUtils) {
		this.mLocalCacheUtils = mLocalCacheUtils;
		this.mMemoryCacheUtils = mMemoryCacheUtils;
	}

	/**
	 * 从网络下载图片
	 * @param ivPic
	 * @param url
	 */
	public void getBitmapFromNet(ImageView ivPic, String url) {
		new BitmapTask().execute(ivPic, url);  //启动 AsyncTask
	}
	
	class BitmapTask extends AsyncTask<Object, Void, Bitmap>
	{
		private ImageView ivPic;
		private String url;

		/**
		 * 耗时方法在此形成,子线程
		 */

		@Override
		protected Bitmap doInBackground(Object... params) {
			ivPic = (ImageView) params[0];
			url = (String) params[1];
			ivPic.setTag(url);
			return downloadBitmap(url);
		}
		
		/**
		 * 更新进度，主线程
		 */
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
		
		/**
		 * 耗时方法结束后，执行该方法，主线程
		 */
		@Override
		protected void onPostExecute(Bitmap result) {
			if(result != null)
			{
				String bindUrl = (String) ivPic.getTag();
				if(url.equals(bindUrl))
				{
					//确保图片设置了正确的ImageView
					ivPic.setImageBitmap(result);
					//将图片保存在本地
					mLocalCacheUtils.setBitmapToLocal(url, result);
					// 将图片保存在内存
					mMemoryCacheUtils.setBitmapToMemory(url, result);
				}
				
			}
			
		}
		
	}
	
	/**
	 * 下载图片
	 * @param url
	 * @return
	 */
	private Bitmap downloadBitmap(String url)
	{
		try {
			conn  = (HttpURLConnection) new URL(url).openConnection();
			conn .setConnectTimeout(5000);
			conn .setReadTimeout(5000);
			conn .setRequestMethod("GET");
			conn .connect();
			
			int responseCode = conn .getResponseCode();
			if(responseCode == 200)
			{
				InputStream inputStream = conn .getInputStream();
				//图片压缩
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;//宽高都压缩为原来的1/2
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null, options);
				return bitmap;
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			conn.disconnect();
		}
		return null;
	}
	
}
