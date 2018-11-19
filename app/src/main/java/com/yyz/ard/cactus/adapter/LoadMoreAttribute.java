package com.yyz.ard.cactus.adapter;


import android.view.View;

import com.yyz.ard.cactus.adapter.joggle.ILoadMoreAttribute;
import com.yyz.ard.cactus.adapter.joggle.ILoadMoreListener;
import com.yyz.ard.cactus.adapter.joggle.ILoadMoreView;
import com.yyz.ard.cactus.adapter.joggle.LoadMoreStatus;
import com.yyz.ard.cactus.uiaf.FindView;


/**
 * 加载更多
 * Created by dell on 3/22/2018.
 *
 * @author yyz
 */
public class LoadMoreAttribute implements ILoadMoreAttribute {

    private boolean isLoading = false;
    private FindView holder;
    private ILoadMoreView loadMoreView;
    private ILoadMoreListener loadMoreListener = null;
    private LoadMoreStatus mLoadMoreStatus = LoadMoreStatus.STATUS_DEFAULT;

    public LoadMoreAttribute(ILoadMoreView loadMoreView, FindView findView) {
        this.loadMoreView = loadMoreView;
        this.holder = findView;
    }

    public void setLoadMoreViewClickListener(ILoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public LoadMoreStatus getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    @Override
    public void changeStatus(LoadMoreStatus status) {
        switch (status) {
            case STATUS_LOADING:
                if (!isLoading) {
                    visibleLoading(holder, true);
                    visibleLoadFail(holder, false);
                    visibleLoadEnd(holder, false);
                    if (loadMoreListener != null) {
                        loadMoreListener.onLoadMore(this);
                    }
                    mLoadMoreStatus = LoadMoreStatus.STATUS_LOADING;
                    isLoading = true;
                }
                break;
            case STATUS_FAIL:
                isLoading = false;
                visibleLoading(holder, false);
                visibleLoadFail(holder, true);
                visibleLoadEnd(holder, false);
                mLoadMoreStatus = LoadMoreStatus.STATUS_FAIL;
                break;
            case STATUS_END:
                isLoading = false;
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, true);
                mLoadMoreStatus = LoadMoreStatus.STATUS_END;
                break;
            default:
            case STATUS_DEFAULT:
                isLoading = false;
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                mLoadMoreStatus = LoadMoreStatus.STATUS_DEFAULT;
                break;
        }
    }

    private void visibleLoading(FindView holder, boolean visible) {
        holder.setVisibility(loadMoreView.getLoadingViewId(), visible);
    }

    private void visibleLoadFail(FindView holder, boolean visible) {
        holder.setVisibility(loadMoreView.getLoadFailViewId(), visible);
        holder.setViewListenerById(loadMoreView.getLoadFailViewId(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadMoreListener != null) {
                    changeStatus(LoadMoreStatus.STATUS_LOADING);
                    loadMoreListener.onLoadMore(LoadMoreAttribute.this);
                }
            }
        });
    }

    private void visibleLoadEnd(FindView holder, boolean visible) {
        holder.setVisibility(loadMoreView.getLoadEndViewId(), visible);
    }


}
