package com.yyz.ard.cactus.uiaf;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity基类
 * Created by dell on 3/5/2018.
 *
 * @author yyz
 */
public class BaseActivity extends AppCompatActivity {

    protected FindView findView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView = new FindView(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        findView.destroy();
    }

}
