package com.zhaobo.progressviews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhaobo.progressviews.R;

import java.text.SimpleDateFormat;

/**
 * Created by zhaobo on 17/2/23.
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private static final int DOWN_PULL_REFRESH = 0;
    private static final int RELEASE_REFRESH = 1;
    private static final int REFRESHING = 2;

    private View footerView, headerView;

    private int footerViewHeight, headerViewHeight;

    private ProgressBar mProgressBar;
    private TextView tvState, tvLastUpdateTime;

    private int firstVisibleItemPosition;
    private boolean isScrollToBottom;
    private boolean isLoadingMore;

    private OnRefreshListener mOnRefershListener;
    int tapSlop;

    private int downY;
    private int currentState = DOWN_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
        Log.i("543","footerViewHeight:"+footerViewHeight+" headerViewHeight:"+headerViewHeight);

        tapSlop = ViewConfiguration.get(getContext()).getScaledDoubleTapSlop();
        setOnScrollListener(this);
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView() {

        footerView = View.inflate(getContext(), R.layout.footer_view, null);
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        this.addFooterView(footerView);
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.head_view, null);

        mProgressBar = (ProgressBar) headerView
                .findViewById(R.id.pb_listview_header);
        tvState = (TextView) headerView
                .findViewById(R.id.tv_listview_header_state);
        tvLastUpdateTime = (TextView) headerView
                .findViewById(R.id.tv_listview_header_last_update_time);

        // 设置最后刷新时间
        tvLastUpdateTime.setText("最后刷新时间: " + getLastUpdateTime());

        headerView.measure(0, 0); // 系统会帮我们测量出headerView的高度
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        this.addHeaderView(headerView); // 向ListView的顶部添加一个view对象
        //initAnimation();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY() - downY;

                if (moveY > tapSlop) {
                    // 移动中的y - 按下的y = 间距.
                    int diff = (moveY - downY) / 2;
                    // -头布局的高度 + 间距 = paddingTop
                    int paddingTop = -headerViewHeight + diff;
                    // 如果: -头布局的高度 > paddingTop的值 执行super.onTouchEvent(ev);
                    if (firstVisibleItemPosition == 0
                            && -headerViewHeight < paddingTop&&currentState!=REFRESHING) {
                        if (paddingTop > 0 && currentState == DOWN_PULL_REFRESH) { // 完全显示了.
                            currentState = RELEASE_REFRESH;
                            refreshHeaderView();
                        } else if (paddingTop < 0
                                && currentState == RELEASE_REFRESH) { // 没有显示完全
                            currentState = DOWN_PULL_REFRESH;
                            refreshHeaderView();
                        }
                        // 下拉头布局
                        headerView.setPadding(0, paddingTop, 0, 0);
                        return true;
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                // 判断当前的状态是松开刷新还是下拉刷新
                if (currentState == RELEASE_REFRESH) {
                    // 把头布局设置为完全显示状态
                    headerView.setPadding(0, 0, 0, 0);
                    // 进入到正在刷新中状态
                    currentState = REFRESHING;
                    refreshHeaderView();

                    if (mOnRefershListener != null) {
                        mOnRefershListener.onDownPullRefresh(); // 调用使用者的监听方法
                    }
                } else if (currentState == DOWN_PULL_REFRESH) {
                    // 隐藏头布局
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据currentState刷新头布局的状态
     */
    private void refreshHeaderView() {
        switch (currentState) {
            case DOWN_PULL_REFRESH: // 下拉刷新状态
                tvState.setText("下拉刷新");
                break;
            case RELEASE_REFRESH: // 松开刷新状态
                tvState.setText("松开刷新");
                break;
            case REFRESHING: // 正在刷新中状态
                mProgressBar.setVisibility(View.VISIBLE);
                tvState.setText("正在刷新中...");
                break;
            default:
                break;
        }
    }

    /**
     * 获得系统的最新时间
     *
     * @return
     */
    private String getLastUpdateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE
                || scrollState == SCROLL_STATE_FLING) {
            // 判断当前是否已经到了底部
            if (isScrollToBottom && !isLoadingMore) {
                isLoadingMore = true;
                // 当前到底部
                footerView.setPadding(0, 0, 0, 0);
                this.setSelection(this.getCount());

                if (mOnRefershListener != null) {
                    mOnRefershListener.onLoadingMore();
                }
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;

        if (getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }

    }

    /**
     * 隐藏头布局
     */
    public void hideHeaderView() {
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        mProgressBar.setVisibility(View.GONE);
        tvState.setText("下拉刷新");
        tvLastUpdateTime.setText("最后刷新时间: " + getLastUpdateTime());
        currentState = DOWN_PULL_REFRESH;
    }

    /**
     * 隐藏脚布局
     */
    public void hideFooterView() {
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        isLoadingMore = false;
    }

    /**
     * 设置刷新监听事件
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefershListener = listener;
    }

    public interface OnRefreshListener {

        /**
         * 下拉刷新
         */
        void onDownPullRefresh();

        /**
         * 上拉加载更多
         */
        void onLoadingMore();
    }
}
