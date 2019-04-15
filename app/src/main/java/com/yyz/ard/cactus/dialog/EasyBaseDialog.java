package com.yyz.ard.cactus.dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.yyz.ard.cactus.dialog.joggle.ADialogCallBack;
import com.yyz.ard.cactus.dialog.joggle.IBaseDialog;

import java.lang.reflect.Method;

import util.StringEnvoy;


/**
 * 自定义dialog
 */
public class EasyBaseDialog implements IBaseDialog {

    private EasyDialogConfig config;
    private Object clickListener = null;
    private Dialog dialog = null;
    private Object data;

    protected EasyBaseDialog(EasyDialogConfig config) {
        this.config = config;
    }

    protected void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    protected Dialog getDialog() {
        return dialog;
    }

    public void setClickListener(Object clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }


    protected void setListener(TextView textView, String callBackMethodName) {
        if (StringEnvoy.isNotEmpty(callBackMethodName)) {
            textView.setVisibility(View.VISIBLE);
            if (clickListener != null) {
                textView.setOnClickListener(v -> {
                    Class cls = clickListener.getClass();
                    try {
                        Method method = cls.getDeclaredMethod(callBackMethodName, getClass());
                        method.setAccessible(true);
                        method.invoke(clickListener, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
            }
        }
    }

    @Override
    public EasyDialogConfig getDialogConfig() {
        return config;
    }


    protected void onController(Dialog dialog) {
    }
}
