package com.tc.beijingwisdom.base;

import java.security.PolicySpi;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tc.beijingwisdom.R;
import com.tc.beijingwisdom.activity.NewsDetailActivity;
import com.tc.beijingwisdom.domain.NewsData.NewsTabData;
import com.tc.beijingwisdom.domain.TabData;
import com.tc.beijingwisdom.domain.TabData.TabNewsData;
import com.tc.beijingwisdom.domain.TabData.TopNewsData;
import com.tc.beijingwisdom.global.GlobalContants;
import com.tc.beijingwisdom.utils.CacheUtils;
import com.tc.beijingwisdom.utils.PrefUtils;
import com.tc.beijingwisdom.view.RefreshListView;
import com.tc.beijingwisdom.view.RefreshListView.OnRefreshListener;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 页签详情页
 * 
 * @author dream
 *
 */
public class TabDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {
	private NewsTabData mTabData;
	private TextView tvText;
	private TabData mTabDetailData;
	@ViewInject(R.id.vp_news)
	private ViewPager mViewPager;
	private BitmapUtils utils;

	private ArrayList<TopNewsData> mTopNewsList;
	private ArrayList<TabNewsData> mNewsList;
	private NewsAdapter mNewsAdapter;

	private String mUrl;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator; // 头条新闻位置指示器
	@ViewInject(R.id.lv_list)
	private RefreshListView lvList;
	private String mMoreUrl;

