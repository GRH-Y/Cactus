package com.yyz.ard.cactus.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyz.ard.cactus.adapter.joggle.IAdapterItemClickListener;
import com.yyz.ard.cactus.adapter.joggle.IAdapterItemLongClickListener;
import com.yyz.ard.cactus.adapter.joggle.ILoadMoreListener;
import com.yyz.ard.cactus.adapter.joggle.ILoadMoreView;
import com.yyz.ard.cactus.adapter.joggle.LoadMoreStatus;
import com.yyz.ard.cactus.uiaf.ViewAssignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * RecyclerView 的通用adapter
 * Created by dell on 3/21/2018.
 *
 * @author yyz
 */
public abstract class SupperRecyclerAdapter extends RecyclerView.Adapter<SupperRecyclerHolder> {

    private List<Object> mAdapterData = new ArrayList<>();

    private Map<String, ItemCell> mItemCellMap = new HashMap<>();

    /**
     * 底部view（提示加载更多）
     */
    private ILoadMoreView mLoadMoreView = null;
    private LoadMoreAttribute mLoadMoreAttribute = null;
    private ILoadMoreListener mLoadMoreListener = null;

    private IAdapterItemClickListener mClickListener;
    private IAdapterItemLongClickListener mLongClickListener;

    private int FOOT_TYPE = 0x123;

    private RecyclerView mRecyclerView = null;
    private LayoutInflater mLayoutInflater = null;

    private boolean mIsOpenAutoFindView = true;

    private boolean isScroll = false;


    public boolean isOpenAutoFindView() {
        return mIsOpenAutoFindView;
    }

    public void setOpenAutoFindView(boolean isOpenAutoFindView) {
        this.mIsOpenAutoFindView = isOpenAutoFindView;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void changeStatus(LoadMoreStatus status) {
        if (mLoadMoreAttribute != null) {
            mLoadMoreAttribute.changeStatus(status);
        }
    }

    public void setOnItemClickListener(IAdapterItemClickListener listener) {
        this.mClickListener = listener;
    }

    public void setOnItemLongClickListener(IAdapterItemLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }

    //---------------------- Listener ---------------------------

    public void setAdapterData(List<Object> data) {
        mAdapterData.clear();
        mAdapterData.addAll(data);
        notifyDataSetChanged();
    }

    public void setAdapterData(Collection<Object> data) {
        mAdapterData.clear();
        mAdapterData.addAll(data);
        notifyDataSetChanged();
    }

    public void setAdapterData(Object data) {
        if (data instanceof Collection) {
            setAdapterData((Collection) data);
        } else if (data instanceof List) {
            setAdapterData((List) data);
        }
    }

    public List<Object> getAdapterData() {
        return mAdapterData;
    }

    public void addAllData(List<Object> data) {
        if (data != null && !data.isEmpty()) {
            this.mAdapterData.addAll(data);
            notifyItemRangeInserted(getItemCount() - data.size(), data.size());
            compatibilityDataSizeChanged(data.size());
        }
    }

    public void addAllData(Object[] data) {
        if (data != null && data.length > 0) {
            for (Object tmp : data) {
                this.mAdapterData.add(tmp);
            }
            notifyItemRangeInserted(getItemCount() - data.length, data.length);
            compatibilityDataSizeChanged(data.length);
//            notifyDataSetChanged();
        }
    }

    public void addAllData(Collection<Object> data) {
        if (data != null && !data.isEmpty()) {
            this.mAdapterData.addAll(data);
            notifyItemRangeInserted(getItemCount() - data.size(), data.size());
            compatibilityDataSizeChanged(data.size());
        }
    }

    public void addAllData(Object data) {
        if (data instanceof List) {
            addAllData((List<Object>) data);
        } else if (data instanceof Object[]) {
            addAllData((Object[]) data);
        } else if (data instanceof Collection) {
            addAllData((Collection<Object>) data);
        }
    }

    public void addAllData(int position, List<Object> data) {
        if (data != null && !data.isEmpty()) {
            this.mAdapterData.addAll(position, data);
            notifyItemRangeInserted(position, data.size());
            compatibilityDataSizeChanged(data.size());
        }
    }

    public void addItem(Object obj) {
        addItem(getActualItemCount(), obj);
    }

    public void addItem(int position, Object obj) {
        mAdapterData.add(position, obj);
        position = mLoadMoreView == null ? position : position + 1;
        notifyItemInserted(position);
        compatibilityDataSizeChanged(1);
    }

    public void removeItem(Object obj) {
        if (!mAdapterData.isEmpty()) {
            int position = mAdapterData.indexOf(obj);
            if (position == -1) {
                return;
            }
            removePosition(position);
        }
    }

    public void removePosition(int position) {
        if (!mAdapterData.isEmpty()) {
            mAdapterData.remove(position);
            int internalPosition = mLoadMoreView == null ? position : position + 1;
            notifyItemRemoved(internalPosition);
            compatibilityDataSizeChanged(0);
            notifyItemRangeChanged(internalPosition, getActualItemCount() - internalPosition);
        }
    }

    public void clear() {
        if (mAdapterData.size() > 0) {
            mAdapterData.clear();
            notifyDataSetChanged();
        }
    }

    private void compatibilityDataSizeChanged(int size) {
        int dataSize = getActualItemCount();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    public Object getBottomData() {
        if (mAdapterData.isEmpty()) {
            return null;
        }
        return mAdapterData.get(getActualItemCount() - 1);
    }

    public Object getTopData() {
        if (mAdapterData.isEmpty()) {
            return null;
        }
        return mAdapterData.get(0);
    }

    //---------------------- add set remove clear ---------------------------

    public void setLoadMoreView(ILoadMoreListener listener, ILoadMoreView loadMoreView) {
        setLoadMoreView(loadMoreView);
        setLoadMoreListener(listener);
    }

    public void setLoadMoreListener(ILoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }

    public void setLoadMoreView(ILoadMoreView loadMoreView) {
        this.mLoadMoreView = loadMoreView;
    }

    //-------------------- setLoadCompleted showLoadMoreDataTitle showNoMoreDataTitle --------------


    /**
     * 定义当前view 类型（实现多种布局）
     *
     * @param position
     * @return
     */
    protected abstract ItemCell onCreateItemCell(int position);


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mLayoutInflater = LayoutInflater.from(recyclerView.getContext());
        this.mRecyclerView = recyclerView;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //用户正在用手指滚动
                    isScroll = true;
                }
            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mRecyclerView = null;
        mItemCellMap.clear();
    }

