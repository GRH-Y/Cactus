package com.yyz.ard.cactus.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yyz
 */
public class XmlNodeEntity {
    private Map<String, String> values = null;
    private List<String> keyList = null;
    private int len = 0;

    public XmlNodeEntity() {
        values = new HashMap<>();
        keyList = new ArrayList<>();
    }

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, String value) {
        boolean isExist = false;
        values.put(key, value);

        for (String str : keyList) {
            isExist = str.equals(key);
        }
        if (isExist == false) {
            keyList.add(key);
        }
    }

    /**
     * Adds a value to the set.
     *
     * @param key      the name of the value to put
     * @param value    the data for the value to put
     * @param isRepeat true key is allow repeat
     */
    public void put(String key, String value, boolean isRepeat) {
        boolean isExist = false;

        for (String str : keyList) {
            isExist = str.equalsIgnoreCase(key);
            if (isRepeat && isExist) {
                key = key + len;
                len++;
            }
        }

        if (isExist == false || isRepeat) {
            keyList.add(key);
            values.put(key, value);
        }
    }

    /**
     * Returns a set of all of the keys
     *
     * @return a set of all of the keys
     */
    public List<String> keySet() {
        return keyList;
    }

    /**
     * Gets a value and converts it to a String.
     *
     * @param key the value to get
     * @return the String for the value
     */
    public String get(String key) {
        return values.get(key);
    }

    /**
     * Returns the number of values.
     *
     * @return the number of values
     */
    public int size() {
        return values.size();
    }

    /**
     * Removes a mapping with the specified key from this {@code Map}.
     *
     * @param key the key of the mapping to remove.
     * @return the value of the removed mapping or {@code null} if no mapping for the specified key
     * was found.
     * @throws UnsupportedOperationException if removing from this {@code Map} is not supported.
     */
    public String remove(Object key) {
        keyList.remove(key);
        return values.remove(key);
    }
}
