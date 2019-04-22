package com.talkweb.unicom.api.json.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.talkweb.unicom.api.json.IJsonHandler;

import java.util.List;

public class FastJsonHandler implements IJsonHandler {

    @Override
    public String toJson(Object obj) {
        return toJson(obj, DEFAULT_DATE_FORMAT);
    }

    @Override
    public String toJson(Object obj, String dateFormat) {
        if(obj == null) {
            return null;
        }
        if(obj instanceof String) {
            return (String) obj;
        }
        return JSON.toJSONStringWithDateFormat(obj, dateFormat);
    }

    @Override
    public <T> T fromJson(String value, Class<T> clazz) {
        if(value == null) {
            return null;
        }
        if(clazz.isAssignableFrom(String.class)) {
            return (T) value;
        }
        return JSON.parseObject(value, clazz, SerializerFeature.WRITE_MAP_NULL_FEATURES);
    }

    @Override
    public <T> List<T> fromJsonList(String value, Class<T> clazz) {
        if(value == null) {
            return null;
        }
        return JSON.parseArray(value, clazz);
    }

}
