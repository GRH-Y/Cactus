package com.yyz.ard.cactus.network;


import com.yyz.ard.cactus.uiaf.SingleHandler;

import connect.network.http.RequestEntity;
import connect.network.http.tool.JavSessionCallBack;


/**
 * 请求回调类
 * Created by Dell on 8/8/2017.
 *
 * @author yyz
 */
public class ArdSessionCallBack extends JavSessionCallBack {

    @Override
    public void notifyData(RequestEntity entity) {
        SingleHandler.getInstance().getHandler().post(() -> notifyDataImp(entity));
    }

    @Override
    public void notifyProcess(RequestEntity entity, int process, int maxProcess, boolean isOver) {
        SingleHandler.getInstance().getHandler().post(() -> super.notifyProcess(entity, process, maxProcess, isOver));
    }

}
