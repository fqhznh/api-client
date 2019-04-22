package com.talkweb.unicom.api.utils;

import com.talkweb.unicom.api.json.IJsonHandler;
import com.talkweb.unicom.api.json.JsonHandlerFactory;

import java.util.List;

/**
 * JSON工具类
 *
 * @author fqh
 * @create 2016-12-12 14:53
 */
public class JsonUtils {

    private static IJsonHandler jsonHandler = new JsonHandlerFactory().create();

    public static void setJsonHandler(IJsonHandler jsonHandler) {
        JsonUtils.jsonHandler = jsonHandler;
    }

    /**
     * 把对象变成json字符串。
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        if(jsonHandler != null) {
            return jsonHandler.toJson(obj);
        }
        return null;
    }

    /**
     * 把对象变成json字符串
     * @param obj
     * @param dateFormat 字符串，默认为：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String toJson(Object obj, String dateFormat) {
        if(jsonHandler != null) {
            return jsonHandler.toJson(obj, dateFormat);
        }
        return null;
    }
    /**
     * 把json字符串转化为对象列表
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonList(String value, Class<T> clazz) {
        if(jsonHandler != null) {
            return jsonHandler.fromJsonList(value, clazz);
        }
        return null;
    }


    /**
     * 把json字符串转化为对象
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String value, Class<T> clazz) {
        if(jsonHandler != null) {
            return jsonHandler.fromJson(value, clazz);
        }
        return null;
    }


}
