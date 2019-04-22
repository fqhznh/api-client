package com.talkweb.unicom.api.json.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.talkweb.unicom.api.exception.ApiException;
import com.talkweb.unicom.api.json.IJsonHandler;

import java.text.SimpleDateFormat;
import java.util.List;

public class JacksonJsonHandler implements IJsonHandler {

    private static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.findAndRegisterModules();
        return mapper;
    }

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
        try {
            ObjectMapper mapper = getMapper();
            mapper.setDateFormat(new SimpleDateFormat(dateFormat));
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new ApiException("8000", e.getMessage(), e);
        }
    }

    @Override
    public <T> T fromJson(String value, Class<T> clazz) {
        if(value == null) {
            return null;
        }
        try {
            return getMapper().readValue(value, clazz);
        } catch (Exception e) {
            throw new ApiException("8000", e.getMessage(), e);
        }
    }

    @Override
    public <T> List<T> fromJsonList(String value, Class<T> clazz) {
        if(value == null) {
            return null;
        }
        try {
            return getMapper().readValue(value, new TypeReference<List<T>>() {});
        } catch (Exception e) {
            throw new ApiException("8000", e.getMessage(), e);
        }
    }

}
