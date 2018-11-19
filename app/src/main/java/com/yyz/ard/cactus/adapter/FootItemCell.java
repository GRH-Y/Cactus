package com.yyz.ard.cactus.adapter;


import com.yyz.ard.cactus.adapter.joggle.ILoadMoreView;

/**
 * @className: FootItemCell
 * @classDescription:
 * @author: yyz
 * @createTime: 7/10/2018
 */
public class FootItemCell extends ItemCell {

    private ILoadMoreView mLoadMoreView;

    public FootItemCell(ILoadMoreView loadMoreView) {
        this.mLoadMoreView = loadMoreView;
    }

    @Override
    protected int getViewLayoutId() {
        return mLoadMoreView.getLayoutId();
    }
}
