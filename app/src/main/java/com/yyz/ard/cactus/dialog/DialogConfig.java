package com.yyz.ard.cactus.dialog;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.yyz.ard.cactus.dialog.joggle.ICurrencyDialog;
import com.yyz.ard.cactus.dialog.joggle.ICustomDialog;
import com.yyz.ard.cactus.dialog.joggle.IDialogController;

public class DialogConfig {

    private Context context;
    private int themeResId;
    private int layout;

    private Dialog dialog;

    private DialogConfig() {
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getThemeResId() {
        return themeResId;
    }

    public void setThemeResId(int themeResId) {
        this.themeResId = themeResId;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    protected void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    protected Dialog getDialog() {
        return dialog;
    }

    public static ICurrencyDialog builder(@NonNull Context context, IDialogController controller) {
        if (context == null) {
            throw new NullPointerException("DialogConfig builder context is null !!! ");
        }
        DialogConfig config = new DialogConfig();
        config.setContext(context);
        return new CurrencyDialog(config, controller);
    }

    public static ICustomDialog builder(@NonNull Context context, @StyleRes int themeResId,
                                        @LayoutRes int layout, IDialogController controller) {
        if (context == null) {
            throw new NullPointerException("DialogConfig builder context is null !!! ");
        }
        DialogConfig config = new DialogConfig();
        config.setContext(context);
        config.setThemeResId(themeResId);
        config.setLayout(layout);
        return new CustomDialog(config, controller);

    }

}
