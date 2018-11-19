package com.yyz.ard.cactus.adapter;


import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.yyz.ard.cactus.uiaf.FindView;

/**
 * SupperAdapter控件存储类
 * Created by No.9 on 2018/3/8.
 *
 * @author yyz
 */
public class SupperListHolder extends FindView {

    /**
     * SupperAdapter控件存储类
     *
     * @param layout 布局layout
     */
    public SupperListHolder(View layout) {
        super(layout);
    }

    public static SupperListHolder getViewHolder(View convertView, ViewGroup parent, @LayoutRes int layoutRid) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), layoutRid, null);
        }
        return getViewHolder(convertView);
    }

    public static SupperListHolder getViewHolder(View convertView) {
        SupperListHolder viewHolder;
        if (convertView.getTag() == null) {
            viewHolder = new SupperListHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SupperListHolder) convertView.getTag();
        }
        return viewHolder;
    }


}
