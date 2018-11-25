package com.yyz.ard.cactus.dialog.joggle;

public interface ICustomDialog extends IBaseDialog {

    void setViewClickListener(int rid, Object clickListener, String callBackMethodName, String text);
}
