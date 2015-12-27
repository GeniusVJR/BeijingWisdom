package com.tc.beijingwisdom.fragment;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tc.beijingwisdom.R;
import com.tc.beijingwisdom.activity.MainActivity;
import com.tc.beijingwisdom.base.BaseFragment;
import com.tc.beijingwisdom.base.impl.NewsCenterPager;
import com.tc.beijingwisdom.domain.NewsData;
import com.tc.beijingwisdom.domain.NewsData.NewsMenuData;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 侧边栏
 * @author dream
 *
 */
public class LeftMenuFragment extends BaseFragment {

	
	ArrayList<NewsMenuData> mMenuList;
	@ViewInject(R.id.lv_list)
	private ListView lvList;
	private MenuAdapter mAdapter;
	
	private int mCurrentPos;  //当前被点击的菜单项
	@Override
	public View initViews() {
		
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void initData() {
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				mCurrentPos = position;
				mAdapter.notifyDataSetChanged();
				setCurrentMenuDetailPager(position);
				toggleSlidingMenu();
			}

			

			
		});
	}
	
	//切换SlidingMenu的状态
	private void toggleSlidingMenu() {
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();//切换状态，显示时隐藏，隐藏时显示
	}
	//设置当前菜单详情页
	private void setCurrentMenuDetailPager(int position) {
	MainActivity mainUi = (MainActivity) mActivity;
	ContentFragment fragment = mainUi.getContentFragment();
	NewsCenterPager page = fragment.getNewsCenterPager();
	page.setCurrentMenuDetailPager(position);
	}
	//设置网络数据
	public void setMenuData()
	{
		
	}
	/**
	 * 侧边栏数据适配器
	 * @author dream
	 *
	 */
	class MenuAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mMenuList.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			// TODO Auto-generated method stub
			return mMenuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.list_menu_item, null);
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
			
			NewsMenuData newsMenuData = getItem(position);
			tvTitle.setText(newsMenuData.title);
			if(mCurrentPos == position)
			{
				tvTitle.setEnabled(true);
			}
			else
			{
				tvTitle.setEnabled(false);
			}
			return view;
		}
		
	}

	public void setMenuData(NewsData data) {
		Log.d("-------->", "侧边栏拿到数据啦" + data);
		mMenuList = data.data;
		mAdapter = new MenuAdapter();
		lvList.setAdapter(mAdapter);
	}

}
