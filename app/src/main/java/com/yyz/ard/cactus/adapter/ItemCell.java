package com.yyz.ard.cactus.adapter;


import com.yyz.ard.cactus.uiaf.FindView;

import java.util.List;

/**
 * @className: ItemCell
 * @classDescription:
 * @author: yyz
 * @createTime: 7/10/2018
 */
public abstract class ItemCell<T> {

    /**
     * 要创建界面的layout资源id
     *
     * @return layout rid
     */
    protected abstract int getViewLayoutId();

    /**
     * @param position
     * @param data
     * @param findView
     */
    protected void onBindView(int position, T data, FindView findView) {
    }


}
