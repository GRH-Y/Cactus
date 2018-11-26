package com.yyz.ard.cactus.adapter.joggle;

import android.view.View;
import android.view.ViewGroup;

public interface IPagerBindViewListener<T> {

    CharSequence getPageTitle(int position, T object);

    boolean isFromObject(View view, T object);

    void onBindViewHolder(ViewGroup container, int position, T object);

    void onDestroy(ViewGroup container, int position, T object);
}
