package com.yyz.ard.cactus.network;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;

import com.yyz.ard.cactus.uiaf.ViewAssignment;

import connect.network.base.JavSessionCallBack;
import connect.network.base.RequestEntity;
import task.message.ThreadAnnotation;


/**
 * 请求回调类
 * Created by Dell on 8/8/2017.
 *
 * @author yyz
 */
public class ArdSessionCallBack extends JavSessionCallBack {
    private FindViewByIdHandler handler = null;


    public ArdSessionCallBack(Object target) {
        super(target);
        init();
    }

    public void setViewTarget(Object viewTarget) {
        if (!checkObject(viewTarget)) {
            throw new RuntimeException("object or data is null or object is not instanceof (Activity ,ViewControlLayer ,Window,Fragment) ");
        }
        handler.setViewTarget(viewTarget);
    }


    public ArdSessionCallBack() {
        init();
    }

    private void init() {
        handler = new FindViewByIdHandler();
    }

    /**
     * 检测该实体是否非法
     *
     * @param object
     * @return
     */
    private boolean checkObject(Object object) {
        return object instanceof Activity || object instanceof View || object instanceof Window ||
                object instanceof android.support.v4.app.Fragment || object instanceof android.app.Fragment;
    }



    @Override
    public void notifyMessage(RequestEntity entity) {
        if (handler != null && entity != null) {
            if (entity.getCallBackTarget() == null) {
                entity.setCallBackTarget(target);
            }
            Message msg = Message.obtain();
            msg.obj = entity;
            handler.sendMessage(msg);
        }
    }


    private static class FindViewByIdHandler extends Handler {

        private Object viewTarget = null;

        FindViewByIdHandler() {
            super(Looper.getMainLooper());
        }


        public void setViewTarget(Object viewTarget) {
            this.viewTarget = viewTarget;
        }

        @Override
        public void handleMessage(Message msg) {
            RequestEntity entity = (RequestEntity) msg.obj;
            if (entity.isAutoSetDataForView() && entity.getResultData() != null) {
                Object object = entity.getViewTarget();
                if (object == null) {
                    object = viewTarget;
                }
                if (object == null) {
                    object = entity.getCallBackTarget();
                }
                ViewAssignment.setViewData(object, entity.getResultData());
            }

            String methodName = entity.getResultData() != null ? entity.getSuccessMethodName() : entity.getErrorMethodName();
            ThreadAnnotation.disposeMessage(methodName, entity.getCallBackTarget(), entity.getResultData());
        }
    }
}
