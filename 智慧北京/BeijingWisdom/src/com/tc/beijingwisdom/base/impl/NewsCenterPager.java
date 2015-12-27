package com.tc.beijingwisdom.base.impl;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tc.beijingwisdom.activity.MainActivity;
import com.tc.beijingwisdom.base.BaseMenuDetailPager;
import com.tc.beijingwisdom.base.BasePager;
import com.tc.beijingwisdom.domain.NewsData;
import com.tc.beijingwisdom.domain.NewsData.NewsMenuData;
import com.tc.beijingwisdom.fragment.LeftMenuFragment;
import com.tc.beijingwisdom.global.GlobalContants;
import com.tc.beijingwisdom.menudetail.InteractMenuDetailPager;
import com.tc.beijingwisdom.menudetail.NewsMenuDetailPager;
import com.tc.beijingwisdom.menudetail.PhotoMenuDetailPager;
import com.tc.beijingwisdom.menudetail.TopicMenuDetailPager;
import com.tc.beijingwisdom.utils.CacheUtils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * 新闻中心
 * 
 * @author dream
 *
 */
public class NewsCenterPager extends BasePager {

	private NewsData mNewsData;
	private ArrayList<BaseMenuDetailPager> mPagers;

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		tvTitle.setText("新闻");
		setSlidingMenuEnable(true);
		// TextView text = new TextView(mActivity);
		// text.setText("新闻中心");
		// text.setTextColor(Color.RED);
		// text.setTextSize(25);
		// text.setGravity(Gravity.CENTER);
		// //向Framlayout中动态添加布局
		// flContent.addView(text);
		String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			parseData(cache);
		}
		getDataFromServer();//不敢有没有缓存都获取最新数据，保证数据最新

	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		// 使用xutils发送请求
		utils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo responseInfo) {
				String result = (String) responseInfo.result;
				Log.d("---------->", "返回结果：" + result);
				parseData(result);
				// 设置缓存
				CacheUtils.setCache(GlobalContants.CATEGORIES_URL, result, mActivity);
			}

			// 失败
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, "访问失败", Toast.LENGTH_SHORT).show();
				error.printStackTrace();
			}

		});
	}

	// 解析网络数据
	private void parseData(String result) {
		Gson gson = new Gson();
		mNewsData = gson.fromJson(result, NewsData.class);
		Log.d("---------->", "解析结果：" + mNewsData);
		MainActivity mainUi = (MainActivity) mActivity;
		LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();
		leftMenuFragment.setMenuData(mNewsData);

		// 准备四个菜单详情页
		mPagers = new ArrayList<BaseMenuDetailPager>();
		mPagers.add(new NewsMenuDetailPager(mActivity, mNewsData.data.get(0).children));
		mPagers.add(new TopicMenuDetailPager(mActivity));
		mPagers.add(new PhotoMenuDetailPager(mActivity,btnPhoto));
		mPagers.add(new InteractMenuDetailPager(mActivity));
		setCurrentMenuDetailPager(0);
	}

	/**
	 * 设置当前菜单详情页
	 */
	public void setCurrentMenuDetailPager(int position) {
		BaseMenuDetailPager pager = mPagers.get(position);
		flContent.removeAllViews();
		flContent.addView(pager.mRootView);
		NewsMenuData menuData = mNewsData.data.get(position);
		tvTitle.setText(menuData.title);

		pager.initData();
		if(pager instanceof PhotoMenuDetailPager)
		{
			btnPhoto.setVisibility(View.VISIBLE);
		}
		else
		{
			btnPhoto.setVisibility(View.GONE);
		}
	}

}
