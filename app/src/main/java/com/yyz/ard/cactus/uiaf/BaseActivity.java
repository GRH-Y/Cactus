package com.yyz.ard.cactus.uiaf;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Activity基类
 * Created by dell on 3/5/2018.
 *
 * @author yyz
 */
public class BaseActivity extends AppCompatActivity {

    protected FindView findView;

    private double downX = 0;

    private boolean enableSlideFinish = false;

    //是否点击返回键回到桌面
    private boolean isMoveTaskToBack = false;

    /**
     * 设置是否开启左滑关闭当前界面
     *
     * @param enableSlideFinish
     */
    public void setEnableSlideFinish(boolean enableSlideFinish) {
        this.enableSlideFinish = enableSlideFinish;
    }

    /**
     * /设置点击返回键是否回到桌面
     *
     * @param moveTaskToBack
     */
    public void setMoveTaskToBack(boolean moveTaskToBack) {
        isMoveTaskToBack = moveTaskToBack;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView = new FindView(this);
        ActivitySupervise.getSupervise().pushActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (findView != null) {
            findView.destroy();
        }
        ActivitySupervise.getSupervise().popActivity(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (enableSlideFinish) {
            return execTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    protected boolean execTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = event.getX();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() - downX > 100) {
                finish();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) && isMoveTaskToBack) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
