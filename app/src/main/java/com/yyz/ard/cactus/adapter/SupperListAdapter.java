package com.yyz.ard.cactus.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yyz.ard.cactus.adapter.joggle.IListViewBindViewListener;
import com.yyz.ard.cactus.uiaf.ViewAssignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 通用的适配器
 *
 * @param <T>
 * @author yyz
 * @date 4/6/16.
 */
public abstract class SupperListAdapter<T> extends BaseAdapter {
    private List<T> adapterData = new ArrayList<>();
    private IListViewBindViewListener<T> listener = null;
    private boolean isOpenAutoFindView = true;


    public boolean isOpenAutoFindView() {
        return isOpenAutoFindView;
    }

    public void setOpenAutoFindView(boolean openAutoFindView) {
        isOpenAutoFindView = openAutoFindView;
    }

    @Override
    public int getCount() {
        return adapterData == null ? 0 : adapterData.size();
    }

    @Override
    public T getItem(int position) {
        return adapterData == null ? null : adapterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setAdapterData(List<T> data) {
        adapterData.clear();
        adapterData.addAll(data);
        notifyDataSetChanged();
    }

    public void setAdapterData(Collection<T> data) {
        adapterData.clear();
        adapterData.addAll(data);
        notifyDataSetChanged();
    }

    public void setAdapterData(T data) {
        adapterData.clear();
        this.addItem(data);
    }

    public List<T> getAdapterData() {
        return adapterData;
    }

    public void addAllData(List<T> data) {
        if (data != null && !data.isEmpty()) {
            this.adapterData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addAllData(T[] data) {
        if (data != null && data.length > 0) {
            for (T tmp : data) {
                this.adapterData.add(tmp);
            }
            notifyDataSetChanged();
        }
    }

    public void addAllData(Collection<T> data) {
        if (data != null && !data.isEmpty()) {
            this.adapterData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addItem(T obj) {
        adapterData.add(obj);
        notifyDataSetChanged();
    }

    public void addItem(int pos, T obj) {
        adapterData.add(pos, obj);
        notifyDataSetChanged();
    }

    public void removeItem(Object obj) {
        if (adapterData.size() > 0) {
            adapterData.remove(obj);
            notifyDataSetChanged();
        }
    }

    public void removeIndex(int index) {
        if (adapterData.size() > 0) {
            adapterData.remove(adapterData.get(index));
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (adapterData.size() > 0) {
            adapterData.clear();
            notifyDataSetChanged();
        }
    }

    public void setBindViewListener(IListViewBindViewListener<T> listener) {
        this.listener = listener;
    }

    /**
     * 要创建界面的layout资源id
     *
     * @return layout rid
     */
    protected abstract int getContentView();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SupperListHolder holder;
        T data = adapterData.get(position);
        if (convertView == null) {
            int rid = getContentView();
            if (rid == 0) {
                throw new RuntimeException("The dialog_setting layout ID is illegal !");
            }
            convertView = View.inflate(parent.getContext(), rid, null);
            holder = new SupperListHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SupperListHolder) convertView.getTag();
        }
        if (isOpenAutoFindView) {
            ViewAssignment.setViewData(holder.getActivityOrView(), data);
        }
        if (listener != null) {
            listener.onBindViewHolder(position, data, holder);
        }
        return convertView;
    }

}
