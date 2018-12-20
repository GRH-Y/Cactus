package com.yyz.ard.cactus.network;


import com.yyz.ard.cactus.xml.XmlParser;

import connect.network.base.JavConvertResult;
import json.JsonUtils;
import util.LogDog;

public class ConvertResult extends JavConvertResult {

    @Override
    public Object handlerEntity(Class resultCls, byte[] result) {
        if (resultCls == null && result == null) {
            return null;
        }
        String jsonStr = new String(result);
        LogDog.d("==> Request to return the content = " + jsonStr);
        Object entity;
        if (jsonStr.startsWith("<?xml")) {
            entity = XmlParser.parserToEntity(jsonStr, resultCls);
        } else {
            entity = JsonUtils.toEntity(resultCls, jsonStr);
        }
        return entity;
    }
}
