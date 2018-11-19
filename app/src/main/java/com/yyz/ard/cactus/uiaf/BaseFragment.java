package com.yyz.ard.cactus.uiaf;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * 基本Fragment，集成了常用的方法
 *
 * @author yyz
 * @date 7/8/2016.
 */
public class BaseFragment extends Fragment {

    protected ManageFragmentActivity activity = null;
    protected FindView findView = null;

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof ManageFragmentActivity) {
            this.activity = (ManageFragmentActivity) activity;
        }
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView = new FindView(getActivity(), getView());
    }

    @Override
    public void onDestroy() {
        findView.destroy();
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            onPause();
        } else {
            onResume();
        }
    }

    /**
     * 开启界面
     *
     * @param fragment
     */
    protected void startFragment(BaseFragment fragment, int layoutId) {
        if (activity != null) {
            activity.showFragment(fragment, layoutId);
        }
    }

    /**
     * 关闭当前界面，返回上个界面
     */
    protected void finishFragment() {
        if (activity != null) {
            activity.backFragment();
        }
    }

    /**
     * 返回到最开始的界面
     */
    protected void backToFirstFragment() {
        if (activity != null) {
            activity.backToFirstFragment();
        }
    }


    /**
     * 在UI线程运行runnable
     *
     * @param runnable
     */
    public void runOnUiThread(Runnable runnable) {
        if (activity != null) {
            activity.runOnUiThread(runnable);
        }
    }


    /**
     * 3大按钮的监听回调(后退,home,菜单)
     *
     * @param keyCode
     * @param event
     * @return
     */
    protected boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }

    protected void onBackPressed() {
    }


    /**
     * 屏幕触摸监听回调
     *
     * @param event
     */
    protected void onTouchEvent(MotionEvent event) {
    }

}
