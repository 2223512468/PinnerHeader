/*
 * Created by Ganlin.Wu on 16-9-14 上午10:43
 * Copyright (c) 2016. All rights reserved.
 */

package com.jaja.pinnerheader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jaja.pinnerheader.adapter.PinnedHeaderAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ganlin.Wu on 2016/9/14.
 */
public class MyPinnedHeaderAdapter extends PinnedHeaderAdapter {

    private Context mContext;
    private ArrayList<String> parents;
    private Map<String, ArrayList<String>> datas = new HashMap<>();
    private ExpandableListView mListView;

    public MyPinnedHeaderAdapter(ExpandableListView listView, ArrayList<String> parent, Map<String, ArrayList<String>> datas) {
        mContext = listView.getContext();
        mListView = listView;
        this.parents = parent;
        this.datas = datas;
    }

    @Override
    public int getGroupCount() {
        return parents == null ? 0 : parents.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String key = parents.get(groupPosition);
        return datas.get(key).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parents.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String key = parents.get(groupPosition);
        return datas.get(key).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.parent_layout, null);
        }

        TextView tv = ViewHolder.get(convertView, R.id.tv_parent);
        tv.setText(parents.get(groupPosition));
        int resId;
        if (isExpanded) {
            resId = R.drawable.qb_down;
        } else {
            resId = R.drawable.qb_right;
        }
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable, null, null, null);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layout, null);
        }
        TextView tv = ViewHolder.get(convertView, R.id.tv_item);
        String key = parents.get(groupPosition);
        String s = datas.get(key).get(childPosition);
        tv.setText(s);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getHeaderState(int groupPos, int childPos) {
        final int childCount = getChildrenCount(groupPos);
        if (childPos == childCount - 1) {
            return PINNED_HEADER_PUSH_UP;
        } else if (childPos < 0 && !mListView.isGroupExpanded(groupPos)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    @Override
    public void configureHeader(View headerView, int groupPos, int childPos) {
        if (groupPos > -1) {
            TextView tv = ViewHolder.get(headerView, R.id.tv_parent);
            tv.setText(parents.get(groupPos));
            boolean isExpanded = mListView.isGroupExpanded(groupPos);
            int resId;
            if (isExpanded) {
                resId = R.drawable.qb_down;
            } else {
                resId = R.drawable.qb_right;
            }
            Drawable drawable = ContextCompat.getDrawable(mContext, resId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv.setCompoundDrawables(drawable, null, null, null);
        }
    }
}
