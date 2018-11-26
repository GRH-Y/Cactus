package com.yyz.ard.cactus.xml;

import android.content.ContentValues;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * 创建Xml字符串工具类
 *
 * @author pinnacle
 */
public class XmlCreate implements IXmlTag {

    private static final String TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";

    private XmlCreate() {
    }

    public static String create(Object entity, String... fixedTag) {
        StringBuilder builder = new StringBuilder();
        scanCreateTag(entity, entity.getClass(), builder);
        StringBuilder newBuilder = new StringBuilder();
        for (String str : fixedTag) {
            startTag(newBuilder, str);
        }
        newBuilder.append(builder.toString());
        for (int index = fixedTag.length; index > 0; index--) {
            endTag(newBuilder, fixedTag[index - 1]);
        }
        return TAG + newBuilder.toString();
    }

    private static void scanCreateTag(Object entity, Class clx, StringBuilder builder) {
        Field[] fields = clx.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object object = field.get(entity);
                String text = String.valueOf(object);
                if ("serialVersionUID".equals(field.getName()) || "$change".equals(field.getName())) {
                    continue;
                }
                createText(builder, field.getName(), text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Class superClass = clx.getSuperclass();
        if (superClass != Object.class) {
            scanCreateTag(entity, superClass, builder);
        }
    }

    private static void startTag(StringBuilder builder, String tag) {
        createTag(builder, tag, true, true);
    }

    private static void endTag(StringBuilder builder, String tag) {
        createTag(builder, tag, false, true);
    }

    private static void createText(StringBuilder builder, String tag, String text) {
        createTag(builder, tag, true, false);
        builder.append(text);
        createTag(builder, tag, false, true);
    }

    private static void createTag(StringBuilder builder, String tag, boolean isStartTag, boolean newLine) {
        if (isStartTag) {
            builder.append("<");
        } else {
            builder.append("</");
        }
        builder.append(tag);
        if (newLine) {
            builder.append(">\n");
        } else {
            builder.append(">");
        }
    }

    /**
     * 基本类型转换成String
     *
     * @param value 要转换的字符串
     * @return 返回转换后的数据类型
     */
    private static String converToString(Object value) {
        String str = null;
        String className = value.getClass().getName();
        if (Integer.class.getName().equals(className) || int.class.getName().equals(className)) {
            str = String.valueOf(value);
        } else if (Double.class.getName().equals(className) || double.class.getName().equals(className)) {
            str = String.valueOf(value);
        } else if (Long.class.getName().equals(className) || long.class.getName().equals(className)) {
            str = String.valueOf(value);
        } else if (Boolean.class.getName().equals(className) || boolean.class.getName().equals(className)) {
            str = String.valueOf(value);
        } else if (Float.class.getName().equals(className) || float.class.getName().equals(className)) {
            str = String.valueOf(value);
        } else if (String.class.getName().equals(className)) {
            str = (String) value;
        }
        return str;
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
