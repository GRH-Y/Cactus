package com.yyz.ard.cactus.network;


import com.yyz.ard.cactus.xml.XmlParser;

import connect.network.base.JavConvertResult;
import json.JsonUtils;
import util.GZipUtils;
import util.LogDog;

public class ArdConvertResult extends JavConvertResult {

    @Override
    public Object handlerEntity(Class resultCls, byte[] result, String encode) {
        if (resultCls == null || result == null) {
            return null;
        }

        Object entity;
        byte[] newData = result;
        if ("gzip".equals(encode)) {
            byte[] unCompressData = GZipUtils.unCompress(result);
            newData = unCompressData == null ? result : unCompressData;
        }
        if (resultCls.isAssignableFrom(byte[].class)) {
            return newData;
        }

        String resultStr = new String(newData);
        LogDog.d("==> Request to return the content = " + resultStr);
        if (resultStr.startsWith("<?xml")) {
            entity = XmlParser.parserToEntity(resultStr, resultCls);
        } else {
            entity = JsonUtils.toEntity(resultCls, resultStr);
        }
        return entity;
    }
}
