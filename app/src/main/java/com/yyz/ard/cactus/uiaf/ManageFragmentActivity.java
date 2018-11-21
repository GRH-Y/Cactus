package com.yyz.ard.cactus.uiaf;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.Stack;

/**
 * 该类集成了Fragment管理全套方法
 *
 * @author yyz
 * @date 7/8/2016.
 */
public abstract class ManageFragmentActivity extends BaseActivity {

    protected Stack<BaseFragment> fragmentStack = new Stack<>();
    private int fragmentIndex = -1;


    /**
     * 创建新的fragment并显示出来
     *
     * @param fragment        要显示的fragment
     * @param containerViewId 布局文件id
     */
    public void showFragment(BaseFragment fragment, @IdRes int containerViewId) {
        showFragment(fragment, null, containerViewId);
    }

    public void showFragment(Class<? extends BaseFragment> cls, @IdRes int containerViewId) {
        showFragment(cls, null, containerViewId);
    }

    /**
     * 创建新的fragment并显示出来
     *
     * @param cls             要显示的fragment
     * @param bundle          传递给fragment的数据
     * @param containerViewId 布局文件id
     */
    public void showFragment(Class<? extends BaseFragment> cls, Bundle bundle, @IdRes int containerViewId) {
        if (cls != null) {
            BaseFragment fragment = null;
            try {
                fragment = findFragment(cls.getName());
                if (fragment == null) {
                    fragment = cls.newInstance();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            showFragment(fragment, bundle, containerViewId);
        }
    }

    /**
     * 创建新的fragment并显示出来
     *
     * @param fragment        要显示的fragment
     * @param bundle          传递给fragment的数据
     * @param containerViewId 布局文件id
     */
    public void showFragment(BaseFragment fragment, Bundle bundle, @IdRes int containerViewId) {
        if (fragment != null) {
            if (fragmentIndex > -1) {
                BaseFragment lastFragment = fragmentStack.get(fragmentIndex);
                if (fragment == lastFragment) {
                    //当前的fragment跟要显示的fragment相同则不需要任何处理
                    return;
                }
                hideFragment(lastFragment);
            }
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            if (!fragmentStack.contains(fragment)) {
                String fragmentTag = fragment.getClass().getName();
                fragment.setArguments(bundle);
                fragmentTransaction.add(containerViewId, fragment, fragmentTag);
                fragmentTransaction.commitAllowingStateLoss();
                getFragmentManager().executePendingTransactions();
                fragmentStack.add(fragment);
                fragmentIndex = fragmentStack.size() - 1;
            } else {
                fragmentTransaction.show(fragment);
                fragmentTransaction.commitAllowingStateLoss();
                getFragmentManager().executePendingTransactions();
                fragmentIndex = findFragmentIndex(fragment.getClass().getName());
            }
        }
    }

    // -------------- showFragment --------------------

    /**
     * 隐藏fragment
     *
     * @param fragment 要隐藏fragment
     */
    private void hideFragment(BaseFragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commitAllowingStateLoss();
            getFragmentManager().executePendingTransactions();
        }
    }

    // -------------- hideFragment --------------------

    /**
     * 返回上一个fragment界面
     */
    public void backFragment() {
        finishCurrentFragment();
        showLastFragment();
    }


    // -------------- backFragment --------------------

    /**
     * 返回到最底部fragment界面
     */
    public void backToFirstFragment() {
        popAllToOneFragment();
        showLastFragment();
    }

    // -------------- backToFirstFragment --------------------

    /**
     * 退到指定的Fragment 并显示它
     *
     * @param fragmentIndex
     */
    public void backFragment(int fragmentIndex, Bundle bundle) {
        popFragment(fragmentIndex);
        showLastFragment(bundle);
    }

    // -------------- backFragment --------------------


    /**
     * 查找Stack中判断是否存在指定的Fragment
     *
     * @param fragmentName 指定搜索的Fragment
     * @return 不存在返回-1，存在返回指定Fragment的在Stack的索引
     */
    private int findFragmentIndex(String fragmentName) {
        int index = -1;
        if (fragmentName != null) {
            for (int len = 0; len < fragmentStack.size(); len++) {
                BaseFragment fragment = fragmentStack.get(len);
                String tag = fragment.getTag();
                if (fragmentName.equals(tag)) {
                    index = len;
                    break;
                }
            }
        }
        return index;
    }

    // -------------- findFragmentIndex --------------------

    /**
     * 查找stack中目标Fragment
     *
     * @param fragmentName 目标Fragment名字
     * @return 找不到返回null
     */
    public BaseFragment findFragment(String fragmentName) {
        BaseFragment baseFragment = null;
        int index = findFragmentIndex(fragmentName);
        if (index != -1) {
            baseFragment = fragmentStack.get(index);
        }
        return baseFragment;
    }

    // -------------- findFragment --------------------


    /**
     * 移除当前界面的fragment
     */
    protected void finishCurrentFragment() {
        int size = fragmentStack.size();
        if (size > 1) {
            popFragment(size - 2);
        }
    }

    // -------------- popLastFragment --------------------

    /**
     * 退到指定的界面
     *
     * @param index 往后退索引值
     */
    protected void popFragment(int index) {
        if (index >= 0) {
            index++;
            int size = fragmentStack.size() - index;
            for (int count = 0; count < size; count++) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragmentStack.pop());
                fragmentTransaction.commitAllowingStateLoss();
                getFragmentManager().executePendingTransactions();
            }
        }
    }

    // -------------- popFragment --------------------

    /**
     * 移除当前界面的fragment
     */
    protected void popAllToOneFragment() {
        popFragment(0);
    }

    // -------------- popAllToOneFragment --------------------

    /**
     * 显示stack倒数第2个Fragment
     */
    protected void showLastFragment() {
        showLastFragment(null);
    }

    // -------------- showHasFragment --------------------

    /**
     * 显示栈顶的fragment，并回调onRefreshFragment()
     *
     * @param bundle
     */
    protected void showLastFragment(Bundle bundle) {
        int size = fragmentStack.size();
        if (size > 0) {
            BaseFragment fragment = fragmentStack.peek();
            fragment.setArguments(bundle);
            if (hasWindowFocus()) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.show(fragment);
                fragmentTransaction.commitAllowingStateLoss();
                getFragmentManager().executePendingTransactions();
                fragmentIndex = fragmentStack.size() - 1;
            }
        } else {
            fragmentIndex = -1;
        }
    }

    // -------------- showHasFragment --------------------

    /**
     * 返回栈中最后进来的数据
     *
     * @return 返回一个Fragment
     */
    public BaseFragment getCurrentFragment() {
        BaseFragment baseFragment = null;
        if (!fragmentStack.isEmpty()) {
            baseFragment = fragmentStack.lastElement();
        }
        return baseFragment;
    }

    // -------------- getCurrentFragment --------------------

    /**
     * 设置Fragment的标签
     *
     * @param tag
     */