	private Handler mHandler;

	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		this.mTabData = newsTabData;
		mUrl = GlobalContants.SERVER_URL + mTabData.url;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
		// 加载头布局
		View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
		ViewUtils.inject(this, view);
		ViewUtils.inject(this, headerView);
		// 将头条布局以头布局的形式加给listview
		lvList.addHeaderView(headerView);
		// mViewPager.setOnPageChangeListener(this);
		// 设置下拉刷新监听
		lvList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getDataFromSever();
			}

			@Override
			public void onLoadMore() {
				if (mMoreUrl != null) {
					getMoreDataFromServer();
				} else {
					Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT).show();
					lvList.onRefreshComplete(false); // 收起脚布局
				}
			}
		});
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String ids = PrefUtils.getString(mActivity, "read_ids", "");
				String readid = mNewsList.get(arg2).id;
				if (!ids.contains(readid)) {
					ids = ids + readid + "";
					PrefUtils.setString(mActivity, "read_ids", ids);
				}
				changeReadState(arg1);
				// 跳转到新闻详情页
				Intent intent = new Intent();
				intent.setClass(mActivity, NewsDetailActivity.class);
				intent.putExtra("url", mNewsList.get(arg2).url);
				mActivity.startActivity(intent);
			}
		});
		return view;
	}

	/**
	 * 改变已读的颜色
	 */
	private void changeReadState(View view) {
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setTextColor(Color.GRAY);
	}

	// /**
	// * 加载下一页数据
	// */
	// private void getMoreDataFromSever() {
	// HttpUtils utils = new HttpUtils();
	// utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> responseInfo) {
	// String result = (String) responseInfo.result;
	// Log.d("---------->", "页签详情页返回结果：" + result);
	// parseData(result,true);
	// lvList.onRefreshComplete(true);
	// }
	//
	// @Override
	// public void onFailure(HttpException error, String msg) {
	// Toast.makeText(mActivity, "访问失败", Toast.LENGTH_SHORT).show();
	// error.printStackTrace();
	// lvList.onRefreshComplete(false);
	// }
	// });
	// }
	/**
	 * 加载下一页数据
	 */
	private void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		Log.d("mMoreUrl的地址值：", mMoreUrl);
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;

				parseData(result, true);

				lvList.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				lvList.onRefreshComplete(false);
			}
		});

	}

	@Override
	public void initData() {
		String cache = CacheUtils.getCache(mUrl, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			parseData(cache, false);
		}
		getDataFromSever();
	}

	private void getDataFromSever() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
				Log.d("---------->", "页签详情页返回结果：" + result);
				parseData(result, false);
				lvList.onRefreshComplete(true);
				CacheUtils.setCache(mUrl, result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, "访问失败", Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				lvList.onRefreshComplete(false);
			}
		});
	}

	protected void parseData(String result, boolean isMore) {
		Gson gson = new Gson();
		mTabDetailData = gson.fromJson(result, TabData.class);
		Log.d("tttttttttttttt", "页签详情页：" + mTabDetailData);
		// 处理下一页链接
		String more = mTabDetailData.data.more;
		if (!TextUtils.isEmpty(more)) {
			mMoreUrl = GlobalContants.SERVER_URL + more;
		} else {
			mMoreUrl = null;
		}
		if (true) {
			mTopNewsList = mTabDetailData.data.topnews;
			mNewsList = mTabDetailData.data.news;
			if (mNewsList != null) {
				mViewPager.setAdapter(new TopNewsAdapter());
				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true);
				mIndicator.setOnPageChangeListener(this);
				mIndicator.onPageSelected(0);// 让指示器重新定位到第一个点
				tvTitle.setTag(mTopNewsList.get(0).title);
			}

			// 填充新闻列表数据
			if (mNewsList != null) {
				mNewsAdapter = new NewsAdapter();
				lvList.setAdapter(mNewsAdapter);
			}
			//自动轮播条
			if (mHandler == null) {
				mHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						int currentItem = mViewPager.getCurrentItem();
						if(currentItem < mTopNewsList.size() - 1)
						{
							currentItem++;
						}
						else
						{
							currentItem = 0;
						}
						mViewPager.setCurrentItem(currentItem);
						mHandler.sendEmptyMessageDelayed(0, 3000);
					}
				};
				//延时3秒发消息，形成循环
				mHandler.sendEmptyMessageDelayed(0, 3000);
			}

		} else // 如果是加载下一页，需要将数据追加到原来的数据
		{
			ArrayList<TabNewsData> news = mTabDetailData.data.news;
			mNewsList.addAll(news);
			mNewsAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 头条新闻适配器
	 * 
	 * @author dream
	 *
	 */
	class TopNewsAdapter extends PagerAdapter {
		public TopNewsAdapter() {
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.topnews_item_default);// 设置默认加载的图片
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabDetailData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {

			ImageView image = new ImageView(mActivity);
			image.setImageResource(R.drawable.topnews_item_default);
			// 基于控件大小填充图片
			image.setScaleType(ScaleType.FIT_XY);
			TopNewsData topNewsData = mTopNewsList.get(position);
			utils.display(image, topNewsData.topimage);
			((ViewGroup) container).addView(image);
			image.setOnTouchListener(new TopNewsTouchListener());
			return image;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}
	}
	
	/**
	 * 头条新闻的触摸监听
	 * @author dream
	 *
	 */
	class TopNewsTouchListener implements OnTouchListener
	{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mHandler.removeCallbacksAndMessages(null);
				break;
			case MotionEvent.ACTION_CANCEL:
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			case MotionEvent.ACTION_UP:
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			default:
				break;
			}
			return true;
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		TopNewsData topNewsData = mTopNewsList.get(arg0);
		tvTitle.setText(topNewsData.title);
	}

	// class NewsAdapter extends BaseAdapter {
	//
	// private BitmapUtils utils;
	//
	// public NewsAdapter() {
	// utils = new BitmapUtils(mActivity);
	// utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
	// }
	//
	// @Override
	// public int getCount() {
	// return mNewsList.size();
	// }
	//
	// @Override
	// public TabNewsData getItem(int position) {
	// return mNewsList.get(position);
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ViewHolder holder;
	// if (convertView == null) {
	// convertView = View.inflate(mActivity, R.layout.list_news_item,
	// null);
	// holder = new ViewHolder();
	// holder.ivPic = (ImageView) convertView
	// .findViewById(R.id.iv_pic);
	// holder.tvTitle = (TextView) convertView
	// .findViewById(R.id.tv_title);
	// holder.tvDate = (TextView) convertView
	// .findViewById(R.id.tv_date);
	//
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	//
	// TabNewsData item = getItem(position);
	//
	// holder.tvTitle.setText(item.title);
	// holder.tvDate.setText(item.pubdate);
	//
	// utils.display(holder.ivPic, item.listimage);
	//
	// return convertView;
	// }
	//
	// }
	//
	// static class ViewHolder {
	// public TextView tvTitle;
	// public TextView tvDate;
	// public ImageView ivPic;
	// }
	/**
	 * 新闻列表的适配器
	 * 
	 * @author dream
	 *
	 */
	class NewsAdapter extends BaseAdapter {
		private BitmapUtils utils;

		public NewsAdapter() {
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mNewsList.size();
		}

		@Override
		public TabNewsData getItem(int position) {
			// TODO Auto-generated method stub
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_news_item, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tvData = (TextView) convertView.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			} else {
				// holder = (ViewHolder) convertView.getTag();
				holder = (ViewHolder) convertView.getTag();
			}

			TabNewsData item = getItem(position);
			holder.tvTitle.setText(item.title);
			holder.tvData.setText(item.pubdate);

			utils.display(holder.ivPic, item.listimage);
			String ids = PrefUtils.getString(mActivity, "read_ids", "");
			if (ids.contains(getItem(position).id)) {
				holder.tvTitle.setTextColor(Color.GRAY);
			} else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}
			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvData;
		public ImageView ivPic;
	}
}
