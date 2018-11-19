package com.yyz.ard.cactus.network.base;


import com.yyz.ard.cactus.xml.XmlParser;

import connect.json.JsonUtils;
import connect.network.http.joggle.IResponseConvert;

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
