package com.yyz.ard.cactus.xml;

/**
 * @author yyz
 */
public interface IXmlTag {

    /**
     * 顶层固定的节点
     */
    String fixedTagName[] = {"Protocol", "Packet"};

    /**
     * 解析过滤的节点名
     */
    String filterTagName[] = {"Date", "KEY_DEVICE"};

    // 开始标签
    String startTag = "<";
    // 结束标签
    String endTag = ">";
}
