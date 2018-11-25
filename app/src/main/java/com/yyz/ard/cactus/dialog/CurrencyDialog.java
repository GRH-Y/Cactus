package com.yyz.ard.cactus.dialog;

import com.yyz.ard.cactus.dialog.joggle.ICurrencyDialog;
import com.yyz.ard.cactus.dialog.joggle.IDialogController;

/**
 * 通用的dialog
 */
class CurrencyDialog implements ICurrencyDialog {

    private DialogConfig config;
    private IDialogController controller;
    private String content = null;
    private String title = null;

    protected CurrencyDialog(DialogConfig config, IDialogController controller) {
        this.config = config;
        this.controller = controller;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setLeftBtnClickListener(Object clickListener, String callBackMethodName, String text) {

    }

    @Override
    public void setMiddleBtnClickListener(Object clickListener, String callBackMethodName, String text) {

    }

    @Override
    public void setRightBtnClickListener(Object clickListener, String callBackMethodName, String text) {

    }

    @Override
    public DialogConfig getDialogConfig() {
        return config;
    }

    @Override
    public IDialogController getDialogController() {
        return controller;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
