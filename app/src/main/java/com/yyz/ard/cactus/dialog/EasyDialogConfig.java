package com.yyz.ard.cactus.dialog;


import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;

public class EasyDialogConfig {

    private Activity activity;
    private int themeResId;
    private int layoutResID;
    private int dialogBackground = 0;
    private boolean fullScreenImmersive = false;
    private int width;
    private int height;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getThemeResId() {
        return themeResId;
    }

    public void setThemeResId(@StyleRes int themeResId) {
        this.themeResId = themeResId;
    }

    public int getContentView() {
        return layoutResID;
    }

    public void setContentView(@LayoutRes int layoutResID) {
        this.layoutResID = layoutResID;
    }

    public int getDialogBackground() {
        return dialogBackground;
    }

    public void setDialogBackground(@ColorInt int dialogBackground) {
        this.dialogBackground = dialogBackground;
    }

    public void setFullScreenImmersive(boolean fullScreenImmersive) {
        this.fullScreenImmersive = fullScreenImmersive;
    }

    public boolean isFullScreenImmersive() {
        return fullScreenImmersive;
    }

    public void setLayout(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
