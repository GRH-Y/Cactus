package com.yyz.ard.cactus.network;


import connect.network.http.JavHttpConnect;
import connect.network.http.tool.JavConvertResult;
import connect.network.http.tool.JavSessionCallBack;

/**
 * 适用于android系统的http网络通讯
 * Created by No.9 on 7/10/2017.
 *
 * @author yyz
 */
public class ArdHttpConnect extends JavHttpConnect {

    private ArdHttpConnect() {
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected JavConvertResult initJavConvertResult() {
        return new ArdConvertResult();
    }

    @Override
    protected JavSessionCallBack initJavSessionCallBack() {
        return new ArdSessionCallBack();
    }
}
