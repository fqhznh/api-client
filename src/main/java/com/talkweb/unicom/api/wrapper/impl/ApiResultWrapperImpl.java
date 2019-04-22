package com.talkweb.unicom.api.wrapper.impl;

import com.talkweb.unicom.api.exception.ApiException;
import com.talkweb.unicom.api.utils.JsonUtils;
import com.talkweb.unicom.api.utils.StringMap;
import com.talkweb.unicom.api.wrapper.IResultWrapper;

import java.util.List;

public class ApiResultWrapperImpl implements IResultWrapper {

    @Override
    public <T> T wrapper(String data, Class<T> clazz) {
        return progressResult(data, clazz);
    }

    @Override
    public <T> List<T> wrapperList(String data, Class<T> clazz) {
        return progressResultWithList(data, clazz);
    }

    private String getData(String result) {
        if(result == null || result.length() == 0) {
            return null;
        }
        StringMap map = JsonUtils.fromJson(result, StringMap.class);
        String code = map.getString("code", "0");
        if("0".equals(code)) {
            return JsonUtils.toJson(map.get("data"));
        } else {
            throw new ApiException(code, map.getString("msg", "系统异常"));
        }
    }

    private <T> T progressResult(String result, Class<T> clazz) {
        String data = getData(result);
        if(data != null) {
            return JsonUtils.fromJson(data, clazz);
        }
        return null;
    }

    private <T> List<T> progressResultWithList(String result, Class<T> clazz) {
        String data = getData(result);
        if(data != null) {
            return JsonUtils.fromJsonList(data, clazz);
        }
        return null;
    }

}
