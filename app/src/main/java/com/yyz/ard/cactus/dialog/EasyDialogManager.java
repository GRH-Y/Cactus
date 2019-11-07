package com.yyz.ard.cactus.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.WindowManager;

public class EasyDialogManager {
    private EasyDialogManager() {
    }

    public static void showDialog(EasyBaseDialog baseDialog) {
        EasyDialogConfig config = baseDialog.getDialogConfig();
        Activity activity = config.getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Dialog dialog = new Dialog(config.getActivity(), config.getThemeResId());
        dialog.setCancelable(false);
        dialog.setContentView(config.getContentView());
        //设置dialog背景颜色
        if (config.getDialogBackground() != 0) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(config.getDialogBackground()));
        }
        //设置dialog沉浸式
        if (config.isFullScreenImmersive()) {
            //设置dialog沉浸式效果
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
        //设置dialog大小
        if (config.getWidth() != 0 && config.getHeight() != 0) {
            dialog.getWindow().setLayout(config.getWidth(), config.getHeight());
        }
        baseDialog.setDialog(dialog);
        baseDialog.onController(dialog, baseDialog.getFindView());
        if (!dialog.isShowing() && !activity.isFinishing()) {
            dialog.show();
        }
    }

    public static void dismissDialog(EasyBaseDialog baseDialog) {
        if (baseDialog != null) {
            EasyDialogConfig config = baseDialog.getDialogConfig();
            config.setActivity(null);
            Dialog dialog = baseDialog.getDialog();
            if (dialog != null) {
                dialog.dismiss();
            }
            baseDialog.getFindView().destroy();
        }
    }

}
