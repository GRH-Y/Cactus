package com.yyz.ard.cactus.adapter.joggle;

/**
 * adapter 下拉加载更多监听器
 * Created by dell on 3/23/2018.
 *
 * @author yyz
 */
public interface ILoadMoreListener {
    /**
     * 加载更多回调
     *
     * @param attribute 加载更多的属性
     */
    void onLoadMore(ILoadMoreAttribute attribute);
}
