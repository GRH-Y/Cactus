package com.yyz.ard.cactus.adapter.joggle;

/**
 * 加载更多扩展属性接口
 * Created by dell on 3/23/2018.
 *
 * @author yyz
 */
public interface ILoadMoreAttribute {

    /**
     * 切换状态
     *
     * @param status
     */
    void changeStatus(LoadMoreStatus status);
}
