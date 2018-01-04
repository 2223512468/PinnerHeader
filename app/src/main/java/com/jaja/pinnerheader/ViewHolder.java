/*
 * Created by Ganlin.Wu on 16-9-14 上午10:49
 * Copyright (c) 2016. All rights reserved.
 */

package com.jaja.pinnerheader;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Ganlin.Wu on 2016/9/14.
 */
public class ViewHolder {
    public static <T extends View> T get(View convertView, int resId) {
        SparseArray<View> views = (SparseArray<View>) convertView.getTag();
        if (views == null) {
            views = new SparseArray<>();
            convertView.setTag(views);
        }
        View view = views.get(resId);
        if (view == null) {
            view = convertView.findViewById(resId);
            views.put(resId, view);
        }
        return (T) view;
    }
}
