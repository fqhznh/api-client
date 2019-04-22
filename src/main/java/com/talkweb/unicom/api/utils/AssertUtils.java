package com.talkweb.unicom.api.utils;


import com.talkweb.unicom.api.exception.ApiException;

public class AssertUtils {

    public static void notNull(Object value, String code, String msg) {
        if(value == null) {
            throw new ApiException(code, msg);
        }
        if(value instanceof String && ((String) value).length() == 0) {
            throw new ApiException(code, msg);
        }
    }

    public static void notNull(Object value, String msg) {
        notNull(value, "7001", msg);
    }

}
