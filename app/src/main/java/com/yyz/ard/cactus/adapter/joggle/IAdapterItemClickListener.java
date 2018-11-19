package com.yyz.ard.cactus.adapter.joggle;

import android.view.View;

/**
 * Created by dell on 3/22/2018.
 *
 * @author yyz
 */
public interface IAdapterItemClickListener<T> {
    /**
     * adapter item 点击事件回调方法
     *
     * @param view     当前点击的item view
     * @param position 当前的索引
     * @param data     当前索引对应的数据
     */
    void onItemClick(View view, int position, T data);
}
