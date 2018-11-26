package com.yyz.ard.cactus.adapter.joggle;


import com.yyz.ard.cactus.uiaf.FindView;

/**
 * ListView适配器监听器
 * Created by dell on 12/8/2017.
 *
 * @author yyz
 * @date 12/8/2017.
 */

public interface IListViewBindViewListener<T> {
    /**
     * 该方法调用于界面需要加载时
     *
     * @param position
     * @param data
     * @param viewHolder
     * @return
     */
    void onBindViewHolder(int position, T data, FindView viewHolder);
}
