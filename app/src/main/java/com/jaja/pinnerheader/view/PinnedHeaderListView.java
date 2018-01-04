/*
 * Created by Ganlin.Wu on 16-9-14 下午2:37
 * Copyright (c) 2016. All rights reserved.
 */

package com.jaja.pinnerheader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.jaja.pinnerheader.adapter.PinnedHeaderAdapter;


/**
 * Created by Ganlin.Wu on 2016/9/14.
 */
public class PinnedHeaderListView extends ExpandableListView implements AbsListView.OnScrollListener, ExpandableListView.OnGroupClickListener {

    private View mHeaderView;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;
    private boolean mHeaderViewVisible;
    private PinnedHeaderAdapter mAdapter;

    public PinnedHeaderListView(Context context) {
        super(context);
        registerListener();
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        registerListener();
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        registerListener();
    }

    private void registerListener() {
        setOnScrollListener(this);
        setOnGroupClickListener(this);
    }


    public void setAdapter(PinnedHeaderAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
    }

    public void setHeaderView(View view) {
        mHeaderView = view;
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mHeaderView != null) {
            mHeaderView.setLayoutParams(lp);
            setFadingEdgeLength(0);
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    /**
     * 获取第一个可见的item的位置以及父节点的位置来确定HeaderView的状态以及位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mHeaderView != null) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            configureHeaderView();
        }
    }


    public void configureHeaderView() {
        if (mHeaderView == null || mAdapter == null || mAdapter.getGroupCount() == 0) {
            return;
        }
        final long packedPos = getExpandableListPosition(getFirstVisiblePosition());
        final int groupPos = getPackedPositionGroup(packedPos);
        final int childPos = getPackedPositionChild(packedPos);
        final int state = mAdapter.getHeaderState(groupPos, childPos);
        switch (state) {
            case PinnedHeaderAdapter.PINNED_HEADER_GONE:
                mHeaderViewVisible = false;
                break;
            case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE:
                mAdapter.configureHeader(mHeaderView, groupPos, childPos);
                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }
                mHeaderViewVisible = true;
                break;
            case PinnedHeaderAdapter.PINNED_HEADER_PUSH_UP:
                View firstView = getChildAt(0);
                int bottom = firstView.getBottom();
                int headerHeight = mHeaderView.getHeight();
                int y = 0;
                if (bottom < headerHeight) {
                    y = bottom - headerHeight;
                }
                mAdapter.configureHeader(mHeaderView, groupPos, childPos);
                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, y + mHeaderViewHeight);
                }
                mHeaderViewVisible = true;
                break;
        }
    }

    /**
     * 列表界面更新时调用该方法
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            // 分组栏是直接绘制到界面中，而不是加入到ViewGroup中
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPos, long id) {
        if (isGroupExpanded(groupPos)) {
            parent.collapseGroup(groupPos);
        } else {
            parent.expandGroup(groupPos);
        }
        return true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        configureHeaderView();
    }

    private float mDownX;
    private float mDownY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mHeaderViewVisible) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    if (mDownX <= mHeaderViewWidth && mDownY <= mHeaderViewHeight) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    float x = ev.getX();
                    float y = ev.getY();
                    float offsetX = Math.abs(x - mDownX);
                    float offsetY = Math.abs(y - mDownY);
                    // 如果 HeaderView 是可见的 , 而且点击在 HeaderView 内 , 那么触发 onHeaderViewClick
                    if (x <= mHeaderViewWidth && y <= mHeaderViewHeight && offsetX <= mHeaderViewWidth && offsetY <= mHeaderViewHeight) {
                        if (mHeaderView != null) {
                            onHeaderViewClick();
                        }
                    }

                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void onHeaderViewClick() {
        long packedPosition = getExpandableListPosition(getFirstVisiblePosition());
        int groupPosition = getPackedPositionGroup(packedPosition);
        collapseGroup(groupPosition);
        setSelectedGroup(groupPosition);
    }
}
