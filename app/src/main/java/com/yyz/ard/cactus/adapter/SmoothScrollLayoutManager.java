package com.yyz.ard.cactus.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * @className: SmoothScrollLayoutManager
 * @classDescription:
 * @author: yyz
 * @createTime: 7/27/2018
 */
public class SmoothScrollLayoutManager extends LinearLayoutManager {

    public SmoothScrollLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 40f / displayMetrics.densityDpi;
            }
        };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
