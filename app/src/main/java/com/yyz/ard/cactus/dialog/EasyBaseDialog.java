package com.yyz.ard.cactus.dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.yyz.ard.cactus.dialog.joggle.IBaseDialog;
import com.yyz.ard.cactus.uiaf.FindView;

import java.lang.reflect.Method;

import util.StringEnvoy;


/**
 * 自定义dialog
 */
public class EasyBaseDialog implements IBaseDialog {

    private EasyDialogConfig config;
    private FindView mFindView;
    private Object callBackTarget = null;
    private Dialog dialog = null;
    private Object data;

    protected EasyBaseDialog(EasyDialogConfig config) {
        this.config = config;
    }

    protected void setDialog(Dialog dialog) {
        if (dialog != null) {
            this.dialog = dialog;
            mFindView = new FindView(dialog.getWindow().peekDecorView());
        }
    }

    protected Dialog getDialog() {
        return dialog;
    }


    public void setCallBackTarget(Object target) {
        this.callBackTarget = target;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }


    public FindView getFindView() {
        return mFindView;
    }

    protected void setListener(TextView textView, String callBackMethodName) {
        if (StringEnvoy.isNotEmpty(callBackMethodName)) {
            textView.setVisibility(View.VISIBLE);
            if (callBackTarget != null) {
                textView.setOnClickListener(v -> {
                    Class cls = callBackTarget.getClass();
                    try {
                        Method method = cls.getDeclaredMethod(callBackMethodName, getClass());
                        method.setAccessible(true);
                        method.invoke(callBackTarget, this);
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


    protected void onController(Dialog dialog, FindView findView) {
    }
}
