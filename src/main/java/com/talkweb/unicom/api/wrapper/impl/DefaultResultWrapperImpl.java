package com.talkweb.unicom.api.wrapper.impl;

import com.talkweb.unicom.api.utils.JsonUtils;
import com.talkweb.unicom.api.wrapper.IResultWrapper;

import java.util.List;

public class DefaultResultWrapperImpl implements IResultWrapper {
    @Override
    public <T> T wrapper(String data, Class<T> clazz) {
        if(data == null || data.length() == 0) {
            return null;
        }
        return JsonUtils.fromJson(data, clazz);
    }

    @Override
    public <T> List<T> wrapperList(String data, Class<T> clazz) {
        if(data == null || data.length() == 0) {
            return null;
        }
        return JsonUtils.fromJsonList(data, clazz);
    }
}