//    public void setFragmentTag(String tag) {
//        this.fragmentTag = tag;
//    }

    // -------------- setFragmentTag --------------------

    /**
     * 获取当前Fragment标签
     *
     * @return
     */
//    public String getFragmentTag() {
//        return this.fragmentTag;
//    }

    // -------------- getFragmentTag --------------------

//    /**
//     * 查找stack中是否存在目标Fragment
//     *
//     * @param cls
//     * @return
//     */
//    public boolean isTargetFragment(Class<? extends BaseFragment> cls) {
//        boolean result = false;
//        if (cls != null) {
//            BaseFragment fragment = getCurrentFragment();
//            if (fragment != null) {
//                String currentTag = fragment.getTag();
//                result = cls.getName().equalsIgnoreCase(currentTag);
//            }
//        }
//        return result;
//    }

    // -------------- isTargetFragment --------------------

    /**
     * 获取Fragment栈的大小
     *
     * @return
     */
    public int getStackSize() {
        return fragmentStack.size();
    }

    // -------------- getStackSize --------------------

    /**
     * 退出整个应用
     */
    protected void exit() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    // -------------- exit --------------------

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment != null) {
            return baseFragment.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment != null) {
            baseFragment.onBackPressed();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment != null) {
            baseFragment.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }


    // -------------- onKeyDown --------------------

    @Override
    protected void onDestroy() {
        fragmentStack.clear();
        super.onDestroy();
    }
}
