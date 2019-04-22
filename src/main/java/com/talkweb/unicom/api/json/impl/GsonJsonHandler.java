package com.talkweb.unicom.api.json.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.talkweb.unicom.api.json.IJsonHandler;

import java.util.List;

public class GsonJsonHandler implements IJsonHandler {

    @Override
    public String toJson(Object obj) {
        return toJson(obj, DEFAULT_DATE_FORMAT);
    }

    private Gson createGson(String dateFormat) {
        return new GsonBuilder().setDateFormat(dateFormat).disableHtmlEscaping().serializeNulls().create();
    }

    @Override
    public String toJson(Object obj, String dateFormat) {
        if(obj == null) {
            return null;
        }
        if(obj instanceof String) {
            return (String) obj;
        }
        return createGson(dateFormat).toJson(obj);
    }

    @Override
    public <T> T fromJson(String value, Class<T> clazz) {
        if(value == null) {
            return null;
        }
        if(clazz.isAssignableFrom(String.class)) {
            return (T) value;
        }
        return createGson(DEFAULT_DATE_FORMAT).fromJson(value, clazz);
    }

    @Override
    public <T> List<T> fromJsonList(String value, Class<T> clazz) {
        if(value == null) {
            return null;
        }
        return createGson(DEFAULT_DATE_FORMAT).fromJson(value, new TypeToken<List<T>>(){}.getType());
    }
}
