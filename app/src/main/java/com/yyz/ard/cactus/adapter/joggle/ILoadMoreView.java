package com.yyz.ard.cactus.adapter.joggle;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

/**
 * Created by dell on 3/22/2018.
 * @author yyz
 */

public interface ILoadMoreView {

    /**
     * load more layout
     *
     * @return
     */
    @LayoutRes
    int getLayoutId();

    /**
     * loading view
     *
     * @return
     */
    @IdRes
    int getLoadingViewId();

    /**
     * load fail view
     *
     * @return
     */
    @IdRes
    int getLoadFailViewId();

    /**
     * load end view, you can return 0
     *
     * @return
     */
    @IdRes
    int getLoadEndViewId();
}
