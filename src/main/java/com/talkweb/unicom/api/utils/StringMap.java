package com.talkweb.unicom.api.utils;

import java.util.HashMap;

public class StringMap extends HashMap<String, Object> {

    public String getString(String key, String defaultValue) {
        Object value = get(key);
        if(value == null) {
            return defaultValue;
        }
        if(value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    public String getString(String key) {
        return getString(key, null);
    }

}
