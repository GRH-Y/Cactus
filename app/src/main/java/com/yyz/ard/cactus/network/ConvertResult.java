package com.yyz.ard.cactus.network;


import com.yyz.ard.cactus.xml.XmlParser;

import connect.network.http.joggle.IResponseConvert;
import json.JsonUtils;

public class ConvertResult implements IResponseConvert {

    @Override
    public Object handlerEntity(Class resultCls, String result) {
        Object entity;
        if (result.startsWith("<?xml")) {
            entity = XmlParser.parserToEntity(result, resultCls);
        } else {
            entity = JsonUtils.toEntity(resultCls, result);
        }
        return entity;
    }
}
