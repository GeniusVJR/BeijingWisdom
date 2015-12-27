package com.tc.beijingwisdom.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tc.beijingwisdom.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下拉刷新
 * 
 * @author dream
 *
 */
public class RefreshListView extends ListView implements OnScrollListener, android.widget.AdapterView.OnItemClickListener{

	private static final int STATE_PULL_REFRESH = 0; // 下拉刷新
	private static final int STATE_RELEASE_REFRESH = 1; // 松开刷新
	private static final int STATE_REFRESHING = 2; // 正在刷新
	private View mHeaderView;
	private int startY = -1; // 滑动起点
	private int endY;
	private int mHeaderViewHeight;
	private int mCurrrentState = STATE_PULL_REFRESH;
	private TextView tvTitle;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	private RotateAnimation animUp;
	private RotateAnimation animDown;

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
	}

	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
		this.addHeaderView(mHeaderView);
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);
		
		mHeaderView.measure(0, 0);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
		initArrowAnim();
		tvTime.setText("最后刷新时间：" + getCurrentTime());
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// 确保startY有效
				startY = (int) ev.getRawY();
			}

			if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
				break;
			}

			int endY = (int) ev.getRawY();
			int dy = endY - startY;// 移动便宜量

			if (dy > 0 && getFirstVisiblePosition() == 0) {// 只有下拉并且当前是第一个item,才允许下拉
				int padding = dy - mHeaderViewHeight;// 计算padding
				mHeaderView.setPadding(0, padding, 0, 0);// 设置当前padding

				if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {// 状态改为松开刷新
					mCurrrentState = STATE_RELEASE_REFRESH;
					refreshState();
				} else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
					mCurrrentState = STATE_PULL_REFRESH;
					refreshState();
				}

				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// 重置

			if (mCurrrentState == STATE_RELEASE_REFRESH) {
				mCurrrentState = STATE_REFRESHING;// 正在刷新
				mHeaderView.setPadding(0, 0, 0, 0);// 显示
				refreshState();
			} else if (mCurrrentState == STATE_PULL_REFRESH) {
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏
			}

			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 刷新下拉控件的布局
	 */
	private void refreshState() {
		switch (mCurrrentState) {
		case STATE_PULL_REFRESH:
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(animDown);
			break;
		case STATE_RELEASE_REFRESH:
			tvTitle.setText("松开刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(animUp);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新...");
			ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
			ivArrow.setVisibility(View.INVISIBLE);
			pbProgress.setVisibility(View.VISIBLE);
//			mListener.onRefresh();
			if (mListener != null) {
				mListener.onRefresh();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 初始化箭头动画
	 */
	private void initArrowAnim() {
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);

		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);

	}
	
	
	OnRefreshListener mListener;
	private View mFooterView;
	private int mFooterViewHeight;;
	public void setOnRefreshListener(OnRefreshListener listener)
	{
		mListener = listener;
	}
	public interface OnRefreshListener
	{
		public void onRefresh();
		public void onLoadMore();
	}
	/**
	 * 收起下拉刷新的控件
	 */
	public void onRefreshComplete(boolean success)
	{
		
		if(isLoadingMore)  //正在加载更多
		{
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);//隐藏脚布局
			isLoadingMore = false;
		}
		else
		{
			mCurrrentState = STATE_PULL_REFRESH;
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);   //隐藏头布局
			if(success)
			{
				tvTime.setText("最后刷新时间：" + getCurrentTime());
			}
		}
		
		
	}
	/***
	 * 初始化脚布局
	 */
	private void initFooterView()
	{
		mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
		this.addFooterView(mFooterView);
		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, -mHeaderViewHeight, 0, 0);   //隐藏脚布局
		this.setOnScrollListener(this);
	}
	//获取当前时间
	public String getCurrentTime()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	private boolean isLoadingMore;
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING)
		{
			if(getLastVisiblePosition() == getCount() - 1 && !isLoadingMore)
			{
				mFooterView.setPadding(0, 0, 0, 0);
				setSelection(getCount() - 1);//改变listview的显示位置
				isLoadingMore = true;
				if(mListener != null)
				{
					mListener.onLoadMore();
				}
			}
		}
	}
	
	OnItemClickListener mItemClickListener;
	@Override
	public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
		// TODO Auto-generated method stub
		super.setOnItemClickListener(this);
		this.mItemClickListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mItemClickListener != null) {
			mItemClickListener.onItemClick(arg0, arg1, arg2 - getHeaderViewsCount(), arg3);
		}
	}
	

}
