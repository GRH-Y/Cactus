package com.yyz.ard.cactus.xml;

import android.content.ContentValues;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

/**
 * 创建Xml字符串工具类
 *
 * @author pinnacle
 */
public class CreateXML implements IXmlTag {

    private CreateXML() {
    }

    /**
     * 生成xml
     *
     * @param values
     * @return
     */
    public static String create(ContentValues values) {
        StringWriter sw = new StringWriter();
        try {
            XmlSerializer serializer = startDocument(sw);
            Set<String> keyList = values.keySet();
            for (String key : keyList) {
                String value = values.getAsString(key);
                setDocument(serializer, key, value);
            }
            endDocument(serializer);
            sw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    /**
     * 生成xml
     *
     * @param values
     * @return
     */
    public static String create(XmlNodeEntity values) {
        StringWriter sw = new StringWriter();
        try {
            XmlSerializer serializer = startDocument(sw);
            List<String> keyList = values.keySet();
            for (String key : keyList) {
                String value = values.get(key);
                setDocument(serializer, key, value);
            }
            endDocument(serializer);
            sw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    private static XmlSerializer startDocument(StringWriter sw) throws IOException {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(sw);
        // 设置Xml编码格式
        serializer.startDocument("utf-8", true);
        // 设置节点开始标签
        for (int len = 0; len < fixedTagName.length; len++) {
            String str = fixedTagName[len];
            serializer.startTag(null, str);
        }
        return serializer;
    }

    private static void setDocument(XmlSerializer serializer, String key, String value)
            throws IOException {
        if (startTag.equals(value)) {
            // 开始标签
            String tag = key.substring(0, key.length() - value.length());
            serializer.startTag(null, tag);
        } else if (endTag.equals(value)) {
            // 结束标签
            String tag = key.substring(0, key.length() - value.length());
            serializer.endTag(null, tag);
        } else {
            // 文本
            if (value != null) {
                serializer.startTag(null, key);
                serializer.text(value);
                serializer.endTag(null, key);
            }
        }
    }

    private static void endDocument(XmlSerializer serializer) throws IOException {
        // 设置节点结束标签
        for (int len = fixedTagName.length - 1; len >= 0; len--) {
            String str = fixedTagName[len];
            serializer.endTag(null, str);
        }
        // Xml文档组装完成
        serializer.endDocument();
    }
}
