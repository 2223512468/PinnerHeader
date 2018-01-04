/*
 * Created by Ganlin.Wu on 16-9-14 下午2:37
 * Copyright (c) 2016. All rights reserved.
 */

package com.jaja.pinnerheader.adapter;

import android.view.View;
import android.widget.BaseExpandableListAdapter;

/**
 * Created by Ganlin.Wu on 2016/9/14.
 */
public abstract class PinnedHeaderAdapter extends BaseExpandableListAdapter {
    public static final int PINNED_HEADER_GONE = 1 << 5;
    public static final int PINNED_HEADER_VISIBLE = 1 << 6;
    public static final int PINNED_HEADER_PUSH_UP = 1 << 7;

    /**
     * 获取Header的状态
     *
     * @param groupPos
     * @param childPos
     * @return
     */
    public abstract int getHeaderState(int groupPos, int childPos);

    public abstract void configureHeader(View headerView, int groupPos, int childPos);

}
