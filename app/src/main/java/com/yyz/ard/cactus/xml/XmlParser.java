package com.yyz.ard.cactus.xml;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.SpeedReflex;


/**
 * 通用的xml解析
 *
 * @author YDZ
 * @version 1.0
 * @time 2015-02-06
 */
public class XmlParser implements IXmlTag {

    private static XmlPullParser parser = null;
    private static String regex = ">\\s*|\\n|\\t|\\r<";


    public static <T> T parserToEntity(@NonNull String xmlStr, @NonNull String className) {
        try {
            Class<T> newClass = (Class<T>) Class.forName(className);
            return parserToEntity(xmlStr, newClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析xml成实体
     *
     * @param xmlStr xml字符串
     * @param cls    要转换的实体
     * @param <T>    实体
     * @return 实体
     */
    public static <T> T  parserToEntity(@NonNull String xmlStr, @NonNull Class<T> cls) {
        T entity = null;
        //处理字符串的空格、回车、换行符、制表符
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(xmlStr);
        xmlStr = m.replaceAll(">");
        SpeedReflex cache = SpeedReflex.getCache();
        cache.setClass(cls);

        StringReader reader = new StringReader(xmlStr);
        try {
            int eventType = getXmlPullParser(reader);
            String insideTag = null;
            Field field = null;
            Class insideClass = null;
            Object insideEntity = null;
            List list = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        entity = cls.newInstance();
                        break;
                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (field != null) {
                            if (insideEntity != null) {
                                setValue(insideEntity, field, text);
                            } else {
                                setValue(entity, field, text);
                            }
                            field = null;
                        }
                        break;
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if (insideClass != null) {
                            field = cache.getField(insideClass, startTag);
//                            field = getField(insideClass, startTag);
                        } else {
                            field = cache.getField(cls, startTag);
//                            field = getField(cls, startTag);
                        }
                        if (field == null) {
                            //找不到变量则查找下个字段
                            eventType = parser.next();
                            continue;
                        }
                        try {
                            field.setAccessible(true);
                            Class type = field.getType();
                            Type genericType = field.getGenericType();
                            if (type == List.class) {
                                //对象是list,获取内部类
                                Class tmpClx = genericType.getClass();
                                if (list == null || insideClass == tmpClx) {
                                    //如果空或者不同的类型内部类则重新创建list
                                    list = new ArrayList<>();
                                }
                                ParameterizedType pt = (ParameterizedType) genericType;
                                //得到泛型里的class类型对象
                                insideClass = (Class<?>) pt.getActualTypeArguments()[0];
                                try {
                                    insideEntity = insideClass.newInstance();
                                } catch (Exception e) {
                                    Constructor constructor = insideClass.getDeclaredConstructors()[0];
                                    constructor.setAccessible(true);
                                    insideEntity = constructor.newInstance(entity);
                                }
                                list.add(insideEntity);
                                field.set(entity, list);
                                insideTag = startTag;
                            } else if (type != int.class && type != long.class && type != double.class
                                    && type != boolean.class && type != String.class && type != float.class) {
                                //对象不是list，而是内部类
                                insideClass = Class.forName(((Class) genericType).getName());
                                try {
                                    insideEntity = insideClass.newInstance();
                                } catch (Exception e) {
                                    Constructor constructor = insideClass.getDeclaredConstructors()[0];
                                    constructor.setAccessible(true);
                                    insideEntity = constructor.newInstance(entity);
                                }
                                field.set(entity, insideEntity);
                                insideTag = startTag;
                            }
                        } catch (Exception e) {
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals(insideTag)) {
                            insideClass = null;
                            insideEntity = null;
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            entity = null;
            e.printStackTrace();
        } finally {
            reader.close();
        }
        return entity;
    }

    private static void setValue(Object entity, Field field, String text) {
        if (entity == null || field == null) {
            return;
        }
        try {
            Class type = field.getType();
            Object value = classNameToClassValue(type.getName(), text);
            field.set(entity, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Field getField(Class clx, String fieldName) {
        try {
            return clx.getDeclaredField(fieldName);
        } catch (Exception e) {
            Class supperClx = clx.getSuperclass();
            if (supperClx != Object.class) {
                return getField(supperClx, fieldName);
            } else {
                return null;
            }
        }
    }

    /**
     * 字符串转指定类型的数据
     *
     * @param typeSimpleName 指定的类型
     * @param value          要转换的字符串
     * @return 返回转换后的数据类型
     */
    private static Object classNameToClassValue(String typeSimpleName, String value) {
        Object object = null;
        if (Integer.class.getName().equals(typeSimpleName) || int.class.getName().equals(typeSimpleName)) {
            object = Integer.parseInt(value);
        } else if (Double.class.getName().equals(typeSimpleName) || double.class.getName().equals(typeSimpleName)) {
            object = Double.parseDouble(value);
        } else if (Long.class.getName().equals(typeSimpleName) || long.class.getName().equals(typeSimpleName)) {
            object = Long.parseLong(value);
        } else if (Boolean.class.getName().equals(typeSimpleName) || boolean.class.getName().equals(typeSimpleName)) {
            object = Boolean.parseBoolean(value);
        } else if (Float.class.getName().equals(typeSimpleName) || float.class.getName().equals(typeSimpleName)) {
            object = Float.parseFloat(value);
        } else if (String.class.getName().equals(typeSimpleName)) {
            object = value;
        }
        return object;
    }

    /**
     * 解析通讯协议(适用于key一样)
     *
     * @param paramString 要解析的xml字符串
     * @return 成功返回解析后的结果，失败返回null
     */
    public static XmlNodeEntity parserToEntity(String paramString, String... fixedTag) {
        XmlNodeEntity nodeEntity = null;
        if (paramString != null && paramString.length() > 0) {
            StringReader reader = new StringReader(paramString);
            int eventType = getXmlPullParser(reader);
            String tag = null;

            if (parser != null) {
                try {
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                nodeEntity = new XmlNodeEntity();
                                break;
                            case XmlPullParser.TEXT:
                                String text = getText(tag, fixedTag);
                                if (text != null) {
                                    nodeEntity.put(tag, text, true);
                                }
                                break;
                            case XmlPullParser.START_TAG:
                                tag = parser.getName();
                                break;
                            default:
                                break;
                        }
                        eventType = parser.next();
                    }
                    reader.close();
                } catch (Exception e) {
                    nodeEntity = null;
                    e.printStackTrace();
                }
            }
        }
        return nodeEntity;
    }

    /**
     * 解析通讯协议(适用于key一样)
     *
     * @param paramString 要解析的xml字符串
     * @return 成功返回解析后的结果，失败返回null
     */
    public static List<String> parserToList(String paramString, String... fixedTag) {
        List<String> list = null;
        if (paramString != null && paramString.length() > 0) {
            StringReader reader = new StringReader(paramString);
            int eventType = getXmlPullParser(reader);
            String tag = null;

            if (parser != null) {
                try {
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                list = new ArrayList<>();
                                break;
                            case XmlPullParser.TEXT:
                                String text = getText(tag, fixedTag);
                                if (text != null) {
                                    list.add(text);
                                }
                                break;
                            case XmlPullParser.START_TAG:
                                tag = parser.getName();
                                break;
                            default:
                                break;
                        }
                        eventType = parser.next();
                    }
                    reader.close();
                } catch (Exception e) {
                    list = null;
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 解析通讯协议
     *
     * @param paramString 要解析的xml字符串
     * @return 成功返回解析后的结果，失败返回null
     */
    public static HashMap<String, String> parserToHashMap(String paramString, String... fixedTag) {
        HashMap<String, String> hashMap = null;
        if (paramString != null && paramString.length() > 0) {
            StringReader reader = new StringReader(paramString);
            int eventType = getXmlPullParser(reader);
            String tag = null;

            if (parser != null) {
                try {
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                hashMap = new HashMap<>();
                                break;
                            case XmlPullParser.TEXT:
                                String text = getText(tag, fixedTag);
                                if (text != null) {
                                    hashMap.put(tag, text);
                                }
                                break;
                            case XmlPullParser.START_TAG:
                                tag = parser.getName();
                                break;
                            default:
                                break;
                        }
                        eventType = parser.next();
                    }
                    reader.close();
                } catch (Exception e) {
                    hashMap = null;
                    e.printStackTrace();
                }
            }
        }
        return hashMap;
    }

    /**
     * 解析通讯协议
     *
     * @param paramString 要解析的xml字符串
     * @return 成功返回解析后的结果，失败返回null
     */
    public static ContentValues parserToContentValues(String paramString, String... fixedTag) {
        ContentValues values = null;
        if (paramString != null && paramString.length() > 0) {
            StringReader reader = new StringReader(paramString);
            int eventType = getXmlPullParser(reader);
            String tag = null;

            if (parser != null) {
                try {
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                values = new ContentValues();
                                break;
                            case XmlPullParser.TEXT:
                                String text = getText(tag, fixedTag);
                                if (text != null) {
                                    values.put(tag, text);
                                }
                                break;
                            case XmlPullParser.START_TAG:
                                tag = parser.getName();
                                break;
                            default:
                                break;
                        }
                        eventType = parser.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    reader.close();
                }
            }
        }
        return values;
    }

    /**
     * Returns the type of the current event (START_TAG, END_TAG, TEXT, etc.)
     *
     * @param reader
     */
    private static int getXmlPullParser(StringReader reader) {
        int eventType = -1;
        try {
            parser = Xml.newPullParser();
            parser.setInput(reader);
            eventType = parser.getEventType();
        } catch (Exception e) {
            parser = null;
            e.printStackTrace();
        }
        return eventType;
    }

    /**
     * 获取节点的内容
     *
     * @param tag      节点的tag名
     * @param fixedTag 过滤的字段
     * @return 返回节点的值，如果开启了过滤有可能会返回null
     */
    private static String getText(String tag, String... fixedTag) {
        String text = null;
        boolean isMatch = false;
        if (tag != null) {
            for (int len = 0; len < fixedTag.length; len++) {
                String str = fixedTag[len];
                if (str.equals(tag)) {
                    isMatch = true;
                    break;
                }
            }
            if (isMatch == false) {
                text = parser.getText();
            }
        }
        return text;
    }

}
