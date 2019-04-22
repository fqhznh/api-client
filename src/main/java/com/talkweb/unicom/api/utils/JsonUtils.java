package com.talkweb.unicom.api.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * JSON工具类
 *
 * @author fqh
 * @create 2016-12-12 14:53
 */
public class JsonUtils {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";



    /**
     * 把对象变成json字符串。
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return toJson(obj, DEFAULT_DATE_FORMAT);
    }

    /**
     * 把对象变成json字符串
     * @param obj
     * @param dateFormat 字符串，默认为：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String toJson(Object obj, String dateFormat) {
        return JSON.toJSONStringWithDateFormat(obj, dateFormat);
    }

    public static String toPrettyFormatJson(Object obj, String dateFormat) {
        return JSON.toJSONStringWithDateFormat(obj, dateFormat, SerializerFeature.PrettyFormat);
    }

    public static String toPrettyFormatJson(Object obj) {
        return toPrettyFormatJson(obj, DEFAULT_DATE_FORMAT);
    }

    /**
     * 把json字符串转化为对象列表
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonList(String value, Class<T> clazz) {
        return JSON.parseArray(value, clazz);
    }

    /**
     * 把json字符串转化为对象列表
     * <p>
     *     可指定json路径，例如：json字符串为：{total: 2, rows: [{}, {}]}，则获取rows的路径为$.rows
     * </p>
     * @param value
     * @param path
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonList(String value, String path, Class<T> clazz) {
        if(path == null) {
            return fromJsonList(value, clazz);
        }
        Object pathValue = JSONPath.read(value, path);
        if(pathValue == null) {
            return null;
        }
        return fromJsonList(pathValue.toString(), clazz);
    }

    /**
     * 把json字符串转化为对象
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String value, Class<T> clazz) {
        return JSON.parseObject(value, clazz, SerializerFeature.WRITE_MAP_NULL_FEATURES);
    }

    public static <T> T fromJson(String value, TypeReference<T> type) {
        return JSON.parseObject(value, type);
    }

    /**
     * 把json字符串转化为对象
     * <p>
     *     可指定json路径，例如：json字符串为：{total: 2, rows: [{}, {}]}，则获取total的路径为$.total
     * </p>
     * @param value
     * @param path
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String value, String path, Class<T> clazz) {
        if(path == null) {
            return fromJson(value, clazz);
        }
        Object pathValue = JSONPath.read(value, path);
        if(pathValue == null) {
            return null;
        }
        return fromJson(pathValue.toString(), clazz);
    }


}