    @Override
    public SupperRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        SupperRecyclerHolder holder;
        if (viewType == FOOT_TYPE && mLoadMoreView != null) {
            FootItemCell itemCell = new FootItemCell(mLoadMoreView);
            View layout = mLayoutInflater.inflate(itemCell.getViewLayoutId(), parent, false);
            holder = new SupperRecyclerHolder(layout);
            if (mLoadMoreAttribute == null) {
                mLoadMoreAttribute = new LoadMoreAttribute(mLoadMoreView, holder.getFindView());
                mLoadMoreAttribute.setLoadMoreViewClickListener(mLoadMoreListener);
            }
            holder.setItemCell(itemCell);
        } else {
            ItemCell itemCell = mItemCellMap.get(String.valueOf(viewType));
            View layout = mLayoutInflater.inflate(itemCell.getViewLayoutId(), parent, false);
            holder = new SupperRecyclerHolder(layout);
            holder.setItemCell(itemCell);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(SupperRecyclerHolder holder, final int position) {

        ItemCell itemCell = holder.getItemCell();
        if (itemCell instanceof FootItemCell) {
            if (isScroll) {
                mLoadMoreAttribute.changeStatus(LoadMoreStatus.STATUS_LOADING);
            }
            return;
        }

        final Object data = mAdapterData.get(position);
        holder.itemView.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, position, data);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mLongClickListener != null) {
                return mLongClickListener.onItemLongClick(v, position, data);
            }
            return false;
        });

        if (mIsOpenAutoFindView) {
            ViewAssignment.setViewData(holder.getFindView().getActivityOrView(), data);
        }

        itemCell.onBindView(position, data, holder.getFindView());

    }


    @Override
    public int getItemCount() {
        return mLoadMoreView == null ? mAdapterData.size() : mAdapterData.size() + 1;
    }

    /**
     * 实际的数据数量（不包含头脚view）
     *
     * @return
     */
    public int getActualItemCount() {
        return mAdapterData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getActualItemCount() && mLoadMoreView != null) {
            return FOOT_TYPE;
        }
        ItemCell itemCell = onCreateItemCell(position);
        int hashcode = itemCell.hashCode();
        mItemCellMap.put(String.valueOf(hashcode), itemCell);
        return hashcode;
    }

}
