package com.yyz.ard.cactus.adapter.dialog;

import android.app.Dialog;

import com.yyz.ard.cactus.adapter.dialog.joggle.ICustomDialog;
import com.yyz.ard.cactus.adapter.dialog.joggle.IDialogController;
import com.yyz.ard.cactus.uiaf.FindView;

import java.lang.reflect.Method;


/**
 * 自定义dialog
 */
class CustomDialog implements ICustomDialog {


    private DialogConfig config;
    private IDialogController controller;

    protected CustomDialog(DialogConfig config, IDialogController controller) {
        this.config = config;
        this.controller = controller;
    }

    @Override
    public void setViewClickListener(int rid, Object clickListener, String callBackMethodName, String text) {
        Dialog dialog = config.getDialog();
        FindView findView = new FindView(dialog.getCurrentFocus());
        findView.setViewText(rid, text);
        findView.setViewListenerById(rid, v -> {
            Class cls = clickListener.getClass();
            try {
                Method method = cls.getDeclaredMethod(callBackMethodName, Dialog.class);
                method.invoke(clickListener, dialog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public DialogConfig getDialogConfig() {
        return config;
    }

    @Override
    public IDialogController getDialogController() {
        return controller;
    }
}
