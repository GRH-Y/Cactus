package com.yyz.ard.cactus.dialog;

import android.app.Dialog;

import com.yyz.ard.cactus.dialog.joggle.IBaseDialog;
import com.yyz.ard.cactus.dialog.joggle.ICurrencyDialog;
import com.yyz.ard.cactus.dialog.joggle.IDialogController;

public class EasyDialog {
    private EasyDialog() {
    }

    public static void showDialog(IBaseDialog baseDialog) {
        DialogConfig config = baseDialog.getDialogConfig();
        Dialog dialog = new Dialog(config.getContext());
        dialog.setContentView(config.getLayout());
        config.setDialog(dialog);
        if (baseDialog instanceof ICurrencyDialog) {

        }
        IDialogController controller = baseDialog.getDialogController();
        if (controller != null) {
            controller.onController(dialog);
        }
        dialog.show();
    }


}
