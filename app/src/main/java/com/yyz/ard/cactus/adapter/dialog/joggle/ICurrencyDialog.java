package com.yyz.ard.cactus.adapter.dialog.joggle;

public interface ICurrencyDialog extends IBaseDialog {

    void setContent(String content);

    void setTitle(String title);

    String getContent();

    String getTitle();

    void setLeftBtnClickListener(Object clickListener, String callBackMethodName, String text);

    void setMiddleBtnClickListener(Object clickListener, String callBackMethodName, String text);

    void setRightBtnClickListener(Object clickListener, String callBackMethodName, String text);
}
