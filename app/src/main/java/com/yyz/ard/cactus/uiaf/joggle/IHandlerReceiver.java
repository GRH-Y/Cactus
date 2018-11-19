package com.yyz.ard.cactus.uiaf.joggle;

import android.os.Bundle;
import android.os.Message;

/**
 * Created by prolog on 2017/12/11.
 */

public interface IHandlerReceiver {

    void onHasMessage(int what, Bundle bundle,Object obj, Message msg);
}
