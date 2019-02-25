package com.yyz.ard.cactus.uiaf;


import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.View;

import java.lang.reflect.Method;

import util.SpeedReflex;


/**
 * 控件操作类
 * Created by dell on 3/14/2018.
 *
 * @author yyz
 */

public class FindView {

    private SparseArray<View> mViews;
    private Activity mActivity = null;
    private View mLayout = null;
    private SpeedReflex mCache = null;

    public FindView(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null ");
        }
        this.mActivity = activity;
        init();
    }

    public FindView(View layout) {
        if (layout == null) {
            throw new NullPointerException("layout is null ");
        }
        this.mLayout = layout;
        init();
    }

    public FindView(Activity activity, View layout) {
        if (layout == null && activity == null) {
            throw new NullPointerException("activity or layout is null ");
        }
        this.mActivity = activity;
        this.mLayout = layout;
        init();
    }

    public void change(Activity activity) {
        this.mActivity = activity;
    }

    public void change(View layout) {
        this.mLayout = layout;
    }

    private void init() {
        mViews = new SparseArray<>();
        mCache = SpeedReflex.getCache();
    }


    public Object getActivityOrView() {
        return mLayout == null ? mActivity : mLayout;
    }

//    public Activity getActivity() {
//        return mActivity;
//    }

    public void destroy() {
        mActivity = null;
        mLayout = null;
        mCache = null;
        if (mViews != null) {
            mViews.clear();
            mViews = null;
        }
    }

    /**
     * 根据控件查找控件对象
     *
     * @param resId 控件id
     * @return view对象
     */
    public <T extends View> T findViewById(@IdRes int resId) {
        T view = null;
        if (mViews != null) {
            view = (T) mViews.get(resId);
            if (view == null) {
                if (mLayout != null) {
                    view = mLayout.findViewById(resId);
                }
                if (mActivity != null && view == null) {
                    view = mActivity.findViewById(resId);
                }
                if (view != null) {
                    mViews.put(resId, view);
                }
            }
        }
        return view;
    }

    /**
     * 查找控件并设置事件监听器
     *
     * @param resId    控件id
     * @param listener 事件监听器
     * @return view对象
     */
    public <T extends View> T setViewListenerById(@IdRes int resId, View.OnClickListener listener) {
        T view = findViewById(resId);
        setViewClickListener(view, listener);
        return view;
    }

    public <T extends View> T setViewClickListener(T view, View.OnClickListener listener) {
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return view;
    }

    public <T extends View> T setViewLongClickListenerById(@IdRes int resId, View.OnLongClickListener listener) {
        T view = findViewById(resId);
        setViewLongClickListener(view, listener);
        return view;
    }


    public <T extends View> T setViewLongClickListener(T view, View.OnLongClickListener listener) {
        if (view != null) {
            view.setOnLongClickListener(listener);
        }
        return view;
    }

    public <T extends View> T setViewText(@IdRes int resId, @StringRes int text) {
        T view = findViewById(resId);
        if (view != null) {
            try {
                Class clx = view.getClass();
                Method method = mCache.getMethod(clx, "setText", int.class);
//                Method method = mCache.getMethod(clx, "setText", int.class);
                method.invoke(view, text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public <T extends View> T setViewText(@IdRes int resId, String text) {
        T view = findViewById(resId);
        if (view != null) {
            try {
                Class clx = view.getClass();
                Method method = mCache.getMethod(clx, "setText", CharSequence.class);
//                Method method = clx.getMethod("setText", CharSequence.class);
                method.invoke(view, text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public <T extends View> T setViewColor(@IdRes int resId, @ColorRes int color) {
        T view = findViewById(resId);
        if (view != null) {
            try {
                Class clx = view.getClass();
                Method method = mCache.getMethod(clx, "setTextColor", int.class);
//                Method method = clx.getMethod("setTextColor", int.class);
                method.invoke(view, color);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }


    public <T extends View> T setViewImageResource(@IdRes int resId, @DrawableRes int imageId) {
        T view = findViewById(resId);
        if (view != null) {
            try {
                Class clx = view.getClass();
                Method method = mCache.getMethod(clx, "setImageResource", int.class);
//                Method method = mCache.getMethod(clx, "setImageResource", int.class);
                method.invoke(view, imageId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public <T extends View> T setViewImageResource(@IdRes int resId, @DrawableRes int imageId, View.OnClickListener listener) {
        T view = setViewImageResource(resId, imageId);
        setViewClickListener(view, listener);
        return view;
    }

    public <T extends View> T setViewTextAndListener(@IdRes int resId, @StringRes int text, View.OnClickListener listener) {
        T view = setViewText(resId, text);
        return setViewClickListener(view, listener);
    }

    public <T extends View> T setViewTextAndListener(@IdRes int resId, String text, View.OnClickListener listener) {
        T view = setViewText(resId, text);
        return setViewClickListener(view, listener);
    }

    public <T extends View> T setViewBackgroundResource(@IdRes int resId, @DrawableRes int imageId) {
        T view = findViewById(resId);
        if (view != null) {
            try {
                Class clx = view.getClass();
                Method method = mCache.getMethod(clx, "setBackgroundResource", int.class);
                method.invoke(view, imageId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public <T extends View> T setVisibility(@IdRes int resId, boolean isVisible) {
        T view = findViewById(resId);
        if (view != null) {
            boolean currentState = view.getVisibility() == View.VISIBLE;
            if (currentState != isVisible) {
                view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        }
        return view;
    }

    public <T extends View> T setVisibile(@IdRes int resId) {
        T view = findViewById(resId);
        if (view != null && view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public <T extends View> T setGone(@IdRes int resId) {
        T view = findViewById(resId);
        if (view != null && view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
        return view;
    }
}
