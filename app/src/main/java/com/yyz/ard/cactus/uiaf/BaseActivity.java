package com.yyz.ard.cactus.uiaf;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    /**
     * 设置是否开启左滑关闭当前界面
     * @param enableSlideFinish
     */
    public void setEnableSlideFinish(boolean enableSlideFinish) {
        this.enableSlideFinish = enableSlideFinish;
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
        findView.destroy();
        ActivitySupervise.getSupervise().popActivity(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (enableSlideFinish) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                downX = event.getX();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() - downX > 100) {
                    finish();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

}
