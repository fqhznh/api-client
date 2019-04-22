package com.talkweb.unicom.api.json;

import java.util.List;

public interface IJsonHandler {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String toJson(Object obj);

    /**
     * 把对象转换为字符串
     * @param obj
     * @param dateFormat
     * @return
     */
    public String toJson(Object obj, String dateFormat);

    public <T> T fromJson(String value, Class<T> clazz);

    public <T> List<T> fromJsonList(String value, Class<T> clazz);

}
