package com.yyz.ard.cactus.dialog;

import android.app.Dialog;

public class EasyDialogManager {
    private EasyDialogManager() {
    }

    public static void showDialog(EasyBaseDialog baseDialog) {
        EasyDialogConfig config = baseDialog.getDialogConfig();
        Dialog dialog = new Dialog(config.getContext(), config.getThemeResId());
        dialog.setCancelable(false);
        dialog.setContentView(config.getLayout());
        baseDialog.setDialog(dialog);
        baseDialog.onController(dialog);
        dialog.show();
    }

    public static void dismissDialog(EasyBaseDialog baseDialog) {
        if (baseDialog != null) {
            Dialog dialog = baseDialog.getDialog();
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

}
