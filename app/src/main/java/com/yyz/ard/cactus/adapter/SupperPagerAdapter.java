package com.yyz.ard.cactus.adapter;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yyz.ard.cactus.adapter.joggle.IPagerBindViewListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 基本ViewPager适配器
 * Created by prolog on 4/8/16.
 *
 * @author yyz
 */
public class SupperPagerAdapter<T> extends PagerAdapter {

    protected List<T> adapterData = new ArrayList<>();

    private IPagerBindViewListener listener = null;

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

    public void removeItem(T obj) {
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

    public void setOnBindViewListener(IPagerBindViewListener listener) {
        this.listener = listener;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (listener != null) {
            T data = adapterData.get(position);
            return listener.getPageTitle(position, data);
        }return null;
    }


    @Override
    public int getCount() {
        return adapterData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (listener != null) {
            return listener.isFromObject(view, object);
        }
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        T data = adapterData.get(position);
        if (listener != null) {
            listener.onBindViewHolder(container, position, data);
        }
        return data;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (listener != null) {
            listener.onDestroy(container, position, object);
        }
//        if (adapterData != null) {
//            container.removeView(adapterData.get(position));
//        }
    }


}
